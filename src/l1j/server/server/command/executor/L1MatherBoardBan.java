package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.MatherBoardTable;
import l1j.server.server.datatables.MatherBoardTable.BanBoard;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.http.connector.HttpLoginSession;

public class L1MatherBoardBan implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1MatherBoardBan();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1MatherBoardBan() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st	= new StringTokenizer(arg);
			String type			= st.nextToken();
			
			switch(type){
			//case "추가":
			case "add":
				String addParam		= st.nextToken();
				L1PcInstance target	= L1World.getInstance().getPlayer(addParam);
				if (target == null || target.getNetConnection() == null) {
					//pc.sendPackets(new S_SystemMessage(String.format("%s 라는 케릭터는 월드상에 존재하지 않습니다.", addParam)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(47), addParam), true);
					return false;
				}
				HttpLoginSession session = target.getNetConnection().getLoginSession();
				if (session == null) {
					//pc.sendPackets(new S_SystemMessage(String.format("%s 라는 케릭터는 세션이 존재하지 않습니다.", addParam)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(58), addParam), true);
					return false;
				}
				if (StringUtil.isNullOrEmpty(session.getBoardId())) {
					//pc.sendPackets(new S_SystemMessage(String.format("%s 라는 케릭터는 마더보드 정보가 존재하지 않습니다.", addParam)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(67), addParam), true);
					return false;
				}
				MatherBoardTable.getInstance().insert(session.getBoardId(), session.getAccount());
				target.getNetConnection().kick();
				//pc.sendPackets(new S_SystemMessage(String.format("%s 캐릭터를 마더보드밴 처리하였습니다.", addParam)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(48), addParam), true);
				return true;
			//case "삭제":
			case "delete":
				String delParam		= st.nextToken();
				MatherBoardTable.getInstance().deleteAccount(delParam);
				//pc.sendPackets(new S_SystemMessage(String.format("%s 계정으로 등록된 마더보드밴을 삭제하였습니다.", delParam)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(68), delParam), true);
				return true;
			//case "리로드":
			case "reload":
				MatherBoardTable.getInstance().reload();
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("MatherBoardTable 리로드 완료"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(477), true), true);
				return true;
			default:
				String param		= st.nextToken();
				BanBoard ban = MatherBoardTable.getBanBoard(param);
				if (ban == null) {
					//pc.sendPackets(new S_SystemMessage(String.format("%s 계정으로 등록된 마더보드밴이 없습니다.", param)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(69), param), true);
					return false;
				}
				pc.sendPackets(new S_SystemMessage(ban.toString()), true);
				return true;
			}
			
		} catch (Exception exception) {
			//pc.sendPackets(new S_SystemMessage(String.format("%s [추가/삭제/리로드] [추가 시 캐릭터명/삭제 시 계정명] 라고 입력해 주세요.", cmdName)), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(63), cmdName), true);
			return false;
		}
	}
}


