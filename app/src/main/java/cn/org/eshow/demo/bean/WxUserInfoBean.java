package cn.org.eshow.demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 豚趣 on 2017/6/9.
 */
public class WxUserInfoBean implements Serializable {

    /**
     * openid : oXzw5v2D9LqTUycbgyXgr5_EIv5g
     * nickname : 豚趣网
     * sex : 0
     * language : zh_CN
     * city :
     * province :
     * country : CN
     * headimgurl : http://wx.qlogo.cn/mmopen/uDyGVcCW1T7OtPZaV8pbRDe9tnG1x4MsmylR8pyLuKYNzWvWPu0iakSnGibar9TbcwZoaz9O0JQ8RSd2G9zbzrZd4XwKibswDq6/0
     * privilege : []
     * unionid : outtiuI6nC8O9nv2NPfw85baVd-I
     */

    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<?> privilege;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<?> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<?> privilege) {
        this.privilege = privilege;
    }
}
