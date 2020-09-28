from PIL import Image
import numpy as np
import requests
import os
import sys
import json

#Local Image Directories
u_directory = "C:\\Users\\drewm\\Desktop\\Vew\\Art\\AllArt\\Uncomp\\"
c_directory = "C:\\Users\\drewm\\Desktop\\Vew\\Art\\AllArt\\Comp\\"

#Login as Admin to Upload Images
u = input("Please provide an administrator username: ")
p = input("Password: ")

#Check what the last index of uploaded images is
resp = requests.get("http://34.72.6.126/loadimages/")
populated_list = resp.json()
uploaded_images = []
current_index = 0

for x in populated_list["data"]:
    uploaded_images.append(x["title"])

if uploaded_images!=[]:
    current_index = int(populated_list["data"][0]["id"])
    
else:
    current_index = 0

for x in populated_list["data"]:
    uploaded_images.append(x["title"])

def upload_directory():
    global u
    global p
    i = 1
    for filename in os.listdir(u_directory):
        if filename.endswith(".jpg") and filename not in uploaded_images:
            #Rename files for storage
            new_filename = "image"+str(current_index+i)+".jpg"
            os.rename(u_directory+filename,u_directory+new_filename)  

            #Compress image locally
            input_jpeg = Image.open(u_directory+new_filename)
            input_jpeg_res = input_jpeg.size
            optimized_jpeg = input_jpeg.resize((int(input_jpeg_res[0]/3.5),int(input_jpeg_res[1]/3.5)),
            Image.ANTIALIAS)
            optimized_jpeg.save(c_directory+new_filename)

            #Post newly uploaded images to the server
            u_url = "https://raw.githubusercontent.com/drooo2/vewart/master/AllArt/Uncomp/" + new_filename
            c_url = "https://raw.githubusercontent.com/drooo2/vewart/master/AllArt/Comp/" + new_filename
            r_res = int((input_jpeg_res[0]*input_jpeg_res[1]))
            jj = json.dumps({
                "username":u,
                "password":p,
                "u_url": u_url,
                "c_url":c_url,
                "title":new_filename,
                "contributor":"",
                "resolution":r_res
            })
            r = requests.post("http://34.72.6.126/upload/", data=jj)
            i+=1
loginjson = json.dumps({"username":u,"password":p})
g = requests.post("http://34.72.6.126/login/",data=loginjson)
a = g.json()
if a["data"]=="True":
    upload_directory()
else:
    print("Wrong Username and Password Combination Entered")
         