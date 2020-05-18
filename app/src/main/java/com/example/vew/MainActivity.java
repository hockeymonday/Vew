package com.example.vew;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.security.auth.callback.CallbackHandler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    GridLayoutManager gridLayoutManager;
    DataAdapter dataAdapter;
    private OkHttpClient client;
    ArrayList<String> imageUrls = new ArrayList<>();
    ArrayList<String> imageUrlsBig = new ArrayList<>();
    ArrayList<ImageUrl> imageUrlList = new ArrayList<>();
    ArrayList<ImageUrl> imageUrlListBig = new ArrayList<>();
    Boolean dark_mode = false;
    Boolean cache = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        relativeLayout = findViewById(R.id.relView);
        recyclerView = findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        client = new OkHttpClient();

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        if (cache){ deleteCache(this);}
        cache=false;
        dataAdapter = new DataAdapter(getApplicationContext(), imageUrlList,imageUrlListBig);
        recyclerView.setAdapter(dataAdapter);
        prepareData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (cache){ deleteCache(this);}
        cache=false;
        prepareData();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        dark_mode = settings.getBoolean("Dark Mode",false);

        if (((ColorDrawable) relativeLayout.getBackground()).getColor() == Color.parseColor("#7E7E7E") && dark_mode) {
            relativeLayout.setBackgroundColor(Color.parseColor("#121212"));
            }
        else if(((ColorDrawable) relativeLayout.getBackground()).getColor() == Color.parseColor("#121212")  && !dark_mode) {
            relativeLayout.setBackgroundColor(Color.parseColor("#7E7E7E"));
            }

    }


    // Method to prepare data for custom adapter
    public void prepareData() {
        Request request = new Request.Builder().url("http://34.72.6.126/loadimages/").build();
        Log.wtf("here","here");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { e.printStackTrace();}

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // Get json string
                String json = response.body().string();
                // parse json
                try {
                    JSONObject Jobject = new JSONObject(json);
                    JSONArray dataList = Jobject.getJSONArray("data");
                    imageUrls.clear();
                    imageUrlsBig.clear();
                    // get compressed and uncompressed image URLS
                    for (int i = 0; i < dataList.length(); i++) {
                        JSONObject object = dataList.getJSONObject(i);
                        String e = object.getString("compressed_url");
                        imageUrls.add(e);
                        String f = object.getString("uncompressed_url");
                        imageUrlsBig.add(f);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                imageUrlList.clear();
                imageUrlListBig.clear();
                for (int i = 0; i < imageUrls.size(); i++) {
                    ImageUrl imageUrl = new ImageUrl();
                    ImageUrl imageUrl1 = new ImageUrl();
                    imageUrl.setImageUrl(imageUrls.get(i));
                    imageUrl1.setImageUrl(imageUrlsBig.get(i));
                    imageUrlList.add(imageUrl);
                    imageUrlListBig.add(imageUrl1);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dataAdapter.notifyDataSetChanged();
                    }
                });
            }});
    }


    public static void deleteCache(Context context) {
        Log.wtf("here", "deleet");
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}


