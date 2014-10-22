/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.cslc.framework.messaging.model.MessageHeader.java
 * 所含类: MessageHeader.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月19日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */
package com.topdot.framework.message.protocol;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.topdot.framework.message.exception.ErrorDefination;
import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.util.CslcJSON;
import com.topdot.framework.util.DateUtils;

/**
 * <p> MessageHeader </p>
 * 
 * <p> 消息头</p>
 * 
 * <p> Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p> Company: xxx </p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */
public class MessageHeader {
	
	/** 消息默认ID -99 标示没有设置 */
	public static final int DEFAULT_SEQUENCE_ID = -99;
	
	/** 用于记录响应JSON格式消息, MessageHeader获取本部消息后，交由子类获取其他消息 */
	protected JSONObject jsonMsg;
	/** 消息ID */
	private int sequenceId = DEFAULT_SEQUENCE_ID;
	
	/** 命令类型 */
	private Integer commandType;
	
	/** 命令Id */
	private Integer commandId;
	
	/** 命令名称-一般以RequetClass全路径命名 */
	private String commandName;
	
	/** 当前系统Id，AM分配的为0 */
	private Integer systemId;
	
	/** 创建日期 */
	private Date createDate;
	
	/** 响应错误代码,如果不为0表示有错误发生,相关错误消息 */
	private Integer errorCode;
	
	/** 超时设置 */
	private int timeout;
	
	/** 是否异步请求，默认是同步 */
	private boolean asyn;
	
	/** 会话Id*/
	private String sessionId;
	
	public MessageHeader() {
		this.createDate = new Date();
	}
	
	/**
	 * <p>接收Response<br/>
	 * 利用Json格式字符串初始化对象，并生成对应的JSON对象
	 * </p>
	 * @param jsonStr
	 * @throws MessageFormateException
	 */
	public MessageHeader(String jsonStr) throws MessageFormateException {
		try {
			jsonMsg = new JSONObject(jsonStr);
			sequenceId = jsonMsg.getInt("SequenceId");
			commandType = jsonMsg.getInt("CommandType");
			commandId = jsonMsg.getInt("CommandId");
			errorCode = jsonMsg.getInt("ErrorCode");
			createDate = jsonMsg.has("SendTime") ? DateUtils.parseDateTime(jsonMsg.getString("SendTime")) : null;
		} catch (JSONException e) {
			throw new MessageFormateException("利用JSON字符串["+jsonStr+"]构建MessageHeader是出现异常", e);
		} catch (ParseException e) {
			throw new MessageFormateException("消息创建时间不符合[yyyy-mm-dd 24H:MI:SS]格式", e);
		}
	}
	
	public JSONObject getJsonMsg() {
		return jsonMsg;
	}
	
	/**
	 * <p>返回MessageHeader对象对应的JSONObject,主要用于发送请求时转换成JSON字符串 </p>
	 * 
	 * @return
	 * @throws MessageFormateException
	 */
	public JSONObject buildReqJSONObject() throws MessageFormateException {
		JSONObject jsonObject = new CslcJSON();
		try {
			jsonObject.put("SequenceId", sequenceId);
			jsonObject.put("CommandType", commandType);
			jsonObject.put("CommandId", commandId);
			jsonObject.put("SystemId", systemId);
			jsonObject.put("SendTime", DateUtils.getDateTimeString(createDate));
			jsonObject.put("ErrorCode", errorCode);
			
		} catch (JSONException e) {
			throw new MessageFormateException("MessageHeader构建JSON消息时异常", e);
		}
		return jsonObject;// JSONObject留做后面继续添加
	}
	
	public void setSystemId(Integer systemId) {
		this.systemId = systemId;
	}
	
	public Integer getSequenceId() {
		return sequenceId;
	}
	
	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}
	
	public Integer getCommandType() {
		return commandType;
	}
	
	public void setCommandType(Integer commandType) {
		this.commandType = commandType;
	}
	
	public Integer getCommandId() {
		return commandId;
	}
	
	public void setCommandId(Integer commandId) {
		this.commandId = commandId;
	}
	
	public String getCommandName() {
		return commandName;
	}
	
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
	
	public Integer getErrorCode() {
		return errorCode;
	}
	
	/**
	 * <p>For Server</p>
	 * @return void
	 */
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public Integer getSystemId() {
		return systemId;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setJsonMsg(JSONObject jsonMsg) {
		this.jsonMsg = jsonMsg;
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public boolean isAsyn() {
		return asyn;
	}
	
	public void setAsyn(boolean asyn) {
		this.asyn = asyn;
	}
	
	/**
	 * <p>判断报文是否正确返回</p>
	 * 
	 * @return
	 * @return boolean
	 */
	public boolean success() {
		return this.errorCode == ErrorDefination.NO_ERROR.getCode();
	}
	
	
	public String getSessionId(){
		return this.sessionId;
	}
}
