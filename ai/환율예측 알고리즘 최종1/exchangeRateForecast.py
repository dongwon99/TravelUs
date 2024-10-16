import os
import logging
from threading import Lock
from datetime import datetime, timedelta
import yfinance as yf
import pandas as pd
import numpy as np
from statsmodels.tsa.statespace.sarimax import SARIMAX
from pmdarima import auto_arima
import schedule
import time
import json
import requests

from exchangeRateForecastDetail import get_all_detailed_predictions

# GPU 설정
os.environ["CUDA_DEVICE_ORDER"]="PCI_BUS_ID"
os.environ["CUDA_VISIBLE_DEVICES"]="2"

# 로깅 설정
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

# 글로벌 변수와 락
global_prediction_result = None
prediction_lock = Lock()

# 통화 리스트 정의
ALL_CURRENCIES = ['USD', 'JPY', 'EUR', 'TWD']

# 사용할 yfinance 데이터 불러오기
def get_data_with_exog(currency, start_date, end_date):
    tickers = {
        "exchange_rate": f"{currency}KRW=X",
        "us_treasury": "^TNX",
        "sp500": "^GSPC",
        "wti_oil": "CL=F",
        "gold": "GC=F",
        "usd_etf": "UUP",
        "kospi": "^KS11",
        "nasdaq": "^IXIC",
        "nikkei": "^N225",
        "dax": "^GDAXI",
        "taiex": "EWT",
    }
    
    all_data = {}
    for name, ticker in tickers.items():
        ticker_data = yf.Ticker(ticker).history(start=start_date, end=end_date)
        # ticker가 exchange_rate라면 (High+Low)/2 값을, 다른 외생변수들은 종가 값 가져오기
        if not ticker_data.empty:
            if name == "exchange_rate":
                all_data[name] = (ticker_data['High'] + ticker_data['Low']) / 2
            else:
                all_data[name] = ticker_data['Close']
            
            all_data[name].index = all_data[name].index.date

    # pandas의 데이터 프레임 형태로 변경
    data = pd.DataFrame(all_data)
    data = data.reset_index().rename(columns={'index': 'Date'})
    data = data.set_index('Date').sort_index()
    
    return data

# inf, NaN 등 결측치를 제거
def handle_missing_data(data):
    data = data.replace([np.inf, -np.inf], np.nan)
    data = data.interpolate().ffill().bfill()
    return data

# ARIMA 모델의 auto_arima에 계절성을 추가하여 모델 학습
def train_model(train_data, exog_columns):
    try:
        exog = train_data[exog_columns]
        
        auto_model = auto_arima(train_data['exchange_rate'], exogenous=exog,
                                seasonal=True, m=5, 
                                start_p=0, start_q=0, max_p=5, max_q=5,
                                start_P=0, start_Q=0, max_P=2, max_Q=2,
                                d=1, D=1, trace=True, error_action='ignore',
                                suppress_warnings=True, stepwise=True,
                                )
        return auto_model.order, auto_model.seasonal_order
    except Exception as e:
        logger.error(f"Error in train_model: {str(e)}")
        raise

# 2주 환율 예측(외생변수를 포함한 SARIMAX) 사용
def forecast_next_2_weeks(train, order, seasonal_order, exog_columns):
    try:
        train_exog = train[exog_columns]
        
        model = SARIMAX(train['exchange_rate'], exog=train_exog,
                        order=order, seasonal_order=seasonal_order)
        model_fit = model.fit(disp=False, maxiter=1000)
        
        last_date = train.index[-1]
        future_dates = pd.date_range(start=last_date + timedelta(days=1), periods=14, freq='B')
        future_exog = pd.DataFrame(index=future_dates, columns=exog_columns)
        
        for col in exog_columns:
            future_exog[col] = train_exog[col].iloc[-1]
        
        forecast = model_fit.forecast(steps=14, exog=future_exog)
        return np.exp(forecast), future_dates
    except Exception as e:
        logger.error(f"Error in forecast_next_2_weeks: {str(e)}")
        raise

# 최근 환율 받아오기
def get_recent_exchange_rates(currency):
    try:
        ticker = f"{currency}KRW=X"
        exchange_rate = yf.Ticker(ticker)
        end_date = datetime.now()
        start_date_3m = end_date - timedelta(days=90)
        data_3m = exchange_rate.history(start=start_date_3m, end=end_date)
        sorted_data = data_3m.sort_index(ascending=False)
        return {
            "3_months": {k.strftime('%Y-%m-%d'): float(v) for k, v in sorted_data['Close'].items()}
        }
    except Exception as e:
        logger.error(f"Error in get_recent_exchange_rates for {currency}: {str(e)}")
        raise

# 예측 실행(2019.01.01 ~ 오늘까지의 데이터를 학습 후 예측 진행)
def run_prediction():
    global global_prediction_result
    try:
        logger.info("Starting prediction process")
        results = {}
        start_date = "2019-01-01"
        end_date = datetime.now().strftime('%Y-%m-%d')

        for currency in ALL_CURRENCIES:
            data = get_data_with_exog(currency, start_date, end_date)
            data = handle_missing_data(data)
            
            exog_columns = [col for col in data.columns if col != 'exchange_rate']
            data['exchange_rate'] = np.log(data['exchange_rate'])

            order, seasonal_order = train_model(data, exog_columns)
            forecast, forecast_dates = forecast_next_2_weeks(data, order, seasonal_order, exog_columns)
            
            recent_rates = get_recent_exchange_rates(currency)

            results[currency] = {
                "forecast": {k.strftime('%Y-%m-%d'): float(v) for k, v in zip(forecast_dates, forecast)},
                "average_forecast": float(forecast.mean()),
                "confidence_interval": {
                    "lower": float(forecast.mean() - 1.96 * forecast.std()),
                    "upper": float(forecast.mean() + 1.96 * forecast.std())
                },
                "recent_rates": recent_rates
            }
        
        results["last_updated"] = datetime.now().isoformat()

        logger.info("Prediction results updated in memory")
        logger.info(f"Prediction Results:\n{json.dumps(results, indent=2)}")
        return results
    
    except Exception as e:
        logger.error(f"An error occurred during prediction: {str(e)}", exc_info=True)

# 최근 예측 결과 받아오기
def get_latest_prediction():
    global global_prediction_result
    with prediction_lock:
        if global_prediction_result is None:
            logger.info("No prediction results in memory. Running prediction...")
            global_prediction_result = run_prediction()
        else:
            logger.info("Returning existing prediction results from memory")
        return global_prediction_result

# 결과 백엔드 /exchange/forecast/update/history에 put 요청 보내기
def put_exchange_rate_history(results):
    url = "https://j11d209.p.ssafy.io/api/v2/exchange/forecast/update/history"
    headers = {'Content-Type': 'application/json'}
    
    try:
        # response = requests.put(url, json=results, headers=headers)
        # response.raise_for_status()
        # logger.info(f"History data updated successfully. Status code: {response.status_code}")

        logger.info("PUT request to update history is currently commented out.")
        return True
    except requests.exceptions.HTTPError as http_err:
        logger.error(f"HTTP error occurred while updating history: {http_err}")
    except Exception as err:
        logger.error(f"An error occurred while updating history: {err}")
    return False

# 예측 세부 결과를 백엔드 /exchange/forecast/update에 post 요청 보내기
def post_exchange_rate_update(results):
    url = "https://j11d209.p.ssafy.io/api/v2/exchange/forecast/update"
    headers = {'Content-Type': 'application/json'}
    
    try:
        # response = requests.post(url, json=results, headers=headers)
        # response.raise_for_status()
        # logger.info(f"Forecast data posted successfully. Status code: {response.status_code}")

        logger.info("POST request to update history is currently commented out.")
        return True
    except requests.exceptions.HTTPError as http_err:
        logger.error(f"HTTP error occurred while posting forecast: {http_err}")
    except Exception as err:
        logger.error(f"An error occurred while posting forecast: {err}")
    return False

# 스케줄러 설정
def schedule_prediction():
    global global_prediction_result
    logger.info("Scheduled prediction running")
    global_prediction_result = get_all_detailed_predictions()
    
    if global_prediction_result:
        # 먼저 history 업데이트
        history_success = put_exchange_rate_history(global_prediction_result)
        if history_success:
            logger.info("History data updated successfully")
        else:
            logger.warning("Failed to update history data")
        
        # 그 다음 forecast 업데이트
        forecast_success = post_exchange_rate_update(global_prediction_result)
        if forecast_success:
            logger.info("Forecast data posted successfully")
        else:
            logger.warning("Failed to post forecast data")
    else:
        logger.error("Prediction failed, no data to update or post")

    logger.info("Prediction completed and stored in memory")

if __name__ == "__main__":
    # 초기 예측 실행
    global_prediction_result = get_all_detailed_predictions()
    if global_prediction_result:
        # 먼저 history 업데이트
        history_success = put_exchange_rate_history(global_prediction_result)
        if history_success:
            logger.info("Initial history data updated successfully")
        else:
            logger.warning("Failed to update initial history data")
        
        # 그 다음 forecast 업데이트
        forecast_success = post_exchange_rate_update(global_prediction_result)
        if forecast_success:
            logger.info("Initial forecast data posted successfully")
        else:
            logger.warning("Failed to post initial forecast data")
    else:
        logger.error("Initial prediction failed, no data to update or post")
            
    logger.info("Initial prediction completed and stored in memory")
    
    # 매일 02:00에 예측 실행 스케줄링
    schedule.every().day.at("02:00").do(schedule_prediction)
    
    # 매 시간마다 현재 메모리 상태 출력
    schedule.every().hour.do(print_current_memory_state)
    
    # 스케줄러 실행
    while True:
        schedule.run_pending()
        time.sleep(60)  # 1분마다 스케줄 체크