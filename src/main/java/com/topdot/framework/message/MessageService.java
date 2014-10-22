package com.topdot.framework.message;

import com.topdot.framework.message.config.IntegrateConfig;
import com.topdot.framework.message.protocol.MessageBody;
import com.topdot.framework.message.protocol.MessageHeader;
import com.topdot.framework.message.protocol.RequestMessage;
import com.topdot.framework.message.protocol.ResponseMessage;

/**
 * <p> MessageService</p>
 * <p> 消息服务接口 </p>
 * 
 * <p> Copyright: 版权所有 (c) 2010 - 2014 </p>
 * <p> Company: xxx </p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */
public interface MessageService {
	
	/** 连接状态 */
	static enum State {
		//TODO 关系之间还需进一步梳理
		BROKEN("断开"),UNCONNECT("未连接"),CONNECT("连接"), INIT("初始化"), LOGON("登陆");
		// 状态描述
		final String desc;
		
		State(final String desc) {
			this.desc = desc;
		}
	}
	
	/**
	 * <p>连接服务,相关参数配置默认参数</p>
	 * 
	 * @param reqMsg
	 * @return
	 * @return ResponseMessage
	 */
	ResponseMessage logon(RequestMessage<? extends MessageHeader, ? extends MessageBody> reqMsg);
	
	/**
	 * <p>重新连接服务</p>
	 * 
	 * @throws ConnectionException
	 * @see connect
	 */
	ResponseMessage reLogon();
	
	/**
	 * <p>登出</p>
	 * 
	 * @param reqMsg
	 * @return
	 * @throws ConnectionException
	 * @return ResponseMessage
	 */
	ResponseMessage logout(RequestMessage<? extends MessageHeader, ? extends MessageBody> reqMsg);
	
	/**
	 * <p>同步调用服务，并等待返回响应</p>
	 * 
	 * @param requestMsg
	 * @return-返回响应
	 * @throws MessageException
	 * @throws MessageErrorException
	 * @return ResponseMessage
	 * @see acall 异步调用
	 */
	ResponseMessage call(RequestMessage<? extends MessageHeader, ? extends MessageBody> requestMsg);
	
	/**
	 * <p>清理资源</p>
	 * 
	 * @return
	 * @return ResponseMessage
	 */
	ResponseMessage cleanup();
	
	/**
	 * <p>获取服务当前状态状态</p>
	 * 
	 * @return
	 * @return State
	 * @see State 枚举说明
	 */
	State getState();
	
	/**
	 * <p>获取API版本</p>
	 * 
	 * @return-API版本
	 * @return String
	 */
	String getVersion();
	
	/**
	 * <p>获取配置信息</p>
	 * 
	 * @return
	 * @return IntegrateConfig
	 */
	IntegrateConfig getConfig();
	
	void registerLogonHook(LogonHook hook);
}
