/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.gimp.eit.AbstractRequestChannel.java
 * 所含类: AbstractRequestChannel.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2013-1-31 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.channel;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.MessageServiceTemplate;
import com.topdot.framework.message.config.IntegrateConfig;
import com.topdot.framework.message.constants.MsgConstants;
import com.topdot.framework.message.exception.ChannelException;
import com.topdot.framework.message.exception.ErrorDefination;
import com.topdot.framework.message.exception.InnerQueueFullException;
import com.topdot.framework.message.exception.MessageFilterException;
import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.exception.RequestException;
import com.topdot.framework.message.exception.TimeoutException;
import com.topdot.framework.message.filter.FilterChain;
import com.topdot.framework.message.filter.FilterMessage;
import com.topdot.framework.message.protocol.MessageBody;
import com.topdot.framework.message.protocol.MessageHeader;
import com.topdot.framework.message.protocol.RequestMessage;
import com.topdot.framework.message.protocol.ResponseMessage;
import com.topdot.framework.util.StringUtils;

/**
 * <p>抽象请求通道模板</p>
 * <p>实现
 * 1、运行状态管理（获取、设置）
 * 2、同步请求-进一步调用异步请求
 * 3、异步请求
 * 未实现方法：
 * 1、发送RequestMessage Filter</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public abstract class AbstractRequestChannel implements RequestChannel {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRequestChannel.class);

	/** 集成配置信息 */
	protected IntegrateConfig config;

	/** 请求通道关联的Chain */
	protected FilterChain chain;

	/** 所属的Service */
	protected MessageServiceTemplate messageService;

	/** 服务启动以来发送的请求数目 */
	// private volatile long i;

	/** 异步消息转同步调用是先进入requestMessageQueue中的临时存储区 生产消费模式中的通道 */
	private RequestMessageQueue requestMessageQueue;

	/** 通道运行状态 */
	private volatile boolean running = true;

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public RequestMessageQueue getRequestMessageQueue() {
		return requestMessageQueue;
	}

	public void setRequestMessageQueue(RequestMessageQueue requestMessageQueue) {
		this.requestMessageQueue = requestMessageQueue;
	}

	public void setConfig(IntegrateConfig config) {
		this.config = config;
	}

	public FilterChain getChain() {
		return chain;
	}

	public void setChain(FilterChain chain) {
		this.chain = chain;
	}

	/**
	 * <p>发送请求消息</p>
	 * 
	 * @param requestMsg
	 * @param reserved是否保存请求消息
	 * @throws RequestException
	 * @return void
	 */
	public void request(RequestMessage<? extends MessageHeader, ? extends MessageBody> requestMsg, boolean reserved)
			throws RequestException {
		int seqIda = requestMsg.getHeader().getSequenceId();
		LOGGER.info("{}:进入请求前处理...", seqIda);
		Long start = System.currentTimeMillis();
		if (!running) {
			throw new RequestException("请求通道已关闭!");
		}
		LOGGER.debug("封装消息定义...");
		MessageHeader messageHeader = requestMsg.getHeader();

		// 如果没有设置消息序列Id，则随机生成一个8位的整数
		if (messageHeader.getSequenceId() == MessageHeader.DEFAULT_SEQUENCE_ID) {
			messageHeader.setSequenceId(start.intValue());
		}
		int seqId = messageHeader.getSequenceId();
		String msgKey = requestMsg.getMessageDefinationKey();
		if (StringUtils.isEmpty(msgKey)) {
			msgKey = requestMsg.getClass().getName();
		}
		MessageHeader messageDef = config.getMessageConfig().getMessageDefByKeyMap().get(msgKey).getMessageType();
		messageHeader.setCommandType(messageDef.getCommandType());
		messageHeader.setCommandId(messageDef.getCommandId());
		messageHeader.setCommandName(messageDef.getCommandName());

		LOGGER.debug("{}:设置超时...", seqId);
		/* 设置超时并自动判断是否是启用异步<br/> 如果timeout -1 表示启用异步 0 采用默认的超时时间 ，其他值 保留 */
		if (messageHeader.getTimeout() <= 0) {// 优先采用运行时设置的，否则进行关联配置
			if (messageDef.getTimeout() == RequestMessage.TIME_OUT_ASYN) {
				messageHeader.setAsyn(true);
			} else {
				messageHeader.setAsyn(messageDef.isAsyn());
				if (messageDef.getTimeout() > RequestMessage.TIME_OUT_DEFAULT) {
					messageHeader.setTimeout(messageDef.getTimeout());
				} else {
					messageHeader.setTimeout(config.getServiceConfig().getDefaultTimeout());
				}
			}
		}

		try {
			JSONObject jo = messageHeader.buildReqJSONObject();
			try {
				LOGGER.debug("{}:设置消息公共消息...", seqId);
				Map<String, Object> commonParameters = config.getServiceConfig().getCommonParameters();
				for (String key : commonParameters.keySet()) {
					// jo.putOnce(key, commonParameters.get(key));
					jo.put(key, commonParameters.get(key));
				}
			} catch (JSONException e) {
				throw new MessageFormateException("附加共用Header消息异常!", e);
			}
			LOGGER.debug("{}:执行消息过滤...", seqId);
			MessageBody mb = requestMsg.getBody();
			FilterMessage filterMsg;
			if (mb == null) {
				filterMsg = new FilterMessage(jo.toString(), null);
			} else {
				filterMsg = new FilterMessage(jo.toString(), mb.buildReqJSONObject().toString());
			}
			LOGGER.debug("{}:Json处理完毕", seqId);
			if (chain != null) {
				chain.getConfig().addAttr(MsgConstants.SERVICE_CONFIG, config.getServiceConfig());
				chain.doFilter(filterMsg);
				LOGGER.debug("{}:Filter处理完毕", seqId);
			}
			/* 是否需要保留请求消息（如果是同步请求需要保存） */
			if (reserved) {
				long end = System.currentTimeMillis();
				Object[] args = new Object[] { seqId, requestMessageQueue.size(), end - start };
				LOGGER.info("{}:记录待发Request，请求队列深度:{},消息处理花费时间:{}ms", args);
				requestMessageQueue.put(seqId, requestMsg);
			}
			/* 实际发送消息 */
			long end = System.currentTimeMillis();
			Object[] args = new Object[] { seqId, end - start };
			LOGGER.debug("{}:内部消息处理花费时间:{}ms", args);
			sendMsg(filterMsg.getFilterMsg());
			args[1] = System.currentTimeMillis() - end;
			LOGGER.debug("{}:消息完毕.发送花费时间:{}ms", args);
		} catch (ChannelException e) {
			throw new RequestException(e);
		} catch (MessageFormateException e) {
			throw new RequestException(e);
		} catch (InnerQueueFullException e) {
			throw new RequestException(e);
		} catch (MessageFilterException e) {
			throw new RequestException("通过Scocket OutputWriter发送消息异常!", e);
		}
	}

	@Override
	public void acall(RequestMessage<? extends MessageHeader, ? extends MessageBody> requestMsg)
			throws RequestException {
		request(requestMsg, false);
	}

	@Override
	public ResponseMessage call(RequestMessage<? extends MessageHeader, ? extends MessageBody> requestMsg)
			throws RequestException {
		requestMsg.setReady(false);
		requestMsg.setCancel(false);
		request(requestMsg, true);
		ResponseMessage responseMsg;
		try {
			responseMsg = requestMsg.getResponseMsg();// 这里是同步点
		} catch (TimeoutException e) {
			throw new RequestException(e);
		}
		if (requestMsg.isCancel()) {
			RequestException ex = new RequestException("请求被取消!");
			ex.setErrorDefination(ErrorDefination.ERROR_RSP_CHANNEL);
			throw ex;
		}
		MessageHeader rspMsgHeader = responseMsg.getHeader();
		if (rspMsgHeader.getErrorCode() != 0) {
			LOGGER.error("响应不正确.请求:{},返回响应:{}", requestMsg, responseMsg);
		}
		return responseMsg;
	}

	public void setMessageService(MessageServiceTemplate messageService) {
		this.messageService = messageService;
	}
}
