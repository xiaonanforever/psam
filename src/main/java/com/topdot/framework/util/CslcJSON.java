/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.util.CslcJSON.java
 * 所含类: CslcJSON.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日5       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

/**
 * <p>Cslc特有JSON实现</p>
 * <p>因SIE、CTP等处理JSON Double数据类型限制规则</br>
 * 1、必须含小数点</br>
 * 2、不能是科学记数</br>
 * 需要MPX发送Double数据时对Double数据类型进行特别处理
 * 实现过程
 * 1、扩展JSONObject
 * 2、因static方法不支持Override，原本计划只覆盖numberToString即可，但现在需要关联把toString、numberToString复制一份，然后
 * 3、重写numberToString方法
 * </p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class CslcJSON extends JSONObject {

	public CslcJSON() {
	}

	/**
	 * <p>重写numberToString</p>
	 * @param number
	 * @return
	 * @throws JSONException
	 * @return String
	 */
	public static String numberToString(Number number) throws JSONException {
		if (number == null) {
			throw new JSONException("Null pointer");
		}
		//testValidity(number);
		if (number instanceof Double) {
			BigDecimal b = new BigDecimal(number.doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
			String string = b.toString();
			return string;
		} else {
			return number.toString();
		}

	}

	// 直接复制
	@SuppressWarnings("rawtypes")
	public static String valueToString(Object value) throws JSONException {
		if (value == null || value.equals(null)) {
			return "null";
		}
		if (value instanceof JSONString) {
			Object object;
			try {
				object = ((JSONString) value).toJSONString();
			} catch (Exception e) {
				throw new JSONException(e);
			}
			if (object instanceof String) {
				return (String) object;
			}
			throw new JSONException("Bad value from toJSONString: " + object);
		}
		if (value instanceof Number) {
			return numberToString((Number) value);
		}
		if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
			return value.toString();
		}
		if (value instanceof Map) {
			return new JSONObject((Map) value).toString();
		}
		if (value instanceof Collection) {
			return new JSONArray((Collection) value).toString();
		}
		if (value.getClass().isArray()) {
			return new JSONArray(value).toString();
		}
		return quote(value.toString());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String toString() {
		try {
			Iterator keys = this.keys();
			StringBuffer sb = new StringBuffer("{");

			while (keys.hasNext()) {
				if (sb.length() > 1) {
					sb.append(',');
				}
				Object o = keys.next();
				sb.append(quote(o.toString()));
				sb.append(':');
				// sb.append(valueToString(this.map.get(o)));//替换成下面
				sb.append(valueToString(this.opt((String) o)));
			}
			sb.append('}');
			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}
}
