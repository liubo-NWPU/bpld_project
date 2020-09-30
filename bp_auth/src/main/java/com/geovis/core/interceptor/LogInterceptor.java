package com.geovis.core.interceptor;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.geovis.core.constant.Constant;
import com.geovis.core.util.DateUtil;
import com.geovis.core.util.StringUtil;
import com.geovis.core.util.SystemCacheUtil;
import com.geovis.web.domain.system.SysUser;
import com.geovis.web.util.system.SysLogUtil;

/**
 * 日志拦截器
 */
public class LogInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
	private static final ThreadLocal<Long> startTimeThreadLocal =
			new NamedThreadLocal<Long>("ThreadLocal StartTime");
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
		if (logger.isDebugEnabled()){
			long beginTime = System.currentTimeMillis();//1、开始时间  
	        startTimeThreadLocal.set(beginTime);		//线程绑定变量（该数据只有当前请求的线程可见）  
	        logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("hh:mm:ss.SSS")
	        	.format(beginTime), request.getRequestURI());
		}
 		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
		if (modelAndView != null){
			logger.info("ViewName: " + modelAndView.getViewName());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

		if(ex == null && response.getStatus() != 200){
			String msg = response.getHeader(Constant.HEADER_ERROR);
			if(StringUtil.isEmpty(msg)){
				return ;
			}
			msg = java.net.URLDecoder.decode(msg, "utf-8");
			ex = new BusinessException(msg);
		}
		SysUser sysUser = SystemCacheUtil.getUserByRequest(request);
		if(sysUser != null){
			// 保存日志
			SysLogUtil.saveLog(request, sysUser, handler, ex, null);
		} 
//		else {
//			return;
//		}

		// 打印JVM信息。
		if (logger.isDebugEnabled()){
			long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）  
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
	        logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
	        		new SimpleDateFormat("hh:mm:ss.SSS").format(endTime), DateUtil.formatDateTime(endTime - beginTime),
					request.getRequestURI(), Runtime.getRuntime().maxMemory()/1024/1024, Runtime.getRuntime().totalMemory()/1024/1024, Runtime.getRuntime().freeMemory()/1024/1024, 
					(Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory())/1024/1024); 
		}
	}

}
