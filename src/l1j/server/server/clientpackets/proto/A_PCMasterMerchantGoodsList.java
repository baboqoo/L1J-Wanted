package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1Npc;

public class A_PCMasterMerchantGoodsList extends ProtoHandler {
	protected A_PCMasterMerchantGoodsList(){}
	private A_PCMasterMerchantGoodsList(byte[] data, GameClient client) {
		super(data, client);
	}
	
	static final int GOODS_NPC_ID = 7000031;// 코하
	static L1NpcInstance MERCHANT_GOODS_NPC;
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || !_pc.isPCCafe()) {
			return;
		}
		
		if (MERCHANT_GOODS_NPC == null) {
			L1Npc temp	= NpcTable.getInstance().getTemplate(GOODS_NPC_ID);
			if (temp == null) {
				System.out.println(String.format("[A_PCMasterMerchantGoodsList] NPC_TEMPLATE_NOT_FOUND : NPC_ID(%d)", GOODS_NPC_ID));
				return;
			}
			if (!temp.isNotification()) {
				temp.setNotification(true);
			}
			MERCHANT_GOODS_NPC = L1World.getInstance().findNpc(temp.getNpcId());
			if (MERCHANT_GOODS_NPC == null) {
				System.out.println(String.format("[A_PCMasterMerchantGoodsList] NPC_NOT_FOUND : NPC_ID(%d)", temp.getNpcId()));
				return;
			}
		}
		
		if (!_pc.knownsObject(MERCHANT_GOODS_NPC)) {
			MERCHANT_GOODS_NPC.onPerceive(_pc);
		}
		MERCHANT_GOODS_NPC.onTalkAction(_pc);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PCMasterMerchantGoodsList(data, client);
	}

}

