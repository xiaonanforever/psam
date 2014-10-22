/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.request.SystemEventMessageBody.java
 * 所含类: SystemEventMessageBody.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.message.protocol.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.psam.message.protocol.PsamReqBody;

/**
 * <p>系统事件上报包体</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class SystemEventRequestBody extends PsamReqBody {
	
	/** 事件流水 */
	private String eventSn;
	
	/** 事件级别 */
	private int eventLevel;
	
	/**事件类别编码*/
	private int eventCategory;
	
	/** 事件类型 */
	private int eventType;
	
	/** 事件时间 */
	private String eventTime;
	
	/** 触发模块 */
	private int eventModule;
	
	/** 事件描述 */
	private String eventDescription;
	
	public SystemEventRequestBody() {
	}
	
	public SystemEventRequestBody(String jsonStr) throws MessageFormateException {
		super(jsonStr);
		try {
			this.eventSn = jsonMsg.getString("EventSn");
			this.eventLevel = jsonMsg.getInt("EventLevel");
			this.eventCategory = jsonMsg.getInt("EventCategory");
			this.eventType = jsonMsg.getInt("EventType");
			this.eventTime = jsonMsg.getString("EventTime");
			this.eventModule = jsonMsg.getInt("EventModule");
			this.eventDescription = jsonMsg.getString("EventDescription");
		} catch (JSONException e) {
			throw new MessageFormateException("通过JSON对象获取ReaderEnableRequestBody异常", e);
		}
	}

	@Override
	public JSONObject buildReqJSONObject() throws MessageFormateException {
		JSONObject jsonObject = super.buildReqJSONObject();
		try {
			jsonObject.put("EventSn", eventSn);
			jsonObject.put("EventLevel", eventLevel);
			jsonObject.put("EventType", eventType);
			jsonObject.put("EventCategory", eventCategory);
			jsonObject.put("EventTime", eventTime);
			jsonObject.put("EventModule", eventModule);
			jsonObject.put("EventDescription", eventDescription);
		} catch (JSONException e) {
			throw new MessageFormateException("SystemEventMessageBody构建JSON消息时异常", e);
		}
		return jsonObject;// JSONObject留做后面继续添加
	}
	
	public String getEventSn() {
		return eventSn;
	}
	
	public void setEventSn(String eventSn) {
		this.eventSn = eventSn;
	}
	
	public int getEventLevel() {
		return eventLevel;
	}
	
	public void setEventLevel(int eventLevel) {
		this.eventLevel = eventLevel;
	}
	
	public int getEventType() {
		return eventType;
	}
	
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	
	public int getEventCategory() {
		return eventCategory;
	}

	public void setEventCategory(int eventCategory) {
		this.eventCategory = eventCategory;
	}

	public String getEventTime() {
		return eventTime;
	}
	
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	
	public int getEventModule() {
		return eventModule;
	}
	
	public void setEventModule(int eventModule) {
		this.eventModule = eventModule;
	}
	
	public String getEventDescription() {
		return eventDescription;
	}
	
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	
}
