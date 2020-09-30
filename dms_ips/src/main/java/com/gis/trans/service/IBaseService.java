package com.gis.trans.service;
import java.io.Serializable;
import java.util.List;

public interface IBaseService<T,K extends Serializable>{
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
