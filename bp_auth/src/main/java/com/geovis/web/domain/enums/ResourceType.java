package com.geovis.web.domain.enums;

public enum ResourceType {
    MENU("菜单", "MENU"), BUTTON("按钮", "BUTTON"), FOLDER("目录", "FOLDER"), SERVICE("服务", "SERVICE");

    ResourceType(String description) {
        this.description = description;

    }

    ResourceType(String description, String value) {
        this.description = description;
        this.value = value;
    }

    private String description;
    private String value;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static ResourceType getResourceType(String type) {
        ResourceType resourceType = ResourceType.valueOf(type);
        return resourceType;
    }

}
