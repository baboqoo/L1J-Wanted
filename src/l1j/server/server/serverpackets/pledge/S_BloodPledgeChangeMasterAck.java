package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeChangeMasterAck extends ServerBasePacket {// 위임 결과
	private static final String S_BLOOD_PLEDGE_CHANGE_MASTER_ACK = "[S] S_BloodPledgeChangeMasterAck";
	private byte[] _byte = null;
	public static final int ACK	= 0x0ac1;
	
	public static final S_BloodPledgeChangeMasterAck eRESULT_OK						= new S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT.eRESULT_OK);
	public static final S_BloodPledgeChangeMasterAck eRESULT_MAINSERVER 			= new S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT.eRESULT_MAINSERVER);
	public static final S_BloodPledgeChangeMasterAck eRESULT_NOT_PLEDGE 			= new S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT.eRESULT_NOT_PLEDGE);
	public static final S_BloodPledgeChangeMasterAck eRESULT_NOT_ALREADY_REQUESTED	= new S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT.eRESULT_NOT_ALREADY_REQUESTED);
	public static final S_BloodPledgeChangeMasterAck eRESULT_NOT_MASTER				= new S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT.eRESULT_NOT_MASTER);
	public static final S_BloodPledgeChangeMasterAck eRESULT_NOT_OWN_CASTLE			= new S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT.eRESULT_NOT_OWN_CASTLE);
	public static final S_BloodPledgeChangeMasterAck eRESULT_NOT_OWN_AGIT			= new S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT.eRESULT_NOT_OWN_AGIT);
	public static final S_BloodPledgeChangeMasterAck eRESULT_NOT_IN_WAR				= new S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT.eRESULT_NOT_IN_WAR);
	public static final S_BloodPledgeChangeMasterAck eRESULT_NOT_ONLINE				= new S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT.eRESULT_NOT_ONLINE);
	public static final S_BloodPledgeChangeMasterAck eRESULT_NOT_SAME_PLEDGE_MEMBER	= new S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT.eRESULT_NOT_SAME_PLEDGE_MEMBER);
	public static final S_BloodPledgeChangeMasterAck eRESULT_NOT_PRINCE				= new S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT.eRESULT_NOT_PRINCE);
	public static final S_BloodPledgeChangeMasterAck eRESULT_NOT_LEVEL				= new S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT.eRESULT_NOT_LEVEL);
	public static final S_BloodPledgeChangeMasterAck eRESULT_NOT_ALLY				= new S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT.eRESULT_NOT_ALLY);
	
	public S_BloodPledgeChangeMasterAck(S_BloodPledgeChangeMasterAck.eRESULT result) {
		write_init();
		write_result(result);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ACK);
	}
	
	void write_result(S_BloodPledgeChangeMasterAck.eRESULT result) {
		writeRaw(0x08);
		writeRaw(result.value);
	}
	
	public enum eRESULT{
		eRESULT_OK(0),
		eRESULT_MAINSERVER(1),
		eRESULT_NOT_PLEDGE(2),
		eRESULT_NOT_ALREADY_REQUESTED(3),
		eRESULT_NOT_MASTER(4),
		eRESULT_NOT_OWN_CASTLE(5),
		eRESULT_NOT_OWN_AGIT(6),
		eRESULT_NOT_IN_WAR(7),
		eRESULT_NOT_ONLINE(8),
		eRESULT_NOT_SAME_PLEDGE_MEMBER(9),
		eRESULT_NOT_PRINCE(10),
		eRESULT_NOT_LEVEL(11),
		eRESULT_NOT_ALLY(12),
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
				return eRESULT_MAINSERVER;
			case 2:
				return eRESULT_NOT_PLEDGE;
			case 3:
				return eRESULT_NOT_ALREADY_REQUESTED;
			case 4:
				return eRESULT_NOT_MASTER;
			case 5:
				return eRESULT_NOT_OWN_CASTLE;
			case 6:
				return eRESULT_NOT_OWN_AGIT;
			case 7:
				return eRESULT_NOT_IN_WAR;
			case 8:
				return eRESULT_NOT_ONLINE;
			case 9:
				return eRESULT_NOT_SAME_PLEDGE_MEMBER;
			case 10:
				return eRESULT_NOT_PRINCE;
			case 11:
				return eRESULT_NOT_LEVEL;
			case 12:
				return eRESULT_NOT_ALLY;
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
		return S_BLOOD_PLEDGE_CHANGE_MASTER_ACK;
	}
}

