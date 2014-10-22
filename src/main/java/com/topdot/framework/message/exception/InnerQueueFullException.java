/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.cslc.framework.messaging.exception.InnerQueueOverflowException.java
 * 所含类: InnerQueueOverflowException.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2012-4-26       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.exception;

import com.topdot.framework.exception.BaseException;

/**
 * <p> InnerQueueFullException </p>
 * 
 * <p> 内部队列，用于内部请求和响应队列满时产生异常 </p>
 * 
 * <p> Copyright: 版权所有 (c) 2010 - 2014 </p>
 * <p> Company: xxx </p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class InnerQueueFullException extends BaseException {

	private static final long serialVersionUID = 857879297952556547L;

	public InnerQueueFullException() {
		super("内部队列已满!");
	}

	/**
	 * @param message
	 */
	public InnerQueueFullException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InnerQueueFullException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InnerQueueFullException(String message, Throwable cause) {
		super(message, cause);
	}
}