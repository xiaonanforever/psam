/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.mgnt.filter.impl.DecryptFilterImpl.java
 * 所含类: DecryptFilterImpl.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.server.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.exception.MessageFilterException;
import com.topdot.framework.message.filter.Filter;
import com.topdot.framework.message.filter.FilterChain;
import com.topdot.framework.message.filter.FilterConfig;
import com.topdot.framework.message.filter.FilterMessage;
import com.topdot.framework.message.security.SecurityCipher;

/**
 * <p>DecryptFilterImpl</p>
 * <p>消息加解密</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class DecryptFilterImpl implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(DecryptFilterImpl.class);
	private SecurityCipher sc;

	public DecryptFilterImpl(FilterChain chain) {
		String securityKey = (String) chain.getConfig().getAttrValue(FilterConfig.ATT_SECURITY_KEY);
		String securityAlgorithm = (String) chain.getConfig().getAttrValue(FilterConfig.ATT_SECURITY_ALGORITHM);
		sc = SecurityCipher.getInstace(securityKey, securityAlgorithm);
	}

	@Override
	public void doFilter(FilterMessage msg, FilterChain chain) throws MessageFilterException {
		LOGGER.info("解密...");
		msg.setFilterMsg(sc.decrypt(msg.getFilterMsg()));
		chain.doFilter(msg);
	}

	@Override
	public String getName() {
		return "DecryptFilter";
	}
}
