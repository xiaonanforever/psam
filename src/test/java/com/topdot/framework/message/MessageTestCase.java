/**
 * ============================================================
 * 版权： 中体彩 版权所有 (c) 2010 - 2014
 * 文件：cn.com.cslc.framework.message.MessageTestCase.java
 * 所含类: MessageTestCase.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2013-5-20 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message;

import java.util.Map;

import junit.framework.TestCase;

import com.topdot.framework.message.MessageServiceTemplate;
import com.topdot.framework.message.config.MessageDefination;
import com.topdot.framework.message.mont.ServiceEventQueue;
import com.topdot.psam.client.ClusterMessageService;
import com.topdot.psam.client.ClusterMessageServiceFactory;
import com.topdot.psam.server.server.ServerStartup;

/**
 * <p>MessageService测试基类</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: 中体彩</p>
 * 
 * @ClassName MessageTestCase
 * @author zhangyongxin
 * @version 1.0
 */

public class MessageTestCase extends TestCase {
	//protected static MessageServiceTemplate ms;
	protected static ClusterMessageService clusterService;
	//protected static SIEMock mock;
	protected static String terminalNo = "10000001";
	//protected static DefaultMessageHandlerMock msgHandlerMock = new DefaultMessageHandlerMock();
	protected final String MESSAGE_DEFINATION_KEY = "message.defination.heart.beat";
	protected static boolean unitTest = true;

	//private static final Logger LOGGER = LoggerFactory.getLogger(MessageTestCase.class);

	public MessageTestCase() {
		this("");
	}

	static {
		if (unitTest) {
			initServerMock();
		}
	}

	public static void initServerMock() {
		//开启服务
		new Thread() {
			public void run() {
				ServerStartup.start();
			}
		}.start();
		
		clusterService = ClusterMessageServiceFactory.startClusterService();
		Map<String, MessageServiceTemplate> msMap = clusterService.getMstMap();
		for (String key : msMap.keySet()) {
			MessageServiceTemplate ms = msMap.get(key);
			Map<String, MessageDefination> msgDefMap = ms.getConfig().getMessageConfig().getMessageDefByKeyMap();
			for (String msgKey : msgDefMap.keySet()) {
				MessageDefination msgDef = msgDefMap.get(msgKey);
				msgDef.getMessageType().setTimeout(6 * 1000);
			}
		}
		ServiceEventQueue.getInstance().setPollTime(250);
	}

	public MessageTestCase(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}
}
