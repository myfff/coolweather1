package gson;

/**
 * Created by Administrator on 2017/3/20.
 */
/* "aqi": {
                "city": {
                    "aqi": "73",
                    "co": "1",
                    "no2": "87",
                    "o3": "19",
                    "pm10": "58",
                    "pm25": "53",
                    "qlty": "良",
                    "so2": "8"
                }
            },*/

public class AQI {
    public  AQICity city;
    public class  AQICity{
        public  String aqi;
        public  String pm25;
    }
}
