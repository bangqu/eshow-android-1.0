package com.bangqu.eshow.demo.bean;

/**
 * 跳转进入InputTelActivity时，intent传入页面标识当前为注册、找回密码
 * Created by daikting on 16/3/7.
 */
public enum Enum_InputTel {
    REGISTER("Register"),FINDPASSWORD("FindPassword");

    private String intentExtra;
    private Enum_InputTel(String intentExtra){
        this.intentExtra = intentExtra;
    }

    @Override
    public String toString() {
        return this.intentExtra;
    }
}
