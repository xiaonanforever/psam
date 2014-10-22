/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.filter.Filter.java
 * 所含类: Filter.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日9       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.filter;

import com.topdot.framework.message.exception.MessageFilterException;

/**
 * <p>消息过滤器</p>
 * <p>用于对请求/应答消息过滤处理，一些消息通用处理逻辑可以通过Filter实现，如加解密</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public interface Filter {

	/**
	 * <p>执行消息过滤</p>
	 * @param msg - 待过滤消息
	 * @param chain - 过滤链
	 * @throws MessageFilterException
	 * @return void
	 */
	void doFilter(FilterMessage msg, FilterChain chain) throws MessageFilterException;

	/**
	 * <p>获取消息过滤器名称</p>
	 * @return
	 * @return String
	 */
	String getName();
}
