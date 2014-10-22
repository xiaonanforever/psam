/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.gimp.eit.message.HttpParser.java
 * 所含类: HttpParser.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.filter.impl;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.constants.HTTPConstants;
import com.topdot.framework.message.constants.MsgConstants;
import com.topdot.framework.message.exception.MessageFilterException;
import com.topdot.framework.message.filter.Filter;
import com.topdot.framework.message.filter.FilterChain;
import com.topdot.framework.message.filter.FilterMessage;
import com.topdot.framework.util.Compress;

/**
 * <p>以HTTP包头封装数据Filter</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class HttpOutFilterImpl implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpOutFilterImpl.class);
	
	public HttpOutFilterImpl() {
	}
	
	@Override
	public void doFilter(FilterMessage msg, FilterChain chain) throws MessageFilterException {
		int jsonLength = 0;
		int contentLength = 0;
		try {
			jsonLength = msg.getHeadMsg().getBytes("UTF-8").length;
			contentLength = msg.getFilterMsg().getBytes("UTF-8").length;
		} catch (UnsupportedEncodingException e) {
			throw new MessageFilterException(e);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("POST CAS HTTP/1.1\r\n");
		sb.append("User-Agent: MPX\r\n");
		
		String	localSA = (String) (chain.getConfig().getAttrValue(MsgConstants.LOCALAUTH));
		sb.append("Host:").append(localSA).append("\r\n");
		sb.append(HTTPConstants.COMPRESS_ATTR).append(":");
		if (MsgConstants.YES.equals(msg.getAttrValue(MsgConstants.NEED_COMPRESS))) {
			sb.append(Compress.COMPRESS);
		} else {
			sb.append(Compress.SOURCE);
		}
		sb.append("\r\n");
		sb.append("Content-Length:").append(contentLength).append("\r\n");
		sb.append("JSON-Length:").append(jsonLength).append("\r\n");
		sb.append("Connection: Keep-Alive\r\n\r\n");
		sb.append(msg.getFilterMsg());
		String sbStr = sb.toString();
		LOGGER.info("HTTP封装JSON数据:{}", sbStr);
		msg.setFilterMsg(sbStr);
		chain.doFilter(msg);
	}
	
	@Override
	public String getName() {
		return "HttpOutFilterImpl";
	}
}
