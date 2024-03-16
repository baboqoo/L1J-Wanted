package l1j.server.server.model.item.function;

import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Einhasad;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class DragonGemstone extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public DragonGemstone(L1Item item) {
		super(item);
	}
	
	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int itemId = this.getItemId();
			switch(itemId){
			case L1ItemId.DRAGON_DIAMOND:case L1ItemId.DRAGON_DIAMOND1:// 드래곤의 다이아몬드
				useDragonGemstone(pc, 100 * Config.EIN.REST_EXP_DEFAULT_RATION, true);
				break;
			case L1ItemId.DRAGON_HIGH_DIAMOND:// 드래곤의 고급 다이아몬드
				useDragonGemstone(pc, 800 * Config.EIN.REST_EXP_DEFAULT_RATION, true);
				break;
			case L1ItemId.DRAGON_FINEST_DIAMOND:// 드래곤의 최고급 다이아몬드
				useDragonGemstone(pc, 2000 * Config.EIN.REST_EXP_DEFAULT_RATION, true);
				break;
			case L1ItemId.DRAGON_SAPPHIRE:
				useDragonGemstone(pc, 50 * Config.EIN.REST_EXP_DEFAULT_RATION, true);
				break;
			case L1ItemId.DRAGON_RUBY:case L1ItemId.TEMPLAR_DRAGON_RUBY:
				useDragonGemstone(pc, 30 * Config.EIN.REST_EXP_DEFAULT_RATION, true);
				break;
			case L1ItemId.DRAGON_TOP_QUALITY_DIAMOND:// 드래곤의 고급 다이아몬드(각인)
				useDragonGemstone(pc, 600 * Config.EIN.REST_EXP_DEFAULT_RATION, true);
				break;
			case L1ItemId.DRAGON_DIAMOND_VERIANA:// 드래곤의 다이아몬드 베리아나
				useDragonGemstone(pc, 20 * Config.EIN.REST_EXP_DEFAULT_RATION, true);
				break;
			case L1ItemId.DRAGON_RUBI_VERIANA:// 드래곤의 루비 베리아나
				useDragonGemstone(pc, 5 * Config.EIN.REST_EXP_DEFAULT_RATION, true);
				break;
			case L1ItemId.DRAGON_SAPPHIRE_VERIANA:// 드래곤의 사파이어 베리아나
				useDragonGemstone(pc, 10 * Config.EIN.REST_EXP_DEFAULT_RATION, true);
				break;
			case L1ItemId.BLESSED_HOLY_WATER_DRAGON:// 드래곤의 축복 성수
				if (useDragonGemstone(pc, 2000 * Config.EIN.REST_EXP_DEFAULT_RATION, true)) {
					dragonBlessPotionExp(pc);
				}
				break;
			case 1000044:// 축복의 배지
				blessBage(pc);
				break;
			default:
				useEtcGemstone(pc, itemId);
				break;
			}
		}
	}
	
	private void blessBage(L1PcInstance pc){
		if (getLastUsed() != null && !CommonUtil.isDayResetTimeCheck(getLastUsed(), 6)) {
			pc.sendPackets(L1ServerMessage.sm1729);// 아직은 사용할 수 없습니다.
			return;
		}
		if (useDragonGemstone(pc, 2000 * Config.EIN.REST_EXP_DEFAULT_RATION, false)) {
			if (getLastUsed() != null) {
				getLastUsed().setTime(System.currentTimeMillis());
			} else {
				setLastUsed(new Timestamp(System.currentTimeMillis()));
			}
			pc.getInventory().saveItem(this, L1PcInventory.COL_SAVE_ALL);
		}
	}
	
	private boolean useDragonGemstone(L1PcInstance pc, int addValue, boolean remove){
		if (pc.getSkill().hasSkillEffect(L1SkillId.EMERALD_YES)) {// 에메랄드 먹고있을때는 못먹음
			pc.sendPackets(L1ServerMessage.sm2146);
			return false;
		}
		int max = Config.EIN.REST_EXP_LIMIT_CHARGE_VALUE;
		L1Einhasad ein = pc.getAccount().getEinhasad();
		if (ein.getPoint() > (max - addValue)) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(new StringBuilder().append("축복지수 ").append((max - addValue) / 10000).append("% 미만에서만 사용하실수 있습니다.").toString()), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(new StringBuilder().append(S_SystemMessage.getRefText(1066)).append((max - addValue) / 10000).append(S_SystemMessage.getRefText(1067)).toString(), true), true);
			return false;
		}
		ein.addPoint(addValue, pc);
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage(new StringBuilder().append("아인하사드의 축복: ").append(addValue / 10000).append("% 추가.").toString()), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(new StringBuilder().append(S_SystemMessage.getRefText(1068)).append(addValue / 10000).append(S_SystemMessage.getRefText(1069)).toString(), true), true);
		
		pc.sendPackets(new S_RestExpInfoNoti(pc), true);
		pc.sendPackets(new S_ExpBoostingInfo(pc), true);
		pc.send_effect(197);
		if (remove) {
			pc.getInventory().removeItem(this, 1);
		}
		return true;
	}
	
	private void useEtcGemstone(L1PcInstance pc, int itemId){
		boolean result = false;
		if (itemId == L1ItemId.EMERALD || itemId == L1ItemId.EMERALD1) {
			if (pc.getSkill().hasSkillEffect(L1SkillId.EMERALD_NO)) {
				pc.sendPackets(L1ServerMessage.sm2145);
				return;
			}
			if (pc.getSkill().hasSkillEffect(L1SkillId.EMERALD_YES)) {
				pc.sendPackets(L1ServerMessage.sm2147);
				return;
			}
			if ((pc.getSkill().hasSkillEffect(L1SkillId.DRAGON_TOPAZ)) || (pc.getSkill().hasSkillEffect(L1SkillId.DRAGON_PUPLE))) {
				pc.sendPackets(L1ServerMessage.sm2147);
				return;
			}
			pc.setEMETime(new Timestamp(System.currentTimeMillis() + 1800000));
			
			if (pc.getPUPLETime() != null) {
				pc.setPUPLETime(null);
			}
			if (pc.getTOPAZTime() != null) {
				pc.setTOPAZTime(null);
			}
			
			pc.getAccount().getEinhasad().addPoint(100 * Config.EIN.REST_EXP_DEFAULT_RATION, pc);
			pc.getSkill().setSkillEffect(L1SkillId.EMERALD_YES, 1800000);
			pc.sendPackets(new S_PacketBox(S_PacketBox.EMERALD_ICON, 2, 1800), true);
			pc.sendPackets(L1ServerMessage.sm2140);
			result = true;
		} else if (itemId == 60255) {// 자수정
			if (pc.getAccount().getEinhasad().getPoint() > 1 * Config.EIN.REST_EXP_DEFAULT_RATION) {
				if (pc.getSkill().hasSkillEffect(L1SkillId.EMERALD_YES)) {
					pc.sendPackets(L1ServerMessage.sm2146);
					return;
				}
				if (pc.getSkill().hasSkillEffect(L1SkillId.DRAGON_TOPAZ)) {
					pc.getSkill().removeSkillEffect(L1SkillId.DRAGON_TOPAZ);
				}
				long sysTime = System.currentTimeMillis();

				Timestamp deleteTime = new Timestamp(sysTime + 1800000);
				pc.setPUPLETime(deleteTime);
				
				if (pc.getEMETime() != null) {
					pc.setEMETime(null);
				}
				if (pc.getEMETime2() != null) {
					pc.setEMETime2(null);
				}
				if (pc.getTOPAZTime() != null) {
					pc.setTOPAZTime(null);
				}
				
				pc.getSkill().setSkillEffect(L1SkillId.DRAGON_PUPLE, 1800000);
				pc.sendPackets(S_PacketBox.EIN_PUPLE_ON);
				try {
					pc.save();
				} catch(Exception e){
				}
				if (pc.getLevel() <= 54) {
					pc.sendPackets(L1SystemMessage.EIN_1_54_VALUE);
				} else if (pc.getLevel() <= 55 && pc.getLevel() <= 59) {
					pc.sendPackets(L1SystemMessage.EIN_55_59_VALUE);
				} else if (pc.getLevel() <= 60 && pc.getLevel() <= 64) {
					pc.sendPackets(L1SystemMessage.EIN_60_64_VALUE);
				} else if (pc.getLevel() <= 65 && pc.getLevel() <= 69) {
					pc.sendPackets(L1SystemMessage.EIN_65_69_VALUE);
				}
				result = true;
			} else {
				pc.sendPackets(L1SystemMessage.EIN_VALUE_EMPTY_FAIL);
			}
		} else if (itemId == 7241) {// 토파즈
			if (pc.getAccount().getEinhasad().getPoint() > 1 * Config.EIN.REST_EXP_DEFAULT_RATION) {
				if (pc.getSkill().hasSkillEffect(L1SkillId.EMERALD_YES)) {
					pc.sendPackets(L1ServerMessage.sm2146);
					return;
				}
				if (pc.getSkill().hasSkillEffect(L1SkillId.DRAGON_PUPLE)) {
					pc.getSkill().removeSkillEffect(L1SkillId.DRAGON_PUPLE);
				}
				int topaztSec = 1800;
				int addtime = pc.getSkill().getSkillEffectTimeSec(L1SkillId.DRAGON_TOPAZ);
				topaztSec += addtime;
				if (topaztSec > 36000) {
					topaztSec = 36000;
				}
				
				if (pc.getEMETime() != null) {
					pc.setEMETime(null);
				}
				if (pc.getEMETime2() != null) {
					pc.setEMETime2(null);
				}
				if (pc.getPUPLETime() != null) {
					pc.setPUPLETime(null);
				}
				
				if (pc.getTOPAZTime() != null) {
					pc.getTOPAZTime().setTime(System.currentTimeMillis() + (long) (topaztSec * 1000));
				} else {
					pc.setTOPAZTime(new Timestamp(System.currentTimeMillis() + (long) (topaztSec * 1000)));
				}
				
				pc.getSkill().setSkillEffect(L1SkillId.DRAGON_TOPAZ, topaztSec * 1000);
				pc.sendPackets(new S_PacketBox(topaztSec, 2, true, true), true);
				try {
					pc.save();
				} catch(Exception e){
				}
				pc.sendPackets(L1SystemMessage.EIN_EXP_150_VALUE);
				result = true;
			} else {
				pc.sendPackets(L1SystemMessage.EIN_VALUE_EMPTY_FAIL);
			}
		}
		if (result) {
			pc.sendPackets(new S_RestExpInfoNoti(pc), true);
			pc.sendPackets(new S_ExpBoostingInfo(pc), true);
			pc.send_effect(197);
			pc.getInventory().removeItem(this, 1);
		}
	}
	
	private void dragonBlessPotionExp(L1PcInstance pc){
		long elixExp	= ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 60);// 52레벨 기준 60%
		if (pc.getExp() + elixExp >= L1ExpPlayer.LIMIT_EXP) {
			pc.sendPackets(L1SystemMessage.LIMIT_LEVEL_EXP_FAIL);
			return;
		}
	    if (pc.getLevel() >= 1 && (elixExp + pc.getExp() > ExpTable.getExpByLevel(pc.getLevel() + 1))) {
	    	elixExp = ExpTable.getExpByLevel(pc.getLevel() + 1) - pc.getExp();
	    }
		pc.addExp(elixExp);
	}
}


