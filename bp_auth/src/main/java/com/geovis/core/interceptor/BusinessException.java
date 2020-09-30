package com.geovis.core.interceptor;

public class BusinessException extends Exception{

    /**
	 * 
	 */
	private static final long serialVersionUID = 11222112L;

	public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }


}