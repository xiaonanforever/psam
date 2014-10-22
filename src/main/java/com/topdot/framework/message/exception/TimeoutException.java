/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.cslc.framework.messaging.exception.TimeoutException.java
 * 所含类: TimeoutException.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2012-4-11       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.exception;


/**
 * <p> TimeoutException </p>
 * 
 * <p> 同步请求超时异常 </p>
 * <p> Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p> Company: xxx </p>
 *
 * @author zhangyongxin
 * @version 1.0
 */

public class TimeoutException extends RequestException {

	private static final long serialVersionUID = 5757592931997658538L;

	public TimeoutException() {
		super("执行超时");
		setErrorDefination(ErrorDefination.ERROR_REQ_TIMEOUT);
	}

	/**
	 * @param s
	 */
	public TimeoutException(String s) {
		super(s);
		setErrorDefination(ErrorDefination.ERROR_REQ_TIMEOUT);
	}
}
