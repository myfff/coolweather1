package gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/20.
 */
/*http://guolin.tech/api/weather?cityid=CN101181009&key=55af7b5faa5e47a8bddd1e92fcdbdf3c
*
* {
    "HeWeather": [
        {
            "aqi": {
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
            },

            "daily_forecast": [
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
                },
                {
                    "astro": {
                        "mr": "00:52",
                        "ms": "11:36",
                        "sr": "05:56",
                        "ss": "18:06"
                    },
                    "cond": {
                        "code_d": "101",
                        "code_n": "300",
                        "txt_d": "多云",
                        "txt_n": "阵雨"
                    },
                    "date": "2017-03-21",
                    "hum": "76",
                    "pcpn": "0.4",
                    "pop": "57",
                    "pres": "1022",
                    "tmp": {
                        "max": "15",
                        "min": "9"
                    },
                    "uv": "9",
                    "vis": "18",
                    "wind": {
                        "deg": "26",
                        "dir": "无持续风向",
                        "sc": "微风",
                        "spd": "10"
                    }
                },
                {
                    "astro": {
                        "mr": "01:41",
                        "ms": "12:26",
                        "sr": "05:55",
                        "ss": "18:06"
                    },
                    "cond": {
                        "code_d": "300",
                        "code_n": "305",
                        "txt_d": "阵雨",
                        "txt_n": "小雨"
                    },
                    "date": "2017-03-22",
                    "hum": "86",
                    "pcpn": "25.2",
                    "pop": "100",
                    "pres": "1019",
                    "tmp": {
                        "max": "12",
                        "min": "9"
                    },
                    "uv": "2",
                    "vis": "15",
                    "wind": {
                        "deg": "92",
                        "dir": "东风",
                        "sc": "微风",
                        "spd": "4"
                    }
                }
            ],
            "hourly_forecast": [
                {
                    "cond": {
                        "code": "309",
                        "txt": "毛毛雨/细雨"
                    },
                    "date": "2017-03-20 19:00",
                    "hum": "95",
                    "pop": "36",
                    "pres": "1016",
                    "tmp": "15",
                    "wind": {
                        "deg": "316",
                        "dir": "西北风",
                        "sc": "微风",
                        "spd": "16"
                    }
                },
                {
                    "cond": {
                        "code": "305",
                        "txt": "小雨"
                    },
                    "date": "2017-03-20 22:00",
                    "hum": "96",
                    "pop": "21",
                    "pres": "1018",
                    "tmp": "14",
                    "wind": {
                        "deg": "324",
                        "dir": "西北风",
                        "sc": "微风",
                        "spd": "15"
                    }
                }
            ],
            "now": {
                "cond": {
                    "code": "300",
                    "txt": "阵雨"
                },
                "fl": "8",
                "hum": "95",
                "pcpn": "0.1",
                "pres": "1016",
                "tmp": "11",
                "vis": "8",
                "wind": {
                    "deg": "280",
                    "dir": "西风",
                    "sc": "3-4",
                    "spd": "12"
                }
            },
            "status": "ok",
            "suggestion": {
                "air": {
                    "brf": "中",
                    "txt": "气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"
                },
                "comf": {
                    "brf": "舒适",
                    "txt": "白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"
                },
                "cw": {
                    "brf": "较适宜",
                    "txt": "较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"
                },
                "drsg": {
                    "brf": "较冷",
                    "txt": "建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"
                },
                "flu": {
                    "brf": "较易发",
                    "txt": "天气较凉，较易发生感冒，请适当增加衣服。体质较弱的朋友尤其应该注意防护。"
                },
                "sport": {
                    "brf": "较适宜",
                    "txt": "天气较好，但考虑气温较低，推荐您进行室内运动，若户外适当增减衣物并注意防晒。"
                },
                "trav": {
                    "brf": "适宜",
                    "txt": "天气较好，但丝毫不会影响您出行的心情。温度适宜又有微风相伴，适宜旅游。"
                },
                "uv": {
                    "brf": "弱",
                    "txt": "紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"
                }
            }
        }
    ]
}*/

public class Basic {
    //JSON中的字段有些可能不适合做Java字段来命名，所以使用注解的方式来建立JSON字段和Java字段之间的映射
   @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherID;
    //内部类也需要在本类中声明
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
/* "basic": {
                "city": "上海",
                "cnty": "中国",
                "id": "CN101020100",
                "lat": "31.213000",
                "lon": "121.445000",
                "update": {
                    "loc": "2017-03-20 18:51",
                    "utc": "2017-03-20 10:51"
                }
            },*/
