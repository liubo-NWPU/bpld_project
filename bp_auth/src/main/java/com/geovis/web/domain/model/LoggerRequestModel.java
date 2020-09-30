package com.geovis.web.domain.model;

public class LoggerRequestModel {
    /**用户ID**/
    private  String  uesrId;
    /**错误信息**/
    private  String errMsg;
     /**用户操作描述**/
    private String  decription;

    public String getUesrId() {
        return uesrId;
    }

    public void setUesrId(String uesrId) {
        this.uesrId = uesrId;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }
}
