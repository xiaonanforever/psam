/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.cslc.framework.messaging.callback.MessageAsynReciver.java
 * 所含类: MessageAsynReciver.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2012-1-31       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.executor;

import com.topdot.framework.message.protocol.RequestMessage;
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

public interface MessageExecutor<Q extends RequestMessage<?, ?>,P extends ResponseMessage> {

	/**
	 * <p>执行请求，返回响应 </p>
	 * @param responseMsg
	 */
	P execute(Q reqMsg);
}
