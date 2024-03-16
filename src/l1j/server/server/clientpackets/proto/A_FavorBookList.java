package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.ServerBasePacket;

public class A_FavorBookList extends ProtoHandler {
	protected A_FavorBookList(){}
	private A_FavorBookList(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int listId;
	
	void parse(){
		if (_total_length < 2) {
			return;
		}
		while (!isEnd()) {
			switch (readC()) {
			case 0x08:
				listId = readC();
				break;
			default:
				return;
			}
		}
	}
	
	boolean isValidation(){
		return listId >= 0;
	}

	@Override
	protected void doWork() throws Exception {
		if (!Config.COLLECTION.FAVOR_BOOK_ACTIVE || _pc == null || _pc.isGhost() || _pc.getFavorBook() == null) {
			return;
		}
		parse();
		if (!isValidation()) {
			return;
		}
		ServerBasePacket pck = _pc.getFavorBook().getListPacket(listId);// 생성되어 있는 패킷
		if (pck == null) {
			System.out.println(String.format(
					"[A_FavorBookUI] PACKET_NOT_FOUND : LIST_ID(%d), NAME(%s)", 
					listId, _pc.getName()));
			return;
		}
		_pc.sendPackets(pck);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_FavorBookList(data, client);
	}

}

