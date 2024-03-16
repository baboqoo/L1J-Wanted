package l1j.server.QuestSystem.Compensator.Element;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconNotiType;

public class BuffCompensator implements CompensatorElement{
	private int 	_buffId;
	private int 	_time;
	private int		_iconId;
	private int		_msgId;
	
	public BuffCompensator(int buffId, int time, int iconId, int msgId){
		_buffId = buffId;
		_time	= time;
		_iconId = iconId;
		_msgId	= msgId;
	}
	
	@Override
	public void compensate(L1PcInstance pc){
		if (_buffId <= 0) {
			return;
		}
		if (pc.getSkill().hasSkillEffect(_buffId)) {
			pc.getSkill().removeSkillEffect(_buffId);
		}
		pc.getSkill().setSkillEffect(_buffId, _time * 1000);
		if (_iconId > 0 && _msgId > 0) {
			pc.sendPackets(new S_SpellBuffNoti(_iconId, SkillIconNotiType.NEW, _msgId, _time), true);
		}
	}
}

