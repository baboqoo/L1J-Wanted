package l1j.server.server.serverpackets.ranking;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1CharacterConfig;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_MyRanking extends ServerBasePacket {
	private static final String S_MY_RANKING = "[S] S_MyRanking";
	private byte[] _byte = null;
	public static final int MY_RANK	= 0x03fe;
	
	public static final S_MyRanking NOW_NOT_SERVICE_MY	= new S_MyRanking(null, 0, S_MyRanking.RankMyResultCode.RC_NOW_NOT_SERVICE);
	public static final S_MyRanking NO_CHANGE_MY		= new S_MyRanking(null, 0, S_MyRanking.RankMyResultCode.RC_NO_CHANGE);
	public static final S_MyRanking NO_RANK_MY			= new S_MyRanking(null, 0, S_MyRanking.RankMyResultCode.RC_NO_RANK);
	public static final S_MyRanking UNKNOWN_ERROR_MY	= new S_MyRanking(null, 0, S_MyRanking.RankMyResultCode.RC_UNKNOWN_ERROR);
	
	public S_MyRanking(L1PcInstance pc, long version, S_MyRanking.RankMyResultCode result) {
		write_init();
		write_result_code(result);
		if (result == S_MyRanking.RankMyResultCode.RC_SUCCESS) {
			write_info(version, pc.getConfig());
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MY_RANK);
	}
	
	void write_result_code(S_MyRanking.RankMyResultCode result) {
		writeRaw(0x08);// result_code
		writeRaw(result.value);
	}
	
	void write_info(long version, L1CharacterConfig config) {
		writeRaw(0x10);// version
		writeBit(version);
		
		writeRaw(0x18);// class_rank
		writeBit(config.get_class_rank());
		
		writeRaw(0x20);// previous_class_rank
		writeBit(config.get_previous_class_rank());
		
		writeRaw(0x28);// all_rank
		writeBit(config.get_all_rank());
		
		writeRaw(0x30);// previous_all_rank
		writeBit(config.get_previous_all_rank());
	}
	
	public enum RankMyResultCode{
		RC_SUCCESS(0),
		RC_NO_CHANGE(1),
		RC_NOW_NOT_SERVICE(2),
		RC_NO_RANK(3),
		RC_UNKNOWN_ERROR(100);
		private int value;
		RankMyResultCode(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(RankMyResultCode v){
			return value == v.value;
		}
		public static RankMyResultCode fromInt(int i){
			switch(i){
			case 0:
				return RC_SUCCESS;
			case 1:
				return RC_NO_CHANGE;
			case 2:
				return RC_NOW_NOT_SERVICE;
			case 3:
				return RC_NO_RANK;
			case 100:
				return RC_UNKNOWN_ERROR;
			default:
				return null;
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
		return S_MY_RANKING;
	}

}
