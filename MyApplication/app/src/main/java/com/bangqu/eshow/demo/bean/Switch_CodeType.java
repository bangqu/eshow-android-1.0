package com.bangqu.eshow.demo.bean;

/**
 * Created by daikting on 16/3/7.
 */
public abstract class Switch_CodeType {
    public Switch_CodeType(Enum_CodeType enum_inputTel){
        switch (enum_inputTel){
            case REGISTER:
                onRegister();
                break;
            case FINDPASSWORD:
                onFindPassword();
                break;
            case LOGIN:
                onLogin();
                break;
            case IDENTITY:
                onIdentity();
                break;

        }
    }

    public void onRegister(){};

    public void onFindPassword(){};

    public void onLogin(){};

    public void onIdentity(){};

}
