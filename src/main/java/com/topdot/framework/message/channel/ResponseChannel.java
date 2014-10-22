/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.gimp.eit.ReciveChannel.java
 * 所含类: ReciveChannel.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月19日       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.channel;

import com.topdot.framework.message.exception.MessageFilterException;
import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.filter.FilterMessage;

/**
 * <p>响应通道接口</p>
 * <p>接口:<br>
 * 1、接受JsonStr格式消息
 * </p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public interface ResponseChannel extends Channel {

	/**
	 * <p>接收消息</p>
	 * @param jsonStr 消息的JSON字符串
	 * @return void
	 * @throws MessageFilterException 
	 */
	void onMessage(String jsonStr) throws MessageFormateException, MessageFilterException;

	void onMessage(FilterMessage msg) throws MessageFormateException, MessageFilterException;
}
