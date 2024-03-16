package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class L1LetterCommand implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1LetterCommand();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1LetterCommand() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try{
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			int letter_id = Integer.parseInt(st.nextToken());

			if (name != null) {
				WritePrivateMail(pc, name, letter_id, cmdName);
				return true;
			}
			return false;
		}catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭터명] [번호] 입력해 주세요."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(463), true), true);
			return false;
		}
	}
	/** 개인편지 자동으로 보내기 **/
	private void WritePrivateMail(L1PcInstance sender, String receiverName, int letter_id, String cmdName) {

		Connection con = null;
		PreparedStatement pstm = null;  
		ResultSet rs = null;

		try{
			//SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd", Locale.KOREA);
			Timestamp dTime = new Timestamp(System.currentTimeMillis());

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT subject, content FROM letter_command WHERE id = ?");
			pstm.setInt(1, letter_id);
			rs = pstm.executeQuery();
			if (!rs.next()) {
//AUTO SRM: 				sender.sendPackets(new S_SystemMessage("그런 번호를 가진 내용이 없습니다."), true); // CHECKED OK
				sender.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(464), true), true);
				return;
			}
			String subject = rs.getString("subject");
			String content = rs.getString("content");

			pstm.close();
			rs.close();
			
			if(subject == null || content == null){
//AUTO SRM: 				sender.sendPackets(new S_SystemMessage("번호에 제목 또는 내용이 등록되어있지 않습니다."), true); // CHECKED OK
				sender.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(465), true), true);
				return;
			}
			
			L1PcInstance target = L1World.getInstance().getPlayer(receiverName);
			if (target == null) {
				target = CharacterTable.getInstance().restoreCharacter(receiverName);
			}
			LetterTable.getInstance().writeLetter(949, dTime, sender.getName(), receiverName, 0, subject, content);
			sendMessageToReceiver(target, sender, 0, 50);

			
//			if (target != null){
//				sender.sendPackets(new S_SystemMessage(receiverName + "님께 편지를 보냈습니다."), true);
//				return;
//			} else if(target == null){
//				sender.sendPackets(new S_SystemMessage(receiverName + " 님은 존재하지 않는 캐릭입니다."), true);
//			}
			if (target == null){
//AUTO SRM: 				sender.sendPackets(new S_SystemMessage(receiverName + " 님은 존재하지 않는 캐릭입니다."), true); // CHECKED OK
				sender.sendPackets(new S_SystemMessage(receiverName  + S_SystemMessage.getRefText(466), true), true);
				return;
			}
//AUTO SRM: 			sender.sendPackets(new S_SystemMessage(receiverName + "님께 편지를 보냈습니다."), true); // CHECKED OK
			sender.sendPackets(new S_SystemMessage(receiverName  + S_SystemMessage.getRefText(467), true), true);
			
			
		}catch (Exception e){
//AUTO SRM: 			sender.sendPackets(new S_SystemMessage(".답장 오류"), true); // CHECKED OK
			sender.sendPackets(new S_SystemMessage(cmdName + S_SystemMessage.getRefText(468), true), true);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void LetterList(L1PcInstance pc, int type, int count) {
		pc.sendPackets(new S_LetterList(pc, type, count), true);
	}

	private void sendMessageToReceiver(L1PcInstance receiver, L1PcInstance sender, final int type, final int MAILBOX_SIZE) {
		if (receiver != null && receiver.getOnlineStatus() != 0) {
			LetterList(receiver, type, MAILBOX_SIZE);
			receiver.send_effect_self(1091);
			receiver.sendPackets(l1j.server.server.construct.message.L1ServerMessage.sm428); // 편지가 도착했습니다.
			sender.sendPackets(new S_LetterList(sender, type, MAILBOX_SIZE), true);
			return;
		}
	}

}


