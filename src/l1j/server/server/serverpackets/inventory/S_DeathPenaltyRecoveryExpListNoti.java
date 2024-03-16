package l1j.server.server.serverpackets.inventory;

import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyExpObject;
import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyObject;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_DeathPenaltyRecoveryExpListNoti extends ServerBasePacket {
	private static final String S_DEATH_PENALTY_RECOVERY_EXP_LIST_NOTI = "[S] S_DeathPenaltyRecoveryExpListNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x09b3;
	
	public S_DeathPenaltyRecoveryExpListNoti(java.util.LinkedList<DeathPenaltyObject> list) {
		write_init();
		if (list != null && !list.isEmpty()) {
			long currentTime = System.currentTimeMillis();
			int index = 0;
			for (DeathPenaltyObject obj : list) {
				long delete_time = obj.get_delete_time().getTime();
				if (delete_time <= currentTime) {
					continue;
				}
				write_death_penalty_exp_list((DeathPenaltyExpObject) obj, delete_time, index++);
			}
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_death_penalty_exp_list(DeathPenaltyExpObject obj, long delete_time, int index) {
		obj.set_index(index);// 인덱스 설정
		writeRaw(0x0a);// death_penalty_exp_list
		writeBytesWithLength(get_death_penalty_exp_list(obj, delete_time, index));
	}
	
	byte[] get_death_penalty_exp_list(DeathPenaltyExpObject obj, long delete_time, int index) {
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			os.writeC(0x08);// index
			os.writeC(index);
			
			os.writeC(0x10);// recovery_cost
			os.writeBit(obj.get_recovery_cost());
			
			os.writeC(0x18);// delete_time
			os.writeBit((int)(delete_time / 1000));
			
			os.writeC(0x25);// exp_ratio
			os.writeFloat(obj.get_exp_ratio());
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
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_DEATH_PENALTY_RECOVERY_EXP_LIST_NOTI;
	}
}

