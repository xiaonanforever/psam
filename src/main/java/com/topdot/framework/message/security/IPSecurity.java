/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2012
 * 文件：com.topdot.framework.message.security.IPSecurity.java
 * 所含类: IPSecurity.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月21日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.security;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>IPSecurity</p>
 * <p>IP鉴权</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2012</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName IPSecurity
 * @author zhangyongxin
 * @version 1.0
 */

public class IPSecurity {

	private static final List<Pattern> ipPatternList = new ArrayList<Pattern>();

	private IPSecurity() {
	}

	public static void init(String patterns) {
		String[] pa = patterns.split("/");
		for(String p:pa) {
			ipPatternList.add(Pattern.compile(p));
		}
	}

	public static boolean check(String ip) {
		for(Pattern p:ipPatternList) {
			Matcher m = p.matcher(ip);
			if(m.matches()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>方法描述</p>
	 * 
	 * @return void
	 * @see
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
