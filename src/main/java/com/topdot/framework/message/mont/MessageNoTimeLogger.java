/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.channel.MessageTimeLogger.java
 * 所含类: MessageTimeLogger.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2013-5-14 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.mont;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.config.ServiceConfig;
import com.topdot.framework.message.protocol.MessageBody;
import com.topdot.framework.message.protocol.MessageHeader;
import com.topdot.framework.message.protocol.RequestMessage;
import com.topdot.framework.message.protocol.ResponseMessage;
import com.topdot.framework.util.DateUtils;

/**
 * <p>失效信息记录</p>
 * 
 * <p>类用途详细说明</p>
 * 
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName MessageTimeLogger
 * @author zhangyongxin
 * @version 1.0
 * 
 */

public class MessageNoTimeLogger {
	
	private static ServiceConfig serviceConfig;
	
	/** 失效响应消息记录器 */
	private static final Logger msgRspNoTime = LoggerFactory.getLogger("msgRspNoTime");
	/** 失效请求消息记录器 */
	private static final Logger msgReqNoTime = LoggerFactory.getLogger("msgReqNoTime");
	
	/**
	 * <p> 判断响应信息是否已经超过存活时间 </p>
	 * 
	 * @param responseMsg
	 * @return
	 * @return boolean
	 */
	public static boolean isTimeOut(ResponseMessage responseMsg) {
		Date createDate = responseMsg.getCreateDate();
		if (createDate == null) {
			return false;
		}
		Date lastTime = DateUtils.addSeconds((Date) createDate.clone(), serviceConfig.getResponseLiveTime());
		if (lastTime.before(new Date())) {
			msgRspNoTime.info("{}-{}", responseMsg.getHeader().getSequenceId(), responseMsg.getJsonStr());
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * <p>判断请求是否已经超时（超时的请求将不再被处理） </p>
	 * 
	 * @param requestMsg
	 * @return
	 * @return boolean
	 */
	public static boolean isTimeOut(RequestMessage<? extends MessageHeader, ? extends MessageBody> requestMsg) {
		Date createDate = requestMsg.getCreateDate();
		if (createDate == null) {
			return false;
		}
		Date lastTime = DateUtils.addSeconds((Date) createDate.clone(), serviceConfig.getRequestLiveTime());
		if (lastTime.before(new Date())) {
			msgReqNoTime.info("{}-{}", requestMsg.getHeader().getSequenceId(), requestMsg);
			return true;
		} else {
			return false;
		}
	}

	public static void setServiceConfig(ServiceConfig serviceConfig) {
		MessageNoTimeLogger.serviceConfig = serviceConfig;
	}	
}
