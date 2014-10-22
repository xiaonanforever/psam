/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.response.TicketInfoMessageBody.java
 * 所含类: TicketInfoMessageBody.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月19日        zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.message.protocol.response;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.psam.message.protocol.PsamRspBody;
import com.topdot.psam.message.vo.TicketInfo;
import com.topdot.psam.message.vo.TransactionInfo;

/**
 * <p>销售票查询响应包体</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class TicketInfoResponseBody extends PsamRspBody {

	/**签名字段*/
	private String signature;

	/**票信息*/
	private TicketInfo ticketInfo;
	
	/**交易标示*/
	private TransactionInfo transactionInfo;

	public TicketInfoResponseBody() {
	}

	/**
	 * @param jsonStr
	 * @throws MessageFormateException
	 */
	public TicketInfoResponseBody(String jsonStr) throws MessageFormateException {
		super(jsonStr);
		try {
			transactionInfo = new TransactionInfo(jsonMsg);
			this.signature = jsonMsg.getString("Signature");
			this.ticketInfo = new TicketInfo(jsonMsg.getJSONObject("TicketInfo"));
		} catch (JSONException e) {
			throw new MessageFormateException("通过JSON对象获取TicketInfoMessageBody异常", e);
		} catch (ParseException e) {
			throw new MessageFormateException("通过JSON对象获取ticketInfo异常,时间格式不正确[yyyy-mm-dd hh:mi:ss]", e);
		}
	}
	
	@Override
	public JSONObject buildReqJSONObject() throws MessageFormateException {
		JSONObject jsonObject = super.buildReqJSONObject();
		try {
			transactionInfo.appendTo(jsonObject);
			jsonObject.put("TicketInfo", ticketInfo.getJSONObject());
			jsonObject.put("Signature", signature);
		} catch (JSONException e) {
			throw new MessageFormateException("TicketInfoResponseBody构建JSON消息时异常", e);
		}
		return jsonObject;// JSONObject留做后面继续添加
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public TicketInfo getTicketInfo() {
		return ticketInfo;
	}

	public void setTicketInfo(TicketInfo ticketInfo) {
		this.ticketInfo = ticketInfo;
	}

	public TransactionInfo getTransactionInfo() {
		return transactionInfo;
	}

	public void setTransactionInfo(TransactionInfo transactionInfo) {
		this.transactionInfo = transactionInfo;
	}
}
