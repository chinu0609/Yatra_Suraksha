from fastapi import FastAPI, Request
from pydantic import BaseModel
from typing import List

app = FastAPI()

class Message(BaseModel):
    text: str
    sender: str

chat_history = []

@app.post("/chat/")
async def chat(message: Message):
    chat_history.append(message)
    # Here you can implement your chatbot logic
    response = f"Echo: {message.text}"
    return {"response": response}

@app.get("/chat/history/", response_model=List[Message])
async def chat_history_endpoint():
    return chat_history
