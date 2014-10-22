/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.protocol.MessageBody.java
 * 所含类: MessageBody.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2012-4-25       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.util.CslcJSON;

/**
 * <p>MessageBody </p>
 * 
 * <p> 消息体</p>
 * 
 * <p> Copyright: 版权所有 (c) 2010 - 2014 </p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class MessageBody {
	/** 用于响应-记录JSON格式响应消息,MessageBody获取本部消息后，交由子类获取其他消息*/
	protected JSONObject jsonMsg;
	/**错误消息*/
	protected String errorMessage;

	public MessageBody() {
	}

	/**
	 * <p>用JSON字符串构建响应对象</p>
	 * @param jsonStr
	 * @throws MessageFormateException
	 */
	public MessageBody(String jsonStr) throws MessageFormateException {
		try {
			jsonMsg = new JSONObject(jsonStr);
			this.errorMessage = jsonMsg.getString("ErrorMessage");
		} catch (JSONException e) {
			throw new MessageFormateException("通过JSON字符串"+jsonStr+"构建MessageBody对象时异常", e);
		}
	}
	public JSONObject buildReqJSONObject() throws MessageFormateException {
		JSONObject jsonObject = new CslcJSON();
		return jsonObject;
	}

	public JSONObject getJsonMsg() {
		return jsonMsg;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	//为了Executor处理方便,增加
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
