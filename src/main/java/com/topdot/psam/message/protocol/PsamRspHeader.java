/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.PsamRspHeader.java
 * 所含类: PsamRspHeader.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.message.protocol;

import org.json.JSONException;

import com.topdot.framework.message.exception.MessageFormateException;

/**
 * <p>PsamRspHeader</p>
 * <p>响应消息公共头</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class PsamRspHeader extends PsamHeader {

	/**其他*/
	private String other;

	public PsamRspHeader() {
	}

	/**
	 * @param jsonStr
	 * @throws MessageFormateException 
	 */
	public PsamRspHeader(String jsonStr) throws MessageFormateException {
		super(jsonStr);
		try {
			this.other = jsonMsg.getString("Other");
			
		} catch (JSONException e) {
			throw new MessageFormateException("通过JSON对象构建TxRspMessageHeader对象时异常", e);
		}
	}

	public String getOther() {
		return other;
	}
}
