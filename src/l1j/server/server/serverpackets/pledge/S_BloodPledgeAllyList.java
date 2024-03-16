package l1j.server.server.serverpackets.pledge;

import java.util.Map;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeAllyList extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_ALLY_LIST = "[S] S_BloodPledgeAllyList";
	private byte[] _byte = null;
	public static final int LIST = 0x040a;
	
	public S_BloodPledgeAllyList(L1Clan clan) {
		int alianceClanId	= -1,	alianceType	= -1;
		for (Map.Entry<Integer, Integer> entry : clan.getAlliance().entrySet()) {
			alianceClanId	= entry.getKey();
			alianceType		= entry.getValue();
		}
		L1Clan aliance		= L1World.getInstance().getClan(alianceClanId);// 동맹 혈맹
		if (aliance == null) {
			System.out.println(String.format(
					"[S_PledgeAllyList] ALLY_EMPTY : OWNER_PLEDGE_ID(%d) ALLY_PLEDGE_ID(%d)",
					clan.getClanId(), alianceClanId));
			return;
		}
		L1Clan master		= alianceType == L1Clan.ALLIANCE_MASTER ? aliance : clan;// 동맹주 혈맹
		L1Clan ally			= alianceType == L1Clan.ALLIANCE_NORMAL ? aliance : clan;// 일반 동맹
		write_init();
		write_master(master);
		write_leader_pledge_foundation_day((int)(master.getClanBirthDay().getTime() / 1000));
		write_allys(master);
		write_allys(ally);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(LIST);
	}
	
	void write_master(L1Clan master) {
		PledgeInfoStream os = null;
		try {
			os = new PledgeInfoStream(master);
			writeC(0x0a);
			writeBytesWithLength(os.getBytes());
			os.clear();
			os = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void write_leader_pledge_foundation_day(int leader_pledge_foundation_day) {
		writeC(0x10);// 동맹주 창설일 leader_pledge_foundation_day
		writeBit(leader_pledge_foundation_day);
	}
	
	void write_allys(L1Clan allys) {
		PledgeInfoStream os = null;
		try {
			os = new PledgeInfoStream(allys);
			writeC(0x1a);
			writeBytesWithLength(os.getBytes());
			os.clear();
			os = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_BLOODPLEDGE_ALLY_LIST;
	}
}

