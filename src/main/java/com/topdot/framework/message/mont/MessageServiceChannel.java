/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.mont.MessageServiceChannel.java
 * 所含类: MessageServiceChannel.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月10日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.mont;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.topdot.framework.message.MessageServiceTemplate;

/**
 * <p>类名</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName MessageServiceChannel
 * @author zhangyongxin
 * @version 1.0
 */

public class MessageServiceChannel {

	private MessageServiceTemplate mst;

	private SocketChannel socketChannel;
	
	private SelectionKey selectKey;

	public MessageServiceChannel() {

	}

	public MessageServiceChannel(MessageServiceTemplate mst, SocketChannel socketChannel) {
		this.mst = mst;
		this.socketChannel = socketChannel;
	}

	public MessageServiceTemplate getMst() {
		return mst;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSelectKey(SelectionKey key) {
		this.selectKey = key;
	}

	public SelectionKey getSelectKey() {
		return selectKey;
	}

}
