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

package com.topdot.framework.message.executor;

import com.topdot.framework.message.protocol.RequestMessage;
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

public abstract class AbstractMessageExecutor<Q extends RequestMessage<?, ?>,P extends ResponseMessage> implements MessageExecutor<Q,P> {
	
	/*public static PsamRspHeader mockRsp(PsamReqHeader reqHeader) {
		// MessageHeader数据
		PsamRspHeader rspHeader = new PsamRspHeader();
		rspHeader.setSequenceId(reqHeader.getSequenceId());
		rspHeader.setCommandType(reqHeader.getCommandType());
		rspHeader.setCommandId(reqHeader.getCommandId());
		rspHeader.setSystemId(reqHeader.getSystemId());
		rspHeader.setCreateDate(new Date());

		// TxMessageHeader
		rspHeader.setTerminalNo(reqHeader.getTerminalNo());
		rspHeader.setTerminalId(1);// TODO 需要管理
		rspHeader.setTerminalIp(reqHeader.getTerminalIp());
		rspHeader.setProvinceId(reqHeader.getProvinceId());

		rspHeader.setTraceLevel(1);
		rspHeader.setServerId("serverId");// TODO 需要在配置文件中配置
		rspHeader.setSessionId("SessionId");// TODO 需要进行分配

		rspHeader.setErrorCode(0);
		//rspHeader.setSn("SN");
		//rspHeader.setVersionNo("VersionNo");

		return rspHeader;
	}*/
}
