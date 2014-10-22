/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.filter.impl.FilterChainImpl.java
 * 所含类: FilterChainImpl.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日9 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.filter.impl;

import java.util.ArrayList;
import java.util.List;

import com.topdot.framework.message.exception.MessageFilterException;
import com.topdot.framework.message.filter.Filter;
import com.topdot.framework.message.filter.FilterChain;
import com.topdot.framework.message.filter.FilterConfig;
import com.topdot.framework.message.filter.FilterMessage;

/**
 * <p>消息过滤列表实现</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class FilterChainImpl implements FilterChain {
	//private static final Logger LOGGER = LoggerFactory.getLogger(FilterChainImpl.class);
	/** 已注册的过滤器 */
	private final List<Filter> filters = new ArrayList<Filter>();
	
	private FilterConfig config = new FilterConfig();
	
	public FilterChainImpl() {
	}
	
	@Override
	public void doFilter(FilterMessage msg) throws MessageFilterException {
		if (msg.getPos() < filters.size()) {
			filters.get(msg.forward()).doFilter(msg, this);
		}
	}
	
	public void addFilter(Filter filter) {
		filters.add(filter);
	}
	
	public FilterConfig getConfig() {
		return config;
	}

	@Override
	public void removeFilter(Filter filter) {
		filters.remove(filter);
	}

	@Override
	public void clear() {
		filters.clear();
	}
}
