
package com.geovis.web.service.impl.system;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geovis.web.base.service.impl.AbstractService;
import com.geovis.web.dao.system.mapper.DemoMapper;
import com.geovis.web.domain.demo.Demo;
import com.geovis.web.domain.system.SysUser;
import com.geovis.web.service.system.DemoService;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

@Service
public class DemoServiceImpl extends AbstractService<Demo> implements DemoService {

	private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

	@Autowired
	private DemoMapper demoMapper;

	/**
	 * 查询方法，支持模糊查询
	 * 
	 * @param sysUser
	 * @return
	 */
	@Override
	public List<Demo> listAll(Demo demo) {
		if (demo.getPage() != null && demo.getRows() != null) {
			PageHelper.startPage(demo.getPage(), demo.getRows());
		}
		Example example = new Example(Demo.class);
		Example.Criteria criteria = example.createCriteria();
		// search condition
		// 名称
		if (demo != null && demo.getName() != null && !"".equals(demo.getName())) {
			criteria.andLike("name", "%" + demo.getName() + "%");
		}
		// 姓名
		if (demo != null && demo.getType() != null && !"".equals(demo.getType())) {
			criteria.andLike("type", "%" + demo.getName() + "%");
		}

		// 获取删除标记为正常的记录
		criteria.andEqualTo("delFlag", demo.getDelFlag());

		return this.demoMapper.selectByExample(example);
	}

	/**
	 * 当数据发生变化时更新缓存 缓存处理参考user
	 * 
	 * @param sysUser
	 * @return
	 */
	@Transactional
	@Override
	public Demo commonSave(SysUser currentUser, Demo demo) {
		try {
			if (demo.getId() == null) {
				super.save(demo);
			} else {
				this.updateNotNull(demo);
			}
			List<Demo> demos = this.demoMapper.selecDemoById(demo.getId());
			if (demos != null && demos.size() > 0) {
				return demos.get(0);
			}
		} catch (Exception e) {
			logger.error("demo insert or update error!" + e.toString());
		}
		return null;
	}

	/**
	 * 删除用户
	 * 
	 * @param userId
	 * @return 注意：清除用户缓存一定要先清除username，因为username需要借助userid
	 */

	@Override
	public boolean removeDemoById(SysUser currentUser, String userId) {
		try {
			this.remove(userId);
			return true;
		} catch (Exception e) {
			logger.error("demo delete error!" + e.toString());
		}

		return false;
	}

	/**
	 * 根据名称查询相关对象
	 * 
	 * @param name
	 * @return 注意：清除用户缓存一定要先清除username，因为username需要借助userid
	 */
	public List<Demo> getByName(SysUser currentUser, String name) {

		try {
			return demoMapper.getByName(name);
		} catch (Exception e) {

			logger.error("find demo by name error!" + e.toString());
		}
		return null;
	}
	/**
	 * 根据名称查询相关对象
	 * 
	 * @param name
	 * @return 注意：清除用户缓存一定要先清除username，因为username需要借助userid
	 */
	public List<Demo> getByNameAndType(SysUser currentUser, String name,String type) {

		try {
			return demoMapper.getByNameAndType(name,type);
		} catch (Exception e) {

			logger.error("find demo by name error!" + e.toString());
		}
		return null;
	}
	/**
	 * 根据类型查询相关对象
	 * 
	 * @param type
	 * @return 注意：清除用户缓存一定要先清除username，因为username需要借助userid
	 */
	public List<Demo> getByType(SysUser currentUser, String type) {

		try {
			return demoMapper.getByType(type);
		} catch (Exception e) {

			logger.error("find demo by type error!" + e.toString());
		}
		return null;
	}
	/**
	 * 根据类型查询相关对象
	 * 
	 * @param type
	 * @return 注意：清除用户缓存一定要先清除username，因为username需要借助userid
	 */
	public Map<String,Object> getByIdToMap(SysUser currentUser, String id) {
		
		try {
			List<Map<String,Object>> map = demoMapper.getByIdToMap(id);
			return map.get(0);
		} catch (Exception e) {
			
			logger.error("execute getByIdToMap error!" + e.toString());
		}
		return null;
	}
	/**
	 * 根据类型查询相关对象
	 * 
	 * @param type
	 * @return 注意：清除用户缓存一定要先清除username，因为username需要借助userid
	 */
	public Map<String,Demo> getByNameToMaps(SysUser currentUser, String name) {
		
		try {
			Map<String,Demo> map = demoMapper.getByNameToMaps(name);
			return map;
		} catch (Exception e) {
			
			logger.error("execute getByNameToMaps error!" + e.toString());
		}
		return null;
	}
}
