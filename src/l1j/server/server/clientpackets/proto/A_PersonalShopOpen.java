package l1j.server.server.clientpackets.proto;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.shop.L1PersonalShop;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.action.S_DoActionShop;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.object.S_PCObject;
import l1j.server.server.serverpackets.polymorph.S_Polymorph;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;

public class A_PersonalShopOpen extends ProtoHandler {
	protected A_PersonalShopOpen(){}
	private A_PersonalShopOpen(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.isDead()) {
			return;
		}
		if (_pc.isInvisble()) {
			_pc.sendPackets(L1ServerMessage.sm755);
			return;
		}
		if (_pc.getMapId() != 800) {
			_pc.sendPackets(L1ServerMessage.sm3405);// 개설 불가 지역
			return;
		}
		if (Config.SERVER.STANDBY_SERVER) {
			_pc.sendPackets(L1SystemMessage.STANBY_USE_FAIL_MSG);
			return;
		}
		
		// 무인상점 체크
		for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
			if (target.getId() != _pc.getId() && target.getAccountName().toLowerCase().equals(_pc.getAccountName().toLowerCase()) && target.isPrivateShop()) {
				_pc.sendPackets(L1SystemMessage.PRIVATE_SHOP_MAX_FAIL);
				return;
			}
		}

		ArrayList<L1PrivateShopSellList> sellList	= _pc.getSellList();
		ArrayList<L1PrivateShopBuyList> buyList		= _pc.getBuyList();
		L1ItemInstance checkItem;
		boolean tradable = true;
		
		readP(1);
		int shoptype = readC();// 0 or 1

		if (shoptype == 0) {// 개시
			int sellTotalCount=0, buyTotalCount=0, objectId=0, price=0, count=0, enchantLevel=0, attrType=0, attrEnchantLevel=0, bless=0;
			boolean isBySearching = false;
			for (int i = 0; i < _total_length; i++) {
				int code = readC();// 판매: 0x12, 구매: 0x1a
				if (code == 0x12 || code == 0x1a) {
					readC();// 아이템 세부 길이
					switch(code){
					case 0x12:// 판매
						for (int i2 = 0; i2 < 3; i2++) {
							int code2 = readC();
							switch (code2) {
							case 0x08:
								objectId	= readBit();
								break;
							case 0x10:
								price		= readBit();
								break;
							case 0x18:
								count		= readBit();
								break;
							}						
						}
						break;
					case 0x1a:// 구매
						for (int i2 = 0; i2 < 5; i2++) {
							int code2 = readC();
							switch(code2) {
							case 0x08:
								objectId	= readBit();// 검색으로 올렷을때 DESCID 취득
								break;
							case 0x10:
								price		= readBit();
								break;
							case 0x18:
								count		= readBit();
								break;
							case 0x22:
								if (readC() > 0x02) {// 세부정보 길이 여부
									for(int i3 = 0; i3 < 4; i3++){
										int code3 = readC();
										switch(code3){
										case 0x08:
											enchantLevel		= readC();// 인첸트레벨
											break;
										case 0x10:
											attrType			= readC();// 속성타입 1:화령, 2:수령, 3:풍령, 4:지령
											break;
										case 0x18:
											attrEnchantLevel	= readC();// 속성레벨
											break;
										case 0x20:
											bless				= readC();// 축복여부 0:축복, 1:일반, 2:저주
											break;
										}
									}
								}
								break;
							case 0x28:
								isBySearching = readC() == 1;// 검색여부
								break;
							}
						}
						break;
					}
					
					if (!isBySearching) {// 검색 타입이 아닌경우 인벤토리 체크
						// 거래 가능한 아이템이나 체크
						checkItem = _pc.getInventory().getItem(objectId);
						if (checkItem == null) {
							continue;
						}
						if (objectId != checkItem.getId()) {
							tradable = false;
							_pc.sendPackets(L1SystemMessage.PRIVATE_SHOP_ITEM_FAIL);
						}
						if (code == 0x12) {
							if (!checkItem.isMerge() && count != 1) {
								tradable = false;
								_pc.sendPackets(L1SystemMessage.PRIVATE_SHOP_ITEM_FAIL);
							}
							if (count > checkItem.getCount()) {
								count = checkItem.getCount();
							}
							if (checkItem.getCount() < count || checkItem.getCount() <= 0) {
								tradable = false;
								_pc.sendPackets(L1SystemMessage.PRIVATE_SHOP_ITEM_FAIL);
							}
						}
						if (count <= 0) {
							tradable = false;
							_pc.sendPackets(L1SystemMessage.PRIVATE_SHOP_ITEM_FAIL);
						}
						if (checkItem.getBless() >= 128) {
							tradable = false;
							//_pc.sendPackets(new S_ServerMessage(210, checkItem.getItem().getDescKr()), true);// 봉인상태
							_pc.sendPackets(new S_ServerMessage(210, checkItem.getItem().getDesc()), true);// 봉인상태
						}
						if (!checkItem.getItem().isTradable() || !checkItem.getItem().isCantSell()) {
							tradable = false;
							_pc.sendPackets(L1ServerMessage.sm941); // 거래 불가 아이템입니다.
						}
						
						if (_pc.getDoll() != null && checkItem.getId() == _pc.getDoll().getItemObjId()) {
							tradable = false;
							_pc.sendPackets(L1ServerMessage.sm941); // 거래 불가 아이템입니다.
						}
						if (_pc.getPet() != null && _pc.getPet().getAmuletId() == checkItem.getId()) {
							tradable = false;
							_pc.sendPackets(L1ServerMessage.sm941); //거래 불가 아이템입니다.
						}
					}
					
					if (code == 0x12) {// 판매 세팅
						if (sellTotalCount > 7) {
							_pc.sendPackets(L1SystemMessage.PRIVATE_SHOP_ITEM_MAX); 
							return;
						}
						
						L1PrivateShopSellList pssl = new L1PrivateShopSellList();
						pssl.setItemObjectId(objectId);
						pssl.setSellPrice(price);
						pssl.setSellTotalCount(count);
						sellList.add(pssl);
						sellTotalCount++;
					} else if (code == 0x1a) {// 구매 세팅
						if (buyTotalCount > 7) {
							_pc.sendPackets(L1SystemMessage.PRIVATE_SHOP_ITEM_MAX); 
							return;
						}
						
						L1PrivateShopBuyList psbl = new L1PrivateShopBuyList();
						psbl.setItemObjectId(objectId);
						psbl.setBuyPrice(price);
						psbl.setBuyTotalCount(count);
						if (isBySearching) {
							psbl.setEnchantLevel(enchantLevel);
							psbl.setAttyType(attrType);
							psbl.setAttyEnchantLevel(attrEnchantLevel);
							psbl.setBless(bless);
							psbl.setBySearching(isBySearching);
						}
						buyList.add(psbl);
						buyTotalCount++;
					}
				} else {
					break;
				}
			}

			if (sellTotalCount == 0 && buyTotalCount == 0) {
				_pc.sendPackets(L1ServerMessage.sm908);
				_pc.setPrivateShop(false);
				_pc.broadcastPacketWithMe(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Idle), true);
				return;
			}

			if (!tradable) { // 거래 불가능한 아이템이 포함되어 있는 경우, 개인 상점 종료
				shopOpenReset(sellList, buyList);
				_pc.broadcastPacketWithMe(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Idle), true);
				return;
			}
			
			/** 수수료 부과 **/
			int shopOpenCount = _pc.getAccount().getShopOpenCount();// 상점 개설 횟수
			int consumeCount = shopOpenCount >= 40 ? 20000 + ((shopOpenCount - 40) * 1000) : 1000;
			if (!_pc.getInventory().consumeItem(L1ItemId.ADENA, consumeCount)) {
				shopOpenReset(sellList, buyList);
				_pc.broadcastPacketWithMe(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Idle), true);
				_pc.sendPackets(L1ServerMessage.sm189); //아데나가 부족합니다
				return;
			}
			
	        byte[] chat		= readByte(readC());// 채팅
	        readP(1);// 2a
	        String polynum	= readS(readC());// 변신명 
			
	        _pc.setShopChat(chat);
	        _pc.setPrivateShop(true);
	        _pc.broadcastPacketWithMe(new S_DoActionShop(_pc.getId(), ActionCodes.ACTION_Shop, chat), true);

			L1PersonalShop.regist(_pc, sellList, 0);// 판매 물품 등록
			L1PersonalShop.regist(_pc, buyList, 1);// 구매 물품 등록

			try {
				int polyId = getShopPolyId(polynum);
				if (polyId != 0) {
					_pc.removeShapeChange();
					L1PolyMorph.undoPoly(_pc);
					L1ItemInstance weapon = _pc.getWeapon();
					if (weapon != null) {
						_pc.getInventory().setEquipped(weapon, false, false, false);
					}
					_pc.setSpriteId(polyId);
					_pc.sendPackets(new S_Polymorph(_pc.getId(), polyId, _pc.getCurrentWeapon()), true);
					if (!_pc.isGmInvis() && !_pc.isInvisble()) {
						_pc.broadcastPacket(new S_Polymorph(_pc.getId(), polyId, _pc.getCurrentWeapon()), true);
					}
					_pc.broadcastPacketWithMe(new S_CharVisualUpdate(_pc, 0x46), true);
					_pc.broadcastPacket(new S_PCObject(_pc), true);

					_pc.curePoison();
				}
				_pc.getAccount().updateShopOpenCount();// 상점 개설 횟수 업데이트
				_pc.sendPackets(new S_PacketBox(S_PacketBox.SHOP_OPEN_COUNT, _pc.getAccount().getShopOpenCount()), true);
				_pc.sendPackets(L1SystemMessage.PRIVATE_SHOP_EXPLAN);
				_pc.sendPackets(L1SystemMessage.PRIVATE_SHOP_EXPLAN_REMA);
			} catch (Exception e) {
				L1PersonalShop.delete(_pc);
				shopOpenReset(sellList, buyList);
				_pc.broadcastPacketWithMe(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Idle), true);
				return;
			}
		} else if (shoptype == 1) {// 종료
			if (_pc.isTwoLogin()) {
				_pc.denals_disconnect(String.format("[A_PersonalShopOpen] TWO_LOGIN_DISCONNECT : NAME(%s)", _pc.getName()));
			}
			shopOpenReset(sellList, buyList);
			int classId = _pc.getClassId();
			_pc.setSpriteId(classId);
			_pc.broadcastPacketWithMe(new S_Polymorph(_pc.getId(), classId, _pc.getCurrentWeapon()), true);
			L1ItemInstance weapon = _pc.getWeapon();
			if (weapon != null) {
				_pc.getInventory().setEquipped(weapon, false, false, false);
			}
			_pc.broadcastPacketWithMe(new S_CharVisualUpdate(_pc), true);
			try {
				L1PersonalShop.delete(_pc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	int getShopPolyId(String polynum){
		switch(polynum){
		case "tradezone1":return 11326;
		case "tradezone2":return 11427;
		case "tradezone3":return 10047;
		case "tradezone4":return 9688;
		case "tradezone5":return 11322;
		case "tradezone6":return 10069;
		case "tradezone7":return 10034;
		case "tradezone8":return 10032;
		default:return 0;
		}
	}
	
	void shopOpenReset(ArrayList<L1PrivateShopSellList> sellList, ArrayList<L1PrivateShopBuyList> buyList){
		searchItemDelete(buyList);
		sellList.clear();
		buyList.clear();
		_pc.setPrivateShop(false);
	}

	void searchItemDelete(ArrayList<L1PrivateShopBuyList> buyItemList){
		for (L1PrivateShopBuyList psb : buyItemList) {
			if (psb.isBySearching() && psb.getBuyItem() != null) {
				L1World.getInstance().removeObject(psb.getBuyItem());
			}
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PersonalShopOpen(data, client);
	}

}

