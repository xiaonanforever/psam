package com.topdot.framework.message;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.channel.MessageWorkerPoolImpl;
import com.topdot.framework.message.channel.RequestChannel;
import com.topdot.framework.message.channel.RequestMessageQueue;
import com.topdot.framework.message.channel.ResponseMessageQueue;
import com.topdot.framework.message.channel.impl.IORequestChannelImpl;
import com.topdot.framework.message.config.IntegrateConfig;
import com.topdot.framework.message.config.ServiceConfig;
import com.topdot.framework.message.exception.ConnectionException;
import com.topdot.framework.message.exception.RequestException;
import com.topdot.framework.message.exception.ServiceNotLogonException;
import com.topdot.framework.message.filter.FilterChain;
import com.topdot.framework.message.filter.impl.CompressFilterImpl;
import com.topdot.framework.message.filter.impl.FilterChainImpl;
import com.topdot.framework.message.filter.impl.HttpOutFilterImpl;
import com.topdot.framework.message.mont.ServiceEventQueue;
import com.topdot.framework.message.mont.ServiceHolding;
import com.topdot.framework.message.protocol.MessageBody;
import com.topdot.framework.message.protocol.MessageHeader;
import com.topdot.framework.message.protocol.RequestMessage;
import com.topdot.framework.message.protocol.ResponseMessage;

public abstract class MessageServiceTemplate implements MessageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceTemplate.class);
	private static AtomicLong sumTimes = new AtomicLong(0);
	private static AtomicInteger callTimes = new AtomicInteger(1);

	/** 登陆次数 */
	private AtomicInteger logTimes = new AtomicInteger(0);
	/** 集成配置 */
	protected IntegrateConfig config;

	/** 服务会话 */
	protected Session session;

	/** 请求通道 */
	protected RequestChannel requestChannel;

	/** 响应队列 */
	protected ResponseMessageQueue rspMsgQueue;

	/** 登陆请求消息 */
	protected RequestMessage<? extends MessageHeader, ? extends MessageBody> logonReqMsg;

	/** 消息处理线程 */
	protected MessageWorkerPoolImpl msgWorkers;
	/** 服务状态 */
	protected volatile State state = State.UNCONNECT;
	/** 服务事件队列 */
	protected ServiceEventQueue serviceEventQueue;

	/** 终端编号 */
	protected String terminalNo;

	/** 登陆完成Hook */
	private LogonHook hook;

	/***
	 * <p>初始化模块配置</p>
	 */
	protected void init() {
		LOGGER.info("连接初始化...");
		ServiceConfig serviceConfig = this.getConfig().getServiceConfig();
		if (terminalNo == null || "".equals(terminalNo)) {
			terminalNo = serviceConfig.getCommonParameterTerminalNo();
		} else {
			serviceConfig.setCommonParameterTerminalNo(terminalNo);
		}

		LOGGER.info("构建请求、响应队列...");
		RequestMessageQueue reqMsgQueue = new RequestMessageQueue();
		reqMsgQueue.setMaxSize(serviceConfig.getReqMaxSize());
		rspMsgQueue = new ResponseMessageQueue(serviceConfig.getResMaxSize());

		LOGGER.info("构建响应消息处理线程池...");
		msgWorkers = MessageWorkerPoolImpl.getInstance(terminalNo);
		msgWorkers.setRequestMessageQueue(reqMsgQueue);
		msgWorkers.setResponseMessageQueue(rspMsgQueue);
		msgWorkers.setConfig(this.getConfig());

		LOGGER.info("构建请求通道...");
		IORequestChannelImpl reqChannel = new IORequestChannelImpl();
		reqChannel.setRunning(true);
		reqChannel.setRequestMessageQueue(reqMsgQueue);
		reqChannel.setConfig(this.getConfig());
		reqChannel.setMessageService(this);
		reqChannel.setServiceEventQueue(serviceEventQueue);

		LOGGER.info("设置请求通道的FilterChain...");
		FilterChain reqChain = new FilterChainImpl();
		reqChain.addFilter(new CompressFilterImpl());
		reqChain.addFilter(new HttpOutFilterImpl());
		reqChannel.setChain(reqChain);
		requestChannel = reqChannel;

		LOGGER.info("启动请求通道...");
		requestChannel.setRunning(true);
		LOGGER.info("启动响应处理线程...");
		msgWorkers.start();

		LOGGER.info("设置服务会话...");
		Session session = new Session();
		// session.setTerminalNo(terminalNo);
		setSession(session);
		config.getMessageConfig().setReciverSession(session);// ?
	}

	/**
	 * <p>建立物理连接</p>
	 * 
	 * @param reqMsg
	 * @return
	 * @return ResponseMessage
	 */
	public ResponseMessage phyLogon() {
		LOGGER.info("建立物理连接...");
		ResponseMessage rspMsg = new ResponseMessage();
		state = State.UNCONNECT;
		try {
			init();
			state = State.INIT;
			connect();
			state = State.CONNECT;
		} catch (ConnectionException e) {// connect()抛出异常
			LOGGER.error("物理连接建立失败", e);
			rspMsg.setErrorMessage(e.getErrorMessage());
			ResponseMessage cleanupRspMsg = cleanup();
			if (cleanupRspMsg != null) {
				rspMsg = cleanupRspMsg;
			}
		}
		return rspMsg;
	}

	/**
	 * <p>建立业务连接</p>
	 * 
	 * @param reqMsg
	 * @return
	 * @return ResponseMessage
	 */
	protected ResponseMessage bizLogon(RequestMessage<? extends MessageHeader, ? extends MessageBody> reqMsg) {
		LOGGER.info("建立业务连接...");
		ResponseMessage rspMsg = null;
		try {
			rspMsg = requestChannel.call(reqMsg);
			if (rspMsg.success()) {
				state = State.LOGON;
				LOGGER.info("业务连接建立成功!");
			} else {
				LOGGER.info("业务连接建立失败...");
				ResponseMessage cleanupRspMsg = cleanup();
				if (cleanupRspMsg != null) {
					rspMsg = cleanupRspMsg;
				}
			}
		} catch (RequestException e) {
			rspMsg = new ResponseMessage();
			rspMsg.setErrorMessage(e.getErrorMessage());
			LOGGER.error("业务连接建立异常...", e);
			ResponseMessage cleanupRspMsg = cleanup();
			if (cleanupRspMsg != null) {
				rspMsg = cleanupRspMsg;
			}
		}
		return rspMsg;
	}

	@Override
	public synchronized ResponseMessage logon(RequestMessage<? extends MessageHeader, ? extends MessageBody> reqMsg) {
		LOGGER.info("终端<{}>第{}次登陆...", config.getServiceConfig().getCommonParameterTerminalNo(), logTimes.incrementAndGet());
		logonReqMsg = reqMsg;
		ResponseMessage rspMsg = null;
		if (State.LOGON.equals(state)) {
			rspMsg = new ResponseMessage();
			// } else if (State.INIT.equals(state)) {
			// rspMsg = bizLogon(reqMsg);
		} else if (State.CONNECT.equals(state)) {
			rspMsg = bizLogon(reqMsg);
		} else {
			rspMsg = phyLogon();
			if (rspMsg.success()) {
				rspMsg = bizLogon(reqMsg);
			}
		}
		if (rspMsg.success()) {
			ServiceHolding.startHold(terminalNo, this);// 服务保持线程开启
			// 本服务准备好，等待前台服务一同准备好之后再释放
			MessageServiceFactory.serviceReady();
			if (hook != null)
				hook.completed();
		}
		return rspMsg;
	}

	@Override
	public ResponseMessage reLogon() {
		LOGGER.info("终端{}重登陆...", terminalNo);
		logonReqMsg.getHeader().setCreateDate(new Date());
		return logon(logonReqMsg);
	}

	@Override
	public ResponseMessage cleanup() {
		ServiceHolding.stopHold(terminalNo);
		LOGGER.info("释放资源...");
		ResponseMessage rspMsg = null;
		state = State.CONNECT;
		if (requestChannel != null) {
			LOGGER.info("停止发送通道...");
			requestChannel.setRunning(false);
		}
		LOGGER.info("释放网络链接...");
		try {
			release();
			state = State.UNCONNECT;
		} catch (ConnectionException e) {
			LOGGER.error("释放网络链接异常!", e);
			rspMsg = new ResponseMessage();
			rspMsg.setErrorMessage(e.getErrorMessage());
		} finally {
			LOGGER.info("停止消息线程...");
			if (msgWorkers != null) {
				msgWorkers.stop();
			}
		}
		return rspMsg;
	}

	@Override
	public synchronized ResponseMessage logout(RequestMessage<? extends MessageHeader, ? extends MessageBody> reqMsg) {
		ResponseMessage rspMsg = null;
		if (reqMsg != null) {
			try {
				rspMsg = requestChannel.call(reqMsg);
			} catch (RequestException e) {
				rspMsg = new ResponseMessage();
				rspMsg.setErrorMessage(e.getErrorMessage());
				return rspMsg;
			}
		}
		ResponseMessage cleanupRspMsg = cleanup();
		if (cleanupRspMsg != null) {
			rspMsg = cleanupRspMsg;
		}

		return rspMsg;
	}

	/**
	 * <p>模板方法:网络资源连接</p>
	 * 
	 * @throws ConnectionException
	 * @return void
	 */
	protected abstract void connect() throws ConnectionException;

	/**
	 * <p>模板方法:网络资源释放</p>
	 * 
	 * @throws ConnectionException
	 * @return void
	 */
	protected abstract void release() throws ConnectionException;

	/**
	 * <p>异步调用服务</p>
	 * 
	 * @param requestMsg
	 * @return
	 * @return boolean
	 * @see call 同步调用
	 */
	public void acall(RequestMessage<? extends MessageHeader, ? extends MessageBody> requestMsg) throws RequestException {
		if (state.equals(State.LOGON)) {
			requestChannel.acall(requestMsg);
		} else {
			LOGGER.error("没有登陆服务,不能执行同步请求!");
			throw new ServiceNotLogonException("没有登陆服务,不能执行同步请求!");
		}
	}

	@Override
	public ResponseMessage call(RequestMessage<? extends MessageHeader, ? extends MessageBody> requestMsg) {
		Long start = System.currentTimeMillis();
		LOGGER.debug("同步请求...");
		int seqId = requestMsg.getHeader().getSequenceId();
		if (seqId == MessageHeader.DEFAULT_SEQUENCE_ID) {
			seqId = (int) (Math.random() * Integer.MAX_VALUE);
		}

		ResponseMessage rspMsg = null;
		if (state.equals(State.LOGON)) {
			try {
				requestMsg.getHeader().setSequenceId(seqId);
				rspMsg = requestChannel.call(requestMsg);
			} catch (RequestException e) {
				LOGGER.error(seqId + ":请求异常->", e);
				if (rspMsg == null) {
					rspMsg = new ResponseMessage();
				}
				rspMsg.setErrorMessage(e.getErrorMessage());
			}
		} else {
			LOGGER.error("{}:没有登陆服务,不能执行同步请求!", seqId);
			throw new ServiceNotLogonException("没有登陆服务,不能执行同步请求!");
		}
		long duration = System.currentTimeMillis() - start;
		LOGGER.info("{}:请求结束耗时:{}ms", seqId, duration);

		sumTimes.addAndGet(duration);
		if (callTimes.incrementAndGet() % 10000 == 0) {
			LOGGER.info("统计信息:请求总次数:{},平均响应时间:{}", callTimes.get(), sumTimes.get() / callTimes.get());
		}
		return rspMsg;
	}

	public void registerLogonHook(LogonHook hook) {
		this.hook = hook;
	}

	@Override
	public IntegrateConfig getConfig() {
		return config;
	}

	public void setConfig(IntegrateConfig config) {
		this.config = config;
	}

	public void setSession(Session session) {
		this.session = session;
		this.session.setMessageService(this);
	}

	public RequestChannel getRequestChannel() {
		return requestChannel;
	}

	public void setRequestChannel(RequestChannel requestChannel) {
		this.requestChannel = requestChannel;
	}

	public State getState() {
		LOGGER.info("<{}>服务状态:<{}>-登陆次数:" + logTimes.get(), terminalNo, state);
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setServiceEventQueue(ServiceEventQueue serviceEventQueue) {
		this.serviceEventQueue = serviceEventQueue;
	}

	public ServiceEventQueue getServiceEventQueue() {
		return serviceEventQueue;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public ResponseMessageQueue getRspMsgQueue() {
		return rspMsgQueue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((terminalNo == null) ? 0 : terminalNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MessageServiceTemplate other = (MessageServiceTemplate) obj;
		if (state != other.state)
			return false;
		if (terminalNo == null) {
			if (other.terminalNo != null)
				return false;
		} else if (!terminalNo.equals(other.terminalNo))
			return false;
		return true;
	}
}