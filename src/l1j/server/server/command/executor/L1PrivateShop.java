package l1j.server.server.command.executor;

import l1j.server.server.GameClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Unknown2;
import l1j.server.server.serverpackets.message.S_SystemMessage;
//import manager.Manager;  // MANAGER DISABLED

public class L1PrivateShop implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1PrivateShop();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1PrivateShop() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (!pc.isPrivateShop()) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("개인상점 상태에서 사용이 가능합니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(549), true), true);
				return false;
			}
//			for(L1PcInstance target : L1World.getInstance().getAllPlayers3()){
//				if(target.getId() != pc.getId() && target.getAccountName().toLowerCase().equals(pc.getAccountName().toLowerCase()) && target.isPrivateShop() ){
//					pc.sendPackets(new S_SystemMessage("이미 당신의 보조 캐릭터가 무인상점 상태입니다."), true);
//					return;
//				} 
//			}
			//manager.LogServerAppend("종료", pc, pc.getNetConnection().getIp(), -1);
			//Manager.getInstance().LogLogOutAppend(pc.getName(), pc.getNetConnection().getHostname()); // MANAGER DISABLED
			GameClient client = pc.getNetConnection();
			pc.setNetConnection(null);
			try { 
				pc.save();
				pc.saveInventory();
			} catch(Exception e) {                    		
			}
			client.setActiveChar(null);
			client.setLoginAvailable();
			client.setCharReStart(true);
			client.sendPacket(new S_Unknown2(1)); // 리스버튼을 위한 구조변경 // Episode U
			//client.close(); // 리마스터 무인상점 접속끓어지게
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}


