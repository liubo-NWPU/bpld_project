package com.geovis.web.domain.enums;

public enum ServiceType {
    PLS("插件服务","PLS"),DMS("数据管理服务","DMS"),MS("监控服务","MS"),XTGL("系统管理","XTGL");
    private String serverName;
    private String value;

    ServiceType(String serverName) {
        this.serverName = serverName;
    }

    ServiceType(String serverName, String value) {
        this.serverName = serverName;
        this.value = value;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static void main(String[] args) {
        System.out.println(ServiceType.PLS.getServerName());
        System.out.println(ServiceType.PLS.getValue());
    }
}
