package com.example.vew;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageUrl {
    String imageUrl;

    public String getImageUrl() {return this.imageUrl; }


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
