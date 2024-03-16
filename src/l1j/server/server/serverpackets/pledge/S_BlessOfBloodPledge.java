package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BlessOfBloodPledge extends ServerBasePacket {
    private static final String S_BLESS_OF_BLOOD_PLEDGE = "[S] S_BlessOfBloodPledge";
    private byte[] _byte = null;
    public static final int BLESS = 0x03fa;

	public S_BlessOfBloodPledge(S_BlessOfBloodPledge.eResult result) {
		write_init();
		write_result(result);// 전체변경 활성화
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(BLESS);
	}
	
	void write_result(S_BlessOfBloodPledge.eResult result) {
		writeRaw(0x08);// result
		writeRaw(result.value);
	}
	
	public enum eResult{
		Success(0),
		WorldExistingFail(1),
		PledgeCheckFail(2),
		PledgeRankFail(3),
		EnoughAdenaFail(4),
		ShuffleConditionFail(5),
		TeleportConditionFail(6),
		;
		private int value;
		eResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eResult v){
			return value == v.value;
		}
		public static eResult fromInt(int i){
			switch(i){
			case 0:
				return Success;
			case 1:
				return WorldExistingFail;
			case 2:
				return PledgeCheckFail;
			case 3:
				return PledgeRankFail;
			case 4:
				return EnoughAdenaFail;
			case 5:
				return ShuffleConditionFail;
			case 6:
				return TeleportConditionFail;
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
        return S_BLESS_OF_BLOOD_PLEDGE;
    }
}

