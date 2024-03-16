package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.craft.CraftInfoLoader;
import l1j.server.GameSystem.craft.bean.L1CraftInfo;
import l1j.server.common.bin.CraftCommonBinLoader;
import l1j.server.common.bin.craft.Craft;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.serverpackets.craft.S_CraftIdList;
import l1j.server.server.serverpackets.craft.S_CraftIdList.eCraftIdListReqResultType;

public class A_CraftIdList extends ProtoHandler {
	protected A_CraftIdList(){}
	private A_CraftIdList(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int npcObjId;
	private L1NpcInstance npc;
	private int[] craftList;
	
	void parse(){
		if (_total_length < 2) {
			return;
		}
		while (!isEnd()) {
			int tag = readC();
			switch(tag){
			case 0x08:
				npcObjId = read4(read_size());
				break;
			default:
				return;
			}
		}
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		parse();
		L1Object obj	= L1World.getInstance().findObject(npcObjId);
		if (obj instanceof L1NpcInstance == false) {
			_pc.sendPackets(S_CraftIdList.ERROR_INVALID_NPC_ID);
			return;
		}
		npc				= (L1NpcInstance) obj;
		craftList		= CraftInfoLoader.getCraftIds(npc.getNpcId());
		if (craftList == null || craftList.length <= 0) {
			_pc.sendPackets(S_CraftIdList.ERROR_INVALID_NPC_ID);
			return;
		}
		
		if (isExceptionNpc() && !_pc.knownsObject(obj)) {
			_pc.sendPackets(S_CraftIdList.ERROR_OUT_OF_RANGE);
			return;
		}
		
		if (isLimitCraft(craftList)) {
			// 한정 제작 리스트
			_pc.sendPackets(new S_CraftIdList(eCraftIdListReqResultType.SUCCESS, craftList, _pc), true);
		} else {
			// 패킷 재사용
			_pc.sendPackets(S_CraftIdList.getCraftPacket(_pc, npc.getNpcId(), craftList));
		}
	}
	
	boolean isExceptionNpc() {
		L1CraftInfo dbInfo = null;
		Craft bin = null;
		for (int craftId : craftList) {
			dbInfo = CraftInfoLoader.getInfo(craftId);
			if (dbInfo == null) {
				System.out.println(String.format("[A_CraftIdList] DB_INFO_NOT_FOUND : NPC_ID(%d), CRAFT_ID(%d)", npc.getNpcId(), craftId));
				return false;
			}
			bin = dbInfo.getBin();
			if (bin == null) {
				System.out.println(String.format("[A_CraftIdList] BIN_DATA_NOT_FOUND : NPC_ID(%d), CRAFT_ID(%d)", npc.getNpcId(), craftId));
				return false;
			}
			if (!bin.get_except_npc()) {
				return false;
			}
		}
		return true;
	}
	
	boolean isLimitCraft(int[] craftList){
		Craft bin = null;
		for (int craftId : craftList) {
			bin = CraftCommonBinLoader.getCraft(craftId);
			if (bin != null && bin.get_max_successcount() > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CraftIdList(data, client);
	}

}

