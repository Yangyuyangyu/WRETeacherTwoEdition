package com.teacherhelp.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.teacherhelp.R;
import com.teacherhelp.adapter.MapSearchAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.bean.AddressBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.Utility;
import com.teacherhelp.utils.bdlocation.BaiduLocationUtil;
import com.teacherhelp.view.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 名称:
 * 作用：
 * Created on 2016/7/29.
 * 创建人：WangHaoMiao
 */
public class AddressNewActivity extends BaseActivity implements
        OnGetSuggestionResultListener, OnGetPoiSearchResultListener {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.keyWorldsView)
    ClearEditText keyWorldsView;
    @Bind(R.id.mapview)
    MapView mapView;
    @Bind(R.id.iv_fresh)
    ImageView iv_fresh;
    @Bind(R.id.btn_MyLoc)
    Button btn_MyLoc;
    @Bind(R.id.listview_mapsearch)
    ListView listview_mapsearch;

    private SuggestionSearch mSuggestionSearch = null;
    private BaiduMap mBaiduMap = null;

    private double latitude = 0;
    private double longtitude = 0;
    private String nowCity = "";
    private String address = "";// 获取反编码的地址
    private LatLng cur_point = null;
    private UiSettings mUiSettings;
    private GeoCoder mSearch = null;
    private boolean flag = true;//用于是否要调用搜索建议的标志
    private String fromcity = "";
    private String fromdistrict = "";
    private List<AddressBean> list = null;
    private Marker marker = null;

    /*******
     * 百度定位
     ********/
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;
    private PoiSearch mPoiSearch = null;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_addresstwo);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
        btn_MyLoc.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitleright.setText("确定");
        tvTitletwo.setText("家庭地址");
        iv_fresh.setVisibility(View.GONE);
        mBaiduMap = mapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
        mBaiduMap.setMyLocationEnabled(true);

        // 初始化搜索模块，注册搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(geoCoderListener);

        keyWorldsView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    InputMethodManager imm = (InputMethodManager) v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
                                0);
                    }
                    flag = true;
                    String city = Utility.StringIsNull(nowCity) ? fromcity + fromdistrict : nowCity;
                    mSearch.geocode(new GeoCodeOption().city(city).address(
                            keyWorldsView.getText().toString()));
                    listview_mapsearch.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }
        });

        keyWorldsView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                flag = false;
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
                // TODO Auto-generated method stub
                iv_fresh.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                // TODO Auto-generated method stub
                LatLng ptCenter = mBaiduMap.getMapStatus().target;
                cur_point = ptCenter;
                reverseGeoCode();
                updateMapState();
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {
                // TODO Auto-generated method stub

            }
        });


        listview_mapsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                if (arg1 != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(arg1.getWindowToken(), 0);
                }
                nowCity = list.get(arg2).getCity();
                String address = "";
                if (!Utility.StringIsNull(list.get(arg2).getDistrict()))
                    address = list.get(arg2).getDistrict()
                            + list.get(arg2).getDetailAdress();
                else
                    address = list.get(arg2).getDetailAdress();
                flag = true;
                mSearch.geocode(new GeoCodeOption().city(
                        list.get(arg2).getCity()).address(address));
                keyWorldsView.setText(list.get(arg2).getDetailAdress());
                listview_mapsearch.setVisibility(View.GONE);
            }
        });

        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        keyWorldsView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                if (flag) {
                    return;
                }
                if (cs.length() <= 0) {
                    listview_mapsearch.setVisibility(View.GONE);
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                iv_fresh.setVisibility(View.VISIBLE);
                String city = "";
                city = Utility.StringIsNull(nowCity) ? fromcity + fromdistrict : nowCity;
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(cs.toString()).city(city));
            }
        });

        InitLocation();
    }


    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            listview_mapsearch.setVisibility(View.GONE);
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {

            list = new ArrayList<AddressBean>();
            for (PoiInfo info : result.getAllPoi()) {
                if (info.name != null) {
                    AddressBean bean = new AddressBean();
                    bean.setCity(info.city);
                    bean.setDistrict(info.city + info.address);
                    bean.setDetailAdress(info.name);
                    list.add(bean);
                }
            }
            if (list.size() > 0) {
                MapSearchAdapter adapter = new MapSearchAdapter(
                        AddressNewActivity.this, list);
                listview_mapsearch.setAdapter(adapter);
                listview_mapsearch.setVisibility(View.VISIBLE);
            } else {
                listview_mapsearch.setVisibility(View.GONE);
            }
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            list = new ArrayList<AddressBean>();
            for (CityInfo info : result.getSuggestCityList()) {
                if (info.city != null) {
                    AddressBean bean = new AddressBean();
                    bean.setCity("");
                    bean.setDistrict("");
                    bean.setDetailAdress(info.city);
                    list.add(bean);
                }
            }
            if (list.size() > 0) {
                MapSearchAdapter adapter = new MapSearchAdapter(
                        AddressNewActivity.this, list);
                listview_mapsearch.setAdapter(adapter);
                listview_mapsearch.setVisibility(View.VISIBLE);
            } else {
                listview_mapsearch.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            listview_mapsearch.setVisibility(View.GONE);
            return;
        }

        list = new ArrayList<AddressBean>();
        //先找出一条拥有城市数据的项目
        String tempCity = "";
        for (int i = 0; i < res.getAllSuggestions().size(); i++) {
            if (res.getAllSuggestions().get(i).key != null) {
                if (!Utility.StringIsNull(res.getAllSuggestions().get(i).city)) {
                    tempCity = res.getAllSuggestions().get(i).city;
                    break;
                }
            }
        }
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                if (!Utility.StringIsNull(info.city)) {
                    AddressBean bean = new AddressBean();
                    bean.setCity(info.city);
                    bean.setDetailAdress(info.key);
                    bean.setDistrict(info.city + info.district);
                    list.add(bean);
                } else {
                    if (!Utility.StringIsNull(tempCity)) {
                        AddressBean bean = new AddressBean();
                        bean.setCity(tempCity);
                        bean.setDetailAdress(info.key);
                        bean.setDistrict(tempCity + info.district);
                        list.add(bean);
                    }
                }

            }
        }
        if (list.size() > 0) {
            MapSearchAdapter adapter = new MapSearchAdapter(
                    AddressNewActivity.this, list);
            listview_mapsearch.setAdapter(adapter);
            listview_mapsearch.setVisibility(View.VISIBLE);
        } else {
            listview_mapsearch.setVisibility(View.GONE);
        }
        iv_fresh.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
            case R.id.tv_titleright:
                Intent i = new Intent();
                i.putExtra("address", keyWorldsView.getText().toString());
                setResult(21, i);
                finish();
                break;
            case R.id.btn_MyLoc:
                InitLocation();
                break;
        }
    }


    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null) {
                return;
            }
            try {
                longtitude = location.getLongitude();
                latitude = location.getLatitude();
                nowCity = location.getCity();

                if (location.getDistrict() != null
                        && location.getStreet() != null)
                    address = location.getDistrict() + location.getStreet();
                if (longtitude != 0 && latitude != 0) {
                    mLocationClient.stop();
                }
                cur_point = new LatLng(latitude, longtitude);
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16.0f));
                mBaiduMap.setMapStatus(MapStatusUpdateFactory
                        .newLatLng(cur_point));
//                updateMapState();
                iv_fresh.setVisibility(View.GONE);
                keyWorldsView.setText(address);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新地图状态
     */
    private void updateMapState() {
//		// 初始化地图
        mBaiduMap.clear();
//		/*************** 获取地理编码结果 ******************/
//		mBaiduMap.addOverlay(new MarkerOptions().position(cur_point)
//				.icon(BitmapDescriptorFactory
//						.fromResource(R.drawable.track_map_icon)));
    }

    /**
     * 地理编码检索监听
     */
    private OnGetGeoCoderResultListener geoCoderListener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有检索到结果
//				showToastLoad(MapSearchActivity.this, "未找到结果,请加上市名或者区名称试试(例：成都市XX路)");
                return;
            }

            // 定义Maker坐标点
            address = result.getAddress();
            cur_point = new LatLng(result.getLocation().latitude,
                    result.getLocation().longitude);
            mBaiduMap.clear();
            mBaiduMap.addOverlay(new MarkerOptions().position(
                    result.getLocation()).icon(
                    BitmapDescriptorFactory
                            .fromResource(R.mipmap.learn_signin_location_icon)));
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                    .getLocation()));
            updateMapState();
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有找到检索结果
                iv_fresh.setVisibility(View.GONE);
                keyWorldsView.setText("");
            }
            nowCity = result.getAddressDetail().city;
            iv_fresh.setVisibility(View.GONE);
            if (result != null && result.getPoiList() != null && result.getPoiList().size() > 0 && result.getPoiList() != null)
                address = result.getPoiList().get(0).name;
            else {
                address = result.getAddress();
            }
            keyWorldsView.setText(address);
        }
    };

    /**
     * 更新地图状态
     */
    private void reverseGeoCode() {
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(cur_point));
    }

    /**
     * 定位当前位置
     */
    public void InitLocation() {
        iv_fresh.setVisibility(View.VISIBLE);
        mLocationClient = new LocationClient(this);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        BaiduLocationUtil.getInstance().getLocation(getApplicationContext(), mMyLocationListener);
    }

    @Override
    protected void onDestroy() {
        if (mSearch != null)
            mSearch.destroy();
        if (marker != null)
            marker.remove(); // 调用Marker对象的remove方法实现指定marker的删除
        if (mSuggestionSearch != null)
            mSuggestionSearch.destroy();
        if (mPoiSearch != null)
            mPoiSearch.destroy();
        super.onDestroy();
        // 退出时销毁定位
        mLocationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mapView = null;

        BaiduLocationUtil.getInstance().Unregister(mMyLocationListener);
    }
}
