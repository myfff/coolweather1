package service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import java.io.IOException;

import gson.Weather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUtil;
import util.Utility;
/*后台自动更新的服务*/
public class AutoUpDateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour=8*60*60*1000;//8小时的毫秒数
        long triggerAtTime= SystemClock.elapsedRealtime()+anHour;
        Intent i=new Intent(this,AutoUpDateService.class);
        PendingIntent pendingIntent=PendingIntent.getService(this,0,i,0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     *
     */
    private void updateBingPic() {
        String requestBingPic="httP://guolin.tech/api/bing_pic";
        HttpUtil.sendOKHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingpic=response.body().string();
                PreferenceManager.getDefaultSharedPreferences(AutoUpDateService.this)
                        .edit().putString("bing_pic",bingpic).commit();
            }
        });

    }

    /**
     * 跟新天气信息
     */
    private void updateWeather() {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        if(weatherString!=null){
            //有缓存直接解析
            Weather weather= Utility.handlerWeatherRrsponse(weatherString);
            String weatherId=weather.basic.weatherID;

            String  weatherUrl="http://guolin.tech/api/weather?cityid=" + weatherId +
                    "&key=55af7b5faa5e47a8bddd1e92fcdbdf3c";
            HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                   e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText=response.body().string();
                    Weather weather=Utility.handlerWeatherRrsponse(responseText);
                    if(weather!=null && "ok".equals(weather.status)){
                        PreferenceManager.getDefaultSharedPreferences(AutoUpDateService.this)
                                .edit().putString("weather",responseText).apply();
                    }
                }
            });

        }
    }
}
