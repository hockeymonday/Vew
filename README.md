# Vew
Vew is an Android application that utilizes the high resolution photography avalible on Unsplash for wallpapers!

#Features and Overview
Vew runs a grid interface of images which it pulls from the internet. The images in the grid interface are compressed 
for faster load times, but when you click on an image and it loads asa fullscreen image, it will load in full resolution (uncompressed).
Because the images load in full resolution when clicked on, stable internet connection is reccomended.

The application is complete with a Dark Theme feature as well!

#APIs and Structure
The application makes use of the Glide API to load images from the web, and uses the OkHttp API to pull the imageUrls from an online 
database that was created by backend developer Andrew Zeng. Andrew's backend database stores links to the images which are hosted on
Github, from which I make get requests to.

#Compatability
Vew is reccomended to be run on Android API 26+ (Oreo).

#Credits
Developed by Adam Kadhim
