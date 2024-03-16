package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.PlaySupportTable;
import l1j.server.server.serverpackets.playsupport.S_StartPlaySupport;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconNotiType;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconDurationShowType;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.Config;

public class A_PlaySupportStart extends ProtoHandler {
	protected A_PlaySupportStart(){}
	private A_PlaySupportStart(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.getConfig().isPlaySupport()) {
			return;
		}
		if (!Config.PSS.PLAY_SUPPORT_ACTIVE && !_pc.isGm()) {
			_pc.sendPackets(L1SystemMessage.PLAY_SUPPORT_FAIL);
			return;
		}
		if (Config.PSS.PLAY_SUPPORT_AUTH_ITEM_ID > 0 && !_pc.getInventory().checkItem(Config.PSS.PLAY_SUPPORT_AUTH_ITEM_ID) && !_pc.isGm()) {
			_pc.sendPackets(S_StartPlaySupport.TIME_EXPIRE);
			_pc.sendPackets(L1SystemMessage.PLAY_SUPPORT_AUTH_ITEM_FAIL);
			return;
		}
		if (Config.PSS.PLAY_SUPPORT_TIME_LIMITED && _pc.getAccount().getPSSTime() <= 0  && !_pc.isGm()) {
			_pc.sendPackets(S_StartPlaySupport.TIME_EXPIRE);
			return;
		}
	
		readP(3);
		int type = readC(); // 1: Support, 2: Arround, 3: All // 1:보조, 2:주변, 3:전체

		if (type == 3 && !Config.PSS.PLAY_SUPPORT_ALL_ACTIVE && !_pc.isGm()) {
			_pc.getConfig().finishPlaySupport();
			_pc.sendPackets(L1SystemMessage.PLAY_SUPPORT_ALL_DISABLED);
			return;
		}
		
		if (type == 3 && _pc.getLevel() < 65 && !_pc.isGm()) {
			_pc.sendPackets(S_StartPlaySupport.INVALID_LEVEL);
			return;
		}
		
		if (PlaySupportTable.isSupportMap(type, _pc.getMap().getBaseMapId())) {
			if (Config.PSS.PLAY_SUPPORT_TIME_LIMITED) {
				_pc.sendPackets(new S_SpellBuffNoti(L1SkillId.STATUS_PSS, SkillIconNotiType.NEW, _pc.getAccount().getPSSTime(), SkillIconDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC, 1));
				_pc.getSkill().setSkillEffect(L1SkillId.STATUS_PSS, _pc.getAccount().getPSSTime() * 1000);
			}

			_pc.getConfig().setPlaySupport(true);
			_pc.getConfig().setPlaySupportType(type);
			_pc.sendPackets(S_StartPlaySupport.VALID);
		} else {
			_pc.sendPackets(S_StartPlaySupport.INVALID_MAP);
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PlaySupportStart(data, client);
	}

}

