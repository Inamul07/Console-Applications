import os
from motor import motor_asyncio

client = motor_asyncio.AsyncIOMotorClient("mongodb://rootuser:rootpass@localhost:27017/")
db = client.password_auth
userCollection = db.users
passwordCollection = db.password_dump

async def uploadData(fileName, vulnerabilityType):
    try:
        with open(file=fileName, mode='r') as file:
            passwords = file.readlines()
            for password in passwords:
                password = password.strip()
                if password:
                    await passwordCollection.insert_one({"password": password, "type": vulnerabilityType})
        return True

    except:
        return False
    
async def passwordDumpAvailable():
    collections = await db.list_collection_names()
    if "password_dump" in collections:
        return True
    return False

async def uploadPasswordDump():
    if await passwordDumpAvailable():
        return {"uploaded": False, "message": "Passwords alreaady available"}
    
    absolutePath = os.path.dirname(__file__) + "/../"
    weakStatus = await uploadData(absolutePath + "./assets/weakpasswords.txt", "weak") 
    breachedStatus = await uploadData(absolutePath + "./assets/breachedpasswords.txt", "breached")

    return {"uploaded": weakStatus and breachedStatus, "message": "Weak and Breached passwords uploaded successfully"}

async def create(data):
    data = dict(data)
    try:
        await userCollection.insert_one(data)
        return True
    except:
        return False
    
async def checkPasswordVulnerability(password):
    if not await passwordDumpAvailable():
        await uploadPasswordDump()

    passwordResult = await passwordCollection.find_one({"password": password})

    if passwordResult:
        return {"is_vulnerable": True, "vulnerability_type": passwordResult["type"]}
    
    return {"is_vulnerable": False}

async def userExists(userName):
    result = await userCollection.find_one({"user_name": userName})

    if result:
        return True
    return False

async def authenticateUser(data):
    data = dict(data)
    result = await userCollection.find_one({"user_name": data["user_name"]})

    if result:
        if data["password"] == result["password"]:
            return {"authenticated": True, "message": "Authentication Successful"}
        else:
            return {"authenticated": False, "message": "Incorrect Password"}
        
    return {"authenticated": False, "message": "No User Found! Please Check You Username."}