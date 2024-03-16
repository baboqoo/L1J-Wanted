package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.craft.CraftInfoLoader;
import l1j.server.GameSystem.craft.bean.L1CraftInfo;
import l1j.server.common.bin.craft.Craft;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.craft.S_CraftBatch;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class A_CraftBatch extends ProtoHandler {
	protected A_CraftBatch(){}
	private A_CraftBatch(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _npc_id;
	private int _craft_id;
	private int _count;
	
	private L1CraftInfo _db_info;
	private Craft _bin;
	
	void parse() {
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x08:
				_npc_id = readBit();
				break;
			case 0x10:
				_craft_id = readBit();
				break;
			case 0x18:
				_count = readBit();
				break;
			default:
				return;
			}
		}
	}
	
	boolean isValidation() {
		return _craft_id > 0 && _count > 0;
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		parse();
		if (!isValidation()) {
			return;
		}
		if (_pc.getInventory().getWeightPercent() >= 99) {
			_pc.sendPackets(L1SystemMessage.CRAFT_WEIGHT_FAIL);
			return;
		}
		
		if (_npc_id > 0) {
			L1Object obj = L1World.getInstance().findObject(_npc_id);
			if (obj == null) {
				return;
			}
			if (!_pc.knownsObject(obj)) {
				return;
			}
		}
		
		// TODO db정보를 불러온다
		_db_info = CraftInfoLoader.getInfo(_craft_id);
		if (_db_info == null) {
			System.out.println(String.format("[A_CraftBatch] DB_INFO_NOT_FOUND : CRAFT_ID(%d)", _craft_id));
			return;
		}
		// TODO bin정보를 불러온다.
		_bin = _db_info.getBin();
		if (_bin == null) {
			System.out.println(String.format("[A_CraftBatch] BIN_DATA_NOT_FOUND : CRAFT_ID(%d)", _craft_id));
			return;
		}
		
		// 제작 시간 검증, 배치 시간 완료전에 제작 요청이 들어오는 경우
		long current_time		= System.currentTimeMillis();
		long user_batch_time	= _pc.getConfig().get_craft_batch_time();
		if (user_batch_time > 0 && user_batch_time > current_time) {
			_pc.sendPackets(new S_CraftBatch(S_CraftBatch.eCraftBatchAckResultType.RP_FAILURE), true);
			denals_print();
			return;
		}
		int batch_sec			= _bin.get_batch_delay_sec();
		if (batch_sec > 1) {// 오차 범위 1초
			_pc.getConfig().set_craft_batch_time(current_time + ((long)--batch_sec * 1000L));
		}
		_pc.sendPackets(new S_CraftBatch(S_CraftBatch.eCraftBatchAckResultType.RP_SUCCESS), true);
	}
	
	void denals_print() {
		//String message = String.format("★☆★ 중계기(CRAFT) 의심 유저 : NAME[%s] 배치 시간이 올바르지 않습니다. ★☆★", _pc.getName());
		String message = String.format("--- Suspected repeater (CRAFT) user: NAME[%s] placement time is incorrect. ---", _pc.getName());
		System.out.println(message);
		S_SystemMessage pck = new S_SystemMessage(message);
		for (L1PcInstance gm : L1World.getInstance().getAllGms()) {
			if (gm == null || gm.getNetConnection() == null) {
				continue;
			}
			gm.sendPackets(pck);
		}
		pck.clear();
		pck = null;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CraftBatch(data, client);
	}

}

