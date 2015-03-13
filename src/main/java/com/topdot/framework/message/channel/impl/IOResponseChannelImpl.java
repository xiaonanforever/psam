/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.reader.protocol.SocketResponseChannelImpl.java
 * 所含类: SocketResponseChannelImpl.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月19日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.channel.impl;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.MessageService;
import com.topdot.framework.message.channel.AbstractResponseChannel;
import com.topdot.framework.message.constants.HTTPConstants;
import com.topdot.framework.message.constants.MsgConstants;
import com.topdot.framework.message.exception.HttpParserException;
import com.topdot.framework.message.exception.MessageFilterException;
import com.topdot.framework.message.filter.FilterMessage;
import com.topdot.framework.message.mont.ActiveSocketChannel;
import com.topdot.framework.message.mont.ChannelDispatchService;
import com.topdot.framework.message.mont.MessageServiceChannel;
import com.topdot.framework.message.mont.ServiceEvent;
import com.topdot.framework.message.mont.ServiceEventQueue;
import com.topdot.framework.util.Compress;

/**
 * <p>IO响应通道</p>
 * <p>含线程主动对象</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class IOResponseChannelImpl extends AbstractResponseChannel implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(IOResponseChannelImpl.class);

	int flag = 0;// 检测网络流进入和处理完毕标志
	private long start;
	private long random;
	private final ByteBuffer bodyBuf = ByteBuffer.allocate(10 * 1024);
	private final ByteBuffer headBuf = ByteBuffer.allocate(1);

	SocketChannel clntChan;

	public IOResponseChannelImpl() {
	}

	@Override
	public void run() {
		while (true) {
			try {
				MessageServiceChannel msc = ActiveSocketChannel.take();
				LOGGER.info("消息驱动...");
				mst = msc.getMst();
				clntChan = msc.getSocketChannel();
				String headContent;
				try {
					headContent = readHeader();
				} catch (HttpParserException e) {
					throw new MessageFilterException("解析Json Head,不符合HTTP协议格式!", e);
				}
				if (headContent == null || "".equals(headContent.trim())) {
					throw new MessageFilterException("Json Head信息为空!");
				}
				int cl = 0;
				String value = getAttribute(headContent, HTTPConstants.CONTENT_LENGTH_ATTR);
				if (value != null) {
					cl = Integer.parseInt(value.trim());
				}
				String bodyContent = "{}";
				if (cl > 0) {
					try {
						bodyContent = readLenData(cl);// reader.readLine();
					} catch (HttpParserException e) {
						throw new MessageFilterException("读取HTTP包体数据异常!", e);
					}
				}
				LOGGER.debug("[{}]网络流处理完毕<<<<<<<<<<<<<<<<<<<花费时间:{}ms", random, System.currentTimeMillis() - start);
				ChannelDispatchService.activate(msc.getSelectKey());
				FilterMessage msg = new FilterMessage(bodyContent, null);
				String compress = getAttribute(headContent, HTTPConstants.COMPRESS_ATTR);
				if (compress == null || Compress.SOURCE.equals(compress)) {
					msg.addAttr(MsgConstants.IS_COMPRESS, MsgConstants.NO);
				} else {
					msg.addAttr(MsgConstants.IS_COMPRESS, MsgConstants.YES);
				}
				onMessage(msg);
			} catch (Exception e) {
				if (e instanceof InterruptedException) {
					LOGGER.info("响应通道结束.");
					return;
				}
				Socket socket = clntChan.socket();
				if (socket.isInputShutdown() || socket.isClosed()) {
					LOGGER.info("Socket InputStream({})或Socket关闭({})", socket.isInputShutdown(), socket.isClosed());
					// 处于登陆状态才需要保持连接
					if (MessageService.State.LOGON.equals(mst.getState())) {
						LOGGER.error("Socket输入通道异常关闭!", e);
						ServiceEventQueue serviceEventQueue = ServiceEventQueue.getInstance();
						try {
							serviceEventQueue.put(new ServiceEvent(mst.getTerminalNo(),
									ServiceEvent.EventType.SOCKET_IN_CLOSED));
							LOGGER.error("终端<{}>输入断开,发送保持事件...", mst.getTerminalNo());
						} catch (InterruptedException e1) {
							LOGGER.error("放置Socket关闭事件异常!", e1);
						}
					}
				} else {
					LOGGER.error("读取I/O数据异常.", e);
				}// end of if
			}// end of catch
		}
	}// end of run

	/**
	 * <p>读取头部 </p>
	 * 
	 * @return 头部消息
	 * @return String
	 * @throws HttpParserException
	 */
	private String readHeader() throws HttpParserException {
		// LOGGER.debug("读取HTTP 头部信息,socket关闭状态:{},{}.....",socket.isClosed(),socket.isInputShutdown());
		random = Math.round(Math.random() * Integer.MAX_VALUE);
		StringBuilder header = new StringBuilder();
		try {
			flag = 0;
			// 已经连接的回车换行数 crlfNum=4为头部结束
			int crlfNum = 0;
			String head = "";

			int readNumber;
			while ((readNumber = clntChan.read(headBuf)) != -1) { // 读取头部
				if (readNumber == 0) {
					LOGGER.info("头部数据读取:<{}>",readNumber);
					continue;// 自旋
				}
				headBuf.flip();
				byte c = headBuf.get();
				headBuf.clear();
				header.append((char) c); // byte数组相加
				if (flag == 0) {
					flag = 1;
					LOGGER.info("[{}]:检查到数据进入.....{}", random, System.currentTimeMillis());
					start = System.currentTimeMillis();
				}
				if (c == HTTPConstants.CRLF_13 || c == HTTPConstants.CRLF_10) {
					if ((++crlfNum) == 4) {
						head = header.toString().toUpperCase();
						LOGGER.info("[{}]:头部数据机收完毕.{}", random, head);
						LOGGER.debug("检测到新数据进入,头部信息:{}", head);
						return head;
					}
				} else {
					crlfNum = 0;
				}
			}
			clntChan.close();
			throw new HttpParserException("Socket Server输入流已断开!");
		} catch (IOException e) {
			if (!clntChan.isOpen()) {
				LOGGER.info("输入通道或者Socket关闭,{},{}", clntChan.isConnected(), clntChan.isConnectionPending());
				return "";
			}
			LOGGER.warn("解析输入信息时发生异常...", e);
			try {
				clntChan.close();
			} catch (IOException e1) {
				LOGGER.info("关闭输入通道发生异常,输入通道关闭:{}或者Socket关闭:{},{}", clntChan.isConnected(),
						clntChan.isConnectionPending());
				return "";
			}
			throw new HttpParserException("读取头部信息异常!", e);
		}
	}

	/**
	 * <p>读取定长数据</p>
	 * 
	 * @param size
	 * @return
	 * @throws HttpParserException
	 * @return String
	 */
	private String readLenData(int size) throws HttpParserException { //
		ByteBuffer innerBuf;
		if (size > bodyBuf.capacity()) {
			innerBuf = ByteBuffer.allocate(size);
		} else {
			bodyBuf.clear();
			bodyBuf.limit(size);
			innerBuf = bodyBuf;
		}
		try {
			while (innerBuf.hasRemaining() && clntChan.read(innerBuf) != -1);
			return new String(innerBuf.array(), 0, size, "UTF-8");
		} catch (IOException e) {
			throw new HttpParserException("读取JSON数据长度!", e);
		}
	}

	/**
	 * <p>获取HTTP包头数据</p>
	 * 
	 * @param key
	 * @return
	 * @return String
	 */
	private String getAttribute(String headContent, String key) {
		int index = headContent.indexOf(key.toUpperCase());
		byte[] requst = headContent.getBytes();
		if (index != -1) { // 从index+1起至/r/n
			StringBuilder sb = new StringBuilder();
			int i = index + key.length() + 1;
			while (true) {
				if (requst[i] == (byte) 13 || requst[i] == (byte) 10) {
					break;
				}
				sb.append((char) requst[i]);
				i++;
			}
			return sb.toString().trim();
		}
		return null;
	}
}
