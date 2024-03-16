package l1j.server.server.model.exp;

import l1j.server.Config;
import l1j.server.server.datatables.CharacterCompanionTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.companion.S_CompanionStatusNoti;
import l1j.server.server.serverpackets.inventory.S_ItemStatus;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;

/**
 * 펫 겸험치 처리 핸들러(L1ExpHandler 상속)
 * @author LinOffice
 */
public class L1ExpPet extends L1ExpHandler {
	public static long LIMIT_EXP = getLimitExp();
	
	public L1ExpPet(L1PetInstance owner) {
		super(owner);
	}

	@Override
	public void addExp(long exp, int align) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addExp(long exp) {
		if (_pet.is_minus_exp_penalty()) {
			return;
		}
	    L1PcInstance master	= (L1PcInstance)_pet.getMaster();
	    if (master == null) {
	    	return;
	    }
	    
	    int levelBefore		= _pet.getLevel();
	    double expPenalty	= ExpTable.getPenalty(_pet.getLevel());
	    long addExp			= (long)(exp * Config.RATE.RATE_XP * _pet.get_bonus_exp_rate() * expPenalty);
	    long totalExp		= (addExp + _pet.getExp());
	    if (totalExp >= LIMIT_EXP) {
	    	return;
	    }
	    
	    L1Pet template		= _pet.getPetInfo();
	    _pet.setExp(totalExp);
	    template.setExp(_pet.getExp());
	    int afterLevel = ExpTable.getLevelByExp(totalExp);
	    if (afterLevel != levelBefore) {
	    	_pet.setLevel(afterLevel);
		    template.setLevel(afterLevel);
	    }
	    // 투지 게이지
	    int friendshipGuageBonus = (int) Math.round((exp) / 100.0D) * 100;
	    friendshipGuageBonus *= Config.COMPANION.FRIENDSHIP_GUAGE_RATE;
	    friendshipGuageBonus *= _pet.get_bonus_friendship_rate();
		template.add_friend_ship_guage(IntRange.ensure(friendshipGuageBonus, 5000, 50000));// 최소 5프로 최대 50프로
	    if (template.get_friend_ship_guage() >= Config.COMPANION.COMBO_ENABLE_POINT) { // 100프로 달성
	    	// 우정 포인트 획득
	    	template.add_friend_ship_marble(Config.COMPANION.FRIENDSHIP_MARBLE_VALUE);
	    	master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.FRIENDSHIP, _pet), true);
	        template.set_friend_ship_guage(0);
	        _pet.startCombo();// 투지 발동
	    } else {
	    	master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.FRIENDSHIP_GUAGE, _pet), true);
	    }

	    int gap = afterLevel - levelBefore;
	    if (gap > 0) {
	        for (int i = 1; i <= gap; i++) {
	        	_pet.addMaxHp(_pet.get_add_min_HP() + CommonUtil.random(_pet.get_add_max_HP() - _pet.get_add_min_HP() + 1));
	        }
	        int currentBonus	= _pet.getAbility().getAddedStr() + _pet.getAbility().getAddedCon() + _pet.getAbility().getAddedInt() + template.get_remain_stats() - template.get_elixir_use_count();
	        int finalBonus		= IntRange.ensure(afterLevel / 10, 0, 5) + IntRange.ensure(afterLevel - 50, 0, 30);
	        if (finalBonus > currentBonus) {
	        	template.add_remain_stats(finalBonus - currentBonus);
	        }
	        _pet.doStatBonus(false);
	        CharacterCompanionTable.getInstance().saveAll(_pet);
	        master.sendPackets(new S_ServerMessage(320, _pet.getName()), true);
	        master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.LEVELUP, _pet), true);
	        master.sendPackets(new S_ItemStatus(_pet.getAmulet(), master), true);
	        _pet.send_companion_card();
	    } else {
	    	master.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.EXP, _pet), true);
	    }
	}
	
	public static long getLimitExp(){
		return ExpTable.getExpByLevel(Config.COMPANION.MAX_LEVEL + 1);
	}

}

