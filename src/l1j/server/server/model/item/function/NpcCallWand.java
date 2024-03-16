package l1j.server.server.model.item.function;

import java.lang.reflect.Constructor;

import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.action.S_AttackStatus;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;

public class NpcCallWand extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public NpcCallWand(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			switch(this.getItemId()){
			case 410014:callNpc(pc, 60001);break;// 창고 소환 막대
			case 410015:callNpc(pc, 70030);break;// 상점 소환 막대
			}
		}
	}
	
	private void callNpc(L1PcInstance pc, int npcId) {
		pc.broadcastPacketWithMe(new S_AttackStatus(pc, 0, ActionCodes.ACTION_Wand), true);
		int chargeCount = this.getChargeCount();
		if (chargeCount <= 0) {
			return;
		}
		try {
			L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId);
			String s = l1npc.getImpl();
			Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
			Object aobj[] = { l1npc };
			L1NpcInstance npc = (L1NpcInstance) constructor.newInstance(aobj);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());
			npc.setX(pc.getX());
			npc.setY(pc.getY());
			npc.setHomeX(pc.getX());
			npc.setHomeY(pc.getY());
			npc.setMap(pc.getMapId());
			npc.getMoveState().setHeading(2);
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
			new L1NpcDeleteTimer(npc, 60000).begin();// 60초후바로삭제처리
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setChargeCount(this.getChargeCount() - 1);
		if (this.getChargeCount() == 0) {
			pc.getInventory().removeItem(this);
		} else {
			pc.getInventory().updateItem(this, L1PcInventory.COL_CHARGE_COUNT);
		}
	}
}

