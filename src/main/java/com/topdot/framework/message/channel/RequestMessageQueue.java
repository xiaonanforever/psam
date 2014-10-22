package com.topdot.framework.message.channel;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.topdot.framework.message.exception.InnerQueueFullException;
import com.topdot.framework.message.protocol.MessageBody;
import com.topdot.framework.message.protocol.MessageHeader;
import com.topdot.framework.message.protocol.RequestMessage;

/**
 * <p> 请求消息队列 </p>
 * <p> MessageSender放入，MessageWorkerThread取出，多线程工作环境 </p>
 * <p> Copyright: 版权所有 (c) 2010 - 2014 </p>
 * <p> Company: xxx </p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */
public class RequestMessageQueue {

	/** 记录已发送请求 */
	private final Map<Integer, RequestMessage<? extends MessageHeader, ? extends MessageBody>> reqMap = new ConcurrentHashMap<Integer, RequestMessage<? extends MessageHeader, ? extends MessageBody>>();

	private final BlockingQueue<RequestMessage<? extends MessageHeader, ? extends MessageBody>> reqQueue = new LinkedBlockingQueue<RequestMessage<? extends MessageHeader, ? extends MessageBody>>();
	/** 请求消息最大数，如果为零表示不允许发送请求 */
	private int maxSize;

	public RequestMessageQueue() {
	}

	/**
	 * <p> 放入消息</p>
	 * 
	 * @param key message的 id
	 * @param reqMsg
	 * @return
	 * @throws InnerQueueFullException
	 */
	public void put(int key, RequestMessage<? extends MessageHeader, ? extends MessageBody> reqMsg)
			throws InnerQueueFullException {
		if (reqMap.size() >= maxSize) {
			throw new InnerQueueFullException("请求队列深度已达预设最大值:" + maxSize);
		}
		reqMap.put(key, reqMsg);
	}

	/**
	 * <p>获取消息 </p>
	 * 
	 * @param key
	 * @param remove-是否移除
	 * @return
	 * @return RequestMsg
	 */
	public RequestMessage<? extends MessageHeader, ? extends MessageBody> take(int key, boolean remove) {
		if (reqMap.containsKey(key)) {
			if (remove) {
				return reqMap.remove(key);
			} else {
				return reqMap.get(key);
			}
		}
		return null;
	}

	public Set<Integer> getAll() {
		return reqMap.keySet();
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public int size() {
		return reqMap.size();
	}

	public void clear() {
		reqMap.clear();
	}

	public void put(RequestMessage<? extends MessageHeader, ? extends MessageBody> req) throws InterruptedException {
		if(!reqQueue.offer(req)) {
			reqQueue.put(req);
		}
	}

	public RequestMessage<? extends MessageHeader, ? extends MessageBody> take(boolean b) throws InterruptedException {
		return reqQueue.take();
	}
}