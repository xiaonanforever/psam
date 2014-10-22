/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.MessageServiceTest.java
 * 所含类: MessageServiceTest.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.mock;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.protocol.ResponseMessage;
import com.topdot.psam.client.ClusterMessageService;
import com.topdot.psam.client.ClusterMessageServiceFactory;
import com.topdot.psam.message.protocol.request.SystemEventRequest;
import com.topdot.psam.message.protocol.request.SystemEventRequestBody;
import com.topdot.psam.message.protocol.request.TicketInfoRequest;
import com.topdot.psam.message.protocol.request.TicketInfoRequestBody;
import com.topdot.psam.message.protocol.request.TicketSaleRequest;
import com.topdot.psam.message.protocol.request.TicketSaleRequestBody;
import com.topdot.psam.message.protocol.response.TicketInfoResponse;
import com.topdot.psam.message.vo.RngParameter;
import com.topdot.psam.message.vo.TicketInfo;
import com.topdot.psam.message.vo.TransactionInfo;

/**
 * <p>消息服务测试</p>
 * <p> 综合测试，测试逻辑
 * 1) 启动集群
 * 2) 售票
 * 3) 售票查询
 * 4) 系统事件上报
 * </p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName PsamClientStartup
 * @author zhangyongxin
 */

public class PsamClientStartup {
	private static final Logger LOGGER = LoggerFactory.getLogger(PsamClientStartup.class);
	private final ClusterMessageService cluster;

	public PsamClientStartup() {
		cluster = ClusterMessageServiceFactory.startClusterService();
	}

	public void sales() {
		LOGGER.info("售票...");
		TicketSaleRequest req = new TicketSaleRequest();
		req.getHeader().setSn("SN");
		TicketSaleRequestBody body = req.getBody();
		body.setNeedRandomNumber(true);
		body.setRngParameter(mockRngParameter());
		body.setTransactionInfo(mockTransactionID());
		body.setTicketInfo(mockTicketInfo());
		body.setRequestTime(new Date());
		ResponseMessage rspMsg = cluster.call(req);// 查询全部
		if (rspMsg.success()) {
			LOGGER.info("售票成功");
		} else {
			LOGGER.error("售票失败:原因:{}", rspMsg.getErrorMessage().toString());
			return;
		}
	}

	public void querySaleInfo() {
		LOGGER.info("售票查询...");
		TicketInfoRequest req = new TicketInfoRequest();
		req.getHeader().setSn("SN");
		TicketInfoRequestBody body = req.getBody();
		body.setTransactionSn("TransactionSn");
		body.setAccountNo("AccountNo");
		body.setRequestTime(new Date());

		ResponseMessage rspMsg = cluster.call(req);
		if (rspMsg.success()) {
			LOGGER.info("售票查询成功!");
			TicketInfoResponse tiRspMsg = (TicketInfoResponse) rspMsg;
			TicketInfo ti = tiRspMsg.getBody().getTicketInfo();
			LOGGER.info("售票信息:<{}>", ti.getStakeDetails());
		} else {
			LOGGER.error("售票查询失败:原因:{}", rspMsg.getErrorMessage().toString());
			return;
		}
	}

	public void reportSystemEnvent() {
		LOGGER.info("系统事件上报...");
		SystemEventRequest req = new SystemEventRequest();
		SystemEventRequestBody body = req.getBody();
		body.setEventDescription("时间描述");
		body.setEventLevel(1);
		body.setEventModule(1);
		body.setEventSn("事件SN");
		body.setEventTime("2012-12-15 23:40:40");
		body.setEventType(1);
		ResponseMessage rspMsg = cluster.call(req);
		if (rspMsg.success()) {
			LOGGER.info("系统事件上报成功!");
		} else {
			LOGGER.error("系统事件上报失败:原因:{}", rspMsg.getErrorMessage().toString());
			return;
		}
	}

	public static void main(String[] args) {
		LOGGER.info("开始测试...");
		PsamClientStartup client = new PsamClientStartup();

		client.sales();
		client.querySaleInfo();
		client.reportSystemEnvent();

		LOGGER.info("测试结束.");
	}

	private TransactionInfo mockTransactionID() {
		TransactionInfo tid = new TransactionInfo();
		tid.setAccountNo("AccountNo");
		tid.setGameNo("GameNo");
		tid.setTransactionSn("TransactionSn");
		return tid;
	}

	private TicketInfo mockTicketInfo() {
		TicketInfo ti = new TicketInfo();
		ti.setTicketNo("TicketNo");
		ti.setEncryptionKey("EncryptionKey");
		ti.setEncryptionVersion("版本");
		ti.setLotteryAmount(12345.09);
		ti.setLotterySn("LotterySn");
		ti.setMultiBetCount(5);
		ti.setSaleTime("2012-12-12 23:40:40");
		ti.setStakeCount(5);
		ti.setStakeDetails("StakeDetails");
		ti.setStepNo(1);

		return ti;
	}

	private RngParameter mockRngParameter() {
		RngParameter rng = new RngParameter();
		rng.setAllowMultipleRequest(true);
		rng.setAllowRepeated(true);
		rng.setMaxValue(Integer.MAX_VALUE);
		rng.setMinValue(0);
		rng.setRandomNumberCount(15);
		return rng;
	}
}
