package l1j.server.server.model.item.function;

import l1j.server.Config;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.controller.action.UserRanking;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class ExpPlusItem extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public ExpPlusItem(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			switch(this.getItemId()){
			case 700000:
				if (exp_1(pc)) {
					pc.getInventory().removeItem(this, 1);
				}
				break;
			case 700001:
				if (exp_2(pc)) {
					pc.getInventory().removeItem(this, 1);
				}
				break;
			case 700083:
				if (expRangking(pc)) {
					pc.getInventory().removeItem(this, 1);
				}
				break;
			case 31731:// 경험치 스크롤
				if (exp_scroll(pc)) {
					pc.getInventory().removeItem(this, 1);
				}
				break;
			}
		}
	}
	
	private boolean exp_1(L1PcInstance pc) {
		if (Config.ETC.EXP_POTION_MAX_LIMIT && pc.getExp() + 10000 >= L1ExpPlayer.LIMIT_EXP) {
			pc.sendPackets(L1SystemMessage.LIMIT_LEVEL_EXP_FAIL);
			return false;
		}
		if (pc.getLevel() <= 48) {
			pc.setExp(pc.getExp() + 326144);
		} else if (pc.getLevel() <= 64) {
			pc.setExp(pc.getExp() + 2609152);
		} else if (pc.getLevel() <= 69) {
			pc.setExp(pc.getExp() + 1304576);
		} else if (pc.getLevel() <= 74) {
			pc.setExp(pc.getExp() + 652288);
		} else if (pc.getLevel() <= 78) {
			pc.setExp(pc.getExp() + 326144);
		} else if (pc.getLevel() == 79) {
			pc.setExp(pc.getExp() + 163072);
		} else if (pc.getLevel() <= 81) {
			pc.setExp(pc.getExp() + 81536);
		} else if (pc.getLevel() <= 83) {
			pc.setExp(pc.getExp() + 40768);
		} else if (pc.getLevel() <= 85) {
			pc.setExp(pc.getExp() + 20384);
		} else if (pc.getLevel() == 86) {
			pc.setExp(pc.getExp() + 10192);
		} else if (pc.getLevel() == 87) {
			pc.setExp(pc.getExp() + 5096);
		} else if (pc.getLevel() == 88) {
			pc.setExp(pc.getExp() + 2048);
		} else if (pc.getLevel() == 89) {
			pc.setExp(pc.getExp() + 1024);
		} else if (pc.getLevel() == 90) {
			pc.setExp(pc.getExp() + 512);
		} else if (pc.getLevel() == 91) {
			pc.setExp(pc.getExp() + 256);
		} else if (pc.getLevel() == 92) {
			pc.setExp(pc.getExp() + 128);
		} else if (pc.getLevel() == 93) {
			pc.setExp(pc.getExp() + 64);
		} else if (pc.getLevel() == 94) {
			pc.setExp(pc.getExp() + 32);
		} else {
			pc.setExp(pc.getExp() + 16);
		}
		return true;
	}
	
	private boolean exp_2(L1PcInstance pc) {
		long exp = ExpTable.getExpFromLevelAndPercent(pc.getLevel(), pc.getLevel(), 1);
		if (pc.getExp() + exp >= L1ExpPlayer.LIMIT_EXP) {// 경험치
			pc.sendPackets(L1SystemMessage.LIMIT_LEVEL_EXP_FAIL);
			return false;
		}
		/** 폭렙 방지 **/
	    if (pc.getLevel() >= 1 && (exp + pc.getExp() > ExpTable.getExpByLevel(pc.getLevel() + 1))) {
	    	exp = ExpTable.getExpByLevel(pc.getLevel() + 1) - pc.getExp();
		}
		pc.addExp(exp);
		pc.send_effect(3944);
		return true;
	}
	
	private boolean exp_scroll(L1PcInstance pc) {
		int percent = getItem().getEtcValue();
		long exp	= ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, percent);
		if (pc.getExp() + exp >= L1ExpPlayer.LIMIT_EXP) {
			pc.sendPackets(L1SystemMessage.LIMIT_LEVEL_EXP_FAIL);
			return false;
		}
		/** 폭렙 방지 **/
	    if (exp + pc.getExp() > ExpTable.getExpByLevel(pc.getLevel() + 1)) {
	    	exp = (ExpTable.getExpByLevel(pc.getLevel() + 1) - pc.getExp());
		}
	    pc.addExp(exp);
		return true;
	}
	
	private boolean expRangking(L1PcInstance pc){
		final int ranking = getItem().getEtcValue();
    	long needExp = UserRanking.getInstance().getRankingExp(ranking);
    	if (pc.getExp() >= needExp) {
    		pc.sendPackets(L1SystemMessage.RANKING_POTION_FAIL);
    		return false;
    	}
    	pc.setExp(needExp + 1);
		//pc.sendPackets(new S_SystemMessage(String.format("%d위 랭킹의 경험치를 획득하였습니다.", ranking)), true);
		pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(95), String.valueOf(ranking)), true);
    	return true;
	}
}


