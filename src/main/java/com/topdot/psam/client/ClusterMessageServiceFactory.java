package com.topdot.psam.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.MessageServiceFactory;
import com.topdot.framework.message.config.MessageConfig;
import com.topdot.framework.message.config.ServiceConfig;
import com.topdot.framework.message.exception.ConfigException;
import com.topdot.framework.message.exception.ServiceClusterReadyException;
import com.topdot.framework.message.protocol.ResponseMessage;
import com.topdot.psam.message.protocol.request.LogonRequest;
import com.topdot.psam.message.vo.Server;

/**
 * <p>PsamMessageServiceFactory</p>
 * <p>Psam消息服务工厂类</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */
public class ClusterMessageServiceFactory extends MessageServiceFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClusterMessageServiceFactory.class);

	private static final String messageConfigFile = "/message.client.properties";
	private static final String serviceConfigFile = "/service.client.properties";

	public static ClusterMessageService startClusterService() throws ServiceClusterReadyException {
		LOGGER.info("准备Psam Cluster Server...");
		ClusterMessageService cluster = new ClusterMessageService(ClusterMessageService.ClusterPolicy.RANDOM);
		
		LOGGER.info("解析配置文件...");
		// ServiceConfig模板
		Properties servicePropsTemplate = new Properties();
		// 加载配置文件
		Properties serverProps = new Properties();
		try {
			InputStream is = ServiceConfig.class.getResourceAsStream(serviceConfigFile);
			serverProps.load(new BufferedInputStream(is));
		} catch (IOException e) {
			throw new ConfigException("加载配置文件[" + serviceConfigFile + "]异常,异常消息:" + e.getMessage(), e);
		}
		MessageConfig mc = MessageConfig.parserConfig(MessageConfig.class.getResourceAsStream(messageConfigFile));
		List<Server> psamServerList = new ArrayList<Server>();
		// 分析server配置
		for (Object propskey : serverProps.keySet()) {
			String key = (String) propskey;
			if (key.startsWith("message.server")) {// 服务器设置
				if (key.endsWith("ip")) {
					String keyPrefix = key.substring(0, key.lastIndexOf('.'));// 某台服务器定义前缀
					String ip = serverProps.getProperty(key);
					int port = Integer.parseInt(serverProps.getProperty(keyPrefix + ".port"));
					Server ss = new Server(ip + ":" + port, ip, port);
					psamServerList.add(ss);
				}
			} else {// 非以serial.server开头的
				String value = serverProps.getProperty(key);
				servicePropsTemplate.put(key, value);
			}
		}
		setMsgServiceNum(psamServerList.size());
		boolean connectSuccess = true;
		for(int i = 0;i<psamServerList.size();i++) {
			Server ss = psamServerList.get(i);
			Properties props = new Properties();
			props.putAll(servicePropsTemplate);
			props.put(ServiceConfig.ConfigKey.SERVER_IP.getKey(), ss.getIp());
			props.put(ServiceConfig.ConfigKey.SERVER_PORT.getKey(), ss.getPort() + "");// 设置int数据获取不出来，必须转为String
			ServiceConfig sc = ServiceConfig.parserConfig(props);
			String terminalNo = sc.getCommonParameterTerminalNo() +":"+i+ "@" + sc.getServerIp() + ":" + sc.getServerPort();
			MessageServiceImpl ms = (MessageServiceImpl) create(terminalNo, sc, mc,
					new MessageServiceImpl());
			LOGGER.info("连接PSAM服务器<{}>...", terminalNo);
			LogonRequest reqMsg = new LogonRequest();
			reqMsg.getHeader().setTerminalNo(sc.getCommonParameterTerminalNo());
			reqMsg.getBody().setTerminalNo(sc.getCommonParameterTerminalNo());
			reqMsg.getBody().setTerminalPassword("123456");
			ResponseMessage rspMsg = ms.logon(reqMsg);
			if (rspMsg.success()) {
				LOGGER.info("连接PSAM服务器<{}>成功!", terminalNo);
			} else {
				connectSuccess = false;
				LOGGER.error("PSAM服务器<{}>连接异常:[{}]!", terminalNo, rspMsg.getErrorMessage().toString());
			}
		}

		if (!connectSuccess) {
			throw new ServiceClusterReadyException();
		}
		return cluster;
	}
}
