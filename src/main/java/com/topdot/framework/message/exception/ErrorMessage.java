/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.exception.ErrorMessage.java
 * 所含类: ErrorMessage.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日7 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.exception;

/**
 * <p>响应错误消息</p>
 * <p>输出给调用者，封装了服务端返回的ErrorCode和ErrorMessage</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class ErrorMessage {
	
	/**服务器异常总称*/
	public static final ErrorMessage ERROR_SERVER = new ErrorMessage(ErrorDefination.ERROR_SERVER);
	
	/** 错误代码 */
	private ErrorDefination errorDefination;

	/** 错误详细信息 */
	private String detail;

	public ErrorMessage() {
	}

	public ErrorMessage(ErrorDefination errorDefination) {
		this.errorDefination = errorDefination;
	}

	public ErrorDefination getErrorDefination() {
		return errorDefination;
	}

	public void setErrorDefination(ErrorDefination errorDefination) {
		this.errorDefination = errorDefination;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public boolean success() {
		return errorDefination.getCode() == ErrorDefination.NO_ERROR.getCode();
	}

	public String toString() {
		String err = "错误信息:[" + this.errorDefination.getCode() + "-" + this.getErrorDefination().getName() + "],详细信息:"
				+ this.detail;
		return err;
	}

}
