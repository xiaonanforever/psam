/**============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.vo.Reader.java
 * 所含类: Reader.java
 * 修改记录：
 * 日期                           作者                            内容
 * =============================================================
 * 2014年1月15日       zhangyongxin       创建文件，实现基本功能
 * ============================================================*/

package com.topdot.psam.message.vo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>Reader</p>
 *
 * <p>读卡器</p>
 *
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @ClassName Reader
 * @author zhangyongxin
 * @version 1.0
 * 
 */

public class Reader {

	/**读卡器Id*/
	private final String id;
	
	/**读卡器状态*/
	private int statuts;
	
	/**是否打开*/
	private boolean opened;
	
	/**所属串口服务器*/
	private SerialServer serialServer;
	
	public Reader() {
		this.id = null;
	}
	
	public Reader(String id) {
		this.id = id;
	}
	
	public Reader(JSONObject jsonObject) throws JSONException {
		this.id = jsonObject.getString("Id");
		this.statuts = jsonObject.getInt("Statuts");
		this.opened = jsonObject.getBoolean("Opened");
	}
	
	public JSONObject getJSONObject() throws JSONException {
		JSONObject jo = new JSONObject();
		jo.put("Id", id);
		jo.put("Statuts", statuts);
		jo.put("Opened", opened);
		return jo;
	}

	public String getId() {
		return id;
	}

	public int getStatuts() {
		return statuts;
	}

	public void setStatuts(int statuts) {
		this.statuts = statuts;
	}

	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
	}

	public SerialServer getSerialServer() {
		return serialServer;
	}

	public void setSerialServer(SerialServer serialServer) {
		this.serialServer = serialServer;
	}

	
}
