package com.topdot.framework.exception;

/**
 * 
 * <p>异常基类</p>
 * 
 * <p>类用途详细说明 </p>
 * 
 * <p> Copyright: 版权所有 (c) 2010 - 2014 </p>
 * <p> Company: xxx</p>
 *
 * @author wanglinzi
 * @version 1.0 
 */
public class BaseException extends Exception {
	private static final long serialVersionUID = -5360246742497979708L;
	public BaseException() {
		super("应用异常!");
	}
	/**
	 * @param message
	 */
	public BaseException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public BaseException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 重载异常栈填充方法，如果是普通业务异常则直接返回null，否则采用异常处理的缺省方法
	 */
	@Override
	public Throwable fillInStackTrace() {
		if (super.getCause() == null){
			return null;
		}else{
			return super.fillInStackTrace();
		}
	}
}
