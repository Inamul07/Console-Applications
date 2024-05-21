import requests

DOMAIN = "http://localhost:8000/"

def getDetails():
    print("Enter Username: ", end="")
    username = input()
    print("Enter password: ", end="")
    password = input()
    return {"user_name": username, "password": password}

def login():
    credentials = getDetails()
    response = requests.post(DOMAIN + "authenticate-user", json=credentials)
    response = response.json()
    if response["authenticated"]:
        return True
    print(response["message"])
    return False

def signup():
    credentials = getDetails()
    response = requests.post(DOMAIN + "create-user", json=credentials)
    response = response.json()
    if response["created"]:
        return True
    print(response["message"])
    return False

print("1. Register\n2. Login\nEnter Your Choice (1/2):", end="")
choice = int(input())
if choice == 1:
    if signup():
        print("User Created Successfully")
elif choice == 2:
    if login():
        print("Login Successful")
else:
    print("Invalid Choice")