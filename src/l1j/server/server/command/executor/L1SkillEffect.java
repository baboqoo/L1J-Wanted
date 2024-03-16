package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1SkillEffect implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1SkillEffect();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1SkillEffect() {}
	
	static class SkillEffect implements Runnable {
		private L1PcInstance _pc;
		private int _first;
		private int _last;
		
		public SkillEffect(L1PcInstance pc, int sprid, int count) {
			_pc		= pc;
			_first	= sprid;
			_last	= count;
		}

		@Override
		public void run() {
			for (int i = _first; i <= _last; i++) {
				try {
					_pc.sendPackets(new S_SystemMessage("spriteId : " + i), true);
					_pc.sendPackets(new S_Effect (_pc.getId(),i), true);			
					Thread.sleep(1000);
				} catch (Exception exception) {
					break;
				}
			}
		}
	}
	
	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			StringTokenizer st = new StringTokenizer(arg);
			int first = Integer.parseInt(st.nextToken(), 10);
			
			int last = 0;
			if (st.hasMoreTokens()) {
				last = Integer.parseInt(st.nextToken(), 10);
			}
			
			if (last > 0 && last > first) {
				SkillEffect spr = new SkillEffect(pc, first, last);
				GeneralThreadPool.getInstance().execute(spr);
				spr = null;
			} else {
				pc.send_effect(first);
			}
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(arg + " [spriteId] 라고 입력해 주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(676), true), true);
			return false;
		}
	}
}


