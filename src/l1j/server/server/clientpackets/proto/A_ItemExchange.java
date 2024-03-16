package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;

public class A_ItemExchange extends ProtoHandler {
	protected A_ItemExchange(){}
	private A_ItemExchange(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _source_warehouse;
	private java.util.LinkedList<Integer> _source_id;
	
	void parse() {
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x08:
				_source_warehouse = readBit();
				break;
			case 0x10:
				if (_source_id == null) {
					_source_id = new java.util.LinkedList<Integer>();
				}
				_source_id.add(readBit());
				break;
			default:
				return;
			}
		}
	}
	
	boolean isValidation() {
		return _source_warehouse > 0 && _source_id != null;
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
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ItemExchange(data, client);
	}

}

