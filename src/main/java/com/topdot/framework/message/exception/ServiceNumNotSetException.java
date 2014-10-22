/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.exception.ServiceNumNotSetException.java
 * 所含类: ServiceNumNotSetException.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日2       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.exception;

import com.topdot.framework.exception.BaseLogicException;

/**
 * <p>需要启动的服务实例数目设置异常</p>
 * <p>服务实例数据<=0时抛出</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class ServiceNumNotSetException extends BaseLogicException {

	private static final long serialVersionUID = 1172543325131233576L;

	public ServiceNumNotSetException() {
		super("请设置需要启动服务实例数目(>0)!");
	}

	/**
	 * @param message
	 */
	public ServiceNumNotSetException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ServiceNumNotSetException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceNumNotSetException(String message, Throwable cause) {
		super(message, cause);
	}
}
