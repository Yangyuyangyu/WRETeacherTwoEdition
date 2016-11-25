package com.teacherhelp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.activity.ShareActivity;
import com.teacherhelp.base.BaseFragment;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.zxing.android.CaptureActivity;

import butterknife.Bind;

/**
 * 消息Fragment
 * Created by Administrator on 2016/7/14.
 */
public class FindFragment extends BaseFragment {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView Title;
    @Bind(R.id.RichScan_rl)
    RelativeLayout RichScan;
    @Bind(R.id.Share_rl)
    RelativeLayout Share;

    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find, null);
    }

    @Override
    protected void initVariables() {
        imgbtnBack.setVisibility(View.GONE);
        Title.setVisibility(View.VISIBLE);
        Title.setText("发现");
    }

    @Override
    protected void addListener() {
        RichScan.setOnClickListener(this);
        Share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.RichScan_rl: //扫一扫
                startActivityForResult(new Intent(getActivity(), CaptureActivity.class),REQUEST_CODE_SCAN);
                break;
            case R.id.Share_rl: //分享
                startActivity(new Intent(getActivity(), ShareActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN) {
            if (data != null) {

                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

                CommonUtils.showToast("解码结果： \n" + content);
            }
        }
    }
}
