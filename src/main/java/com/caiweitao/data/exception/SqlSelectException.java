package com.caiweitao.data.exception;
/**
 * @author caiweitao
 * @Date 2022年3月30日
 * @Description sql查询异常
 */
public class SqlSelectException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SqlSelectException () {
		super();
	}
	
	public SqlSelectException (String msg) {
		super(msg);
	}
	
	public SqlSelectException(Throwable cause) {
        super(cause);
    }
	
	public SqlSelectException(String message, Throwable cause) {
        super(message, cause);
    }

}
