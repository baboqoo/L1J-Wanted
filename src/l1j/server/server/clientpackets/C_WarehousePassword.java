package l1j.server.server.clientpackets;

import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.warehouse.S_WareHouseItemListNoti;
import l1j.server.server.serverpackets.warehouse.S_WareHouseItemListNoti.eWarehouseType;

public class C_WarehousePassword extends ClientBasePacket{
	private static final String C_WAREHOUSE_PASSWORD = "[C] C_WarehousePassword";
	
	private static final int SETTING_PASSWORD	= 0;
	private static final int WAREHOUSE_LIST		= 1;
	
	public C_WarehousePassword(byte[] data, GameClient client){
		super(data);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		Account account = client.getAccount();
		if (account == null) {
			return;
		}
		int warehouse_password = account.getWarehousePassword();
		int type = readC();
		switch(type){
		case SETTING_PASSWORD:// 설정
			int oldPassword = readCH();		
			readP(1);	// dummy
			int newPassword = readCH();
			if (warehouse_password == 0 || warehouse_password == oldPassword) {
				account.updateWarehousePassword(newPassword);
			} else {
				pc.sendPackets(L1ServerMessage.sm835);
			}
			break;
		case WAREHOUSE_LIST:// 창고 찾기
			int checkPassword = readCH();							
			readP(1);	// dummy
			int objId = readD();			
			if (warehouse_password == 0 || warehouse_password == checkPassword) {
			    if (pc.getLevel() >= 5) {
			    	L1NpcInstance npc = (L1NpcInstance) L1World.getInstance().findObject(objId);
			    	if (npc == null) {
			    		return;
			    	}
			    	S_WareHouseItemListNoti rpl = new S_WareHouseItemListNoti(eWarehouseType.TRADE_RETRIEVE, npc, pc);
			    	pc.sendPackets(rpl, true);
			    }
			} else {
				pc.sendPackets(L1ServerMessage.sm835);
			}
			break;
		}
	}

	public String getType() {
		return C_WAREHOUSE_PASSWORD;
	}
}
