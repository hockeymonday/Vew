package com.example.vew;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

interface WallpaperCallback {
    void populateImage(ImageView imageView, ImageUrl url);
}

public class WallpaperView extends AppCompatActivity implements WallpaperCallback {
    ImageView imageView;
    Button button;
    int width = getScreenWidth();
    int height = getScreenHeight();
    Boolean canSet = true;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_wall_view);

        Bundle b = getIntent().getExtras();
        final String url = b.getString("imageUrlBig");

        button = findViewById(R.id.float_but);
        imageView = findViewById(R.id.imgv);
        imageView.setImageResource(0);
        relativeLayout = findViewById(R.id.full_id);

        // Make image fill the whole screen
        try { this.getSupportActionBar().hide(); }
        catch (NullPointerException e){ e.printStackTrace();}

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean dark_mode = settings.getBoolean("Dark Mode", false);

        if (((ColorDrawable) relativeLayout.getBackground()).getColor() == Color.parseColor("#7E7E7E") && dark_mode) {
            relativeLayout.setBackgroundColor(Color.parseColor("#121212"));
        }
        else if(((ColorDrawable) relativeLayout.getBackground()).getColor() == Color.parseColor("#121212")  && !dark_mode) {
            relativeLayout.setBackgroundColor(Color.parseColor("#7E7E7E"));
        }

        Toast.makeText(this,"Loading Image",Toast.LENGTH_SHORT).show();

        Glide.with(getApplicationContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        final WallpaperCallback callback = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Setting Wallpaper!",Toast.LENGTH_SHORT).show();
                new WallpaperTask(callback).execute(url);
                //Toast.makeText(getApplicationContext(),"Wallpaper Set!",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setWallpaper(Bitmap finalImage){
        if (canSet){
            WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                myWallpaperManager.setBitmap(finalImage);
            } catch (IOException e) {
                e.printStackTrace(); }
        }
        else{
            Toast.makeText(getApplicationContext(),"Unable to set Wallpaper", Toast.LENGTH_SHORT).show();
        }
    }


    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }


    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void populateImage(ImageView imageView, ImageUrl url) {

    }

    public class WallpaperTask extends AsyncTask<String, Void, Bitmap>{
        WallpaperCallback callback;

        @Override
        protected Bitmap doInBackground(String... imageUrls) {
            // Save image to set for wall
            Bitmap image = null;
            try {
                image = Glide.with(getApplicationContext()).load(imageUrls[0]).asBitmap().into(width,height).get();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
                canSet = false;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                canSet = false;
            }
            final Bitmap finalImage = image;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (canSet){ setWallpaper(finalImage);
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    System.exit(0);
                    }
                }
            });
            return null;
        }

        public WallpaperTask(WallpaperCallback cd){
            this.callback = cd;
        }
    }
}
