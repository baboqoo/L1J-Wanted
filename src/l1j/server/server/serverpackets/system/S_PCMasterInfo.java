package l1j.server.server.serverpackets.system;

import l1j.server.GameSystem.freebuffshield.GoldenBuffInfo;
import l1j.server.GameSystem.freebuffshield.FreeBuffShieldHandler;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PCMasterInfo extends ServerBasePacket {
	private static final String S_PC_MASTER_INFO = "[S] S_PCMasterInfo";
	private byte[] _byte = null;
	public static final int INFO	= 0x0a7f;
	
	public S_PCMasterInfo(FreeBuffShieldHandler handler) {
		DISABLE_FREE_BUFF_SHIELD disable	= handler.get_disable_state();
		FREE_BUFF_SHIELD_INFO info			= handler.get_free_buff_shield_info(FREE_BUFF_SHIELD_TYPE.PC_CAFE_SHIELD);
		
		write_init();
		write_favor_state(disable == null);
		write_favor_remain_count(info == null ? 0 : info.get_favor_remain_count());
		write_favor_locked_time(disable == null ? 0 : disable.get_favor_locked_time());
		write_reward_item_count(handler.get_pccafe_reward_item_count());
		write_golden_buff_info(handler.get_golden_buff_infos());
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INFO);
	}
	
	void write_favor_state(boolean favor_state) {
		writeRaw(0x08);
		writeB(favor_state);
	}
	
	void write_favor_remain_count(int favor_remain_count) {
		writeRaw(0x10);
		writeRaw(favor_remain_count);
	}
	
	void write_favor_locked_time(int favor_locked_time) {
		writeRaw(0x18);
		writeBit(favor_locked_time);
	}
	
	void write_reward_item_count(int reward_item_count) {
		writeRaw(0x20);
		writeRaw(reward_item_count);
	}
	
	void write_golden_buff_info(java.util.LinkedList<GoldenBuffInfo> golden_buff_infos) {
		for (GoldenBuffInfo info : golden_buff_infos) {
			writeRaw(0x2a);
			writeBytesWithLength(get_golden_buff_info(info));
		}
	}
	
	byte[] get_golden_buff_info(GoldenBuffInfo info) {
		GoldenBuffInfoStream os = null;
		try {
			os = new GoldenBuffInfoStream(info);
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
		return S_PC_MASTER_INFO;
	}
}

