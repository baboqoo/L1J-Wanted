package l1j.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.message.S_CommonNews;
import l1j.server.server.utils.SQLUtil;

public class C_ReadNews extends ClientBasePacket {
	private static final String C_READ_NEWS = "[C] C_ReadNews";
	
	public C_ReadNews(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		try{
			int noticeCount = S_CommonNews.getNoticeCount(client.getAccountName());
			if (noticeCount > 0) {// 공지 존재
				int type = readC();// 0: 확인, 1:건너띄기
				if (type == 1) {// 건너 띄기
					passNotice(client.getAccountName(), noticeCount);
					new C_CharacterSelect(client);
					return;
				}
				S_CommonNews notice = new S_CommonNews(client.getAccountName(), client);
				client.sendPacket(notice);
				notice.clear();
				notice = null;
				return;
			}
			new C_CharacterSelect(client);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}
	
	private void passNotice(String account, int noticeId){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET notice=? WHERE login=?");
			pstm.setInt(1, noticeId);
			pstm.setString(2, account);
			pstm.execute();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	@Override
	public String getType() {
		return C_READ_NEWS;
	}

}

