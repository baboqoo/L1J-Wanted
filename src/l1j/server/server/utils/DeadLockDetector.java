package l1j.server.server.utils;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeadLockDetector extends Thread{
	private static Logger _log = Logger.getLogger(DeadLockDetector.class.getName());

	private final ThreadMXBean tmx;
	
	private static DeadLockDetector _instance;
	public static DeadLockDetector getInstance(){
		if (_instance == null) {
			_instance = new DeadLockDetector();
		}
		return _instance;
	}

	public DeadLockDetector(){
		super("DeadLockDetector");
		tmx = ManagementFactory.getThreadMXBean();
	}

	@Override 
	public final void run(){
		boolean deadlock = false;
		while (!deadlock) {
			try {
				long[] ids = tmx.findDeadlockedThreads();

				if (ids != null) {
					deadlock = true;
					ThreadInfo[] tis = tmx.getThreadInfo(ids,true,true);
					String info = "DeadLock Found!\n";
					for (ThreadInfo ti : tis) {
						info += ti.toString();
					}
					for (ThreadInfo ti : tis) {
						LockInfo[] locks = ti.getLockedSynchronizers();
						MonitorInfo[] monitors = ti.getLockedMonitors();
						if (locks.length == 0 && monitors.length == 0) {
							continue;
						}

						ThreadInfo dl = ti;
						info += "Java-level deadlock:\n";
						info += "\t"+dl.getThreadName()+" is waiting to lock "+dl.getLockInfo().toString()+" which is held by "+dl.getLockOwnerName()+"\n";
						while ((dl = tmx.getThreadInfo(new long[]{dl.getLockOwnerId()},true,true)[0]).getThreadId() != ti.getThreadId()) {
							info += "\t"+dl.getThreadName()+" is waiting to lock "+dl.getLockInfo().toString()+" which is held by "+dl.getLockOwnerName()+"\n";
						}
					}
					
					//System.out.println("!!!!!!!!!!!!!!!! 데드락발생  !!!!!!!!!!"+info);
					System.out.println("!!!!!!!!!!!!!!! Deadlock occurred !!!!!!!!!!"+info);
					_log.warning(info);
//					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
//						pc.sendPackets(new S_SystemMessage("데드락된 쓰레드가 발견되었습니다 서버를 재시작합니다."), true);
//						try{
//							pc.save();
//							pc.saveInventory();
//						}catch(Exception e){}
//					}
//					GameServer.getInstance().shutdownWithCountdown(10);
				}
				Thread.sleep(1000); 
			} catch(Exception e) {
				_log.log(Level.WARNING,"DeadLockDetector: ",e);
			}
		}
	}
}

