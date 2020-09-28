import json
from flask import Flask, request
from db import db
import dao
import hashlib, binascii, os
import password_checker

"""This module contains all routes and methods for the wallpaper app."""

#Define db filename
app = Flask(__name__)
db_filename = "images.db"

#Setup config
app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///%s" % db_filename
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
app.config["SQLALCHEMY_ECHO"] = True

#Initialize app
db.init_app(app)
with app.app_context():
    db.create_all()

#Global Vars
NUM_ADMINS = 2

#Helper methods
def success_response(data, code=200):
    """Returns a generic failure response with a specific error message
    as a json."""
    return json.dumps({"success": True, "data": data}), code

def failure_response(message, code=404):
    """Returns a generic failure response with a specific error message
    as a json."""
    return json.dumps({"success": False, "error": message}), code

def ju(jason):
    """Unpacks jsons"""
    return json.loads(jason)

#Routes
@app.route('/loadimages/', methods = ['GET'])
def wallpaper_list():
    return success_response(dao.get_image_table())

@app.route('/loadimages/<int:id>', methods = ['GET'])
def get_image(id):
    data = dao.get_image(id)
    if data != None:
        return success_response(data)
    else:
        return failure_response("Image does not exist")

@app.route('/upload/', methods = ['POST'])
def upload_image():
    query = ju(request.data)
    un = query["username"]
    pw = query["password"]
    if not password_checker.pass_check(un,pw):
        return failure_response("Invalid username or password")
    uc = query["u_url"]
    c = query["c_url"]
    title = query["title"]
    cont = query["contributor"]
    res = query["resolution"]
    try:
        assert type (uc) == str and uc is not None
        assert type (c) == str and c is not None
        assert type (title) == str or title is None
        assert type (cont) == str or cont is None
        assert type(res)==int and res is not None
    except:
        return failure_response("Invalid image or metadata")
    data = dao.upload_image(uc,c,title,cont,res)
    return success_response(data)

@app.route('/delete/', methods = ['DELETE'])
def delete_image():
    query = ju(request.data)
    id = query["id"]
    un = query["username"]
    pw = query["password"]
    if not password_checker.pass_check(un,pw):
        return failure_response("Invalid username or password")
    if dao.image_deleter(id) == None:
        return failure_response("Image does not exist or has already been deleted")
    else:
        return success_response("Image deleted")

@app.route('/register/', methods = ['POST'])
def register():
    global NUM_ADMINS
    query = ju(request.data)
    try:
        user_name = query["username"]
        pass_word_hashed = password_checker.hash_password(query["password"])
    except:
        return failure_response("Invalid username or password entered")
    if dao.num_users() >= NUM_ADMINS:
        return failure_response("Cannot register new administrator account")
    elif dao.register(user_name,pass_word_hashed):
        return success_response("Your account has been created")
    else:
        return failure_response("That username is already taken")

@app.route('/login/', methods = ['POST'])
def login():
    """Method that forces client to enter the correct password to prevent unncessary remote changes"""
    try:
        query = ju(request.data)
        un = query["username"]
        pw = query["password"]
        if password_checker.pass_check(un,pw):
            return success_response("True")
    except:
        return failure_response("Invalid query")

@app.route('/password_change/', methods = ['POST'])
def change_password():
    try:
        query = ju(request.data)
        un = query["username"]
        pw = query["old_password"]
        npw = password_checker.hash_password(query["new_password"])
    except:
        return failure_response("Invalid query")
    if password_checker.pass_check(un,pw):
        a = dao.reset_password(un,npw)
        if a == 0:
            return failure_response("User does not exist")
        elif a == 1:
            return failure_response("New password cannot be the old password")
        elif a == 2:
            return success_response("Your password has been changed")

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
