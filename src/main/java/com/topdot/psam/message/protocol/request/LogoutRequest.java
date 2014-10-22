/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.request.LogoutRequest.java
 * 所含类: LogoutRequest.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月19日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.message.protocol.request;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.psam.message.protocol.PsamReqBody;
import com.topdot.psam.message.protocol.PsamReqHeader;
import com.topdot.psam.message.protocol.PsamReq;

/**
 * <p>终端登出</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class LogoutRequest extends PsamReq<PsamReqHeader, PsamReqBody> {

	public LogoutRequest() {
		this.setHeader(new PsamReqHeader());
		this.setBody(null);
	}

	/**
	 * @param jsonStr
	 * @throws MessageFormateException
	 */
	public LogoutRequest(String jsonStr) throws MessageFormateException {
		this(jsonStr, false);
	}

	public LogoutRequest(String jsonStr, boolean parseBody) throws MessageFormateException {
		super(jsonStr, parseBody);
	}

	@Override
	public LogoutRequest parse(String jsonStr) throws MessageFormateException {
		return new LogoutRequest(jsonStr);
	}
}
