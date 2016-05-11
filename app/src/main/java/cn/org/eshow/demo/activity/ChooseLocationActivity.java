package cn.org.eshow.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.org.eshow.demo.R;
import cn.org.eshow.demo.common.CommonActivity;
import cn.org.eshow.demo.common.Global;
import cn.org.eshow.demo.fragment.AroundPlaceFragment;
import cn.org.eshow.demo.fragment.AroundPlaceSlidingFragment;
import cn.org.eshow.demo.view.LoginAutoCompleteEdit;
import cn.org.eshow_framwork.util.AbLogUtil;
import cn.org.eshow_framwork.util.AbViewUtil;

/**
 * 地图选点页面
 * Created by daikting on 16/3/17.
 */
@EActivity(R.layout.activity_chooselocation)
public class ChooseLocationActivity extends CommonActivity implements LocationSource,AMapLocationListener,AMap.OnCameraChangeListener{

    private Context mContext = ChooseLocationActivity.this;
    @ViewById(R.id.rlBack)
    RelativeLayout rlBack;
    @ViewById(R.id.material_back_button)
    MaterialMenuView material_back_button;
    @ViewById(R.id.etSearch)
    LoginAutoCompleteEdit etSearch;
    @ViewById(R.id.mvMap)
    MapView mvMap;
    private AMap aMap;
    @ViewById(R.id.content_frame)
    FrameLayout content_frame;
    Bundle savedInstanceState;

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @AfterViews
    void init() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        material_back_button.setState(MaterialMenuDrawable.IconState.ARROW);

        initMap();

    }


    private void initMap() {
        mvMap.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mvMap.getMap();
            AbLogUtil.i(mContext, "最大缩放比例：" + aMap.getMaxZoomLevel() + "，最小缩放比例：" + aMap.getMinZoomLevel());
            // 自定义系统定位小蓝点
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
            myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
            myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
            myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细

            aMap.setMyLocationStyle(myLocationStyle);
            aMap.setLocationSource(this);
            aMap.getUiSettings().setZoomControlsEnabled(false);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setOnCameraChangeListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mvMap.onDestroy();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mvMap.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mvMap.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvMap.onSaveInstanceState(outState);
    }
    @Click(R.id.rlBack)
    void onBack() {
        finish();
    }

    @Click(R.id.etSearch)
    void onSearch(){
        SearchLocationActivity_.intent(mContext).extra(SearchLocationActivity_.INTENT_CITYNAME,"").start();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        finish();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        AbLogUtil.i(mContext, "onLocationChanged ");
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                String aoiname = aMapLocation.getAoiName();
                double lat = aMapLocation.getLatitude();
                double lon = aMapLocation.getLongitude();
                String location = changeLocationStr(lon,lat);
                showAround(location);
                AbLogUtil.d(mContext,"location:"+location);
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 合并经纬度到字符串
     * @param lon
     * @param lat
     * @return
     */
    private String changeLocationStr(double lon,double lat){
        String lonStr = String.valueOf(lon);
        String latStr = String.valueOf(lat);
        String result = lonStr+","+latStr;
        return result;
    }

    /**
     * 显示地图下方周围的信息
     * @param location
     */
    private void showAround(String location){
        AroundPlaceSlidingFragment aroundPlaceSlidingFragment = new AroundPlaceSlidingFragment();
        aroundPlaceSlidingFragment.setLocation(location);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, aroundPlaceSlidingFragment)
                .commit();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng latLng = cameraPosition.target;
        double lat = latLng.latitude;
        double lon = latLng.longitude;
        String locationStr = changeLocationStr(lon,lat);
        AbLogUtil.d(mContext, "移动后的坐标：" + locationStr);

        Intent locationChange = new Intent();
        locationChange.setAction(Global.EShow_Broadcast_Action.ACTION_LOCATION_CHANGED);
        locationChange.putExtra(AroundPlaceFragment.INTENT_CURRENT_LOCATION,locationStr);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(locationChange);
    }
}
