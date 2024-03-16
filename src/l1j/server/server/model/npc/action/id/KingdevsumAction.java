package l1j.server.server.model.npc.action.id;

import l1j.server.Config;
import l1j.server.server.GameServerSetting;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class KingdevsumAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new KingdevsumAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private KingdevsumAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return devliZone(pc, s);
	}
	
	private String devliZone(L1PcInstance pc, String s){
		if (pc.getLevel() < Config.DUNGEON.DEVIL_KING_LIMIT_LEVEL) {
			//pc.sendPackets(new S_SystemMessage("레벨 " + Config.DUNGEON.DEVIL_KING_LIMIT_LEVEL + " 이상만 입장할 수 있습니다."), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(104), String.valueOf(Config.DUNGEON.DEVIL_KING_LIMIT_LEVEL)), true);
			return null;
		}
		if (s.equalsIgnoreCase("b")) {
			if (!GameServerSetting.DEVIL_ZONE) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("악마왕의 영토가 아직 열리지않았습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1137), true), true);
				return null;
			}
			L1Location loc = new L1Location(32723, 32800, 5167).randomLocation(5, true);
			pc.getTeleport().start(loc, pc.getMoveState().getHeading(), true);
			loc = null;
			pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("열린시각으로부터 60분동안 입장이 가능합니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1138), true), true);
			return StringUtil.EmptyString;
		}
		return null;
	}
}


