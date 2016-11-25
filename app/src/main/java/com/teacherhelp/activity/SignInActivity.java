package com.teacherhelp.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.bean.AddressBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;
import com.teacherhelp.utils.Utility;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 名称:打卡页面
 * 作用：用于教师打卡
 * Created on 2016/7/27.
 * 创建人：WangHaoMiao
 */
public class SignInActivity extends BaseActivity implements
        OnGetSuggestionResultListener, OnGetPoiSearchResultListener {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.rl_signin)
    RelativeLayout rlSignin;
    @Bind(R.id.OnClass)
    Button OnClass;
    @Bind(R.id.DownClass)
    Button DownClass;
    @Bind(R.id.mapview)
    MapView mapView;
    @Bind(R.id.btn_MyLoc)
    Button btn_MyLoc;

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
    private Marker marker = null;
    /*******
     * 百度定位
     ********/
    private LocationClient mLocationClient;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "bd09ll";
    private SignInActivity.MyLocationListener mMyLocationListener;
    private PoiSearch mPoiSearch = null;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_signin);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        rlSignin.setOnClickListener(this);
        OnClass.setOnClickListener(onClickListener);
        DownClass.setOnClickListener(onClickListener);
        btn_MyLoc.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitletwo.setText("打卡");

        InitLocation();
        mBaiduMap = mapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();

        // 初始化搜索模块，注册搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(geoCoderListener);

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
                // TODO Auto-generated method stub

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
    }


    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {

            for (PoiInfo info : result.getAllPoi()) {
                if (info.name != null) {
                    AddressBean bean = new AddressBean();
                    bean.setCity(info.city);
                    bean.setDistrict(info.city + info.address);
                    bean.setDetailAdress(info.name);

                }
            }

            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            for (CityInfo info : result.getSuggestCityList()) {
                if (info.city != null) {
                    AddressBean bean = new AddressBean();
                    bean.setCity("");
                    bean.setDistrict("");
                    bean.setDetailAdress(info.city);
                }
            }

        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }

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
                } else {
                    if (!Utility.StringIsNull(tempCity)) {
                        AddressBean bean = new AddressBean();
                        bean.setCity(tempCity);
                        bean.setDetailAdress(info.key);
                        bean.setDistrict(tempCity + info.district);
                    }
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back: //返回
                finish();
                saveEditTextAndCloseIMM();
                break;
            case R.id.rl_signin: //打卡记录
                startActivity(new Intent(SignInActivity.this, SignRecordActivity.class));
                break;
            case R.id.btn_MyLoc:
                InitLocation();
                break;
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.OnClass: //上班打卡

                    break;
                case R.id.DownClass: //下班打卡

                    break;

            }
        }
    };

    /**
     * 打卡
     */
    private void clockCard(String id, String type) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConfig.GET_PunchClock(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:

                        break;
                    default:
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CommonUtils.showToast("请求失败，请重试！");
                LogHelper.d(volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("type", "1上班 2下班");
                map.put("addr", "打卡地址");
                map.put("addrLat", "纬度");
                map.put("addrLng", "经度");
                map.put("ctime", "打卡时间");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", ShareReferenceUtils.getValue("myToken"));
                LogHelper.d("请求头:" + headers.toString());
                return headers;
            }
        };
        setTmie(stringRequest);
        stringRequest.setTag("volleyclockCard");
        MyApplication.getHttpQueue().add(stringRequest);
    }


    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

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
                updateMapState();
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
            }
            nowCity = result.getAddressDetail().city;
            if (result != null && result.getPoiList() != null && result.getPoiList().size() > 0 && result.getPoiList() != null)
                address = result.getPoiList().get(0).name;
            else {
                address = result.getAddress();
            }
            //获取到地址address
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
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new SignInActivity.MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// 设置定位模式
        option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
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
    }
}
