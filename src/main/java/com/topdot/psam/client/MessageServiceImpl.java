/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.client.PsamMessageServiceImpl.java
 * 所含类: PsamMessageServiceImpl.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日8 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.filter.FilterChain;
import com.topdot.framework.message.filter.impl.CompressFilterImpl;
import com.topdot.framework.message.filter.impl.HttpOutFilterImpl;
import com.topdot.framework.message.impl.SocketServiceImpl;
import com.topdot.framework.message.protocol.MessageBody;
import com.topdot.framework.message.protocol.MessageHeader;
import com.topdot.framework.message.protocol.RequestMessage;
import com.topdot.framework.message.protocol.ResponseMessage;
import com.topdot.psam.message.protocol.response.LogonResponse;

/**
 * <p>Psam业务服务实现</p>
 * <p>主要实现登陆之后设置TerminalId</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class MessageServiceImpl extends SocketServiceImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

	public MessageServiceImpl() {
	}

	/***
	 * <p>初始化模块配置</p>
	 */
	protected void init() {
		super.init();
		FilterChain chain = requestChannel.getChain();
		chain.clear();
		LOGGER.info("配置加密算法...");
		//ServiceConfig sc = this.config.getServiceConfig();
		//chain.getConfig().addAttr(FilterConfig.ATT_SECURITY_ALGORITHM, sc.getSecurityAlgorithm());
		//chain.getConfig().addAttr(FilterConfig.ATT_SECURITY_KEY, sc.getSecurityKey());
		chain.addFilter(new CompressFilterImpl());
		//chain.addFilter(new EncryptFilterImpl(chain));
		chain.addFilter(new HttpOutFilterImpl());
	}

	/**
	 * 链路已经创建，这里不负责链路
	 */
	@Override
	public ResponseMessage logon(RequestMessage<? extends MessageHeader, ? extends MessageBody> reqMsg) {
		ResponseMessage rspMsg = super.logon(reqMsg);
		if (rspMsg.success()) {
			Object terminalId = config.getServiceConfig().getCommonParameters().get("TerminalId");
			if (terminalId == null || terminalId.equals("")) {
				// 登陆成功之后需要设置TerminalId
				config.getServiceConfig().getCommonParameters().put("TerminalId",
						((LogonResponse) rspMsg).getHeader().getTerminalId());
			}
		}
		return rspMsg;
	}

	@Override
	public String getVersion() {
		return "0.3.1";
	}
}
