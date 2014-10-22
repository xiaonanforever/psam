/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.channel.Channel.java
 * 所含类: Channel.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日7       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.channel;

import com.topdot.framework.message.filter.FilterChain;

/**
 * <p>通道接口</p>
 * <p>接口：<br>
 * 1、获取通道运行状态
 * 2、设置通道运行状态</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public interface Channel {

	/**
	 * <p>获取通道是否运行状态</p>
	 * @return
	 * @return boolean
	 */
	boolean isRunning();

	/**
	 * <p>设置通道运行状态</p>
	 * @param running
	 * @return void
	 */
	void setRunning(boolean running);

	/**
	 * <p>获取通道FilterChain</p>
	 * @return
	 * @return FilterChain
	 */
	FilterChain getChain();

	/**
	 * <p>设置通道FilterChain</p>
	 * @param chain
	 * @return void
	 */
	void setChain(FilterChain chain);
}
