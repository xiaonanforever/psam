/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.exception.MessageFilterException.java
 * 所含类: MessageFilterException.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日9       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.exception;


/**
 * <p>消息过滤异常</p>
 * <p>执行消息过滤时发生</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class MessageFilterException extends ErrorCodeException {

	private static final long serialVersionUID = 1816223565363563615L;

	public MessageFilterException() {
		this("消息过滤异常");
	}

	/**
	 * @param message
	 */
	public MessageFilterException(String message) {
		super(message);
		setErrorDefination(ErrorDefination.ERROR_MSG_FILTER);
	}

	/**
	 * @param cause
	 */
	public MessageFilterException(Throwable cause) {
		super(cause);
		setErrorMessage(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MessageFilterException(String message, Throwable cause) {
		super(message, cause);
		setErrorMessage(cause);
	}
	
	private void setErrorMessage(Throwable cause){
		if(cause instanceof ErrorCodeException){
			ErrorMessage  em = ((ErrorCodeException) cause).getErrorMessage();
			if(em!=null){
				setErrorDefination(em.getErrorDefination());
			}
		}else{
			setErrorDefination(ErrorDefination.ERROR_MSG_FILTER);
		}
	}

}