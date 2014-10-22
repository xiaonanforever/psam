/**
 * ============================================================
 * 版权： xxx 版权所有 (c) 2010 - 2014
 * 文件：com.topdot.framework.message.filter.impl.HttpInFilterImpl.java
 * 所含类: HttpInFilterImpl.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2014年1月18日 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message.filter.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.constants.HTTPConstants;
import com.topdot.framework.message.constants.MsgConstants;
import com.topdot.framework.message.exception.HttpParserException;
import com.topdot.framework.message.exception.MessageFilterException;
import com.topdot.framework.message.filter.Filter;
import com.topdot.framework.message.filter.FilterChain;
import com.topdot.framework.message.filter.FilterConfig;
import com.topdot.framework.message.filter.FilterMessage;
import com.topdot.framework.util.Compress;

/**
 * <p>解析HTTP报文Filter</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: xxx</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class HttpInFilterImpl implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpInFilterImpl.class);

	int flag = 0;// 检测网络流进入和处理完毕标志
	private long start;
	private long random;

	SocketChannel clntChan;

	public HttpInFilterImpl() {
	}

	/**
	 * <p>读取头部 </p>
	 * 
	 * @return 头部消息
	 * @return String
	 * @throws HttpParserException
	 */
	private String readHeader() throws HttpParserException {
		// LOGGER.debug("读取HTTP 头部信息,socket关闭状态:{},{}.....",socket.isClosed(),socket.isInputShutdown());
		random = Math.round(Math.random() * Integer.MAX_VALUE);
		StringBuilder header = new StringBuilder();
		try {
			flag = 0;
			// 已经连接的回车换行数 crlfNum=4为头部结束
			int crlfNum = 0;
			String head = "";
			ByteBuffer readBuf = ByteBuffer.allocate(1);
			int readNumber;
			while ((readNumber = clntChan.read(readBuf)) != -1) { // 读取头部
				if (readNumber == 0) {
					continue;// 自旋
				}
				readBuf.flip();
				byte c = readBuf.get();
				readBuf.clear();
				header.append((char) c); // byte数组相加
				if (flag == 0) {
					flag = 1;
					LOGGER.info("[{}]:检查到数据进入.....{}", random, System.currentTimeMillis());
					start = System.currentTimeMillis();
				}
				if (c == HTTPConstants.CRLF_13 || c == HTTPConstants.CRLF_10) {
					if ((++crlfNum) == 4) {
						head = header.toString().toUpperCase();
						LOGGER.info("[{}]:头部数据机收完毕.{}", random, head);
						LOGGER.debug("检测到新数据进入,头部信息:{}", head);
						return head;
					}
				} else {
					crlfNum = 0;
				}
			}
			clntChan.close();
			throw new HttpParserException("Socket Server输入流已断开!");
		} catch (IOException e) {
			if (!clntChan.isOpen()) {
				LOGGER.info("输入通道或者Socket关闭,{},{}", clntChan.isConnected(), clntChan.isConnectionPending());
				return "";
			}
			LOGGER.warn("解析输入信息时发生异常...", e);
			try {
				clntChan.close();
			} catch (IOException e1) {
				LOGGER.info("关闭输入通道发生异常,输入通道关闭:{}或者Socket关闭:{},{}", clntChan.isConnected(),
						clntChan.isConnectionPending());
				return "";
			}
			throw new HttpParserException("读取头部信息异常!", e);
		}
	}

	/**
	 * <p>读取定长数据</p>
	 * 
	 * @param size
	 * @return
	 * @throws HttpParserException
	 * @return String
	 */
	private String readLenData(int size) throws HttpParserException { //
		ByteBuffer readBuf = ByteBuffer.allocate(size);
		try {
			while (readBuf.hasRemaining() && clntChan.read(readBuf) != -1);
			return new String(readBuf.array(), "UTF-8");
		} catch (IOException e) {
			throw new HttpParserException("读取JSON数据长度!", e);
		}
	}

	/**
	 * <p>获取HTTP包头数据</p>
	 * 
	 * @param key
	 * @return
	 * @return String
	 */
	private String getAttribute(String headContent, String key) {
		int index = headContent.indexOf(key.toUpperCase());
		byte[] requst = headContent.getBytes();
		if (index != -1) { // 从index+1起至/r/n
			StringBuilder sb = new StringBuilder();
			int i = index + key.length() + 1;
			while (true) {
				if (requst[i] == (byte) 13 || requst[i] == (byte) 10) {
					break;
				}
				sb.append((char) requst[i]);
				i++;
			}
			return sb.toString().trim();
		}
		return null;
	}

	@Override
	public void doFilter(FilterMessage msg, FilterChain chain) throws MessageFilterException {
		LOGGER.info("HttpInFilter再次进入....");
		FilterConfig filterConfig = chain.getConfig();
		try {
			clntChan = (SocketChannel) filterConfig.getAttrValue(MsgConstants.SOCKET_CHANNL);
		} catch (Exception e) {
			throw new MessageFilterException("获取Socket InputStream异常!", e);
		}

		String headContent;
		try {
			headContent = readHeader();
		} catch (HttpParserException e) {
			throw new MessageFilterException("解析Json Head,不符合HTTP协议格式!", e);
		}
		if (headContent == null || "".equals(headContent.trim())) {
			throw new MessageFilterException("Json Head信息为空!");
		}
		int cl = 0;
		String value = getAttribute(headContent, HTTPConstants.CONTENT_LENGTH_ATTR);
		if (value != null) {
			cl = Integer.parseInt(value.trim());
		}
		String bodyContent = "{}";
		if (cl > 0) {
			try {
				bodyContent = readLenData(cl);// reader.readLine();
			} catch (HttpParserException e) {
				throw new MessageFilterException("读取HTTP包体数据异常!", e);
			}
		}
		LOGGER.debug("[{}]Body采集完毕:{}.", random, bodyContent);
		String compress = getAttribute(headContent, HTTPConstants.COMPRESS_ATTR);
		if (compress == null || Compress.SOURCE.equals(compress)) {
			msg.addAttr(MsgConstants.IS_COMPRESS, MsgConstants.NO);
		} else {
			msg.addAttr(MsgConstants.IS_COMPRESS, MsgConstants.YES);
		}
		msg.setFilterMsg(bodyContent);
		LOGGER.debug("[{}]网络流处理完毕<<<<<<<<<<<<<<<<<<<花费时间:{}ms", random, System.currentTimeMillis() - start);
		chain.doFilter(msg);
	}

	@Override
	public String getName() {
		return "HttpInFilterImpl";
	}
}
