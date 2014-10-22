/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.vo.Server.java
 * 所含类: Server.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月15日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.message.vo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>类名</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName SerialServer
 * @author zhangyongxin
 * @version 1.0
 */

public class Server {
	
	private final String id;
	
	private final String ip;
	
	private final int port;
	
	public Server(String id,String ip,int port) {
		this.id = id;
		this.ip = ip;
		this.port = port;
	}
	
	public Server(JSONObject jo) throws JSONException {
		this.id = jo.getString("Id");
		this.ip = jo.getString("Ip");
		this.port = jo.getInt("Port");
	}
	
	/**
	 * <p>获得串口服务器JSON Object对象</p>
	 * @param hasHeader:是否包含Reader信息
	 * @return JSONObject
	 * @see
	 */
	public JSONObject getJSONObject() throws JSONException {
		JSONObject jo = new JSONObject();
		jo.put("ID", id);
		jo.put("IP", ip);
		jo.put("Port", port);
		return jo;
	}

	public String getId() {
		return id;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}
}
