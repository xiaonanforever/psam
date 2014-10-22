/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.cslc.framework.msaging.reciver.DefaultMessageReciver.java
 * 所含类: DefaultMessageReciver.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2012-4-10       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.reciver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.protocol.ResponseMessage;

/**
 * <p>死信消息接收</p>
 *
 * <p>类用途详细说明</p>
 *
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class DeadMessageReciver extends AbstractMessageReciver<ResponseMessage> {
	/**死信消息记录器*/
	private static final Logger msgDead = LoggerFactory.getLogger("msgDead");
	public DeadMessageReciver() {
	}

	/* (non-Javadoc)
	  com.cslc.framework.messaging.reciver.AbstractMessageReciver#parse(java.lang.String)
	 */
	@Override
	public ResponseMessage parse(String jsonStr) throws MessageFormateException {
		ResponseMessage responseMessage = new ResponseMessage(jsonStr);
		return responseMessage;
	}

	/* (non-Javadoc)
	  com.cslc.framework.messaging.reciver.AbstractMessageReciver#recive(com.cslc.framework.messaging.model.ResponseMessage)
	 */
	@Override
	public void recive(ResponseMessage responseMsg) {
		msgDead.info("{}-{}", responseMsg.getHeader().getSequenceId(),responseMsg.getJsonStr());
	}

}
