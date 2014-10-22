/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.vo.TicketInfo.java
 * 所含类: TicketInfo.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2013年2月19日        zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.message.vo;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>票信息</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class TicketInfo {

	/***/
	private String ticketNo;
	/**售票序列号*/
	private String lotterySn = "";
	/**票面内容*/
	private String stakeDetails = "";
	/**投注金额*/
	private double lotteryAmount;
	/**投注倍数*/
	private int multiBetCount;
	/**步骤序号*/
	private int stepNo;
	/**出票时间*/
	private String saleTime;
	/**注数*/
	private int stakeCount;
	/**加密版本号*/
	private String encryptionVersion = "";
	/**加密信息*/
	private String encryptionKey = "";

	public TicketInfo() {
	}

	public TicketInfo(JSONObject jo) throws JSONException, ParseException {
		this.ticketNo = jo.getString("TicketNo");
		this.lotterySn = jo.getString("LotterySn");
		this.stakeDetails = jo.getString("StakeDetails");
		this.lotteryAmount = jo.getDouble("LotteryAmount");
		this.multiBetCount = jo.getInt("MultiBetCount");
		this.stepNo = jo.getInt("StepNo");
		this.saleTime = jo.getString("SaleTime");
		this.stakeCount = jo.getInt("StakeCount");
		this.encryptionVersion = jo.getString("EncryptionVersion");
		this.encryptionKey = jo.getString("EncryptionKey");
	}

	public JSONObject getJSONObject() throws JSONException {
		JSONObject jo = new JSONObject();
		jo.put("TicketNo", ticketNo);
		jo.put("LotterySn", lotterySn);
		jo.put("StakeDetails", stakeDetails);
		jo.put("LotteryAmount", lotteryAmount);
		jo.put("MultiBetCount", multiBetCount);
		jo.put("StepNo", stepNo);
		jo.put("SaleTime",saleTime);
		jo.put("StakeCount", stakeCount);
		jo.put("EncryptionVersion", encryptionVersion);
		jo.put("EncryptionKey", encryptionKey);
		return jo;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getLotterySn() {
		return lotterySn;
	}

	public void setLotterySn(String lotterySn) {
		this.lotterySn = lotterySn;
	}

	public String getStakeDetails() {
		return stakeDetails;
	}

	public void setStakeDetails(String stakeDetails) {
		this.stakeDetails = stakeDetails;
	}

	public double getLotteryAmount() {
		return lotteryAmount;
	}

	public void setLotteryAmount(double lotteryAmount) {
		this.lotteryAmount = lotteryAmount;
	}

	public int getMultiBetCount() {
		return multiBetCount;
	}

	public void setMultiBetCount(int multiBetCount) {
		this.multiBetCount = multiBetCount;
	}

	public int getStepNo() {
		return stepNo;
	}

	public void setStepNo(int stepNo) {
		this.stepNo = stepNo;
	}

	public String getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(String saleTime) {
		this.saleTime = saleTime;
	}

	public int getStakeCount() {
		return stakeCount;
	}

	public void setStakeCount(int stakeCount) {
		this.stakeCount = stakeCount;
	}

	public String getEncryptionVersion() {
		return encryptionVersion;
	}

	public void setEncryptionVersion(String encryptionVersion) {
		this.encryptionVersion = encryptionVersion;
	}

	public String getEncryptionKey() {
		return encryptionKey;
	}

	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

}
