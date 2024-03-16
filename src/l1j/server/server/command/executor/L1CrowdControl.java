package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CrowdControlNoti;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1CrowdControl implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1CrowdControl();
	}

	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	
	private L1CrowdControl() {}
	
//AUTO SRM: 	private static final S_SystemMessage MESSAGE = new S_SystemMessage("관리자가 행동에 제약을 부여하였습니다."); // CHECKED OK
	private static final S_SystemMessage MESSAGE = new S_SystemMessage(S_SystemMessage.getRefText(331), true);

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st	= new StringTokenizer(arg);
			String target		= st.nextToken();
			
			boolean teleport_impossible = false;
			boolean block_teleport = false;
			boolean block_change_equip = false;
			boolean block_invisibillity = false;
			boolean block_attack = false;
			boolean block_use_spell = false;
			boolean block_use_item_without_potion = false;
			
			if (st.hasMoreTokens()) {
				teleport_impossible = block_teleport = st.nextToken().equals("1");
			}
			if (st.hasMoreTokens()) {
				block_change_equip = st.nextToken().equals("1");
			}
			if (st.hasMoreTokens()) {
				block_invisibillity = st.nextToken().equals("1");
			}
			if (st.hasMoreTokens()) {
				block_attack = st.nextToken().equals("1");
			}
			if (st.hasMoreTokens()) {
				block_use_spell = st.nextToken().equals("1");
			}
			if (st.hasMoreTokens()) {
				block_use_item_without_potion = st.nextToken().equals("1");
			}
			switch (target) {
			//case "전체":
			case "all":		
				for (L1PcInstance user : L1World.getInstance().getAllPlayers()) {
					if (user == null || user.getNetConnection() == null) {
						continue;
					}
					user.sendPackets(new S_CrowdControlNoti(user.getId(), teleport_impossible, block_teleport, block_change_equip, block_invisibillity, block_attack, block_use_spell, block_use_item_without_potion), true);
					user.sendPackets(MESSAGE);
				}
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(String.format("전체 유저의 의 행동을 제약하였습니다.\r\nblock_teleport : %b\r\nblock_change_equip : %b\r\nblock_invisibillity : %b\r\nblock_attack : %b\r\nblock_use_spell : %b\r\nblock_use_item_without_potion : %b",  block_teleport, block_change_equip, block_invisibillity, block_attack, block_use_spell, block_use_item_without_potion)), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(String.format(S_SystemMessage.getRefText(332) + " %b " + "\r\nblock_change_equip : " + " %b " + "\r\nblock_invisibillity : " + " %b " + "\r\nblock_attack : " + " %b " + "\r\nblock_use_spell : " + " %b " + "\r\nblock_use_item_without_potion : " + " %b ",  block_teleport, block_change_equip, block_invisibillity, block_attack, block_use_spell, block_use_item_without_potion), true), true);
				return true;
			default:
				L1PcInstance user	= L1World.getInstance().getPlayer(target);
				if (user == null) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("접속중인 유저가 아닙니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(333), true), true);
					return false;
				}
				user.sendPackets(new S_CrowdControlNoti(user.getId(), teleport_impossible, block_teleport, block_change_equip, block_invisibillity, block_attack, block_use_spell, block_use_item_without_potion), true);
				user.sendPackets(MESSAGE);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(String.format("'%s'의 행동을 제약하였습니다.\r\nblock_teleport : %b\r\nblock_change_equip : %b\r\nblock_invisibillity : %b\r\nblock_attack : %b\r\nblock_use_spell : %b\r\nblock_use_item_without_potion : %b",  target, block_teleport, block_change_equip, block_invisibillity, block_attack, block_use_spell, block_use_item_without_potion)), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(String.format("'" + " %s " + S_SystemMessage.getRefText(334) + " %b " + "\r\nblock_change_equip : " + " %b " + "\r\nblock_invisibillity : " + " %b " + "\r\nblock_attack : " + " %b " + "\r\nblock_use_spell : " + " %b " + "\r\nblock_use_item_without_potion : " + " %b ",  target, block_teleport, block_change_equip, block_invisibillity, block_attack, block_use_spell, block_use_item_without_potion), true), true);
				return true;
			}
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭터명/전체] [block_teleport] [block_equip] [block_invis] [block_attack] [block_spell] [block_item]\n[0: false, 1: true]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(335), true), true);
			return false;
		}
	}
	
	
}


