package cn.org.eshow.bean;

/**
 * 第三方登录支持的登录方式
 * Created by daikting on 16/3/14.
 */
public enum Enum_ThirdType {
    QQ("qq"),WeChat("weixin");

    private String platformType;
    Enum_ThirdType(String platformType){
        this.platformType = platformType;
    }

    @Override
    public String toString() {
        return this.platformType;
    }
}
