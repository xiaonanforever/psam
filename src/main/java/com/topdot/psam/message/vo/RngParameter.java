/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.vo.RngParameter.java
 * 所含类: RngParameter.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2013年1月19日        zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.message.vo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>随机数参数</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class RngParameter {

	/**随机数最小值*/
	private int minValue;

	/**随机数最大值*/
	private int maxValue;

	/**随机数个数*/
	private int randomNumberCount;

	/**随机数是否允许重复*/
	private boolean isAllowRepeated;

	/**是否允许多次获取随机数*/
	private boolean isAllowMultipleRequest;

	public RngParameter() {
	}

	public RngParameter(JSONObject jo) throws JSONException {
		this.minValue = jo.getInt("MinValue");
		this.maxValue = jo.getInt("MaxValue");
		this.randomNumberCount = jo.getInt("RandomNumberCount");
		this.isAllowRepeated = jo.getBoolean("IsAllowRepeated");
		this.isAllowMultipleRequest = jo.getBoolean("IsAllowMultipleRequest");
	}

	public JSONObject getJSONObject() throws JSONException {
		JSONObject jo = new JSONObject();
		jo.put("MinValue", minValue);
		jo.put("MaxValue", maxValue);
		jo.put("RandomNumberCount", randomNumberCount);
		jo.put("IsAllowRepeated", isAllowRepeated);
		jo.put("IsAllowMultipleRequest", isAllowMultipleRequest);
		return jo;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getRandomNumberCount() {
		return randomNumberCount;
	}

	public void setRandomNumberCount(int randomNumberCount) {
		this.randomNumberCount = randomNumberCount;
	}

	public boolean isAllowRepeated() {
		return isAllowRepeated;
	}

	public void setAllowRepeated(boolean isAllowRepeated) {
		this.isAllowRepeated = isAllowRepeated;
	}

	public boolean isAllowMultipleRequest() {
		return isAllowMultipleRequest;
	}

	public void setAllowMultipleRequest(boolean isAllowMultipleRequest) {
		this.isAllowMultipleRequest = isAllowMultipleRequest;
	}
}
