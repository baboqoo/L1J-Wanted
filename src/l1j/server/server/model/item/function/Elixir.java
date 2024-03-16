package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.exp.L1ExpPlayer;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.returnedstat.S_TotalDrinkedElixirNoti;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class Elixir extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public Elixir(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int itemId = this.getItemId();
			switch (itemId) {
			case 820020:// 엘릭서(EXP)
				elixirEXP(pc);
				break;
			case 820021:// 엘릭서 추출 물약
				elixirExtract(pc);
				break;
			default:// 엘릭서
				elixir(pc, itemId);
				break;
			}
		}
	}
	
	void elixirEXP(L1PcInstance pc){
		long elixExp	= ExpTable.getExpFromLevelAndPercent(pc.getLevel(), 52, 540);// 52레벨 기준 540%
		if (pc.getExp() + elixExp >= L1ExpPlayer.LIMIT_EXP) {
			pc.sendPackets(L1SystemMessage.LIMIT_LEVEL_EXP_FAIL);
			return;
		}
	    if (pc.getLevel() >= 1 && (elixExp + pc.getExp() > ExpTable.getExpByLevel(pc.getLevel() + 1))) {
	    	elixExp = (ExpTable.getExpByLevel(pc.getLevel() + 1) - pc.getExp());
	    }
		pc.addExp(elixExp);
		pc.send_effect(3944);
		pc.getInventory().removeItem(this, 1);
	}
	
//AUTO SRM: 	static final S_SystemMessage EXTRACT_EMPTY_MESSAGE		= new S_SystemMessage("복용중인 엘릭서가 없습니다."); // CHECKED OK
	static final S_SystemMessage EXTRACT_EMPTY_MESSAGE		= new S_SystemMessage(S_SystemMessage.getRefText(1072), true);
	void elixirExtract(L1PcInstance pc){
		if (pc.getElixirStats() < 1) {
			pc.sendPackets(EXTRACT_EMPTY_MESSAGE);
			return;
		}
		int extractItemId = 0, extractCount = 1;
		L1Ability ablity = pc.getAbility();
		
		// 베이스 스탯을 제외한 value
		byte str	= (byte)(ablity.getStr() - ablity.getBaseStr());
		byte dex	= (byte)(ablity.getDex() - ablity.getBaseDex());
		byte con	= (byte)(ablity.getCon() - ablity.getBaseCon());
		byte inti	= (byte)(ablity.getInt() - ablity.getBaseInt());
		byte wis	= (byte)(ablity.getWis() - ablity.getBaseWis());
		byte chari	= (byte)(ablity.getCha() - ablity.getBaseCha());
		
		// compare
		if (str > 0 && str > dex && str > con && str > inti && str > wis && str > chari) {
			extractItemId = 40033;
			pc.getAbility().addStr(-1);
		} else if (dex > 0 && dex > str && dex > con && dex > inti && dex > wis && dex > chari) {
			extractItemId = 40035;
			pc.getAbility().addDex(-1);
		} else if (con > 0 && con > str && con > dex && con > inti && con > wis && con > chari) {
			extractItemId = 40034;
			pc.getAbility().addCon(-1);
		} else if (inti > 0 && inti > str && inti > dex && inti > con && inti > wis && inti > chari) {
			extractItemId = 40036;
			pc.getAbility().addInt(-1);
		} else if (wis > 0 && wis > str && wis > dex && wis > con && wis > inti && wis > chari) {
			extractItemId = 40037;
			pc.getAbility().addWis(-1);
		} else if (chari > 0 && chari > str && chari > dex && chari > con && chari > inti && chari > wis) {
			extractItemId = 40038;
			pc.getAbility().addCha(-1);
		}
		if (extractItemId == 0) {
			return;
		}
		boolean result		= CommonUtil.nextBoolean();// 결과
		if (!result) {// 실패
			extractItemId	= 820018;
			extractCount	= CommonUtil.random(21) + 10;// 10 ~ 30개
		}
		pc.setElixirStats(pc.getElixirStats() - 1);
		L1ItemInstance item = pc.getInventory().storeItem(extractItemId, extractCount);
		pc.send_effect_self(result ? 20834 : 20836);
		pc.sendPackets(result ? L1ServerMessage.sm8702 : L1ServerMessage.sm8703);
		pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", item.getItem().getDesc(), extractCount)), true);
		pc.sendPackets(new S_OwnCharStatus2(pc), true);
		pc.sendPackets(new S_TotalDrinkedElixirNoti(pc.getElixirStats()), true);
		pc.getInventory().removeItem(this, 1);
		try {
			pc.save();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void elixir(L1PcInstance pc, int itemId){
		int level		= pc.getLevel();
		int levelCount	= ((level - 48) >> 1) + (level >= 80 ? 1 : 0) + (level >= 90 ? 1 : 0) + (level >= 100 ? 2 : 0) + (level >= 101 ? (level - 99) >> 1 : 0);
		int curCount	= pc.getElixirStats();
		/*if (curCount >= 33) {
			pc.sendPackets(L1ServerMessage.sm4473);// 더 이상 엘릭서를 복용할 수 없습니다.
			return;
		}*/
		if (levelCount < 1 || levelCount <= curCount) {
			pc.sendPackets(L1ServerMessage.sm4472);// 현재 레벨에서 복용할 수 있는 엘릭서의 수량을 모두 사용했습니다.
			return;
		}
		boolean result = false;
		int maxStatus = level >= 100 ? 60 : level >= 90 ? 55 : 50;
		if (itemId == 40033 && pc.getAbility().getStr() < maxStatus) {// 힘
			pc.getAbility().addStr((byte) 1);
			result = true;
		} else if (itemId == 40034 && pc.getAbility().getCon() < maxStatus) {// 콘
			pc.getAbility().addCon((byte) 1);
			result = true;
		} else if (itemId == 40035 && pc.getAbility().getDex() < maxStatus) {// 덱스
			pc.getAbility().addDex((byte) 1);
			result = true;
		} else if (itemId == 40036 && pc.getAbility().getInt() < maxStatus) {// 인트
			pc.getAbility().addInt((byte) 1);
			result = true;
		} else if (itemId == 40037 && pc.getAbility().getWis() < maxStatus) {// 위즈
			pc.getAbility().addWis((byte) 1);
			result = true;
		} else if (itemId == 40038 && pc.getAbility().getCha() < maxStatus) {// 카리
			pc.getAbility().addCha((byte) 1);
			result = true;
		}
		
		if (!result) {
			pc.sendPackets(L1ServerMessage.sm481);// 해당 스탯 최대 값은 45입니다. 다른 스탯을 선택해주세요.
			return;
		}
		
		pc.getInventory().removeItem(this, 1);
		pc.setElixirStats(curCount + 1);
		pc.sendPackets(new S_OwnCharStatus2(pc), true);
		pc.sendPackets(new S_TotalDrinkedElixirNoti(pc.getElixirStats()), true);
		
		try {
			pc.save();// DB에 캐릭터 정보를 기입한다
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


