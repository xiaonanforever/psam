/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.exception.ServiceNotLogon.java
 * 所含类: ServiceNotLogon.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日2       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.exception;

import com.topdot.framework.exception.BaseLogicException;

/**
 * <p>服务没有登陆异常</p>
 * <p>服务在没有登陆的条件下执行call操作时抛出</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class ServiceNotLogonException extends BaseLogicException {

	private static final long serialVersionUID = 1172543325131233576L;

	public ServiceNotLogonException() {
		super("服务没有处理登陆状态!");
	}

	/**
	 * @param message
	 */
	public ServiceNotLogonException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ServiceNotLogonException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceNotLogonException(String message, Throwable cause) {
		super(message, cause);
	}
}
