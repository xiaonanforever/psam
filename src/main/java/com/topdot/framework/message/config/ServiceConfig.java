/**
 * ============================================================ 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.gimp.eit.config.EitServiceDefaultConfig.java 所含类: EitServiceDefaultConfig.java 修改记录： 日期 作者 内容
 * ============================================================= 2014年1月18日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.exception.ConfigException;
import com.topdot.framework.util.StringUtils;

/**
 * <p>系统全局参数配置 </p>
 * <p>类用途详细说明 </p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx </p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class ServiceConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfig.class);
	/** 报头公共参数key前缀 */
	private static final String COMMON_PARAMETER_PRE = "message.common.parameter.";

	/** 全局配置Key */
	public enum ConfigKey {
		/* 网络参数 */
		SERVER_IP("message.server.ip", "远程服务IP", ""), //
		SERVER_PORT("message.server.port", "远程服务端口", ""), //
		SERVER_CONNECTION_TIMEOUT("message.server.connection.timeout", "远程服务连接超时（单位:ms）", "20000"), //
		SERVER_READ_TIMEOUT("message.server.read.timeout", "远程服务读超时（单位:ms）", "5000"), //
		SERVER_LOCAL_INTERFACE("message.server.local.interface", "本地绑定接口", "*"), //
		SERVER_LOCAL_PORT("message.server.local.port", "本地绑定端口", "-1"), //
		SERVER_REDIAL_TIMES("message.server.redial.times", "网络连接断开时重新连接次数", "10"), // 负数表示不限次数

		/* 消息头通用参数 */
		COMMON_PARAMETER_TERMINAL_NO(COMMON_PARAMETER_PRE + "terminal.no", "终端编号", ""), //
		COMMON_PARAMETER_TERMINAL_ID(COMMON_PARAMETER_PRE + "terminal.id.i", "终端ID", ""), //
		COMMON_PARAMETER_TERMINAL_IP(COMMON_PARAMETER_PRE + "terminal.ip", "终端IP", ""), //
		COMMON_PARAMETER_PROVINCE_ID(COMMON_PARAMETER_PRE + "province.id.i", "省ID	", ""), //
		COMMON_PARAMETER_VERSION_NO(COMMON_PARAMETER_PRE + "version.no", "终端版本号", ""), //
		COMMON_PARAMETER_SYSTEM_ID(COMMON_PARAMETER_PRE + "system.id.i", "客户端版本-手机即开", ""),

		/* 消息处理配置 */
		WORKER_THREAD_NUMBER("message.worker.thread.number", "消息接收处理工作线程数", "3"), //
		REQUEST_QUEUE_MAXSIZE("message.request.queue.maxsize", "消息请求队列消息最大存储数，如果为0则表示队列几乎关闭", "10000"), //
		RESPONSE_QUEUE_MAXSIZE("message.response.queue.maxsize", "消息请求队列消息最大存储数，如果为0则表示队列几乎关闭", "10000"), //
		REQUEST_LIVE_TIME("message.request.live.time", "消息请求存活时间（单位：ms，用于无用垃圾信息回收,#1h）", "600000"), //
		RESPONSE_LIVE_TIME("message.response.live.time", "消息响应存活时间（单位:ms）", "600000"), //
		VALID_COMMAND_IDS("message.valid.command.ids", "合法消息指令ID列表（以','分隔）,只有出现在这里的指令id,其消息才被处理,如果接受所有,设置为*", "*"), //
		DEFAULT_TIMEOUT("message.default.timeout", "同步消息等待接收超时时间（单位:ms）", "10000"), //
		COMPRESS_SIZE_LIMIT("message.compress.size.limit", "数据包压缩Limit(Byte),如果为'<=0'表示全部压缩", "200"), //
		CLIENT_WORKER_NUMBER("message.client.worker.number", "Socket client工作线程数", "6"), //

		/* 安全 */
		SECURITY_IP_AUTH("message.security.ip.auth", "IP鉴权范围列表，每一项用'/'分割，每一项均为正则表达式", "*"),
		//
		SECURITY_ALGORITHM("message.security.algorithm", "加密算法", ""),		//
		SECURITY_KEY("message.security.key", "加密密钥", "");

		/** Key,描述,缺省值 */
		private final String key, desc, value;

		private ConfigKey(String key, String desc, String value) {
			this.key = key;
			this.desc = desc;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getDesc() {
			return desc;
		}

		public String getValue() {
			return value;
		}
	};

	/**
	 * 全局配置Item
	 */
	public class ConfigItem {
		/** 配置项Key,不允许修改 */
		private final ConfigKey key;

		/** 配置项Value,可以修改 */
		private String value;

		private ConfigItem(ConfigKey key) {
			this.key = key;
			this.value = key.getValue();
		}

		public void setValue(String value) {
			this.value = value;
			LOGGER.info("设置全局配置{}={}", this.key.getKey(), value);
		}

		public String getValue() {
			return value;
		}
	}

	/** 全局配置Map */
	protected final Map<ConfigKey, ConfigItem> configMap = new HashMap<ConfigKey, ConfigItem>();

	public ServiceConfig() {
		configMap.put(ConfigKey.SERVER_IP, new ConfigItem(ConfigKey.SERVER_IP));
		configMap.put(ConfigKey.SERVER_PORT, new ConfigItem(ConfigKey.SERVER_PORT));
		configMap.put(ConfigKey.SERVER_CONNECTION_TIMEOUT, new ConfigItem(ConfigKey.SERVER_CONNECTION_TIMEOUT));
		configMap.put(ConfigKey.SERVER_READ_TIMEOUT, new ConfigItem(ConfigKey.SERVER_READ_TIMEOUT));
		configMap.put(ConfigKey.SERVER_LOCAL_INTERFACE, new ConfigItem(ConfigKey.SERVER_LOCAL_INTERFACE));
		configMap.put(ConfigKey.SERVER_LOCAL_PORT, new ConfigItem(ConfigKey.SERVER_LOCAL_PORT));
		configMap.put(ConfigKey.SERVER_REDIAL_TIMES, new ConfigItem(ConfigKey.SERVER_REDIAL_TIMES));

		configMap.put(ConfigKey.COMMON_PARAMETER_TERMINAL_NO, new ConfigItem(ConfigKey.COMMON_PARAMETER_TERMINAL_NO));
		configMap.put(ConfigKey.COMMON_PARAMETER_TERMINAL_ID, new ConfigItem(ConfigKey.COMMON_PARAMETER_TERMINAL_ID));
		configMap.put(ConfigKey.COMMON_PARAMETER_TERMINAL_IP, new ConfigItem(ConfigKey.COMMON_PARAMETER_TERMINAL_IP));
		configMap.put(ConfigKey.COMMON_PARAMETER_PROVINCE_ID, new ConfigItem(ConfigKey.COMMON_PARAMETER_PROVINCE_ID));
		configMap.put(ConfigKey.COMMON_PARAMETER_VERSION_NO, new ConfigItem(ConfigKey.COMMON_PARAMETER_VERSION_NO));
		configMap.put(ConfigKey.COMMON_PARAMETER_SYSTEM_ID, new ConfigItem(ConfigKey.COMMON_PARAMETER_SYSTEM_ID));

		configMap.put(ConfigKey.WORKER_THREAD_NUMBER, new ConfigItem(ConfigKey.WORKER_THREAD_NUMBER));
		configMap.put(ConfigKey.REQUEST_QUEUE_MAXSIZE, new ConfigItem(ConfigKey.REQUEST_QUEUE_MAXSIZE));
		configMap.put(ConfigKey.RESPONSE_QUEUE_MAXSIZE, new ConfigItem(ConfigKey.RESPONSE_QUEUE_MAXSIZE));
		configMap.put(ConfigKey.REQUEST_LIVE_TIME, new ConfigItem(ConfigKey.REQUEST_LIVE_TIME));
		configMap.put(ConfigKey.RESPONSE_LIVE_TIME, new ConfigItem(ConfigKey.RESPONSE_LIVE_TIME));
		configMap.put(ConfigKey.VALID_COMMAND_IDS, new ConfigItem(ConfigKey.VALID_COMMAND_IDS));
		configMap.put(ConfigKey.DEFAULT_TIMEOUT, new ConfigItem(ConfigKey.DEFAULT_TIMEOUT));
		configMap.put(ConfigKey.COMPRESS_SIZE_LIMIT, new ConfigItem(ConfigKey.COMPRESS_SIZE_LIMIT));
		configMap.put(ConfigKey.CLIENT_WORKER_NUMBER, new ConfigItem(ConfigKey.CLIENT_WORKER_NUMBER));

		configMap.put(ConfigKey.SECURITY_IP_AUTH, new ConfigItem(ConfigKey.SECURITY_IP_AUTH));
		configMap.put(ConfigKey.SECURITY_ALGORITHM, new ConfigItem(ConfigKey.SECURITY_ALGORITHM));
		configMap.put(ConfigKey.SECURITY_KEY, new ConfigItem(ConfigKey.SECURITY_KEY));
	}

	/** 消息头公共参数 */
	private Map<String, Object> commonParameters = new HashMap<String, Object>();

	/**
	 * <p>外部自行设置消息头公共参数</p>
	 * 
	 * @return void
	 */
	public void setCommonParameters(Map<String, Object> commonParameters) {
		this.commonParameters = commonParameters;
	}

	/**
	 * <p> 获取通用参数的Map对象 </p>
	 * 
	 * @return
	 * @return Map<String,Object>
	 */
	public Map<String, Object> getCommonParameters() {
		if (!commonParameters.isEmpty()) {
			return commonParameters;
		}
		for (ConfigKey gc : ConfigKey.values()) {
			String key = gc.getKey();
			String strValue = configMap.get(gc).getValue();
			Object value = strValue;
			int index = key.indexOf(COMMON_PARAMETER_PRE);
			if (StringUtils.isEmpty(strValue) || index < 0) {
				continue;
			}
			// 去掉公共头
			key = key.replaceAll(COMMON_PARAMETER_PRE, "");
			if (key.endsWith(".i")) {// Int
				key = key.substring(0, key.lastIndexOf(".i"));
				value = Integer.parseInt(strValue);
			} else if (key.endsWith(".j")) {// Long
				key = key.substring(0, key.lastIndexOf("."));
				value = Long.parseLong(strValue);
			} else if (key.endsWith(".d")) {// Double
				key = key.substring(0, key.lastIndexOf(".d"));
				value = Double.parseDouble(strValue);
			}
			String[] parameterParts = key.split("\\.");
			for (int i = 0; i < parameterParts.length; i++) {
				parameterParts[i] = StringUtils.upperCase1stLetter(parameterParts[i]);
			}
			key = StringUtils.array2String(parameterParts);
			commonParameters.put(key, value);
		}
		return commonParameters;
	}

	// ---Begin of Bridge Method----//
	public int getServerRedialTimes() {
		return Integer.parseInt(configMap.get(ConfigKey.SERVER_REDIAL_TIMES).getValue());
	}

	public void setServerRedialTimes(int redialTimes) {
		configMap.get(ConfigKey.SERVER_REDIAL_TIMES).setValue(String.valueOf(redialTimes));
	}

	public int getDefaultTimeout() {
		return Integer.parseInt(configMap.get(ConfigKey.DEFAULT_TIMEOUT).getValue());
	}

	public void setDefaultTimeout(int defaultTimeout) {
		configMap.get(ConfigKey.DEFAULT_TIMEOUT).setValue(String.valueOf(defaultTimeout));
	}

	public int getReqMaxSize() {
		return Integer.parseInt(configMap.get(ConfigKey.REQUEST_QUEUE_MAXSIZE).getValue());
	}

	public void setReqMaxSize(int reqMaxSize) {
		configMap.get(ConfigKey.REQUEST_QUEUE_MAXSIZE).setValue(String.valueOf(reqMaxSize));
	}

	public int getResMaxSize() {
		return Integer.parseInt(configMap.get(ConfigKey.RESPONSE_QUEUE_MAXSIZE).getValue());
	}

	public void setResMaxSize(int resMaxSize) {
		configMap.get(ConfigKey.RESPONSE_QUEUE_MAXSIZE).setValue(String.valueOf(resMaxSize));
	}

	public int getWorkerThread() {
		return Integer.parseInt(configMap.get(ConfigKey.WORKER_THREAD_NUMBER).getValue());
	}

	public void setWorkerThread(int workerThread) {
		configMap.get(ConfigKey.WORKER_THREAD_NUMBER).setValue(String.valueOf(workerThread));
	}

	public int getRequestLiveTime() {
		return Integer.parseInt(configMap.get(ConfigKey.REQUEST_LIVE_TIME).getValue());
	}

	public void setRequestLiveTime(int requestLiveTime) {
		configMap.get(ConfigKey.REQUEST_LIVE_TIME).setValue(String.valueOf(requestLiveTime));
	}

	public int getResponseLiveTime() {
		return Integer.parseInt(configMap.get(ConfigKey.RESPONSE_LIVE_TIME).getValue());
	}

	public void setResponseLiveTime(int responseLiveTime) {
		configMap.get(ConfigKey.RESPONSE_LIVE_TIME).setValue(String.valueOf(responseLiveTime));
	}

	public String getValidCommandIds() {
		return configMap.get(ConfigKey.VALID_COMMAND_IDS).getValue();
	}

	public void setValidCommandIds(String validCommandIds) {
		configMap.get(ConfigKey.VALID_COMMAND_IDS).setValue(validCommandIds);
	}

	public String getServerIp() {
		return configMap.get(ConfigKey.SERVER_IP).getValue();
	}

	public void setServerIp(String serverIp) {
		configMap.get(ConfigKey.SERVER_IP).setValue(serverIp);
	}

	public int getServerPort() {
		return Integer.parseInt(configMap.get(ConfigKey.SERVER_PORT).getValue());
	}

	public void setServerPort(int serverPort) {
		configMap.get(ConfigKey.SERVER_PORT).setValue(String.valueOf(serverPort));
	}

	public int getServerConnectionTimeout() {
		return Integer.parseInt(configMap.get(ConfigKey.SERVER_CONNECTION_TIMEOUT).getValue());
	}

	public void setServerConnectionTimeout(int connectionTimeout) {
		configMap.get(ConfigKey.SERVER_CONNECTION_TIMEOUT).setValue(String.valueOf(connectionTimeout));
	}

	public int getServerReadTimeout() {
		return Integer.parseInt(configMap.get(ConfigKey.SERVER_READ_TIMEOUT).getValue());
	}

	public void setServerReadTimeout(int readTimeout) {
		configMap.get(ConfigKey.SERVER_READ_TIMEOUT).setValue(String.valueOf(readTimeout));
	}

	public String getServerLocalInterface() {
		return configMap.get(ConfigKey.SERVER_LOCAL_INTERFACE).getValue();
	}

	public void setServerLocalInterface(String localInterface) {
		configMap.get(ConfigKey.SERVER_LOCAL_INTERFACE).setValue(localInterface);
	}

	public int getServerLocalPort() {
		return Integer.parseInt(configMap.get(ConfigKey.SERVER_LOCAL_PORT).getValue());
	}

	public void setServerLocalPort(int localPort) {
		configMap.get(ConfigKey.SERVER_LOCAL_PORT).setValue(String.valueOf(localPort));
	}

	public int getCompressSizeLimit() {
		return Integer.parseInt(configMap.get(ConfigKey.COMPRESS_SIZE_LIMIT).getValue());
	}

	public void setCompressSizeLimit(int sizeLimit) {
		configMap.get(ConfigKey.COMPRESS_SIZE_LIMIT).setValue(String.valueOf(sizeLimit));
	}

	public int getClientWorkerNumber() {
		return Integer.parseInt(configMap.get(ConfigKey.CLIENT_WORKER_NUMBER).getValue());
	}

	public void setClientWorkerNumber(int clientWorkerNumber) {
		configMap.get(ConfigKey.CLIENT_WORKER_NUMBER).setValue(String.valueOf(clientWorkerNumber));
	}

	// -公共包头设置
	public String getCommonParameterTerminalNo() {
		return configMap.get(ConfigKey.COMMON_PARAMETER_TERMINAL_NO).getValue();
	}

	public void setCommonParameterTerminalNo(String terminalNo) {
		configMap.get(ConfigKey.COMMON_PARAMETER_TERMINAL_NO).setValue(terminalNo);
		commonParameters.clear();
	}

	public int getCommonParameterTerminalId() {
		return Integer.parseInt(configMap.get(ConfigKey.COMMON_PARAMETER_TERMINAL_ID).getValue());
	}

	public void setCommonParameterTerminalId(int terminalId) {
		configMap.get(ConfigKey.COMMON_PARAMETER_TERMINAL_ID).setValue(String.valueOf(terminalId));
		commonParameters.clear();
	}

	public String getCommonParameterTerminalIp() {
		return configMap.get(ConfigKey.COMMON_PARAMETER_TERMINAL_IP).getValue();
	}

	public void setCommonParameterTerminalIp(String terminalIp) {
		configMap.get(ConfigKey.COMMON_PARAMETER_TERMINAL_IP).setValue(terminalIp);
		commonParameters.clear();
	}

	public int getCommonParameterProvinceId() {
		return Integer.parseInt(configMap.get(ConfigKey.COMMON_PARAMETER_PROVINCE_ID).getValue());
	}

	public void setCommonParameterProvinceId(int provinceId) {
		configMap.get(ConfigKey.COMMON_PARAMETER_PROVINCE_ID).setValue(String.valueOf(provinceId));
		commonParameters.clear();
	}

	public String getCommonParameterVersionNo() {
		return configMap.get(ConfigKey.COMMON_PARAMETER_VERSION_NO).getValue();
	}

	public void setCommonParameterVersionNo(String versionNo) {
		configMap.get(ConfigKey.COMMON_PARAMETER_VERSION_NO).setValue(versionNo);
		commonParameters.clear();
	}

	public int getCommonParameterSystemId() {
		return Integer.parseInt(configMap.get(ConfigKey.COMMON_PARAMETER_SYSTEM_ID).getValue());
	}

	public void setCommonParameterSystemId(int systemId) {
		configMap.get(ConfigKey.COMMON_PARAMETER_SYSTEM_ID).setValue(String.valueOf(systemId));
		commonParameters.clear();// 变更了重新加载
	}

	public String getSecurityIpAuth() {
		return configMap.get(ConfigKey.SECURITY_IP_AUTH).getValue();
	}

	public void setSecurityIpAuth(String securityIpAuth) {
		configMap.get(ConfigKey.SECURITY_IP_AUTH).setValue(securityIpAuth);
	}

	public String getSecurityAlgorithm() {
		return configMap.get(ConfigKey.SECURITY_ALGORITHM).getValue();
	}

	public void setSecurityAlgorithm(String securityAlgorithm) {
		configMap.get(ConfigKey.SECURITY_ALGORITHM).setValue(securityAlgorithm);
	}

	public String getSecurityKey() {
		return configMap.get(ConfigKey.SECURITY_KEY).getValue();
	}

	public void setSecurityKey(String securityKey) {
		configMap.get(ConfigKey.SECURITY_KEY).setValue(securityKey);
	}

	// --------End of Bridge Method--------//
	/**
	 * <p> 解析指定的全局配置文件 </p>
	 * 
	 * @param gloabConfigFile全局配置文件
	 * @return
	 * @return MessageServiceConfig
	 */
	public static ServiceConfig parserConfig(File gloabConfigFile) {
		if (gloabConfigFile == null || !gloabConfigFile.exists()) {
			throw new ConfigException("配置文件为空,或者" + gloabConfigFile.getAbsolutePath() + "不存在!");
		}
		try {
			return parserConfig(new BufferedInputStream(new FileInputStream(gloabConfigFile)));
		} catch (IOException e) {
			throw new ConfigException("加载配置文件[" + gloabConfigFile + "]异常,异常消息:" + e.getMessage(), e);
		}
	}

	/**
	 * <p> 在加载ServiceConfig的类加载根路径下加载service.properties配置文件 </p>
	 * 
	 * @return
	 * @return MessageServiceConfig
	 */
	public static ServiceConfig parserConfig() {
		return parserConfig(ServiceConfig.class.getResourceAsStream("/service.properties"));
	}

	/**
	 * <p>依据消息定义配置输入流加载配置信息</p>
	 * 
	 * @param msgDefinationStream消息定义输入流
	 * @return
	 * @return MessageConfig
	 */
	public static ServiceConfig parserConfig(InputStream msgDefinationStream) {
		Properties props = new Properties();
		try {
			props.load(new BufferedInputStream(msgDefinationStream));
		} catch (IOException e) {
			throw new ConfigException("加载配置文件[" + msgDefinationStream + "]异常,异常消息:" + e.getMessage(), e);
		}
		ServiceConfig config = new ServiceConfig();
		config.prepareConfig(props);
		props.clear();
		return config;
	}

	/**
	 * <p>解析全局配置</p>
	 * 
	 * @param props
	 * @return void
	 */
	public static ServiceConfig parserConfig(Properties props) {
		ServiceConfig config = new ServiceConfig();
		config.prepareConfig(props);
		props.clear();
		return config;
	}

	/**
	 * <p>解析全局配置</p>
	 * 
	 * @param props
	 * @return void
	 */
	private void prepareConfig(Properties props) {
		for (ConfigKey gc : ConfigKey.values()) {// message.server.port
			String key = gc.getKey();
			configMap.get(gc).setValue(props.getProperty(key, gc.getValue()));
			// LOGGER.info("解析全局配置:{}[{}]:{}", new String[] { gc.getKey(), gc.getDesc(), gc.getValue() });
		}
	}

	public Map<ConfigKey, ConfigItem> getConfigMap() {
		return configMap;
	}

}
