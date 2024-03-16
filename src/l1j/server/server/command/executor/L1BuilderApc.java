package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.command.S_BuilderAPCHpMpInfo;
import l1j.server.server.serverpackets.command.S_BuilderAPCList;
import l1j.server.server.serverpackets.command.S_BuilderAPCPartyInfo;
import l1j.server.server.serverpackets.command.S_UserEquipInfo;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1BuilderApc implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1BuilderApc();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1BuilderApc() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st	= new StringTokenizer(arg);
			String type			= st.nextToken();
			
			switch(type) {
			//case "리스트": 
			case "list":
			{
				pc.sendPackets(new S_BuilderAPCList(), true);
			}
				return true;
			//case "피엠": 
			case "pm": 
			{
				String targetName	= st.nextToken();
				L1PcInstance target	= L1World.getInstance().getPlayer(targetName);
				if (target == null) {
					pc.sendPackets(new S_ServerMessage(73, targetName), true); // \f1%0은 게임을 하고 있지 않습니다.
					return false;
				}
				pc.sendPackets(new S_BuilderAPCHpMpInfo(target), true);
			}
				return true;
			//case "장비": 
			case "equip": 
			{
				String targetName	= st.nextToken();
				L1PcInstance target	= L1World.getInstance().getPlayer(targetName);
				if (target == null) {
					pc.sendPackets(new S_ServerMessage(73, targetName), true); // \f1%0은 게임을 하고 있지 않습니다.
					return false;
				}
				pc.sendPackets(new S_UserEquipInfo(target), true);
			}
				return true;
			//case "파티": 
			case "party":
			{
				String targetName	= st.nextToken();
				L1PcInstance target	= L1World.getInstance().getPlayer(targetName);
				if (target == null) {
					pc.sendPackets(new S_ServerMessage(73, targetName), true); // \f1%0은 게임을 하고 있지 않습니다.
					return false;
				}
				if (!target.isInParty()) {
					//pc.sendPackets(new S_SystemMessage(String.format("%s 는 파티 상태가 아닙니다.", targetName)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(52), targetName), true);
					return false;
				}
				
				pc.sendPackets(new S_BuilderAPCPartyInfo(target.getParty()), true);
			}
				return true;
			default:
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage(cmdName + " [리스트/피엠/장비/파티] [피엠/장비/파티:캐릭터명]라고 입력해 주세요. "), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(233), true), true);
				return false;
			}
		} catch (Exception exception) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [리스트/피엠/장비/파티] [피엠/장비/파티:캐릭터명]라고 입력해 주세요. "), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(233), true), true);
			return false;
		}
	}
}


