package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.model.item.collection.time.L1TimeCollectionHandler;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionNotiType;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;
import l1j.server.server.serverpackets.inventory.S_TimeCollectionBuffNoti;
import l1j.server.server.serverpackets.inventory.S_TimeCollectionChangeBuff;

public class A_TimeCollectionChange extends ProtoHandler {
	protected A_TimeCollectionChange(){}
	private A_TimeCollectionChange(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int groupId;
	private int setId;
	private L1TimeCollectionBuffType buffType;
	
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
			case 0x18:
				buffType = L1TimeCollectionBuffType.fromInt(readC());
				break;
			default:
				return;
			}
		}
	}
	
	boolean isValidation(){
		return groupId > 0 && buffType != null && setId > 0;
	}

	@Override
	protected void doWork() throws Exception {
		if (!Config.COLLECTION.TIME_COLLECTION_ACTIVE || _pc == null || _pc.isGhost()) {
			return;
		}
		parsePacket();
		if (!isValidation()) {
			return;
		}
		
		// 컬렉션 여부 검사
		L1TimeCollection obj				= L1TimeCollectionLoader.getData(groupId, setId);
		if (obj == null) {
			System.out.println(String.format(
					"[A_TimeCollectionChange] COLLECTION EMPTY : GROUP(%d), ID(%d), NAME(%s)", 
					groupId, setId, _pc.getName()));
			return;
		}
		
		// 핸들러 검사
		L1TimeCollectionHandler handler		= _pc.getTimeCollection();
		if (handler == null) {
			System.out.println(String.format(
					"[A_TimeCollectionChange] HANDLER_EMPTY : GROUP(%d), ID(%d), NAME(%s)", 
					groupId, setId, _pc.getName()));
			return;
		}
		
		// 버프 검사
		L1TimeCollectionUser user			= handler.getUser(setId);
		if (user == null || user.getBonusList() == null || user.getBuffTimer() == null || user.getBuffType().equals(buffType)) {
			return;
		}
		
		// 기존 옵션 제거
		user.doBonus(_pc, false);
		_pc.sendPackets(new S_TimeCollectionBuffNoti(user, L1TimeCollectionNotiType.REFRESH), true);
		
		// 새로운 옵션 부여
		user.setBuffType(buffType);
		handler.activeAblity(user);
		_pc.sendPackets(new S_TimeCollectionChangeBuff(user), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_TimeCollectionChange(data, client);
	}

}

