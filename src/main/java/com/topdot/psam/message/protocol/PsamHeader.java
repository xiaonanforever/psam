/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.TxMessageHeader.java
 * 所含类: TxMessageHeader.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月19日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.message.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.protocol.MessageHeader;

/**
 * <p>Eit请求/响应消息公共Header</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class PsamHeader extends MessageHeader {
	
	/** 终端编号 */
	private String terminalNo;
	
	/** 终端ID */
	private Integer terminalId;
	
	/** 终端IP */
	private String terminalIp;
	
	/** 省ID */
	private Integer provinceId;
	
	/** 跟踪级别 */
	private Integer traceLevel;
	
	/** Server标识 ,服务端传过来的*/
	private String serverId;
	
	/** 应用类型（子系统）ID */
	private Integer appTypeId;
	
	/**会话色素sionId*/
	private String sessionId;
	
	public PsamHeader() {
	}
	
	/**
	 * @param jsonStr
	 * @throws MessageFormateException
	 */
	public PsamHeader(String jsonStr) throws MessageFormateException {
		super(jsonStr);
		try {
			this.terminalNo = jsonMsg.getString("TerminalNo");
			this.terminalId = jsonMsg.getInt("TerminalId");
			this.terminalIp = jsonMsg.getString("TerminalIp");
			this.provinceId = jsonMsg.getInt("ProvinceId");
			this.traceLevel = jsonMsg.getInt("TraceLevel");
			this.serverId = jsonMsg.getString("ServerId");
			this.appTypeId = jsonMsg.getInt("AppTypeId");
			this.sessionId = jsonMsg.getString("SessionId");
		} catch (JSONException e) {
			throw new MessageFormateException("利用JSON对象构建TxMessageHeader出现异常", e);
		}
	}
	
	@Override
	public JSONObject buildReqJSONObject() throws MessageFormateException {
		JSONObject jsonObject = super.buildReqJSONObject();
		try {
			jsonObject.put("TerminalNo", terminalNo);
			jsonObject.put("TerminalId", terminalId);
			jsonObject.put("TerminalIp", terminalIp);
			jsonObject.put("ProvinceId", provinceId);
			jsonObject.put("SessionId", sessionId);
			// jsonObject.put("TraceLevel", traceLevel);
			// jsonObject.put("CasId", casId);
			// jsonObject.put("CasSessionIndex", casSessionIndex);
			// jsonObject.put("CasConnectionId", casConnectionId);
			// jsonObject.put("AppTypeId", appTypeId);
		} catch (JSONException e) {
			throw new MessageFormateException("EitMessageHeader构建JSON消息时异常", e);
		}
		return jsonObject;// JSONObject留做后面继续添加
	}
	
	public String getTerminalNo() {
		return terminalNo;
	}
	
	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}
	
	public Integer getTerminalId() {
		return terminalId;
	}
	
	public void setTerminalId(Integer terminalId) {
		this.terminalId = terminalId;
	}
	
	public String getTerminalIp() {
		return terminalIp;
	}
	
	public void setTerminalIp(String terminalIp) {
		this.terminalIp = terminalIp;
	}
	
	public Integer getProvinceId() {
		return provinceId;
	}
	
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	
	public Integer getTraceLevel() {
		return traceLevel;
	}
	
	public String getServerId() {
		return serverId;
	}
	
	/**
	 * <p>For Server</p>
	 * @return void
	 */
	public void setTraceLevel(Integer traceLevel) {
		this.traceLevel = traceLevel;
	}

	/**
	 * <p>For server</p>
	 * @return void
	 */
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	/**
	 * <p>For server</p>
	 * @return void
	 */
	public void setAppTypeId(Integer appTypeId) {
		this.appTypeId = appTypeId;
	}

	public Integer getAppTypeId() {
		return appTypeId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
