/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.gimp.eit.config.EitServiceDefaultConfig.java
 * 所含类: EitServiceDefaultConfig.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.Session;
import com.topdot.framework.message.exception.ConfigException;
import com.topdot.framework.message.executor.MessageExecutor;
import com.topdot.framework.message.protocol.MessageHeader;
import com.topdot.framework.message.protocol.RequestMessage;
import com.topdot.framework.message.protocol.ResponseMessage;
import com.topdot.framework.message.reciver.MessageReciver;
import com.topdot.framework.util.StringUtils;

/**
 * <p>消息定义配置</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class MessageConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageConfig.class);

	/** Request Package */
	private final static String REQUEST_PACKAGE = "message.defination.request.package";

	/** Response Package */
	private final static String RESPONSE_PACKAGE = "message.defination.response.package";

	/** Executor Package */
	private final static String EXECUTOR_PACKAGE = "message.defination.executor.package";

	/** Reciver Package */
	private final static String RECIVER_PACKAGE = "message.defination.reciver.package";

	/** 当前工作目录，主要用于加载上面的Package下的class */
	private final static String CURRENT_DIR = ".";

	/** Default Reciver */
	private final static String DEFAULT_RECIVER = "message.default.reciver";

	/** 缺省消息接收者 */
	private MessageReciver<ResponseMessage> defaultMessageReciver;

	/** 消息类型定义Map<消息标示,消息类型定义> */
	private Map<String, MessageDefination> messageDefByKeyMap;

	/** 消息类型定义Map<CommonadId,消息类型定义> */
	private Map<Integer, MessageDefination> messageDefByIdMap;

	/**
	 * <p>在加载MessageConfig的类加载根路径下加载message.properties配置文件</p>
	 * 
	 * @return
	 * @return MessageConfig
	 */
	public static MessageConfig parserConfig() {
		return parserConfig(MessageConfig.class.getResourceAsStream("/message.properties"));
	}

	/**
	 * <p>依据消息定义配置文件加载配置信息</p>
	 * 
	 * @param msgDefinationFile消息定义文件
	 * @return
	 * @return MessageConfig
	 */
	public static MessageConfig parserConfig(File msgDefinationFile) {
		if (msgDefinationFile == null || !msgDefinationFile.exists()) {
			throw new ConfigException("消息定义文件为空,或者" + msgDefinationFile.getAbsolutePath() + "不存在!");
		}
		Properties props = new Properties();
		try {
			props.load(new BufferedInputStream(new FileInputStream(msgDefinationFile)));
			return parserConfig(props);
		} catch (IOException e) {
			throw new ConfigException("加载消息定义文件[" + msgDefinationFile + "]异常,异常消息:" + e.getMessage(), e);
		}
	}

	/**
	 * <p>依据消息定义配置输入流加载配置信息</p>
	 * 
	 * @param msgDefinationStream消息定义输入流
	 * @return
	 * @return MessageConfig
	 */
	public static MessageConfig parserConfig(InputStream msgDefinationStream) {
		Properties props = new Properties();
		try {
			props.load(new BufferedInputStream(msgDefinationStream));
			return parserConfig(props);
		} catch (IOException e) {
			throw new ConfigException("加载消息定义文件[" + msgDefinationStream + "]异常,异常消息:" + e.getMessage(), e);
		}
	}

	/**
	 * <p>依据消息定义配置Properties加载配置信息（消息定义的最终加载者）</p>
	 * 
	 * @param props
	 * @return
	 * @return MessageConfig
	 */
	@SuppressWarnings("unchecked")
	public static MessageConfig parserConfig(Properties props) {
		String requestPackage = props.getProperty(REQUEST_PACKAGE);
		String responsePackage = props.getProperty(RESPONSE_PACKAGE);
		String executorPackage = props.getProperty(EXECUTOR_PACKAGE);
		String reciverPackage = props.getProperty(RECIVER_PACKAGE);

		Map<String, String> msgMap = new HashMap<String, String>();
		for (Object propskey : props.keySet()) {
			String keyStr = (String) propskey;
			if (keyStr.contains("id")) {
				String key = keyStr.substring(0, keyStr.lastIndexOf('.'));// 获取消息定义字符串
				String reqKey = key + ".request";
				if (props.containsKey(reqKey)) {// 消息定义优先采用RequestClassName
					String reqValue = props.getProperty(reqKey);
					if (reqValue.startsWith(CURRENT_DIR)) {
						reqValue = requestPackage + reqValue;
					}
					msgMap.put(key, reqValue);
				} else {
					msgMap.put(key, key);
				}
			}
		}
		/** 解析消息定义 */
		Map<String, MessageDefination> msgDefMap = new HashMap<String, MessageDefination>();
		for (String key : msgMap.keySet()) {
			MessageHeader header = new MessageHeader();

			// 设置消息名称
			String msgName;
			try {
				// 消息名称，优先 采用预先设置的Name,如果Name没有设置，那么用使用默认值
				msgName = new String(props.getProperty(key + ".name", msgMap.get(key)).getBytes("iso-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new ConfigException("系统异常:" + e.getMessage(), e);
			}
			header.setCommandName(msgName);
			msgName = "[" + msgName + "]";
			// 设置消息类型
			String msgTypeKey = key + ".type";
			String msgType = props.getProperty(msgTypeKey);
			if (StringUtils.isEmpty(msgType)) {
				throw new ConfigException(msgTypeKey + msgName + "的消息命令类型不存在!");
			}
			header.setCommandType(Integer.parseInt(msgType));

			// 设置消息Id
			String msgIdKey = key + ".id";
			String msgId = props.getProperty(msgIdKey);
			if (StringUtils.isEmpty(msgId)) {
				throw new ConfigException(msgIdKey + msgName + "的消息命令ID不存在!");
			}
			header.setCommandId(Integer.parseInt(msgId));

			// 设置超时时间
			String timeOutKey = key + ".timeout";
			String timeOut = props.getProperty(timeOutKey);
			if (StringUtils.isEmpty(timeOut)) {
				// 缺省超时时间标示，不是实际的超时，具体是超时是多少，在实际消息发送之前设置
				header.setTimeout(RequestMessage.TIME_OUT_DEFAULT);
			} else {
				header.setTimeout(Integer.parseInt(timeOut));
			}

			// 设置消息定义
			MessageDefination md = new MessageDefination();
			md.setMessageType(header);

			// 加载消息Reciver
			String reciverKey = key + ".reciver";
			String reciverClass = props.getProperty(reciverKey);
			if (StringUtils.isEmpty(reciverClass)) {
				LOGGER.warn("{}{}的消息命令Reciver Class[{}]不存在!系统将采用缺省的DefaultReciver", new Object[] { reciverKey, msgName,
						reciverClass });
			} else {
				if (reciverClass.startsWith(CURRENT_DIR)) {
					reciverClass = reciverPackage + reciverClass;
				}
				try {
					MessageReciver<ResponseMessage> reciver = (MessageReciver<ResponseMessage>) Class.forName(
							reciverClass).newInstance();
					md.setMessageReciver(reciver);
				} catch (Exception e) {
					throw new ConfigException(reciverKey + msgName + "的消息命令Reciver不存在!", e);
				}
			}

			// 加载消息Request
			String reqKey = key + ".request";
			String reqClass = props.getProperty(reqKey);
			if (StringUtils.isEmpty(reqClass)) {// Request Class不存在的条件下，Reciver必须存在
				if (StringUtils.isEmpty(reciverClass)) {
					throw new ConfigException(msgName + "[" + reqKey + "]的消息命令Request 不存在!");
				} else {
					LOGGER.info("{}[{}]的消息命令Request不存在!", reciverKey, msgName);
				}
			} else {
				if (reqClass.startsWith(CURRENT_DIR)) {
					reqClass = requestPackage + reqClass;
				}
				try {
					md.setReqMsg((RequestMessage<?, ?>) Class.forName(reqClass).newInstance());
				} catch (Exception e) {
					throw new ConfigException(reqKey + msgName + "的消息命令RequestMessage Class[" + reqClass + "]不存在!", e);
				}
			}

			// 设置消息的Response
			String rspKey = key + ".response";
			String rspClass = props.getProperty(rspKey);
			if (StringUtils.isEmpty(rspClass)) {
				LOGGER.warn("{}-{}的消息命令Response配置不存在!", rspKey, msgName);
			} else {
				if (rspClass.startsWith(CURRENT_DIR)) {
					rspClass = responsePackage + rspClass;
				}
				try {
					md.setRspMsg((ResponseMessage) Class.forName(rspClass).newInstance());
				} catch (Exception e) {
					throw new ConfigException(rspKey + msgName + "的消息命令Response Class[" + rspClass + "]解析异常!", e);
				}
			}

			// 设置消息的Response
			String executorKey = key + ".executor";
			String executorClass = props.getProperty(executorKey);
			if (StringUtils.isEmpty(executorClass)) {
				LOGGER.warn("{}-{}的消息命令Executor配置不存在!", executorKey, msgName);
			} else {
				if (executorClass.startsWith(CURRENT_DIR)) {
					executorClass = executorPackage + executorClass;
				}
				try {
					md.setExecutor((MessageExecutor<RequestMessage<?, ?>, ResponseMessage>) Class
							.forName(executorClass).newInstance());
				} catch (Exception e) {
					throw new ConfigException(
							executorKey + msgName + "的消息命令Executor Class[" + executorClass + "]解析异常!", e);
				}
			}

			msgDefMap.put(msgMap.get(key), md);
		}

		MessageConfig config = new MessageConfig();
		config.messageDefByKeyMap = msgDefMap;

		Map<Integer, MessageDefination> idMsgMap = new HashMap<Integer, MessageDefination>();

		for (String key : msgDefMap.keySet()) {
			MessageDefination messageDefination = msgDefMap.get(key);
			int commandId = messageDefination.getMessageType().getCommandId();
			idMsgMap.put(commandId, messageDefination);
		}
		config.messageDefByIdMap = idMsgMap;

		/** 增加缺省Reciver */
		String defaultReciverClass = props.getProperty(DEFAULT_RECIVER);

		if (defaultReciverClass.startsWith(CURRENT_DIR)) {
			defaultReciverClass = reciverPackage + defaultReciverClass;
		}
		try {
			config.defaultMessageReciver = (MessageReciver<ResponseMessage>) Class.forName(defaultReciverClass)
					.newInstance();
		} catch (Exception e) {
			throw new ConfigException("不存在DefaultReciver[" + defaultReciverClass + "]配置!", e);
		}

		return config;
	}

	public MessageReciver<ResponseMessage> getDefaultMessageReciver() {
		return defaultMessageReciver;
	}

	public Map<String, MessageDefination> getMessageDefByKeyMap() {
		return messageDefByKeyMap;
	}

	public Map<Integer, MessageDefination> getMessageDefByIdMap() {
		return messageDefByIdMap;
	}

	/** 配置Reciver 所属Session */
	public void setReciverSession(Session session) {
		for (MessageDefination md : messageDefByKeyMap.values()) {
			if (md.getMessageReciver() != null) {
				md.getMessageReciver().setSession(session);
			}
		}
	}

}
