package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Burf implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Burf();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Burf() {}

	static class Burfskill implements Runnable {
		private L1PcInstance _pc = null;
		private int _sprid;
		private int _count;
		
		public Burfskill(L1PcInstance pc, int sprid, int count) {
			_pc = pc;
			_sprid = sprid;
			_count = count;
		}

		@Override
		public void run() {
			for (int i = 0; i < _count; i++) {
				try {
					Thread.sleep(500);
					int num = _sprid + i;
//AUTO SRM: 					_pc.sendPackets(new S_SystemMessage("스킬번호: "+num), true); // CHECKED OK
					_pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(234) + num, true), true);
					_pc.send_effect(_sprid+i);
				} catch (Exception exception) {
					break;
				}
			}

		}

	}
	
	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int sprid = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);
			
			Burfskill spr = new Burfskill(pc, sprid, count);
			GeneralThreadPool.getInstance().execute(spr);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [castgfx] 라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(235), true), true);
			return false;
		}
	}
}


