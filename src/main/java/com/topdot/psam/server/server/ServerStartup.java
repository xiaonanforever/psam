/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.mgnt.server.ServerStartup.java
 * 所含类: ServerStartup.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月19日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.server.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>ServerStartup</p>
 * <p> Psam服务器启动 </p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName ServerMock
 * @author zhangyongxin
 * @version 1.0
 */

public class ServerStartup {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerStartup.class);

	public  static void start(){
		LOGGER.info("启动PSAM Server...");
		try {
			new PsamServer().startup();
		} catch (Exception e) {
			LOGGER.error("服务运行失败!", e);
			System.exit(1);
		}
	}
	/**
	 * <p>启动服务</p>
	 * 
	 * @return void
	 */
	public static void main(String[] args) {
		ServerStartup.start();
	}
}
