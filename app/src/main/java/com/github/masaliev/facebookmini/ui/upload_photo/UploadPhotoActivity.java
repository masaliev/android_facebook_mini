package com.github.masaliev.facebookmini.ui.upload_photo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.masaliev.facebookmini.R;

public class UploadPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
    }

    public static Intent getStartIntent(Context context){
        return new Intent(context, UploadPhotoActivity.class);
    }
}
