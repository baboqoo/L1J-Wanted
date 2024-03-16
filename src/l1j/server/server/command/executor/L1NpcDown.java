package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.NotificationTable;
import l1j.server.server.datatables.NpcSpawnTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1BoardInstance;
import l1j.server.server.model.Instance.L1CastleGuardInstance;
import l1j.server.server.model.Instance.L1CataInstance;
import l1j.server.server.model.Instance.L1DwarfInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1GuardInstance;
import l1j.server.server.model.Instance.L1GuardianInstance;
import l1j.server.server.model.Instance.L1HousekeeperInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PeopleInstance;
import l1j.server.server.model.Instance.L1ScarecrowInstance;
import l1j.server.server.model.Instance.L1TeleporterInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1NpcDown implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1NpcDown();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1NpcDown() {}
	
//AUTO SRM: 	private static final S_SystemMessage COMMAND = new S_SystemMessage(".엔피씨다운 [몹/엔] [배치/삭제] 를 입력해주세요.");

	public boolean execute(L1PcInstance pc, String cmdName, String st) {
		try {
			StringTokenizer token	= new StringTokenizer(st);
			String type				= token.nextToken();
			String sub				= token.nextToken();
			switch(type){
			//case "몹":	downMonster(sub, pc);return true;
			case "mob":	downMonster(sub, pc, cmdName);return true;
			//case "엔":	downNpc(sub, pc);return true;
			case "npc":	downNpc(sub, pc, cmdName);return true;
			default:	
			  //pc.sendPackets(COMMAND);
			  pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(496), true), true);
			  return false;
			}
		} catch (Exception e) {
			//pc.sendPackets(COMMAND);
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(496), true), true);
			return false;
		}
	}
	
	private void downMonster(String sub, L1PcInstance pc, String cmdName){
		switch(sub){
		//case "배치":
		case "reload":
			SpawnTable.getInstance().reload1();
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("모든 몬스터가 배치되었습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(497), true), true);
			break;
		//case "삭제":
		case "delete":
			for (L1Object l1object : L1World.getInstance().getObject()) {
				if(l1object == null)continue;
				if(l1object instanceof L1MonsterInstance){
					L1MonsterInstance npc = (L1MonsterInstance)l1object;
					npc.setRespawn(false);
					npc.deleteMe();
				}
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("모든 몬스터가 삭제되었습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(498), true), true);
			break;
		default:
		  //pc.sendPackets(COMMAND);
		  pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(496), true), true);
		  break;
		}
	}
	
	private void downNpc(String sub, L1PcInstance pc, String cmdName){
		switch(sub){
		//case "배치":
		case "reload":
			NpcTable.reload();
			NpcSpawnTable.getInstance().reload1();
			NotificationTable.getInstance().reload();
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("모든 엔피씨가 배치되었습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(499), true), true);
			break;
		//case "삭제":
		case "delete":
			for(L1Object l1object : L1World.getInstance().getObject()){
				if(l1object == null)continue;
				if(l1object instanceof L1MerchantInstance 
						|| l1object instanceof L1PeopleInstance
						|| l1object instanceof L1TeleporterInstance
						|| l1object instanceof L1BoardInstance
						|| l1object instanceof L1CastleGuardInstance
						|| l1object instanceof L1CataInstance
						|| l1object instanceof L1DwarfInstance
						|| l1object instanceof L1FieldObjectInstance
						|| l1object instanceof L1GuardianInstance
						|| l1object instanceof L1GuardInstance
						|| l1object instanceof L1HousekeeperInstance
						|| l1object instanceof L1ScarecrowInstance) {
					L1NpcInstance npc = (L1NpcInstance) l1object;
					npc.setRespawn(false);
					npc.deleteMe();
				}
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("모든 엔피씨가 삭제되었습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(500), true), true);
			break;
		default:
		  //pc.sendPackets(COMMAND);
		  pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName + S_SystemMessage.getRefText(496), true), true);

		  break;
		}
	}
}

