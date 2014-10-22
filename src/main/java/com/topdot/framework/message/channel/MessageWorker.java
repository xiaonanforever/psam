/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.cslc.framework.messaging.service.impl.MessageWorkerThread.java
 * 所含类: MessageWorkerThread.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月19日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */
package com.topdot.framework.message.channel;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.config.MessageDefination;
import com.topdot.framework.message.exception.ErrorDefination;
import com.topdot.framework.message.exception.ErrorMessage;
import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.mont.MessageNoTimeLogger;
import com.topdot.framework.message.protocol.MessageBody;
import com.topdot.framework.message.protocol.MessageHeader;
import com.topdot.framework.message.protocol.RequestMessage;
import com.topdot.framework.message.protocol.ResponseMessage;
import com.topdot.framework.message.reciver.MessageReciver;

/**
 * <p> MessageWorkerThread </p>
 * <p> 消息处理分发线程 </p>
 * <p> Copyright: 版权所有 (c) 2010 - 2014 </p>
 * <p> Company: xxx </p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */
public class MessageWorker extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageWorker.class);

	/** 死信消息记录器 */
	private static final Logger msgDead = LoggerFactory.getLogger("msgDead");

	private final RequestMessageQueue requestMsgQueue;

	private final ResponseMessageQueue responseMsgQueue;

	private final Map<Integer, MessageDefination> messageDefByIdMap;

	private final MessageReciver<ResponseMessage> defaultMessageReciver;

	private volatile Boolean isRunning = true;

	public MessageWorker(String name, MessageWorkerPoolImpl workerPool) {
		super(name);
		this.requestMsgQueue = workerPool.getRequestMessageQueue();
		this.responseMsgQueue = workerPool.getResponseMessageQueue();
		this.messageDefByIdMap = workerPool.config.getMessageConfig().getMessageDefByIdMap();
		this.defaultMessageReciver = workerPool.config.getMessageConfig().getDefaultMessageReciver();
	}

	@Override
	public void run() {
		LOGGER.info("消息工作线程启动...");
		boolean rspLive = true;
		while (isRunning) {
		//while (!Thread.currentThread().isInterrupted()) {
			int seqId = 0;
			try {
				ResponseMessage tmpRspMsg = responseMsgQueue.take();
				if (tmpRspMsg == null) {
					continue;
				}
				MessageHeader messageHeader = tmpRspMsg.getHeader();
				seqId = messageHeader.getSequenceId();
				long start = System.currentTimeMillis();
				LOGGER.info("{}:处理消息", seqId);
				if (MessageNoTimeLogger.isTimeOut(tmpRspMsg)) {
					LOGGER.warn("响应{}超过存活时间,创建于{}", seqId, messageHeader.getCreateDate());
					rspLive = false;
				}
				// 通过消息指令ID获得消息定义
				MessageDefination msgDefination = messageDefByIdMap.get(messageHeader.getCommandId());
				if (msgDefination != null) {
					ResponseMessage rspMsg = tmpRspMsg;
					if (messageHeader.success() && msgDefination.getRspMsg() != null) {// 只有返回业务正确才需要进步解析
						rspMsg = msgDefination.getRspMsg().parse(tmpRspMsg.getJsonStr());
						LOGGER.info("{}:解析完成时间.", seqId);
					}
					if (!rspLive) {// 响应超过有效时间
						ErrorMessage errorMessage = new ErrorMessage(ErrorDefination.ERROR_RSP_NOTIME);
						errorMessage.setDetail("响应超过有效时间");
						rspMsg.setErrorMessage(errorMessage);
					}
					// 设置以下CommandName
					rspMsg.getHeader().setCommandName(msgDefination.getMessageType().getCommandName());
					MessageReciver<ResponseMessage> msgReciver = msgDefination.getMessageReciver();
					if (msgReciver == null) {
						msgReciver = defaultMessageReciver;
					}
					if (msgDefination.isReport()) { // 主动报告
						msgReciver.recive(rspMsg);
						continue;
					}
					RequestMessage<? extends MessageHeader, ? extends MessageBody> reqMsg = requestMsgQueue.take(
							seqId, true);
					if (reqMsg == null || reqMsg.getHeader().isAsyn()) {// 异步
						// MessageReciver<ResponseMessage> msgReciver = msgDefination.getMessageReciver();
						msgReciver.recive(rspMsg);
					} else {
						if (MessageNoTimeLogger.isTimeOut(reqMsg)) {
							LOGGER.warn("请求{}已经超过存活时间", seqId);
							ErrorMessage errorMessage = new ErrorMessage(ErrorDefination.ERROR_REQ_NOTIME);
							errorMessage.setDetail("请求超过有效时间");
							rspMsg.setErrorMessage(errorMessage);
						}
						long end = System.currentTimeMillis();
						LOGGER.info("{}:设置响应之前花费时间:{}ms", seqId, end - start);
						reqMsg.setResponseMsg(rspMsg);
						LOGGER.info("{}:设置响应花费时间:{}ms", seqId, System.currentTimeMillis() - end);
					}
				} else {
					// LOGGER.warn("消息指令{}未定义.", messageHeader.getCommandId());
					msgDead.warn("{}-{}", messageHeader.getCommandId(), tmpRspMsg.getJsonStr());
				}
				LOGGER.debug("消息{}处理完毕.", seqId);
			} catch (MessageFormateException e) {
				LOGGER.error("消息" + seqId + "JSON异常", e);
			} catch (InterruptedException e) {
				LOGGER.info("工作线程停止.");
				return;
			} catch (Exception e) {
				LOGGER.error("消息" + seqId + "其他异常", e);
			}
		}
	}

	public void setIsRunning(Boolean isRunning) {
		this.isRunning = isRunning;
	}
}
