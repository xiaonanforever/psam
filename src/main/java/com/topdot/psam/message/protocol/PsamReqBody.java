/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.protocol.TxReqMessageBody.java
 * 所含类: TxReqMessageBody.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月19日       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.message.protocol;

import com.topdot.framework.message.exception.MessageFormateException;
import com.topdot.framework.message.protocol.MessageBody;

/**
 * <p>Eit请求消息公共体</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class PsamReqBody extends MessageBody {

	public PsamReqBody() {
	}

	/**
	 * @param jsonStr
	 * @throws MessageFormateException 
	 */
	public PsamReqBody(String jsonStr) throws MessageFormateException {
		super(jsonStr);
	}
}
