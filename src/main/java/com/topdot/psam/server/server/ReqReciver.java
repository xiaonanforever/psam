/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.mgnt.server.ReqReciver.java
 * 所含类: ReqReciver.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月19日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.server.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.channel.RequestMessageQueue;
import com.topdot.framework.message.config.MessageDefination;
import com.topdot.framework.message.constants.MsgConstants;
import com.topdot.framework.message.exception.ErrorMessage;
import com.topdot.framework.message.exception.MessageFilterException;
import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.exception.TimeoutException;
import com.topdot.framework.message.filter.FilterChain;
import com.topdot.framework.message.filter.FilterMessage;
import com.topdot.framework.message.protocol.MessageBody;
import com.topdot.framework.message.protocol.MessageHeader;
import com.topdot.framework.message.protocol.RequestMessage;
import com.topdot.framework.message.protocol.ResponseMessage;

/**
 * <p>ReqReciver</p>
 * <p>请求受理</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class ReqReciver implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReqReciver.class);

	/** 请求受理Filter */
	private final FilterChain reqChain;

	/** 响应受理Filter */
	private final FilterChain rspChain;

	private final RequestMessageQueue reqMsgQueue;
	
	private final Map<Integer, MessageDefination> messageDefByIdMap;

	public ReqReciver() {
		this.reqChain = null;
		this.rspChain = null;
		this.reqMsgQueue = null;
		this.messageDefByIdMap = null;
	}

	public ReqReciver(FilterChain reqChain, FilterChain rspChain, RequestMessageQueue reqMsgQueue,
			Map<Integer, MessageDefination> messageDefByIdMap) {
		this.reqChain = reqChain;
		this.rspChain = rspChain;
		this.reqMsgQueue = reqMsgQueue;
		this.messageDefByIdMap = messageDefByIdMap;
	}

	@Override
	public void run() {
		while (true) {
			LOGGER.info("等待受理客户端请求...");
			SelectionKey key;
			SocketChannel clntChan;
			try {
				key = ActiveClientChannel.take();
				clntChan = (SocketChannel) key.channel();
			} catch (InterruptedException e) {
				LOGGER.info("受理通道结束.");
				return;
			}

			ResponseMessage rspMsg = null;
			LOGGER.info("请求信息预处理...");
			FilterMessage msg = new FilterMessage(null, null);

			// 转换成Req,放入队列,等待返回
			try {
				if (reqChain != null) {
					reqChain.getConfig().addAttr(MsgConstants.SOCKET_CHANNL, clntChan);
					reqChain.doFilter(msg);

					PsamServer.activate(key);

					RequestMessage<?, ?> req = null;

					req = new RequestMessage<MessageHeader,MessageBody>(msg.getFilterMsg());// MessageFormateException
					MessageDefination md = messageDefByIdMap.get(req.getHeader().getCommandId());
					req.getHeader().setTimeout(md.getMessageType().getTimeout());
					
					reqMsgQueue.put(req);//
					rspMsg = req.getResponseMsg();// 请求会超时
				} else {
					rspMsg = new ResponseMessage();
					rspMsg.setErrorMessage(ErrorMessage.ERROR_SERVER);
				}
			} catch (MessageFilterException e) {
				LOGGER.error("请求消息预处理异常!", e);
				rspMsg = new ResponseMessage();
				rspMsg.setErrorMessage(e.getErrorMessage());
			} catch (MessageFormateException e) {
				LOGGER.error("请求小时格式不正确!", e);
				rspMsg = new ResponseMessage();
				rspMsg.setErrorMessage(ErrorMessage.ERROR_SERVER);
			} catch (TimeoutException e) {
				LOGGER.error("请求超时!", e);
				rspMsg = new ResponseMessage();
				rspMsg.setErrorMessage(ErrorMessage.ERROR_SERVER);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String rspStr = rspMsg.getJsonStr();
			LOGGER.info("RSP Str:<{}>",rspStr);
			FilterMessage rspFilterMsg = new FilterMessage(rspStr, null);
			if (rspChain != null) {
				LOGGER.info("消息后处理...");
				try {
					rspChain.doFilter(rspFilterMsg);
				} catch (MessageFilterException e) {
					LOGGER.error("响应消息后处理异常!", e);
				}
			}

			try {
				ByteBuffer wbf = ByteBuffer.wrap(rspFilterMsg.getFilterMsg().getBytes("UTF-8"));
				while (wbf.hasRemaining() && clntChan.write(wbf) != -1);
				LOGGER.info("发送客户端完成!");
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("严重错误,不支持UTF-8编码", e);
				return;
			} catch (IOException e) {
				LOGGER.error("SocketChannel输出异常!", e);
			}
		}
	}// end of run
}
