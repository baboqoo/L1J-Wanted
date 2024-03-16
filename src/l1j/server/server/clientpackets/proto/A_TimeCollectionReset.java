package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionNotiType;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;

public class A_TimeCollectionReset extends ProtoHandler {
	protected A_TimeCollectionReset(){}
	private A_TimeCollectionReset(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int groupId;
	private int setId;
	
	void parsePacket(){
		if (_total_length < 2) {
			return;
		}
		while (!isEnd()) {
			int code	= readC();
			switch (code) {
			case 0x08:
				groupId = readC();
				break;
			case 0x10:
				setId = readC();
				break;
			default:
				return;
			}
		}
	}
	
	boolean isValidation(){
		return groupId > 0 && setId > 0;
	}

	@Override
	protected void doWork() throws Exception {
		if (!Config.COLLECTION.TIME_COLLECTION_ACTIVE || _pc == null || _pc.isGhost() || _pc.getTimeCollection() == null) {
			return;
		}
		parsePacket();
		if (!isValidation()) {
			return;
		}
		
		// 컬렉션 여부 검사
		L1TimeCollection collection			= L1TimeCollectionLoader.getData(groupId, setId);
		if (collection == null) {
			System.out.println(String.format(
					"[A_TimeCollectionReset] NOT_COLLECTION : GROUP(%d), ID(%d), NAME(%s)", 
					groupId, setId, _pc.getName()));
			return;
		}
		
		// 제거
		_pc.getTimeCollection().reset(collection, L1TimeCollectionNotiType.END);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_TimeCollectionReset(data, client);
	}

}

