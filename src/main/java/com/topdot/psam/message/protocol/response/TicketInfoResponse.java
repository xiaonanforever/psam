/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.response.TicketInfoResponse.java
 * 所含类: TicketInfoResponse.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月19日        zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.message.protocol.response;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.psam.message.protocol.PsamRsp;

/**
 * <p>销售票查询响应</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class TicketInfoResponse extends PsamRsp {

	public TicketInfoResponse() {
	}
	/**
	 * @param jsonStr
	 * @throws MessageFormateException 
	 */
	public TicketInfoResponse(String jsonStr) throws MessageFormateException {
		super(jsonStr, false);
		if (includeBody) {
			super.setBody(new TicketInfoResponseBody(jsonStr.substring(bodyIndex + 1)));
		}
	}

	@Override
	public TicketInfoResponse parse(String jsonStr) throws MessageFormateException {
		return new TicketInfoResponse(jsonStr);
	}

	@Override
	public TicketInfoResponseBody getBody() {
		return (TicketInfoResponseBody) body;
	}
}
