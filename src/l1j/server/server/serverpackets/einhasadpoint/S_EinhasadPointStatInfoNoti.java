package l1j.server.server.serverpackets.einhasadpoint;

import l1j.server.common.data.eEinhasadStatType;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EinhasadPointStatInfoNoti extends ServerBasePacket {
	private static final String S_EINHASAD_POINT_STAT_INFO_NOTI = "[S] S_EinhasadPointStatInfoNoti";
	private byte[] _byte = null;
	public static final int INFO_NOTI	= 0x092c;
	
	public S_EinhasadPointStatInfoNoti(L1PcInstance pc, L1Ability ability) {
		write_init();
		write_total_stat(pc.getEinTotalStat());
		write_enchant_level(pc.getEinCurEnchantLevel());
		write_point(pc.getEinPoint());
		for (eEinhasadStatType statType : eEinhasadStatType.ARRAY) {
			if (statType == eEinhasadStatType._MAX_) {
				continue;
			}
			write_each_stat(statType, pc.getAbility());
		}
		write_last_value(0);
		write_result_value(0);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INFO_NOTI);
	}
	
	void write_stat_bless(int stat_bless) {
		writeRaw(0x08);// stat_bless
		writeBit(stat_bless);
	}
	
	void write_stat_lucky(int stat_lucky) {
		writeRaw(0x10);// stat_lucky
		writeBit(stat_lucky);
	}
	
	void write_stat_vital(int stat_vital) {
		writeRaw(0x18);// stat_vital
		writeBit(stat_vital);
	}
	
	void write_total_stat(int total_stat) {
		writeRaw(0x20);// total_stat 획득한 스탯
		writeBit(total_stat);
	}
	
	void write_enchant_level(int enchant_level) {
		writeRaw(0x28);// enchant_level 남은 카드
		writeRaw(enchant_level);
	}
	
	void write_point(int point) {
		writeRaw(0x30);// point 아인하사드 포인트
		writeBit(point);
	}
	
	void write_each_stat(eEinhasadStatType statType, L1Ability ability) {
		writeRaw(0x3a);// each_stat
		writeBytesWithLength(getDetailInfo(statType, ability));
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
	
	void write_last_value(int last_value) {
		writeRaw(0x40);// last_value
		writeRaw(last_value);
	}
	
	void write_result_value(int result_value) {
		writeRaw(0x48);// result_value
		writeRaw(result_value);
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
		return S_EINHASAD_POINT_STAT_INFO_NOTI;
	}
}

