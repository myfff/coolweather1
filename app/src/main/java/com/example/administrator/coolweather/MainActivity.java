package com.example.administrator.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    /*程序主入口 ，加载一个ChooseAreaFragmeent， */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //如果上次用户已经选择，那么我们就不在让其再次去选择而是直接进去WeatherActivity
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getString("weather",null)!=null){
            Intent intnt=new Intent(this,WeatherActivity.class);
            startActivity(intnt);
            finish();
        }
    }
}
