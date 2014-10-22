/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.filter.FilterConfig.java
 * 所含类: FilterConfig.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日9       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>过滤器整体配置</p>
 * <p>类用途详细说明:</br>
 * 设置过滤器链的整体参数</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class FilterConfig {
	
	public static final String ATT_SECURITY_ALGORITHM = "ATT_SECURITY_ALGORITHM";
	public static final String ATT_SECURITY_KEY = "ATT_SECURITY_KEY";

	private final Map<String, Object> attrs = new HashMap<String, Object>();

	public FilterConfig() {
	}
	public void addAttr(String key, Object value) {
		attrs.put(key, value);
	}

	public Object getAttrValue(String key) {
		return attrs.get(key);
	}

}
