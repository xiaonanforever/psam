/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.request.TicketSaleRequest.java
 * 所含类: TicketSaleRequest.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月19日        zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.message.protocol.request;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.psam.message.protocol.PsamReq;
import com.topdot.psam.message.protocol.PsamReqHeader;

/**
 * <p>销售票请求</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class TicketSaleRequest extends PsamReq<PsamReqHeader, TicketSaleRequestBody> {

	public TicketSaleRequest() {
		this.setHeader(new PsamReqHeader());
		this.setBody(new TicketSaleRequestBody());
	}
	
	/**
	 * @param jsonStr
	 * @throws MessageFormateException
	 */
	public TicketSaleRequest(String jsonStr) throws MessageFormateException {
		this(jsonStr, true);
	}

	public TicketSaleRequest(String jsonStr, boolean parseBody) throws MessageFormateException {
		super(jsonStr, parseBody);
		if (includeBody) {
			if (bodyIndex + 1 < jsonStr.length() && parseBody) {
				super.setBody(new TicketSaleRequestBody(jsonStr.substring(bodyIndex + 1)));
			}
		}
	}

	@Override
	public TicketSaleRequest parse(String jsonStr) throws MessageFormateException {
		return new TicketSaleRequest(jsonStr);
	}
}
