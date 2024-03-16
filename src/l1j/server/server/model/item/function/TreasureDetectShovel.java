package l1j.server.server.model.item.function;

import l1j.server.IndunSystem.treasureisland.TreasureIsland;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TreasureInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class TreasureDetectShovel extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public TreasureDetectShovel(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int objid = packet.readD();
			L1TreasureInstance treasure = (L1TreasureInstance)L1World.getInstance().findObject(objid);

			if (treasure == null || treasure.isExcavation()) {
				return;
			}

			TreasureIsland treasureIsl = TreasureIsland.getInstance();
			if (!treasureIsl.isActive()) {
				return;// 가동 여부
			}
			treasureIsl.excavationTreasure(pc, treasure);// 보물 발굴
			treasure.setExcavation(); // <--- new line moved from A_StartExcavationTreasure to fix the problem with Treasure Island
			treasure.deleteMe();// 제거
		}
	}
}

