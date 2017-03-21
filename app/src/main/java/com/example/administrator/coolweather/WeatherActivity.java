package com.example.administrator.coolweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import gson.Forecast;
import gson.Weather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUtil;
import util.Utility;
/*程序的详情界面*/

/**
 *
 */
public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;

    private TextView titleCity;//头布局
    private TextView titltUpdataTime;

    private TextView degreeText;//温度
    private TextView weatherInfoText;//天气信息

    private LinearLayout forecastLayout;//动态加入未来几天信息

    private TextView aqiText;//空气质量
    private TextView pm25Text;

    private TextView comfortText;//生活建议
    private TextView carWashText;
    private TextView sportText;
    /**
     * 获取必应背图片作为显示天气信息界面的背景图
     */
    private ImageView  bingPicImg;

    /**
     *采用下拉来触发更新事件
     * 外面嵌套了一层SwipeRefreshLayout
     */
   public SwipeRefreshLayout swipeRefresh;
    /**
     * 去服务器上查询天气信息的id
     */
    private String mWeratherId;

    /**
     * 以下两个控件是用来处理滑动菜单的逻辑
     * @param savedInstanceState
     */
    public DrawerLayout drawerLayout;//要共有，要不然在、fragment中得不到对象
    private Button navButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        drawerLayout=(DrawerLayout)findViewById(R.id.draw_layout);
        navButton=(Button)findViewById(R.id.nav_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);//此处的Start一定要和布局文件中我们设置的一样
            //在这里打开侧边栏即遍历省市县，我们还是需要做点击后的逻辑跳转（实在WeratherActivity还是在MainActivity活动中跳转的，具体要在Fragment中做判断）
            }
        });
        //做下拉的监听
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);//设置下拉刷新进度条的颜色

        //让状态栏和背景融合到一起
        if(Build.VERSION.SDK_INT>=20){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(  View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //初始化个控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);

        //头布局
       titleCity=(TextView)findViewById(R.id.title_text);
        titltUpdataTime = (TextView) findViewById(R.id.title_updata_time);
        //当天消息
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        //未来几日消息
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        //aqi pm25指数布局
        aqiText = (TextView) findViewById(R.id.api_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        //生活建议
        comfortText=(TextView)findViewById(R.id.comfort_text);
        carWashText=(TextView)findViewById(R.id.car_wash_text);
        sportText=(TextView)findViewById(R.id.sport_text);

        bingPicImg=(ImageView)findViewById(R.id.bing_pic_img);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //看图片是否有缓存链接地址
        String bingpic=prefs.getString("bing_pic",null);
        if(bingpic!=null){
            //有缓存就直接进行加载，根据bingpic链接地址加载给控件bingPicImg适配图片
            Glide.with(this).load(bingpic).into(bingPicImg);
        }else {
            //去网络中加载图片
            loadBingPic();
        }
       //获取Weather实例
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            //有缓存直接解析天气信息
            Weather weather = Utility.handlerWeatherRrsponse(weatherString);
           mWeratherId=weather.basic.weatherID;
            showWeatherInfo(weather);
        } else {
            //无缓存区服务器上取缓存
           mWeratherId= getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);//请求的时候隐藏ScoolerView，要不然为空很难看
         //下拉时请求最新的天气
            requestWether(mWeratherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //如果下拉，就进行天气的更新，即网络请求新的天气（但此时我们需要一个weatherid，需要在）
                requestWether(mWeratherId);
            }
        });
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic="httP://guolin.tech/api/bing_pic";
        HttpUtil.sendOKHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求网络失败
                Toast.makeText(getApplicationContext(),"请求必应每日一图的链接地址失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到链接地址保存并且要更新图片
                final String bingpic=response.body().string();
                PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this)
                        .edit().putString("bing_pic",bingpic).commit();
                //刷新背景图片(从子线程中切换到主线程)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingpic).into(bingPicImg);
                    }
                });
            }
        });
    }

    /**
     * 根据天气id请求城市天气信息
     *
     * @param weatherId
     */
    public  void requestWether(String weatherId) {
        //CN101020100 就是weatherid（只有这一部分是变得）
        String weatherURl = "http://guolin.tech/api/weather?cityid=" + weatherId +
                "&key=55af7b5faa5e47a8bddd1e92fcdbdf3c";
        Log.i("ahkjd ", "requestWether: "+weatherURl);
        HttpUtil.sendOKHttpRequest(weatherURl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //通过此方法切换到主线程中更新
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                   swipeRefresh.setRefreshing(false);//刷新事件结束，并隐藏刷新进度条
                    }
                });
            }
            /**请求成功
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                //从服务器上取得JSON数据，并交给工具类去解析，得到的Weather对象，
                final Weather weather = Utility.handlerWeatherRrsponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Werather对象不为空并且状态码为ok，就进行存储，并将数据展示到界面（调用方法）
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                            editor.putString("weather", responseText);
                            editor.commit();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(getApplicationContext(), "获取解析天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);//刷新事件结束，并隐藏刷新进度条
                    }
                });
            }
        });
        loadBingPic();//每次请求天气信息的时候也会刷新背景图片
    }

    /**
     * 处理并展示Weather实体类中的数据
     *
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        //如果请求和解析成功。需要显示到界面
        String nameCity=weather.basic.cityName;
        String updateTime=weather.basic.update.updateTime.split(" ")[1];

        String degree=weather.now.temperature+"度";
        String weatherInfo=weather.now.more.info;

        titleCity.setText(nameCity);
         titltUpdataTime.setText(updateTime);

        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        //在添加未来信息的时候需要清空
        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);

            TextView dataTxext=(TextView)view.findViewById(R.id.data_text);
            TextView infoText=(TextView)view.findViewById(R.id.info_text);
            TextView  maxText=(TextView)view.findViewById(R.id.max_text);
            TextView  minText=(TextView)view.findViewById(R.id.min_text);

            dataTxext.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }

if(weather.aqi!=null){
    aqiText.setText(weather.aqi.city.aqi);
    pm25Text.setText(weather.aqi.city.pm25);

}

        String  comfort="舒适度："+weather.suggestion.comfort.info;
        String  carWash="洗车指数: "+weather.suggestion.carWash.info;
        String  sport="运动建议: "+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

        weatherLayout.setVisibility(View.VISIBLE);
    }
}
