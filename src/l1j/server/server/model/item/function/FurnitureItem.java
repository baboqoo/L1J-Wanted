package l1j.server.server.model.item.function;

import java.lang.reflect.Constructor;

import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.action.S_AttackStatus;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;

public class FurnitureItem extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public FurnitureItem(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int itemId = this.getItemId();
			if (itemId == 41401) {
				useFurnitureRemovalWand(pc, packet.readD());
			} else {
				useFurnitureItem(pc, itemId, this.getId());
			}
		}
	}
	
	private void useFurnitureItem(L1PcInstance pc, int itemId, int itemObjectId) {
		if (!L1HouseLocation.isInHouse(pc.getX(), pc.getY(), pc.getMapId())) {
			pc.sendPackets(L1ServerMessage.sm563); // \f1 여기에서는 사용할 수 없습니다.
			return;
		}
		boolean isAppear = true;
		L1FurnitureInstance furniture = null;
		for (L1Object l1object : L1World.getInstance().getObject()) {
			if (l1object instanceof L1FurnitureInstance) {
				furniture = (L1FurnitureInstance) l1object;
				if (furniture.getItemObjId() == itemObjectId) { // 이미 꺼내고 있는 가구
					isAppear = false;
					break;
				}
			}
		}
		if (isAppear) {
			if (pc.getMoveState().getHeading() != 0 && pc.getMoveState().getHeading() != 2) {
				return;
			}
			int npcId = getItem().getEtcValue();
			try {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(npcId);
				if (l1npc != null) {
					try {
						String s = l1npc.getImpl();
						Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance").getConstructors()[0];
						Object aobj[] = { l1npc };
						furniture = (L1FurnitureInstance) constructor.newInstance(aobj);
						furniture.setId(IdFactory.getInstance().nextId());
						furniture.setMap(pc.getMapId());
						if (pc.getMoveState().getHeading() == 0) {
							furniture.setX(pc.getX());
							furniture.setY(pc.getY() - 1);
						} else if (pc.getMoveState().getHeading() == 2) {
							furniture.setX(pc.getX() + 1);
							furniture.setY(pc.getY());
						}
						furniture.setHomeX(furniture.getX());
						furniture.setHomeY(furniture.getY());
						furniture.getMoveState().setHeading(0);
						furniture.setItemObjId(itemObjectId);

						L1World.getInstance().storeObject(furniture);
						L1World.getInstance().addVisibleObject(furniture);
						FurnitureSpawnTable.getInstance().insertFurniture(furniture);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception exception) {
			}
		} else {
			furniture.deleteMe();
			FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
		}
	}
	
	private void useFurnitureRemovalWand(L1PcInstance pc, int targetId) {
		pc.broadcastPacketWithMe(new S_AttackStatus(pc, 0, ActionCodes.ACTION_Wand), true);
		int chargeCount = this.getChargeCount();
		if(chargeCount <= 0)return;
		L1Object target = L1World.getInstance().findObject(targetId);
		if (target != null && target instanceof L1FurnitureInstance) {
			L1FurnitureInstance furniture = (L1FurnitureInstance) target;
			furniture.deleteMe();
			FurnitureSpawnTable.getInstance().deleteFurniture(furniture);
			this.setChargeCount(this.getChargeCount() - 1);
			if (this.getChargeCount() == 0) {
				pc.getInventory().removeItem(this);
			} else {
				pc.getInventory().updateItem(this, L1PcInventory.COL_CHARGE_COUNT);
			}
		}
	}
	
}

