package com.geovis.core.constant;

public class Constant {

    public static final String USER_NAME_TARGET="username";

    public static final String USER_ACCESSTOKEN_TARGET="Authorization";

    public static final String  USER_INSERT_OPERATION="insert";

    public static final String  USER_UPDATE_OPERATION="update";

    public static final String  USER_DELETE_OPERATION="delete";
    //用户状态：正常
    public static final  String USER_STATE_NORMAL = "NORMAL";
    //用户状态：锁定
    public static final  String USER_STATE_LOCK = "LOCK";
    //用户状态：锁定免费
    public static final  String USER_STATE_LOCKFREE = "LOCKFREE";
    //用户状态：新注册
    public static final  String USER_STATE_REGIST = "REGIST";
    //删除状态：正常
    public static final  String DEL_FLAG_NORMAL = "NORMAL";
    //删除状态：删除
    public static final  String DEL_FLAG_DELETE = "DELETE";

    //返回头信息
    public static final String HEADER_ERROR = "X-geovisuavApp-error";
    public static final String HEADER_ALERT = "X-geovisuavApp-alert";
    public static final String HEADER_PARAM = "X-geovisuavApp-param";

    public static final String EXPOSE_HEADER = HEADER_ERROR + ", " + HEADER_ALERT + ", " + HEADER_PARAM;

    /***插件管理key**/
    public  static final String PLGUGIN_HEADER_ADRESSS="transAdress";

    public static final String  SERVICE_TYPE="serviceType";
    //阿里短信相关配置
//    public static final String SMS_ACCESS_KEY_ID = "LTAIcCgvfg8zE21T";
//    public static final String SMS_ACCESS_KEY_SECRET = "J5Ft3US5uXKLl82qjtLY5yX7hBOQ9a";
    //短信模板
//    public static final String SMS_TEMPLATE_CODE_TEST = "SMS_119190070";
}
