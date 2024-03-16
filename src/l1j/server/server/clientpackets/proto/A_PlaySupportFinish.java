package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconNotiType;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconDurationShowType;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.Config;
import java.lang.Math;

public class A_PlaySupportFinish extends ProtoHandler {
	protected A_PlaySupportFinish(){}
	private A_PlaySupportFinish(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (Config.PSS.PLAY_SUPPORT_TIME_LIMITED) {
			int remainingTime = _pc.getSkill().getSkillEffectTimeSec(L1SkillId.STATUS_PSS) + 1;	
			_pc.getSkill().killSkillEffectTimer(L1SkillId.STATUS_PSS);		
			
			if (remainingTime > 0) 
				_pc.sendPackets(new S_SpellBuffNoti(L1SkillId.STATUS_PSS, SkillIconNotiType.NEW, (int)(_pc.getAccount().getPSSTime() / 60), SkillIconDurationShowType.TYPE_EFF_MINUTE, 1));
			else
				_pc.sendPackets(new S_SpellBuffNoti(L1SkillId.STATUS_PSS, SkillIconNotiType.END, (int)(_pc.getAccount().getPSSTime() / 60), SkillIconDurationShowType.TYPE_EFF_MINUTE, 1));		
		}

		_pc.getConfig().finishPlaySupport();			
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PlaySupportFinish(data, client);
	}

}

