/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.filter.impl.CompressFilterImpl.java
 * 所含类: CompressFilterImpl.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日9 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.filter.impl;

import java.io.IOException;

import com.topdot.framework.message.config.ServiceConfig;
import com.topdot.framework.message.constants.MsgConstants;
import com.topdot.framework.message.exception.MessageFilterException;
import com.topdot.framework.message.filter.Filter;
import com.topdot.framework.message.filter.FilterChain;
import com.topdot.framework.message.filter.FilterMessage;
import com.topdot.framework.util.Compress;

/**
 * <p>消息加解压过滤</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class CompressFilterImpl implements Filter {
	public CompressFilterImpl() {
	}
	@Override
	public void doFilter(FilterMessage msg, FilterChain chain) throws MessageFilterException {	
		try {
			ServiceConfig config = (ServiceConfig) chain.getConfig().getAttrValue(MsgConstants.SERVICE_CONFIG);
			if (msg.getMsg().getBytes().length >= config.getCompressSizeLimit()) {
				msg.setFilterMsg(Compress.gzip(msg.getFilterMsg()));
				msg.addAttr(MsgConstants.NEED_COMPRESS,MsgConstants.YES);
			}
		} catch (IOException e) {
			throw new MessageFilterException("消息[" + msg.getFilterMsg() + "]加压异常", e);
		}
		chain.doFilter(msg);
	}
	
	@Override
	public String getName() {
		return "CompressFilter";
	}
}
