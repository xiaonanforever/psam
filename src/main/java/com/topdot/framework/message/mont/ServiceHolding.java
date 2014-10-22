/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.mont.ServiceHoldingThread.java
 * 所含类: ServiceHoldingThread.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2013-5-14 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.mont;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.MessageService;
import com.topdot.framework.message.MessageServiceTemplate;
import com.topdot.framework.message.channel.AbstractRequestChannel;
import com.topdot.framework.message.channel.RequestMessageQueue;

/**
 * <p>服务可用性保持</p>
 * <p>发送和接收数据时如果发现输入/输出流断开，发起重新登陆请求（只发一次）</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName ServiceHoldingThread
 * @author zhangyongxin
 * @version 1.0
 */

public class ServiceHolding extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceHolding.class);

	private static final Map<String, MessageServiceTemplate> srvHoldingMap = new ConcurrentHashMap<String, MessageServiceTemplate>();
	
	private static ServiceHolding thread;

	private static final ServiceEventQueue serviceEventQueue = ServiceEventQueue.getInstance();

	private static final ExecutorService  serviceHoldingPool = Executors.newSingleThreadExecutor(new MsgThreadFactory(
			"Service-Holding"));

	private ServiceHolding() {
		super("Service Holding:" + System.currentTimeMillis());
	}

	/** 服务重拨剩余次数 */
	private static AtomicInteger remainTimes;

	public void run() {
		LOGGER.info("服务保持启动.");
		while (true) {
			try {
				ServiceEvent event = serviceEventQueue.take(true);
				if (event == null) {//
					LOGGER.debug("在没有服务故障事件的条件下，执行数据清理");
					for (String terminalNo : srvHoldingMap.keySet()) {
						MessageServiceTemplate ms = srvHoldingMap.get(terminalNo);
						RequestMessageQueue reqQueue = ((AbstractRequestChannel) ms.getRequestChannel())
								.getRequestMessageQueue();
						for (Integer key : reqQueue.getAll()) {
							boolean timeout = MessageNoTimeLogger.isTimeOut(reqQueue.take(key, false));
							if (timeout) {
								reqQueue.take(key, true);
								LOGGER.warn("移除失效请求消息:{}.", key);
							}
						}
					}
					continue;
				}
				LOGGER.info("处理虚拟终端<{}>事件:{}...", event.getTerminalNo(), event.getEventType());
				MessageServiceTemplate ms = srvHoldingMap.get(event.getTerminalNo());
				ms.setState(MessageService.State.BROKEN);
				ms.cleanup();
				srvHoldingMap.remove(ms.getTerminalNo());
				RequestMessageQueue reqQueue = ((AbstractRequestChannel) ms.getRequestChannel())
						.getRequestMessageQueue();
				for (Integer key : reqQueue.getAll()) {
					reqQueue.take(key, true).cancelResponseMsg();
				}

				switch (event.getEventType()) {
				case SOCKET_CLOSED: {
					break;
				}
				case SOCKET_IN_CLOSED: {
					break;
				}
				case SOCKET_OUT_CLOSED: {
					break;
				}
				default: {
					LOGGER.warn("不支持事件:{}.", event.getEventType());
				}
				}
				// 执行重新登陆
				int redialTimes = ms.getConfig().getServiceConfig().getServerRedialTimes();
				remainTimes = new AtomicInteger(redialTimes);// 重新拨号（登陆次数）
				if (redialTimes > 0) {// 不需要执行重新登陆
					while (remainTimes.get() > 0) {
						LOGGER.info("还有{}次重连机会...", remainTimes);
						// 修改成后面只是简单的处理，这样可以把登陆放到最后
						if (ms.reLogon().success()) {
							LOGGER.info("终端<{}>登陆成功之后，清理相关服务事件...", event.getTerminalNo());
							serviceEventQueue.tryClean(event.getTerminalNo());
							break;// 成功之后跳出
						}
						if (remainTimes.decrementAndGet() == 0) {
							LOGGER.error("重新连接次数超过最大尝试连接次数:{}", redialTimes);
						}
					}
				}		
			} catch (Exception e) {
				LOGGER.warn("服务保持出现告警.", e);
			}
		}// end of while
	}

	/**
	 * <p>开始服务保持</p>
	 * 
	 * @param ms
	 * @param serviceEventQueue
	 */
	public synchronized static void startHold(String terminalNo, MessageServiceTemplate ms) {
		if (thread == null) {
			thread = new ServiceHolding();
			thread.setDaemon(true);
			serviceHoldingPool.submit(thread);
		}
		srvHoldingMap.put(terminalNo, ms);
		LOGGER.info("虚拟终端<{}>服务保持...", terminalNo);
	}

	/***
	 * <p>停止服务保持</p>
	 * 
	 * @return void
	 */
	public synchronized static void stopHold(String terminalNo) {
		srvHoldingMap.remove(terminalNo);
		LOGGER.info("虚拟终端<{}>服务保持解除.", terminalNo);
		if(srvHoldingMap.size()==0) {
			ChannelDispatchService.stopRun();
			//同时需要清理selectKeyMap数据
		}
	}

	public static int getRemainTimes() {
		return remainTimes.get();
	}
	
	public static Map<String,MessageServiceTemplate> getActiveMST(){
		return Collections.unmodifiableMap(srvHoldingMap);
	}
}
