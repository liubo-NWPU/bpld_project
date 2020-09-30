package com.geovis.core.common.exception;

/**
 *  查询时如果未查找到相关数据, 抛出该异常
 */
@SuppressWarnings("all")
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 8096927982277821689L;

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
