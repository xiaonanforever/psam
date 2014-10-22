/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.vo.PrizeInfo.java
 * 所含类: PrizeInfo.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2013年1月19日       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.message.vo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>中奖信息</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class PrizeInfo {

	/**是否大奖*/
	private boolean isBigPrize;
	/**奖级名称*/
	private String prizeLevelName = "";
	/**兑奖操作员*/
	private String paidOperatorId = "";
	/**兑奖地点*/
	private String paidAddress = "";
	/**中奖人信息*/
	private String winnerInfo = "";
	/**中奖金额*/
	private double prizeAmount;
	/**缴税金额*/
	private double taxAmount;
	/**兑奖金额*/
	private double paidAmount;
	/**实际兑奖金额*/
	private double actualPaidAmount;
	/**奖级版本号*/
	private String prizeLevelVersion = "";

	public PrizeInfo() {
	}

	public JSONObject getJSONObject() throws JSONException {
		JSONObject jo = new JSONObject();
		jo.put("PrizeAmount", prizeAmount);
		jo.put("TaxAmount", taxAmount);
		jo.put("PaidAmount", paidAmount);
		jo.put("ActualPaidAmount", actualPaidAmount);
		jo.put("PrizeLevelVersion", prizeLevelVersion);
		jo.put("IsBigPrize", isBigPrize);
		jo.put("PrizeLevelName", prizeLevelName);
		jo.put("PaidOperatorId", paidOperatorId);
		jo.put("PaidAddress", paidAddress);
		jo.put("WinnerInfo", winnerInfo);
		return jo;
	}

	public boolean isBigPrize() {
		return isBigPrize;
	}

	public void setBigPrize(boolean isBigPrize) {
		this.isBigPrize = isBigPrize;
	}

	public String getPrizeLevelName() {
		return prizeLevelName;
	}

	public void setPrizeLevelName(String prizeLevelName) {
		this.prizeLevelName = prizeLevelName;
	}

	public String getPaidOperatorId() {
		return paidOperatorId;
	}

	public void setPaidOperatorId(String paidOperatorId) {
		this.paidOperatorId = paidOperatorId;
	}

	public String getPaidAddress() {
		return paidAddress;
	}

	public void setPaidAddress(String paidAddress) {
		this.paidAddress = paidAddress;
	}

	public String getWinnerInfo() {
		return winnerInfo;
	}

	public void setWinnerInfo(String winnerInfo) {
		this.winnerInfo = winnerInfo;
	}

	public double getPrizeAmount() {
		return prizeAmount;
	}

	public void setPrizeAmount(double prizeAmount) {
		this.prizeAmount = prizeAmount;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public double getActualPaidAmount() {
		return actualPaidAmount;
	}

	public void setActualPaidAmount(double actualPaidAmount) {
		this.actualPaidAmount = actualPaidAmount;
	}

	public String getPrizeLevelVersion() {
		return prizeLevelVersion;
	}

	public void setPrizeLevelVersion(String prizeLevelVersion) {
		this.prizeLevelVersion = prizeLevelVersion;
	}
}
