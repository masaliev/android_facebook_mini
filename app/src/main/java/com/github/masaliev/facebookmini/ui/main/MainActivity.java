package com.github.masaliev.facebookmini.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.masaliev.facebookmini.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public static Intent getStartIntent(Context context){
        return new Intent(context, MainActivity.class);
    }
}
