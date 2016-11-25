package com.teacherhelp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.teacherhelp.R;
import com.teacherhelp.activity.EvaluationStudentActivity;
import com.teacherhelp.activity.HomeWorkActivity;
import com.teacherhelp.activity.HomeWorkCorrectActivity;
import com.teacherhelp.activity.LeaveRecordActivity;
import com.teacherhelp.activity.SignInActivity;
import com.teacherhelp.activity.StudentSalaryActivity;
import com.teacherhelp.activity.TeachLeaveRecordActivity;
import com.teacherhelp.activity.TeachingContentActivity;
import com.teacherhelp.activity.curriculum.TeacherCurriculumActivity;
import com.teacherhelp.adapter.GridViewAdapter;
import com.teacherhelp.base.BaseFragment;
import com.teacherhelp.bean.TeachBean;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.view.NoScrollGridView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * 教学Fragment
 * Created by Administrator on 2016/7/14.
 */
public class TeachFragment extends BaseFragment {

    @Bind(R.id.tv_titletwo)
    TextView Title;
    @Bind(R.id.convenientbanner)
    ConvenientBanner convenientbanner;
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.gridview)
    NoScrollGridView gridview;
    private ArrayList<String> localImages = new ArrayList<String>();
    private GridViewAdapter adapter;
    private String[] Name = {"教师课表", "审批请假", "学生考勤", "评价学生", "布置作业", "批改作业", "教师打卡", "教师请假", "教学内容", "事项办理"};
    private ArrayList<TeachBean> list;

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_teach, null);
    }

    @Override
    protected void initVariables() {
        imgbtnBack.setVisibility(View.GONE);
        Title.setVisibility(View.VISIBLE);
        Title.setText("教学");

        //初始化画廊
        loadTestDatas();
        convenientbanner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                //设置翻页的效果，不需要翻页效果可用不设
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
        ;

        //初始化RecyclerView
        initRecyclerView();
    }

    @Override
    protected void addListener() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

    }

    private void initRecyclerView() {
        list = new ArrayList<>();

        upData();
        adapter = new GridViewAdapter(getActivity(), list);
        gridview.setAdapter(adapter);
        gridview.setFocusable(false);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //教师课表
                        startActivity(new Intent(getActivity(), TeacherCurriculumActivity.class));
                        break;
                    case 1: //审批请假
                        startActivity(new Intent(getActivity(), LeaveRecordActivity.class));
                        break;
                    case 2: //学生考勤
                        startActivity(new Intent(getActivity(), StudentSalaryActivity.class));
                        break;
                    case 3: //评价学生
                        startActivity(new Intent(getActivity(), EvaluationStudentActivity.class));
                        break;
                    case 4: //布置作业
                        startActivity(new Intent(getActivity(), HomeWorkActivity.class));
                        break;
                    case 5: //批改作业
                        startActivity(new Intent(getActivity(), HomeWorkCorrectActivity.class));
                        break;
                    case 6: //教师打卡
                        startActivity(new Intent(getActivity(), SignInActivity.class));
                        break;
                    case 7: //教师请假
                        startActivity(new Intent(getActivity(), TeachLeaveRecordActivity.class));
                        break;
                    case 8://教学内容
                        startActivity(new Intent(getActivity(), TeachingContentActivity.class));
                        break;
                    case 9://事项办理
                        break;
                }
            }
        });
    }


    private void loadTestDatas() {
        if (localImages.size() <= 0) {
            localImages.add("http://images.17173.com/2012/news/2012/05/07/y0507cos10s.jpg");
            localImages.add("http://img2.duitang.com/uploads/item/201302/03/20130203180340_rjunn.thumb.600_0.jpeg");
            localImages.add("http://img1.imgtn.bdimg.com/it/u=2056737077,1535970590&fm=21&gp=0.jpg");
            localImages.add("http://image.tianjimedia.com/uploadImages/2013/149/UV36B4YUU8G0_1000x500.jpg");
        }
    }

    /**
     * 轮播图片适配
     */
    public class LocalImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            ImageLoader.getInstance().displayImage(data, imageView);
        }

    }

    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        convenientbanner.startTurning(5000);
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientbanner.stopTurning();
    }

    private void upData() {
        for (int i = 0; i < 10; i++) {
            TeachBean bean = new TeachBean();
            bean.setImg(getResId("teach_func_icon_" + i, R.mipmap.class));
            bean.setName(Name[i]);
            list.add(bean);
        }
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
