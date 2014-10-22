/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.vo.TransactionInfo.java
 * 所含类: TransactionID.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2013年2月18日       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.message.vo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>交易标示信息</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class TransactionInfo {

	/**账户编码*/
	private String accountNo;

	/**游戏编码*/
	private String gameNo;

	/**交易流水*/
	private String transactionSn;

	public TransactionInfo(JSONObject jsonMsg) throws JSONException {
		accountNo = jsonMsg.getString("AccountNo");
		gameNo = jsonMsg.getString("GameNo");
		transactionSn = jsonMsg.getString("TransactionSn");
	}

	public TransactionInfo() {
	}

	/**
	 * <p>附加相关属性到JSONObject</p>
	 * @param jsonObject
	 * @throws JSONException
	 * @return void
	 */
	public void appendTo(JSONObject jsonObject) throws JSONException {
		jsonObject.put("AccountNo", accountNo);
		jsonObject.put("TransactionSn", transactionSn);
		jsonObject.put("GameNo", gameNo);
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getGameNo() {
		return gameNo;
	}

	public void setGameNo(String gameNo) {
		this.gameNo = gameNo;
	}

	public String getTransactionSn() {
		return transactionSn;
	}

	public void setTransactionSn(String transactionSn) {
		this.transactionSn = transactionSn;
	}
}
