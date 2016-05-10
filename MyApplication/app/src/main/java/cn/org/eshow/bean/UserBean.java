package cn.org.eshow.bean;

import cn.org.eshow_structure.util.ESStrUtil;

/**
 * Created by daikting on 16/3/14.
 */
public class UserBean {
    private String username = "";
    private String nickname = "";
    private String photo = "";
    private String realname = "";
    private String age = "";
    private boolean male = true;

    private String email = "";
    private String birthday = "";
    private String intro = "";


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if(ESStrUtil.isEmpty(username)){
            return;
        }
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        if(ESStrUtil.isEmpty(nickname)){
            return;
        }
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        if(ESStrUtil.isEmpty(photo)){
            return;
        }
        this.photo = photo;
    }

    public String getRealname() {

        return realname;
    }

    public void setRealname(String realname) {
        if(ESStrUtil.isEmpty(realname)){
            return;
        }
        this.realname = realname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        if(ESStrUtil.isEmpty(age)){
            return;
        }
        this.age = age;
    }

    public boolean isMale() {
        return male;
    }


    public String getSexStr(){
        if(male){
            return "男";
        }else{
            return "女";
        }

    }
    public void setMale(boolean male) {
        this.male = male;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(ESStrUtil.isEmpty(email)){
            return;
        }
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        if(ESStrUtil.isEmpty(birthday)){
            return;
        }
        this.birthday = birthday;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        if(ESStrUtil.isEmpty(intro)){
            return;
        }
        this.intro = intro;
    }
}
