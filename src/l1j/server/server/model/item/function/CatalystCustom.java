package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.CatalystTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.message.S_NotificationMessage;
import l1j.server.server.serverpackets.message.S_NotificationMessage.display_position;
import l1j.server.server.serverpackets.message.S_MessegeNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1CatalystCustom;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class CatalystCustom extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	private L1PcInstance pc;
	private L1PcInventory inv;
	private L1ItemInstance inputItem;
	private L1CatalystCustom dbInfo;
	private int useItemId;
	private int inputItemId;
	private int inputEnchant;
	
	public CatalystCustom(L1Item item) {
		super(item);
		useItemId = item.getItemId();
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance == false) {
			return;
		}
		pc					= (L1PcInstance) cha;
		int inputObjId		= packet.readD();
		inv					= pc.getInventory();
		inputItem			= inv.getItem(inputObjId);
		if (inputItem == null) {
			return;
		}
		
		inputItemId			= inputItem.getItem().getItemId();
		inputEnchant		= inputItem.getEnchantLevel();
		dbInfo				= CatalystTable.getCatalystCustom(useItemId, inputItemId, inputEnchant);
		if (dbInfo == null) {
			pc.sendPackets(L1ServerMessage.sm79);
			return;
		}
		
		int outputItemId	= dbInfo.getOutputItemId();
		int successProb		= dbInfo.getSuccessProb();
		
		L1Item outputItem	= ItemTable.getInstance().getTemplate(outputItemId);
		if (outputItem == null) {
			System.out.println(String.format(
					"[CatalystCustom] OUTPUT_NOT_FOUND : USE_DB_ID(%d), INPUT_DB_ID(%d), INPUT_ENCHANT(%d), OUTPUT_DB_ID(%d)",
					this.getItemId(), inputItemId, inputEnchant, outputItemId));
			return;
		}
		
		int rnd = 0;
		if (successProb != 100) {
			rnd = CommonUtil.random(100) + 1;
		}
		if (successProb == 100 || rnd <= successProb) {
			success(outputItem, dbInfo.getRewardCount(), dbInfo.getRewardEnchant());
		} else {
			failure();
		}
		
		// 재료 소모
		inv.removeItem(inputItem, 1);
		inv.removeItem(this, 1);
		
		if (successProb != 100 && pc.isGm()) {
			pc.sendPackets(new S_SystemMessage(String.format("CATALYST_CHANCE : RND(%d) / PROB(%d)", rnd, successProb)), true);
		}
	}
	
	/**
	 * 성공
	 * @param pc
	 * @param outputItem
	 * @param rewardCount
	 */
	void success(L1Item outputItem, int rewardCount, int rewardEnchant) {
		L1ItemInstance result = inv.storeItem(outputItem.getItemId(), rewardCount, rewardEnchant);
		
		if (useItemId == 31086 || useItemId == 31154) {// 생명의 나뭇잎
			pc.sendPackets(new S_ServerMessage(4964, inputItem.getItem().getDesc()), true);// %0은(는) 새 생명이 부여 되었습니다.(4964)
			if (dbInfo != null && dbInfo.isBroad()) {
				sendMessage(new S_MessegeNoti(5085, inputItem.getItem().getDesc(), result.getItem().getItemNameId()));
			}
		} else if (useItemId == 37200) {// 소생 주문서
			pc.sendPackets(new S_ServerMessage(5439, inputItem.getItem().getDesc()), true);// %0에 소생의 축복의 스며듭니다.
			if (dbInfo != null && dbInfo.isBroad()) {
				sendMessage(new S_MessegeNoti(5085, inputItem.getItem().getDesc(), result.getItem().getItemNameId()));
			}
		} else {
			pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", result.getDesc(), rewardCount)), true);// {0} 획득
			if (dbInfo.isBroad()) {
//AUTO SRM: 				S_NotificationMessage message = new S_NotificationMessage(display_position.screen_top, String.format("아덴월드의 누군가 %s를(을) 획득하였습니다.", result.getViewName()), "00 ff 00", 10); // CHECKED OK
				S_NotificationMessage message = new S_NotificationMessage(display_position.screen_top, String.format(S_SystemMessage.getRefText(1289) + " %s " + S_SystemMessage.getRefText(1290), result.getViewName()), "00 ff 00", 10);
				sendMessage(message);
			}
		}
	}
	
	/**
	 * 실패
	 * @param pc
	 * @param catalyst
	 */
	void failure() {
		if (useItemId == 31086 || useItemId == 31154) {// 생명의 나뭇잎
			pc.sendPackets(new S_ServerMessage(4965, inputItem.getItem().getDesc()), true);// %0은(는) 기운을 흡수하지 못하고 소멸하였습니다.(4965)
		} else if (useItemId == 37200) {// 소생 주문서
			pc.sendPackets(new S_ServerMessage(5440, inputItem.getItem().getDesc()), true);// %0은 소생하지 못하고 증발하엿습니다.
		} else {
			pc.sendPackets(new S_ServerMessage(158, inputItem.getItem().getDesc()), true);// 소멸: {0}%s 증발 하였습니다.
		}
	}
	
	/**
	 * 메세지 출력
	 * @param pc
	 * @param message
	 */
	void sendMessage(ServerBasePacket message) {
		if (pc.getConfig().isGlobalMessege()) {
			pc.sendPackets(message);
		}
		L1World.getInstance().broadcastPacket(pc, message, true);
	}
	
}


