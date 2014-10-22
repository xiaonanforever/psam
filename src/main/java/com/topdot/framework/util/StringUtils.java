/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.cslc.framework.uitl.StringUtils.java
 * 所含类: StringUtils.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2012-2-17       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.util;


/**
 * <p> 类名</p>
 * <p> 类用途详细说明 </p>
 * <p> Copyright: 版权所有 (c) 2010 - 2014 </p>
 * <p> Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public final class StringUtils {
	// private static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);

	/**
	 * Check if an Object is empty
	 * 
	 * @param obj Object
	 * @return boolean
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof String) {
			return ((String) obj).trim().length() == 0;
		} else {
			return false;
		}
	}

	public static String upperCase1stLetter(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String array2String(String[] array) {
		StringBuffer strBuff = new StringBuffer();
		if ((array != null) && (array.length > 0)) {
			for (int i = 0; i < array.length; i++) {
				strBuff.append(array[i]);
			}
		}
		return strBuff.toString();
	}

}
