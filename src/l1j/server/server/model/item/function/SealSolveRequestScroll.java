package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.StringUtil;

public class SealSolveRequestScroll extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public SealSolveRequestScroll(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (isValidQuiz(pc)) {
				pc.sendPackets(L1SystemMessage.SECURITY_NOT_SETTING);
				return;
			}
			if (!pc.isQuizValidated()) {
				pc.sendPackets(L1SystemMessage.SECURITY_SOLVE_SETTING);
				return;
			}
			if (pc.getInventory().checkItem(50021) || pc.getInventory().checkItem(50022)) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("이미 봉인해제 주문서를 보유하고 있습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1095), true), true);
				return;
			}
			if (pc.getInventory().getSize() > L1PcInventory.MAX_SIZE - 1 || pc.getInventory().getWeightPercent() >= 100) {
				pc.sendPackets(L1ServerMessage.sm82);// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				return;
			}
			if (pc.getInventory().consumeItem(700021, 1)) {
				L1ItemInstance item = pc.getInventory().storeItem(50022, 1);
				pc.sendPackets(new S_ServerMessage(403, item.getItem().getDesc()), true);
			}
		}
	}
	
	private boolean isValidQuiz(L1PcInstance pc) {
		return StringUtil.isNullOrEmpty(pc.getAccount().getQuiz());
	}
}


