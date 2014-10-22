package com.topdot.psam.message.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.executor.AbstractMessageExecutor;
import com.topdot.psam.message.protocol.PsamRspHeader;
import com.topdot.psam.message.protocol.request.TicketInfoRequest;
import com.topdot.psam.message.protocol.request.TicketInfoRequestBody;
import com.topdot.psam.message.protocol.response.TicketInfoResponse;
import com.topdot.psam.message.protocol.response.TicketInfoResponseBody;
import com.topdot.psam.message.vo.TicketInfo;
import com.topdot.psam.message.vo.TransactionInfo;

public class TicketInfoExecutor extends AbstractMessageExecutor<TicketInfoRequest, TicketInfoResponse> {
	private static final Logger LOGGER = LoggerFactory.getLogger(TicketInfoExecutor.class);
	public TicketInfoExecutor() {

	}

	@Override
	public TicketInfoResponse execute(TicketInfoRequest reqMsg) {
		LOGGER.info("查询售票...");
		TicketInfoResponse rsp = new TicketInfoResponse();

		PsamRspHeader rspHeader = mockRsp(reqMsg.getHeader());
		rsp.setHeader(rspHeader);
		TicketInfoRequestBody reqBody = reqMsg.getBody();
		LOGGER.info("交易流水:<{}>",reqBody.getTransactionSn());
		
		TicketInfoResponseBody rspBody = new TicketInfoResponseBody();
		rspBody.setSignature("签名验签");
		
		rspBody.setTicketInfo(mockTicketInfo());
		rspBody.setTransactionInfo(mockTransactionIo());
		rsp.setBody(rspBody);
		return rsp;
	}
	
	private TransactionInfo mockTransactionIo() {
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
}