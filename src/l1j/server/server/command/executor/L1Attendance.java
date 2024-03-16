package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.GameSystem.attendance.AttendanceTable;
import l1j.server.GameSystem.attendance.AttendanceGroupType;
import l1j.server.GameSystem.attendance.bean.AttendanceAccount;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1Attendance implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Attendance();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Attendance() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {

			StringTokenizer str = new StringTokenizer(arg);
			String targetName	= str.nextToken();
			AttendanceGroupType type	= AttendanceGroupType.fromInt(Integer.parseInt(str.nextToken()));
			if (type == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("존재하지 않는 출석체크 그룹입니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(187), true), true);
			}
			int modify_number	= Integer.parseInt(str.nextToken());
			int modify_result	= Integer.parseInt(str.nextToken());
			
			AttendanceAccount account = null;
			//if (targetName.equalsIgnoreCase("전체")) {
			if (targetName.equalsIgnoreCase("all")) {
				for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
					if (target == null || target.isPrivateShop() || target.isAutoClanjoin() || target.noPlayerCK) {
						continue;
					}
					
					account = target.getAccount().getAttendance();
					if (modify_number == 0) {
						byte[] reset = new byte[AttendanceTable.getGroupItemSize(type)];
						account.setGroupData(type, reset);
						account.getRewardHisoty().remove(type);
					} else {
						account.getGroupData(type)[modify_number - 1] = (byte) modify_result;
					}
				}
			} else {
				L1PcInstance target = L1World.getInstance().getPlayer(targetName);
				if (target == null){
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("월드에 존재하지 않는 유저입니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(156), true), true);
					return false;
				}
				account = target.getAccount().getAttendance();
				if (modify_number == 0) {
					byte[] reset = new byte[AttendanceTable.getGroupItemSize(type)];
					account.setGroupData(type, reset);
					account.getRewardHisoty().remove(type);
				} else {
					account.getGroupData(type)[modify_number - 1] = (byte) modify_result;
				}
			}
			
			if (modify_number == 0) {
				//pc.sendPackets(new S_SystemMessage(String.format("%s의 %s 출석정보가 수정되었습니다.", targetName, type.name())), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(45), targetName, type.name()), true);
			} else {
				//pc.sendPackets(new S_SystemMessage(String.format("%s의 %s [%d]일자의 출석정보가 수정되었습니다.", targetName, type.name(), modify_number)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(46), targetName, type.name(), String.valueOf(modify_number)), true);
			}
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage((new StringBuilder()).append(".출석수정 [유저이름or전체] [그룹번호] [일자(0:전체)] [0,1,2]로 입력해 주세요. ").toString()), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(192), true), true);
			return false;
		}
	}
}


