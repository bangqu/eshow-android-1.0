package cn.org.eshow.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.bangqu.bean.CityBean;
import com.bangqu.utils.CharacterParser;
import com.bangqu.utils.PinyinComparator;
import com.bangqu.utils.StringUtil;
import com.bangqu.utils.ToastUtil;
import com.bangqu.view.ClearEditText;
import com.bangqu.view.SideBar;
import com.sina.weibo.sdk.utils.LogUtil;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.google.gson.reflect.TypeToken;
import cn.org.eshow.demo.R;
import cn.org.eshow.demo.adapter.CitySortAdapter;
import cn.org.eshow.demo.common.CommonActivity;
import cn.org.eshow.demo.common.SharedPrefUtil;
import cn.org.eshow.framwork.util.AbJsonUtil;
import cn.org.eshow.framwork.util.AbViewUtil;

public class CityChangeActivity extends CommonActivity implements
        OnClickListener, OnItemClickListener, AMapLocationListener {
    protected View progressbar;
    // 定位城市
    private TextView btnGPSCity;
    private LinearLayout llGPSCity;
    public double lat;
    public double lng;
    private String cityName = "正在定位...";
    private static final String cityJsonFile = "city.json";
    private static final String cityHotJsonFile = "city_hot.json";
    private LocationManager aMapLocManager = null;
    private AMapLocation aMapLocation;// 用于判断定位超时
    private Handler startHandler;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    private List<CityBean> hotcityList = new ArrayList<CityBean>();

    // 全部城市列表
    private List<CityBean> cityList = new ArrayList<CityBean>();
    // 城市历史列表
    private List<CityBean> cityListHis = new ArrayList<CityBean>();
    private View hotView;

    /**
     * 汉字转换成拼音的类
     */
    private List<CityBean> SourceDateList;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private SideBar sideBar;
    private TextView dialog;
    private CitySortAdapter adapter;
    private ClearEditText mClearEditText;
    // 数据库缓存
    private LinearLayout llNoEdit;

    private ListView sortListView;
    private View headview;
    private GridView mGrid;
    private List<String> hotCityNames = new ArrayList<>();

    // 标题栏
    private TextView mTitleView;
    RelativeLayout rlBack;
    MaterialMenuView materialBackButton;

    private Boolean isSelectCity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏视图
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_change_layout);
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.llParent));
        isSelectCity = getIntent().getBooleanExtra("isSelectCity", false);
        characterParser = new CharacterParser();
        findView();
        startLocation();
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

    public void findView() {


        progressbar = (View) findViewById(R.id.progressbar);
        cityListHis = SharedPrefUtil.getCityHis(this);
        cityList = SharedPrefUtil.getCitys(this);
        if (null == cityList || cityList.size() == 0) {
            cityList = (List<CityBean>) AbJsonUtil.fromJson(getJson(CityChangeActivity.this, cityJsonFile), new TypeToken<ArrayList<CityBean>>() {
            });
        }

        if (null == hotcityList || hotcityList.size() == 0) {
            hotcityList = (List<CityBean>) AbJsonUtil.fromJson(getJson(CityChangeActivity.this, cityHotJsonFile), new TypeToken<ArrayList<CityBean>>() {
            });
        }
        System.out.println("hotcityList" + hotcityList.size());
        for (CityBean cityBean : hotcityList) {
            hotCityNames.add(cityBean.getName());
            System.out.println("hotcityList" + cityBean.getName());
        }
        // 标题栏
        mTitleView = (TextView) findViewById(R.id.tvTitle);
        mTitleView.setText("城市选择");

        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        rlBack.setOnClickListener(this);
        materialBackButton = (MaterialMenuView) findViewById(R.id.material_back_button);
        materialBackButton.setState(MaterialMenuDrawable.IconState.ARROW);
        rlBack.setOnClickListener(this);

        if (!StringUtil.isBlank(SharedPrefUtil.getgpscity(this))) {
            cityName = SharedPrefUtil.getgpscity(this);
        }

        llNoEdit = (LinearLayout) findViewById(R.id.llNoEdit);

        sortListView = (ListView) findViewById(R.id.lvSvCityList);

        headview = getLayoutInflater().inflate(R.layout.include_location_title,
                null);
        hotView = headview.findViewById(R.id.hotView);
        mGrid = (GridView) hotView.findViewById(R.id.mGrid);
        mGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item_hot_city, R.id.hotCity, hotCityNames));
        mGrid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                addCityToHis(hotcityList.get(position));
                Intent intent = new Intent();
                intent.putExtra("cityName", hotCityNames.get(position));
                setResult(RESULT_OK, intent);
            }
        });
        calGridViewWidthAndHeigh(3, mGrid);
        llNoEdit = (LinearLayout) headview.findViewById(R.id.llNoEdit);
        sortListView.addHeaderView(headview, null, true);

        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                try {
                    searchData(s.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // 定位城市
        btnGPSCity = (TextView) findViewById(R.id.btnGPSCity);
        llGPSCity = (LinearLayout) findViewById(R.id.llGPSCity);
        llGPSCity.setOnClickListener(this);
        btnGPSCity.setOnClickListener(this);
        btnGPSCity.setText(cityName);

        startHandler = new Handler();
        // 实例化汉字转拼音类
        pinyinComparator = new PinyinComparator();
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                if ("当前".equals(s)) {
                    hotView.setVisibility(View.VISIBLE);
                    fillData(cityList, false);
                } else if ("最近".equals(s)) {
                    hotView.setVisibility(View.GONE);
                    fillData(cityListHis, false);
                } else if ("热门".equals(s)) {
                    hotView.setVisibility(View.GONE);
                    fillData(hotcityList, false);
                } else {
                    // 该字母首次出现的位置
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        sortListView.setSelection(position + 1);
                    }
                }
            }
        });
//        if (!SharedPrefUtil.isFistDatabase(this)) {
        fillData(cityList, true);
        if (null == hotcityList || hotcityList.size() == 0) {
            hotView.setVisibility(View.GONE);
        } else {
            hotView.setVisibility(View.VISIBLE);
        }
//            progressbar.setVisibility(View.GONE);
//        }
    }

    public void fillData(List<CityBean> list, boolean isSave) {
        // 根据a-z进行排序源数据
        SourceDateList = filledData(list);
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new CitySortAdapter(CityChangeActivity.this, SourceDateList, onClick);
        sortListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (isSave) {
            cityList.clear();
            cityList.addAll(SourceDateList);
            SharedPrefUtil.setCitys(this, SourceDateList);
        }
    }


    /**
     * 为sortModel填充数据
     *
     * @param cityList2
     * @author zcx
     */
    public List<CityBean> filledData(List<CityBean> cityList2) {
        List<CityBean> mSortList = new ArrayList<CityBean>();

        for (CityBean city : cityList2) {
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(city.getName());
            city.setPinyin(pinyin);

            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                city.setSortLetters(sortString.toUpperCase());
            } else {
                city.setSortLetters("#");
            }
            mSortList.add(city);
        }
        return mSortList;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void searchData(String filterStr) throws Exception {
        List<CityBean> searchDateList = new ArrayList<CityBean>();

        if (TextUtils.isEmpty(filterStr)) {
            llNoEdit.setVisibility(View.VISIBLE);
            searchDateList = cityList;
        } else {
            llNoEdit.setVisibility(View.GONE);
            for (CityBean city : cityList) {
                String name = city.getName();
                String pinyin = city.getPinyin();
                if (name.contains(filterStr.toString())
                        || (pinyin.contains(filterStr.toString()))) {
                    searchDateList.add(city);
                }
            }
        }
        Collections.sort(searchDateList, pinyinComparator);
        adapter = new CitySortAdapter(CityChangeActivity.this, searchDateList, onClick);
        sortListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlBack:
                finish();
                break;

            case R.id.btnGPSCity:
                if (!cityName.equals("正在定位...")) {
                    if (isSelectCity) {
                        Intent intent = new Intent();
                        intent.putExtra("cityName", cityName);
                        setResult(RESULT_OK, intent);
                    } else {
//                        Intent intent0 = new Intent(CityChangeActivity.this,
//                                LocationListActivity.class);
//                        SharedPrefUtil.setCityName(CityChangeActivity.this,
//                                cityName);
//                        intent0.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        intent0.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent0);
                    }
                    CityChangeActivity.this.finish();
                } else {
                    ToastUtil.showShort(CityChangeActivity.this, "正在定位中...");
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position,
                            long arg3) {

    }

    @Override
    protected void onDestroy() {
        stopLocation();
        super.onDestroy();

    }

    public void run() {
        if (aMapLocation == null) {
            stopLocation();// 销毁掉定位
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocation();// 停止定位
    }

    /**
     * 销毁定位
     */
    private void stopLocation() {
        if (aMapLocManager != null) {

        }
        aMapLocManager = null;
    }


    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    AMapLocationClient mlocationClient;

    void startLocation() {
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mlocationClient = new AMapLocationClient(this);
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();

    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null) {
            if (location.getErrorCode() == 0) {
                this.aMapLocation = location;// 判断超时机制
                if (!StringUtil.isBlank(location.getCity() + "")) {
                    cityName = location.getCity() + "";
                    cityName = cityName.substring(0, cityName.length() - 1);
                    SharedPrefUtil.setGpscity(CityChangeActivity.this, cityName);
                    btnGPSCity.setText(cityName);
//                    btnGPSCity.setTextColor(Color.RED);
                }
                stopLocation();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + location.getErrorCode() + ", errInfo:"
                        + location.getErrorInfo());
            }
        }
    }

    void addCityToHis(CityBean cityBean) {
        List<CityBean> list = new ArrayList<>();
        list.addAll(cityListHis);
        for (int i = 0; i < cityListHis.size(); i++) {
            if (i < list.size() && list.get(i).getCityID() == cityBean.getCityID()) {
                list.remove(i);
            }
        }
        list.add(cityBean);
        cityListHis = list;
        SharedPrefUtil.setCityHis(CityChangeActivity.this, list);
    }

    /**
     * 计算GridView宽高
     *
     * @param gridView
     */
    public void calGridViewWidthAndHeigh(int numColumns, GridView gridView) {

        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
//        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0); // 计算子项View 的宽高

        int j = listAdapter.getCount() / numColumns;
        if (listAdapter.getCount() % numColumns > 0) {
            j = j + 2;
        }
        totalHeight = j * listItem.getMeasuredHeight() + listItem.getMeasuredHeight() / 2;
//        totalHeight += 40;

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        gridView.setLayoutParams(params);
    }

    CitySortAdapter.OnClick onClick = new CitySortAdapter.OnClick() {
        @Override
        public void onClick(CityBean cityBean) {
            addCityToHis(cityBean);
        }
    };
}