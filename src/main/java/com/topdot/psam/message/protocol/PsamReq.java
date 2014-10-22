/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.EitRequestMessage.java
 * 所含类: EitRequestMessage.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.message.protocol;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.protocol.RequestMessage;

/**
 * <p>Eit请求标示</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class PsamReq<H extends PsamReqHeader, B extends PsamReqBody> extends RequestMessage<H, B> {

	public PsamReq() {
		
	}
	/**
	 * @param jsonStr
	 * @throws MessageFormateException
	 */
	private PsamReq(String jsonStr) throws MessageFormateException {
		this(jsonStr, true);
	}

	@SuppressWarnings("unchecked")
	protected PsamReq(String jsonStr, boolean parseBody) throws MessageFormateException {
		super(jsonStr, parseBody);
		if (includeBody) {
			super.setHeader((H) new PsamReqHeader(jsonStr.substring(0, bodyIndex)));
		} else {
			super.setHeader((H) new PsamReqHeader(jsonStr));
		}
	}

	@Override
	public PsamReq<H,B> parse(String jsonStr) throws MessageFormateException {
		return new PsamReq<H,B>(jsonStr);
	}

}
