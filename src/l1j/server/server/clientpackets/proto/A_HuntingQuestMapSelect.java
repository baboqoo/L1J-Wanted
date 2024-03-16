package l1j.server.server.clientpackets.proto;

import java.util.Collection;

import l1j.server.Config;
import l1j.server.GameSystem.huntingquest.HuntingQuestObject;
import l1j.server.GameSystem.huntingquest.HuntingQuestTable;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUser;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUserTable;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUserTemp;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.huntingquest.S_HuntingQuestMapSelectFail;
import l1j.server.server.serverpackets.huntingquest.S_HuntingQuestMapList;

public class A_HuntingQuestMapSelect extends ProtoHandler {
	protected A_HuntingQuestMapSelect(){}
	private A_HuntingQuestMapSelect(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		int map_number		= readBit();
		readP(1);// 0x10
		int location_desc	= readBit();
		readP(1);// 0x18
		boolean is_select	= readC() == 1;
		int quest_id		= 0;
		if (!is_select) {// 삭제
			readP(1);// 0x20
			quest_id		= readBit();
		}
		HuntingQuestUserTable hunt					= HuntingQuestUserTable.getInstance();
		HuntingQuestUser user						= _pc.getHuntingQuest();
		Collection<HuntingQuestUserTemp> templist	= user.getInfo().values();
		if (is_select) {// 등록
			if (templist != null && templist.size() >= Config.QUEST.HUNTING_QUEST_REGIST_COUNT) {// 최대 등록 가능 수
				return;
			}
			if (user != null && user.getTemp(map_number, location_desc) != null) {
				return;// 이미 추가되어 있는 사냥터인지 체크
			}
			HuntingQuestObject huntObj	= HuntingQuestTable.getHuntInfo(map_number, location_desc);
			if (huntObj == null) {
				_pc.sendPackets(S_HuntingQuestMapSelectFail.HUNTING_QUEST_MAP_SELECT_FAIL);// 등록 불가 사냥터
				return;
			}
			if (!huntObj.isUse()) {
				_pc.sendPackets(S_HuntingQuestMapSelectFail.HUNTING_QUEST_MAP_SELECT_FAIL);// 등록 불가 사냥터
				return;
			}
			HuntingQuestUserTemp temp	= new HuntingQuestUserTemp(hunt.nextId(), _pc.getId(), map_number, location_desc, huntObj.getQuestId(), 0, false);
			user.add(temp.getQuestId(), temp);
		} else {// 삭제
			if (templist == null || templist.isEmpty()) {
				return;
			}
			HuntingQuestUserTemp temp	= user.getTempInfo(quest_id);
			if (temp == null || temp.isComplete()) {
				return;
			}
			user.remove(temp.getQuestId());
		}
		
		_pc.sendPackets(new S_HuntingQuestMapList(user.getInfo().values()), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_HuntingQuestMapSelect(data, client);
	}

}

