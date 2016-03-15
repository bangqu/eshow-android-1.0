package com.bangqu.eshow.demo.bean;

/**
 * Created by daikting on 16/3/7.
 */
public class Switch_CodeType {
    public Switch_CodeType(Enum_CodeType enum_inputTel){
        switch (enum_inputTel){
            case REGISTER:
                onRegister();
                break;
            case FINDPASSWORD:
                onFindPassword();
                break;
            case REPASSWORD:
                onRePassword();
                break;
        }
    }

    /**
     * 注册
     */
    public void onRegister(){};

    /**
     * 找回密码
     */
    public void onFindPassword(){};

    /**
     * 重置密码
     */
    public void onRePassword(){};

}
