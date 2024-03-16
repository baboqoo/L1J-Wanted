package l1j.server.server.clientpackets;

import java.util.List;
import java.util.Random;

import l1j.server.server.ActionCodes;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.ItemMentTable;
import l1j.server.server.datatables.ItemMentTable.ItemMentType;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.sprite.AcceleratorChecker.ACT_TYPE;
import l1j.server.server.monitor.Logger.ItemActionType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.action.S_AttackStatus;
import l1j.server.server.serverpackets.message.S_NotificationMessage;
import l1j.server.server.serverpackets.message.S_NotificationMessage.display_position;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
//import manager.Manager;  // MANAGER DISABLED

public class C_PickUpItem extends ClientBasePacket {
	private static final String C_PICK_UP_ITEM = "[C] C_PickUpItem";
	private static final Random random = new Random(System.nanoTime());
	
	L1PcInstance pc;
	L1PcInstance owner;
	L1ItemInstance item;
	
	private boolean isHateChance(){
		int outerHate = item.getItemOuterValue(pc);
		if (outerHate <= 0 || random.nextInt(100) + 1 > outerHate) {// 공헌도에 따른 획득 확률
			pc.sendPackets(L1ServerMessage.sm623);
			return false;
		}
		return true;
	}
	
	private boolean isValidationItemOwner(){
		if (owner.isInParty()) {
			if (!owner.getParty().isMember(pc)) {
				return isHateChance();
			}
		} else {
			if (owner.getId() != pc.getId()) {
				return isHateChance();
			}
		}
		return true;
	}
	
	public C_PickUpItem(byte decrypt[], GameClient client) throws Exception {
		super(decrypt);
		pc = client.getActiveChar();
		if (pc == null || pc.isGhost() || pc.isDead() || pc.isTwoLogin() || pc.isInvisble() || pc.isInvisDelay()) {
			return;
		}
		if (pc.getOnlineStatus() != 1) {
			pc.denals_disconnect(String.format("[C_PickUpItem] NOT_ONLINE_USER : NAME(%s)", pc.getName()));
			return;
		}
		L1PcInventory inv = pc.getInventory();
		if (inv.getWeightPercent() >= 100) {
			pc.sendPackets(L1ServerMessage.sm82); // 무게 게이지가 가득찼습니다.
			return;
		}
		int x			= readH();
		int y			= readH();
		int objectId	= readD();
		int pickupCount	= readD();
		
		L1World world	= L1World.getInstance();
		L1Inventory groundInventory = world.getInventory(x, y, pc.getMapId());
		item = groundInventory.getItem(objectId);
		if (item == null) {
			return;
		}
		owner = item.getItemOwner();
		if (owner != null && !isValidationItemOwner()) {
			return;
		}
		
		/** 버그방지 **/ 
		if (objectId != item.getId() || (!item.isMerge() && pickupCount != 1)) {
			pc.denals_disconnect(String.format("[C_PickUpItem] PACKET_DENALS : NAME(%s)", pc.getName()));
			return;
		}
		if (pickupCount <= 0 || item.getCount() <= 0 || item.getCount() > L1Inventory.MAX_AMOUNT) {
			pc.denals_disconnect(String.format("[C_PickUpItem] COUNT_DENALS : NAME(%s)", pc.getName()));
			groundInventory.deleteItem(item);
			return;
		}
		if (pickupCount > item.getCount()) {
			pickupCount = item.getCount();
		}
		if (item.getItem().getItemId() != L1ItemId.ADENA && item.getItem().getItemId() != L1ItemId.GEMSTONE && item.getItem().getItemId() != L1ItemId.PIXIE_FEATHER && item.getCount() > 90000) {
			return;
		}
		
		/** 토글 할 수 있는 거리인지 체크 */
		if (pc.getLocation().getTileLineDistance(item.getLocation()) > 2) {
			return;
		}
		if (x > pc.getX() + 1 || x < pc.getX() - 1 || y > pc.getY() + 1 || y < pc.getY() - 1) {
			return;
		}
		
		/** 해당 아이템 드랍 체크및 설정 즉 밑 아이템은 위 조건문을 통해 갯수설정 **/ 
		/*if (((item.getCount() >= 100) 
				&& ((item.getItemId() == L1ItemId.PIXIE_FEATHER) 
						|| (item.getItemId() == L1ItemId.SCROLL_OF_ENCHANT_WEAPON) 
						|| (item.getItemId() == L1ItemId.SCROLL_OF_ENCHANT_ARMOR))) 
						|| (item.getEnchantLevel() > 0) 
						|| ((item.getCount() >= 1000000) && (item.getItemId() == L1ItemId.ADENA)) 
						|| ((item.getCount() >= 1000) && (item.getItemId() != L1ItemId.ADENA))) {
			//Manager.getInstance().PicupAppend(item.getLogName(), item.getDescKr(), item.getCount(), 0);
		}*/  // MANAGER DISABLED
		
		if (item.getItem().getItemId() == L1ItemId.ADENA) {
			L1ItemInstance inventoryItem = inv.findItemId(L1ItemId.ADENA);
			int inventoryItemCount = inventoryItem != null ? inventoryItem.getCount() : 0;
			// 주운 후에 2 G를 초과하지 않게 체크
			if ((long) inventoryItemCount + (long) pickupCount > L1Inventory.MAX_AMOUNT) {
				pc.sendPackets(L1SystemMessage.ADENA_FICKUP_MAX_FAIL);
				return;
			}
		}
		// 용량 중량 확인 및 메세지 송신
		try {
			boolean is_drop_from_npc = false;
			if (inv.checkAddItem(item, pickupCount) == L1Inventory.OK && item.getX() != 0 && item.getY() != 0) {
				// 파티 획득
				if (pc.isInParty())
				{
					L1Party party = pc.getParty();
					if (pc.getLocation().getTileLineDistance(pc.getLocation()) < 14) {
						if (party.isAutoDistribution() && item.getDropNpc() != null) {// 자동분배 타입
							L1Npc npc = item.getDropNpc();
							is_drop_from_npc = true;
							List<L1PcInstance> members = world.getVisiblePartyPlayer(pc, 20);// 범위내 파티 멤버
							L1PcInstance luckyUser = members.get(random.nextInt(members.size()));// 랜덤으로 누구 한테 갈껀지
							// 아데나
							if (item.getItemId() == L1ItemId.ADENA)
							{
								int divAden = pickupCount / members.size();// 맴버배당
								if (members.size() > 1) {
									int modNum = pickupCount % members.size();// 나머지
									if (modNum == 0) {
										for (int row = 0; row < members.size(); row++) {
											groundInventory.tradeItem(item, divAden, members.get(row).getInventory());
											for (L1PcInstance user : members) {
												if (!user.getConfig().RootMent) {
													continue;
												}
												//user.sendPackets(new S_SystemMessage(String.format("아데나 (%d) 획득 : %s (%s)", divAden, members.get(row).getName(), npc.getDesc())), true);
												user.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(26), String.valueOf(divAden), members.get(row).getName(), npc.getDesc()), true);
											}
										}
									} else if (pickupCount < members.size()) {
										groundInventory.tradeItem(item, pickupCount, inv);
									} else {
										for (int row = 0; row < members.size(); row++) {
											if (pc.getId() == members.get(row).getId()) {// 먹는사람
												groundInventory.tradeItem(item, divAden + modNum, inv);
												for (L1PcInstance user : members) {
													if (!user.getConfig().RootMent) {
														continue;
													}
 													//user.sendPackets(new S_SystemMessage(String.format("아데나 (%d) 획득 : %s (%s)", divAden + modNum, members.get(row).getName(), npc.getDesc())), true);
													user.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(26), String.valueOf(divAden + modNum), members.get(row).getName(), npc.getDesc()), true);
												}
											} else {// 맴버
												groundInventory.tradeItem(item, divAden, members.get(row).getInventory());
												for (L1PcInstance user : members) {
													if (!user.getConfig().RootMent) {
														continue;
													}
													//user.sendPackets(new S_SystemMessage(String.format("아데나 (%d) 획득 : %s (%s)", divAden, members.get(row).getName(), npc.getDesc())), true);
													user.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(26), String.valueOf(divAden), members.get(row).getName(), npc.getDesc()), true);
												}
											}
										}
									}
								} else {
									groundInventory.tradeItem(item, pickupCount, inv);
								}
							}
							// 아데나 이외
							else
							{
								groundInventory.tradeItem(item, pickupCount, luckyUser.getInventory());
								S_ServerMessage message = new S_ServerMessage(813, npc.getDesc(), item.getLogNameRef(), luckyUser.getName());
								for (L1PcInstance partymember : party.getMembersArray()) {
									if (!partymember.getConfig().RootMent) {
										continue;
									}
									partymember.sendPackets(message);
								}
								message.clear();
								message = null;
							}
							item.setDropNpc(null);
						} else {
							// 일반 분배
							groundInventory.tradeItem(item, pickupCount, inv);
							if (item.getDropNpc() != null) {
								L1Npc npc = item.getDropNpc();
								is_drop_from_npc = true;
								S_ServerMessage message = new S_ServerMessage(813, npc.getDesc(), item.getLogNameRef(), pc.getName());
								for (L1PcInstance partymember : party.getMembersArray()) {
									if (!partymember.getConfig().RootMent) {
										continue;
									}
									partymember.sendPackets(message);
								}
								message.clear();
								message = null;
								item.setDropNpc(null);
							} else if (item.getItemId() == 31152 || item.getItemId() == 131152 || item.getItemId() == 3000122 || item.getItemId() == 90012 || item.getItemId() == 90013) {// 가호 획득 멘트
								S_ServerMessage message = new S_ServerMessage(403, String.format("%s : %s", item.getLogNameRef(), pc.getName()));
								for (L1PcInstance partymember : party.getMembersArray()) {
									if (!partymember.getConfig().RootMent) {
										continue;
									}
									partymember.sendPackets(message);
								}
								message.clear();
								message = null;
							}
						}
					}
					pc.getLight().turnOnOffLight();
					pc.saveInventory();// 아이템저장시킴
				}
				// 일반 획득
				else
				{
					if ((item.getItem().getItemId() == L1ItemId.INN_ROOM_KEY || item.getItem().getItemId() == L1ItemId.INN_HALL_KEY) && pc.getInventory().full()) {
						pc.sendPackets(L1ServerMessage.sm3560);// 인벤토리 공간이 부족합니다.
						return;						
					}
										
					if (item.getDropNpc() != null) {
						is_drop_from_npc = true;
						item.setDropNpc(null);
					}
					groundInventory.tradeItem(item, pickupCount, inv);
					pc.getLight().turnOnOffLight();
					LoggerInstance.getInstance().addItemAction(ItemActionType.Pickup, pc, item, pickupCount);
				}
				item.disposeItemOuter();
				S_AttackStatus actionPck = new S_AttackStatus(pc, objectId, ActionCodes.ACTION_Pickup);
				if (!pc.isGmInvis() && !pc.isGhost() && !pc.isInvisble()) {
					pc.broadcastPacket(actionPck, false);
				}
				pc.sendPackets(actionPck, true);
				pc.getSpeedSync().setAttackSyncInterval(pc.getAcceleratorChecker().getRightInterval(ACT_TYPE.PICK_UP) + System.currentTimeMillis());// 액션 시간
				
				if (is_drop_from_npc && ItemMentTable.isMent(ItemMentType.PICK_UP, item.getItemId())) {
					S_NotificationMessage message = new S_NotificationMessage(display_position.screen_top, String.format(ItemMentTable.PICKUP_MESSAGE, item.getViewName()), "00 ff 00", 10);
					if (pc.getConfig().isGlobalMessege()) {
						pc.sendPackets(message);
					}
					world.broadcastPacket(pc, message, true);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
	    }
	}
	
	@Override
	public String getType() {
		return C_PICK_UP_ITEM;
	}
}


