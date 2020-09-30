package com.gis.manager.service;
import java.io.Serializable;
import java.util.List;

/**
 * 文件名称:IBaseService.java
 * 日期:2017年9月12日
 * 时间:上午7:34:39
 * 创建人:王猛
 */
public interface IBaseService <T,K extends Serializable>{
	//增加
	void insert(T t);
	//更新
	void update(T t);
	//删除
	void delete(T t);
	//获取全部
	List<T> queryAll();
	//ID查询
	T getById(K id);
}
