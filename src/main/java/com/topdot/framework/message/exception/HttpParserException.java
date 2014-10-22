/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.gimp.eit.exception.HttpParserException.java
 * 所含类: HttpParserException.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.exception;

import com.topdot.framework.exception.BaseException;

/**
 * <p>HTTP报文解析异常</p>
 * <p>解析服务端返回的HTTP报文信息时抛出</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class HttpParserException extends BaseException {

	private static final long serialVersionUID = 6476876400402901762L;

	public HttpParserException() {
		super("HTTP报文解析异常!");
	}

	/**
	 * @param message
	 */
	public HttpParserException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public HttpParserException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public HttpParserException(String message, Throwable cause) {
		super(message, cause);
	}
}
