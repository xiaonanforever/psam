/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.cslc.framework.messaging.model.Message.java
 * 所含类: Message.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2012-4-26 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.protocol;

import java.util.Date;

/**
 * <p>Message</p>
 * 
 * <p> 消息根类,包含维度分为Header和Body,继承角度一般有请求(Request)和响应(Response) </p>
 * 
 * <p> Copyright: 版权所有 (c) 2010 - 2014 </p>
 * <p> Company: xxx </p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public abstract class Message<H extends MessageHeader, B extends MessageBody> {
	
	public final static char MESSAGE_SEPERATE = '\0';
	
	/** 消息头 */
	protected H header;
	
	/** 消息体 */
	protected B body;
	
	/** 是否包含信息体 */
	protected boolean includeBody;
	
	/**
	 * <p> 返回消息生成时间 </p>
	 * 
	 * @return
	 * @return Date
	 */
	public abstract Date getCreateDate();
	
	public H getHeader() {
		return header;
	}
	
	public void setHeader(H header) {
		this.header = header;
	}
	
	public B getBody() {
		return body;
	}
	
	public void setBody(B body) {
		this.body = body;
	}
	
}
