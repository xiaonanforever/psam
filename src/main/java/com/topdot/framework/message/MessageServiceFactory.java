package com.topdot.framework.message;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.config.IntegrateConfig;
import com.topdot.framework.message.config.MessageConfig;
import com.topdot.framework.message.config.ServiceConfig;
import com.topdot.framework.message.exception.ErrorMessage;
import com.topdot.framework.message.exception.ServiceNumNotSetException;
import com.topdot.framework.message.mont.MessageNoTimeLogger;
import com.topdot.framework.message.mont.ServiceEventQueue;
import com.topdot.framework.message.protocol.ResponseMessage;
import com.topdot.framework.util.StringUtils;

/**
 * <p>消息服务工厂类</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */
public class MessageServiceFactory {

	/** 需要启动的服务数目 */
	private static int msgServiceNum = 0;

	//private static CountDownLatch allLongon;

	public static int getMsgServiceNum() {
		return msgServiceNum;
	}

	/**
	 * <p>设置需要启动的服务数目，只有在非0的条件下才允许设置</p>
	 * 
	 * @return void
	 * @see
	 */
	public static void setMsgServiceNum(int msgServiceNum) {
		//if (MessageServiceFactory.msgServiceNum <= 0 && msgServiceNum > 0) {
			MessageServiceFactory.msgServiceNum = msgServiceNum;
			LOGGER.info("服务数目:<{}>",msgServiceNum);
			//allLongon = new CountDownLatch(msgServiceNum);
		//}
	}
	
	public static void serviceReady() {
//		allLongon.countDown();
//		try {
//			allLongon.await();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	/**
	 * 消息服务注册容器
	 * Map<TerminalNo,MessageService>
	 */
	private static final Map<String, MessageService> messageServiceRepo = new HashMap<String, MessageService>();

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceFactory.class);

	/**
	 * <p>按照提供的配置构建服务对象</p>
	 * 
	 * @param terminalNo
	 * @param serviceConfig
	 * @param service
	 * @return
	 * @return MessageService
	 */
	protected static synchronized MessageService create(String terminalNo, ServiceConfig serviceConfig,
			MessageServiceTemplate service) {
		return create(terminalNo, serviceConfig, null, service);
	}

	protected static synchronized MessageService create(String terminalNo, ServiceConfig serviceConfig,
			MessageConfig msgConfig, MessageServiceTemplate service) {
		if (MessageServiceFactory.msgServiceNum <= 0) {
			throw new ServiceNumNotSetException();
		}

		if (StringUtils.isEmpty(terminalNo)) {
			return null;
		}
		service.terminalNo = terminalNo;
		if (messageServiceRepo.containsKey(terminalNo)) {
			return getService(terminalNo);
		}

		IntegrateConfig ic = new IntegrateConfig();
		ic.setServiceConfig(serviceConfig);
		if (msgConfig == null) {
			msgConfig = MessageConfig.parserConfig();
		}
		ic.setMessageConfig(msgConfig);
		service.setConfig(ic);
		service.setServiceEventQueue(ServiceEventQueue.getInstance());
		messageServiceRepo.put(terminalNo, service);

		MessageNoTimeLogger.setServiceConfig(serviceConfig);
		LOGGER.info("MessageService构建完毕!");
		return service;
	}

	/**
	 * <p>取消终端注册的服务</p>
	 * 
	 * @param terminalNo
	 * @return void
	 */
	public static synchronized void destroy(String terminalNo) {
		if (messageServiceRepo.containsKey(terminalNo)) {
			MessageService ms = messageServiceRepo.remove(terminalNo);
			ResponseMessage rspMsg = null;
			if (ms.getState().equals(MessageService.State.LOGON)) {
				rspMsg = ms.logout(null);
			} else if (ms.getState().equals(MessageService.State.CONNECT)) {
				rspMsg = ms.cleanup();
			}
			if (rspMsg == null || rspMsg.success()) {
				LOGGER.info("终端<{}>服务注销成功!");
			} else {
				ErrorMessage em = rspMsg.getErrorMessage();
				LOGGER.error(em.toString());
			}
		}
	}

	/**
	 * <p>通过终端号得到缓存的服务对象</p>
	 * 
	 * @param terminalNo
	 * @return
	 * @return MessageService
	 */
	public static MessageService getService(String terminalNo) {
		return messageServiceRepo.get(terminalNo);
	}
}
