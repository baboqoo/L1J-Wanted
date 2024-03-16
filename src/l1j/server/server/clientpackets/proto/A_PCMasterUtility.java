package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.freebuffshield.FreeBuffShieldHandler;
import l1j.server.common.bin.pcmaster.PCMasterInfoForClient;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.model.skill.L1SkillUseType;
import l1j.server.server.utils.StringUtil;

public class A_PCMasterUtility extends ProtoHandler {
	protected A_PCMasterUtility(){}
	private A_PCMasterUtility(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || !_pc.isPCCafe() || _pc.isStop() || _pc.isNotEnablePc()) {
			return;
		}
		FreeBuffShieldHandler handler = _pc.getConfig().get_free_buff_shield();
		if (handler == null) {
			System.out.println(String.format("[A_PCMasterUtility] HANDLER_NOT_FOUND : CHAR_NAME(%s)", _pc.getName()));
			return;
		}

		readP(1);// 0x0a
		int length = readC();
		String action = readS(length);
		if (StringUtil.isNullOrEmpty(action)) {
			return;
		}
		PCMasterInfoForClient.UtilityT utility = handler.get_pcmaster_bin().get_utilities().get(action);
		if (utility == null) {
			System.out.println(String.format("[A_PCMasterUtility] PC_MASTER_BIN_UTILITY_EMPTY : CHAR_NAME(%s), ACTION(%s)", _pc.getName(), action));
			return;
		}
		int cost = utility.get_cost();
		if (cost > 0 && !_pc.getInventory().consumeItem(L1ItemId.PIXIE_GOLD_FEATHER, cost)) {
			_pc.sendPackets(L1ServerMessage.sm4487);// 픽시의 금빛 깃털이 부족하여 사용할 수 없습니다.
			return;
		}
		
		switch (utility.get_action()) {
		case "a_giant":
			enterDungeon(32769, 32878, 624);
			break;
		case "a_soul_center":
			if (_pc.getAlignment() < 0) {
				_pc.sendPackets(L1ServerMessage.sm8334);// 카오틱 상태에서는 입장이 불가능합니다.
				return;
			}
			enterDungeon(32798, 32917, 430);
			break;
		case "buff_enchant":
			buffEnchant();
			break;
		case "buff_acceleration":
			buffAcceleration();
			break;
		default:
			System.out.println(String.format(
					"[A_PCMasterUtility] UNDEFINED_ACTION : ACTION(%s), CHAR_NAME(%s)",
					utility.get_action(), _pc.getName()));
			break;
		}
	}
	
	void enterDungeon(int telX, int telY, int telMapId) {
		if ((_pc.getMapId() == 99 || _pc.getMapId() == 6202 || _pc.getMapId() == 5490 || _pc.getMapId() == 5166 
				|| _pc.isNotTeleport() || _client.isInterServer() || !_pc.getMap().isEscapable()) 
				&& !_pc.isGm()) {
			_pc.sendPackets(L1ServerMessage.sm647);// 이곳에서는 텔레포트를 할 수 없습니다.
			return;
		}
		_pc.getDungoenTimer().enter(telX, telY, telMapId, false, 0, 0);
	}
	
	void buffEnchant() {
		int[] skill_array = L1SkillInfo.PCCAFE_BUFF_ARRAY;
		int time_secs = 7200;
		L1SkillUse l1skilluse = new L1SkillUse(true);
		for (int i = 0; i < skill_array.length; i++) {
			l1skilluse.handleCommands(_pc, skill_array[i], _pc.getId(), _pc.getX(), _pc.getY(), time_secs, L1SkillUseType.GMBUFF);
		}
		l1skilluse = null;
	}
	
	void buffAcceleration() {
		if (_pc.getHasteItemEquipped() == 0) {
			L1BuffUtil.haste(_pc, 3600000);
		}
		if (_pc.getDrunkenItemEquipped() == 0) {
			L1BuffUtil.drunken(_pc, 3600000);
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PCMasterUtility(data, client);
	}

}

