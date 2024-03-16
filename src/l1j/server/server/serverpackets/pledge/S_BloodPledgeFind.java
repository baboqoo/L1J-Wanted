package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeFind extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_FIND = "[S] S_BloodPledgeFind";
	private byte[] _byte = null;
	public static final int FIND	= 0x096b;
	
	public S_BloodPledgeFind(L1Clan clan) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(FIND);
		writeRaw(0x0a);// blood_pledge_list_info
		writeBytesWithLength(getPledgeInfo(clan));
		writeH(0x00);
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
		return S_BLOODPLEDGE_FIND;
	}
}

