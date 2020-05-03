package com.app.irinde;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ViewImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        ImageView imageView = findViewById(R.id.imgView);
        //loading image attached to text

        String imageUri = getIntent().getStringExtra("image");
        Glide.with(this)
                .load(imageUri)
                .placeholder(R.drawable.logo_24dp) //placeholder
                .error(R.drawable.logo_24dp) //error
                .into(imageView);
    }
}
