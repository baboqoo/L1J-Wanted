package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.inventory.S_SetToggleInfo;
import l1j.server.server.serverpackets.inventory.eToggleInfoType;

public class A_SetToggleIinfo extends ProtoHandler {
	protected A_SetToggleIinfo(){}
	private A_SetToggleIinfo(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private eToggleInfoType _toggle_info_type;
	private boolean _is_enable;

	@Override
	public void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		_toggle_info_type = eToggleInfoType.fromInt(readC());
		readP(1);// 0x10
		_is_enable = readBool();
		if (_toggle_info_type == null) {
			return;
		}
		switch (_toggle_info_type) {
		case TOGGLE_INFO_NONE_TYPE:
			_pc.sendPackets(new S_SetToggleInfo(S_SetToggleInfo.eSetToggleResult.CAN_NOT_FIND_TOGGLE_INFO, _toggle_info_type, _is_enable, false), true);
			break;
		case TOGGLE_INFO_FAITH_OF_HALPAH_USABLE_TYPE:
			L1ItemInstance item = _pc.getInventory().getEquippedArmor();// 착용중인 갑옷
			if (item != null && item.getItemId() >= 23000 && item.getItemId() <= 23002) {
				_pc.sendPackets(_is_enable ? S_SetToggleInfo.ENABLE_FAITH_OF_HALPAH : S_SetToggleInfo.DISABLE_FAITH_OF_HALPAH);
				_pc.getConfig()._halpasLoyaltyEnable = _is_enable;
			} else {
				_pc.sendPackets(S_SetToggleInfo.NOT_ENOUGH_FAITH_OF_HALPAH);
			}
			break;
		default:
			_pc.sendPackets(new S_SetToggleInfo(S_SetToggleInfo.eSetToggleResult.SUCCESS, _toggle_info_type, _is_enable, false), true);
			break;
		}
	}

	@Override
	public ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_SetToggleIinfo(data, client);
	}

}

