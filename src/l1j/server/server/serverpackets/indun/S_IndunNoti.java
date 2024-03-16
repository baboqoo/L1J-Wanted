package l1j.server.server.serverpackets.indun;

import l1j.server.IndunSystem.indun.IndunList;
import l1j.server.IndunSystem.indun.IndunType;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_IndunNoti extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_INDUN_NOTI = "[S] S_IndunNoti";
	public static final int NOTI	= 0x08b6;

	//	사용하는 타입	202:오림,	203:악어섬, 204:몽환의 섬, 205:차원이 얽힌 공간, 206:아우라키아 정화
	private static final IndunType[] TYPE_ARRAY		= {
		IndunType.ORIM, IndunType.CROCODILE, IndunType.FANTASY, /*IndunType.SPACE,*/ IndunType.AURAKIA
	};
	
	public S_IndunNoti(ArenaNotiResult result) {
		write_init();
		write_result(result);
		for (IndunType indunType : TYPE_ARRAY) {
			write_noti_info(indunType.getMapKind(), IndunList.getTypeSize(indunType));
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_result(ArenaNotiResult result) {
		writeRaw(0x08);// result
		writeRaw(result.value);
	}
	
	void write_noti_info(int map_kind, int count) {
		writeRaw(0x12);// noti_info
		writeRaw(5);
		
		writeRaw(0x08);// map_kind
		writeBit(map_kind);
		
		writeRaw(0x10);// count
		writeRaw(count);
	}
 	
 	public enum ArenaNotiResult{
		SUCCESS(1),
		ERROR_INTENAL(2),
		ERROR_NOT_EXIST_USER(3),
		;
		private int value;
		ArenaNotiResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ArenaNotiResult v){
			return value == v.value;
		}
		public static ArenaNotiResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return ERROR_INTENAL;
			case 3:
				return ERROR_NOT_EXIST_USER;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
			}
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
		return S_INDUN_NOTI;
	}
}
