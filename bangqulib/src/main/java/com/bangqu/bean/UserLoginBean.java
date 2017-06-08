package com.bangqu.bean;

import java.io.Serializable;

/**
 * Created by 豚趣 on 2016/12/15.
 */
public class UserLoginBean implements Serializable {

    /**
     * status : 1
     * msg : 登录成功
     * accessToken : {"id":15,"addTime":"2017-06-08 16:34:06","updateTime":"2017-06-08 16:34:06","accessToken":"a31ab1c4989be16a73348a2114aaa334","expiresIn":2592000000,"refreshToken":"a5343e1a30c924b05fb89ee3ddde33e"}
     * user : {"id":469,"version":0,"addTime":"2017-06-08 16:34:06","updateTime":"2017-06-08 16:34:06","username":"15295013726","nickname":null,"age":0,"photo":"https://qiniu.easyapi.com/user/default.jpg","realname":"","male":null,"birthday":null,"constellation":0,"birthAttrib":0,"bloodType":0,"hobby":null,"marital":0,"intro":null,"website":null,"clientId":"","deviceToken":"","easemobId":"15295013726"}
     */

    private String status;
    private String msg;
    /**
     * id : 15
     * addTime : 2017-06-08 16:34:06
     * updateTime : 2017-06-08 16:34:06
     * accessToken : a31ab1c4989be16a73348a2114aaa334
     * expiresIn : 2592000000
     * refreshToken : a5343e1a30c924b05fb89ee3ddde33e
     */

    private AccessTokenBean accessToken;
    /**
     * id : 469
     * version : 0
     * addTime : 2017-06-08 16:34:06
     * updateTime : 2017-06-08 16:34:06
     * username : 15295013726
     * nickname : null
     * age : 0
     * photo : https://qiniu.easyapi.com/user/default.jpg
     * realname : 
     * male : null
     * birthday : null
     * constellation : 0
     * birthAttrib : 0
     * bloodType : 0
     * hobby : null
     * marital : 0
     * intro : null
     * website : null
     * clientId : 
     * deviceToken : 
     * easemobId : 15295013726
     */

    private UserBean user;

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

    public AccessTokenBean getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessTokenBean accessToken) {
        this.accessToken = accessToken;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class AccessTokenBean {
        private int id;
        private String addTime;
        private String updateTime;
        private String accessToken;
        private long expiresIn;
        private String refreshToken;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public long getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    public static class UserBean {
        private int id;
        private int version;
        private String addTime;
        private String updateTime;
        private String username;
        private String nickname;
        private int age;
        private String photo;
        private String realname;
        private String male;
        private String birthday;
        private int constellation;
        private int birthAttrib;
        private int bloodType;
        private String hobby;
        private int marital;
        private String intro;
        private String website;
        private String clientId;
        private String deviceToken;
        private String easemobId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getMale() {
            return male;
        }

        public void setMale(String male) {
            this.male = male;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getConstellation() {
            return constellation;
        }

        public void setConstellation(int constellation) {
            this.constellation = constellation;
        }

        public int getBirthAttrib() {
            return birthAttrib;
        }

        public void setBirthAttrib(int birthAttrib) {
            this.birthAttrib = birthAttrib;
        }

        public int getBloodType() {
            return bloodType;
        }

        public void setBloodType(int bloodType) {
            this.bloodType = bloodType;
        }

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public int getMarital() {
            return marital;
        }

        public void setMarital(int marital) {
            this.marital = marital;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getEasemobId() {
            return easemobId;
        }

        public void setEasemobId(String easemobId) {
            this.easemobId = easemobId;
        }
    }
}
