package l1j.server.server.serverpackets.gamegate;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_UserStartSundry extends ServerBasePacket {
	private static final String S_USER_START_SUNDRY = "[S] S_UserStartSundry";
	private byte[] _byte = null;
	public static final int SUNDRY = 0x007e;
	
	public S_UserStartSundry(boolean is_pccafe) {
		int pc_move_delay_reduce_rate	= Config.SPEED.PC_MOVE_DELAY_REDUCE_RATE;
		int npc_move_delay_reduce_rate	= Config.SPEED.NPC_MOVE_DELAY_REDUCE_RATE;
		write_init();
		write_is_pccafe(is_pccafe);
		write_is_enable_ranking_system(Config.RANKING.RANKING_SYSTEM_ACTIVE);
		if (pc_move_delay_reduce_rate > 0) {
			write_pc_move_delay_reduce_rate(pc_move_delay_reduce_rate);
		}
		if (npc_move_delay_reduce_rate > 0) {
			write_npc_move_delay_reduce(npc_move_delay_reduce_rate);
		}
		write_account_id(0);
		writeH(0x00);
	}
	
	/**
	 * 캐릭터 생성 제한 설정(캐릭터 선택창에서 호출 된다)
	 * @param allowCreateSlot
	 * @param allowGameClass
	 */
	public S_UserStartSundry(int allowCreateSlot, int allowGameClass) {
		write_init();
		write_eventInfo(allowCreateSlot, allowGameClass);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SUNDRY);
	}
	
	void write_is_pccafe(boolean is_pccafe) {
		writeRaw(0x08);// is_pccafe
		writeB(is_pccafe);
	}
	
	void write_is_enable_ranking_system(boolean is_enable_ranking_system) {
		writeRaw(0x10);// is_enable_ranking_system
		writeB(is_enable_ranking_system);
	}
	
	void write_pc_move_delay_reduce_rate(int pc_move_delay_reduce_rate) {
		writeRaw(0x18);// pc_move_delay_reduce_rate
		writeBit(pc_move_delay_reduce_rate);
	}
	
	void write_npc_move_delay_reduce(int npc_move_delay_reduce) {
		writeRaw(0x20);// npc_move_delay_reduce
		writeBit(npc_move_delay_reduce);
	}
	
	void write_eventInfo(int allowCreateSlot, int allowGameClass) {
		writeRaw(0x2a);
		writeRaw(4);
		
		writeRaw(0x08);// allowCreateSlot
		writeRaw(allowCreateSlot);
		
		writeRaw(0x10);// allowGameClass
		writeRaw(allowGameClass);
	}
	
	void write_account_id(int account_id) {
		writeRaw(0x30);// account_id
		writeBit(account_id);
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
		return S_USER_START_SUNDRY;
	}
}

