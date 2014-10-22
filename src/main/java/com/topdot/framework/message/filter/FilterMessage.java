/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.filter.FilterMessage.java
 * 所含类: FilterMessage.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日9       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.filter;

import java.util.HashMap;
import java.util.Map;

import com.topdot.framework.message.protocol.Message;

/**
 * <p>过滤消息封装</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class FilterMessage {
	
	/**消息目前在过滤链中的执行位置*/
	private int pos;

	/**原始头部数据(一般包含业务的请求头和请求体)*/
	private final String headMsg;
	
	/**原始体部数据*/
	private final String bodyMsg;

	/**过滤后数据*/
	private String filterMsg;
	
	/**过滤消息属性*/
	private final Map<String, Object> attrs = new HashMap<String, Object>();

	public FilterMessage(String headMsg, String bodyMsg) {
		this.headMsg = headMsg;
		this.bodyMsg = bodyMsg;
	}
	public int getPos() {
		return pos;
	}

	/**过滤消息沿着过滤列表往后走一步*/
	public int forward() {
		return pos++;
	}

	public String getMsg() {
		StringBuilder sb = new StringBuilder();
		sb.append(headMsg);
		if (bodyMsg != null) {
			sb.append(Message.MESSAGE_SEPERATE).append(bodyMsg);
		}
		return sb.toString();
	}

	public String getHeadMsg() {
		return headMsg;
	}

	public String getBodyMsg() {
		return bodyMsg;
	}

	public String getFilterMsg() {
		if (filterMsg == null) {
			return getMsg();
		}
		return filterMsg;
	}

	public void setFilterMsg(String filterMsg) {
		this.filterMsg = filterMsg;
	}
	
	public void addAttr(String key, Object value) {
		attrs.put(key, value);
	}

	public Object getAttrValue(String key) {
		return attrs.get(key);
	}
}
