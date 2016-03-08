package com.bangqu.eshow.demo.bean;

/**
 * 注册登录模块语音播放接口需要传入的类型
 * Created by daikting on 16/3/7.
 */
public enum Enum_VoiceType {
    REGISTER("Register"),LOGIN("Login"),IDENTITY("Identity");

    private String type;
    private Enum_VoiceType(String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
