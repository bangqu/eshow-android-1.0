package com.bangqu.bean;

/**
 * 已开通城市列表实体类
 */

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CityBean implements Serializable {

    private static final long serialVersionUID = 1L;

    String id;// 城市ID
    String name;// 城市名
    String state;// 是否开通
    String version;
    String pinyin;
    String areaCode;
    String shopSize;
    String locationSize;
    String hot;
    String provinceBean;
    int CityID;
    private String sortLetters; // 显示数据拼音的首字母

    public static final class Table {
        public static final String id = "CityID";
        public static final String name = "name";
        public static final String pinyin = "pinyin";
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getShopSize() {
        return shopSize;
    }

    public void setShopSize(String shopSize) {
        this.shopSize = shopSize;
    }

    public String getLocationSize() {
        return locationSize;
    }

    public void setLocationSize(String locationSize) {
        this.locationSize = locationSize;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static List<CityBean> constractList(JSONArray jsonArray) {
        try {
            List<CityBean> list = new ArrayList<CityBean>();
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(JSON.parseObject(jsonArray.getJSONObject(i).toString(), CityBean.class));
            }
            return list;

        } catch (JSONException je) {
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProvinceBean() {
        return provinceBean;
    }

    public void setProvinceBean(String provinceBean) {
        this.provinceBean = provinceBean;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    @Override
    public boolean equals(Object o) {
        CityBean cityBean = (CityBean) o;
        if (null != cityBean) {
            return cityBean.getCityID() == CityID || cityBean.getName().equals(name);
        }
        return super.equals(o);
    }
}
