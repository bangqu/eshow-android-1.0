package cn.org.eshow.demo.bean;

/**
 * Created by 豚趣 on 2017/6/9.
 */
public class ThirdPartyLoginBean {

    /**
     * status : 1
     * msg : 等待绑定账号
     * bind : false
     */

    private String status;
    private String msg;
    private boolean bind;

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

    public boolean isBind() {
        return bind;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }
}
