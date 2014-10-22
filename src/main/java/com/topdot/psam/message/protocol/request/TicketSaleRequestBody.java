/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.request.TicketSaleMessageBody.java
 * 所含类: TicketSaleMessageBody.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月19日        zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.message.protocol.request;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.util.DateUtils;
import com.topdot.psam.message.protocol.PsamReqBody;
import com.topdot.psam.message.vo.RngParameter;
import com.topdot.psam.message.vo.TicketInfo;
import com.topdot.psam.message.vo.TransactionInfo;

/**
 * <p>销售票请求包体</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class TicketSaleRequestBody extends PsamReqBody {

	/**交易信息*/
	private TransactionInfo transactionInfo;

	/**票信息*/
	private TicketInfo ticketInfo;

	/**是否获取随机数*/
	private boolean isNeedRandomNumber;

	/**随机数请求参数*/
	private RngParameter rngParameter;
	
	/**交易请求时间*/
	private Date requestTime;

	public TicketSaleRequestBody() {
	}

	public TicketSaleRequestBody(String jsonStr) throws MessageFormateException {
		super(jsonStr);
		try {
			this.transactionInfo = new TransactionInfo();
			this.transactionInfo.setAccountNo(jsonMsg.getString("AccountNo"));
			this.transactionInfo.setTransactionSn(jsonMsg.getString("TransactionSn"));
			this.transactionInfo.setGameNo(jsonMsg.getString("GameNo"));
			this.ticketInfo = new TicketInfo(jsonMsg.getJSONObject("TicketInfo"));
			this.isNeedRandomNumber = jsonMsg.getBoolean("IsNeedRandomNumber");
			this.rngParameter = new RngParameter(jsonMsg.getJSONObject("RngParameter"));
			this.requestTime = DateUtils.parseDateTime(jsonMsg.getString("RequestTime"));
		} catch (JSONException e) {
			throw new MessageFormateException("通过JSON对象获取TicketSaleRequestBody异常", e);
		} catch (ParseException e) {
			throw new MessageFormateException("通过JSON对象获取TicketSaleRequestBody异常", e);
		}
	}

	@Override
	public JSONObject buildReqJSONObject() throws MessageFormateException {
		JSONObject jsonObject = super.buildReqJSONObject();
		try {
			transactionInfo.appendTo(jsonObject);
			jsonObject.put("TicketInfo", ticketInfo.getJSONObject());
			jsonObject.put("IsNeedRandomNumber", isNeedRandomNumber);
			jsonObject.put("RngParameter", rngParameter.getJSONObject());
			jsonObject.put("RequestTime",DateUtils.getDateTimeString(requestTime));
		} catch (JSONException e) {
			throw new MessageFormateException("构建JSON消息时异常", e);
		}
		return jsonObject;// JSONObject留做后面继续添加
	}

	public TicketInfo getTicketInfo() {
		return ticketInfo;
	}

	public void setTicketInfo(TicketInfo ticketInfo) {
		this.ticketInfo = ticketInfo;
	}

	public boolean isNeedRandomNumber() {
		return isNeedRandomNumber;
	}

	public void setNeedRandomNumber(boolean isNeedRandomNumber) {
		this.isNeedRandomNumber = isNeedRandomNumber;
	}

	public RngParameter getRngParameter() {
		return rngParameter;
	}

	public void setRngParameter(RngParameter rngParameter) {
		this.rngParameter = rngParameter;
	}

	public TransactionInfo getTransactionInfo() {
		return transactionInfo;
	}

	public void setTransactionInfo(TransactionInfo transactionInfo) {
		this.transactionInfo = transactionInfo;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
}
