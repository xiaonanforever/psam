/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.TransactionMesageHeaderImpl.java
 * 所含类: TransactionMesageHeaderImpl.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.message.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.topdot.framework.message.exception.MessageFormateException;

/**
 * <p>Eit请求业务公共请求头</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class PsamReqHeader extends PsamHeader {
	
	/** 签名用的SN */
	private String sn;
	
	/** 终端版本号 */
	private String versionNo;
	
	public PsamReqHeader() {
	}
	
	@Override
	public JSONObject buildReqJSONObject() throws MessageFormateException {
		JSONObject jo = super.buildReqJSONObject();
		try {
			jo.put("Sn", sn);
			jo.putOpt("VersionNo", versionNo);
		} catch (JSONException e) {
			throw new MessageFormateException("EitReqMessageHeader构建JSON消息时异常", e);
		}
		return jo;
	}
	
	/**
	 * @param jsonStr
	 * @throws MessageFormateException 
	 */
	public PsamReqHeader(String jsonStr) throws MessageFormateException {
		super(jsonStr);
		try {
			this.sn = jsonMsg.getString("Sn");
			this.versionNo = jsonMsg.getString("VersionNo");
		} catch (JSONException e) {
			throw new MessageFormateException("通过JSON对象构建TxRspMessageHeader对象时异常", e);
		}
	}
	
	public String getSn() {
		return sn;
	}
	
	public void setSn(String sn) {
		this.sn = sn;
	}
	
	public String getVersionNo() {
		return versionNo;
	}
	
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
}
