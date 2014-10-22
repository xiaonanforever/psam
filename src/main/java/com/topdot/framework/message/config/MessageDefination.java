/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.cslc.framework.messaging.model.MessageDefination.java
 * 所含类: MessageDefination.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2012-4-9 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.config;

import com.topdot.framework.message.executor.MessageExecutor;
import com.topdot.framework.message.protocol.MessageBody;
import com.topdot.framework.message.protocol.MessageHeader;
import com.topdot.framework.message.protocol.RequestMessage;
import com.topdot.framework.message.protocol.ResponseMessage;
import com.topdot.framework.message.reciver.MessageReciver;

/**
 * <p> MessageDefination</p>
 * 
 * <p> 注册的消息类型，包括请求标示和响应处理类（如果是异步响应消息） </p>
 * 
 * <p> Copyright: 版权所有 (c) 2010 - 2014 </p>
 * <p> Company: xxx </p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class MessageDefination {
	/** 注册消息类型定义 */
	private MessageHeader messageType;
	
	/** 注册消息类型的响应报文处理类 */
	private MessageReciver<ResponseMessage> messageReciver;
	
	/** 请求对应的响应 ,用户解析响应（使用parse方法）*/
	private RequestMessage<? extends MessageHeader, ? extends MessageBody> reqMsg;
	
	/** 请求对应的响应 ,用户解析响应（使用parse方法）*/
	private ResponseMessage rspMsg;
	
	private MessageExecutor<RequestMessage<?, ?>,ResponseMessage> executor;
	
	/**
	 * 是否主动报告,如果是,则RequestQueue中没有Request，并且需要有Reciver存在(否则采用DefaultReciver)
	 */
	private boolean report;
	
	public MessageDefination() {
	}
	
	public boolean isReport() {
		return report;
	}
	
	public void setReport(boolean report) {
		this.report = report;
	}
	
	public MessageHeader getMessageType() {
		return messageType;
	}
	
	public void setMessageType(MessageHeader messageType) {
		this.messageType = messageType;
	}
	
	public MessageReciver<ResponseMessage> getMessageReciver() {
		return messageReciver;
	}
	
	public void setMessageReciver(MessageReciver<ResponseMessage> messageReciver) {
		this.messageReciver = messageReciver;
	}
	
	public ResponseMessage getRspMsg() {
		return rspMsg;
	}
	
	public void setRspMsg(ResponseMessage rspMsg) {
		this.rspMsg = rspMsg;
	}

	public MessageExecutor<RequestMessage<?, ?>,ResponseMessage> getExecutor() {
		return executor;
	}

	public void setExecutor(MessageExecutor<RequestMessage<?, ?>,ResponseMessage> executor) {
		this.executor = executor;
	}

	public RequestMessage<? extends MessageHeader, ? extends MessageBody> getReqMsg() {
		return reqMsg;
	}

	public void setReqMsg(RequestMessage<? extends MessageHeader, ? extends MessageBody> reqMsg) {
		this.reqMsg = reqMsg;
	}
		
}
