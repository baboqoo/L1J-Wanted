package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.C_RestartComplete;
import l1j.server.server.datatables.BuddyTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1CharacterNameDenal implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1CharacterNameDenal();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1CharacterNameDenal() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st	= new StringTokenizer(arg);
			String targetName	= st.nextToken();
			L1PcInstance target	= L1World.getInstance().getPlayer(targetName);
			if (target == null) {
				target = L1PcInstance.load(targetName);
			}
			if (target == null) {
				//pc.sendPackets(new S_SystemMessage(String.format("'%s' 라는 캐릭터는 존재하지 않습니다.", targetName)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(53), targetName), true);
				return false;
			}
			
			String destinationName = "_L_" + target.getName();
			
			CharacterTable.getInstance().CharacterNameChange("UPDATE characters SET char_name='" + destinationName + "' WHERE char_name='" + target.getName() + "'");
			target.save();// 저장
			BuddyTable.getInstance().removeBuddy(target.getId(), target.getName());
			LetterTable.getInstance().removeLetter(target);
			
			//pc.sendPackets(new S_SystemMessage(String.format("'%s' 의 캐릭터명을 변경 요청하였습니다.", targetName)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(54), targetName), true);
			
			if (target.getOnlineStatus() == 1) {
//AUTO SRM: 				target.sendPackets(new S_SystemMessage("불건전한 캐릭터명입니다.\r\n잠시후 캐릭터명 변경을 위해 리스타트가 진행됩니다."), true); // CHECKED OK
				target.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(273), true), true);
				GeneralThreadPool.getInstance().schedule(new OnlineUserRestart(target), 3000L);
			}
			st = null;
			return true;
		} catch (Exception exception) {
			//pc.sendPackets(new S_SystemMessage(String.format("%s [캐릭터명] 라고 입력해 주세요.", cmdName)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(55), cmdName), true);
			return false;
		}
	}
	
	class OnlineUserRestart implements Runnable {
		L1PcInstance target;
		public OnlineUserRestart(L1PcInstance target) {
			this.target = target;
		}
		@Override
		public void run() {
			if (target == null) {
				return;
			}
			try {
				CharacterTable.getInstance().removeContainNameList(target.getName());
				target.getAccount().setCharSlotChange(true);
				C_RestartComplete.restartProcess(target);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}


