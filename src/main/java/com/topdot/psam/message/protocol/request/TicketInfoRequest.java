/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.request.TicketInfoRequest.java
 * 所含类: TicketInfoRequest.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月19日  zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.message.protocol.request;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.psam.message.protocol.PsamReq;
import com.topdot.psam.message.protocol.PsamReqHeader;

/**
 * <p>销售票查询</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class TicketInfoRequest extends PsamReq<PsamReqHeader, TicketInfoRequestBody> {

	public TicketInfoRequest() {
		this.setHeader(new PsamReqHeader());
		this.setBody(new TicketInfoRequestBody());
	}

	public TicketInfoRequest(String jsonStr) throws MessageFormateException {
		this(jsonStr, true);
	}

	public TicketInfoRequest(String jsonStr, boolean parseBody) throws MessageFormateException {
		super(jsonStr, parseBody);
		if (includeBody) {
			if (bodyIndex + 1 < jsonStr.length() && parseBody) {
				super.setBody(new TicketInfoRequestBody(jsonStr.substring(bodyIndex + 1)));
			}
		}
	}

	@Override
	public TicketInfoRequest parse(String jsonStr) throws MessageFormateException {
		return new TicketInfoRequest(jsonStr);
	}
}
