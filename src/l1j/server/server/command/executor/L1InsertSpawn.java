package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.NpcSpawnTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.model.L1Spawn;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.L1SpawnUtil;

public class L1InsertSpawn implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1InsertSpawn();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1InsertSpawn() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		String msg = null;

		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String type = tok.nextToken();
			int npcId = Integer.parseInt(tok.nextToken().trim());
			L1Npc template = NpcTable.getInstance().getTemplate(npcId);

			if (template == null) {
				//msg = "해당 NPC가 발견되지 않습니다.";
				msg = "The NPC was not found.";
				return false;
			}
			L1Spawn spawn = null;
			switch (type) {
			//case "몹":
			case "mob":
				if (!template.getImpl().matches(L1MonsterInstance.MONSTER_IMPLEMENTS_REGEX)) {
					//msg = "지정한 NPC는 L1Monster가 아닙니다.";
					msg = "The specified NPC is not an L1Monster.";
					return false;
				}
				spawn = SpawnTable.getInstance().storeSpawn(pc, template);
				break;
			//case "엔":
			case "npc":
				if (template.getImpl().matches(L1MonsterInstance.MONSTER_IMPLEMENTS_REGEX)) {
					//msg = "지정한 NPC는 L1Monster입니다.";
					msg = "The specified NPC is L1Monster.";
					return false;
				}
				spawn = NpcSpawnTable.getInstance().storeSpawn(pc, template);
				break;
			default:
				//msg = cmdName + " [몹,엔] [NPCID] 라고 입력해 주세요. ";
				msg = cmdName + "Please enter [mob,n] [NPCID]. ";
				return false;
			}
			if (spawn == null) {
				//msg = "L1Spawn 데이터를 취득하지 못하였습니다.";
				msg = "Failed to acquire L1Spawn data.";
				return false;
			}
			L1SpawnUtil.spawn(spawn, pc, npcId);
			msg = new StringBuilder().append("ID(").append(spawn.getId())
					.append(") DESC(").append(template.getDesc())
					.append(") NPCID(").append(npcId)
					//.append(") 를 DB에 추가했습니다.").toString();
					.append(") has been added to the DB.").toString();
			return true;
		} catch (Exception e) {
			//msg = cmdName + " [몹,엔] [NPCID] 라고 입력해 주세요. ";
			msg = cmdName + "Please enter [mob,n] [NPCID]. ";
		} finally {
			if (msg != null) {
				pc.sendPackets(new S_SystemMessage(msg, true), true);
			}
		}
		return false;
	}
	
}

