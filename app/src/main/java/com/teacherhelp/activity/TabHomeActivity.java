package com.teacherhelp.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.fragment.FindFragment;
import com.teacherhelp.fragment.MessageFragment;
import com.teacherhelp.fragment.MineFragment;
import com.teacherhelp.fragment.TeachFragment;
import com.teacherhelp.listener.MyReceiveMessageListener;
import com.teacherhelp.listener.MySendMessageListener;
import com.teacherhelp.utils.CommonUtils;

import butterknife.Bind;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import me.majiajie.pagerbottomtabstrip.Controller;
import me.majiajie.pagerbottomtabstrip.PagerBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.TabItemBuilder;
import me.majiajie.pagerbottomtabstrip.TabLayoutMode;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectListener;

/**
 * 主页 现在不用这个页面了
 * Created by Administrator on 2016/7/14.
 */
public class TabHomeActivity extends BaseActivity {

    @Bind(R.id.tab)
    PagerBottomTabLayout tab;

    public Controller controller;
    private TeachFragment teachFragment;
    private MessageFragment messageFragment;
    private FindFragment findFragment;
    private MineFragment mineFragment;
    private Fragment[] fragments; //fragment集合

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_tabhome);
    }

    @Override
    protected void aadListenter() {
        controller.addTabItemClickListener(listener);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().setActivity(this);

        //初始化Fragment
        initFragment();
        //初始化底部导航栏
        BottomTabTest();

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
            RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new MyReceiveUnreadCountChangedListener());

            /**
             * 设置接收未读消息的监听器。
             *
             * @param listener          接收未读消息消息的监听器。
             * @param conversationTypes 接收指定会话类型的未读消息数。
             */
            RongIM.getInstance().setOnReceiveUnreadCountChangedListener(new MyReceiveUnreadCountChangedListener(), Conversation.ConversationType.PRIVATE);
        }

    }


    private void BottomTabTest() {
        //用TabItemBuilder构建一个导航按钮
        TabItemBuilder teachBuilder = new TabItemBuilder(this).create()
                .setDefaultIcon(R.mipmap.teach)
                .setText("教学")
                .setSelectedColor(getResources().getColor(R.color.rer_color))
                .setTag("A")
                .build();

        TabItemBuilder messageBuilder = new TabItemBuilder(this).create()
                .setDefaultIcon(R.mipmap.message)
                .setText("消息")
                .setSelectedColor(getResources().getColor(R.color.rer_color))
                .setTag("B")
                .build();
        //构建导航栏,得到Controller进行后续控制
        controller = tab.builder()
                .addTabItem(teachBuilder)
                .addTabItem(messageBuilder)
                .addTabItem(R.mipmap.find, "发现", getResources().getColor(R.color.rer_color))
                .addTabItem(R.mipmap.my, "我", getResources().getColor(R.color.rer_color))
                .setMode(TabLayoutMode.HIDE_TEXT | TabLayoutMode.CHANGE_BACKGROUND_COLOR)
                .build();
        //右上角数字显示 第一个参数是TAG，第二个是数字
//        controller.setMessageNumber("B",2);
        //右上角红点显示 第一个参数是哪个位置，第二个是是否显示
//        controller.setDisplayOval(1,true);
    }


    /**
     * 接收未读消息的监听器。
     */
    private class MyReceiveUnreadCountChangedListener implements RongIM.OnReceiveUnreadCountChangedListener {

        /**
         * @param count           未读消息数。
         */
        @Override
        public void onMessageIncreased(int count) {
            if(count!=0){
                controller.setMessageNumber("B",count);
            }else{
                controller.setMessageNumber("B",0);
            }
        }
    }


    //导航栏监听事件
    OnTabItemSelectListener listener = new OnTabItemSelectListener() {
        @Override
        public void onSelected(int index, Object tag) {
            Log.i("asd", "onSelected:" + index + "   TAG: " + tag.toString());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.push_up_in, R.anim.push_up_out);
            transaction.replace(R.id.fragment_container, fragments[index]);
            transaction.commit();
        }

        @Override
        public void onRepeatClick(int index, Object tag) {
            Log.i("asd", "onRepeatClick:" + index + "   TAG: " + tag.toString());
        }
    };

    private void initFragment() {
        //初始化5个fragment
        teachFragment = new TeachFragment();
        messageFragment = new MessageFragment();
        findFragment = new FindFragment();
        mineFragment = new MineFragment();
        fragments = new Fragment[]{teachFragment, messageFragment, findFragment, mineFragment};
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, teachFragment)
                .hide(teachFragment).show(teachFragment).commit();
    }


    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                CommonUtils.showToast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                MyApplication.setIsmessage(true);
                moveTaskToBack(false);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
