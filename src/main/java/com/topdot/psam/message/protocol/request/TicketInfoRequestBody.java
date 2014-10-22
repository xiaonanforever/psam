/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.request.TicketInfoMessageBody.java
 * 所含类: TicketInfoMessageBody.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月19日  zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.message.protocol.request;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.util.DateUtils;
import com.topdot.psam.message.protocol.PsamReqBody;

/**
 * <p>销售票查询</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class TicketInfoRequestBody extends PsamReqBody {
	
	/** 交易流水 */
	private String transactionSn;
	
	/** 账户编码 */
	private String accountNo;
	
	/**游戏类型*/
	private int	gameType;
	
	/**对应交易流水号的销售票请求时间*/
	private Date requestTime;
	
	public TicketInfoRequestBody() {
	}
	
	public TicketInfoRequestBody(String jsonStr) throws MessageFormateException {
		super(jsonStr);
		try {
			this.transactionSn = jsonMsg.getString("TransactionSn");
			this.accountNo = jsonMsg.getString("AccountNo");
			this.gameType = jsonMsg.getInt("GameType");
			this.requestTime = DateUtils.parseDateTime(jsonMsg.getString("RequestTime"));
		} catch (JSONException e) {
			throw new MessageFormateException("通过JSON对象获取TicketInfoRequestBody异常", e);
		} catch (ParseException e) {
			throw new MessageFormateException("通过JSON对象获取TicketInfoRequestBody异常", e);
		}
	}

	@Override
	public JSONObject buildReqJSONObject() throws MessageFormateException {
		JSONObject jsonObject = super.buildReqJSONObject();
		try {
			jsonObject.put("TransactionSn", transactionSn);
			jsonObject.put("AccountNo", accountNo);
			jsonObject.put("GameType", gameType);
			jsonObject.put("RequestTime",DateUtils.getDateTimeString(requestTime));
		} catch (JSONException e) {
			throw new MessageFormateException("构建JSON消息时异常", e);
		}
		return jsonObject;// JSONObject留做后面继续添加
	}
	
	public String getTransactionSn() {
		return transactionSn;
	}
	
	public void setTransactionSn(String transactionSn) {
		this.transactionSn = transactionSn;
	}
	
	public String getAccountNo() {
		return accountNo;
	}
	
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
}
