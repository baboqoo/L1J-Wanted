package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.craft.S_CraftListAll;
import l1j.server.server.utils.HexHelper;

public class A_CraftListAll extends ProtoHandler {
	protected A_CraftListAll(){}
	private A_CraftListAll(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private byte[] _hash_value;
	
	void parse(){
		if (_total_length < 2) {
			return;
		}
		while(!isEnd()){
			int code = readC();
			switch(code) {
			case 0x0a:
				_hash_value = readByte();
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
		if (_pc.isGm()) {
			System.out.println("CRAFR_LIST_ALL_HASH\r\n" + HexHelper.toSourceString(_hash_value, _hash_value.length));
		}
		_pc.sendPackets(S_CraftListAll.LOAD_FINISH);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CraftListAll(data, client);
	}

}

