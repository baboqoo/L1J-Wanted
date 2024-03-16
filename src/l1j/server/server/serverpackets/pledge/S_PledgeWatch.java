package l1j.server.server.serverpackets.pledge;

import java.util.Collection;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PledgeWatch extends ServerBasePacket {
	private static final String S_PLEDGE_WATCH = "[S] S_PledgeWatch";
	
	public static final S_PledgeWatch ATTENTION_EMPTY = new S_PledgeWatch(null);

	public S_PledgeWatch(String[] name) {
		writeC(Opcodes.S_PLEDGE_WATCH);
		writeC(0x02);
		writeC(0x00);
		if (name == null) {
			writeD(0);
			writeH(0);
		} else {
			writeD(name.length);
			for (String clanName : name) {
				writeS(clanName);
			}
		}
	}
	
	public S_PledgeWatch(L1PcInstance pc, int type, boolean on) {
		writeC(Opcodes.S_PLEDGE_WATCH);
		writeH(type);
		if (type == 2) {
			if (on) {
				Collection<L1Clan> list = L1World.getInstance().getAllClans();
				int size = list.size();
				writeD(size);
				if (size > 0) {
					for (L1Clan clan : list) {
						writeS(clan.getClanName());
					}
				}
			} else {
				writeD(0x00);
			}
		}
		writeH(0);
	}

	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_PLEDGE_WATCH;
	}
}
