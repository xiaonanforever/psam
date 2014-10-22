/**
 * ============================================================
 * 版权： 中体彩 版权所有 (c) 2010 - 2014
 * 文件：cn.com.cslc.framework.message.LoadRunTest.java
 * 所含类: LoadRunTest.java
 * 修改记录：
 * 日期 作者 内容
 * =============================================================
 * 2013-2-26 zhangyongxin 创建文件，实现基本功能
 * ============================================================
 */

package com.topdot.framework.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdot.framework.message.exception.ConnectionException;
import com.topdot.framework.message.exception.RequestException;
import com.topdot.psam.client.ClusterMessageService;
import com.topdot.psam.message.protocol.PsamReqHeader;
import com.topdot.psam.message.protocol.request.TicketInfoRequest;
import com.topdot.psam.message.protocol.request.TicketInfoRequestBody;

/**
 * <p>压力测试以及集成整体测试</p>
 * <p>类用途详细说明</p>
 * <p>Copyright: 版权所有 (c) 2010 - 2014</p>
 * <p>Company: 中体彩</p>
 * 
 * @author zhangyongxin
 * @version 1.0
 */

public class LoadRunTest extends MessageTestCase {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoadRunTest.class);
	private final int repeatTime = 1000;
	private static final ExecutorService pool = Executors.newCachedThreadPool();
	private CyclicBarrier barrier;

	// 最短执行时间
	private static long shortExcuteTime = Long.MAX_VALUE;
	private static String shortExcuteName;
	// 最近三次执行时间，用于判断后续测试逻辑,如果连续三次走低，继续增加会话，如果连续三次走高，则停止增加会话
	private List<Long> excuteTimes = new ArrayList<Long>();

	public LoadRunTest() {
		this("");
	}

	public LoadRunTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(LoadRunTest.class);
	}

	/**
	 * <p>查询出最优执行路径</p>
	 * @return void
	 */
	private void findOpti() throws InterruptedException {
		int sessionNum = 1;
		// 需要预热
		while (true) {
			if (excuteTimes.size() >= 3) {
				int index = excuteTimes.size() - 1;
				if (excuteTimes.get(index) > excuteTimes.get(index - 1)
						&& excuteTimes.get(index - 1) > excuteTimes.get(index - 2)) {
					return;// 耗费时间连续两次变长
				} else {
					singleExcute(repeatTime, sessionNum++);
				}
			} else {// 继续增加会话
				singleExcute(repeatTime, sessionNum++);
			}
		}
	}

	/**
	 * 多个客户端会话测试
	 */
	public void testMultiClientSession() {
		LOGGER.info("testMultiClientSession...");
		try {
			findOpti();
		} catch (InterruptedException e) {
			LOGGER.info("执行线程被中止...",e);
		}
	}

	/**
	 * <p>方法描述</p>
	 * 
	 * @param repeatTimes 总体需要执行请求数
	 * @param excutorNum 执行者数目
	 * @throws InterruptedException
	 */
	private long singleExcute(int repeatTimes, int excutorNum) throws InterruptedException {
		String excuteMode = "任务数:[" + repeatTimes + "]-会话数:["+ excutorNum + "]......";
		LOGGER.info(excuteMode);
		barrier = new CyclicBarrier(excutorNum + 1);
		List<Future<TimePair>> restults = new ArrayList<Future<TimePair>>(excutorNum);
		for (int i = 0; i < excutorNum; i++) {
			restults.add(pool.submit(new Task("Session" + i, repeatTimes / excutorNum)));
		}

		try {
			barrier.await();// 等待完成集结
		} catch (BrokenBarrierException e) {
			LOGGER.error("主线程Barrier.await",e);
		}
		long duration = 0;
		try {
			long[] startTimes = new long[excutorNum];
			for (int i = 0; i < excutorNum; i++) {
				startTimes[i] = restults.get(i).get().getStartTime();
			}
			long[] endTimes = new long[excutorNum];
			for (int i = 0; i < excutorNum; i++) {
				endTimes[i] = restults.get(i).get().getEndTime();
			}
			duration = NumberUtils.max(endTimes) - NumberUtils.min(startTimes);
			excuteTimes.add(duration);
			if (duration < shortExcuteTime) {
				shortExcuteName = excuteMode;
				shortExcuteTime = duration;
			}
			LOGGER.info("花费时间:[{}]", duration);
			LOGGER.info("目前执行最短时间模式->{}-{}", shortExcuteTime, shortExcuteName);
		} catch (ExecutionException e) {
			LOGGER.error("获取Future Result",e);
		}
		return duration;
	}

	class TimePair {
		private long startTime;
		private long endTime;

		public TimePair(long startTime, long endTime) {
			super();
			this.startTime = startTime;
			this.endTime = endTime;
		}

		public long getStartTime() {
			return startTime;
		}

		public long getEndTime() {
			return endTime;
		}
	}

	class Task implements Callable<TimePair> {
		private final int batchNum;
		private String name;

		public Task(String name, int batchNum) {
			this.name = name;
			this.batchNum = batchNum;
		}

		@Override
		public TimePair call() {
			long startTime;
			long endTime;
			try {
				startTime = System.currentTimeMillis();
				for (int i = 0; i < batchNum; i++) {
					testCall(clusterService);
				}
				endTime = System.currentTimeMillis();
				long duration = endTime - startTime;
				LOGGER.info("{}花费时间:{}ms", this.name, duration);
				return new TimePair(startTime, endTime);
			} catch (Exception e) {
				LOGGER.error("Task执行异常!",e);
			}finally{
				try {
					barrier.await();
				} catch (Exception e) {
					LOGGER.error("Barrier.await执行异常!",e);
				}
			}
			return null;
		}

	}

	private void testCall(ClusterMessageService clusterService) throws ConnectionException, RequestException {
		TicketInfoRequest req = new TicketInfoRequest();
		req.setHeader(mockReq());
		TicketInfoRequestBody body = req.getBody();
		body.setTransactionSn("TransactionSn");
		body.setAccountNo("AccountNo");
		body.setGameType(1);
		body.setRequestTime(new Date());
		clusterService.call(req);
	}

	public static PsamReqHeader mockReq() {
		PsamReqHeader header = new PsamReqHeader();
		// 基础头部数据
		// header.setSequenceId(1);
		// header.setCommandType(95);
		// header.setCommandId(950001);
		// header.setSystemId(10);// 手机即开

		// TxMessageHeader
		// header.setTerminalNo("12");
		// header.setTerminalId(12);
		// header.setTerminalIp("127.0.0.1");
		// header.setProvinceId(12);

		// TxRspMessageHeader
		// header.setVersionNo(12);
		header.setSn("SN");
		return header;
	}

	public static Thread[] findAllThreads() {
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		ThreadGroup topGroup = group;
		// 遍历线程组树，获取根线程组
		while (group != null) {
			topGroup = group;
			group = group.getParent();
		}
		// 激活的线程数加倍
		Thread[] slackList = new Thread[topGroup.activeCount() * 2];
		// 获取根线程组的所有线程
		int actualSize = topGroup.enumerate(slackList);
		// copy into a list that is the exact size
		Thread[] list = new Thread[actualSize];
		System.arraycopy(slackList, 0, list, 0, actualSize);

		return list;
	}

	public static void main(String[] args) {
		for (Thread t : findAllThreads()) {
			LOGGER.info("Thread Name:{}", t.getName());
		}
	}

}
