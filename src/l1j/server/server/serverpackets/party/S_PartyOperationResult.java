package l1j.server.server.serverpackets.party;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_PartyOperationResult extends ServerBasePacket {
	private static final String S_PARTY_OPERATION_RESULT = "[S] S_PartyOperationResult";
	private byte[] _byte = null;
	public static final int RESULT	= 0x021b;

	public S_PartyOperationResult(L1PcInstance pc, S_PartyOperationResult.ePARTY_OPERATION_TYPE type) {
		write_init();
		write_type(type);
		write_actor_name(pc.getName());
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(RESULT);
	}
	
	void write_type(S_PartyOperationResult.ePARTY_OPERATION_TYPE type) {
		writeRaw(0x08);// type
		writeRaw(type.value);
	}
	
	void write_actor_name(String actor_name) {
		writeRaw(0x12);// actor_name
		writeS2(actor_name);
	}
	
	public enum ePARTY_OPERATION_TYPE{
		ePARTY_OPERATION_TYPE_INVITE_REJECT(1),
		ePARTY_OPERATION_TYPE_INVITE_ACCEPT(2);
		private int value;
		ePARTY_OPERATION_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ePARTY_OPERATION_TYPE v){
			return value == v.value;
		}
		public static ePARTY_OPERATION_TYPE fromInt(int i){
			switch(i){
			case 1:
				return ePARTY_OPERATION_TYPE_INVITE_REJECT;
			case 2:
				return ePARTY_OPERATION_TYPE_INVITE_ACCEPT;
			default:
				return null;
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
		return S_PARTY_OPERATION_RESULT;
	}

}

