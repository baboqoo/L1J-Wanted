package l1j.server.server.serverpackets.system;

import l1j.server.GameSystem.freebuffshield.GoldenBuffInfo;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PCMasterGoldenBuffUpdateNoti extends ServerBasePacket {
	private static final String S_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI = "[S] S_PCMasterGoldenBuffUpdateNoti";
	private byte[] _byte = null;
	public static final int UPDATE_NOTI	= 0x0a84;
	
	public S_PCMasterGoldenBuffUpdateNoti(java.util.LinkedList<GoldenBuffInfo> golden_buff_infos, S_PCMasterGoldenBuffUpdateNoti.eUpdateReason reason) {
		write_init();
		write_golden_buff_info(golden_buff_infos);
		write_reason(reason);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(UPDATE_NOTI);
	}
	
	void write_golden_buff_info(java.util.LinkedList<GoldenBuffInfo> golden_buff_infos) {
		for (GoldenBuffInfo info : golden_buff_infos) {
			writeRaw(0x0a);
			writeBytesWithLength(get_golden_buff_info(info));
		}
	}
	
	void write_reason(S_PCMasterGoldenBuffUpdateNoti.eUpdateReason reason) {
		writeRaw(0x10);
		writeRaw(reason.value);
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
	
	public enum eUpdateReason{
		UPDATE(0),			// 활성화, 비활성화
		ENFORCE_ACK(1),		// 강화
		SWITCH_TYPE_ACK(2),	// 옵션 변경
		;
		private int value;
		eUpdateReason(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eUpdateReason v){
			return value == v.value;
		}
		public static eUpdateReason fromInt(int i){
			switch(i){
			case 0:
				return UPDATE;
			case 1:
				return ENFORCE_ACK;
			case 2:
				return SWITCH_TYPE_ACK;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eUpdateReason, %d", i));
			}
		}
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
		return S_PC_MASTER_GOLDEN_BUFF_UPDATE_NOTI;
	}
}

