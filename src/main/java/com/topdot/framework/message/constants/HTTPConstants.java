/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.constants.HTTPConstants.java
 * 所含类: HTTPConstants.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日6       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.constants;

/**
 * <p>HTTP协议相关属性常量</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class HTTPConstants {

	/**HTTP Body数据长度标示*/
	public final static String CONTENT_LENGTH_ATTR = "CONTENT-LENGTH";
	/**HTTP Body数据压缩标示*/
	public final static String COMPRESS_ATTR = "GZIP";

	// 回车/换行
	public final static byte CRLF_13 = (byte) 13; // ´/r´
	public final static byte CRLF_10 = (byte) 10; // ´/n´
}
