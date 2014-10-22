/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.exception.ErrorCode.java
 * 所含类: ErrorCode.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日7 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.exception;


/**
 * <p>错误定义</p>
 * <p>外部系统调用时需要处理的错误或者错误类型</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public final class ErrorDefination implements Cloneable {

	public static final ErrorDefination NO_ERROR;
	public static final ErrorDefination ERROR_SERVER;
	public static final ErrorDefination ERROR_TRANSACTION;
	public static final ErrorDefination ERROR_CONNECT;
	public static final ErrorDefination ERROR_REQ;
	public static final ErrorDefination ERROR_REQ_TIMEOUT;
	public static final ErrorDefination ERROR_REQ_CHANNEL;
	public static final ErrorDefination ERROR_RSP_NOTIME;
	public static final ErrorDefination ERROR_REQ_NOTIME;
	public static final ErrorDefination ERROR_RSP_CHANNEL;
	public static final ErrorDefination ERROR_UNNKONW;
	public static final ErrorDefination ERROR_MSG_FILTER;
	// private static final ErrorDefination[] ENUM$VALUES;
	static {
		NO_ERROR = new ErrorDefination(0, "没有错误");
		ERROR_SERVER = new ErrorDefination(10, "服务器异常（总称）");
		ERROR_TRANSACTION = new ErrorDefination(20, "交易异常类型,代表了集成控制平台返回所有异常");
		ERROR_CONNECT = new ErrorDefination(70, "服务连接异常");
		ERROR_REQ = new ErrorDefination(80, "请求异常");
		ERROR_REQ_TIMEOUT = new ErrorDefination(81, "请求超时异常");
		ERROR_REQ_CHANNEL = new ErrorDefination(82, "请求通道异常");
		ERROR_REQ_NOTIME = new ErrorDefination(83, "请求失效");
		ERROR_RSP_CHANNEL = new ErrorDefination(92, "响应通道异常");
		ERROR_RSP_NOTIME = new ErrorDefination(93, "响应失效");
		ERROR_UNNKONW = new ErrorDefination(-1, "未知错误!");
		ERROR_MSG_FILTER = new ErrorDefination(60, "消息过滤错误!");
		// ENUM$VALUES = (new ErrorDefination[] { NO_ERROR, ERROR_CONNECT, ERROR_REQUEST, ERROR_UNNKONW });
	}

	private int code = 90;
	private String name;

	public ErrorDefination(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	//
	public String getName() {
		return name;
	}

	public ErrorDefination clone() {
		ErrorDefination ed = new ErrorDefination(this.code, this.name);
		return ed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErrorDefination other = (ErrorDefination) obj;
		if (code != other.code)
			return false;
		return true;
	}
}
