package com.teacherhelp.activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.app.UrlConfig;
import com.teacherhelp.app.UserInfo;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.fragment.FindFragment;
import com.teacherhelp.fragment.MessageFragment;
import com.teacherhelp.fragment.MineFragment;
import com.teacherhelp.fragment.TeachFragment;
import com.teacherhelp.listener.MyReceiveMessageListener;
import com.teacherhelp.listener.MySendMessageListener;
import com.teacherhelp.utils.CommonUtils;
import com.teacherhelp.utils.LogHelper;
import com.teacherhelp.utils.ShareReferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cn.finalteam.galleryfinal.GalleryFinal;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 主页  现在用这个主页
 */
public class TabHomeOldActivity extends BaseActivity implements RongIM.UserInfoProvider {

    @Bind(R.id.mainactivity_btn_teach)
    Button BtnTeach;
    @Bind(R.id.mainactivity_btn_message)
    Button BtnMessage;
    @Bind(R.id.mainactivity_unread_message_number)
    TextView TvMessageNumber;
    @Bind(R.id.mainactivity_btn_find)
    Button BtnFind;
    @Bind(R.id.mainactivity_btn_mine)
    Button BtnMine;
    @Bind(R.id.mainactivity_fragment_container)
    FrameLayout Container;


    private Button[] mTabs; //主界面下面4个按钮
    private int index = 0; //判断当前是哪个fragment显示
    // 当前fragment的index
    private int currentTabIndex = 0;
    private TeachFragment teachFragment;
    private MessageFragment messageFragment;
    private FindFragment findFragment;
    private MineFragment mineFragment;
    private Fragment[] fragments; //fragment集合
    private MyReceiveUnreadCountChangedListener myReceiveUnreadCountChangedListener;


    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void aadListenter() {

    }

    @Override
    protected void initVariables() {
        //添加activity
        MyApplication.getInstance().addActivity(this);
        initView();
        myReceiveUnreadCountChangedListener = new MyReceiveUnreadCountChangedListener();
        /**
         * 设置发出消息的监听器
         */
        if (RongIM.getInstance() != null) {
            //设置自己发出的消息监听器。
            RongIM.getInstance().setSendMessageListener(new MySendMessageListener());
        }

        /**
         *  设置接收消息的监听器。
         */
        RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener());

        if (RongIM.getInstance() != null) {
            /**
             * 接收未读消息的监听器。
             *
             * @param listener          接收所有未读消息消息的监听器。
             */
            RongIM.getInstance().setOnReceiveUnreadCountChangedListener(myReceiveUnreadCountChangedListener, Conversation.ConversationType.PRIVATE);
            RongIM.getInstance().setOnReceiveUnreadCountChangedListener(myReceiveUnreadCountChangedListener, Conversation.ConversationType.SYSTEM);

        }

        getUserInfo();

        /**
         * 设置融云用户头像
         */
        RongIM.setUserInfoProvider(this, true);


    }

    private void initView() {
        mTabs = new Button[4];
        mTabs[0] = BtnTeach;
        mTabs[1] = BtnMessage;
        mTabs[2] = BtnFind;
        mTabs[3] = BtnMine;
        mTabs[0].setSelected(true);

        //初始化5个fragment
        teachFragment = new TeachFragment();
        messageFragment = new MessageFragment();
        findFragment = new FindFragment();
        mineFragment = new MineFragment();
        fragments = new Fragment[]{teachFragment, messageFragment, findFragment, mineFragment};
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainactivity_fragment_container, teachFragment)
                .hide(teachFragment).show(teachFragment).commit();
    }

    /**
     * button点击事件
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.mainactivity_btn_teach:
                index = 0;
                break;
            case R.id.mainactivity_btn_message:
                index = 1;
                break;
            case R.id.mainactivity_btn_find:
                index = 2;
                break;
            case R.id.mainactivity_btn_mine:
                index = 3;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.mainactivity_fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                CommonUtils.showToast("再按一次退出程序");
                //    Utility.showSnackbar(Container,"再按一次退出程序", TabHomeOldActivity.this);
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(false);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 接收未读消息的监听器。
     */
    private class MyReceiveUnreadCountChangedListener implements RongIM.OnReceiveUnreadCountChangedListener {
        /**
         * @param count 未读消息数。
         */
        @Override
        public void onMessageIncreased(int count) {
            if (count != 0) {
                TvMessageNumber.setVisibility(View.VISIBLE);
                if (count > 99) {
                    TvMessageNumber.setText("99+");
                } else {
                    TvMessageNumber.setText("" + count);
                }
            } else {
                TvMessageNumber.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 获取登录用户信息
     */
    private void getUserInfo() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConfig.UserInfo.GET_Info, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                LogHelper.d("请求返回的结果：" + result);
                //请求成功
                JSONObject jsonObject = JSONObject.parseObject(result);
                int responseCode = jsonObject.getIntValue("code");
                switch (responseCode) {
                    case 0:
                        Log.e("获取用户信息成功", jsonObject.get("data") + "");
                        Gson gson = new Gson();
                        UserInfo user = gson.fromJson(
                                jsonObject.getString("data"),
                                new TypeToken<UserInfo>() {
                                }.getType());
                        MyApplication.userInfo = user;
                        break;
                    default:
                        CommonUtils.showToast("获取用户信息失败" + responseCode + jsonObject.get("msg"));
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

                return null;
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
        stringRequest.setTag("volleygetUserInfo");
        MyApplication.getHttpQueue().add(stringRequest);
    }

    /**
     * 用户信息的提供者
     * 融云会话界面 和 会话列表的 头像 昵称展示
     */
    @Override
    public io.rong.imlib.model.UserInfo getUserInfo(String userId) {
        //先从获取数据库操作的实例
//        FriendDao friendDao = DBManager.getInstance(MainActivity.this)
//                .getDaoSession().getFriendDao();
        //获取数据库中我所有好友的bean对象
//        List<UserInfo> friends = null;
//        if (friends != null && friends.size() > 0) {
//            //增强for把所有的用户信息 return 到融云服务端
//            for (UserInfo friend : friends) {
//                //判断返回的userId
//                if (friend.getCellPhone().equals(userId)) {
//                    return new io.rong.imlib.model.UserInfo(friend.getCellPhone(), friend.getName(),
//                            Uri.parse(friend.getHeadImg()));
//                }
//            }
//        }
        if (MyApplication.userInfo.getCellPhone().equals(userId)) {
            return new io.rong.imlib.model.UserInfo(MyApplication.userInfo.getCellPhone(), MyApplication.userInfo.getName(),
                    Uri.parse(MyApplication.userInfo.getHeadImg()));
        }

        return null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
        GalleryFinal.cleanCacheFile();
    }
}
