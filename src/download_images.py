import os
import requests
import shutil
import json

"""This module downloads 5 random open source images to be compressed and stored in the application's database"""

#Get jsons for 10 random images from Unsplash
get_image_list = requests.get("https://api.unsplash.com/photos/random?client_id=o2aj9cozxaJPq_Q08ypYzZOGXdNO8KdeFoyPxkL-iYg&count=10&orientation=portrait&query=landscape").json()

#Local uncompressed image directory
u_dir = "C:\\Users\\drewm\\Desktop\\Vew\\Art\\AllArt\\Uncomp\\"

#Download the images for local storage
i = 0
for pic in get_image_list:
    full_url = pic["urls"]["full"]
    resp = requests.get(full_url, stream=True)
    
    #Create local placeholder file
    if pic["alt_description"]!= None:
        local_file = open(str(u_dir+(pic["alt_description"]))+".jpg", "wb")
    elif pic["description"]!=None:
        local_file = open(str(u_dir+(pic["description"]))+".jpg", "wb")
    else:
        local_file = open(u_dir + "untitled" + str(i) +".jpg", "wb")
        i+=1

    #Save the downloaded raw image to placeholder file
    resp.raw.decode_content = True
    shutil.copyfileobj(resp.raw, local_file)

    #Delete Response
    del resp



