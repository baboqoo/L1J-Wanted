package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.CalcConStat;
import l1j.server.server.utils.CalcWisStat;

public class S_StatusBaseInfo extends ServerBasePacket {
	private static final String S_STATUS_BASE_INFO	= "[S] S_StatusBaseInfo";
	private byte[] _byte							= null;
	public static final int BASE_STATUS				= 0x01e7;
	
	public static final S_StatusBaseInfo STAT_12	= new S_StatusBaseInfo(12);
	public static final S_StatusBaseInfo STAT_25	= new S_StatusBaseInfo(25);
	public static final S_StatusBaseInfo STAT_35	= new S_StatusBaseInfo(35);
	public static final S_StatusBaseInfo STAT_45	= new S_StatusBaseInfo(45);
	public static final S_StatusBaseInfo STAT_55	= new S_StatusBaseInfo(55);
	public static final S_StatusBaseInfo STAT_60	= new S_StatusBaseInfo(60);
	
	private static final byte[] MINUS_BYTES			= { 
		(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0x01 
	};
	
	public S_StatusBaseInfo(int stat) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(BASE_STATUS);

		if (stat == 12) {
			writeRaw(0x0a);// STR
			writeRaw(11);
			writeRaw(0x08);
			writeByte(MINUS_BYTES);
			
			writeRaw(0x12);// INT
			writeRaw(11);
			writeRaw(0x08);
			writeByte(MINUS_BYTES);
			
			writeRaw(0x1a);// WIS
			writeRaw(11);
			writeRaw(0x08);
			writeByte(MINUS_BYTES);
			
			writeRaw(0x22);// DEX
			writeRaw(11);
			writeRaw(0x08);
			writeByte(MINUS_BYTES);
			
			writeRaw(0x2a);// CON
			writeRaw(11);
			writeRaw(0x08);
			writeByte(MINUS_BYTES);
			
			writeRaw(0x32);// CHA
			writeRaw(8);
			writeRaw(0x08);// basecha
			writeRaw(stat);
			writeRaw(0x10);// pierceall
			writeRaw(1);
			writeRaw(0x18);// decreasecooltime
			writeRaw(100);
			writeRaw(0x20);// decreaseCCduration
			writeRaw(100);
		} else {
			// STR
			int shortDmg	= stat == 60 || stat == 55 ? 5 : stat == 45 ? 3 : 1;
			int shortHit	= stat == 60 || stat == 55 ? 5 : stat == 45 ? 3 : 1;
			int shortCri	= stat == 60 || stat == 55 ? 2 : stat == 45 ? 1 : 0;
			int strLength	= 3 + getBitSize(stat) + getBitSize(shortDmg) + getBitSize(shortHit) + (shortCri > 0 ? getBitSize(shortCri) + 1 : 0);
			writeRaw(0x0a);
			writeRaw(strLength);
			writeRaw(0x08);// basestr
			writeRaw(stat);
			writeRaw(0x10);// damagebonus 근거리대미지
			writeRaw(shortDmg);
			writeRaw(0x18);// hitbonus 근거리 명중
			writeRaw(shortHit);
			if (shortCri > 0) {
				writeRaw(0x20);// cribonus 근거리치명타
				writeRaw(shortCri);
			}
			// 0x28 carrybonus
			
			// INT
			int magicDmg	= stat == 60 || stat == 55 ? 5 : stat == 45 ? 3 : 1;
			int magicHit	= stat == 60 || stat == 55 ? 5 : stat == 45 ? 3 : 1;
			int magicCri	= stat == 60 || stat == 55 ? 2 : stat == 45 ? 1 : 0;
			int intiLength	= 3 + getBitSize(stat) + getBitSize(magicDmg) + getBitSize(magicHit) + (magicCri > 0 ? getBitSize(magicCri) + 1 : 0);
			writeRaw(0x12);
			writeRaw(intiLength);
			writeRaw(0x08);// baseint
			writeRaw(stat);
			writeRaw(0x10);// damagebonus 마법대미지
			writeRaw(magicDmg);
			writeRaw(0x18);// hitbonus 마법 명중
			writeRaw(magicHit);
			if (magicCri > 0) {
				writeRaw(0x20);// cribonus 마법치명타
				writeRaw(magicCri);
			}
			// 0x28 magicbonus
			// 0x30 reducemp
			
			// WIS
			int mpregen		= stat == 60 || stat == 55 ? 5 : stat == 45 ? 3 : 1;
			int mpPotion	= stat == 60 || stat == 55 ? 5 : stat == 45 ? 3 : 1;
			int addMpInc	= CalcWisStat.increaseMpBonus(stat);
			int wisLength	= 4 + getBitSize(stat) + getBitSize(mpregen) + getBitSize(mpPotion) + getBitSize(addMpInc);
			writeRaw(0x1a);
			writeRaw(wisLength);
			writeRaw(0x08);// basewis
			writeRaw(stat);
			writeRaw(0x10);// mpregen 엠틱
			writeRaw(mpregen);
			writeRaw(0x18);// mpincpotion 물약회복증가
			writeRaw(mpPotion);
			// 0x20 mrbonus
			// 0x28 maxmplow
			// 0x30 maxmphigh
			writeRaw(0x38);// addmpinc 엠피 증가 50 100 150 200
			writeBit(addMpInc);
			
			// DEX
			int longDmg		= stat == 60 || stat == 55 ? 5 : stat == 45 ? 3 : 1;
			int longHit		= stat == 60 || stat == 55 ? 5 : stat == 45 ? 3 : 1;
			int longCri		= stat == 60 || stat == 55 ? 2 : stat == 45 ? 1 : 0;
			int dexLength	= 3 + getBitSize(stat) + getBitSize(longDmg) + getBitSize(longHit) + (longCri > 0 ? getBitSize(longCri) + 1 : 0);
			writeRaw(0x22);
			writeRaw(dexLength);
			writeRaw(0x08);// basedex
			writeRaw(stat);
			writeRaw(0x10);// damagebonus 원거리대미지
			writeRaw(longDmg);
			writeRaw(0x18);// hitbonus 원거리 명중
			writeRaw(longHit);
			if (longCri > 0) {
				writeRaw(0x20);// cribonus 원거리치명타
				writeRaw(longCri);
			}
			// 0x28 acbonus
			// 0x30 evasionbonus
			
			// CON
			int hpregen		= stat == 60 || stat == 55 ? 5 : stat == 45 ? 3 : 1;
			int hpPotion	= stat == 60 || stat == 55 ? 4 : stat == 45 ? 2 : stat == 35 ? 1 : 0;
			int addHpInc	= CalcConStat.increaseHpBonus(stat);
			int conLength	= 3 + getBitSize(stat) + getBitSize(hpregen) + getBitSize(addHpInc) + (hpPotion > 0 ? getBitSize(hpPotion) + 1 : 0);
			writeRaw(0x2a);
			writeRaw(conLength);
			writeRaw(0x08);// basecon
			writeRaw(stat);
			writeRaw(0x10);// hpregen 피틱
			writeRaw(hpregen);
			if (hpPotion > 0) {
				writeRaw(0x18);// hpincpotion 물약회복증가
				writeRaw(hpPotion);
			}
			// 0x20 carrybonus
			// 0x28 maxhpinc
			writeRaw(0x30);// addhpinc
			writeBit(addHpInc);
			
			// CHA
			int allHit		= stat == 25 ? 0 : 1;
			int coolTime	= stat == 60 || stat == 25 ? 100 : 0;
			int duration	= stat == 60 || stat == 25 ? 100 : 0;
			int chaLength	= 1 + getBitSize(stat) + (allHit > 0 ? getBitSize(allHit) + 1 : 0) + (coolTime > 0 ? getBitSize(coolTime) + 1 : 0) + (duration > 0 ? getBitSize(duration) + 1 : 0);
			writeRaw(0x32);
			writeRaw(chaLength);
			writeRaw(0x08);// basecha
			writeRaw(stat);
			if (allHit > 0) {
				writeRaw(0x10);// pierceall
				writeRaw(allHit);
			}
			if (coolTime > 0) {
				writeRaw(0x18);// decreasecooltime
				writeBit(coolTime);
			}
			if (duration > 0) {
				writeRaw(0x20);// decreaseCCduration
				writeBit(duration);
			}
		}

		writeH(0x00);
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
		return S_STATUS_BASE_INFO;
	}
}

