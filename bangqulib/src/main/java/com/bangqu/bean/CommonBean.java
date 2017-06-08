package com.bangqu.bean;

import java.io.Serializable;

/**
 * Created by 豚趣 on 2017/4/25.
 */
public class CommonBean implements Serializable {

    /**
     * status : 1
     * msg : 验证码已经发送到手机15295013726
     */

    private String status;
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
