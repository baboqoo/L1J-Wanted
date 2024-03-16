package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.S_CastleProperty;

public class A_CastleProperty extends ProtoHandler {
	protected A_CastleProperty(){}
	private A_CastleProperty(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private A_CastleProperty.REQ_KIND _reqKind;
	private int _castle_Id;
	
	void parse() {
		if (_total_length < 4) {
			return;
		}
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x08:
				_reqKind = A_CastleProperty.REQ_KIND.fromInt(readC());
				break;
			case 0x10:
				_castle_Id = readC();
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
		if (_reqKind == null) {
			return;
		}
		if (_reqKind == A_CastleProperty.REQ_KIND.CASTLE_TOTAL) {
			_pc.sendPackets(new S_CastleProperty(), true);
		} else {
			_pc.sendPackets(new S_CastleProperty(_castle_Id), true);
		}
	}
	
	public enum REQ_KIND{
		CASTLE_TOTAL(1),
		CASTLE_ONE(2),
		;
		private int value;
		REQ_KIND(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(REQ_KIND v){
			return value == v.value;
		}
		public static REQ_KIND fromInt(int i){
			switch(i){
			case 1:
				return CASTLE_TOTAL;
			case 2:
				return CASTLE_ONE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments REQ_KIND, %d", i));
			}
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CastleProperty(data, client);
	}

}

