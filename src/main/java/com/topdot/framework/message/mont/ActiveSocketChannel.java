/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.mont.ActiveChannel.java
 * 所含类: ActiveChannel.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月10日       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.mont;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>类名</p>
 *
 * <p>类用途详细说明</p>
 *
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName ActiveChannel
 * @author zhangyongxin
 * @version 1.0
 * 
 */

public class ActiveSocketChannel {

	private final static BlockingQueue<MessageServiceChannel> q = new LinkedBlockingQueue<MessageServiceChannel>();
	
	public static void offer(MessageServiceChannel sc) {
		q.offer(sc);
	}
	
	public static MessageServiceChannel take() throws InterruptedException {
		return q.take();
	}
	
	public static int size() {
		return q.size();
	}

}
