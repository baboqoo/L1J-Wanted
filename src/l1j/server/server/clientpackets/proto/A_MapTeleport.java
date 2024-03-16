package l1j.server.server.clientpackets.proto;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.serverpackets.S_Paralysis;

public class A_MapTeleport extends ProtoHandler {
	protected A_MapTeleport(){}
	private A_MapTeleport(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private static L1NpcInstance TELEPORT_NPC;
	private static final String ACTION_T_PCBANG				= "T_pcbang";
	private static final String ACTION_UNICORN_TEMPLE		= "tel_unicorn_temple";
	private static final String ACTION_UNICORN_TEMPLE_BOOST	= "tel_unicorn_temple_boost";
	private static final ConcurrentHashMap<String, L1NpcAction> ACTIONS = new ConcurrentHashMap<>();

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.isNotTeleport()) {
			return;
		}
		if (!_pc.getMap().isEscapable() || _client.isInterServer()) {
			_pc.sendPackets(S_Paralysis.TELEPORT_UNLOCK);
			_pc.sendPackets(L1ServerMessage.sm647);
			return;
		}
		if (TELEPORT_NPC == null) {
			TELEPORT_NPC = L1World.getInstance().findNpc(9000);
		}
		if (TELEPORT_NPC == null) {
			System.out.println("A_MapTeleport -> worldmap not found teleport_npc and npcId_9000 spawn check");
			return;
		}
		readP(1);
		int length				= readC();
		String mapIndex			= readS(length);
		boolean consumeComplete	= false;
		switch(mapIndex){
		case ACTION_T_PCBANG:				consumeComplete = _pc.getInventory().consumeItem(L1ItemId.PIXIE_GOLD_FEATHER, 1);break;
		case ACTION_UNICORN_TEMPLE:			consumeComplete = _pc.getInventory().consumeItem(810000, 1);break;
		case ACTION_UNICORN_TEMPLE_BOOST:	consumeComplete = _pc.getInventory().consumeItem(810000, 3);break;
		default:							consumeComplete = _pc.getInventory().consumeItem(140100, 1);break;
		}
		if (!consumeComplete) {
			return;
		}
		
		L1NpcAction action = ACTIONS.get(mapIndex);
		if (action == null) {
			action = NpcActionTable.getInstance().get(mapIndex, _pc, TELEPORT_NPC);
			if (action == null) {
				System.out.println(String.format("[A_MapTeleport] ACTION_EMPTY : REQUEST(%s)", mapIndex));
				return;
			}
			ACTIONS.put(mapIndex, action);
		}
		action.execute(mapIndex, _pc, TELEPORT_NPC, null);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_MapTeleport(data, client);
	}

}

