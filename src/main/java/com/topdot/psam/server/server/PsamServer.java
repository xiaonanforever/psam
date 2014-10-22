/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.mgnt.server.PsamServer.java
 * 所含类: PsamServer.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月19日  zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.server.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.channel.RequestMessageQueue;
import com.topdot.framework.message.config.MessageConfig;
import com.topdot.framework.message.config.ServiceConfig;
import com.topdot.framework.message.constants.MsgConstants;
import com.topdot.framework.message.filter.FilterChain;
import com.topdot.framework.message.filter.impl.CompressFilterImpl;
import com.topdot.framework.message.filter.impl.FilterChainImpl;
import com.topdot.framework.message.filter.impl.HttpInFilterImpl;
import com.topdot.framework.message.filter.impl.HttpOutFilterImpl;
import com.topdot.framework.message.filter.impl.UnCompressFilterImpl;
import com.topdot.framework.message.mont.MessageNoTimeLogger;
import com.topdot.framework.message.mont.MsgThreadFactory;
import com.topdot.framework.message.security.IPSecurity;

/**
 * <p>PsamServer</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName ServerMock
 * @author zhangyongxin
 * @version 1.0
 */

public class PsamServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(PsamServer.class);

	private static final String messageConfigFile = "/message.server.properties";

	private static final String serviceConfigFile = "/service.server.properties";

	private ServerSocketChannel serverChannel;
	private static Selector selector;
	private int listeningPort;

	public PsamServer() {
	}

	private void init() throws IOException {
		LOGGER.info("初始化...");
		serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);

		LOGGER.info("解析配置文件...");
		MessageConfig mc = MessageConfig.parserConfig(MessageConfig.class.getResourceAsStream(messageConfigFile));
		ServiceConfig sc = ServiceConfig.parserConfig(ServiceConfig.class.getResourceAsStream(serviceConfigFile));
		MessageNoTimeLogger.setServiceConfig(sc);
		listeningPort = sc.getServerPort();
		
		LOGGER.info("构建IP鉴权信息...");
		IPSecurity.init(sc.getSecurityIpAuth());
		
		LOGGER.info("构建请求队列...");
		RequestMessageQueue reqMsgQueue = new RequestMessageQueue();
		reqMsgQueue.setMaxSize(sc.getReqMaxSize());

		LOGGER.info("构建消息处理线程池...");
		ExecutorService esReqWorkPool = Executors.newFixedThreadPool(sc.getWorkerThread(), new MsgThreadFactory(
				"Req-Worker"));
		for (int i = 0; i < sc.getWorkerThread(); i++) {
			ReqWorker reqWorker = new ReqWorker(reqMsgQueue, mc.getMessageDefByIdMap());
			esReqWorkPool.execute(reqWorker);
		}

		LOGGER.info("构建消息受理线程池...");
		ExecutorService esReqReciverPool = Executors.newFixedThreadPool(sc.getClientWorkerNumber(),
				new MsgThreadFactory("Req-Reciver"));
		for (int i = 0; i < sc.getClientWorkerNumber(); i++) {
			LOGGER.info("设置请求受理Filter...");
			FilterChain reqChain = new FilterChainImpl();
			//reqChain.getConfig().addAttr(FilterConfig.ATT_SECURITY_ALGORITHM, sc.getSecurityAlgorithm());
			//reqChain.getConfig().addAttr(FilterConfig.ATT_SECURITY_KEY, sc.getSecurityKey());

			reqChain.addFilter(new HttpInFilterImpl());
			//reqChain.addFilter(new DecryptFilterImpl(reqChain));
			reqChain.addFilter(new UnCompressFilterImpl());

			LOGGER.info("设置请求返回Filter...");
			FilterChain rspChain = new FilterChainImpl();
			//rspChain.getConfig().addAttr(FilterConfig.ATT_SECURITY_ALGORITHM, sc.getSecurityAlgorithm());
			//rspChain.getConfig().addAttr(FilterConfig.ATT_SECURITY_KEY, sc.getSecurityKey());
			rspChain.getConfig().addAttr(MsgConstants.SERVICE_CONFIG, sc);

			rspChain.addFilter(new CompressFilterImpl());
			//rspChain.addFilter(new EncryptFilterImpl(rspChain));
			rspChain.addFilter(new HttpOutFilterImpl());

			ReqReciver reqReciver = new ReqReciver(reqChain, rspChain, reqMsgQueue, mc.getMessageDefByIdMap());
			esReqReciverPool.execute(reqReciver);
		}
		
	}

	public void startup() throws IOException {
		init();
		ServerSocket serverSocket = serverChannel.socket();
		serverSocket.setReceiveBufferSize(1024 * 1024);
		SocketAddress sa = new InetSocketAddress(listeningPort);
		serverSocket.bind(sa);
		selector = Selector.open();
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		run();
		LOGGER.info("启动完成");
	}

	private void run() {
		while (true) {
			try {
				selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					iterator.remove();
					if (key.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						final SocketChannel client = server.accept();
						LOGGER.info("客户端<{}>连接,执行IP鉴权...", client.socket());
						Socket cls = client.socket();
						String ip = ((InetSocketAddress)cls.getRemoteSocketAddress()).getAddress().getHostAddress();
						if(IPSecurity.check(ip)) {
							LOGGER.info("客户端<{}>IP鉴权通过.", client.socket());
						}else {
							LOGGER.warn("客户端<{}>IP鉴权失败.", client.socket());
							client.close();
							continue;
						}
						try {
							client.configureBlocking(false);
							client.register(selector, SelectionKey.OP_READ);
							cls.setTcpNoDelay(true);
						} catch (IOException e) {
							LOGGER.error("客户端<" + client.socket() + ">接入异常!", e);
						}
					} else {
						if (key.isReadable()) {
							SocketChannel client = (SocketChannel) key.channel();
							LOGGER.info("客户端<{}>有数据进入...", client);
							if (!client.isOpen()) {
								key.cancel();
								continue;
							}
							key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
							ActiveClientChannel.offer(key);
						}
						if (key.isValid() && key.isWritable()) {
						}
					}
				}// while
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}// while
	}

	public static void activate(SelectionKey selectKey) {
		selectKey.interestOps(selectKey.interestOps() | SelectionKey.OP_READ);
		selector.wakeup();
	}

	public void shutdown() {

	}

	public void shutdownNow() {

	}
}
