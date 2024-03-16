package l1j.server.server.serverpackets.spell;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_SkillIconGFX extends ServerBasePacket {
	public static final int ATTR		= 0x0f;
	public static final int DOLL_BUFF	= 0x11;
	public static final int MANA_ICON	= 0x22;
	public static final int POLY		= 0x23;
	public static final int CHAT_ICON	= 0x24;
	public static final int DOLL_ICON	= 0x38;
	
	public static final S_SkillIconGFX ATTR_EMPTY			= new S_SkillIconGFX(ATTR, 0);
	public static final S_SkillIconGFX ATTR_FIRE			= new S_SkillIconGFX(ATTR, 1);
	public static final S_SkillIconGFX ATTR_WATER			= new S_SkillIconGFX(ATTR, 2);
	public static final S_SkillIconGFX ATTR_FIRE_WATER		= new S_SkillIconGFX(ATTR, 3);
	public static final S_SkillIconGFX ATTR_AIR				= new S_SkillIconGFX(ATTR, 4);
	public static final S_SkillIconGFX ATTR_FIRE_AIR		= new S_SkillIconGFX(ATTR, 5);
	public static final S_SkillIconGFX ATTR_WATER_AIR		= new S_SkillIconGFX(ATTR, 6);
	public static final S_SkillIconGFX ATTR_EARTH			= new S_SkillIconGFX(ATTR, 8);
	public static final S_SkillIconGFX ATTR_FIRE_EARTH		= new S_SkillIconGFX(ATTR, 9);
	public static final S_SkillIconGFX ATTR_WATER_EARTH		= new S_SkillIconGFX(ATTR, 10);
	public static final S_SkillIconGFX ATTR_AIR_EARTH		= new S_SkillIconGFX(ATTR, 12);

	public S_SkillIconGFX(int i, int j) {
		writeC(Opcodes.S_EVENT);
		writeC(i);
		writeH(j);
		writeH(0);// 추가
	}
	
	public S_SkillIconGFX(int i, int j, int gfxid, int objid) {
		writeC(Opcodes.S_EVENT);
		writeC(i);
		writeH(j);
		// writeBit(gfxid);//주석 밑에추가
		writeH(gfxid);// 인형더블클릭ON표기
		writeD(objid);
	}
	
	public S_SkillIconGFX(int i) {
		writeC(Opcodes.S_EVENT);
		writeC(0xa0);
		writeC(1);
		writeH(0);
		writeC(2);
		writeH(i);
	}
	
	public S_SkillIconGFX(int i, int j, boolean on) {
		writeC(Opcodes.S_EVENT);
		writeC(i);
		writeH(j);
		writeC(on ? 1 : 0);
	}
	
	public S_SkillIconGFX(int i, int j, boolean on, boolean lv100) {
		writeC(Opcodes.S_EVENT);
		writeC(i);
		writeH(j);
		writeC(on ? (lv100 ? 2 : 1) : 0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}

