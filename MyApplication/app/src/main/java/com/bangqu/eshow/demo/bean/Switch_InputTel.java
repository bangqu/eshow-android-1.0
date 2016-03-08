package com.bangqu.eshow.demo.bean;

/**
 * Created by daikting on 16/3/7.
 */
public abstract class Switch_InputTel {
    public Switch_InputTel(Enum_InputTel enum_inputTel){
        switch (enum_inputTel){
            case REGISTER:
                onRegister();
                break;
            case FINDPASSWORD:
                onFindPassword();
                break;
        }
    }

    public abstract void onRegister();

    public abstract void onFindPassword();
}
