package com.topdot.framework.message.channel;

import com.topdot.framework.message.exception.ChannelException;
import com.topdot.framework.message.exception.RequestException;
import com.topdot.framework.message.protocol.MessageBody;
import com.topdot.framework.message.protocol.MessageHeader;
import com.topdot.framework.message.protocol.RequestMessage;
import com.topdot.framework.message.protocol.ResponseMessage;

/**
 * <p> 请求通道接口 </p>
 * <p> 接口:<br>
 * 1、同步发送RequestMessage<br>
 * 2、异步发送RequestMessage<br>
 * 3、发送RequestMessage 的过滤消息，为同步、异步发送提供底层实现</p>
 * <p> Copyright: 版权所有 (c) 2010 - 2014 </p>
 * <p> Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */
public interface RequestChannel extends Channel {

	/**
	 * <p>调用通道方法具体发送消息字符串</p>
	 * @param reqStr
	 * @throws ChannelException
	 * @return void
	 */
	void sendMsg(String msg) throws ChannelException;

	/**
	 * <p>异步调用服务</p>
	 * @param requestMsg
	 * @return
	 * @return boolean
	 * @see call 同步调用
	 */
	void acall(RequestMessage<? extends MessageHeader, ? extends MessageBody> requestMsg) throws RequestException;

	/**
	 * <p>同步调用服务，并等待返回响应</p>
	 * @param requestMsg
	 * @return-返回响应
	 * @throws MessageException
	 * @throws MessageErrorException
	 * @return ResponseMessage
	 * @see acall 异步调用
	 */
	ResponseMessage call(RequestMessage<? extends MessageHeader, ? extends MessageBody> requestMsg)
			throws RequestException;
}
