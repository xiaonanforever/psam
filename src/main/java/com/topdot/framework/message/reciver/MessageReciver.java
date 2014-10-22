/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.cslc.framework.messaging.callback.MessageAsynReciver.java
 * 所含类: MessageAsynReciver.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2012-1-31       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.reciver;

import com.topdot.framework.message.Session;
import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.protocol.ResponseMessage;

/**
 * <p>异步报告消息接收接口 </p>
 * 
 * <p>  </p>
 * 
 * <p> Copyright: 版权所有 (c) 2010 - 2014 </p>
 * <p> Company: xxx </p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public interface MessageReciver<R extends ResponseMessage> {

	/** 
	 * <p>接收异步消息(字符串格式,消息头为JSON格式,而消息体有的是JSON格式,有的是字符串HTTP协议) </p>
	 * @param jsonStr
	 */
	void recive(String jsonStr) throws MessageFormateException;

	/**
	 * <p>接收异步消息(转换后的对象格式) </p>
	 * @param responseMsg
	 */
	void recive(R responseMsg);

	/**
	 * <p> 解析字符串格式生产对象格式 </p>
	 * @param jsonStr
	 * @return
	 * @throws MessageFormateException
	 * @return ResponseMessage
	 */
	R parse(String jsonStr) throws MessageFormateException;

	/**
	 * <p>设置所属Session</p>
	 * @param session
	 * @return void
	 */
	void setSession(Session session);
}
