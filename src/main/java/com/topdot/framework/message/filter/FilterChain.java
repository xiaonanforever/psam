/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.filter.FilterChain.java
 * 所含类: FilterChain.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日9       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.filter;

import com.topdot.framework.message.exception.MessageFilterException;

/**
 * <p>消息过滤器链</p>
 * <p>接口:<br>
 * 1、执行消息过滤<br>
 * 2、增加消息过滤器<br>
 * 3、获取/设置配置</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public interface FilterChain {

	/**
	 * <p>执行消息过滤</p>
	 * @param msg
	 * @throws MessageFilterException
	 */
	void doFilter(FilterMessage msg) throws MessageFilterException;

	/**
	 * <p>增加消息过滤器</p>
	 * @param filter
	 */
	void addFilter(Filter filter);
	
	void removeFilter(Filter filter);
	
	void clear();

	FilterConfig getConfig();

}
