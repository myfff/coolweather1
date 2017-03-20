package util;

import db.Province;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/3/19.
 * okhttp请求网络，回调接口内部已经封装
 */

public class HttpUtil {
     public  static  void sendOKHttpRequest(String address,okhttp3.Callback callback){
         OkHttpClient client=new OkHttpClient();
         Request request=new Request.Builder().url(address).build();
         client.newCall(request).enqueue(callback);
     }
}
