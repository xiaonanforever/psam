package com.topdot.framework.message.channel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.topdot.framework.message.protocol.ResponseMessage;

/**
 * <p>Message 响应消息队列</p>
 * <p>由JMS Listener放入消息，而由MessageWorkerThread取出，多线程工作环境</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */
public class ResponseMessageQueue {
	//private static final Logger LOGGER = LoggerFactory.getLogger(ResponseMessageQueue.class);

	/**
	 * 内部消息队列（相对MQ而言）- 接收到的响应消息。 如果是同步处理，那么需要同步处理的消息请求消息记录在synReqQueue
	 */
	
	private final BlockingQueue<ResponseMessage> rspQueue;// ConcurrentLinkedQueue<ResponseMessage>

	/** 最大队列深度 */
	private int maxSize = 10000;

	public ResponseMessageQueue() {
		this.rspQueue = new LinkedBlockingQueue<ResponseMessage>(maxSize);
	}

	public ResponseMessageQueue(int maxSize) {
		this.rspQueue = new LinkedBlockingQueue<ResponseMessage>(maxSize);
	}
	
	public  void put(ResponseMessage message) throws InterruptedException {
		boolean flag  = rspQueue.offer(message);
		if(!flag) {
			rspQueue.put(message);
		}
	}

	public  ResponseMessage take() throws InterruptedException {
		return rspQueue.take();
	}

	/**
	 * <p> 查询接收Queue当前未处理容量 </p>
	 * 
	 * @return
	 * @return int
	 */
	public int size() {
		return rspQueue.size();
	}
}