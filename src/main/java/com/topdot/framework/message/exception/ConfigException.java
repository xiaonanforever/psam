/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.gimp.eit.exception.ConfigException.java
 * 所含类: ConfigException.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.exception;

import com.topdot.framework.exception.BaseLogicException;

/**
 * <p>配置异常</p>
 * <p>系统配置或消息定义不正确时抛出</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class ConfigException extends BaseLogicException {

	private static final long serialVersionUID = 8507814177967922114L;

	public ConfigException() {
		super("服务配置错误!");
	}

	/**
	 * @param message
	 */
	public ConfigException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ConfigException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}

}
