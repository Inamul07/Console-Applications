from pydantic import BaseModel

class User(BaseModel):
    user_name: str
    password: str

    class Config:
        schema_extra = {
            "example": {
                "user_name": "name",
                "password": "vkdvbkjdbv",
            }
        }