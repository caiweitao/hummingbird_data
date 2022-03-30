package com.caiweitao.data.exception;
/**
 * @author caiweitao
 * @Date 2022年3月30日
 * @Description 缓存查询异常
 */
public class CacheSelectException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CacheSelectException () {
		super();
	}
	
	public CacheSelectException (String msg) {
		super(msg);
	}
	
	public CacheSelectException(Throwable cause) {
        super(cause);
    }
	
	public CacheSelectException(String message, Throwable cause) {
        super(message, cause);
    }

}
