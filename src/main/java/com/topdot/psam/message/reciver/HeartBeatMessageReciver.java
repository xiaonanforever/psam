/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.reciver.HeartBeatMessageReciver.java
 * 所含类: HeartBeatMessageReciver.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月19日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.message.reciver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.MessageService;
import com.topdot.framework.message.MessageServiceTemplate;
import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.exception.RequestException;
import com.topdot.framework.message.protocol.RequestMessage;
import com.topdot.framework.message.protocol.ResponseMessage;
import com.topdot.framework.message.reciver.AbstractMessageReciver;
import com.topdot.psam.message.protocol.PsamReqBody;
import com.topdot.psam.message.protocol.PsamReqHeader;
import com.topdot.psam.message.protocol.PsamRsp;

/**
 * <p>HeartBeatMessageReciver</p>
 * <p>服务端心跳探测</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class HeartBeatMessageReciver extends AbstractMessageReciver<ResponseMessage> {
	private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeatMessageReciver.class);
	private static final String MESSAGE_DEFINATION_KEY = "message.defination.heart.beat";

	@Override
	public PsamRsp parse(String jsonStr) throws MessageFormateException {
		return new PsamRsp(jsonStr);
	}

	@Override
	public void recive(ResponseMessage responseMsg) {
		LOGGER.info("探测心跳,收到心跳包后发送回报...");
		MessageService ms = getSession().getMessageService();
		if (ms != null) {
			RequestMessage<PsamReqHeader, PsamReqBody> reqMsg = new RequestMessage<PsamReqHeader, PsamReqBody>();
			reqMsg.setMessageDefinationKey(MESSAGE_DEFINATION_KEY);
			reqMsg.setHeader(new PsamReqHeader());
			reqMsg.setBody(null);
			try {
				((MessageServiceTemplate) ms).acall(reqMsg);
			} catch (RequestException e) {
				LOGGER.error("响应心跳探测异常", e);
			}
		} else {
			LOGGER.error("MessageService服务对象为空");
		}
	}

}
