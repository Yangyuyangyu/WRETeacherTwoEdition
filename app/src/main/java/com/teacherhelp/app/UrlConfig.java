package com.teacherhelp.app;

import com.teacherhelp.utils.SHA1Utils;

/**
 * Created by Administrator on 2016/5/23.
 * 访问连接
 */
public class UrlConfig {
    /**
     * 融云聊天服务使用API需提供header:  App-Key  Nonce  Timestamp  Signature
     */
    public static final String RongCloudAppKey = "6tnym1br65qc7";
    public static final String RongCloudAppSecret = "3cmcroFYhV91";
    public static final String RongCloudNonce = "581467449";//随机6位数

    public static String RongCloudSignature(String timestamp) {
        StringBuilder signature = new StringBuilder();
        signature.append(RongCloudAppSecret);
        signature.append(RongCloudNonce);
        signature.append(timestamp);
        return SHA1Utils.hex_sha1(signature.toString());
    }

    //获取融云用户的token
    public final static String Token_URL = "https://api.cn.ronghub.com/user/getToken.json";
    //刷新融云用户的信息
    public final static String RC_Refresh = "http://api.cn.ronghub.com/user/refresh.json";

    /**
     * QQ分享平台AppId
     */
    public final static String Share_QQ = "1105728451";
    /**
     * 微信分享平台AppId
     */
    public final static String Share_Wx = "wxa375723cdf533524";
    /**
     * 新浪分享平台AppKey
     */
    public final static String Share_Sina = "2096170139";
    public static final String Sina_REDIRECT_URL = "http://www.sina.com";// 应用的回调页
    public static final String Sina_SCOPE =                            // 应用申请的高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";


    /**
     * 基础URL
     * 127.0.0.1:8031    彬哥服务器域名
     * 192.168.10.34:8031  杨鹏鹏服务器域名
     * 192.168.10.36:8031
     */
    public final static String BASE_URL = "http://192.168.10.36:8031/";

    /**
     * 商品
     */
    public class Commodity {

        //获取商品目录
        public final static String GET_CATALOGUE = BASE_URL + "business/app/items/itemsList";

    }


    /**
     * 用户
     */
    public class User {

        //验证是否是手机号
        public final static String GET_Register = BASE_URL + "business/app/user/getbyphone";

        //发送手机验证码
        public final static String GET_Code = BASE_URL + "business/app/user/sendSms";

        //注册
        public final static String GET_RegisterThird = BASE_URL + "business/app/user/register";

        //登录
        public final static String GET_Login = BASE_URL + "business/app/user/userlogin";

        //找回密码
        public final static String GET_FindPassworld = BASE_URL + "business/app/user/recPwd";

        //认证验证码
        public final static String GET_ShopCode = BASE_URL + "business/app/shop/shopSendSms";

        //上传图片
        public final static String GET_UpImage = BASE_URL + "api/upload/ajaxImgUpload";

    }


    /**
     * 新增登录、注册、注册验证码
     */


    public class Login {
        public final static String POST_Login = BASE_URL + "login/phone";
        public final static String POST_Register = BASE_URL + "reg/phone";

    }

    public static String GET_SendCode(String phone) {
        return BASE_URL + "authCode/" + phone + "/sms";
    }

    /**
     * 上传图片
     */
    public final static String POST_UpImage = BASE_URL + "fileUpload/img";

    /**
     * 当前登陆者信息、修改
     */
    public class UserInfo {
        public final static String GET_Info = BASE_URL + "teacher/loginedInfo";
        public final static String POST_Update = BASE_URL + "teacher/updateInfo";
    }

    /**
     * 个人经历
     * 列表、添加、修改、删除
     */

    public class PersonalExperience {
        public final static String GET_List = BASE_URL + "teacher/history/list";
        public final static String POST_Add = BASE_URL + "teacher/history/add";


    }

    public static String POST_EditPersonalExperience(String id) {
        return BASE_URL + "teacher/history/" + id + "/edit";
    }

    public static String GET_DelPersonalExperience(String id) {
        return BASE_URL + "teacher/history/" + id + "/del";
    }

    /**
     * 个人成果
     * 列表、添加、修改、删除
     */
    public class PersonalResults {
        public final static String GET_List = BASE_URL + "teacher/results/list";
        public final static String POST_Add = BASE_URL + "teacher/results/add";

    }

    public static String POST_EditPersonalResults(String id) {
        return BASE_URL + "teacher/results/" + id + "/edit";
    }

    public static String GET_DelPersonalResults(String id) {
        return BASE_URL + "teacher/results/" + id + "/del";
    }

    /**
     * 个人风采
     * 列表、添加、修改、删除
     */
    public class PersonalStyle {
        public final static String GET_List = BASE_URL + "teacher/style/list";
        public final static String POST_Add = BASE_URL + "teacher/style/add";


    }

    public static String POST_EditPersonalStyle(String id) {
        return BASE_URL + "teacher/style/" + id + "/edit";
    }

    public static String GET_DelPersonalStyle(String id) {
        return BASE_URL + "teacher/style/" + id + "/del";
    }

    /**
     * 教务
     */
    /**
     * 课表：列表、详情
     */
    public static String GET_ListSchedule(String orgId, String schoolId) {
        return BASE_URL + "schedule/" + orgId + "/" + schoolId + "/list";
    }

    public static String GET_DetailSchedule(String timeId) {
        return BASE_URL + "schedule/" + timeId + "/info";
    }

    /**
     * 课程：获取课程下已报名的学生
     * {courseId}:课程id
     * {teacheMode}:课程教学方式(1个别课,2集体课)
     */

    public static String GET_ListCourse(String courseId, String teacheMode) {
        return BASE_URL + "course/" + courseId + "/" + teacheMode + "/studentList";
    }


    /**
     * 考勤：列表、详情、考勤
     */

    public static String GET_ListAttendance(String schoolId) {
        return BASE_URL + "rollCall/" + schoolId + "/list";
    }

    public static String GET_DetailAttendance(String id) {
        return BASE_URL + "rollCall/" + id + "/info";
    }

    public static String POST_Attendance(String id) {
        return BASE_URL + "rollCall/" + id + "/rollCall";
    }


    /**
     * 作业：列表、布置作业、学生提交的作业列表、批改
     */

    public static String GET_ListHomework(String schoolId) {
        return BASE_URL + "homeWork/" + schoolId + "/list";
    }

    public static String POST_DecorateHomework(String id) {
        return BASE_URL + "homeWork/" + id + "/arrange";
    }

    public static String GET_SubedListHomework(String id) {
        return BASE_URL + "homeWork/" + id + "/subedList";
    }

    public static String POST_CorrectingHomework(String studentSubHomeWorkId) {
        return BASE_URL + "homeWork/" + studentSubHomeWorkId + "/correcting";
    }


    /**
     * 打卡:记录、打卡
     */

    public static String GET_LogClock(String schoolId) {
        return BASE_URL + "punchClock/" + schoolId + "/log";
    }

    public static String GET_PunchClock(String schoolId) {
        return BASE_URL + "punchClock/" + schoolId + "/punch";
    }


    /**
     * 修改密码
     */
    public static String POST_ChangPwd() {
        return BASE_URL + "teacher/updatePwd";
    }

    /**
     * 课程统计、筛选
     */
    public static String POST_Statistical() {
        return BASE_URL + "teacher/myInfo/allCourse";
    }

    /**
     * 我的机构
     */
    public final static String POST_Organization = BASE_URL + "teacher/org/myOrgList";

    /**
     * 机构详情
     */
    public final static String POST_OrgDetail = BASE_URL + "teacher/org/orgInfo";
    /**
     * 机构下的教师
     */
    public final static String POST_OrgTeacher = BASE_URL + "teacher/org/orgTeacher";

    /**
     * 身份认证设置
     */
    public final static String POST_AddPapers = BASE_URL + "teacher/papers/addPapers";

    /**
     * 认证列表
     */
    public final static String POST_PaperList = BASE_URL + "teacher/papers/papersList";

    /**
     * 认证更新 传入id
     */
    public final static String POST_PaperMod = BASE_URL + "teacher/papers/papersMod";
    /**
     * 教学内容
     */
    public final static String POST_TeachContent=BASE_URL+"teacher/content/courseList";
    /**
     * 提交教学内容
     */
    public final static String POST_AddTeachContent=BASE_URL+"teacher/content/addContent";

}
