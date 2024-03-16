package l1j.server.server.command.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import l1j.server.server.datatables.NpcSpawnTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.model.L1Spawn;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class L1ToSpawn implements L1CommandExecutor {
	private static final Map<Integer, Integer> _spawnId = new HashMap<Integer, Integer>();

	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ToSpawn();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ToSpawn() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null || pc.getNetConnection() == null) {
				return false;
			}
			if (!_spawnId.containsKey(pc.getId())) {
				_spawnId.put(pc.getId(), 0);
			}
			int id = _spawnId.get(pc.getId());
			if (arg.isEmpty() || arg.equals(StringUtil.PlusString)) {
				id++;
			} else if (arg.equals(StringUtil.MinusString)) {
				id--;
			} else {
				StringTokenizer st = new StringTokenizer(arg);
				id = Integer.parseInt(st.nextToken());
			}
			L1Spawn spawn = NpcSpawnTable.getInstance().getTemplate(id);
			if (spawn == null) {
				spawn = SpawnTable.getInstance().getTemplate(id);
			}
			if (spawn != null) {
				pc.getTeleport().start(spawn.getLocX(), spawn.getLocY(), spawn.getMapId(), 5, false);
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("spawnid(" + id + ")의 원래로 납니다"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage("spawnid(" + id  + S_SystemMessage.getRefText(705), true), true);
			} else {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("spawnid(" + id + ")(은)는 발견되지 않습니다"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage("spawnid(" + id  + S_SystemMessage.getRefText(706), true), true);
			}
			_spawnId.put(pc.getId(), id);
			return true;
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [스폰아이디] [+,-]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(707), true), true);
			return false;
		}
	}
}


