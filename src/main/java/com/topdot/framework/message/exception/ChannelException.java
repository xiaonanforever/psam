/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.exception.ChannelException.java
 * 所含类: ChannelException.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2013-2-8 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.exception;

/**
 * <p>通道异常</p>
 * <p>通道发送和接收消息时抛出</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class ChannelException extends RequestException {
	
	private static final long serialVersionUID = 2596829185921746452L;
	
	public ChannelException() {
		super("网络通道异常!");
		setErrorDefination(ErrorDefination.ERROR_REQ_CHANNEL);
	}
	
	/**
	 * @param message
	 */
	public ChannelException(String message) {
		super(message);
		setErrorDefination(ErrorDefination.ERROR_REQ_CHANNEL);
	}
	
	/**
	 * @param cause
	 */
	public ChannelException(Throwable cause) {
		super(cause);
		setErrorDefination(ErrorDefination.ERROR_REQ_CHANNEL);
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public ChannelException(String message, Throwable cause) {
		super(message, cause);
		setErrorDefination(ErrorDefination.ERROR_REQ_CHANNEL);
	}
}
