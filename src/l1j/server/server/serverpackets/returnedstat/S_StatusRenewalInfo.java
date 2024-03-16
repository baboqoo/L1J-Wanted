package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.construct.L1Status;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.CalcChaStat;
import l1j.server.server.utils.CalcConStat;
import l1j.server.server.utils.CalcDexStat;
import l1j.server.server.utils.CalcIntelStat;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.CalcStrStat;
import l1j.server.server.utils.CalcWisStat;

public class S_StatusRenewalInfo extends ServerBasePacket {
	private static final String S_STATUS_RENEWAL_INFO	= "[S] S_StatusRenewalInfo";
	private byte[] _byte						= null;
	public static final int STAT_UPDATE			= 0x01e3;
	private static final byte[] MINUS_BYTES		= { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0x01 };
	
	void strInfo(int str, int con) {
		int damageBonus	= CalcStrStat.shortDamage(str);
		int hitBonus	= CalcStrStat.shortHitup(str);
		int criBonus	= CalcStrStat.shortCritical(str);
		int carryBonus	= CalcStat.carryBonus(str, con);
		int length		= 4 + (damageBonus < 0 ? 10 : 1) + (hitBonus < 0 ? 10 : 1) + (criBonus < 0 ? 10 : 1) + getBitSize(carryBonus);
		writeRaw(0x12);
		writeRaw(length);
		writeRaw(0x08);// damagebonus
		writeRaw(damageBonus);
		if (damageBonus < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x10);// hitbonus
		writeRaw(hitBonus);
		if (hitBonus < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x18);// cribonus
		writeRaw(criBonus);
		if (criBonus < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x20);// carrybonus
		byteWrite(carryBonus);
	}
	
	void intInfo(int inti) {
		int damageBonus	= CalcIntelStat.magicDamage(inti);
		int hitBonus	= CalcIntelStat.magicHitup(inti);
		int criBonus	= CalcIntelStat.magicCritical(inti);
		int magicBonus	= CalcIntelStat.magicBonus(inti);
		int reduceMp	= CalcIntelStat.reduceMp(inti);
		int length		= 5 + (damageBonus < 0 ? 10 : 1) + (hitBonus < 0 ? 10 : 1) + (criBonus < 0 ? 10 : 1) + (magicBonus < 0 ? 10 : 1) + (reduceMp < 0 ? 10 : 1);
		writeRaw(0x1a);
		writeRaw(length);
		writeRaw(0x08);// damagebonus
		writeRaw(damageBonus);
		if (damageBonus < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x10);// hitbonus
		writeRaw(hitBonus);
		if (hitBonus < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x18);// cribonus
		writeRaw(criBonus);
		if (criBonus < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x20);// magicbonus
		writeRaw(magicBonus);
		if (magicBonus < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x28);// reducemp
		writeRaw(reduceMp);
		if (reduceMp < 0) {
			writeByte(MINUS_BYTES);
		}
	}
	
	void wisInfo(int wis, int classType, int basemaxmp) {
		int mpRegen		= CalcWisStat.mpRegen(wis);
		int mpIncPotion	= CalcWisStat.mpIncPotion(wis);
		int mrBonus		= CalcWisStat.mrBonus(classType, wis);
		int[] maxMp		= CalcWisStat.levelUpMp(classType, wis);
		int length		= 8 + (mpRegen < 0 ? 10 : 1) + (mpIncPotion < 0 ? 10 : 1) + getBitSize(mrBonus) + getBitSize(maxMp[0]) + getBitSize(maxMp[1]) + getBitSize(basemaxmp);
		writeRaw(0x22);
		writeRaw(length);
		writeRaw(0x08);// mpregen
		writeRaw(mpRegen);
		if (mpRegen < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x10);// mpincpotion
		writeRaw(mpIncPotion);
		if (mpIncPotion < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x18);// mrbonus
		writeBit(mrBonus);
		writeRaw(0x20);// maxmplow
		writeBit(maxMp[0]);
		writeRaw(0x28);// maxmphigh
		writeBit(maxMp[1]);
		writeRaw(0x30);// magicpoint
		writeRaw(0);
		writeRaw(0x38);// basemaxmp
		writeBit(basemaxmp);
	}
	
	void dexInfo(int dex) {
		int damagebonus		= CalcDexStat.longDamage(dex);
		int hitbonus		= CalcDexStat.longHitup(dex);
		int cribonus		= CalcDexStat.longCritical(dex);
		int acbonus			= CalcDexStat.acBonus(dex);
		int evasionbonus	= CalcDexStat.evasionBonus(dex);
		int length			= 5 + (damagebonus < 0 ? 10 : 1) + (hitbonus < 0 ? 10 : 1) + (cribonus < 0 ? 10 : 1) + (acbonus < 0 ? 10 : 1) + (evasionbonus < 0 ? 10 : 1);
		writeRaw(0x2a);
		writeRaw(length);
		writeRaw(0x08);// damagebonus
		writeRaw(damagebonus);
		if (damagebonus < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x10);// hitbonus
		writeRaw(hitbonus);
		if (hitbonus < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x18);// cribonus
		writeRaw(cribonus);
		if (cribonus < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x20);// acbonus
		writeRaw(acbonus);
		if (acbonus < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x28);// evasionbonus
		writeRaw(evasionbonus);
		if (evasionbonus < 0) {
			writeByte(MINUS_BYTES);
		}
	}
	
	void conInfo(int con, int str, int classType, int basemaxhp) {
		int hpRegen		= CalcConStat.hpRegen(con);
		int hpIncPotion	= CalcConStat.hpIncPotion(con);
		int carryBonus	= CalcStat.carryBonus(str, con);
		int maxHpInc	= CalcConStat.levelUpHp(classType, con);
		int length		= 7 + (hpRegen < 0 ? 10 : 1) + (hpIncPotion < 0 ? 10 : 1) + getBitSize(carryBonus) + (maxHpInc < 0 ? 10 : 1) + getBitSize(basemaxhp);
		writeRaw(0x32);
		writeRaw(length);
		writeRaw(0x08);// hpregen
		writeRaw(hpRegen);
		if (hpRegen < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x10);// hpincpotion
		writeRaw(hpIncPotion);
		if (hpIncPotion < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x18);// carrybonus
		writeBit(carryBonus);
		writeRaw(0x20);// maxhpinc
		writeRaw(maxHpInc);
		if (maxHpInc < 0) {
			writeByte(MINUS_BYTES);
		}
		writeRaw(0x28);// hitpoint
		writeRaw(0);
		writeRaw(0x30);// basemaxhp
		writeBit(basemaxhp);
	}
	
	void chaInfo(int cha) {
		int pierceAll			= CalcChaStat.pierceAll(cha);
		int decreaseCoolTime	= CalcChaStat.decreaseSpellCoolTime(cha);
		int decreaseCCDuration	= CalcChaStat.decreaseCCDuration(cha);
		int length				= 5 + getBitSize(pierceAll) + getBitSize(decreaseCoolTime) + getBitSize(decreaseCCDuration);
		writeRaw(0x3a);
		writeRaw(length);
		writeRaw(0x08);// dummy
		writeB(true);
		writeRaw(0x10);// pierceall
		writeBit(pierceAll);
		writeRaw(0x18);// decreasecooltime
		writeBit(decreaseCoolTime);
		writeRaw(0x20);// decreaseCCduration
		writeBit(decreaseCCDuration);
	}
	
	public S_StatusRenewalInfo(int infoType, int str, int inti, int wis, int dex, int con, int cha, int classType, L1PcInstance pc) {
		write_init();
		write_infotype(infoType << 1);
		if (str > 0) {
			strInfo(str, con);
		}
		if (inti > 0) {
			intInfo(inti);
		}
		if (wis > 0) {
			wisInfo(wis, classType, pc == null ? 0 : pc.getBaseMaxMp());
		}
		if (dex > 0) {
			dexInfo(dex);
		}
		if (con > 0) {
			conInfo(con, str, classType, pc == null ? 0 : pc.getBaseMaxHp());
		}
		if (cha > 0) {
			chaInfo(cha);
		}
		writeH(0x00);
	}
	
	public S_StatusRenewalInfo(int infoType, int stat, int stat2, L1Status statType, int classType, L1PcInstance pc) {
		write_init();
		write_infotype(infoType << 1);
		switch(statType){
		case STR:
			strInfo(stat, stat2);
			break;
		case INT:
			intInfo(stat);
			break;
		case WIS:
			wisInfo(stat, classType, pc == null ? 0 : pc.getBaseMaxMp());
			break;
		case DEX:
			dexInfo(stat);
			break;
		case CON:
			conInfo(stat, stat2, classType, pc == null ? 0 : pc.getBaseMaxHp());
			break;
		case CHA:
			chaInfo(stat);
			break;
		}
		writeH(0x00);
	}

	public S_StatusRenewalInfo(L1PcInstance pc, int infotype, L1Status statType) {
		L1Ability ability = pc.getAbility();
		write_init();
		write_infotype(infotype << 1);
		switch (statType) {
		case STR:
			strInfo(ability.getTotalStr(), ability.getTotalCon());
			break;
		case INT:
			intInfo(ability.getTotalInt());
			break;
		case WIS:
			wisInfo(ability.getTotalWis(), pc.getType(), pc == null ? 0 : pc.getBaseMaxMp());
			break;
		case DEX:
			dexInfo(ability.getTotalDex());
			break;
		case CON:
			conInfo(ability.getTotalCon(), ability.getTotalStr(), pc.getType(), pc == null ? 0 : pc.getBaseMaxHp());
			break;
		case CHA:
			chaInfo(ability.getTotalCha());
			break;
		default:break;
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(STAT_UPDATE);
	}
	
	void write_infotype(int infotype) {
		writeRaw(0x08);// infotype
		writeRaw(infotype);
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
		return S_STATUS_RENEWAL_INFO;
	}
}

