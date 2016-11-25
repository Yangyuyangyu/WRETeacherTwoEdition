package com.teacherhelp.activity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.utils.Utility;

import butterknife.Bind;

/**
 * 名称:家庭地址页面
 * 作用：设置用户家庭地址
 * Created on 2016/7/27.
 * 创建人：WangHaoMiao
 */
public class AddressActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.mapview)
    MapView mapview;

    private BaiduMap mBaiduMap;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_address);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
        tvTitleright.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        tvTitleright.setVisibility(View.VISIBLE);
        tvTitleright.setText("确定");
        tvTitletwo.setText("家庭地址");

        mBaiduMap = mapview.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //定义Maker坐标点
        mBaiduMap.setTrafficEnabled(false);
        mBaiduMap.setMyLocationEnabled(true);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);//设置地图的缩放比例
        mBaiduMap.setMapStatus(msu);//将前面的参数交给BaiduMap类
        mapview.showScaleControl(true);//是否显示比例尺控件
        mapview.showZoomControls(true);//是否显示缩放控件
        mapview.removeViewAt(1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                saveEditTextAndCloseIMM();
                break;
            case R.id.tv_titleright:
                break;
        }
    }

}
