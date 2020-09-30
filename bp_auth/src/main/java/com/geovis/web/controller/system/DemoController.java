package com.geovis.web.controller.system;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geovis.core.util.ResponseUtil;
import com.geovis.core.util.SystemCacheUtil;
import com.geovis.web.domain.demo.Demo;
import com.geovis.web.service.system.DemoService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tk.mybatis.mapper.util.StringUtil;

@Api(value = "DemoController", description = "demo测试接口")
@RestController
@SuppressWarnings("all")
@RequestMapping("/system")
public class DemoController {

	@Autowired
	private DemoService demoService;

	/**
	 * 查询demo
	 * 
	 * @param Demo
	 * @return
	 */
	@ApiOperation(value = "获取所有demo", notes = "获取所有demo")
	@GetMapping("/demos")
	@RequiresPermissions(value = { "system:demo:list","admin"}, logical = Logical.OR)
	public ResponseEntity listAll(@ApiParam Demo demo) {
		List<Demo> demoList = this.demoService.listAll(demo);
		return ResponseUtil.success(new PageInfo<Demo>(demoList));
	}

	/**
	 * 保存demo
	 * 
	 * @param Demo
	 * @return
	 */
	@ApiOperation(value = "保存demo", notes = "保存demo")
	@PostMapping("/demo")
	@RequiresPermissions(value = { "system:demo:add","admin"}, logical = Logical.OR)
	public ResponseEntity save(HttpServletRequest request, @ApiParam @RequestBody Demo demo) {
		if (demo.getId() != null) {
			return ResponseUtil.failure("参数错误！");
		}
		if (StringUtil.isEmpty(demo.getName())) {
			// 给出默认值
			demo.setName("test" + new Random().nextInt(10));
		}
		demo = this.demoService.commonSave(SystemCacheUtil.getUserByRequest(request), demo);

		return ResponseUtil.success(demo);
	}

	/**
	 * 修改demo
	 * 
	 * @param Demo
	 * @return
	 */
	@ApiOperation(value = "修改demo", notes = "修改demo")
	@PutMapping("/demo")
	@RequiresPermissions(value = { "system:demo:update","admin"}, logical = Logical.OR)
	public ResponseEntity update(HttpServletRequest request, @ApiParam @RequestBody Demo demo) {
		if (demo.getId() == null || "".equals(demo.getId())) {
			return ResponseUtil.failure("参数错误！");
		}
		demo = this.demoService.commonSave(SystemCacheUtil.getUserByRequest(request), demo);

		return ResponseUtil.success(demo);
	}

	/**
	 * 删除demo
	 * 
	 * @param Demo
	 * @return
	 */
	@ApiOperation(value = "删除demo", notes = "删除demo")
	@DeleteMapping("/demo/{id}")
	@RequiresPermissions(value = { "system:demo:add","admin"}, logical = Logical.OR)
	public ResponseEntity delete(HttpServletRequest request, @PathVariable String id) {

		this.demoService.removeDemoById(SystemCacheUtil.getUserByRequest(request), id);

		return ResponseUtil.success();
	}

	// *******************以上是一些标准的自带方法******************
	// *******************以下是一些自定义的方法********************
	/**
	 * 获取demo
	 * 
	 * @param Demo
	 * @return
	 */
	@ApiOperation(value = "传参demo示例1", notes = "传参demo示例一个参数")
	@GetMapping("/demoByName/{name}")
	public ResponseEntity getByName(HttpServletRequest request, @PathVariable String name) {

		List<Demo> demos = this.demoService.getByName(SystemCacheUtil.getUserByRequest(request), name);

		return ResponseUtil.success(demos);
	}

	/**
	 * 获取demo
	 * 
	 * @param Demo
	 * @return
	 */
	@ApiOperation(value = "传参demo示例2", notes = "传参demo示例多个参数")
	@GetMapping("/demoByNameAndType/{name}/{type}")
	public ResponseEntity getByNameAndType(HttpServletRequest request, @PathVariable String name,
			@PathVariable String type) {

		List<Demo> demos = this.demoService.getByNameAndType(SystemCacheUtil.getUserByRequest(request), name, type);

		return ResponseUtil.success(demos);
	}

	/**
	 * 获取demo
	 * 
	 * @param Demo
	 * @return
	 */
	@ApiOperation(value = "传参demo示例3", notes = "传参demo示例一个参数非map")
	@GetMapping("/demoByType/{type}")
	public ResponseEntity getByType(HttpServletRequest request, @PathVariable String type) {

		List<Demo> demos = this.demoService.getByType(SystemCacheUtil.getUserByRequest(request), type);

		return ResponseUtil.success(demos);
	}

	/**
	 * 获取demo
	 * 
	 * @param Demo
	 * @return
	 */
	@ApiOperation(value = "获取demoMap", notes = "获取demoMap")
	@GetMapping("/demoByIdToMap/{id}")
	public ResponseEntity getByIdToMap(HttpServletRequest request, @PathVariable String id) {

		Map map = this.demoService.getByIdToMap(SystemCacheUtil.getUserByRequest(request), id);

		return ResponseUtil.success(map);
	}

	/**
	 * 获取demo
	 * 
	 * @param Demo
	 * @return
	 */
	@ApiOperation(value = "获取demoMaps", notes = "获取demoMaps")
	@GetMapping("/demoByNameToMaps/{name}")
	public ResponseEntity getByNameToMaps(HttpServletRequest request, @PathVariable String name) {

		Map<String, Demo> maps = this.demoService.getByNameToMaps(SystemCacheUtil.getUserByRequest(request), name);

		return ResponseUtil.success(maps);
	}

}
