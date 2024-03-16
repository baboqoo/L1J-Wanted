package l1j.server.server.serverpackets.companion;

import java.util.Map;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_CompanionSkillNoti extends ServerBasePacket {
	private static final String S_COMPANION_SKILL_NOTI = "[S] S_CompanionSkillNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x07d6;
	
	public S_CompanionSkillNoti(int id, int enchant) {
		write_init();
		write_skills(id, enchant);
	    writeH(0x00);
	}
	  
	public S_CompanionSkillNoti(Map<Integer, Integer> map) {
		write_init();
		write_skills(map);
	    writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
	    writeH(NOTI);
	}
	
	void write_skills(Map<Integer, Integer> map) {
		for (int id : map.keySet()) {
			write_skills(id, map.get(id));
	    }
	}
	
	void write_skills(int id, int enchant) {
		writeRaw(0x0a);
		writeRaw(4);
		
		writeRaw(0x08);
		writeRaw(id);
		
		writeRaw(0x10);
		writeRaw(enchant);
	}
	
	public static class Skill extends BinaryOutputStream {
		public Skill(int id, int enchant) {
			super();
			write_id(id);
			write_enchant(enchant);
		}
		
		void write_id(int id) {
			writeC(0x08);
			writeBit(id);
		}
		
		void write_enchant(int enchant) {
			writeC(0x08);
			writeC(enchant);
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
		return S_COMPANION_SKILL_NOTI;
    }
}
