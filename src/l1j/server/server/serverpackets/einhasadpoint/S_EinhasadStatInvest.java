package l1j.server.server.serverpackets.einhasadpoint;

import l1j.server.common.data.eEinhasadStatType;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EinhasadStatInvest extends ServerBasePacket {
	private static final String S_EINHASAD_STAT_INVEST = "[S] S_EinhasadStatInvest";
	private byte[] _byte = null;
	public static final int INVEST	= 0x092e;
	
	public S_EinhasadStatInvest(L1PcInstance pc, eEinhasadStatType stateType) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INVEST);
		writeRaw(0x0a);// stat_result
		writeBytesWithLength(getDetailInfo(stateType, pc.getAbility()));
		writeH(0x00);
	}
	
	byte[] getDetailInfo(eEinhasadStatType statType, L1Ability ability){
		EinhasadStatDetailStream os = null;
		try {
			os = new EinhasadStatDetailStream(statType, ability);
			return os.getBytes();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
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
		return S_EINHASAD_STAT_INVEST;
	}
}

