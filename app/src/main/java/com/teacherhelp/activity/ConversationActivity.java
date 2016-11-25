package com.teacherhelp.activity;


import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teacherhelp.R;
import com.teacherhelp.app.MyApplication;
import com.teacherhelp.base.BaseActivity;
import com.teacherhelp.utils.ShareReferenceUtils;

import java.util.Locale;

import butterknife.Bind;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;


/**
 * 会话界面
 * Created by Administrator on 2016/7/19.
 */
public class ConversationActivity extends BaseActivity {
    @Bind(R.id.imgbtn_back)
    ImageButton imgbtnBack;
    @Bind(R.id.tv_titletwo)
    TextView tvTitletwo;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    /**
     * 目标 Id
     */
    private String mTargetId;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

//    private Handler mHandler;
//    public static final int SET_TEXT_TYPING_TITLE = 1;
//    public static final int SET_VOICE_TYPING_TITLE = 2;
//    public static final int SET_TARGETID_TITLE = 0;
//    private final String TextTypingTitle = "对方正在输入...";
//    private final String VoiceTypingTitle = "对方正在讲话...";

    @Override
    protected void initLayout() {
        setContentView(R.layout.activity_conversation);
    }

    @Override
    protected void aadListenter() {
        imgbtnBack.setOnClickListener(this);
    }

    @Override
    protected void initVariables() {
        MyApplication.getInstance().addActivity(this);
        tvTitletwo.setVisibility(View.VISIBLE);
        Intent intent = getIntent();

        getIntentDate(intent);

        isReconnect(intent);

//        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
//            @Override
//            public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
//                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
//                if (type.equals(mConversationType) && targetId.equals(mTargetId)) {
//                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
//                    int count = typingStatusSet.size();
//                    if (count > 0) {
//                        Iterator iterator = typingStatusSet.iterator();
//                        TypingStatus status = (TypingStatus) iterator.next();
//                        String objectName = status.getTypingContentType();
//
//                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
//                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
//                        //匹配对方正在输入的是文本消息还是语音消息
//                        if (objectName.equals(textTag.value())) {
//                            //显示“对方正在输入”
//                            mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
//                        } else if (objectName.equals(voiceTag.value())) {
//                            //显示"对方正在讲话"
//                            mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
//                        }
//                    } else {
//                        //当前会话没有用户正在输入，标题栏仍显示原来标题
//                        mHandler.sendEmptyMessage(SET_TARGETID_TITLE);
//                    }
//                }
//            }
//        });
//        mHandler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                tvTitle.setVisibility(View.VISIBLE);
//                tvTitletwo.setVisibility(View.GONE);
//                switch (msg.what) {
//                    case SET_TEXT_TYPING_TITLE:
//                        tvTitle.setText(TextTypingTitle);
//                        break;
//                    case SET_VOICE_TYPING_TITLE:
//                        tvTitle.setText(VoiceTypingTitle);
//                        break;
//                    case SET_TARGETID_TITLE:
//                        tvTitle.setVisibility(View.GONE);
//                        tvTitletwo.setVisibility(View.VISIBLE);
//             //           setActionBarTitle(mConversationType, mTargetId);
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        enterFragment(mConversationType, mTargetId);
        if (intent.getData().getQueryParameter("title") == null) {
            setActionBarTitle(mTargetId);
        } else {
            setActionBarTitle(intent.getData().getQueryParameter("title"));
        }

    }

    /**
     * 设置 actionbar title
     */
    private void setActionBarTitle(String targetid) {
        tvTitletwo.setText(targetid);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName)
                .buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId)
                .build();

        fragment.setUri(uri);
    }

    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect(Intent intent) {


        String token = null;

        if (MyApplication.getInstance() != null) {

            token = ShareReferenceUtils.getValue("Token");
        }

        //push或通知过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push") != null
                    && intent.getData().getQueryParameter("push").equals("true")) {

                reconnect(token);
            } else {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {

                    reconnect(token);
                } else {
                    enterFragment(mConversationType, mTargetId);
                }
            }
        }
    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {

        if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {

                }

                @Override
                public void onSuccess(String s) {

                    enterFragment(mConversationType, mTargetId);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.imgbtn_back:
                finish();
                break;
        }
    }


}
