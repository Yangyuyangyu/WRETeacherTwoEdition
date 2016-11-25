package com.teacherhelp.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.teacherhelp.R;
import com.teacherhelp.adapter.DegreeAdapter;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.base.BaseRecyclerAdapter;
import com.teacherhelp.bean.ClassBean;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 名称:学历页面
 * 作用：设置用户学历
 * Created on 2016/7/27.
 * 创建人：WangHaoMiao
 */
public class DegreeActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_titleright)
    TextView tvTitleright;
    @Bind(R.id.xRecyclerview)
    XRecyclerView xRecyclerview;

    /*分页，用于表示当前是获取第几页的内容*/
    private int page = 1;
    /*判断是否是上拉加载更多*/
    private boolean isdown = false;
    /*数据存放的list*/
    private ArrayList<ClassBean> list;
    private DegreeAdapter adapter;

    private String[] arry = { "大专", "本科", "硕士", "博士"};

    //保存选择学历
    private String degreeStr = null;

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_degree);
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
        tvTitletwo.setText("学历");

        initRecyclerView();
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
                if (degreeStr == null) {
                    return;
                }
                Intent i = new Intent();
                i.putExtra("degree", degreeStr);
                setResult(16, i);
                finish();
                break;
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(DegreeActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerview.setLayoutManager(layoutManager);

        //设置刷新样式
        xRecyclerview.setPullRefreshEnabled(false);
        xRecyclerview.setLoadingMoreEnabled(false);

        GetData();
        adapter = new DegreeAdapter(DegreeActivity.this, list);
        xRecyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object model) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setIscheck(false);
                }
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.recyclerview_degree_checkbox);
                checkBox.setChecked(!checkBox.isChecked());
                if (checkBox.isChecked()) {
                    list.get(position).setIscheck(true);
                    degreeStr = arry[position];
                } else {
                    list.get(position).setIscheck(false);
                    degreeStr = null;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemLongClick(View view, int position, Object model) {

            }
        });
    }


    /**
     * 加入数据
     */
    private void GetData() {
        for (int i = 0; i < arry.length; i++) {
            ClassBean bean = new ClassBean();
            bean.setName(arry[i]);
            list.add(bean);
        }
    }
}
