from typing import Dict, List, Optional, Set
from fastapi import FastAPI, Body, Query
from fastapi.param_functions import Cookie
from pydantic import BaseModel
from uuid import UUID

app = FastAPI()

@app.get('/{_id}')
def hello(*, _id: Optional[int], a: Optional[int] = Query(..., title='测试', description='解释', ge=3)):
    return 'hello '  + _id

class ItemPost(BaseModel):
    name: str
    age: int = None
    uid: UUID
    sex: Optional[str] = '女'
    l1: List[str]
    l2: Set[str]
    c: Optional[str] = Cookie(None)

    class Config:
        scheme_extra = {
            'example': {
                'name': '123'
            }
        }

@app.post('/test_post', response_model=ItemPost)
async def test_post(item: ItemPost = None,
    importance: List[int] = Body(..., gt=0,), a: str = Cookie(None)):
    return item

@app.get("/keyword-weights/", response_model=Dict[str, float])
async def read_keyword_weights():
    return {"foo": 2.3, "bar": 3.4}