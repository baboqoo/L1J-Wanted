package l1j.server.server.serverpackets.spell;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AvailableSpellNoti extends ServerBasePacket {
	private static final String S_AVAILABLE_SPELL_NOTI = "[S] S_AvailableSpellNoti";
	private byte[] _byte = null;
	public static final int NOTI	= 0x0411;
	
	public static final S_AvailableSpellNoti ENERGE_BOLT				= new S_AvailableSpellNoti(L1SkillId.ENERGY_BOLT, true);
	public static final S_AvailableSpellNoti TELEPORT_MOTHER			= new S_AvailableSpellNoti(L1SkillId.TELEPORT_TO_MATHER, true);
	
	public static final S_AvailableSpellNoti HELMET_OF_STR				= new S_AvailableSpellNoti(new int[]{ L1SkillId.ENCHANT_WEAPON, L1SkillId.DETECTION, L1SkillId.PHYSICAL_ENCHANT_STR }, true);
	public static final S_AvailableSpellNoti HELMET_OF_DEX				= new S_AvailableSpellNoti(new int[]{ L1SkillId.PHYSICAL_ENCHANT_DEX, L1SkillId.HASTE }, true);
	public static final S_AvailableSpellNoti HELMET_OF_HEAL				= new S_AvailableSpellNoti(new int[]{ L1SkillId.HEAL, L1SkillId.EXTRA_HEAL }, true);
	public static final S_AvailableSpellNoti HELMET_OF_OLD_WIND			= new S_AvailableSpellNoti(L1SkillId.HASTE, true);
	public static final S_AvailableSpellNoti HELMET_OF_WIND				= new S_AvailableSpellNoti(L1SkillId.GREATER_HASTE, true);
	
	public static final S_AvailableSpellNoti PHYSICAL_ENCHANT_DEX_OFF	= new S_AvailableSpellNoti(L1SkillId.PHYSICAL_ENCHANT_DEX, false);
	public static final S_AvailableSpellNoti PHYSICAL_ENCHANT_STR_OFF	= new S_AvailableSpellNoti(L1SkillId.PHYSICAL_ENCHANT_STR, false);
	public static final S_AvailableSpellNoti HASTE_OFF					= new S_AvailableSpellNoti(L1SkillId.HASTE, false);
	public static final S_AvailableSpellNoti GREATER_HASTE_OFF			= new S_AvailableSpellNoti(L1SkillId.GREATER_HASTE, false);
	public static final S_AvailableSpellNoti HEAL_OFF					= new S_AvailableSpellNoti(L1SkillId.HEAL, false);
	public static final S_AvailableSpellNoti EXTRA_HEAL_OFF				= new S_AvailableSpellNoti(L1SkillId.EXTRA_HEAL, false);
	public static final S_AvailableSpellNoti ENCHANT_WEAPON_OFF			= new S_AvailableSpellNoti(L1SkillId.ENCHANT_WEAPON, false);
	public static final S_AvailableSpellNoti DETECTION_OFF				= new S_AvailableSpellNoti(L1SkillId.DETECTION, false);
	
	public static final S_AvailableSpellNoti ARROW_OF_AURAKIA_ON		= new S_AvailableSpellNoti(L1SkillId.ARROW_OF_AURAKIA, true);
	public static final S_AvailableSpellNoti ARROW_OF_AURAKIA_OFF		= new S_AvailableSpellNoti(L1SkillId.ARROW_OF_AURAKIA, false);
	
	public S_AvailableSpellNoti(int spell_id, boolean available) {
		write_init();
		write_spell_info(spell_id, available);
        writeH(0x00);
	}
	
	public S_AvailableSpellNoti(int[] array, boolean available){
		write_init();
		for (int spell_id : array) {
			write_spell_info(spell_id, available);
		}
		writeH(0x00);
	}
	
	public S_AvailableSpellNoti(List<Integer> list, boolean available){
		write_init();
		for (int spell_id : list) {
			write_spell_info(spell_id, available);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_spell_info(int spell_id, boolean available) {
		writeRaw(0x0a);
		writeRaw(getBitSize(spell_id) + 3);
		write_spell_id(spell_id);
		write_available(available);
	}
	
	void write_spell_id(int spell_id) {
		writeRaw(0x08);// spell_id
		writeBit(spell_id);
	}
	
	void write_available(boolean available) {
		writeRaw(0x10);// available
		writeB(available);
	}
	
	void write_mastery_rank(int mastery_rank) {
		writeRaw(0x18);// mastery_rank
		writeRaw(mastery_rank);
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
		return S_AVAILABLE_SPELL_NOTI;
	}
}

