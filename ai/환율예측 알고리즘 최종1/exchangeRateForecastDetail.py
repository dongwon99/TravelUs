import yfinance as yf
import pandas as pd
import numpy as np
from statsmodels.tsa.statespace.sarimax import SARIMAX
from pmdarima import auto_arima
from scipy import stats
from datetime import datetime, timedelta
import requests
import logging

# 로깅 설정
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

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
    
    data = pd.DataFrame()
    for name, ticker in tickers.items():
        ticker_data = yf.Ticker(ticker).history(start=start_date, end=end_date)
        if not ticker_data.empty:
            if name == "exchange_rate":
                data[name] = (ticker_data['High'] + ticker_data['Low']) / 2
            else:
                data[name] = ticker_data['Close']
    
    return data

# frequency가 B(영업일)인 데이터 받아오기
def prepare_data(data):
    data = data.asfreq('B')
    data = data.interpolate()
    data['exchange_rate'] = np.log(data['exchange_rate'])
    return data

# 2주 간의 환율 그래프가 어떻게 될 것인지 예측
def get_two_week_trend(data):
    end_date = data.index[-1]
    start_date = end_date - timedelta(days=14)
    two_week_data = data.loc[start_date:end_date, 'exchange_rate']
    
    x = np.arange(len(two_week_data))
    y = two_week_data.values
    slope, _, _, _, _ = stats.linregress(x, y)
    
    if slope > 0:
        return "upward"
    elif slope < 0:
        return "downward"
    else:
        return "stable"

# auto_arima를 통해 학습
def train_sarimax_model(data):
    exog = data.drop('exchange_rate', axis=1)
    model = auto_arima(data['exchange_rate'], exogenous=exog,
                       seasonal=True, m=5, 
                       start_p=0, start_q=0, max_p=5, max_q=5,
                       start_P=0, start_Q=0, max_P=2, max_Q=2,
                       d=1, D=1, trace=False, error_action='ignore',
                       suppress_warnings=True, stepwise=True)
    return model

# 환율 예측값 세부화를 위한 예측 진행
def forecast_exchange_rate(model, steps, exog_future):
    forecast = model.predict(n_periods=steps, X=exog_future)
    return np.exp(forecast)

# 데이터 저장하기
def get_exchange_rate_prediction(currency):
    # 실시간 환율 받아오기
    live_rate_data = yf.Ticker(f'{currency}KRW=X').history(period="1d")
    if live_rate_data.empty:
        logger.error(f"실시간 환율 데이터를 가져오는 데 실패했습니다: {currency}KRW=X")
        return None
    
    # 실시간 환율의 종가 가져오기
    current_rate = (live_rate_data['High'].iloc[-1] + live_rate_data['Low'].iloc[-1]) / 2
    
    # 데이터 준비
    end_date = datetime.now()
    # start_date = end_date - timedelta(days=365*5)  # 5년치 데이터
    start_date = "2019-01-01"
    data = get_data_with_exog(currency, start_date, end_date)
    prepared_data = prepare_data(data)
    
    # 현재 환율
    # current_rate = np.exp(prepared_data['exchange_rate'].iloc[-1])
    
    # 2주 추세 분석
    two_week_trend = get_two_week_trend(prepared_data)
    
    # SARIMAX 모델 훈련
    model = train_sarimax_model(prepared_data)
    
    # 2주(10 영업일) 예측을 위한 외생변수 준비
    last_date = prepared_data.index[-1]
    last_exog = prepared_data.loc[last_date, prepared_data.columns != 'exchange_rate']
    exog_future = pd.DataFrame([last_exog] * 14, index=pd.date_range(start=prepared_data.index[-1] + timedelta(days=1), periods=14, freq='B'))
    
    # 예측 수행
    forecast = forecast_exchange_rate(model, steps=14, exog_future=exog_future)
    forecast_dates = pd.date_range(start=prepared_data.index[-1] + timedelta(days=1), periods=14, freq='B')
    forecast = pd.Series(forecast, index=forecast_dates)
    
    # 예측값 통계 계산
    forecast_stats = {
        "average": forecast.mean(),
        "min": forecast.min(),
        "max": forecast.max(),
    }
    
    # 백분위수 계산
    min_val = forecast_stats["min"]
    max_val = forecast_stats["max"]
    range_val = max_val - min_val
    
    for i in range(1, 11):
        percentile = i * 10
        percentile_value = min_val + (range_val * (percentile / 100))
        forecast_stats[f"{percentile}%"] = percentile_value
    
    # 결과 딕셔너리 생성
    result = {
        # "currency": currency,
        "forecast": {date.strftime('%Y-%m-%d'): rate for date, rate in forecast.items()},
        "current_rate": current_rate,
        "trend": two_week_trend,
        "forecast_stats": forecast_stats
    }
    
    return result

# detail 받을 통화 선택
def get_all_detailed_predictions():
    currencies = ['USD', 'JPY', 'EUR', 'TWD']
    results = {}
    for currency in currencies:
        print(f"{currency} 처리 중...")
        results[currency] = get_exchange_rate_prediction(currency)
    
    results["last_updated"] = datetime.now().isoformat()
    return results

if __name__ == "__main__":
    # 테스트 실행
    all_predictions = get_all_detailed_predictions()
    print(json.dumps(all_predictions, indent=2))