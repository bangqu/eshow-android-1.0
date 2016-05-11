package cn.org.eshow.demo.bean;

/**
 * Created by daikting on 16/3/31.
 */
public class CityBean {
    private int CityID;
    private String name;
    private int ProID;
    private String CitySort;

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProID() {
        return ProID;
    }

    public void setProID(int proID) {
        ProID = proID;
    }

    public String getCitySort() {
        return CitySort;
    }

    public void setCitySort(String citySort) {
        CitySort = citySort;
    }
}
