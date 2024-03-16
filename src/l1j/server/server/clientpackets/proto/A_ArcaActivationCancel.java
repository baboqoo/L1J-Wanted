package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.arca.L1Arca;
import l1j.server.GameSystem.arca.L1ArcaActivation;
import l1j.server.GameSystem.arca.L1ArcaRemain;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ArcaActivationInfo;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class A_ArcaActivationCancel extends ProtoHandler {
	protected A_ArcaActivationCancel(){}
	private A_ArcaActivationCancel(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int user_id;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (_pc.getSkill().hasSkillEffect(L1SkillId.ARCA_CANCEL_DELAY)) {
			_pc.sendPackets(L1ServerMessage.sm1729);
			return;
		}
		readP(1);// 0x08
		user_id	= readBit();
		if (user_id <= 0) {
			_pc.getSkill().setSkillEffect(L1SkillId.ARCA_CANCEL_DELAY, 10800000);
			return;
		}
		cancel();
	}
	
	void cancel(){
		L1Arca arca = _pc.getAccount().getArca();
		L1ArcaActivation active = arca.getActivations().get(user_id);
		if (active == null || active.getRemain().isEmpty()) {// 상태 검증
			_pc.getSkill().setSkillEffect(L1SkillId.ARCA_CANCEL_DELAY, 10800000);
			return;
		}
		L1ArcaRemain remain	= active.getRemain().poll();// 예약을 꺼낸다
		if (remain == null) {
			_pc.getSkill().setSkillEffect(L1SkillId.ARCA_CANCEL_DELAY, 10800000);
			return;
		}
		if (_pc.getInventory().checkAddItem(ItemTable.getInstance().getTemplate(remain.getUseItemId()), 1) != L1Inventory.OK) return;
		L1ItemInstance item	= _pc.getInventory().storeItem(remain.getUseItemId(), 1);// 아이템 생성
		_pc.sendPackets(new S_ServerMessage(403, String.format("%s (1)", item.getDesc())), true);
		_pc.sendPackets(new S_ArcaActivationInfo(_pc.getAccount(), 0), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_ArcaActivationCancel(data, client);
	}

}

