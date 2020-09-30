package com.gis.manager.service.impl;

import com.gis.manager.service.IBaseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 文件名称:BaseServiceImpl.java
 * 日期:2017年9月12日
 * 时间:上午7:34:39
 * 创建人:王猛
 */
@Transactional
public abstract class BaseServiceImpl<T,K extends Serializable> implements IBaseService<T,K> {
	public abstract JpaRepository<T,K> getDao();
	@Override
	public void insert(T t) {
		getDao().save(t);
	}

	@Override
	public void update(T t) {
		getDao().save(t);
	}

	@Override
	public void delete(T t) {
		getDao().delete(t);
	}
	@Override
	public List<T> queryAll() {
		// TODO Auto-generated method stub
		return getDao().findAll();
	}
	@Override
	public T getById(K id) {
		Optional<T> opt =  getDao().findById(id);
		if(opt.isPresent())
			return opt.get();
		else
			return null;
	}

}
