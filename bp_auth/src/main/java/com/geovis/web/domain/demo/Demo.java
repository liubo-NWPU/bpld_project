package com.geovis.web.domain.demo;

import javax.persistence.Table;

import com.geovis.web.base.domain.BaseDomain;

@Table(name = "demo")
public class Demo extends BaseDomain {
    /**
     * name
     */
    private String name;

    /**
     * type
     */
    private String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Demo [name=" + name + ", type=" + type + "]";
	}
}