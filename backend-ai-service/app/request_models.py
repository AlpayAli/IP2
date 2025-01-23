from typing import List

from pydantic import BaseModel


class QueryRequest(BaseModel):
    query_text: str


class StatisticsRequest(BaseModel):
    min_players: int
    max_players: int
    play_time: int
    min_age: int
    users_rated: int
    domains: List[str]
    mechanics: List[str]


class StatisticsResponse(BaseModel):
    average_rating: float
    complexity_rating: float
    owned_users: float
