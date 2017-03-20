package db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/3/19.
 */
/*<litepal>
    <dename value="cool_weaather"/>
    <version value="1"/>
    <list>
        <mapping class="java.db.Province"/>
        <mapping class="java.db.City"/>
        <mapping class="java.db.County"/>
    </list>
    </litepal>*/
public class City extends DataSupport {
   private  int id;
    private  String cityName;
    private int cityCode;
    private  int  provinceId;

    public int  getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int  provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {

        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }



    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getCityCode() {
        return cityCode;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
