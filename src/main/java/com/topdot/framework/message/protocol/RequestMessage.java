/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.cslc.framework.messaging.model.RequestMessage.java
 * 所含类: RequestMessage.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2012-5-31 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.protocol;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.exception.TimeoutException;

/**
 * <p>请求消息</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class RequestMessage<H extends MessageHeader, B extends MessageBody> extends Message<H, B> {

	/** 超时设置为-1表示异步消息 */
	public static final int TIME_OUT_ASYN = -1;

	/** 超时设置为0表示为采用缺省超时设置 */
	public static final int TIME_OUT_DEFAULT = 0;

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestMessage.class);

	private static final Logger msgReqTimeout = LoggerFactory.getLogger("msgReqTimeout");

	/** RequestMsg对应的ResponseMsg */
	private ResponseMessage responseMsg;

	/** Future模式，内容是否准备好 */
	private boolean ready;

	/** 请求是否被需求-如发生在响应通道已经断开的条件，只能需求并进行重新发送，而不能坐等超时 */
	private boolean cancel;

	/** 消息定义Key */
	private String messageDefinationKey;
	private boolean timeout;
	
	/** Json Body内容在Json字符串中的位置 */
	protected int bodyIndex;

	/** 收到最原始的字符串 */
	private String jsonStr;

	public RequestMessage() {
	}

//	public RequestMessage(String messageDefinationKey) {
//		this.messageDefinationKey = messageDefinationKey;
//	}
	
	/**
	 * 根据JSONStr构建Response
	 * 
	 * @param jsonStr
	 * @throws MessageFormateException
	 */
	public RequestMessage(String jsonStr) throws MessageFormateException {
		this(jsonStr, true);
	}

	@SuppressWarnings("unchecked")
	protected RequestMessage(String jsonStr, boolean parseBody) throws MessageFormateException {
		this.jsonStr = jsonStr;
		bodyIndex = jsonStr.indexOf(MESSAGE_SEPERATE);
		if (bodyIndex >= 0) {
			header =   (H) new MessageHeader(jsonStr.substring(0, bodyIndex));
			if (parseBody && (bodyIndex + 1) < jsonStr.length()) {
				body = (B) new MessageBody(jsonStr.substring(bodyIndex + 1));
			}
			includeBody = true;
		} else {
			header = (H) new MessageHeader(jsonStr);
		}
	}
	public RequestMessage<?, ?> parse(String jsonStr) throws MessageFormateException {
		return new RequestMessage<H,B>(jsonStr);
	}

	/**
	 * <p>生成消息对应的JSON格式字符串 </p>
	 * 
	 * @return
	 * @return String
	 * @throws MessageFormateException
	 */
	public String buildReqMessageJSONStr() throws MessageFormateException {
		StringBuilder messageStrBuilder = new StringBuilder(header.buildReqJSONObject().toString());
		if (body != null) {
			messageStrBuilder.append(MESSAGE_SEPERATE);
			messageStrBuilder.append(body.buildReqJSONObject().toString());
		}
		return messageStrBuilder.toString();
	}

	/**
	 * <p> 同步处理请求 <br/>
	 * 发出请求之后获取响应，这是同步点</p>
	 * 
	 * @return
	 * @return ResponseMsg
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public ResponseMessage getResponseMsg() throws TimeoutException {
		LOGGER.debug("同步请求:{}-{}", header.getCommandName(), header.getSequenceId());
		long startTime = System.currentTimeMillis();
		while (!ready) {
			try {
				long nowTime = System.currentTimeMillis();
				long restTime = header.getTimeout() - (nowTime - startTime);
				if (restTime <= 0) {
					timeout = true;
					// msgReqTimeout.info("{}-{}", this.getHeader().getSequenceId(), this.toString());
					msgReqTimeout.info("{}", this.getHeader().getSequenceId());
					throw new TimeoutException("执行超时。超时设置:" + header.getTimeout() + ".请求ID:" + header.getSequenceId());
				}
				synchronized (this) {
					wait(header.getTimeout());
				}
			} catch (InterruptedException e) {
				LOGGER.error("线程执行对象wait方法时,出现InterruptedExceptiony异常!", e);
				// balk
			}
		}
		return responseMsg;
	}

	/**
	 * <p>响应在准备好后</p>
	 * 
	 * @param rspMsg
	 * @return void
	 */
	public void setResponseMsg(ResponseMessage rspMsg) {
		// if (ready) {
		// return; // balk
		// }
		MessageHeader rspHeader = rspMsg.getHeader();
		LOGGER.debug("同步响应:{}-{}", rspHeader.getCommandName(), rspHeader.getSequenceId());
		synchronized (this) {
			responseMsg = rspMsg;
			ready = true;
			notifyAll();
		}
	}

	/**
	 * <p>撤销请求</p>
	 * 
	 * @return void
	 */
	public synchronized void cancelResponseMsg() {
		LOGGER.warn("取消请求:{}!", this.getHeader().getSequenceId());
		ready = true;
		cancel = true;
		notifyAll();
	}

	public boolean isReady() {
		return ready;
	}

	/**
	 * <p>设置准备状态,外部接口不要使用</p>
	 * 
	 * @param ready
	 * @return void
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	/**
	 * <p>获取消息定义标示（主要用于消除命令在代码内写死）</p>
	 * 
	 * @return
	 * @return String
	 */
	public String getMessageDefinationKey() {
		return messageDefinationKey;
	}

	public void setMessageDefinationKey(String messageDefinationKey) {
		this.messageDefinationKey = messageDefinationKey;
	}

	@Override
	public Date getCreateDate() {
		return header.getCreateDate();
	}

	// public String getRequest

	public boolean isTimeout() {
		return timeout;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
	
	public String getJsonStr() {
		return jsonStr;
	}

	@Override
	public String toString() {
		try {
			String jsonStr = buildReqMessageJSONStr().toString();
			return jsonStr.replaceFirst("\u0000", "*");
		} catch (MessageFormateException e) {
			return "消息格式异常!";
		}
	}
}