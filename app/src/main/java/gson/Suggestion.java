package gson;

import com.google.gson.annotations.SerializedName;

import db.County;

/**
 * Created by Administrator on 2017/3/20.
 */
/* "suggestion": {
                "air": {
                    "brf": "中",
                    "txt": "气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"
                },

                "drsg": {
                    "brf": "较冷",
                    "txt": "建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"
                },
                "flu": {
                    "brf": "较易发",
                    "txt": "天气较凉，较易发生感冒，请适当增加衣服。体质较弱的朋友尤其应该注意防护。"
                },

                "trav": {
                    "brf": "适宜",
                    "txt": "天气较好，但丝毫不会影响您出行的心情。温度适宜又有微风相伴，适宜旅游。"
                },

                "uv": {
                    "brf": "弱",
                    "txt": "紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"
                },


                 "comf": {要
                    "brf": "舒适",
                    "txt": "白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"
                },
                "cw": {
                    "brf": "较适宜",
                    "txt": "较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"
                },
                 "sport": {
                    "brf": "较适宜",
                    "txt": "天气较好，但考虑气温较低，推荐您进行室内运动，若户外适当增减衣物并注意防晒。"
                }
            }
        }
    ]*/

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public  CarWash carWash;
    public  Sport sport;

    public  class Comfort{
        @SerializedName("txt")
        public  String info;
    }
    public  class CarWash{
        @SerializedName("txt")
        public  String  info;
    }
    public  class Sport{
        @SerializedName("txt")
        public  String  info;
    }
}
