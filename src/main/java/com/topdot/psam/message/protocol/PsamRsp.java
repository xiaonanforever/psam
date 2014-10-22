/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.TxResponseMessage.java
 * 所含类: TxResponseMessage.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月19日       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.message.protocol;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.protocol.ResponseMessage;

/**
 * <p>Eit响应消息</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class PsamRsp extends ResponseMessage {

	public PsamRsp() {
		includeBody = false;
	}

	/**
	 * @param jsonStr
	 * @throws MessageFormateException 
	 */
	public PsamRsp(String jsonStr) throws MessageFormateException {
		this(jsonStr, true);
	}

	public PsamRsp(String jsonStr, boolean parseBody) throws MessageFormateException {
		super(jsonStr, parseBody);
		if (includeBody) {
			super.setHeader(new PsamRspHeader(jsonStr.substring(0, bodyIndex)));
			if (bodyIndex + 1 < jsonStr.length() && parseBody){
				super.setBody(new PsamRspBody(jsonStr.substring(bodyIndex + 1)));
			}
		} else {
			super.setHeader(new PsamRspHeader(jsonStr));
		}
	}

	@Override
	public PsamRsp parse(String jsonStr) throws MessageFormateException {
		return new PsamRsp(jsonStr);
	}

	@Override
	public PsamRspHeader getHeader() {
		return (PsamRspHeader) header;
	}

	@Override
	public PsamRspBody getBody() {
		return (PsamRspBody) body;
	}
}
