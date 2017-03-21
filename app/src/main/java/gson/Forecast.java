package gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/20.
 */
/* "daily_forecast": [
                {
                    "astro": {
                        "mr": "00:02",
                        "ms": "10:49",
                        "sr": "05:57",
                        "ss": "18:05"
                    },
                    "cond": {
                        "code_d": "305",
                        "code_n": "104",
                        "txt_d": "小雨",
                        "txt_n": "阴"
                    },
                    "date": "2017-03-20",
                    "hum": "91",
                    "pcpn": "2.5",
                    "pop": "100",
                    "pres": "1017",
                    "tmp": {
                        "max": "13",
                        "min": "9"
                    },
                    "uv": "4",
                    "vis": "11",
                    "wind": {
                        "deg": "215",
                        "dir": "无持续风向",
                        "sc": "微风",
                        "spd": "0"
                    }
                },*/
public class Forecast {
    public  String date;

    @SerializedName("tmp")
    public  Temperature temperature;

    @SerializedName("cond")
    public  More more;
    public  class Temperature{
        public  String max;
        public  String min;
    }
    public  class More{
        @SerializedName("txt_d")
        public  String info;
    }

}
