/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.cslc.framework.messaging.service.impl.MessageWorkerPoolImpl.java
 * 所含类: MessageWorkerPoolImpl.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2011-12-15 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.channel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.config.IntegrateConfig;
import com.topdot.framework.util.DateUtils;

/**
 * <p>响应消息工作线程池</p>
 * 
 * <p> 类用途详细说明 </p>
 * 
 * <p> Copyright: 版权所有 (c) 2010 - 2014 </p>
 * <p> Company: xxx </p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class MessageWorkerPoolImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageWorkerPoolImpl.class);
	private static Map<String, MessageWorkerPoolImpl> msgWorkerPoolMap;
	private static MessageWorkerPoolImpl msgWorkerPool;
	protected IntegrateConfig config;
	
	/** 线程池运行状态 */
	private boolean isRunning = true;
	
	/** 请求消息队列 */
	private RequestMessageQueue requestMessageQueue;
	
	/** 线程池 */
	private MessageWorker[] threadPool;
	
	/** 响应/报告消息队列 */
	private ResponseMessageQueue responseMessageQueue;
	
	private MessageWorkerPoolImpl() {
	}
	
	public static synchronized MessageWorkerPoolImpl getInstance() {
		if (msgWorkerPool == null) {
			msgWorkerPool = new MessageWorkerPoolImpl();
		}
		return msgWorkerPool;
	}
	
	public static synchronized MessageWorkerPoolImpl getInstance(String poolKey) {
		if (msgWorkerPoolMap == null) {
			msgWorkerPoolMap = new HashMap<String, MessageWorkerPoolImpl>();
		}
		if (!msgWorkerPoolMap.containsKey(poolKey)) {
			msgWorkerPoolMap.put(poolKey, new MessageWorkerPoolImpl());
		}
		return msgWorkerPoolMap.get(poolKey);
	}
	
	/**
	 * <p>启动工作线程</p>
	 * 
	 * @return void
	 */
	public void start() {
		LOGGER.info("开启工作线程...");
		// 创建响应消息处理分法工作线程
		threadPool = new MessageWorker[config.getServiceConfig().getWorkerThread()];
		String datetimeStr = DateUtils.getDateTimeString(new Date());
		for (int i = 0; i < threadPool.length; i++) {
			threadPool[i] = new MessageWorker("Message Worker-" + config.getServiceConfig().getCommonParameterTerminalNo()+":"+datetimeStr, this);
			threadPool[i].setIsRunning(true);
			threadPool[i].start();
		}
		isRunning = true;
	}
	
	/**
	 * <p>停止工作线程</p>
	 * 
	 * @return void
	 */
	public void stop() {
		if (isRunning && threadPool != null && threadPool.length > 0) {
			for (int i = 0; i < threadPool.length; i++) {
				threadPool[i].setIsRunning(false);
				threadPool[i].interrupt();
				threadPool[i] = null;
			}
			isRunning = false;
		}
	}
	
	public RequestMessageQueue getRequestMessageQueue() {
		return requestMessageQueue;
	}
	
	public void setRequestMessageQueue(RequestMessageQueue requestMessageQueue) {
		this.requestMessageQueue = requestMessageQueue;
	}
	
	public ResponseMessageQueue getResponseMessageQueue() {
		return responseMessageQueue;
	}
	
	public void setResponseMessageQueue(ResponseMessageQueue responseMessageQueue) {
		this.responseMessageQueue = responseMessageQueue;
	}
	
	public void setConfig(IntegrateConfig config) {
		this.config = config;
	}
}
