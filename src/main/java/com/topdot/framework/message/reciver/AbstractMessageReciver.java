/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.cslc.framework.messaging.callback.AbstractMessageReciver.java
 * 所含类: AbstractMessageReciver.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2012-4-6 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.reciver;

import com.topdot.framework.message.Session;
import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.protocol.ResponseMessage;

/**
 * <p>异步报告消息抽象类</p>
 * 
 * <p>类用途详细说明</p>
 * 
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public abstract class AbstractMessageReciver<R extends ResponseMessage> implements MessageReciver<R> {
	
	/** Reciver所属Session */
	private Session session;
	
	public AbstractMessageReciver() {
	}
	
	/*
	 * (non-Javadoc) com.cslc.framework.messaging.callback.MessageReciver#recive(java.lang.String)
	 */
	@Override
	public void recive(String jsonStr) throws MessageFormateException {
		recive(parse(jsonStr));
	}
	
	public Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}
	
}
