package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.freebuffshield.GoldenBuffInfo;
import l1j.server.GameSystem.freebuffshield.FreeBuffShieldHandler;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.system.S_PCMasterGoldenBuffUpdateNoti;

public class A_PCMasterGoldenBuffSwitchType extends ProtoHandler {
	protected A_PCMasterGoldenBuffSwitchType(){}
	private A_PCMasterGoldenBuffSwitchType(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _index;

	void parse() {
		readP(1);// 0x08
		_index = readC();
	}
	
	boolean isValidation() {
		return _index >= 0;
	}
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || !_pc.isPCCafe()) {
			return;
		}
		FreeBuffShieldHandler handler = _pc.getConfig().get_free_buff_shield();
		if (handler == null) {
			System.out.println(String.format(
					"[A_PCMasterGoldenBuffSwitchType] HANDLER_NOT_FOUND : CHAR_NAME(%s)", 
					_pc.getName()));
			return;
		}
		parse();
		if (!isValidation()) {
			System.out.println(String.format(
					"[A_PCMasterGoldenBuffSwitchType] VALIDATION_FAIL : CHAR_NAME(%s), INDEX(%d)", 
					_pc.getName(), _index));
			return;
		}
		
		GoldenBuffInfo buffInfo = handler.get_golden_buff_info(_index);
		if (buffInfo == null) {
			System.out.println(String.format(
					"[A_PCMasterGoldenBuffSwitchType] BUFF_INFO_EMPTY : CHAR_NAME(%s), INDEX(%d)", 
					_pc.getName(), _index));
			return;
		}
		
		int type				= buffInfo.getType();
		int type_size			= buffInfo.getBuffBonusT().get_buff_category().size();
		buffInfo.setType(type >= type_size ? GoldenBuffInfo.DEFAULT_TYPE : ++type);
		_pc.sendPackets(new S_PCMasterGoldenBuffUpdateNoti(
				handler.get_golden_buff_infos(), 
				S_PCMasterGoldenBuffUpdateNoti.eUpdateReason.SWITCH_TYPE_ACK), 
				true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PCMasterGoldenBuffSwitchType(data, client);
	}

}

