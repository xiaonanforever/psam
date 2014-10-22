/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.executor.LogoutExecutor.java
 * 所含类: LogoutExecutor.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.message.executor;

import com.topdot.framework.message.executor.AbstractMessageExecutor;
import com.topdot.psam.message.protocol.PsamReqHeader;
import com.topdot.psam.message.protocol.PsamRspHeader;
import com.topdot.psam.message.protocol.request.LogoutRequest;
import com.topdot.psam.message.protocol.response.LogoutResponse;

/**
 * <p>LogoutExecutor</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName LogoutExecutor
 * @author zhangyongxin
 * @version 1.0
 */

public class LogoutExecutor extends AbstractMessageExecutor<LogoutRequest,LogoutResponse> {

	@Override
	public LogoutResponse execute(LogoutRequest reqMsg) {
		LogoutResponse rsp = new LogoutResponse();
		PsamReqHeader header = reqMsg.getHeader();
		PsamRspHeader rspHeader = mockRsp(header);
		rsp.setHeader(rspHeader);
		rspHeader.setErrorCode(0);
		return rsp;
	}
}
