package com.topdot.psam.message.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.executor.AbstractMessageExecutor;
import com.topdot.psam.message.protocol.PsamRspHeader;
import com.topdot.psam.message.protocol.request.SystemEventRequest;
import com.topdot.psam.message.protocol.request.SystemEventRequestBody;
import com.topdot.psam.message.protocol.response.SystemEventResponse;

public class SystemEventExecutor extends AbstractMessageExecutor<SystemEventRequest, SystemEventResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemEventExecutor.class);
	public SystemEventExecutor() {

	}

	@Override
	public SystemEventResponse execute(SystemEventRequest reqMsg) {
		LOGGER.info("系统事件上报...");
		SystemEventResponse rsp = new SystemEventResponse();

		PsamRspHeader rspHeader = mockRsp(reqMsg.getHeader());
		rsp.setHeader(rspHeader);
		SystemEventRequestBody body = reqMsg.getBody();
		LOGGER.info("事件描述:<{}>",body.getEventDescription());
		return rsp;
	}

}