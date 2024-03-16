package l1j.server.server.model.skill.action;

import l1j.server.common.data.ChatType;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class DragonAnsig extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker instanceof L1NpcInstance) {
			attacker.broadcastPacket(new S_NpcChatPacket((L1NpcInstance)attacker, "$3717", ChatType.CHAT_NORMAL), true);
		} else {
			attacker.broadcastPacket(new S_SystemMessage("$3717"), true);
		}
		return cha.getCurrentHp();
	}

	@Override
	public void stop(L1Character cha) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new DragonAnsig().setValue(_skillId, _skill);
	}

}

