package gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/20.
 */
/* "now": {
                "cond": {  要
                    "code": "300",
                    "txt": "阵雨"
                },
                "fl": "8",
                "hum": "95",
                "pcpn": "0.1",
                "pres": "1016",
                "tmp": "11", 要
                "vis": "8",
                "wind": {
                    "deg": "280",
                    "dir": "西风",
                    "sc": "3-4",
                    "spd": "12"
                }
            },*/
public class Now {
    @SerializedName("tmp")
   public   String temperature;

    @SerializedName("cond")
   public   More more;

    public  class More{
        @SerializedName("txt")
        public  String info;

    }
}
