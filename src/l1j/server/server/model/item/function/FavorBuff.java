package l1j.server.server.model.item.function;

import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ExpBoostingInfo;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.templates.L1Item;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class FavorBuff extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public FavorBuff(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int itemId = this.getItemId();
			switch(itemId){
			case 51165:case 51166:case 51167:case 51168:
				favorOfEinhasad(pc, itemId);
				break;
			case 51160:case 51161:case 51162:
				favorOfHero(pc, itemId);
				break;
			case 51170:case 51171:case 51172:
				favorOfLife(pc, itemId);
				break;
			}
		}
	}
	
	/**
	 * 아인하사드의 가호
	 * @param pc
	 * @param itemId
	 */
	void favorOfEinhasad(L1PcInstance pc, int itemId) {
		//System.out.println("en favorOfEinhasad()");
		//pc.sendPackets(new S_SystemMessage("en favorOfEinhasad()", true), true);		
		if (pc.getSkill().hasSkillEffect(L1SkillId.EINHASAD_FAVOR)) {
			//pc.sendPackets(L1ServerMessage.sm7030);// 아인하사드의 가호 효과를 이미 받고 있습니다.
			//System.out.println("en favorOfEinhasad() Ya está el einhasad activado.");
			//pc.sendPackets(new S_SystemMessage("en favorOfEinhasad(). Ya está el einhasad activado.", true), true);		
			favorOfEinhasadScheduled(pc);
			return;
		}
		long sysTime = System.currentTimeMillis();
		try {
			if (!pc._isDragonFavor && pc.getAccount().getEinhasad().getPoint() < Config.EIN.REST_EXP_DEFAULT_RATION && !pc.isPCCafe()) {
				pc.getAccount().getEinhasad().setDragonFavor(pc, true);
			}
			int time = 0;
			if (itemId == 51165) {
				time = 86400 * 3;
			} else if (itemId == 51166) {
				time = 86400 * 7;
			} else if (itemId == 51167) {
				time = 86400 * 30;
			} else if (itemId == 51168) {
				time = 86400 * 15;
			}
			//System.out.println("en favorOfEinhasad() Activando el einhasad para ." + time);
			//pc.sendPackets(new S_SystemMessage("en favorOfEinhasad(). Activando el einhasad para ." + time, true), true);		
			pc.getSkill().setSkillEffect(L1SkillId.EINHASAD_FAVOR, (long) time * 1000);
			if (pc.getEinhasadGraceTime() != null) {
				pc.getEinhasadGraceTime().setTime(sysTime + (time * 1000));
			} else {
				pc.setEinhasadGraceTime(new Timestamp(sysTime + (time * 1000)));
			}
			pc.sendPackets(new S_SpellBuffNoti(pc, L1SkillId.EINHASAD_FAVOR, true, time), true);// 아이콘 출력
			pc.sendPackets(new S_RestExpInfoNoti(pc), true);
			pc.sendPackets(new S_ExpBoostingInfo(pc), true);
			pc.getInventory().removeItem(this, 1);
			pc.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void favorOfEinhasadScheduled(L1PcInstance pc) {
		if (isScheduled()) {
			setScheduled(false);
			pc.sendPackets(L1ServerMessage.sm7803);// 아인하사드의 가호 예약이 취소되었습니다.
			pc.getInventory().updateItem(this, L1PcInventory.COL_SCHEDULED);
			pc.getInventory().saveItem(this, L1PcInventory.COL_SCHEDULED);
			return;
		}
		for (L1ItemInstance item : pc.getInventory().getItems()) {
			if (item.isScheduled() && item.getIconId() == this.getIconId()) {
				pc.sendPackets(L1ServerMessage.sm7804);// 이미 예약되어 추가 예약은 불가능 합니다.
				return;
			}
		}
		setScheduled(true);
		pc.sendPackets(L1ServerMessage.sm7802);// 아인하사드의 가호가 예약 되었습니다.
		pc.getInventory().updateItem(this, L1PcInventory.COL_SCHEDULED);
		pc.getInventory().saveItem(this, L1PcInventory.COL_SCHEDULED);
	}
	
	/**
	 * 영웅의 가호
	 * @param pc
	 * @param itemId
	 */
	void favorOfHero(L1PcInstance pc, int itemId) {
		if(pc.getSkill().hasSkillEffect(L1SkillId.HERO_FAVOR)){
			pc.sendPackets(L1SystemMessage.HERO_BUFF_RE_USE);
			return;
		}
		long sysTime = System.currentTimeMillis();
		try {
			int time = 0;
			if (itemId == 51160) {
				time = 86400 * 3;
			} else if (itemId == 51161) {
				time = 86400 * 7;
			} else if (itemId == 51162) {
				time = 86400 * 30;
			}
			pc.getSkill().setSkillEffect(L1SkillId.HERO_FAVOR, (long) time * 1000);
			pc.sendPackets(new S_SpellBuffNoti(pc, L1SkillId.HERO_FAVOR, true, time), true);// 아이콘 출력
			if (pc.getAccount().getBuff_HERO() != null) {
				pc.getAccount().getBuff_HERO().setTime(sysTime + (time * 1000));
			} else {
				pc.getAccount().setBuff_HERO(new Timestamp(sysTime + (time * 1000)));
			}
			pc.getAbility().addAddedStr((byte) 1);
			pc.getAbility().addAddedDex((byte) 1);
			pc.getAbility().addAddedInt((byte) 1);
			pc.getAbility().addShortHitup(3);
			pc.getAbility().addLongHitup(3);
			pc.getAbility().addMagicHitup(3);
			pc.getResistance().addHitupAll(3);
			pc.getAbility().addPVPDamage(3);
			pc.sendPackets(new S_SPMR(pc), true);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.getInventory().removeItem(this, 1);
			pc.getAccount().updateHeroBuff();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 생명의 가호
	 * @param pc
	 * @param itemId
	 */
	void favorOfLife(L1PcInstance pc, int itemId) {
		if (pc.getSkill().hasSkillEffect(L1SkillId.LIFE_FAVOR)) {
			pc.sendPackets(L1SystemMessage.LIFE_BUFF_RE_USE);
			return;
		}
		long sysTime = System.currentTimeMillis();
		try {
			int time = 0;
			if (itemId == 51170) {
				time = 86400 * 3;
			} else if (itemId == 51171) {
				time = 86400 * 7;
			} else if (itemId == 51172) {
				time = 86400 * 30;
			}
			pc.getSkill().setSkillEffect(L1SkillId.LIFE_FAVOR, (long) time * 1000);
			pc.sendPackets(new S_SpellBuffNoti(pc, L1SkillId.LIFE_FAVOR, true, time), true);// 아이콘 출력
			Account account = pc.getAccount();
			if (account.getBuff_LIFE() != null) {
				account.getBuff_LIFE().setTime(sysTime + (time * 1000));
			} else {
				account.setBuff_LIFE(new Timestamp(sysTime + (time * 1000)));
			}
			pc.addMaxHp(300);
			pc.addMaxMp(100);
			pc.getInventory().removeItem(this, 1);
			account.updateLifeBuff();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

