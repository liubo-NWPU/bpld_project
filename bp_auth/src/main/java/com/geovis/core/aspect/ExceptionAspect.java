package com.geovis.core.aspect;

import com.geovis.core.common.exception.ForbiddenException;
import com.geovis.core.common.exception.NotFoundException;
import com.geovis.core.util.ResponseUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  title: 全局异常处理切面
 *  Description: 利用 @ControllerAdvice + @ExceptionHandler组合处理Controller层RuntimeException异常
 */
@ControllerAdvice
@ResponseBody
public class ExceptionAspect {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAspect.class);

    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        logger.error("could_not_read_json..."+e.getMessage());
        return ResponseUtil.failure(HttpStatus.NOT_FOUND.value(),"JSON 格式错误：" ,e.getMessage());
    }

    /**
     * 400 - Bad Request
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity handleValidationException(MethodArgumentNotValidException e) {
        logger.error("parameter_validation_exception..."+e.getMessage());
        return ResponseUtil.failure(HttpStatus.NOT_FOUND.value(),"parameter_validation_exception",null);
    }

    /**
     * 处理未发现的异常,比如用户名不存在等, 抛出http status 404
     * @param e
     * @return
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(Exception e) {
        logger.error("request_not_fond..."+e.getMessage());
        return ResponseUtil.failure(HttpStatus.NOT_FOUND.value(),"未找到资源！",null);
    }

    /**
     * 405 - Method Not Allowed。HttpRequestMethodNotSupportedException
     * 是ServletException的子类,需要Servlet API支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        logger.error("request_method_not_supported..."+e.getMessage());
        return ResponseUtil.failure(HttpStatus.METHOD_NOT_ALLOWED.value(),"不被支持的访问方法！",null);
    }

    /**
     * 415 - Unsupported Media Type。HttpMediaTypeNotSupportedException
     * 是ServletException的子类,需要Servlet API支持
     */
    @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
    public ResponseEntity handleHttpMediaTypeNotSupportedException(Exception e) {
        logger.error("content_type_not_supported..."+e.getMessage());
        return ResponseUtil.failure(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),"不支持的媒体类型！",e.getMessage());
    }

    /**
     * 403 - Forbidden
     * @param e
     * @return
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity handleForbiddenException(Exception e) {
        logger.error("request_forbidden..."+e.getMessage());
        return ResponseUtil.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage(),null);
    }

    /**
     * shiro认证异常
     * @param e
     * @return
     */
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity handleUnauthenticatedException(Exception e) {
        logger.error("Internal Server Error..."+e.getMessage());
        return ResponseUtil.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(),"身份认证失败，请重新登录！",e.getMessage());
    }

    /**
     * shiro 授权异常
     * @param e
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedException(Exception e) {
        logger.error("Internal Server Error..."+e.getMessage());
        return ResponseUtil.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(),"无权访问！",e.getMessage());
    }

    /**
     * 500 - shiro权限异常处理
     */
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity handleAuthorizationException(Exception e) {
        logger.error("Internal Server Error..."+e.getMessage());
        return ResponseUtil.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(),"身份认证失败！",e.getMessage());
    }

    /**
     * 500 - Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        logger.error("Internal Server Error..."+e.getMessage());
        return ResponseUtil.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(),"程序内部错误！",e.getMessage());
    }
}
