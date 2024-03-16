package l1j.server.server.model.npc.action.id;

import l1j.server.common.data.ChatType;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.StringUtil;

public class BossKeyAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new BossKeyAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private BossKeyAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return sesylia(pc, s, obj);
	}
	
	private String sesylia(L1PcInstance pc, String s, L1Object obj){
		L1NpcInstance npc = (L1NpcInstance) obj;
		int monsterId = 0;
		String msg = StringUtil.EmptyString;
		// S_SystemMessage.getRefText(113)
		if (s.equalsIgnoreCase("d") && pc.getInventory().consumeItem(80478, 1)) {
			monsterId = 45600; // $274
			//msg = "'흑기사 대장 커츠'가 중앙에 소환되었습니다.";
			msg = "'$274'" + S_SystemMessage.getRefText(113);
		} else if (s.equalsIgnoreCase("e") && pc.getInventory().consumeItem(80479, 1)) {
			monsterId = 45573; // $306
			//msg = "'바포메트'가 중앙에 소환되었습니다.";
			msg = "'$306'" + S_SystemMessage.getRefText(113);
		} else if (s.equalsIgnoreCase("Q") && pc.getInventory().consumeItem(80464, 1)) {
			monsterId = 45456; // $331
			//msg = "'네크로맨서'가 중앙에 소환되었습니다.";
			msg = "'$331'" + S_SystemMessage.getRefText(113);
		} else if (s.equalsIgnoreCase("S") && pc.getInventory().consumeItem(80465, 1)) {
			monsterId = 45601; // $371
			//msg = "'데스나이트'가 중앙에 소환되었습니다.";
			msg = "'$371'" + S_SystemMessage.getRefText(113);
		} else if (s.equalsIgnoreCase("A") && pc.getInventory().consumeItem(80466, 1)) {
			monsterId = 900076; // $30231 $11514
			//msg = "'하딘의분신'이 중앙에 소환되었습니다.";
			msg = "'$30231 $11514'" + S_SystemMessage.getRefText(113);
		} else if (s.equalsIgnoreCase("B") && pc.getInventory().consumeItem(80467, 1)) {
			monsterId = 900070; // $30231 $2039
			//msg = "'흑마법사'가 중앙에 소환되었습니다.";
			msg = "'$30231 $2039'" + S_SystemMessage.getRefText(113);
		} else if (s.equalsIgnoreCase("C") && pc.getInventory().consumeItem(80450, 1)) {
			monsterId = 45649; // $1175
			//msg = "'데몬'이 중앙에 소환되었습니다.";
			msg = "'$1175'" + S_SystemMessage.getRefText(113);
		} else if (s.equalsIgnoreCase("D") && pc.getInventory().consumeItem(80451, 1)) {
			monsterId = 45685; // $3407
			//msg = "'타락'이 중앙에 소환되었습니다.";
			msg = "'$3407'" + S_SystemMessage.getRefText(113);
		}
		if (monsterId > 0) {
			L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), 5, monsterId, 0, 3600000 , 0);
			pc.sendPackets(new S_NpcChatPacket(npc, msg, ChatType.CHAT_NORMAL), true);
		}
		return "bosskey10";
	}
}

