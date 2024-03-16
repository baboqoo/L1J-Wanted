package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.inventory.S_GetToggleInfo;
import l1j.server.server.serverpackets.inventory.eToggleInfoType;

public class A_GetToggleIinfo extends ProtoHandler {
	protected A_GetToggleIinfo(){}
	private A_GetToggleIinfo(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private eToggleInfoType _toggle_info_type;

	@Override
	public void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		_toggle_info_type	= eToggleInfoType.fromInt(readC());
		if (_toggle_info_type == null) {
			return;
		}
		switch (_toggle_info_type) {
		case TOGGLE_INFO_NONE_TYPE:
			_pc.sendPackets(new S_GetToggleInfo(S_GetToggleInfo.eGetToggleResult.CAN_NOT_FIND_TOGGLE_INFO, _toggle_info_type, false, false));
			break;
		default:
			_pc.sendPackets(new S_GetToggleInfo(S_GetToggleInfo.eGetToggleResult.SUCCESS, _toggle_info_type, false, false));
			break;
		}
	}

	@Override
	public ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_GetToggleIinfo(data, client);
	}

}

