package com.topdot.psam.message.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.executor.AbstractMessageExecutor;
import com.topdot.psam.message.protocol.PsamRspHeader;
import com.topdot.psam.message.protocol.request.TicketSaleRequest;
import com.topdot.psam.message.protocol.request.TicketSaleRequestBody;
import com.topdot.psam.message.protocol.response.TicketSaleResponse;
import com.topdot.psam.message.protocol.response.TicketSaleResponseBody;

public class TicketSaleExecutor extends AbstractMessageExecutor<TicketSaleRequest, TicketSaleResponse> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TicketSaleExecutor.class);

	public TicketSaleExecutor() {

	}

	@Override
	public TicketSaleResponse execute(TicketSaleRequest reqMsg) {
		LOGGER.info("售票...");
		TicketSaleResponse rsp = new TicketSaleResponse();

		PsamRspHeader rspHeader = mockRsp(reqMsg.getHeader());
		rsp.setHeader(rspHeader);
		TicketSaleRequestBody reqBody = reqMsg.getBody();
		LOGGER.info("票面信息:<{}>", reqBody.getTicketInfo().getStakeDetails());
		
		TicketSaleResponseBody rspBody = new TicketSaleResponseBody();
		rspBody.setTicketInfo(reqBody.getTicketInfo());
		rspBody.setTransactionInfo(reqBody.getTransactionInfo());
		rspBody.setPrizeAmount(23400D);
		rspBody.setSignature("签名验签");
		rsp.setBody(rspBody);
		return rsp;
	}

}