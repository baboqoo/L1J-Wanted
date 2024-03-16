package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.common.bin.TimeCollectionCommonBinLoader;
import l1j.server.common.bin.timecollection.TimeCollection;
import l1j.server.common.data.TimeCollectionSetType;
import l1j.server.server.GameClient;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;
import l1j.server.server.serverpackets.inventory.S_TimeCollectionAdenaRefill;
import l1j.server.server.serverpackets.inventory.S_TimeCollectionSetDataNoti;
import l1j.server.server.utils.StringUtil;

public class A_TimeCollectionAdenaRefill extends ProtoHandler {
	protected A_TimeCollectionAdenaRefill(){}
	private A_TimeCollectionAdenaRefill(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int groupId;
	private int setId;
	private int count;
	
	void parse(){
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
				count = readC();
				break;
			default:
				return;
			}
		}
	}
	
	boolean isValidation(){
		return groupId > 0 && setId > 0 && count > 0;
	}

	@Override
	protected void doWork() throws Exception {
		if (!Config.COLLECTION.TIME_COLLECTION_ACTIVE || _pc == null || _pc.isGhost() || _pc.getTimeCollection() == null) {
			return;
		}
		parse();
		if (!isValidation()) {
			return;
		}
		
		// 컬렉션 여부 검사
		L1TimeCollection collection			= L1TimeCollectionLoader.getData(groupId, setId);
		if (collection == null) {
			System.out.println(String.format(
					"[A_TimeCollectionAdenaRefill] NOT_COLLECTION : GROUP(%d), SET_ID(%d), NAME(%s)", 
					groupId, setId, _pc.getName()));
			_pc.sendPackets(S_TimeCollectionAdenaRefill.DATA_ERROR);
			return;
		}
		
		// 리필 타입 검사
		if (collection.getSet().get_SetType() != TimeCollectionSetType.TC_ADENA_REFILL) {
			System.out.println(String.format(
					"[A_TimeCollectionAdenaRefill] NOT_ADENA_REFILL_TYPE : GROUP(%d), SET_ID(%d), COUNT(%d), NAME(%s)", 
					groupId, setId, count, _pc.getName()));
			_pc.sendPackets(S_TimeCollectionAdenaRefill.FAIL_CAN_NOT_REFILL_SET);
			return;
		}
		
		L1TimeCollectionUser user = _pc.getTimeCollection().getUser(setId);
		if (user == null) {
			System.out.println(String.format(
					"[A_TimeCollectionAdenaRefill] USER_OBJ_EMPTY : GROUP(%d), SET_ID(%d), COUNT(%d), NAME(%s)", 
					groupId, setId, count, _pc.getName()));
			_pc.sendPackets(S_TimeCollectionAdenaRefill.DATA_ERROR);
			return;
		}
		
		if (!user.isBuffActive()) {
			_pc.sendPackets(S_TimeCollectionAdenaRefill.FAIL_NOT_BUFF_STATE);
			return;
		}
		
		TimeCollection.ExtraTimeSectionT sectionT = TimeCollectionCommonBinLoader.getData().get_ExtraTimeSection();
		if (sectionT == null) {
			System.out.println(String.format(
					"[A_TimeCollectionAdenaRefill] EXTRA_SECTION_T_EMPTY : GROUP(%d), SET_ID(%d), COUNT(%d), NAME(%s)", 
					groupId, setId, count, _pc.getName()));
			_pc.sendPackets(S_TimeCollectionAdenaRefill.DATA_ERROR);
			return;
		}
		
		int extra_id = collection.getSet().get_ExtraTimeId();
		TimeCollection.ExtraTimeSectionT.ExtraTimeT timeT = sectionT.get_ExtraTime(extra_id);
		if (timeT == null) {
			System.out.println(String.format(
					"[A_TimeCollectionAdenaRefill] EXTRA_TIME_T_EMPTY : GROUP(%d), SET_ID(%d), COUNT(%d), EXTRA_ID(%d), NAME(%s)", 
					groupId, setId, count, extra_id, _pc.getName()));
			_pc.sendPackets(S_TimeCollectionAdenaRefill.DATA_ERROR);
			return;
		}
		
		TimeCollection.ExtraTimeSectionT.ExtraTimeT.ExtraTimeLevelT levelT = timeT.get_EnchantLevel(user.getSumEnchant());
		if (levelT == null) {
			System.out.println(String.format(
					"[A_TimeCollectionAdenaRefill] EXTRA_TIME_LEVEL_T_EMPTY : GROUP(%d), SET_ID(%d), COUNT(%d), TOTAL_ENCHANT(%d), NAME(%s)", 
					groupId, setId, count, user.getSumEnchant(), _pc.getName()));
			_pc.sendPackets(S_TimeCollectionAdenaRefill.FAIL_NOT_BUFF_STATE);
			return;
		}
		
		// 리필 횟수 조사
		if (levelT.get_Limit() > 0 && user.getRefillCount() + count > levelT.get_Limit()) {
			_pc.sendPackets(S_TimeCollectionAdenaRefill.FAIL_NOT_ENOUGH_REFILL_COUNT);
			return;
		}
		
		// 비용 소모
		if (levelT.get_Cost() > 0 && !_pc.getInventory().consumeItem(L1ItemId.ADENA, levelT.get_Cost() * count)) {
			_pc.sendPackets(S_TimeCollectionAdenaRefill.FAIL_NOT_ENOUGH_ADENA);
			return;
		}
		
		// 리필
		user.refill(_pc, count, StringUtil.get_time_string_to_long(timeT.get_ExtraTime()));
		_pc.sendPackets(new S_TimeCollectionSetDataNoti(_pc, user), true);
		_pc.sendPackets(new S_TimeCollectionAdenaRefill(
				S_TimeCollectionAdenaRefill.ADENA_REFILL_ACK_RESULT.TCAR_SUCCESS, 
				user.getRefillCount()),
				true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_TimeCollectionAdenaRefill(data, client);
	}

}

