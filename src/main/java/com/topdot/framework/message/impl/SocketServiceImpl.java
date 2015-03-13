package com.topdot.framework.message.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.MessageServiceTemplate;
import com.topdot.framework.message.channel.impl.IORequestChannelImpl;
import com.topdot.framework.message.config.ServiceConfig;
import com.topdot.framework.message.constants.MsgConstants;
import com.topdot.framework.message.exception.ConnectionException;
import com.topdot.framework.message.mont.ChannelDispatchService;

public class SocketServiceImpl extends MessageServiceTemplate {
	private static final Logger LOGGER = LoggerFactory.getLogger(SocketServiceImpl.class);
	private SocketChannel clntChan;

	@Override
	public void release() throws ConnectionException {
		if (clntChan == null) {
			return;
		}
		try {
			clntChan.close();
			state = State.UNCONNECT;
		} catch (IOException e) {
			throw new ConnectionException("断开连接时异常!", e);
		}
	}

	@Override
	public void connect() throws ConnectionException {
		Socket socket = null;
		ServiceConfig sc = config.getServiceConfig();
		try {
			clntChan = SocketChannel.open();
			clntChan.configureBlocking(false);
			if (!clntChan.connect(new InetSocketAddress(sc.getServerIp(), sc.getServerPort()))) {
				while (!clntChan.finishConnect());
			}
			ChannelDispatchService.registe(this);
			socket = clntChan.socket();
			socket.setReceiveBufferSize(10 * 1024);
			socket.setTcpNoDelay(true);
			// socket.setPerformancePreferences(0, 2, 3);
			LOGGER.info("Socket Recv:{},After Connect...", socket.getReceiveBufferSize());
		} catch (IOException e) {
			throw new ConnectionException(
					"连接远程服务IP[" + sc.getServerIp() + "]和端口[" + sc.getServerPort() + "]异常", e);
		}
		try {
			socket.setKeepAlive(true);
			socket.setSoTimeout(sc.getServerReadTimeout());
		} catch (SocketException e) {
			throw new ConnectionException("设置SO_TIMEOUT参数[" + sc.getServerReadTimeout() + "]异常", e);
		}

		// 设置RequestChannel
		((IORequestChannelImpl) requestChannel).setClntChan(clntChan);

		InetSocketAddress localSA = (InetSocketAddress) (socket.getLocalSocketAddress());
		String localSAStr = localSA.getAddress().getHostAddress() + ":" + localSA.getPort();
		requestChannel.getChain().getConfig().addAttr(MsgConstants.LOCALAUTH, localSAStr);
	}

	@Override
	public String getVersion() {
		return "1.0";
	}
	
	public SocketChannel getClntChan() {
		return clntChan;
	}

	public Socket getSocket() {
		return clntChan.socket();
	}
}