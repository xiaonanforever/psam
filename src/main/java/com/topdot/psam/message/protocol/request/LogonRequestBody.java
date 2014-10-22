/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.request.LogonMessageBody.java
 * 所含类: LogonMessageBody.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.message.protocol.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.psam.message.protocol.PsamReqBody;

/**
 * <p>终端登陆包体</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class LogonRequestBody extends PsamReqBody {

	/**终端编号*/
	private String terminalNo;

	/**终端密码/签名信息*/
	private String terminalPassword;

	public LogonRequestBody() {
	}

	public LogonRequestBody(String jsonStr) throws MessageFormateException {
		super(jsonStr);
		try {
			this.terminalNo = jsonMsg.getString("TerminalNo");
			this.terminalPassword = jsonMsg.getString("TerminalPassword");
		} catch (JSONException e) {
			throw new MessageFormateException("通过JSON对象获取LogonRequestBody异常", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.topdot.gimp.eit.message.MessageBody#buildReqBodyJSONObject()
	 */
	@Override
	public JSONObject buildReqJSONObject() throws MessageFormateException {
		JSONObject jsonObject = super.buildReqJSONObject();
		try {
			jsonObject.put("TerminalNo", terminalNo);
			jsonObject.put("TerminalPassword", terminalPassword);
		} catch (JSONException e) {
			throw new MessageFormateException("构建JSON消息时异常", e);
		}
		return jsonObject;// JSONObject留做后面继续添加
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getTerminalPassword() {
		return terminalPassword;
	}

	public void setTerminalPassword(String terminalPassword) {
		this.terminalPassword = terminalPassword;
	}
}
