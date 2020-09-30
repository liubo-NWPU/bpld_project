package com.geovis.core.util;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("all")
public class ResponseUtil {

    /**
     * 成功处理封装
     *
     * @return
     */
    public static ResponseEntity success() {
        return success(null);
    }

    /**
     * 成功处理封装
     *
     * @param body
     * @return
     */
    public static ResponseEntity success(Object body) {
        return ResponseEntity.ok().body(body);
    }
    /**
     * 成功下载文件处理封装
     *
     * @param body
     * @return
     */
    public static ResponseEntity success(HttpHeaders headers, Resource resource) {
        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/x-msdownload")).body(resource);

    }

    /**
     * 成功处理封装
     *
     * @param message
     * @param body
     * @return
     */
    public static ResponseEntity success(String message, Object body) {
        return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert(message))
                .body(body);
    }

    /**
     * 失败处理
     *
     * @param message
     * @return
     */
    public static ResponseEntity failure(String message) {
        return failure(message, null);
    }

    /**
     * 失败处理
     *
     * @param message
     * @param body
     * @return
     */
    public static ResponseEntity failure(String message, Object body) {
        return failure(HttpStatus.BAD_REQUEST.value(), message, body);
    }


    /**
     * 失败处理+验证码
     *
     * @param message
     * @param body
     * @return
     */
    public static ResponseEntity failureYzm(String message, Object body) {
        return failure(HttpStatus.BAD_REQUEST.value(), message, body);
    }




    public static ResponseEntity failure(int status, String message, Object body) {
        return ResponseEntity.status(status).
                headers(HeaderUtil.createFailureAlert(message))
                .body(body);
    }

}
