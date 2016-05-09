package cn.org.eshow.bean;

/**
 * 跳转进入InputTelActivity时，intent传入页面标识当前为注册、找回密码
 * Created by daikting on 16/3/7.
 */
public enum Enum_CodeType {
    REGISTER("register"),FINDPASSWORD("identity"),REPASSWORD("identity"),BOUND("bound");

    private String intentExtra;
    Enum_CodeType(String intentExtra){
        this.intentExtra = intentExtra;
    }

    @Override
    public String toString() {
        return this.intentExtra;
    }
}
