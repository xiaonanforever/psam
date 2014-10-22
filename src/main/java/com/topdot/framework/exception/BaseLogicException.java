/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.exception.BaseLogicException.java
 * 所含类: BaseLogicException.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日1       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.exception;

/**
 * <p>应用逻辑异常</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class BaseLogicException extends RuntimeException {

	private static final long serialVersionUID = 1184816491719509594L;

	public BaseLogicException() {
		super("应用逻辑异常!");
	}

	/**
	 * @param message
	 */
	public BaseLogicException(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public BaseLogicException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BaseLogicException(String message, Throwable cause) {
		super(message, cause);
	}

}
