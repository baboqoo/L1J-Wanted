package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1ItemInstance;

public class A_ItemSelectCancel extends ProtoHandler {
	protected A_ItemSelectCancel(){}
	private A_ItemSelectCancel(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _bag_obj_id;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null) {
			return;
		}
		readP(1);// 0x08
		_bag_obj_id = readBit();
		L1ItemInstance bag_item	= _pc.getInventory().getItem(_bag_obj_id);
		if (bag_item == null) {
			_pc.denals_disconnect(String.format("[A_ItemSelectCancel] USE_ITEM_NOT_FOUND : NAME(%s)", _pc.getName()));
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ItemSelectCancel(data, client);
	}

}

