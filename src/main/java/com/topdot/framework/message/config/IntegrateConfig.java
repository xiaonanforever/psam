/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.config.IntegrateConfig.java
 * 所含类: IntegrateConfig.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2013-3-1 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.config;

/**
 * <p>系统所有配置集成</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class IntegrateConfig {
	
	/** 消息定义配置 */
	private MessageConfig messageConfig;
	
	/** 服务层面共性配置 */
	private ServiceConfig serviceConfig;
	
	public IntegrateConfig() {
	}
	
	public MessageConfig getMessageConfig() {
		return messageConfig;
	}
	
	public void setMessageConfig(MessageConfig messageConfig) {
		this.messageConfig = messageConfig;
	}
	
	public ServiceConfig getServiceConfig() {
		return serviceConfig;
	}
	
	public void setServiceConfig(ServiceConfig serviceConfig) {
		this.serviceConfig = serviceConfig;
	}
	
}
