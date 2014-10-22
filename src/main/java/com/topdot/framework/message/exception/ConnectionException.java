/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.exception.ConnectionException.java
 * 所含类: ConnectionException.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2013-2-8       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.exception;


/**
 * <p>网络连接异常</p>
 * <p>连接服务或者断开连接时抛出</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class ConnectionException extends ErrorCodeException {

	private static final long serialVersionUID = 6512154386626753710L;

	public ConnectionException() {
		super("网络建立连接或释放连接时异常!");
		setErrorDefination(ErrorDefination.ERROR_CONNECT);
	}

	/**
	 * @param message
	 */
	public ConnectionException(String message) {
		super(message);
		setErrorDefination(ErrorDefination.ERROR_CONNECT);
	}

	/**
	 * @param cause
	 */
	public ConnectionException(Throwable cause) {
		super(cause);
		setErrorDefination(ErrorDefination.ERROR_CONNECT);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
		setErrorDefination(ErrorDefination.ERROR_CONNECT);
	}

}
