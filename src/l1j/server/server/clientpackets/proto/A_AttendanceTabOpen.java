package l1j.server.server.clientpackets.proto;

import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.GameSystem.attendance.bean.AttendanceAccount;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.attendance.S_AttenDanceTabOpen;

public class A_AttendanceTabOpen extends ProtoHandler {
	protected A_AttendanceTabOpen(){}
	private A_AttendanceTabOpen(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null) {
			return;
		}
		readP(1);// 0x08
		AttendanceGroupType type = AttendanceGroupType.fromInt(readC());
		if (type == null || !type.isTab() || !AttendanceGroupType.isUse(type)) {
			return;
		}
		readP(1);// 0x10
		int seasonId = readC();
		if (seasonId != type.getSeasonId()) {
			return;
		}
		
		AttendanceAccount attendAccount = _pc.getAccount().getAttendance();
		if (attendAccount.isGroupOpen(type)) {// 이미 개방된 탭
			return;
		}
		
		if (!_pc.getInventory().consumeItem(type.getTabOpenItemId(), type.getTabOpenItemCount())) {// 재료 소모 여부
			_pc.sendPackets(new S_AttenDanceTabOpen(null, type, null, false, false), true);
			return;
		}
		
		attendAccount.setGroupOpen(type, true);// 개방
		byte[] attendBytes	= attendAccount.getGroupData(type);
		_pc.sendPackets(new S_AttenDanceTabOpen(attendAccount, type, attendBytes, true, false), true);
		_pc.sendPackets(new S_AttenDanceTabOpen(attendAccount, type, attendBytes, true, true), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_AttendanceTabOpen(data, client);
	}

}

