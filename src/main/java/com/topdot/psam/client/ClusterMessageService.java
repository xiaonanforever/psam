/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.client.PsamServiceCluster.java
 * 所含类: PsamServiceCluster.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月16日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.MessageServiceTemplate;
import com.topdot.framework.message.exception.ServiceNotLogonException;
import com.topdot.framework.message.mont.ServiceHolding;
import com.topdot.framework.message.protocol.MessageBody;
import com.topdot.framework.message.protocol.MessageHeader;
import com.topdot.framework.message.protocol.RequestMessage;
import com.topdot.framework.message.protocol.ResponseMessage;

/**
 * <p>PsamClusterService</p>
 * <p>PSAM集群服务</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName PsamServiceCluster
 * @author zhangyongxin
 * @version 1.0
 */

public class ClusterMessageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClusterMessageService.class);

	/** 集群策略 */
	public enum ClusterPolicy {
		ROUND("ROUND", "轮询"), //
		SESSION("SESSION", "会话绑定"), //
		RANDOM("RANDOM", "随机");

		private final String key, desc;

		private ClusterPolicy(String key, String desc) {
			this.key = key;
			this.desc = desc;
		}

		public String getKey() {
			return key;
		}

		public String getDesc() {
			return desc;
		}
	}

	private final ClusterPolicy clusterPolicy;

	/** 存活的Message Service List */
	private final List<MessageServiceTemplate> mstList = new ArrayList<MessageServiceTemplate>();

	/** 存活的Message Service Map */
	private Map<String, MessageServiceTemplate> mstMap = null;

	/** 当前请求计数 */
	private final AtomicInteger couter = new AtomicInteger(0);

	ClusterMessageService(ClusterPolicy clusterPolicy) {
		this.clusterPolicy = clusterPolicy;
	}

	public ResponseMessage call(RequestMessage<? extends MessageHeader, ? extends MessageBody> requestMsg) {
		LOGGER.info("请求...");
		int retry = mstList.size();
		if(retry==0){
			Map<String, MessageServiceTemplate> tempMstMap = ServiceHolding.getActiveMST();
			if (mstMap == null || tempMstMap.hashCode() != mstMap.hashCode()) {
				mstMap = tempMstMap;
				mstList.clear();
				for (String key : mstMap.keySet()) {
					mstList.add(mstMap.get(key));
				}
			}
			retry = mstList.size();
		}
		MessageServiceImpl pms;
		ResponseMessage rspMsg = null;
		while (retry > 0) {
			Map<String, MessageServiceTemplate> tempMstMap = ServiceHolding.getActiveMST();
			if (mstMap == null || tempMstMap.hashCode() != mstMap.hashCode()) {
				mstMap = tempMstMap;
				mstList.clear();
				for (String key : mstMap.keySet()) {
					mstList.add(mstMap.get(key));
				}
			}

			int currentCouter = couter.incrementAndGet();
			// 第一次请求的话，没有Session
			if (requestMsg.getHeader().getSessionId() == null) {
				switch (clusterPolicy) {
					case RANDOM: {
						Long random = Math.round(Math.random() * (mstList.size() - 1));
						pms = (MessageServiceImpl) mstList.get(random.intValue());
					}
					default: {// 默认是轮询
						pms = (MessageServiceImpl) mstList.get(currentCouter % mstList.size());
					}
				}
			} else {
				switch (clusterPolicy) {
					case ROUND: {
						pms = (MessageServiceImpl) mstList.get(currentCouter % mstList.size());
					}
					case SESSION: {
						pms = (MessageServiceImpl) mstMap.get(requestMsg.getHeader().getSessionId());// 这里需要改进//TODO
					}
					case RANDOM: {
						Long random = Math.round(Math.random() * mstList.size());
						pms = (MessageServiceImpl) mstList.get(random.intValue());
					}
					default: {// 默认是轮询
						pms = (MessageServiceImpl) mstList.get(currentCouter % mstList.size());
					}
				}
			}

			try {
				rspMsg = pms.call(requestMsg);
				return rspMsg;
			} catch (ServiceNotLogonException e) {
				retry--;
			}
		}
		// 这里返回的必然是异常的
		return rspMsg;
	}

	public Map<String, MessageServiceTemplate> getMstMap() {
		if (mstMap == null) {
			mstMap = ServiceHolding.getActiveMST();
			for (String key : mstMap.keySet()) {
				mstList.add(mstMap.get(key));
			}
		}
		return Collections.unmodifiableMap(mstMap);
	}
}
