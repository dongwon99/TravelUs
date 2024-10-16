import os
import logging
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Dict, List, Optional, Union
import uvicorn
import threading
import schedule
import time
from datetime import datetime, timedelta
from exchangeRateForecast import run_prediction, get_latest_prediction, schedule_prediction, ALL_CURRENCIES
from exchangeRateForecastDetail import get_all_detailed_predictions

# GPU 설정
os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"
os.environ["CUDA_VISIBLE_DEVICES"] = "2"

# 로깅 설정
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

app = FastAPI()

# Pydantic 모델 정의
class ConfidenceInterval(BaseModel):
    lower: float
    upper: float

class PredictionCurrencyData(BaseModel):
    forecast: Dict[str, float]
    average_forecast: float
    confidence_interval: ConfidenceInterval
    # daily_changes: Dict[str, float]
    recent_rates: Dict[str, Dict[str, float]]

class PredictionResult(BaseModel):
    USD: PredictionCurrencyData
    JPY: PredictionCurrencyData
    EUR: PredictionCurrencyData
    TWD: PredictionCurrencyData
    last_updated: str

# Detail 페이지를 위한 새로운 클래스 선언
class ForecastStats(BaseModel):
    average: float
    min: float
    max: float
    p10: Optional[float] = None
    p20: Optional[float] = None
    p30: Optional[float] = None
    p40: Optional[float] = None
    p50: Optional[float] = None
    p60: Optional[float] = None
    p70: Optional[float] = None
    p80: Optional[float] = None
    p90: Optional[float] = None
    p100: Optional[float] = None

class DetailedPrediction(BaseModel):
    forecast: Dict[str, float]
    current_rate: float
    trend: str
    forecast_stats: ForecastStats

class AllDetailedPredictions(BaseModel):
    USD: DetailedPrediction
    JPY: DetailedPrediction
    EUR: DetailedPrediction
    TWD: DetailedPrediction
    last_updated: str

# 전역 변수로 예측 결과를 저장
cached_prediction = None
cached_detailed_prediction = None
prediction_lock = threading.Lock()

def update_predictions():
    global cached_prediction, cached_detailed_prediction
    try:
        with prediction_lock:
            cached_prediction = run_prediction()
            cached_detailed_prediction = get_all_detailed_predictions()
        logger.info("Predictions updated successfully")
    except Exception as e:
        logger.error(f"Error updating predictions: {str(e)}")

@app.get("/prediction/", response_model=PredictionResult)
async def get_prediction():
    global cached_prediction
    with prediction_lock:
        if cached_prediction is None:
            update_predictions()
        if cached_prediction is None:
            raise HTTPException(status_code=500, detail="Failed to calculate predictions")
    
    try:
        formatted_result = {}
        for currency in ALL_CURRENCIES:
            formatted_result[currency] = PredictionCurrencyData(
                forecast=cached_prediction[currency]["forecast"],
                average_forecast=cached_prediction[currency]["average_forecast"],
                confidence_interval=ConfidenceInterval(**cached_prediction[currency]["confidence_interval"]),
                recent_rates=cached_prediction[currency]["recent_rates"]
            )
        formatted_result["last_updated"] = cached_prediction["last_updated"]
        
        return PredictionResult(**formatted_result)
    except Exception as e:
        logger.error(f"Error in get_prediction: {str(e)}")
        raise HTTPException(status_code=500, detail="Internal server error")

@app.get("/recent-rates/{currency}")
async def get_recent_rates(currency: str):
    global cached_prediction
    if currency not in ALL_CURRENCIES:
        raise HTTPException(status_code=400, detail="Invalid currency")
    
    with prediction_lock:
        if cached_prediction is None:
            update_predictions()
        if cached_prediction is None:
            raise HTTPException(status_code=500, detail="Failed to calculate predictions")
    
    try:
        if currency not in cached_prediction or 'recent_rates' not in cached_prediction[currency]:
            raise HTTPException(status_code=404, detail="Recent rates not found")
        return {"recent_rates": cached_prediction[currency]['recent_rates']}
    except Exception as e:
        logger.error(f"Error in get_recent_rates: {str(e)}")
        raise HTTPException(status_code=500, detail="Internal server error")

@app.get("/prediction/detail/", response_model=AllDetailedPredictions)
async def get_all_detailed_predictions_route():
    global cached_detailed_prediction
    with prediction_lock:
        if cached_detailed_prediction is None:
            update_predictions()
        if cached_detailed_prediction is None:
            raise HTTPException(status_code=500, detail="Failed to calculate detailed predictions")
    
    try:
        formatted_result = {}
        for currency in ALL_CURRENCIES:
            prediction = cached_detailed_prediction[currency]
            forecast_stats = prediction["forecast_stats"]
            formatted_result[currency] = DetailedPrediction(
                forecast=prediction["forecast"],
                current_rate=prediction["current_rate"],
                trend=prediction["trend"],
                forecast_stats=ForecastStats(
                    average=forecast_stats["average"],
                    min=forecast_stats["min"],
                    max=forecast_stats["max"],
                    **{f"p{i*10}": forecast_stats.get(f"{i*10}%") for i in range(1, 11)}
                )
            )
        formatted_result["last_updated"] = cached_detailed_prediction["last_updated"]
        
        return AllDetailedPredictions(**formatted_result)
    except Exception as e:
        logger.error(f"Error in get_all_detailed_predictions_route: {str(e)}")
        raise HTTPException(status_code=500, detail="Internal server error")

@app.get("/run-prediction/")
async def trigger_prediction():
    try:
        update_predictions()
        return {"message": "Prediction process completed successfully"}
    except Exception as e:
        logger.error(f"Error in trigger_prediction: {str(e)}")
        raise HTTPException(status_code=500, detail="Failed to trigger prediction")

@app.get("/memory-state/")
async def get_memory_state():
    try:
        state = "Predictions are cached" if cached_prediction is not None else "No predictions cached"
        return {"message": "Memory state retrieved", "state": state}
    except Exception as e:
        logger.error(f"Error in get_memory_state: {str(e)}")
        raise HTTPException(status_code=500, detail="Failed to get memory state")

def run_scheduler():
    while True:
        try:
            schedule.run_pending()
            time.sleep(60)
        except Exception as e:
            logger.error(f"Error in scheduler: {str(e)}")

if __name__ == "__main__":
    try:
        # 초기 예측 실행
        update_predictions()
        
        # 매일 00:00에 예측 실행 스케줄링
        schedule.every().day.at("00:00").do(update_predictions)
        
        # 스케줄러를 별도의 스레드에서 실행
        scheduler_thread = threading.Thread(target=run_scheduler, daemon=True)
        scheduler_thread.start()

        # FastAPI 서버 실행
        uvicorn.run(
            app, 
            host=os.getenv("HOST", "70.12.130.121"), 
            port=int(os.getenv("PORT", 11209)),
        )
    except Exception as e:
        logger.error(f"Error in main execution: {str(e)}")