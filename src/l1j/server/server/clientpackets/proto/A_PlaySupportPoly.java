package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.GameServerSetting;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class A_PlaySupportPoly extends ProtoHandler {
	private static final String TAM_POLY_REGEX				= "tam 60a|tam 60b|tam 60c|tam 60d|tam 60e|tam 60f|tam 60g|tam 60h";
	private static final String EVENT_PLEDGE_POLY_REGEX		= "event pledge death knight|event pledge darkelf";
	private static final String LEGENDLY_POLY_REGEX			= "event lv87 lms atun|event lv87 ninety darkstarjoe|event lv87 ninety zilian|event lv87 night slasher";
	private static final String ARCH_POLY_REGEX				= "scroll arch knight|scroll arch lance master|scroll arch scouter|scroll arch mage";
	private static final String DOMINATION_POLY_REGEX		= "lv88 gunter|lv86 curts|lv88 bluedica|lv86 slave|lv88 prokel|lv86 iris|lv88 zillian|lv86 helvine|lv88 joewoo|lv86 hardin|lv100 doppelganger king|lv100 zillian|lv100 crister|lv100 ishilotte|lv100 joewoo|lv100 atun";
	private static final String EVENT_DEATH_POLY_REGEX		= "event jin death knight|event jin assassin|event jin baphomet master|event jin lance master|event owen|event guard spear red";
	private static final String EVENT_DOMINATION_POLY_REGEX	= "maple lv86 hardin ev|maple lv88 joewoo ev|maple lv86 helvine ev|maple lv88 zillian ev|maple lv86 iris ev|maple lv88 prokel ev|maple lv86 slave ev|maple lv86 curts ev|maple lv88 bluedica ev|maple lv88 gunter ev|maple lv100 doppelganger king ev|maple lv100 zillian ev";
	
	private boolean isLev100Poly;
	
	protected A_PlaySupportPoly(){}
	private A_PlaySupportPoly(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (!Config.PSS.PLAY_SUPPORT_ACTIVE || _pc == null || _pc.isDead() || _pc.isGhost() || !_pc.getConfig().isPlaySupport()) {
			return;
		}
		
		if (_total_length <= 2) {
			return;
		}
		readP(1);// 0x0a;
		int nameLength	= readC();
		if (nameLength <= 0) {
			return;
		}
		String polyName	= readS(nameLength);
		if (StringUtil.isNullOrEmpty(polyName)) {
			return;
		}
		
		L1ItemInstance checkItem = null, polyItem = null, dominationRing = null;
		
		if (polyName.matches(EVENT_DEATH_POLY_REGEX)) {
			polyItem = _pc.getInventory().findItemId(520281);
			if(polyItem != null){
				eventDeathKnightPolyRing(polyName);
			}
			return;
		}
		if (polyName.matches(EVENT_DOMINATION_POLY_REGEX)) {
			polyItem = _pc.getInventory().findItemId(8025);
			if(polyItem != null){
				eventJibaePolyScroll(polyName, polyItem);
			}
			return;
		}
		
		if (polyName.startsWith(L1PolyMorph.MAPLE_STR)) {
			polyName = polyName.replace(L1PolyMorph.MAPLE_STR, StringUtil.EmptyString).trim();
		}
		if (polyName.contains(L1PolyMorph.MAPLE_STR) && polyName.contains(L1PolyMorph.EV_STR)) {
			polyName = polyName.replace(L1PolyMorph.MAPLE_STR, StringUtil.EmptyString).trim();
			polyName = polyName.replace(L1PolyMorph.EV_STR, StringUtil.EmptyString).trim();
		}
		
		// 100레벨 변신 체크
		if (polyName.length() > 4 && polyName.substring(polyName.length() - 4).equals(L1PolyMorph.LEVEL_100_STR)) {
			polyName = polyName.substring(0, polyName.length() - 4);
			isLev100Poly = true;
		}
		
		// 보유중친 변신 아이템
		int size = _total_length - 2 - nameLength;
		int cur_offset = 0;
		while (cur_offset < size) {
			if (!isRead(1)) {
				break;
			}
			int r_size	= read_size();
			int code	= readC();
			switch (code) {
			case 0x10:
				r_size		+= read_size();
				int objId	= readBit();
				checkItem	= _pc.getInventory().findItemObjId(objId);
				if (checkItem == null) {
					break;
				}
				
				int itemId = checkItem.getItemId();
				if (itemId == 202810 || itemId == 202813) {
					dominationRing = checkItem;
				}
				
				if (isLev100Poly) {// 100레벨 변신
					if (itemId == 202813) {
						polyItem = checkItem; 
					}
				} else if (polyName.matches(TAM_POLY_REGEX)) {
					if (itemId == 60359){
						polyItem = checkItem; 
					}
				} else if (polyName.matches(EVENT_PLEDGE_POLY_REGEX)) {// 레전드 혈맹 변신 마법서
					if (itemId == 60772) {
						polyItem = checkItem; 
					}
				} else if (polyName.matches(LEGENDLY_POLY_REGEX)) {// 우승자의 반지
					if (itemId == 60773) {
						polyItem = checkItem; 
					}
				} else if (polyName.matches(ARCH_POLY_REGEX)) {// 아크 변신 마법서
					if (itemId == 60775) {
						polyItem = checkItem; 
					}
				} else if (polyName.matches(DOMINATION_POLY_REGEX)) {// 지배 변신
					if (itemId == 202810) {
						polyItem = checkItem; 
					}
				} else {
					polyItem = dominationRing != null ? dominationRing : checkItem;
				}
				break;
			}
			cur_offset += r_size;
		}
		
		if (polyItem == null) {
			return;
		}
		
		int itemId = polyItem.getItemId();
		switch(itemId){
		case 40088:		// 변신 주문서
		case 40096:		// 상아탑의 변신 주문서
		case 210112:	// 복지 변신 주문서
		case 140088:	// 축복 변신 주문서
		case 60359:		// 조우의 변신 주문서
		case 130041:	// 변신 주문서 [공성전]
			if (poly(polyName, itemId)) {
				_pc.getInventory().removeItem(polyItem, 1);
				if (itemId == 40096) {
					_pc.getQuest().questItemUse(itemId);
				}
			} else {
				_pc.sendPackets(L1ServerMessage.sm181);// \f1 그러한 monster에게는 변신할 수 없습니다.
				if (_pc.isGm()) {
					//_pc.sendPackets(new S_SystemMessage(String.format("\\aH변신이름  >> %s", polyName)), true);
					_pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(40), polyName), true);
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
			if (!poly(polyName, itemId)) {
				_pc.sendPackets(L1ServerMessage.sm181);// \f1 그러한 monster에게는 변신할 수 없습니다.
				if (_pc.isGm()) {
					//_pc.sendPackets(new S_SystemMessage(String.format("\\aH변신이름  >> %s", polyName)), true);
					_pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(40), polyName), true);
				}
			}
			break;
		default:break;
		}
	}
	
	private boolean poly(String s, int itemId){
		int time = 0;
		switch (itemId) {
		case 40088:case 40096:case 60359:case 130041:
			time = L1PolyMorph.polyAlignTime(_pc.getAlignment(), 1800, 600);
			break;
		case 140088:
			time = L1PolyMorph.polyAlignTime(_pc.getAlignment(), 2100, 900);
			break;
		case 210112:
			time = L1PolyMorph.polyAlignTime(_pc.getAlignment(), 7200, 2400);
			break;
		case 202810:// 변신 지배 반지
		case 202813:// [%i] 변신 지배 반지
			time = 3600;
			break;
		case 60751:case 60770:case 60771:case 60772:case 60773:case 60774:
			time = 1800;
			break;
		case 60775:// 아크 변신 마법서
			time = 1200;
			break;
		default:return false;
		}
		
		L1InterServer inter = _pc.getNetConnection().getInter();
		if (inter == L1InterServer.TREASURE_ISLAND) {
			return false;
		}
		if (L1InterServer.isWorldPolyInter(inter)) {
			if (itemId == 202810) {
				if (s.matches(DOMINATION_POLY_REGEX)) {
					L1PolyMorph.worldClassPoly(_pc, false);
				} else if (s.equalsIgnoreCase(StringUtil.EmptyString) && _pc._isWorldPoly) {
					return false;
				} else {
					return false;
				}
			} else if (itemId == 202813) {
				if (isLev100Poly) {
					L1PolyMorph.worldClassPoly(_pc, false);
				} else if (s.equalsIgnoreCase(StringUtil.EmptyString) && _pc._isWorldPoly) {
					return false;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		
		boolean isRank = _pc.getConfig().is_rank_poly();
		if (s.startsWith(L1PolyMorph.RANKING_STR) && !isRank) {
			return false;
		}
		
		if (s.equalsIgnoreCase(L1PolyMorph.RANKING_CLASS_STR)) {
			if (!isRank) {
				return false;
			}
			s = L1PolyMorph.RANKING_CLASS_ARRAY[_pc.getType()][_pc.getGender().toInt()];
		} else if (s.equalsIgnoreCase(L1PolyMorph.BASIC_CLASS_STR)) {
			s = L1PolyMorph.BASIC_CLASS_ARRAY[_pc.getType()][_pc.getGender().toInt()];
		}
		
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
		if (poly != null) {
			if ((poly.getMinLevel() <= _pc.getLevel()) || _pc.isGm() || GameServerSetting.POLY_LEVEL_EVENT) {
				L1PolyMorph.doPoly(_pc, poly.getPolyId(), time, itemId == 202813 ? L1PolyMorph.MORPH_BY_100LEVEL : itemId == 202810 ? L1PolyMorph.MORPH_BY_DOMINATION : L1PolyMorph.MORPH_BY_ITEMMAGIC);
				if (_pc.isGm()) {
					//_pc.sendPackets(new S_SystemMessage(String.format("\\aH변신이름  >> %s 변신번호  >> %d", s, _pc.getSpriteId())), true);
					_pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(16), s, String.valueOf(_pc.getSpriteId())), true);
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private void eventDeathKnightPolyRing(String action) {
		if (L1InterServer.isNotPolyInter(_pc.getNetConnection().getInter())) {
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
		L1PolyMorph.doPoly(_pc, polyId, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
	}
	
	private void eventJibaePolyScroll(String action, L1ItemInstance polyItem){
		if (L1InterServer.isNotPolyInter(_pc.getNetConnection().getInter())) {
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
		L1PolyMorph.doPoly(_pc, polyId, 3600, L1PolyMorph.MORPH_BY_DOMINATION);
		_pc.getInventory().removeItem(polyItem, 1);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PlaySupportPoly(data, client);
	}

}


