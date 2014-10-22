/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.gimp.eit.exception.MessageFormateException.java
 * 所含类: MessageFormateException.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.exception;

import com.topdot.framework.exception.BaseException;

/**
 * <p>消息格式异常</p>
 * <p>处理JSON格式时出现异常抛出</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class MessageFormateException extends BaseException {

	private static final long serialVersionUID = -4158018600441994816L;

	public MessageFormateException() {
		super("消息格式错误!");
	}

	/**
	 * @param message
	 */
	public MessageFormateException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MessageFormateException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MessageFormateException(String message, Throwable cause) {
		super(message, cause);
	}

}
