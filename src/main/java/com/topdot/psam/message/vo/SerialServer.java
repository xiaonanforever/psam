/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.psam.message.vo.SerialServer.java
 * 所含类: SerialServer.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月15日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.psam.message.vo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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

public class SerialServer {

	private final String id;

	private final String ip;

	private final int port;
	/** 伺服连接的读卡器列表 */
	private final List<Reader> readerList = new ArrayList<Reader>();

	public SerialServer(String id, String ip, int port) {
		this.id = id;
		this.ip = ip;
		this.port = port;
	}

	public SerialServer(JSONObject jo) throws JSONException {
		this.id = jo.getString("Id");
		this.ip = jo.getString("Ip");
		this.port = jo.getInt("Port");
		if (jo.has("ReaderInfoList")) {
			JSONArray readerInfoArray = jo.getJSONArray("ReaderInfoList");
			for (int i = 0; i < readerInfoArray.length(); i++) {
				addReader(new Reader((JSONObject) readerInfoArray.get(i)));
			}
		}
	}

	/**
	 * <p>获得串口服务器JSON Object对象</p>
	 * 
	 * @param hasHeader:是否包含Reader信息
	 * @return JSONObject
	 * @see
	 */
	public JSONObject getJSONObject(boolean hasReader) throws JSONException {
		JSONObject jo = new JSONObject();
		jo.put("ID", id);
		jo.put("IP", ip);
		jo.put("Port", port);
		if (hasReader) {
			JSONArray readerInfoList = new JSONArray();
			jo.put("ReaderInfoList", readerInfoList);
			for (Reader reader : readerList) {
				readerInfoList.put(reader.getJSONObject());
			}
		}
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

	public List<Reader> getReaderList() {
		return readerList;
	}

	public void addReader(Reader reader) {
		reader.setSerialServer(this);
		readerList.add(reader);
	}

	public void removeReader(Reader reader) {
		reader.setSerialServer(null);
		readerList.remove(reader);
	}
}
