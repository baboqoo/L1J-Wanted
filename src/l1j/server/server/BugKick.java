package l1j.server.server;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class BugKick {
	private static BugKick _instance;
	public static BugKick getInstance() {
		if (_instance == null) {
			_instance = new BugKick();
		}
		return _instance;
	}

	private BugKick() {}
	
//AUTO SRM: 	private static final S_SystemMessage msg = new S_SystemMessage("버그사용이 감지되었습니다."); // CHECKED OK
	private static final S_SystemMessage msg = new S_SystemMessage(S_SystemMessage.getRefText(68), true);

	public void KickPlayer(L1PcInstance pc){
		try {
			pc.getTeleport().start(32737, 32796, (short) 99, 5, true);
			pc.broadcastPacketWithMe(new S_Poison(pc.getId(), 2), true);// 동결 상태가 되었습니다.
		    pc.sendPackets(S_Paralysis.STURN_ON);
		    pc.getSkill().killSkillEffectTimer(L1SkillId.SHOCK_STUN);
		    pc.getSkill().setSkillEffect(L1SkillId.SHOCK_STUN, 86400000);//여기까지 스턴
		    pc.sendPackets(msg);
//AUTO SRM: 		    L1World.getInstance().broadcastServerMessage(String.format("\\fY버그사용자 [%s] 신고바람!!", pc.getName()), true); // CHECKED OK
		    L1World.getInstance().broadcastServerMessage(String.format(S_SystemMessage.getRefText(69) + " %s " + S_SystemMessage.getRefText(70), pc.getName()), true);
		} catch (Exception e) {
			//System.out.println(String.format("[%s] 버그사용자 등록 에러", pc.getName()));
			System.out.println(String.format("[%s] bug user registration error", pc.getName()));
		}
	}
}
	

