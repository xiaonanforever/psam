/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.exception.RequestException.java
 * 所含类: RequestException.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月18日0       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.framework.message.exception;



/**
 * <p>请求异常</p>
 * <p>同步、异步请求时使用</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * @author zhangyongxin
 * @version 1.0
 */

public class RequestException extends ErrorCodeException {

	private static final long serialVersionUID = -8900314577253089850L;

	public RequestException() {
		super("请求异常");
		setErrorDefination(ErrorDefination.ERROR_REQ);
	}

	/**
	 * @param message
	 */
	public RequestException(String message) {
		super(message);
		setErrorDefination(ErrorDefination.ERROR_REQ);
	}

	/**
	 * @param cause
	 */
	public RequestException(Throwable cause) {
		super(cause);
		setErrorMessage(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RequestException(String message, Throwable cause) {
		super(message, cause);
		setErrorMessage(cause);
	}
	
	private void setErrorMessage(Throwable cause){
		if(cause instanceof ErrorCodeException){
			ErrorMessage  em = ((ErrorCodeException) cause).getErrorMessage();
			if(em!=null){
				setErrorDefination(em.getErrorDefination());
			}
		}else{
			setErrorDefination(ErrorDefination.ERROR_REQ);
		}
	}
}
