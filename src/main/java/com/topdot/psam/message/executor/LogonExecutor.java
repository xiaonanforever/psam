/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.executor.LogonExecutor.java
 * 所含类: LogonExecutor.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.message.executor;

import com.topdot.framework.message.executor.AbstractMessageExecutor;
import com.topdot.psam.message.protocol.PsamRspHeader;
import com.topdot.psam.message.protocol.request.LogonRequest;
import com.topdot.psam.message.protocol.request.LogonRequestBody;
import com.topdot.psam.message.protocol.response.LogonResponse;

/**
 * <p>LogonExecutor</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName LogonExecutor
 * @author zhangyongxin
 * @version 1.0
 */

public class LogonExecutor extends AbstractMessageExecutor<LogonRequest, LogonResponse> {

	@Override
	public LogonResponse execute(LogonRequest reqMsg) {
		LogonResponse rsp = new LogonResponse();
		
		PsamRspHeader rspHeader = mockRsp(reqMsg.getHeader());
		rsp.setHeader(rspHeader);
		
		LogonRequestBody body = reqMsg.getBody();
		rsp.setBody(body);
//		if (!body.getTerminalNo().contains("Pass") || !body.getTerminalPassword().equals("123456")) {
//			rspHeader.setErrorCode(505001);
//			rsp.getBody().setErrorMessage("用户密码输入错误!");
//		}
		return rsp;
	}

}
