package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeStoreAllowAdd extends ServerBasePacket {
	private static final String S_BLOOD_PLEDGE_STORE_ALLOW_ADD = "[S] S_BloodPledgeStoreAllowAdd";
	private byte[] _byte = null;
	public static final int ADD	= 0x0aca;
	
	public static final S_BloodPledgeStoreAllowAdd eRESULT_OK		= new S_BloodPledgeStoreAllowAdd(S_BloodPledgeStoreAllowAdd.eRESULT.eRESULT_OK);
	public static final S_BloodPledgeStoreAllowAdd eRESULT_NO_USER	= new S_BloodPledgeStoreAllowAdd(S_BloodPledgeStoreAllowAdd.eRESULT.eRESULT_NO_USER);
	public static final S_BloodPledgeStoreAllowAdd eRESULT_MAX_USER	= new S_BloodPledgeStoreAllowAdd(S_BloodPledgeStoreAllowAdd.eRESULT.eRESULT_MAX_USER);
	
	public S_BloodPledgeStoreAllowAdd(S_BloodPledgeStoreAllowAdd.eRESULT result) {
		write_init();
		write_result(result);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ADD);
	}
	
	void write_result(S_BloodPledgeStoreAllowAdd.eRESULT result) {
		writeRaw(0x08);
		writeRaw(result.value);
	}
	
	public enum eRESULT{
		eRESULT_OK(0),
		eRESULT_NO_USER(1),
		eRESULT_MAX_USER(2),
		;
		private int value;
		eRESULT(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eRESULT v){
			return value == v.value;
		}
		public static eRESULT fromInt(int i){
			switch(i){
			case 0:
				return eRESULT_OK;
			case 1:
				return eRESULT_NO_USER;
			case 2:
				return eRESULT_MAX_USER;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eRESULT, %d", i));
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
		return S_BLOOD_PLEDGE_STORE_ALLOW_ADD;
	}
}

