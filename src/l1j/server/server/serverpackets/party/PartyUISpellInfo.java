package l1j.server.server.serverpackets.party;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.datatables.SkillsInfoTable;
import l1j.server.server.utils.BinaryOutputStream;

public class PartyUISpellInfo extends BinaryOutputStream {
	public static final ConcurrentHashMap<Integer, byte[]> PARTY_ICON_SKILL_BYTES = new ConcurrentHashMap<>(L1SkillInfo.PARTY_ICON_LIST.size());
	
	protected PartyUISpellInfo() {
		super();
	}
	
	protected PartyUISpellInfo write_info(int spell_id) {
		int bufficon_id = SkillsInfoTable.getSkillStatusIcon(spell_id);
		if (bufficon_id == 0) {
			System.out.println(String.format("[PartyUISpellInfo] ICON_NOT_FOUND : ID(%d)", spell_id));
			return null;
		}
		int length = 7 + getBitSize(bufficon_id) + getBitSize(spell_id);
		writeC(0x4a);
		writeC(length);
		write_bufficon_id(bufficon_id);
		write_isGood(true);
		write_spell_id(spell_id);
		write_duration(1800);
		return this;
	}
	
	void write_bufficon_id(int bufficon_id) {
		writeC(0x08);// bufficon_id
		writeBit(bufficon_id);
	}
	
	void write_isGood(boolean isGood) {
		writeC(0x10);// isGood
		writeB(isGood);
	}
	
	void write_spell_id(int spell_id) {
		writeC(0x18);// spell_id
		writeBit(spell_id);
	}
	
	void write_duration(int duration) {
		writeC(0x20);// duration
		writeBit(duration);
	}
	
	void write_tooltip_id(int tooltip_id) {
		writeC(0x28);// tooltip_id
		writeBit(tooltip_id);
	}
	
	static byte[] putSkillByteInfo(int skillId) {
		PartyUISpellInfo os = null;
		try {
			os = new PartyUISpellInfo().write_info(skillId);
			PARTY_ICON_SKILL_BYTES.put(skillId, os.getBytes());
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
	
	public static void init(){
		for (int skillId : L1SkillInfo.PARTY_ICON_LIST) {
			putSkillByteInfo(skillId);
		}
	}
	
}

