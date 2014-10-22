/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.mont.ServiceEvent.java
 * 所含类: ServiceEvent.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2013-5-13 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.mont;

/**
 * <p>服务事件</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName ServiceEvent
 * @author zhangyongxin
 * @version 1.0
 */
public class ServiceEvent {
	public enum EventType {
		SOCKET_CLOSED("Socket断开"), //
		SOCKET_CLOSED_ACK("Socket断开响应-处理成功"), //
		SOCKET_CLOSED_ACK_FAIL("Socket断开响应-处理失败"), //
		SOCKET_IN_CLOSED("Socket输入流断开"), //
		SOCKET_IN_CLOSED_ACK("Socket输入流断开响应-处理成功"), //
		SOCKET_IN_CLOSED_ACK_FAIL("Socket输入流断开响应-处理失败"),		//
		SOCKET_OUT_CLOSED("Socket输出流断开"), //
		SOCKET_OUT_CLOSED_ACK("Socket输出流断开响应-处理成功"),		//
		SOCKET_OUT_CLOSED_ACK_FAIL("Socket输出流断开响应-处理失败");

		/** 描述 */
		final String desc;

		EventType(final String desc) {
			this.desc = desc;
		}
		
	}
	/** 虚拟终端 */
	private String terminalNo;

	/** 事件类型 */
	private EventType eventType;

	public ServiceEvent(String terminalNo, EventType eventType) {
		this.terminalNo = terminalNo;
		this.eventType = eventType;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public EventType getEventType() {
		return eventType;
	}
}
