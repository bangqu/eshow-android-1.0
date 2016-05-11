package cn.org.eshow.demo.bean;

import java.io.Serializable;

/**
 * 区的数据封装
 * Created by daikting on 16/3/31.
 */
public class AreaBean implements Serializable{
    private int Id;
    private String DisName;
    private int CityID;
    private String DisSort;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDisName() {
        return DisName;
    }

    public void setDisName(String disName) {
        DisName = disName;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public String getDisSort() {
        return DisSort;
    }

    public void setDisSort(String disSort) {
        DisSort = disSort;
    }
}
