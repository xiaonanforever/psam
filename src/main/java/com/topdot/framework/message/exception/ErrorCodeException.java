/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.exception.ErrorCodeException.java
 * 所含类: ErrorCodeException.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日7 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.exception;

import com.topdot.framework.exception.BaseException;

/**
 * <p>ErrorCode异常封装</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class ErrorCodeException extends BaseException {
	private static final long serialVersionUID = 1L;
	private final ErrorMessage errorMessage = new ErrorMessage(ErrorDefination.ERROR_UNNKONW);
	
	public ErrorCodeException() {
	}
	
	/**
	 * @param message
	 */
	public ErrorCodeException(String message) {
		super(message);
		errorMessage.setDetail(message);
	}
	
	/**
	 * @param cause
	 */
	public ErrorCodeException(Throwable cause) {
		super(cause);
		errorMessage.setDetail(cause.getMessage());
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public ErrorCodeException(String message, Throwable cause) {
		super(message, cause);
		errorMessage.setDetail(cause.getMessage());
	}
	
	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorDefination(ErrorDefination errorDefination) {
		errorMessage.setErrorDefination(errorDefination);
	}
	
}
