/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.gimp.eit.message.SocketSendChannel.java
 * 所含类: SocketSendChannel.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月19日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.channel.impl;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.MessageService.State;
import com.topdot.framework.message.channel.AbstractRequestChannel;
import com.topdot.framework.message.exception.ChannelException;
import com.topdot.framework.message.exception.ErrorDefination;
import com.topdot.framework.message.mont.ServiceEvent;
import com.topdot.framework.message.mont.ServiceEventQueue;

/**
 * <p>IO请求通道</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class IORequestChannelImpl extends AbstractRequestChannel {

	private static final Logger LOGGER = LoggerFactory.getLogger(IORequestChannelImpl.class);
	/** 服务事件队列 */
	protected ServiceEventQueue serviceEventQueue;
	private SocketChannel clntChan;

	public void setClntChan(SocketChannel clntChan) {
		this.clntChan = clntChan;
	}

	public IORequestChannelImpl() {
	}

	@Override
	public void sendMsg(String msg) throws ChannelException {
		try {
			ByteBuffer bf = ByteBuffer.wrap(msg.getBytes("UTF-8"));
			while (bf.hasRemaining() && clntChan.write(bf) != -1);
			LOGGER.debug("发送完毕.");
		} catch (Exception e) {
			try {
				Socket socket = clntChan.socket();
				if (socket.isOutputShutdown() || socket.isClosed()) {
					if (State.LOGON.equals(messageService.getState())) {
						LOGGER.error("终端<{}>输出断开,发送保持事件...",messageService.getTerminalNo());
						serviceEventQueue.put(new ServiceEvent(messageService.getTerminalNo(),
								ServiceEvent.EventType.SOCKET_OUT_CLOSED));
						ChannelException ex = new ChannelException("发现Socket输出通道断开,请重新发送!", e);
						ex.setErrorDefination(ErrorDefination.ERROR_REQ_CHANNEL);
						throw ex;
					} else {
						throw new ChannelException("服务没有登陆,不能执行请求!", e);
					}
				} else {
					throw new ChannelException("通过Scocket输出通道发送消息异常!", e);
				}
			} catch (InterruptedException e1) {
				throw new ChannelException("发现Socket输出通道断开,尝试发起连接失败!!", e);
			}
		}
	}

	public void setServiceEventQueue(ServiceEventQueue serviceEventQueue) {
		this.serviceEventQueue = serviceEventQueue;
	}
}
