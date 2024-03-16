package l1j.server.server.serverpackets.companion;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_CompanionSkillEnchant extends ServerBasePacket {
	private static final String S_COMPANION_SKILL_ENCHANT = "[S] S_CompanionSkillEnchant";
	private byte[] _byte = null;
	public static final int ENCHANT	= 0x07d9;
	
	public S_CompanionSkillEnchant(int skill_id, S_CompanionSkillEnchant.eResult result) {
		write_init();
		write_skill_id(skill_id);
		write_result(result);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ENCHANT);
	}
	
	void write_skill_id(int skill_id) {
		writeRaw(0x08);
		writeRaw(skill_id);
	}
	
	void write_result(S_CompanionSkillEnchant.eResult result) {
		writeRaw(0x10);
		writeRaw(result.value);
	}
	
	public enum eResult{
		SUCCESS(0),
		FAIL(1),
		NOTHING(2),
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
				return SUCCESS;
			case 1:
				return FAIL;
			case 2:
				return NOTHING;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
			}
		}
	}

	public byte[] getContent() {
	    if (_byte == null) {
	    	_byte = getBytes();
	    }
	    return _byte;
	}
  
    @Override
	public String getType() {
		return S_COMPANION_SKILL_ENCHANT;
    }
}
