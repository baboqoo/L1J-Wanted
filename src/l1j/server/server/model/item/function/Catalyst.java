package l1j.server.server.model.item.function;

import java.util.HashMap;

import l1j.server.common.bin.CatalystTableCommonBinLoader;
import l1j.server.common.bin.catalyst.CatalystTableT;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.CatalystTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.message.S_MessegeNoti;
import l1j.server.server.serverpackets.message.S_NotificationMessage;
import l1j.server.server.serverpackets.message.S_NotificationMessage.display_position;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Catalyst;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class Catalyst extends L1ItemInstance {// catalyst-common.bin
	private static final long serialVersionUID = 1L;
	
	private L1PcInstance pc;
	private L1PcInventory inv;
	private final HashMap<Integer, CatalystTableT.CatalystRewardInfoT> map;
	private CatalystTableT.CatalystRewardInfoT catalyst;
	private L1ItemInstance inputItem;
	private L1Catalyst dbInfo;
	private int useNameId;
	private int inputNameId;
	
	public Catalyst(L1Item item) {
		super(item);
		useNameId			= item.getItemNameId();
		map					= CatalystTableCommonBinLoader.getCatalyst(useNameId);
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
		
		// 낚시 릴
		if (inputItem.getItem().get_interaction_type() == L1ItemType.FISHING_ROD.getInteractionType() 
				&& FishingRil.useRil(pc, this, inputItem)) {
			return;
		}
		
		inputNameId			= inputItem.getItem().getItemNameId();
		catalyst			= map.get(inputNameId);
		if (catalyst == null) {
			pc.sendPackets(L1ServerMessage.sm79);
			return;
		}
		
		int output			= catalyst.get_output();
		int successProb		= catalyst.get_successProb();
		int rewardCount		= catalyst.get_rewardCount();
		
		dbInfo	= CatalystTable.getCatalyst(useNameId, inputNameId);
		if (dbInfo != null && dbInfo.get_successProb() > 0) {
			successProb		= dbInfo.get_successProb();
		}
		
		L1Item outputItem	= ItemTable.getInstance().findItemByNameId(output);
		if (outputItem == null) {
			System.out.println(String.format(
					"[Catalyst] OUTPUT_NOT_FOUND : USE_DB_ID(%d), INPUT_ID(%d), OUTPUT_ID(%d)",
					this.getItemId(), inputNameId, output));
			return;
		}
		
		int rnd = 0;
		if (successProb != 100) {
			rnd = CommonUtil.random(100) + 1;
		}
		if (successProb == 100 || rnd <= successProb) {
			success(outputItem, rewardCount);
		} else {
			failure();
		}
		
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
	void success(L1Item outputItem, int rewardCount) {
		L1ItemInstance result = null;
		if (useNameId >= 4651 && useNameId <= 4655 
				|| useNameId == 6740 || useNameId == 6913
				|| useNameId >= 9264 && useNameId <= 9416
				|| useNameId >= 12617 && useNameId <= 12622
				|| useNameId >= 12651 && useNameId <= 12656
				|| useNameId >= 12689 && useNameId <= 12699
				|| useNameId >= 12735 && useNameId <= 12746
				|| useNameId == 12772 || useNameId == 14187
				|| useNameId >= 14159 && useNameId <= 14167
				|| useNameId >= 14416 && useNameId <= 14424
				) {// 인챈트 보존
			result = inv.storeItem(outputItem.getItemId(), rewardCount, inputItem.getEnchantLevel());
		} else {
			result = inv.storeItem(outputItem.getItemId(), rewardCount);
		}
		if (useNameId == 17053 || useNameId == 17054) {// 생명의 나뭇잎
			pc.sendPackets(new S_ServerMessage(4964, inputItem.getItem().getDesc()), true);// %0은(는) 새 생명이 부여 되었습니다.(4964)
			if (dbInfo != null && dbInfo.is_broad()) {
				sendMessage(new S_MessegeNoti(5085, inputItem.getItem().getDesc(), inputNameId));
			}
		} else if (useNameId == 25940) {// 소생 주문서
			pc.sendPackets(new S_ServerMessage(5439, inputItem.getItem().getDesc()), true);// %0에 소생의 축복의 스며듭니다.
			if (dbInfo != null && dbInfo.is_broad()) {
				sendMessage(new S_MessegeNoti(5085, inputItem.getItem().getDesc(), inputNameId));
			}
		} else {
			pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", result.getDesc(), rewardCount)), true);// {0} 획득
			if (dbInfo != null && dbInfo.is_broad()) {
//AUTO SRM: 				sendMessage(new S_NotificationMessage(display_position.screen_top, String.format("아덴월드의 누군가 %s를(을) 획득하였습니다.", result.getViewName()), "00 ff 00", 10)); // CHECKED OK
				sendMessage(new S_NotificationMessage(display_position.screen_top, String.format(S_SystemMessage.getRefText(1289) + " %s " + S_SystemMessage.getRefText(1290), result.getViewName()), "00 ff 00", 10));
			}
		}
		
		// 재료 소모
		inv.removeItem(inputItem, 1);
		inv.removeItem(this, 1);
	}
	
	/**
	 * 실패
	 * @param pc
	 * @param catalyst
	 */
	void failure() {
		int preserveProb	= catalyst.get_preserveProb();
		if (preserveProb > 0 && CommonUtil.random(100) + 1 <= preserveProb) {
			preserve();
			return;
		}
		
		int failOutput	= catalyst.get_failOutput();
		if (failOutput > 0) {
			failOutput(failOutput);
		}
		
		if (useNameId == 17053 || useNameId == 17054) {// 생명의 나뭇잎
			pc.sendPackets(new S_ServerMessage(4965, inputItem.getItem().getDesc()), true);// %0은(는) 기운을 흡수하지 못하고 소멸하였습니다.(4965)
		} else if (useNameId == 25940) {// 소생 주문서
			pc.sendPackets(new S_ServerMessage(5440, inputItem.getItem().getDesc()), true);// %0은 소생하지 못하고 증발하엿습니다.
		} else {
			pc.sendPackets(new S_ServerMessage(158, inputItem.getItem().getDesc()), true);// 소멸: {0}%s 증발 하였습니다.
		}
		
		// 재료 소모
		inv.removeItem(inputItem, 1);
		inv.removeItem(this, 1);
	}
	
	/**
	 * 재료 보존
	 * @param pc
	 */
	void preserve() {
		if (useNameId == 17054) {// 축복받은 생명의 나뭇잎
			pc.sendPackets(new S_ServerMessage(5116, inputItem.getItem().getDesc()), true);// %0은(는) 기운을 흡수하지 못하였으나 다행이 소멸하지 않았습니다.(5116)
		} else {
			pc.sendPackets(new S_ServerMessage(4056, inputItem.getLogNameRef()), true);// 인챈트: {0}%s 소멸의 굴레를 극복 하였습니다.
		}
		inv.removeItem(this, 1);
	}
	
	/**
	 * 실패 보상
	 * @param pc
	 * @param failOutput
	 */
	void failOutput(int failOutput) {
		L1Item failoutputItem	= ItemTable.getInstance().findItemByNameId(failOutput);
		if (failoutputItem == null) {
			System.out.println(String.format(
					"[Catalyst] OUTPUT_NOT_FOUND : USE_DB_ID(%d), INPUT_ID(%d), FAIL_OUTPUT_ID(%d)",
					this.getItemId(), inputNameId, failOutput));
			return;
		}
		L1ItemInstance result = inv.storeItem(failoutputItem.getItemId(), 1);
		pc.sendPackets(new S_ServerMessage(403, String.format("%s (%d)", result.getDesc(), 1)), true);// {0} 획득
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


