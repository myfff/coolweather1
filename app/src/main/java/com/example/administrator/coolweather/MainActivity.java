package com.example.administrator.coolweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import util.Utility;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /* *//* LitePal.getDatabase();*//*
       findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Utility.handleProvinceResponse("{\"name\":\"fff\",\"id\":\"3\"}");
           }
       });*/
    }

    @Override
    protected void onDestroy() {
    }
}
