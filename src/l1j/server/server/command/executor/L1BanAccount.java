package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.BanAccountTable;
import l1j.server.server.datatables.BanAccountTable.BanAccount;
import l1j.server.server.datatables.BanAccountTable.BanAccountReason;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.DelayClose;
import l1j.server.server.utils.StringUtil;

public class L1BanAccount implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1BanAccount();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1BanAccount() {}
	
	static S_SystemMessage MESSAGE;

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
				if(target == null || target.getNetConnection() == null){
					//pc.sendPackets(new S_SystemMessage(String.format("%s 라는 케릭터는 월드상에 존재하지 않습니다.", addParam)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(47), addParam), true);
					return false;
				}
				
				int reasonCode		= Integer.parseInt(st.nextToken(), 10);
				BanAccount ban = BanAccountTable.getInstance().insert(target.getAccountName(), BanAccountReason.getReason(reasonCode));
				if (ban == null) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("등록에 실패하였습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(209), true), true);
					return false;
				}
				//pc.sendPackets(new S_SystemMessage(String.format("%s 캐릭터를 마더보드밴 처리하였습니다.", addParam)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(48), addParam), true);
				//target.sendPackets(new S_SystemMessage(String.format( "안녕하세요 운영자입니다.\r\n고객님께선\r\n'%s' 사유로\r\n%s까지 이용제한 되었습니다.\r\n잠시 후 클라이언트가 종료됩니다.", ban.getReason().getReason(), StringUtil.getFormatDate(ban.getLimitTime()) )), true);
				target.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(49), ban.getReason().getReason(), StringUtil.getFormatDate(ban.getLimitTime())), true);
				GeneralThreadPool.getInstance().schedule(new DelayClose(target.getNetConnection()), 1500L);
				return true;
			//case "삭제":
			case "delete":
				String delParam		= st.nextToken();
				if (!BanAccountTable.getInstance().delete(delParam)) {
//AUTO SRM: 					pc.sendPackets(new S_SystemMessage("삭제에 실패하였습니다."), true); // CHECKED OK
					pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(214), true), true);
					return false;
				}
				//pc.sendPackets(new S_SystemMessage(String.format("%s 계정으로 등록된 계정밴을 삭제하였습니다.", delParam)), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(50), delParam), true);
				return true;
			//case "리로드":
			case "reload":
				BanAccountTable.reload();
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("BanAccountTable 리로드 완료"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(216), true), true);
				break;
			default:
				String param		= st.nextToken();
				BanAccount banAccount = BanAccountTable.getBan(param);
				if (banAccount == null) {
					//pc.sendPackets(new S_SystemMessage(String.format("%s 계정으로 등록된 계정밴이 없습니다.", param)), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(51), param), true);
					return false;
				}
				pc.sendPackets(new S_SystemMessage(banAccount.toString()), true);
				return true;
			}
			return false;
		} catch (Exception exception) {
			if (MESSAGE == null) {
				StringBuilder sb = new StringBuilder();
				//sb.append(cmdName).append(" [추가/삭제/리로드] [추가 시 캐릭터명/삭제 시 계정명] [추가 시 사유코드] 라고 입력해 주세요.\r\n사유코드\r\n");
				sb.append(cmdName).append(" Please enter [Add/Delete/Reload] [Character name (when adding)/Account name (when deleting)] [Reason code (when adding)].\r\nReason code\r\n" );
				for (BanAccountReason reason : BanAccountReason.getAllReason()) {
					sb.append(reason.getCode()).append(StringUtil.PeriodString).append(reason.getReason()).append(StringUtil.LineString);
				}
				MESSAGE = new S_SystemMessage(sb.toString());
			}
			pc.sendPackets(MESSAGE);
			return false;
		}
	}
	
}


