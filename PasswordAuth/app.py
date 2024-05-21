from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from database import db
from models.userModel import User

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
def root():
    return {"Message": "Hello World!!!"}

@app.get("/upload-password-dump")
async def uploadPasswordDump():
    result = await db.uploadPasswordDump()
    return result

@app.post("/create-user")
async def createUser(user: User):

    userExists = await db.userExists(user.user_name)

    if userExists:
        return {"created": False, "message": "User already exists."}

    vulnerabilityResult = await db.checkPasswordVulnerability(user.password)

    if vulnerabilityResult["is_vulnerable"]:
        return {"created": False, "message": "The entered password is " + vulnerabilityResult["vulnerability_type"]}
    
    result = await db.create(user)
    return {"created": result, "message": "User Created Successfully"}

@app.post("/authenticate-user")
async def authenticateUser(user: User):
    result = await db.authenticateUser(user)
    return result