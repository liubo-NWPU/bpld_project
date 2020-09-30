package com.gis.trans.service.impl;

import com.gis.trans.service.IBaseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

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
		//findOne返回对象实体，不存在为null，getOne返回引用，任何时候都不为null
		return getDao().getOne(id);
	}

}
