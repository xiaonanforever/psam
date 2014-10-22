/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.mgnt.server.ReqWorker.java
 * 所含类: ReqWorker.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月19日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */
package com.topdot.psam.server.server;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.channel.RequestMessageQueue;
import com.topdot.framework.message.config.MessageDefination;
import com.topdot.framework.message.exception.ErrorDefination;
import com.topdot.framework.message.exception.ErrorMessage;
import com.topdot.framework.message.mont.MessageNoTimeLogger;
import com.topdot.framework.message.protocol.MessageBody;
import com.topdot.framework.message.protocol.MessageHeader;
import com.topdot.framework.message.protocol.RequestMessage;
import com.topdot.framework.message.protocol.ResponseMessage;

/**
 * <p> ReqWorker </p>
 * <p> 请求处理，从请求受理放入的队列中获取待请求消息 </p>
 * <p> Copyright: 版权所有 (c) 2010 - 2014 </p>
 * <p> Company: xxx </p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */
public class ReqWorker implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReqWorker.class);

	/** 死信消息记录器 */
	private static final Logger msgDead = LoggerFactory.getLogger("msgDead");

	private final RequestMessageQueue reqMsgQueue;

	private final Map<Integer, MessageDefination> messageDefByIdMap;

	private volatile Boolean isRunning = true;

	public ReqWorker(RequestMessageQueue reqMsgQueue, Map<Integer, MessageDefination> messageDefByIdMap) {
		// super(name);
		this.reqMsgQueue = reqMsgQueue;
		this.messageDefByIdMap = messageDefByIdMap;
	}

	@Override
	public void run() {
		LOGGER.info("消息工作线程启动...");
		boolean rspLive = true;
		while (isRunning) {
			// while (!Thread.currentThread().isInterrupted()) {
			int seqId = 0;
			try {
				RequestMessage<? extends MessageHeader, ? extends MessageBody> reqMsg = reqMsgQueue.take(true);
				if (reqMsg == null) {
					continue;
				}
				MessageHeader messageHeader = reqMsg.getHeader();
				seqId = messageHeader.getSequenceId();
				long start = System.currentTimeMillis();

				if (MessageNoTimeLogger.isTimeOut(reqMsg)) {
					LOGGER.warn("请求{}超过存活时间,创建于{}", seqId, messageHeader.getCreateDate());
					rspLive = false;
				}
				// 通过消息指令ID获得消息定义
				MessageDefination msgDefination = messageDefByIdMap.get(messageHeader.getCommandId());
				if (msgDefination != null) {
					ResponseMessage rspMsg = null;// = tmpRspMsg;
					if (msgDefination.getExecutor() != null) {
						LOGGER.info("{}:处理请求", seqId);
						RequestMessage<?,?> sreqMsg = msgDefination.getReqMsg().parse(reqMsg.getJsonStr());
						rspMsg = msgDefination.getExecutor().execute(sreqMsg);
						LOGGER.info("{}:处理完成.", seqId);
					}
					if (!rspLive) {// 响应超过有效时间
						ErrorMessage errorMessage = new ErrorMessage(ErrorDefination.ERROR_RSP_NOTIME);
						errorMessage.setDetail("响应超过有效时间");
						rspMsg.setErrorMessage(errorMessage);
					}
					// 设置以下CommandName
					rspMsg.getHeader().setCommandName(msgDefination.getMessageType().getCommandName());
					
					long end = System.currentTimeMillis();
					LOGGER.info("{}:设置响应之前花费时间:{}ms", seqId, end - start);
					reqMsg.setResponseMsg(rspMsg);
					LOGGER.info("{}:设置响应花费时间:{}ms", seqId, System.currentTimeMillis() - end);

				} else {
					msgDead.warn("{}-{}", messageHeader.getCommandId(), reqMsg.toString());
				}
				LOGGER.debug("消息{}处理完毕.", seqId);
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
