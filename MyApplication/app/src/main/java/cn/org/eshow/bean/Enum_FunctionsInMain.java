package cn.org.eshow.bean;
import cn.org.eshow.R;

/**
 * 首页上功能列表项
 * Created by daikting on 16/3/14.
 */
public enum Enum_FunctionsInMain {

    FROM("信息表单", R.drawable.main_function_1),
    IMAGE("图片列表",R.drawable.main_function_2),
    DOWNLOAD("文件下载",R.drawable.main_function_3),
    CITY("城市选择",R.drawable.main_function_4),
    MUSIC("音乐播放",R.drawable.main_function_5),
    MAP("地图",R.drawable.main_function_6),
    PAY("支付",R.drawable.main_function_7),
    SHARE("分享",R.drawable.main_function_8),
    CHAT("聊天",R.drawable.main_function_9),
    BLUETOOTH("蓝牙",R.drawable.main_function_9);

    private String platformType;
    private int drawableId;
    Enum_FunctionsInMain(String platformType,int drawableId) {
        this.platformType = platformType;
        this.drawableId = drawableId;
    }

    @Override
    public String toString() {
        return this.platformType;
    }

    public int getDrawableId(){
        return this.drawableId;
    }
}
