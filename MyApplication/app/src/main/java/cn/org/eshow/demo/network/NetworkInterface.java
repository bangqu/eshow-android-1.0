package cn.org.eshow.demo.network;

import android.content.Context;
import android.util.Log;

import cn.org.eshow.demo.bean.Enum_CodeType;
import cn.org.eshow.demo.bean.Enum_ThirdType;
import cn.org.eshow.demo.common.Global;
import cn.org.eshow.demo.common.SharedPrefUtil;
import cn.org.eshow.http.ESRequestParams;

/**
 * 网络接口
 * Created by daikting on 15/10/12.
 */
public class NetworkInterface {
    /**
     * 登录
     *
     * @param context
     * @param userName
     * @param password
     * @param responseListener
     */
    public static void login(Context context, String userName, String password, ESResponseListener responseListener) {
        ESRequestParams abRequestParams = new ESRequestParams();
        abRequestParams.put("user.username", userName);
        abRequestParams.put("user.password", password);
        new MyHttpUtil(context).post("user/login", abRequestParams, responseListener);
    }

    /**
     * 注册、找回密码时发送短信验证码
     * @param context
     * @param userName
     * @param responseListener
     */
    public static void sendCode(Context context,String userName,Enum_CodeType type,ESResponseListener responseListener){
        ESRequestParams abRequestParams = new ESRequestParams();
        abRequestParams.put("user.username", userName);
        abRequestParams.put("type",type.toString());
        new MyHttpUtil(context).post("user/check", abRequestParams, responseListener);
    }

    /**
     * 注册
     * @param context
     * @param userName
     * @param code
     * @param password
     * @param responseListener
     */
    public static void regist(Context context,String userName,String code,String password,ESResponseListener responseListener){
        ESRequestParams abRequestParams = new ESRequestParams();
        abRequestParams.put("user.username", userName);
        abRequestParams.put("code",code);
        abRequestParams.put("user.password",password);
        new MyHttpUtil(context).post("user/signup", abRequestParams, responseListener);
    }

    /**
     * 重置密码
     * @param context
     * @param userName
     * @param code
     * @param password
     * @param responseListener
     */
    public static void rePassword(Context context,String userName,String code,String password,ESResponseListener responseListener){
        ESRequestParams abRequestParams = new ESRequestParams();
        abRequestParams.put("user.username", userName);
        abRequestParams.put("code",code);
        abRequestParams.put("user.password",password);
        new MyHttpUtil(context).post("user/password", abRequestParams, responseListener);
    }

    /**
     * 语音播报
     * @param context
     * @param mobile
     * @param enum_codeType
     * @param responseListener
     */
    public static void voice(Context context,String mobile,Enum_CodeType enum_codeType,ESResponseListener responseListener){
        ESRequestParams abRequestParams = new ESRequestParams();
        abRequestParams.put("mobile", mobile);
        abRequestParams.put("type",enum_codeType.toString());
        new MyHttpUtil(context).post("code/voice", abRequestParams, responseListener);
    }

    /**
     * 获取七牛token
     *
     * @param context
     * @param responseListener
     */
    public static void getQiniuToken(Context context, ESResponseListener responseListener) {
        ESRequestParams abRequestParams = new ESRequestParams();
        new MyHttpUtil(context).post("qiniu/uptoken", abRequestParams, responseListener);
    }

    /**
     * 获取七牛Key
     */
    public static void getQiniuKey(Context context, ESResponseListener responseListener) {
        ESRequestParams abRequestParams = new ESRequestParams();
        new MyHttpUtil(context).post("qiniu/key", abRequestParams, responseListener);
    }

    /**
     * 第三放登录
     * @param context
     * @param token
     * @param type 平台类型
     * @param responseListener
     */
    public static void thirdLogin(Context context,String token,Enum_ThirdType type,ESResponseListener responseListener){
        Log.i("NetworkInterface","第三放登录请求地址："+ Global.SERVER_URL+"user/third.json?thirdParty.platform="+type.toString()+"&thirdParty.username="+token+"&from=android");

        ESRequestParams abRequestParams = new ESRequestParams();
        abRequestParams.put("thirdParty.platform", type.toString());
        abRequestParams.put("thirdParty.username", token);
        new MyHttpUtil(context).post("user/third", abRequestParams, responseListener);
    }

    /**
     * 绑定第三方账号到手机号
     * @param context
     * @param userName
     * @param token
     * @param responseListener
     */
    public static void thirdBound(Context context,String userName,String token,ESResponseListener responseListener){
        ESRequestParams abRequestParams = new ESRequestParams();
        abRequestParams.put("user.username", userName);
        abRequestParams.put("thirdParty.username",token);
        new MyHttpUtil(context).post("user/mobile", abRequestParams, responseListener);
    }

    /**
     * 请求高德地图获取周边的小区、写字楼\产业园区、学校、
     * @param context
     * @param location
     * @param types
     * @param responseListener
     */
    public static void placeAround(Context context,String location,String types,AMapResponseListener responseListener){
        ESRequestParams esRequestParams = new ESRequestParams();
        esRequestParams.put("key", "b9410630740ff6a90c303b4bfdfef1ef");
        esRequestParams.put("location",location);
        esRequestParams.put("output","json");
        esRequestParams.put("radius","1000");
        esRequestParams.put("types",types);
        new AMapHttpUtil(context).post("v3/place/around",esRequestParams,responseListener);
    }

    /**
     * 请求高德地图根据关键词搜索
     * @param context
     * @param searchStr
     * @param city
     * @param responseListener
     */
    public static void searchPlace(Context context,String searchStr,String city,AMapResponseListener responseListener){
        ESRequestParams esRequestParams = new ESRequestParams();
        esRequestParams.put("key", "b9410630740ff6a90c303b4bfdfef1ef");
        esRequestParams.put("keywords",searchStr);
        esRequestParams.put("city",city);
        esRequestParams.put("citylimit","true");
        esRequestParams.put("output","json");
        esRequestParams.put("offset","10");
        esRequestParams.put("page","1");
        esRequestParams.put("extensions","base");
        esRequestParams.put("types","写字楼|产业园区|小区|学校");
        new AMapHttpUtil(context).post("v3/place/text",esRequestParams,responseListener);
    }

    /**
     * 获取绑定第三方账号状态
     * @param context
     * @param responseListener
     */
    public static void checkThirdBound(Context context,ESResponseListener responseListener){
        ESRequestParams abRequestParams = new ESRequestParams();
        String token = SharedPrefUtil.getAccessToken(context);
        abRequestParams.put("accessToken", token);
        new MyHttpUtil(context).post("third-party/check", abRequestParams, responseListener);
    }

    /**
     * 取消绑定第三方账号状态
     * @param context
     * @param responseListener
     */
    public static void clearnThirdBound(Context context,Enum_ThirdType type,ESResponseListener responseListener){
        ESRequestParams abRequestParams = new ESRequestParams();
        String token = SharedPrefUtil.getAccessToken(context);
        abRequestParams.put("accessToken", token);
        abRequestParams.put("thirdParty.platform",type.toString());
        new MyHttpUtil(context).post("user/cancel", abRequestParams, responseListener);
    }

    /**
     * 绑定第三方账号
     * @param context
     * @param type
     * @param thirdToken
     * @param responseListener
     */
    public static void thirdBound(Context context,Enum_ThirdType type,String thirdToken,ESResponseListener responseListener){
        ESRequestParams abRequestParams = new ESRequestParams();
        abRequestParams.put("accessToken", SharedPrefUtil.getAccessToken(context));
        abRequestParams.put("thirdParty.platform",type.toString());
        abRequestParams.put("thirdParty.username",thirdToken);
        new MyHttpUtil(context).post("third-party/save", abRequestParams, responseListener);
    }

    /**
     * 刷新用户信息
     * @param context
     * @param responseListener
     */
    public static void refreshUserInfo(Context context,ESResponseListener responseListener){
        ESRequestParams abRequestParams = new ESRequestParams();
        abRequestParams.put("user.username", SharedPrefUtil.getUser(context).getUsername());
        new MyHttpUtil(context).post("user/view", abRequestParams, responseListener);
    }

}
