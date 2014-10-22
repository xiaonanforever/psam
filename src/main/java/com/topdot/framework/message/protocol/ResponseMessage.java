/**
 * 
 */
package com.topdot.framework.message.protocol;

import java.util.Date;

import com.topdot.framework.message.exception.ErrorDefination;
import com.topdot.framework.message.exception.ErrorMessage;
import com.topdot.framework.message.exception.MessageFormateException;

/**
 * @author zhangyongxin
 */
public class ResponseMessage extends Message<MessageHeader, MessageBody> {

	/** Json Body内容在Json字符串中的位置 */
	protected int bodyIndex;

	/** 收到最原始的字符串 */
	private String jsonStr;

	/** 错误消息 */
	private ErrorMessage errorMessage;

	public ResponseMessage() {
	}

	/**
	 * 根据JSONStr构建Response
	 * 
	 * @param jsonStr
	 * @throws MessageFormateException
	 */
	public ResponseMessage(String jsonStr) throws MessageFormateException {
		this(jsonStr, true);
	}

	public ResponseMessage(String jsonStr, boolean parseBody) throws MessageFormateException {
		this.jsonStr = jsonStr;
		bodyIndex = jsonStr.indexOf(MESSAGE_SEPERATE);
		if (bodyIndex >= 0) {
			header = new MessageHeader(jsonStr.substring(0, bodyIndex));
			if (parseBody && (bodyIndex + 1) < jsonStr.length()) {
				body = new MessageBody(jsonStr.substring(bodyIndex + 1));
			}
			includeBody = true;
		} else {
			header = new MessageHeader(jsonStr);
		}
		if (!header.success()) {
			ErrorDefination ed = ErrorDefination.ERROR_TRANSACTION.clone();
			ed.setCode(header.getErrorCode());
			errorMessage = new ErrorMessage(ed);
			if (body != null) {
				errorMessage.setDetail(body.getErrorMessage());
			}
		}
	}

	public ResponseMessage parse(String jsonStr) throws MessageFormateException {
		return new ResponseMessage(jsonStr);
	}

	/**
	 * <p>生成消息对应的JSON格式字符串 </p>
	 * 
	 * @return
	 * @return String
	 * @throws MessageFormateException
	 */
	public String buildReqMessageJSONStr() throws MessageFormateException {
		StringBuilder messageStrBuilder = new StringBuilder(header.buildReqJSONObject().toString());
		if (body != null) {
			messageStrBuilder.append(MESSAGE_SEPERATE);
			messageStrBuilder.append(body.buildReqJSONObject().toString());
		}
		return messageStrBuilder.toString();
	}
	
	public String getJsonStr() {
		if (this.jsonStr == null || "".equals(this.jsonStr)) {
			try {
				return buildReqMessageJSONStr();
			} catch (MessageFormateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;

	}

	@Override
	public Date getCreateDate() {
		return header.getCreateDate();
	}

	@Override
	public String toString() {
		return header.getJsonMsg() + "*" + body.getJsonMsg();
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean success() {
		if (errorMessage == null) {
			return true;
		}
		return errorMessage.success();
	}
}
