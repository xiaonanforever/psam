/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.gimp.eit.DefaultResponseChannel.java
 * 含类: DefaultResponseChannel.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.channel;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.MessageServiceTemplate;
import com.topdot.framework.message.exception.MessageFilterException;
import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.filter.FilterChain;
import com.topdot.framework.message.filter.FilterMessage;
import com.topdot.framework.message.protocol.ResponseMessage;

/**
 * <p>抽象通道模板</p>
 * <p>实现：<br>
 * 1、接收接收JsonStr格式消息并放入到响应队列
 * 抽象：
 * 1、具体怎么接收到JsonStr交给实现</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public abstract class AbstractResponseChannel implements ResponseChannel {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResponseChannel.class);

	/** 关联的FilterChain */
	protected FilterChain chain;
	/** 当前的Service */
	protected MessageServiceTemplate mst;

	private volatile boolean running = true;

	private AtomicLong counter = new AtomicLong(1);


	@Override
	public void onMessage(String jsonStr) throws MessageFormateException, MessageFilterException {
		FilterMessage msg = new FilterMessage(jsonStr, null);
		onMessage(msg);
	}
	
	@Override
	public void onMessage(FilterMessage msg) throws MessageFormateException, MessageFilterException {
		long start = System.currentTimeMillis();
		if (chain != null) {
			chain.doFilter(msg);
		}
		counter.incrementAndGet();
		// chain.doFilter(msg);
		long end = System.currentTimeMillis();
		ResponseMessage rsp = new ResponseMessage(msg.getFilterMsg());
		int seqId = rsp.getHeader().getSequenceId();

		// MessageServiceFactory.getService(terminalNo)
		Object[] args = new Object[] { seqId, mst.getRspMsgQueue().size(), end - start };
		LOGGER.info("{}:接收消息,响应队列深度:{},Filter花费时间:{}ms", args);
		if (mst.getConfig().getServiceConfig().getValidCommandIds().indexOf('*') == -1
				&& mst.getConfig().getServiceConfig().getValidCommandIds()
						.indexOf(String.valueOf(rsp.getHeader().getCommandId())) == -1) {
			LOGGER.warn("跳过消息{}-{}处理.", rsp.getHeader().getCommandId(), msg.getFilterMsg());
			return;
		}
		try {
			mst.getRspMsgQueue().put(rsp);
			LOGGER.info("{}:放入队列花费时间:{}ms>>>>", seqId, System.currentTimeMillis() - end);
		} catch (InterruptedException e) {
			LOGGER.error(seqId + ":放入响应队列异常", e);
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public FilterChain getChain() {
		return chain;
	}

	public void setChain(FilterChain chain) {
		this.chain = chain;
	}	
}
