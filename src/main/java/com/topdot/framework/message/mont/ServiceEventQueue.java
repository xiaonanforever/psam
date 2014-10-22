/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.mont.ServiceEventQueue.java
 * 所含类: ServiceEventQueue.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2013-5-13 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.mont;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>服务事件队列</p>
 * <p>目前实现不足：take消息时会强占</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName ServiceEventQueue
 * @author zhangyongxin
 * @version 1.0
 */

public class ServiceEventQueue {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceEventQueue.class);

	/** 单例 */
	private final static ServiceEventQueue serviceEventQueue = new ServiceEventQueue();
	/** Poll数据最长等待时间:毫秒 */
	private AtomicInteger pollTime = new AtomicInteger(5 * 1000);
	/** 记录已发送请求 */
	private final BlockingQueue<ServiceEvent> queue = new LinkedBlockingQueue<ServiceEvent>(256);//new SynchronousQueue<ServiceEvent>();
	/** 记录已发送请求 */
	//final Map<String, SynchronousQueue<ServiceEvent>> queueAckMap = new ConcurrentHashMap<String, SynchronousQueue<ServiceEvent>>();

	private ServiceEventQueue() {
	}

	public static ServiceEventQueue getInstance() {
		return serviceEventQueue;
	}

	/**
	 * <p>放入事件</p>
	 * 
	 * @param serviceEvent
	 * @throws InterruptedException
	 */
	public void put(ServiceEvent serviceEvent) throws InterruptedException {
		if(!queue.offer(serviceEvent)) {
			queue.put(serviceEvent);
		}
	}

	/**
	 * <p>获取消息</p>
	 * 
	 * @return
	 * @return ServiceEvent
	 * @throws InterruptedException
	 */
	public ServiceEvent take(boolean remove) throws InterruptedException {
		if (remove) {
			return queue.poll(pollTime.intValue(), TimeUnit.MILLISECONDS);
		}
		return queue.peek();
	}
	
	public void tryClean(String terminalNo) {
		ServiceEvent event;
		while((event=queue.peek())!=null) {
			if(event.getTerminalNo().equals(terminalNo)) {
				LOGGER.info("移除相关事件:{}",queue.poll());
			}
		}
	}

	public void setPollTime(int pollTime) {
		this.pollTime.set(pollTime);
	}

}
