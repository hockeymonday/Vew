from db import *

"""This module interfaces between modules app and db to retrieve 
or modify information stored in the database tables.
"""
#Global Variables

#Dictionary Containing WallPaper Categories
IMGSET = {"all":AllImages}


#Database interaction methods
def get_image(id,category="all"):
    """This image gets the url and metadata for a single image in the database and returns it as
    a dictionary.

    Parameter id: The unique id corresponding to an image in the list.
    Precondition: id is an int."""
    imagedata = IMGSET[category].query.filter_by(id=id).first()
    if imagedata is None:
        return None
    else:
        cd = imagedata.serialize_images()
        return cd

def get_image_table(category="all"):
    """Gets the full image table"""
    x = []
    for img in IMGSET[category].query.all():
        f = get_image(img.id)
        x.insert(0,f)
    return (x)

def image_deleter(id,category="all"):
    """Deletes an existing image from the system.
    
    Parameter id: The id of the image to remove
    Precondition: id is an int. """
    deleted_img = get_image(id)
    c = IMGSET[category].query.filter_by(id=id).first()
    if deleted_img == None:
        return None
    else:
        db.session.delete(c)
        db.session.commit()
        return deleted_img

def upload_image(u_url,c_url,title,cont,res,category="all"):
    """Uploads a new image to the database

    Parameters u_url, c_url: The uncompressed and compressed urls of the uploaded image
    Precondition: u_url, c_url are strings not None.

    Parameter title: The title of the image if the uploader wants to include one.
    Precondition: string, possibly None
    
    Parameter cont: The name of a contributor; e.g: artist or photographer.
    Precondtion: string, possibly None

    Parameter res: Uncompressed image resolution;
    Precondtion: res is an int that is not null
    """
    image_data = IMGSET[category](uncomp = u_url, comp = c_url, title = title, contributor = cont, resolution = res)
    db.session.add(image_data)
    db.session.commit()
    return image_data.serialize_images()

def register(user,pass_hash):
    """Adds an administrator to the system.

    Parameter user: The new administrator's username
    Precondition: String not null

    Parameter pass_hash: The new administrator's hashed password
    Precondition: String not null"""
    if get_login(user) is None:
        new_user = Password(user = user, pass_hash = pass_hash)
        db.session.add(new_user)
        db.session.commit() 
        return True
    else:
        return False

def get_login(user):
    """This method retrieves a user's password hash to compare it to the one provided at login.

    Parameter user: the user's username
    Precondition: user is a string, not None"""
    person_details = Password.query.filter_by(user = user).first()
    if person_details is None:
        return None
    else:
        return person_details.get_hash()

def reset_password(user, new_pass):
    """This method updates an existing user's password to a new one
    Parameter user: the user's username
    Precondition: user is a string, not None

    Parameter new_pass: the new password provided at password change
    Precondition: new_pass is a string, not None"""
    person_details = Password.query.filter_by(user = user).first()
    if person_details is None:
        return 0
    elif new_pass == person_details.pass_hash:
        return 1
    else:
        person_details.pass_hash = new_pass
        db.session.add(person_details)
        db.session.commit()
        return 2

def login(user,password):
    """This method verifies remote login attempts
    Parameter user: The user's username
    Precondition: String not null

    Parameter password: The user's password
    Precondition: String not null
    """
    
def num_users():
    return len(Password.query.all())



