package com.example.administrator.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import db.City;
import db.County;
import db.Province;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUtil;
import util.Utility;

/**
 * Created by Administrator on 2017/3/20.
 */
/*遍历全国省市县的代码我们在后面们还要用到，为了方便复用我们写在碎片里（后面想要直接在布局里面加载即可），而不是activity*/

public class ChooseAreaFragment extends Fragment {
    private Button bt_back;
    private TextView tv_title;
    private ListView listview;
    //省的等级常量
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    /**
     * 集合数据，泛型为String，是用来存储省，或市或县的集合列表
     */
    private List<String> listDatas = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    /**
     * 省级列表
     */
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    /**
     * 选中的省份
     */
    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;
    /**
     * 当前选中的级别
     */
    private int currentLeve;

    /**
     * 下载进度条对话框
     */
    private ProgressDialog progrssDialog;
    /**
     * 初始化布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.choose_area, null);
        bt_back = (Button) view.findViewById(R.id.bt_back);
        tv_title = (TextView) view.findViewById(R.id.title_text);
        listview = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listDatas);
        listview.setAdapter(adapter);
        return view;
    }

    /**
     * 做点击条目和返回按钮状态的监听
     * listView点省查询所有该省的市，点市查询该市的所县
     * back点击时县查市，是市查省
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLeve == LEVEL_PROVINCE) {
                    //得到选择的省
                    selectedProvince = provinceList.get(position);
                    //查询市
                    queryCitys();
                } else if (currentLeve == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCountys();
                }else if (currentLeve==LEVEL_COUNTY){   //我们需要在我们点击具体的某个县的时候，进行天气预报的跳转
                    //得到Weather_id  我们进行跳转
                    String weather_id=countyList.get(position).getWeatherId();
                    if(getActivity()instanceof MainActivity){
                        //打开WeatherActivity
                        Intent intent=new Intent(getContext(),WeatherActivity.class);
                        intent.putExtra("weather_id",weather_id);
                        startActivity(intent);
                        getActivity().finish();
                    }else  if (getActivity() instanceof  WeatherActivity){
                        //在本页面，无需做逻辑处理，只需要得到上下文对象关闭侧边栏打开主界面即可
                        WeatherActivity activity= (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        //跳转显示进度条
                        activity.swipeRefresh.setRefreshing(true);
                        //然后请求最新天气的信息
                        activity.requestWether(weather_id);
                    }
                }
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLeve == LEVEL_COUNTY) {
                    queryCitys();
                } else if (currentLeve == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
        queryProvinces();//由于第一次初始化的时候，这些逻辑都不会走，我们只有调用一下查询Province
    }

    /**
     * 查询所有省,优先从数据库中查询，当数据库中查询不到，再到服务器上查询
     */
    private void queryProvinces() {
        tv_title.setText("中国");
        bt_back.setVisibility(View.INVISIBLE);
        //查询数据库中所有的省份数据
        provinceList = DataSupport.findAll(Province.class);
        //如果数据库中有数据解我们直接用，没有我们就去请求
        if (provinceList.size() > 0) {
            listDatas.clear();//保证数据干净，先清空
            for (Province province : provinceList) {
                listDatas.add(province.getProviceName());
            }
            //【】通知适配器刷新
            adapter.notifyDataSetChanged();
            //默认选择第一个省份
            listview.setSelection(0);
            //将当前状态设置为省级
            currentLeve = LEVEL_PROVINCE;
        } else {
            String adress = "http://guolin.tech/api/china";
            queryFromServer(adress, "Province");
        }
    }


    /**
     * 查询所有市 数据库没有就网络请求
     */
    private void queryCitys() {
        tv_title.setText(selectedProvince.getProviceName());
        bt_back.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceId=?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            listDatas.clear();
            for (City city : cityList) {
                listDatas.add(city.getCityName());
            }
            //【】通知适配器刷新
            adapter.notifyDataSetChanged();
            //默认选择第一个省份
            listview.setSelection(0);
            //将当前状态设置为省级
            currentLeve = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvince.getProviceCode();
            String adress = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(adress, "City");
        }
    }

    /**
     * 查询所有县  数据库没有就网络请求
     */
    private void queryCountys() {
        tv_title.setText(selectedCity.getCityName());
        bt_back.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityId=?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            listDatas.clear();
            for (County conuty : countyList) {
                listDatas.add(conuty.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listview.setSelection(0);
            currentLeve = LEVEL_COUNTY;
        } else {
            int provinceCode = selectedProvince.getProviceCode();
            int cityCode = selectedCity.getCityCode();
            String adress = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(adress,"County");
        }
    }


    /**
     * 从服务器上查询 根据传递过来的地址和字符
     *
     * @param adress
     * @param type   判断是请求的哪个等级 好做处理
     */
    private void queryFromServer(String adress, final String type) {
        showProgressDialog();
        HttpUtil.sendOKHttpRequest(adress, new Callback() {
            /**请求成功
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //请求中弹出一个对话框
                boolean result = false;
                String reponseText = response.body().string();
                //很具不同的类型，解析Json数据并将数据存入数据库用来使用
                if (type.equals("Province")) {
                    result = Utility.handleProvinceResponse(reponseText);
                } else if (type.equals("City")) {
                    result = Utility.handleCityResponse(reponseText, selectedProvince.getId());
                } else if (type.equals("County")) {
                    result = Utility.handleCountyResponse(reponseText, selectedCity.getId());
                }
                //根据是否解析保存数据库成功，然后去再次调用查询功能区更新UI（因为最终更新UI在queryPRovince（）...中）
                if (result) {
                    //通过runOnUiThread切换到主线程里面去工作（就是更新UI）
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //请求成功，当显示的时候，关闭进度条
                            closeProgressDialog();
                            if (type.equals("Province")) {
                                queryProvinces();
                            } else if (type.equals("City")) {
                                queryCitys();
                            } else if (type.equals("County")) {
                                queryCountys();
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败，同样通过runOnUiThrea（）ff互道主线程
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    /**
     * 关下载进度对话库
     */
    private void closeProgressDialog() {
        if(progrssDialog!=null){
            progrssDialog.dismiss();
        }
    }

    /**
     * 显示下载进度对话框
     */
    private void showProgressDialog() {
        if(progrssDialog==null) {
            progrssDialog = new ProgressDialog(getActivity());
            progrssDialog.setMessage("正在加载。。。");
            progrssDialog.setCanceledOnTouchOutside(false);//在外面点击不可取消
        }
        progrssDialog.show();
    }
}
