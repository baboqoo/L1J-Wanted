package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.einhasadfaith.EinhasadFaithHandler;
import l1j.server.GameSystem.einhasadfaith.bean.EinhasadFaithInfo;
import l1j.server.common.bin.EinhasadPointFaithCommonBinLoader;
import l1j.server.common.bin.einhasadpoint.EinhasadPointFaithT;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadFaithEnableIndex;

public class A_EinhasadFaithEnableIndex extends ProtoHandler {
	protected A_EinhasadFaithEnableIndex(){}
	private A_EinhasadFaithEnableIndex(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _indexId;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		_indexId = readC();
		EinhasadPointFaithT.GroupListT.GroupT groupT		= EinhasadPointFaithCommonBinLoader.getGroupT(_indexId);
		EinhasadPointFaithT.GroupListT.GroupT.IndexT indexT = EinhasadPointFaithCommonBinLoader.getIndexT(_indexId);
		if (indexT == null) {
			System.out.println(String.format(
					"[A_EinhasadFaithEnableIndex] INDEX_T_EMPTY : INDEX_ID(%d), CHAR_NAME(%s)", 
					_indexId, _pc.getName()));
			return;
		}
		
		EinhasadFaithHandler handler = _pc.getEinhasadFaith();
		EinhasadFaithInfo indexVal = handler.getInfo(groupT.get_GroupId(), _indexId);
		if (indexVal == null) {
			System.out.println(String.format(
					"[A_EinhasadFaithEnableIndex] INDEX_VAL_EMPTY : INDEX_ID(%d), CHAR_NAME(%s)", 
					_indexId, _pc.getName()));
			return;
		}
		if (indexVal.getSpellId() != indexT.get_spellId()) {
			System.out.println(String.format(
					"[A_EinhasadFaithEnableIndex] NOT_INDEX_SPELL : INFO_SPELL_ID(%d), BIN_SPELL_ID(%d), INDEX_ID(%d), CHAR_NAME(%s)", 
					indexVal.getSpellId(), indexT.get_spellId(), _indexId, _pc.getName()));
			return;
		}
		
		EinhasadFaithInfo groupVal = handler.getInfo(groupT.get_GroupId(), 0);
		if (groupVal == null) {
			System.out.println(String.format(
					"[A_EinhasadFaithEnableIndex] GROUP_VAL_EMPTY : GROUP_ID(%d), CHAR_NAME(%s)", 
					groupT.get_GroupId(), _pc.getName()));
			return;
		}
		if (groupVal.getSpellId() != groupT.get_spellId()) {
			System.out.println(String.format(
					"[A_EinhasadFaithEnableIndex] NOT_GROUP_SPELL : INFO_SPELL_ID(%d), BIN_SPELL_ID(%d), CHAR_NAME(%s)", 
					groupVal.getSpellId(), groupT.get_spellId(), _pc.getName()));
			return;
		}
		
		// 비용 소모
		if (_pc.getEinPoint() < indexT.get_cost()) {
			_pc.sendPackets(L1ServerMessage.sm9310);
			_pc.sendPackets(S_EinhasadFaithEnableIndex.FAIL_WRONG_REQUEST);
			return;
		}
		_pc.addEinPoint(-indexT.get_cost());
		
		// 버프 활성화
		handler.enable(groupVal, indexVal, groupT, indexT);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EinhasadFaithEnableIndex(data, client);
	}
	
}

