package db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/3/19.
 */

public class Province  extends DataSupport {
    private  int  id;//每个实体都应该有的
    private  String  proviceName;//省的name
    private int  proviceCode;//记录 省的代号

    public int getProviceCode() {
        return proviceCode;
    }

    public void setProviceCode(int proviceCode) {
        this.proviceCode = proviceCode;
    }

    public String getProviceName() {

        return proviceName;
    }

    public void setProviceName(String proviceName) {
        this.proviceName = proviceName;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
