package l1j.server.server.model.item.function;

import l1j.server.server.GameServerSetting;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.StringUtil;

public class PolyScroll extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	private static final String TAM_POLY_REGEX				= "tam 60a|tam 60b|tam 60c|tam 60d|tam 60e|tam 60f|tam 60g|tam 60h";
	private static final String EVENT_PLEDGE_POLY_REGEX		= "event pledge death knight|event pledge darkelf";
	private static final String LEGENDLY_POLY_REGEX			= "event lv87 lms atun|event lv87 ninety darkstarjoe|event lv87 ninety zilian|event lv87 night slasher";
	private static final String ARCH_POLY_REGEX				= "scroll arch knight|scroll arch lance master|scroll arch scouter|scroll arch mage";
	private static final String DOMINATION_POLY_REGEX		= "lv88 gunter|lv86 curts|lv88 bluedica|lv86 slave|lv88 prokel|lv86 iris|lv88 zillian|lv86 helvine|lv88 joewoo|lv86 hardin|lv100 doppelganger king|lv100 zillian|lv100 crister|lv100 ishilotte|lv100 joewoo|lv100 atun";
	
	public PolyScroll(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		L1PcInstance pc	= (L1PcInstance) cha;
		String polyName	= packet.readS();
		int itemId		= this.getItemId();
		switch(itemId){
		case 40088:		// 변신 주문서
		case 40096:		// 상아탑의 변신 주문서
		case 210112:	// 복지 변신 주문서
		case 140088:	// 축복 변신 주문서
		case 60359:		// 조우의 변신 주문서
		case 130041:	// 변신 주문서 [공성전]
			if (poly(pc, itemId, polyName)) {
				pc.getInventory().removeItem(this, 1);
				if (itemId == 40096) {
					pc.getQuest().questItemUse(itemId);
				}
			} else {
				pc.sendPackets(L1ServerMessage.sm181);// \f1 그러한 monster에게는 변신할 수 없습니다.
				if (pc.isGm()) {
					//pc.sendPackets(new S_SystemMessage(String.format("\\aH변신이름  >> %s", polyName)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(40), polyName), true);
				}
			}
			break;
		case 202810:	// 변신 지배 반지
		case 202813:	// [%i] 변신 지배 반지
		case 60751:		// 영웅의 데스나이트 변신 반지
		case 60770:		// 21주년 전설 변신 반지
		case 60771:		// 영웅의 변신 반지
		case 60772:		// 레전드 혈맹 변신 마법서
		case 60773:		// 우승자의 반지
		case 60774:		// 영웅의 변신 반지
		case 60775:		// 아크 변신 마법서
			if (!poly(pc, itemId, polyName)) {
				pc.sendPackets(L1ServerMessage.sm181);// \f1 그러한 monster에게는 변신할 수 없습니다.
				if (pc.isGm()) {
					//pc.sendPackets(new S_SystemMessage(String.format("\\aH변신이름  >> %s", polyName)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(40), polyName), true);
				}
			}
			break;
		case 520281:	// 진 데스나이트의 변신 반지
			eventDeathKnightPolyRing(pc, polyName);
			break;
		case 8025:		// 지배 변신 주문서
			eventJibaePolyScroll(pc, polyName);
			break;
		}
	}
	
	private boolean poly(L1PcInstance pc, int item_id, String polyName) {
		int time = 0;
		switch (item_id) {
		case 40088:case 40096:case 60359:case 130041:
			time = L1PolyMorph.polyAlignTime(pc.getAlignment(), 1800, 600);
			break;
		case 140088:
			time = L1PolyMorph.polyAlignTime(pc.getAlignment(), 2100, 900);
			break;
		case 210112:
			time = L1PolyMorph.polyAlignTime(pc.getAlignment(), 7200, 2400);
			break;
		case 202810:case 202813:// 변신 지배 반지
			time = 3600;
			break;
		case 60751:case 60770:case 60771:case 60772:case 60773:case 60774:
			time = 1800;
			break;
		case 60775:// 아크 변신 마법서
			time = 1200;
			break;
		default:
			return false;
		}
		if (polyName.startsWith(L1PolyMorph.MAPLE_STR)) {
			polyName = polyName.replace(L1PolyMorph.MAPLE_STR, StringUtil.EmptyString).trim();
		}
		if (polyName.contains(L1PolyMorph.MAPLE_STR) && polyName.contains(L1PolyMorph.EV_STR)) {
			polyName = polyName.replace(L1PolyMorph.MAPLE_STR, StringUtil.EmptyString).trim();
			polyName = polyName.replace(L1PolyMorph.EV_STR, StringUtil.EmptyString).trim();
		}
		
		// 100레벨 변신 체크
		boolean isLev100Poly = false;
		if (!StringUtil.isNullOrEmpty(polyName) && polyName.length() > 4 && polyName.substring(polyName.length() - 4).equals(L1PolyMorph.LEVEL_100_STR)) {
			polyName = polyName.substring(0, polyName.length() - 4);
			isLev100Poly = true;
		}

		if (isLev100Poly) {// 100레벨 변신
			if (item_id != 202813) {// 100레벨 변신 지배 반지
				polyFaileSupportFinish(pc);
				return false;
			}
		} else if (polyName.matches(TAM_POLY_REGEX) && item_id != 60359) {
			polyFaileSupportFinish(pc);
			return false;
		} else if (polyName.matches(EVENT_PLEDGE_POLY_REGEX) && item_id != 60772) {// 레전드 혈맹 변신 마법서
			polyFaileSupportFinish(pc);
			return false;
		} else if (polyName.matches(LEGENDLY_POLY_REGEX) && item_id != 60773) {// 우승자의 반지
			polyFaileSupportFinish(pc);
			return false;
		} else if (polyName.matches(ARCH_POLY_REGEX) && item_id != 60775) {// 아크 변신 마법서
			polyFaileSupportFinish(pc);
			return false;
		} else if (polyName.matches(DOMINATION_POLY_REGEX) && item_id != 202810) {
			polyFaileSupportFinish(pc);
			return false;
		}
		
		if (item_id == 60359 && !polyName.matches(TAM_POLY_REGEX)) {
			polyFaileSupportFinish(pc);
			return false;
		}
		if (item_id == 60772 && !polyName.matches(EVENT_PLEDGE_POLY_REGEX)) {
			polyFaileSupportFinish(pc);
			return false;
		}
		if (item_id == 60773 && !polyName.matches(LEGENDLY_POLY_REGEX)) {
			polyFaileSupportFinish(pc);
			return false;
		}
		if (item_id == 60775 && !polyName.matches(ARCH_POLY_REGEX)) {
			polyFaileSupportFinish(pc);
			return false;
		}
		
		L1InterServer inter = pc.getNetConnection().getInter();
		if (inter == L1InterServer.TREASURE_ISLAND) {
			return false;
		}
		if (L1InterServer.isWorldPolyInter(inter)) {
			if (item_id == 202810) {
				if (polyName.matches(DOMINATION_POLY_REGEX)) {
					L1PolyMorph.worldClassPoly(pc, false);
				} else if (polyName.equalsIgnoreCase(StringUtil.EmptyString) && pc._isWorldPoly) {
					return false;
				} else {
					return false;
				}
			} else if (item_id == 202813) {
				if (isLev100Poly) {
					L1PolyMorph.worldClassPoly(pc, false);
				} else if (polyName.equalsIgnoreCase(StringUtil.EmptyString) && pc._isWorldPoly) {
					return false;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		
		boolean isRank = pc.getConfig().is_rank_poly();
		if (polyName.startsWith(L1PolyMorph.RANKING_STR) && !isRank) {
			return false;
		}
		
		if (polyName.equalsIgnoreCase(L1PolyMorph.RANKING_CLASS_STR)) {
			if (!isRank) {
				return false;
			}
			polyName = L1PolyMorph.RANKING_CLASS_ARRAY[pc.getType()][pc.getGender().toInt()];
		} else if (polyName.equalsIgnoreCase(L1PolyMorph.BASIC_CLASS_STR)) {
			polyName = L1PolyMorph.BASIC_CLASS_ARRAY[pc.getType()][pc.getGender().toInt()];
		}
		
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyName);
		if (poly != null || polyName.equals(StringUtil.EmptyString)) {
			if (polyName.equals(StringUtil.EmptyString)) {
				if (pc.getSpriteId() == 6034 || pc.getSpriteId() == 6035) {
					return true;
				}
				pc.removeShapeChange();
				return true;
			} else if ((poly.getMinLevel() <= pc.getLevel()) || pc.isGm() || GameServerSetting.POLY_LEVEL_EVENT) {
				L1PolyMorph.doPoly(pc, poly.getPolyId(), time, item_id == 202813 ? L1PolyMorph.MORPH_BY_100LEVEL : item_id == 202810 ? L1PolyMorph.MORPH_BY_DOMINATION : L1PolyMorph.MORPH_BY_ITEMMAGIC);
				if (pc.isGm()) {
					//pc.sendPackets(new S_SystemMessage(String.format("\\aH변신이름  >> %s 변신번호  >> %d", polyName, pc.getSpriteId())), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(16), polyName, String.valueOf(pc.getSpriteId())), true);
				}
				return true;
			}
			return false;
		}
		return false;
	}
	
	private void eventDeathKnightPolyRing(L1PcInstance pc, String action) {
		if (L1InterServer.isNotPolyInter(pc.getNetConnection().getInter())) {
			return;
		}
		int polyId = 0;
		switch (action) {
		case "event jin death knight":		polyId = 13152;break;
		case "event jin assassin":			polyId = 15868;break;
		case "event jin baphomet master":	polyId = 15550;break;
		case "event jin lance master":		polyId = 15539;break;
		case "event owen":					polyId = 13635;break;
		case "event guard spear red":		polyId = 15833;break;
		default:return;
		}
		L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
	}
	
	private void eventJibaePolyScroll(L1PcInstance pc, String action){
		if (L1InterServer.isNotPolyInter(pc.getNetConnection().getInter())) {
			return;
		}
		int polyId = 0;
		switch (action) {
		case "maple lv86 hardin ev":				polyId = 16027;break;
		case "maple lv88 joewoo ev":				polyId = 16040;break;
		case "maple lv86 helvine ev":				polyId = 16002;break;
		case "maple lv88 zillian ev":				polyId = 16074;break;
		case "maple lv86 iris ev":					polyId = 16008;break;
		case "maple lv88 prokel ev":				polyId = 16056;break;
		case "maple lv86 slave ev":					polyId = 15986;break;
		case "maple lv86 curts ev":					polyId = 16014;break;
		case "maple lv88 bluedica ev":				polyId = 16053;break;
		case "maple lv88 gunter ev":				polyId = 16284;break;
		case "maple lv100 doppelganger king ev":	polyId = 20438;break;
		case "maple lv100 zillian ev":				polyId = 20442;break;
		default:return;
		}
		L1PolyMorph.doPoly(pc, polyId, 3600, L1PolyMorph.MORPH_BY_DOMINATION);
		pc.getInventory().removeItem(this, 1);
	}
	
	private void polyFaileSupportFinish(L1PcInstance pc){
		if (!pc.getConfig().isPlaySupport()) {
			return;
		}
		pc.sendPackets(L1ServerMessage.sm6871);// [PSS 종료] 플레이 서포트가 종료되었어요. 캐릭터 상태를 확인해보세요.
		pc.getConfig().finishPlaySupport();
	}

}


