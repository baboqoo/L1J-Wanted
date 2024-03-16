package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.command.executor.L1UserCalc;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.command.S_WhoAmount;
import l1j.server.server.serverpackets.command.S_WhoCharinfo;
import l1j.server.server.serverpackets.command.S_WhoUserNoti;

public class C_Who extends ClientBasePacket {
	private static final String C_WHO = "[C] C_Who";

	public C_Who(byte[] decrypt, GameClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		
		String name			= readS();
		L1Character find	= L1World.getInstance().getPlayer(name);
		if (find == null) {
			find			= L1World.getInstance().getShopNpc(name);
		}
		if (find == null || find instanceof L1AiUserInstance) {
			notFoundPlayer(pc);
			return;
		}
		if (find instanceof L1PcInstance) {
			if (pc.isGm()) {
				pc.sendPackets(new S_WhoUserNoti((L1PcInstance)find, true), true);
				return;
			}
			pc.sendPackets(new S_WhoCharinfo((L1PcInstance)find), true);
			return;
		}
		pc.sendPackets(new S_WhoCharinfo((L1NpcShopInstance)find), true);
	}
	
	void notFoundPlayer(L1PcInstance pc){
		int userCount	= (int) (L1World.getInstance().get_players_size() * 1.5);
		int calcUser	= L1UserCalc.getClacUser();
		userCount		+= calcUser;// 뻥티기
		pc.sendPackets(new S_WhoAmount(Integer.toString(userCount)), true);
	}

	@Override
	public String getType() {
		return C_WHO;
	}
}

