/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.mont.ChannelDispatch.java
 * 所含类: ChannelDispatch.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月10日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.mont;

import java.io.IOException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.channel.impl.IOResponseChannelImpl;
import com.topdot.framework.message.filter.FilterChain;
import com.topdot.framework.message.filter.impl.FilterChainImpl;
import com.topdot.framework.message.filter.impl.UnCompressFilterImpl;
import com.topdot.framework.message.impl.SocketServiceImpl;

/**
 * <p>NIO Channel管理和派发</p>
 * <p>需要解决的问题:
 * 1) 一个受理线程需要保持稳定和健壮
 * 2) N个处理线程
 * 3) 链路断了之后如何加入
 * 4) 资源清理
 * </p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName ChannelDispatch
 * @author zhangyongxin
 * @version 1.0
 */

public class ChannelDispatchService implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelDispatchService.class);
	private final static int channelNum = 6;// TODO替换成配置里的内容
	private static ExecutorService es;
	private static Selector selector;
	private static ChannelDispatchService serivce;

	private static Map<SelectionKey, MessageServiceChannel> selectKeyMap = new HashMap<SelectionKey, MessageServiceChannel>();

	private static Queue<MessageServiceChannel> clientChannelQueue = new ConcurrentLinkedQueue<MessageServiceChannel>();

	//private boolean useEncrypt = false;
	//private String securityAlgorithm;
	//private String securityKey;

	private ChannelDispatchService() {
	}

	public static void stopRun() {
		LOGGER.info("停止Socket管理...");
		serivce = null;
		try {
			selector.close();
		} catch (IOException e) {
			LOGGER.error("关闭Selector异常!",e);
		}
		es.shutdownNow();
	}

	public void run() {
		es = Executors.newFixedThreadPool(channelNum, new MsgThreadFactory("I/O-RSP"));
		// 开启一批Response Channel
		for (int i = 0; i < channelNum; i++) {
			LOGGER.info("构建响应通道...");
			IOResponseChannelImpl rspChannel = new IOResponseChannelImpl();
			LOGGER.info("设置响应通道的FilterChain...");
			FilterChain rspChain = new FilterChainImpl();
			// TODO权益之计，只有采用了加密算法，才加入
//			if (useEncrypt) {
//				rspChain.getConfig().addAttr(FilterConfig.ATT_SECURITY_ALGORITHM, securityAlgorithm);
//				rspChain.getConfig().addAttr(FilterConfig.ATT_SECURITY_KEY, securityKey);
//				rspChain.addFilter(new DecryptFilterImpl(rspChain));
//			}
			rspChain.addFilter(new UnCompressFilterImpl());
			rspChannel.setChain(rspChain);
			es.execute(rspChannel);
		}
		while (true) {
			try {
				Set<SelectionKey> keys;
				LOGGER.info("等待选择Ready Channel..");
				try {
					selector.select();
					keys = selector.selectedKeys();
				} catch (ClosedSelectorException e) {
					LOGGER.info("Selector关闭!");
					return;
				}
				LOGGER.info("选择Ready Channel数目:<{}>", keys.size());
				MessageServiceChannel msc = clientChannelQueue.poll();
				if (msc != null) {
					SocketChannel clntChan = msc.getSocketChannel();
					SelectionKey key = clntChan.register(selector, SelectionKey.OP_READ);
					msc.setSelectKey(key);
					selectKeyMap.put(key, msc);
					LOGGER.info("<{}>注册成功!", clntChan);
				}
				if (keys.size() == 0) {// 可能是唤醒，这这个时刻注册一下新的client
					continue;
				}
				Iterator<SelectionKey> iterator = keys.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					iterator.remove();
					SocketChannel client = (SocketChannel) key.channel();
					LOGGER.info("客户端<{}>有数据进入...", client);
					if (!client.isOpen()) {
						key.cancel();
						continue;
					}
					if (key.isReadable() && client.isOpen()) {
						key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
						ActiveSocketChannel.offer(selectKeyMap.get(key));
					}
					if (key.isValid() && key.isWritable()) {
					}
				}// while
			} catch (IOException e) {
				e.printStackTrace();
			}
		}// while
	}

	/**
	 * <p>注册关注服务</p>
	 * 
	 * @return void
	 * @see
	 */
	public static void register(SocketServiceImpl socketServiceImpl) {
		synchronized (ChannelDispatchService.class) {
			try {
				if (serivce == null) {
					selector = Selector.open();
					serivce = new ChannelDispatchService();
//					ServiceConfig sc = socketServiceImpl.getConfig().getServiceConfig();
//					serivce.securityAlgorithm = sc.getSecurityAlgorithm();
//					if (serivce.securityAlgorithm != null && !"".equals(serivce.securityAlgorithm)) {
//						serivce.useEncrypt = true;
//						serivce.securityKey = sc.getSecurityKey();
//					}
					Thread t = new Thread(serivce, ChannelDispatchService.class.getSimpleName());
					t.start();
				} else {
					if (!selector.isOpen()) {
						selector = null;
						selector = Selector.open();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		MessageServiceChannel msc = new MessageServiceChannel(socketServiceImpl, socketServiceImpl.getClntChan());
		selector.wakeup();
		clientChannelQueue.offer(msc);
	}

	public static void activate(SelectionKey selectKey) {
		selectKey.interestOps(selectKey.interestOps() | SelectionKey.OP_READ);
		selector.wakeup();
	}
}
