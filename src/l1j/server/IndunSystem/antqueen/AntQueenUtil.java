package l1j.server.IndunSystem.antqueen;

import javolution.util.FastTable;
import l1j.server.GameSystem.inn.InnType;
import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.templates.L1Item;

public class AntQueenUtil {
	protected static FastTable<L1PcInstance> PcStageCK() {
		FastTable<L1PcInstance> pcList = new FastTable<L1PcInstance>();
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if ((pc.getMapId() >= 15891 && pc.getMapId() <= 15899) || pc.getMapId() == 15902) {
				pcList.add(pc);
			}
		}
		return pcList;
	}
	
	protected static void timerSend(int time){
		S_PacketBox pck = new S_PacketBox(S_PacketBox.TIME_COUNT, time);
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers() ){
			if (pc.getMapId() >= 15871 && pc.getMapId() <= 15902) {
				pc.sendPackets(pck);
			}
		}
		pck.clear();
		pck = null;
	}
	
	protected static void sendRoomMessage(String msg) {
		FastTable<L1PcInstance> pcList = PcStageCK();
		S_PacketBox pck = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg);
		for (L1PcInstance pc : pcList) {
			pc.sendPackets(pck);
		}
		pck.clear();
		pck = null;
		pcList.clear();
		pcList = null;
	}
	
	protected static void sendAllMessage(String msg) {
		S_PacketBox pck = new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg);
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getMapId() >= 15871 && pc.getMapId() <= 15902) {
				pc.sendPackets(pck);
			}
		}
		pck.clear();
		pck = null;
	}
	
	protected static void sendEffect(int effectId){
		FastTable<L1PcInstance> pcList = PcStageCK();
		for (L1PcInstance pc : pcList) {
			pc.send_effect(effectId);
		}
		pcList.clear();
		pcList = null;
	}
	
	protected static void spawn(int x, int y, short MapId, int Heading, int npcId, int randomRange, int timeMillisToDelete) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(MapId);
			if (randomRange == 0) {
				npc.getLocation().set(x, y, MapId);
				npc.getLocation().forward(Heading);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if(npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation()))break;
				} while (tryCount < 50);
				if (tryCount >= 50) {
					npc.getLocation().forward(Heading);
				}
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(Heading);
			if (npcId == 5182) {// 독이팩트
				S_EffectLocation effect		= new S_EffectLocation(npc.getX(), npc.getY(), 14258);
				S_CharVisualUpdate visual	= new S_CharVisualUpdate(npc, 0);
				npc.setActionStatus(ActionCodes.ACTION_AxeAttack);
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc)) {
					npc.onPerceive(pc);
					pc.sendPackets(effect);// 독이팩트
					pc.sendPackets(visual);
				}
				npc.setActionStatus(0);
				effect.clear();
				visual.clear();
				effect = null;
				visual = null;
			}
			L1World world = L1World.getInstance();
			world.storeObject(npc);
			world.addVisibleObject(npc);
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
		}
	}
	
	protected static void deleteAction(int deleteId) {
		L1ItemInstance item = null;
		L1World world = L1World.getInstance();
		for (L1NpcInstance npc : world.getAllNpc()) {
			if (npc.getNpcId() == deleteId) {
				if (deleteId == AntQueen.REWARD_TRAP) {
					for (L1PcInstance pc : world.getVisiblePlayer(npc, 1)) {
						if (!pc.isDead() && pc.getX() == npc.getX() && pc.getY() == npc.getY()) {
							L1Item tempItem = ItemTable.getInstance().getTemplate(420114);
							if (pc.getInventory().checkAddItem(tempItem, 1) != L1Inventory.OK) 
								continue;
							else {
								item = pc.getInventory().storeItem(420114, 1);// 에르자베 상자
								pc.sendPackets(new S_ServerMessage(403, item.getItem().getDesc()), true);
								break;
							}
						}
					}
				}
				npc.setCurrentHp(0);
				npc.setDead(true);
				npc.setActionStatus(ActionCodes.ACTION_Die);
				Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Die), true);
				npc.deleteMe();
			}
		}
	}
	
}

