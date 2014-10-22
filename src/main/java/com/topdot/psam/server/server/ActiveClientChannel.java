/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.mgnt.server.ActiveClientChannel.java
 * 所含类: ActiveClientChannel.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月19日        zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.server.server;

import java.nio.channels.SelectionKey;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>com.topdot.psam.mgnt.server</p>
 *
 * <p>活跃（有数据进入）ClientChannel</p>
 *
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName ActiveClientChannel
 * @author zhangyongxin
 * @version 1.0
 * 
 */

public class ActiveClientChannel {

	private final static BlockingQueue<SelectionKey> q = new LinkedBlockingQueue<SelectionKey>();
	
	public static void offer(SelectionKey sc) {
		q.offer(sc);
	}
	
	public static SelectionKey take() throws InterruptedException {
		return q.take();
	}
	
	public static int size() {
		return q.size();
	}

}
