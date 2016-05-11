package cn.org.eshow.demo.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.google.gson.reflect.TypeToken;
import cn.org.eshow.demo.R;
import cn.org.eshow.demo.adapter.AreaWheelAdapter;
import cn.org.eshow.demo.adapter.CityWheelAdapter;
import cn.org.eshow.demo.adapter.ProviceWheelAdapter;
import cn.org.eshow.demo.bean.AreaBean;
import cn.org.eshow.demo.bean.CityBean;
import cn.org.eshow.demo.bean.ProviceBean;
import cn.org.eshow_framwork.util.AbJsonUtil;
import cn.org.eshow_framwork.util.AbViewUtil;
import cn.org.eshow_framwork.view.wheel.AbWheelView;


/**
 * 省、市、区选择对话框
 */
public class ChooseCityDialog extends Dialog {
    private static final String proviceJsonFile = "provice.json";
    private static final String cityJsonFile = "city.json";
    private static final String areaJsonFile = "area.json";
    List<ProviceBean> provices = null;
    List<CityBean> cities = null;
    List<AreaBean> areas = null;

    List<CityBean> tempCities = null;
    List<AreaBean> tempAreas = null;

    Context context;

    LinearLayout llParent;
    TextView tvFinish;
    AbWheelView wvProvince;
    AbWheelView wvCity;
    AbWheelView wvArea;

    ProviceWheelAdapter proviceWheelAdapter;
    CityWheelAdapter cityWheelAdapter;
    AreaWheelAdapter areaWheelAdapter;

    public ChooseCityDialog(Context context) {
        super(context, R.style.confirm_dialog);
        this.context = context;
        new DataThread(context, handler).start();

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(provices.size() > 0){
                wvProvince.setAdapter(new ProviceWheelAdapter(provices));
                ProviceBean proviceBean = provices.get(0);
                chooseCity(proviceBean);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choosecity);
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        getWindow().setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        getWindow().setWindowAnimations(R.style.ShowShareDialogAni);  //添加动画
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        llParent = (LinearLayout) findViewById(R.id.llParent);
        tvFinish = (TextView) findViewById(R.id.tvFinish);
        tvFinish.setOnClickListener(clickListener);
        wvProvince = (AbWheelView) findViewById(R.id.wvProvince);
        wvProvince.setValueTextSize(32);
        wvCity = (AbWheelView) findViewById(R.id.wvCity);
        wvCity.setValueTextSize(32);
        wvArea = (AbWheelView) findViewById(R.id.wvArea);
        wvArea.setValueTextSize(32);

        tempCities = new ArrayList<CityBean>();
        tempAreas  = new ArrayList<AreaBean>();

        wvProvince.addChangingListener(new AbWheelView.AbOnWheelChangedListener() {
            @Override
            public void onChanged(AbWheelView wheel, int oldValue, int newValue) {
                ProviceBean proviceBean = provices.get(newValue);
                chooseCity(proviceBean);
            }
        });
        wvCity.addChangingListener(new AbWheelView.AbOnWheelChangedListener() {
            @Override
            public void onChanged(AbWheelView wheel, int oldValue, int newValue) {
                CityBean cityBean = tempCities.get(newValue);
                chooseArea(cityBean);
            }
        });
        wvArea.addChangingListener(new AbWheelView.AbOnWheelChangedListener() {
            @Override
            public void onChanged(AbWheelView wheel, int oldValue, int newValue) {

            }
        });
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvFinish:
                    if(onFinishListener != null){
                        int indexProvice = wvProvince.getCurrentItem();
                        ProviceBean proviceBean = provices.get(indexProvice);
                        int indexCity = wvCity.getCurrentItem();
                        CityBean cityBean = tempCities.get(indexCity);
                        int indexArea = wvArea.getCurrentItem();
                        AreaBean areaBean = tempAreas.get(indexArea);
                        onFinishListener.onFinishListener(proviceBean,cityBean,areaBean);
                    }
                    dismiss();
                    break;
            }
        }
    };

    class DataThread extends Thread {
        Context context;
        Handler handler;

        public DataThread(Context context, Handler handler) {
            this.handler = handler;
            this.context = context;
        }

        @Override
        public void run() {
            provices = (List<ProviceBean>) cn.org.eshow_framwork.util.AbJsonUtil.fromJson(getJson(context, proviceJsonFile), new TypeToken<ArrayList<ProviceBean>>() {
            });
            cities = (List<CityBean>) AbJsonUtil.fromJson(getJson(context, cityJsonFile), new TypeToken<ArrayList<CityBean>>() {
            });
            areas = (List<AreaBean>) AbJsonUtil.fromJson(getJson(context, areaJsonFile), new TypeToken<ArrayList<AreaBean>>() {
            });
            handler.sendMessage(handler.obtainMessage());
        }

        String getJson(Context context, String fileName) {

            StringBuilder stringBuilder = new StringBuilder();
            try {
                AssetManager assetManager = context.getAssets();
                BufferedReader bf = new BufferedReader(new InputStreamReader(
                        assetManager.open(fileName)));
                String line;
                while ((line = bf.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }
    }

    private void chooseCity(ProviceBean proviceBean){
        tempCities.clear();
        for(CityBean cityBean:cities){
            if(proviceBean.getProID() == cityBean.getProID()){
                tempCities.add(cityBean);
            }
        }
        wvCity.setAdapter(new CityWheelAdapter(tempCities));

        if(tempCities.size() > 0){
            CityBean cityBean = tempCities.get(0);
            chooseArea(cityBean);
        }else{
            wvArea.setAdapter(new AreaWheelAdapter(new ArrayList<AreaBean>()));
        }
    }

    private void chooseArea(CityBean cityBean){
        tempAreas.clear();
        for(AreaBean areaBean:areas){
            if(cityBean.getCityID() == areaBean.getCityID()){
                tempAreas.add(areaBean);
            }
        }
        wvArea.setAdapter(new AreaWheelAdapter(tempAreas));
    }

    OnFinishListener onFinishListener;
    public void setOnFinishListener(OnFinishListener onFinishListener){
        this.onFinishListener = onFinishListener;
    }

    public interface  OnFinishListener {
        public void onFinishListener(ProviceBean proviceBean,CityBean cityBean,AreaBean areaBean);
    }
}
