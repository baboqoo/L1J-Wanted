package l1j.server.server.serverpackets.pledge;

import java.util.Collection;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeList extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_LIST = "[S] S_BloodPledgeList";
	private byte[] _byte = null;
	public static final int LIST	= 0x0969;
	
	public S_BloodPledgeList(Collection<L1Clan> list) {
		write_init();
		for (L1Clan clan : list) {
			if (clan == null || clan.isBot()) {
				continue;
			}
			write_blood_pledge_list_info(clan);
		}
		write_is_last(true);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(LIST);
	}
	
	void write_blood_pledge_list_info(L1Clan clan) {
		writeRaw(0x0a);// blood_pledge_list_info
		writeBytesWithLength(getPledgeInfo(clan));
	}
	
	void write_is_last(boolean is_last) {
		writeRaw(0x10);// is_last
		writeB(is_last);
	}
	
	byte[] getPledgeInfo(L1Clan clan){
		BloodPledgeListInfoStream os = null;
		try {
			os = new BloodPledgeListInfoStream(clan);
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_BLOODPLEDGE_LIST;
	}
}

