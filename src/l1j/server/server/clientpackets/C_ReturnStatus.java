package l1j.server.server.clientpackets;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.GameClient;
import l1j.server.server.construct.L1BaseStatus;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.L1StatReset;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.returnedstat.S_ReturnedStatElixir;
import l1j.server.server.serverpackets.returnedstat.S_ReturnedStatFinish;
import l1j.server.server.serverpackets.returnedstat.S_ReturnedStatLevelup;
import l1j.server.server.serverpackets.returnedstat.S_ReturnedStatStart;
import l1j.server.server.utils.CalcConStat;
import l1j.server.server.utils.CalcDexStat;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.CalcWisStat;

public class C_ReturnStatus extends ClientBasePacket {
	static final ConcurrentHashMap<Integer, Integer> ELIXIR_LEVELS = new ConcurrentHashMap<Integer, Integer>();
	
	private L1PcInstance pc;
	private L1StatReset sr;
	
	public C_ReturnStatus(byte[] decrypt, GameClient client) {
		super(decrypt);
		try {
			int type	= readC();
			pc			= client.getActiveChar();
			if (pc == null || pc.getReturnStat_exp() == 0 || pc.getReturnStatus() != type) {
				return;
			}
			switch(type){
			case 2:levelUpStatus();break;// 레벨업
			case 1:baseStatus();break;// 베이스 스탯
			default:return;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void levelUpStatus(){
		sr = pc.getStatReset();
		if (sr == null) {
			return;
		}
		int levelup	= readC();// 레벨업 구분
		if (levelup != 8 && sr.getEndLevel() <= sr.getNowLevel()) {
			pc.sendPackets(new S_ReturnedStatLevelup(sr), true);
			return;
		}
		switch (levelup) {
		case 0:onlyLevelUpStatus();break;
		case 1:case 2:case 3:case 4:case 5:case 6:statusLevelUp(levelup);break;
		case 7:rangeLevelUp();break;
		case 8:complete();break;
		case 14:elixerStatus();break;
		default:break;
		}
	}
	
	void statusLevelUp(int status) {
		switch (status) {
		case 1:sr.addStr();break;
		case 2:sr.addIntel();break;
		case 3:sr.addWis();break;
		case 4:sr.addDex();break;
		case 5:sr.addCon();break;
		case 6:sr.addCha();break;
		}
		levelUp();
		pc.sendPackets(new S_ReturnedStatLevelup(sr), true);
	}
	
	void levelUp() {
		sr.addNowLevel();
		elixir();
		sr.setAC(CalcDexStat.acBonus(sr.getDex()));
		sr.setMaxHp(sr.getMaxHp() + CalcConStat.levelUpHp(pc.getType(), sr.getMaxHp(), sr.getCon()));
		sr.setMaxMp(sr.getMaxMp() + CalcWisStat.levelUpMp(pc.getType(), sr.getMaxMp(), sr.getWis()));
	}
	
	void elixerStatus(){
		sr.setStr(readC());
		sr.setIntel(readC());
		sr.setWis(readC());
		sr.setDex(readC());
		sr.setCon(readC());
		sr.setCha(readC());
		levelUp();
		pc.sendPackets(new S_ReturnedStatLevelup(sr), true);
	}
	
	void elixir() {
		int elixerCount = pc.getElixirStats();
		if (elixerCount <= 0) {
			return;
		}
		int endLevel = sr.getEndLevel(), nowLevel = sr.getNowLevel();
		if ((endLevel == 100 && nowLevel == endLevel - 1 && elixerCount > 27) || (endLevel > nowLevel && nowLevel == 100 && elixerCount > 27)) {
			pc.sendPackets(elixerCount > 30 ? S_ReturnedStatElixir.ELIXIR_4 : elixerCount > 29 ? S_ReturnedStatElixir.ELIXIR_3 : elixerCount > 28 ? S_ReturnedStatElixir.ELIXIR_2 : S_ReturnedStatElixir.ELIXIR_1);
		} else if ((endLevel == 90 && nowLevel == endLevel - 1 && elixerCount > 21) || (endLevel > nowLevel && nowLevel == 90 && elixerCount > 21)) {
			pc.sendPackets(elixerCount > 22 ? S_ReturnedStatElixir.ELIXIR_2 : S_ReturnedStatElixir.ELIXIR_1);
		} else if ((endLevel == 80 && nowLevel == endLevel - 1 && elixerCount > 15) || (endLevel > nowLevel && nowLevel == 80 && elixerCount > 15)) {
			pc.sendPackets(elixerCount > 16 ? S_ReturnedStatElixir.ELIXIR_2 : S_ReturnedStatElixir.ELIXIR_1);
		} else {
			Integer endLevelCount = ELIXIR_LEVELS.get(endLevel), nowLevelCount = ELIXIR_LEVELS.get(nowLevel);
			if ((endLevelCount != null && nowLevel == endLevel - 1 && elixerCount > endLevelCount) || (nowLevelCount != null && endLevel > nowLevel && elixerCount > nowLevelCount)) {
				pc.sendPackets(S_ReturnedStatElixir.ELIXIR_1);
			}
		}
	}
	
	void onlyLevelUpStatus() {
		levelUp();
		pc.sendPackets(new S_ReturnedStatLevelup(sr), true);
	}
	
	static {
		ELIXIR_LEVELS.put(50, 0);
		ELIXIR_LEVELS.put(52, 1);
		ELIXIR_LEVELS.put(54, 2);
		ELIXIR_LEVELS.put(56, 3);
		ELIXIR_LEVELS.put(58, 4);
		ELIXIR_LEVELS.put(60, 5);
		ELIXIR_LEVELS.put(62, 6);
		ELIXIR_LEVELS.put(64, 7);
		ELIXIR_LEVELS.put(66, 8);
		ELIXIR_LEVELS.put(68, 9);
		ELIXIR_LEVELS.put(70, 10);
		ELIXIR_LEVELS.put(72, 11);
		ELIXIR_LEVELS.put(74, 12);
		ELIXIR_LEVELS.put(76, 13);
		ELIXIR_LEVELS.put(78, 14);
		ELIXIR_LEVELS.put(80, 15);
		ELIXIR_LEVELS.put(82, 17);
		ELIXIR_LEVELS.put(84, 18);
		ELIXIR_LEVELS.put(86, 19);
		ELIXIR_LEVELS.put(88, 20);
		ELIXIR_LEVELS.put(90, 21);
		ELIXIR_LEVELS.put(92, 23);
		ELIXIR_LEVELS.put(94, 24);
		ELIXIR_LEVELS.put(96, 25);
		ELIXIR_LEVELS.put(98, 26);
		ELIXIR_LEVELS.put(100, 27);
		for (int i=101; i<=110; i++) {
			ELIXIR_LEVELS.put(i, 30 + (i - 101));
		}
	}
	
	void baseStatus(){
		byte str	= (byte) readC();
		byte intel	= (byte) readC();
		byte wis	= (byte) readC();
		byte dex	= (byte) readC();
		byte con	= (byte) readC();
		byte cha	= (byte) readC();
		baseStatSetting(str, intel, wis, dex, con, cha);
	}
	
	void baseStatSetting(byte str, byte intel, byte wis, byte dex, byte con, byte cha){
		int statusAmount = str + intel + wis + dex + con + cha;
		if (str > 20 || intel > 20 || dex > 20 || wis > 20 || con > 20 || cha > 20 || statusAmount != 75) {
			pc.setReturnStatus(1);
			pc.sendPackets(new S_ReturnedStatStart(pc), true);
			return;
		}
		sr = new L1StatReset();
		sr.setStr(str);
		sr.setWis(wis);
		sr.setDex(dex);
		sr.setCon(con);
		sr.setIntel(intel);
		sr.setCha(cha);
		sr.setBaseStr(str);
		sr.setBaseWis(wis);
		sr.setBaseDex(dex);
		sr.setBaseCon(con);
		sr.setBaseIntel(intel);
		sr.setBaseCha(cha);
		sr.setMaxHp(CalcStat.startHp(pc.getType()));
		sr.setMaxMp(CalcStat.startMp(pc.getType(), sr.getBaseWis()));
		sr.setAC(10);
		sr.setNowLevel(1);
		sr.setEndLevel(ExpTable.getLevelByExp(pc.getReturnStat_exp()));
		pc.setStatReset(sr);
		pc.setReturnStatus(2);
		pc.sendPackets(new S_ReturnedStatLevelup(sr), true);
	}
	
	void rangeLevelUp(){
		if (sr.getNowLevel() >= 40) {
			return;
		}
		for (int cnt = 0; cnt < 10; cnt++) {
			sr.addNowLevel();
			sr.setMaxHp(sr.getMaxHp() + CalcConStat.levelUpHp(pc.getType(), sr.getMaxHp(), sr.getCon()));
			sr.setMaxMp(sr.getMaxMp() + CalcWisStat.levelUpMp(pc.getType(), sr.getMaxMp(), sr.getWis()));
		}
		elixir();
		sr.setAC(CalcDexStat.acBonus(sr.getDex()));
		pc.sendPackets(new S_ReturnedStatLevelup(sr), true);
	}
	
	void complete() {
		if (sr.getEndLevel() != sr.getNowLevel()) {
			pc.sendPackets(new S_ReturnedStatLevelup(sr), true);
			return;
		}
		complete(readC());
	}
	
	void complete(int statusup){
		if (pc.getLevel() > 50) {
			switch (statusup) {
			case 1:sr.addStr();break;
			case 2:sr.addIntel();break;
			case 3:sr.addWis();break;
			case 4:sr.addDex();break;
			case 5:sr.addCon();break;
			case 6:sr.addCha();break;
			}
		}
		try {
			if (!is_complete()) {
				pc.setReturnStatus(1);
				pc.sendPackets(new S_ReturnedStatStart(pc), true);
				return;
			}
			setCharStatus();
			pc.setExp(pc.getReturnStat_exp());
			pc.setReturnStat_exp(0);
			pc.setReturnStatus(0);
			pc.setBonusStats(pc.getLevel() >= 51 ? pc.getLevel() - 50 : 0);
			pc.sendPackets(new S_ReturnedStatFinish(pc, 0), true);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(new S_OwnCharAttrDef(pc), true);
			pc.setCurrentHp(pc.getMaxHp());
			pc.setCurrentMp(pc.getMaxHp());
			if (pc.isClanBuff()) {
				pc.sendPackets(S_PacketBox.CLAN_BUFF_ON);
				pc.sendPackets(new S_ExpBoostingInfo(pc), true);
			}
			pc.sendPackets(new S_RestExpInfoNoti(pc), true);
			pc.getTeleport().start(32612, 32734, (short) 4, 5, true);
			pc.LoadCheckStatus();
			pc.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void setCharStatus() {
		int addHp = sr.getMaxHp() - pc.getBaseMaxHp();
		int addMp = sr.getMaxMp() - pc.getBaseMaxMp();
		L1Ability ability = pc.getAbility();
		ability.init();// 스탯 초기화
		ability.setBaseStr(sr.getBaseStr());
		ability.setBaseInt(sr.getBaseIntel());
		ability.setBaseWis(sr.getBaseWis());
		ability.setBaseDex(sr.getBaseDex());
		ability.setBaseCon(sr.getBaseCon());
		ability.setBaseCha(sr.getBaseCha());
		ability.setStr(sr.getStr());
		ability.setInt(sr.getIntel());
		ability.setWis(sr.getWis());
		ability.setDex(sr.getDex());
		ability.setCon(sr.getCon());
		ability.setCha(sr.getCha());
		pc.addBaseMaxHp(addHp);
		pc.setCurrentHp(pc.getBaseMaxHp());
		pc.addBaseMaxMp(addMp);
		pc.setCurrentMp(pc.getBaseMaxMp());
		pc.setLevel(sr.getNowLevel());
		pc.resetBaseAc();
		pc.resetBaseMr();
	}
	
	/**
	 * 스탯 초기화가 정상적 완료 검사
	 * @return boolean
	 */
	boolean is_complete() {
		int baseTotal	= sr.getBaseStatusAmount();
		int total		= sr.getStatusAmount();
		int levelBonus	= sr.getEndLevel() - 50;// 보너스 스테이터스
		int increase	= total - baseTotal;// 총 증가 보너스 수치
		if (levelBonus + pc.getElixirStats() != increase) {// 스탯 수치 체크(레벨 보너스 + 엘릭서 수치 != 완료된 증가 보너스 수치)
			return false;
		}
		int checkstate	= pc.getLevel() >= 100 ? 60 : pc.getLevel() >= 90 ? 55 : 50;
		if (sr.getStr() > checkstate || sr.getDex() > checkstate || sr.getIntel() > checkstate || sr.getWis() > checkstate || sr.getCha() > checkstate || sr.getCon() > checkstate) {
			return false;
		}
		L1BaseStatus base = L1BaseStatus.fromInt(pc.getType());
		if (sr.getStr() < base.get_base_str() || sr.getDex() < base.get_base_dex() || sr.getCon() < base.get_base_con() 
				|| sr.getWis() < base.get_base_wis() || sr.getCha() < base.get_base_cha() || sr.getIntel() < base.get_base_int()) {
			return false;
		}
		if (sr.getBaseStr() < base.get_base_str() || sr.getBaseDex() < base.get_base_dex() || sr.getBaseCon() < base.get_base_con() 
				|| sr.getBaseWis() < base.get_base_wis() || sr.getBaseCha() < base.get_base_cha() || sr.getBaseIntel() < base.get_base_int()) {
			return false;
		}
		return true;
	}
	
	private static final String C_RETURN_STATUS = "[C] C_ReturnStatus";
	
	@Override
	public String getType() {
		return C_RETURN_STATUS;
	}
}
