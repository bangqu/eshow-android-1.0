package com.bangqu.bean;

import java.io.Serializable;

/**
 * Created by 豚趣 on 2017/2/8.
 */
public class AccessTokenBean implements Serializable {

    /**
     * access_token : KvKmjv5Gm-1j1NCHyLtQiBU-uDbxylGqqTUmSD_nU6wpMnfH0MLz40bZ1-k8MBHODpO8KsrYEiPr_sKDLFYU07W6rBh-FoiMDfdS0XkUAd4
     * expires_in : 7200
     * refresh_token : sh5Xmea4n607ZBhNQ2eKwJ2ZzpwUluxt7oZ5k6DMu66CQ9QlJiwNpwZ_w5AP92fF8XC8cFk7LAEs3H16fv_Vi5Q_0uKi2sB0EeObxXaGjow
     * openid : oSJJBv-kpK9TXEI74xEuGZkbgR-4
     * scope : snsapi_userinfo
     * unionid : o5Gs1wMAP-RrBni4s1xZ7dGxIpiY
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
