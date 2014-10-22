/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.Session.java
 * 所含类: Session.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日0       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message;

/**
 * <p>服务会话</p>
 * <p>类用途详细说明:</br>
 * 用户Reciver接收异步信息时，通过terminalNo获得要已经注册的MessageService</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class Session {

	/**会话ID*/
	//private String sessionId;

	/**会话关联的终端No*/
	//private String terminalNo;

	/***/
	//private String connectionStr;

	/**会话绑定的消息服务*/
	private MessageService messageService;

	public Session() {
	}

//	public String getSessionId() {
//		return sessionId;
//	}
//
//	public void setSessionId(String sessionId) {
//		this.sessionId = sessionId;
//	}

//	public String getTerminalNo() {
//		return terminalNo;
//	}

//	public void setTerminalNo(String terminalNo) {
//		this.terminalNo = terminalNo;
//	}

	

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
}
