package l1j.server.server.serverpackets.spell;

import l1j.server.server.Opcodes;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SpellPassiveOnOff extends ServerBasePacket {
	private static final String S_SPELL_PASSIVE_ON_OFF = "[S] S_SpellPassiveOnOff";
	private byte[] _byte = null;
	
	public static final int ON_OFF = 0x0199;
	
	public static final S_SpellPassiveOnOff[] BLOOD_TO_SOUL = {
		new S_SpellPassiveOnOff(L1PassiveId.BLOOD_TO_SOUL.toInt(), false),
		new S_SpellPassiveOnOff(L1PassiveId.BLOOD_TO_SOUL.toInt(), true)
	};

	public S_SpellPassiveOnOff(int passiveId, boolean onoff){
		write_init();
		write_result(PassiveSkillSwitchResult.eRES_OK);
		write_passiveId(passiveId);
		write_onoff(onoff);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ON_OFF);
	}
	
	void write_result(PassiveSkillSwitchResult result) {
		writeRaw(0x08);// result
		writeRaw(result.value);
	}
	
	void write_passiveId(int passiveId) {
		writeRaw(0x10);// passiveId
		writeRaw(passiveId);
	}
	
	void write_onoff(boolean onoff) {
		writeRaw(0x18);// onoff
		writeB(onoff);
	}
	
	public enum PassiveSkillSwitchResult {
		eRES_OK(0),
		eRES_FAIL_TOGGLEID(1),
		eRES_FAIL_PASSIVEID(2),
		;
		private int value;
		PassiveSkillSwitchResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(PassiveSkillSwitchResult v){
			return value == v.value;
		}
		public static PassiveSkillSwitchResult fromInt(int i){
			switch(i){
			case 0:
				return eRES_OK;
			case 1:
				return eRES_FAIL_TOGGLEID;
			case 2:
				return eRES_FAIL_PASSIVEID;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments PassiveSkillSwitchResult, %d", i));
			}
		}
	}
	
	public static void init(){}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_SPELL_PASSIVE_ON_OFF;
	}
}

