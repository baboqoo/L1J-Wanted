package l1j.server.server.clientpackets.proto;

import l1j.server.common.bin.EinhasadPointCommonBinLoader;
import l1j.server.common.bin.einhasadpoint.EinhasadPointStatInfoT;
import l1j.server.server.GameClient;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadPointEnchantStat;

public class A_EinhasadStatBonusCardOpen extends ProtoHandler {
	protected A_EinhasadStatBonusCardOpen(){}
	private A_EinhasadStatBonusCardOpen(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int _open_value;
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		EinhasadPointStatInfoT infoT = EinhasadPointCommonBinLoader.getInfo();
		if (infoT == null) {
			System.out.println(String.format(
					"[A_EinhasadStatBonusCardOpen] BIN_INFO_EMPTY : CHAR_NAME(%s)", 
					_pc.getName()));
			return;
		}
		
		int cur_stat = _pc.getEinTotalStat();
		if (cur_stat >= infoT.get_totalStatMax()) {
			return;
		}
		
		readP(1);// 0x08
		_open_value = readC();
		
		if (_open_value <= 0) {
			System.out.println(String.format(
					"[A_EinhasadStatBonusCardOpen] BONUS_VALUE_DENALS : CHAR_NAME(%s), VALUE(%d)", 
					_pc.getName(), _open_value));
			return;
		}
		
		/*if (_open_value != _pc.getEinBonusCardOpenValue()) {
			_open_value = _pc.getEinBonusCardOpenValue();
		}*/
		// 획득할 수치가 최대 수치를 벗어날경우 기본수치로 변경해준다.
		if (_pc.getEinTotalStat() + _open_value > infoT.get_totalStatMax()) {
			_open_value = 1;
		}
		
		_pc.setEinBonusCardOpenValue(0);// 처리 완료 되었다면 초기화
		_pc.addEinTotalStat(_open_value);
		_pc.setEinCurEnchantLevel(7);// 대성공시 확률 중가, 그외 초기화
		_pc.sendPackets(new S_EinhasadPointEnchantStat(_pc, 0, _open_value), true);
		LoggerInstance.getInstance().addEinState(_pc, true, _open_value, infoT.get_EnchantCost().get(cur_stat).get_point());
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EinhasadStatBonusCardOpen(data, client);
	}

}

