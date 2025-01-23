from fastapi import FastAPI, HTTPException
import sys
import os

sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from services import create_faiss_index, query_faiss_index, query_llama_model, initialize_database, get_cached_answer
from request_models import *
import prediction_services as pds

app = FastAPI()

initialize_database()


@app.post("/query")
def query_endpoint(request: QueryRequest):
    try:
        # Check the cache first
        cached_answer = get_cached_answer(request.query_text)
        if cached_answer:
            return cached_answer

        # Query the FAISS index for context if not cached
        context_text = query_faiss_index(request.query_text)

        # Query the Llama model using the retrieved context
        response = query_llama_model(request.query_text, context_text)
        return response
    except FileNotFoundError:
        raise HTTPException(status_code=404, detail="FAISS index not found. Please create the index first.")
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Unexpected error: {str(e)}")


@app.post("/stats")
def statistics_endpoint(request: StatisticsRequest):
    prediction = pds.PredictionService()
    statis_request = StatisticsRequest(
        min_players=request.min_players,
        max_players=request.max_players,
        play_time=request.play_time,
        min_age=request.min_age,
        users_rated=request.users_rated,
        domains=request.domains,
        mechanics=request.mechanics,
    ).dict()
    average = prediction.predict_average_rating(data=statis_request)
    complexity = prediction.predict_complexity_average(data=statis_request)
    owned = prediction.predict_owned_users(data=statis_request)
    return StatisticsResponse(average_rating=average, complexity_rating=complexity, owned_users=owned)


@app.post("/create-index")
async def create_index():
    try:
        create_faiss_index()
        return {"message": "FAISS index created successfully."}
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))
