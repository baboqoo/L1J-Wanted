package l1j.server.server.serverpackets.message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class S_CommonNews extends ServerBasePacket{
	private byte[] _data = null;

	private StringBuffer sb = new StringBuffer();
	private static final String S_COMMON_NEWS = "[S] S_CommonNews";
	
/* 
	public static final S_CommonNews CHARACTER_EMPTY		= new S_CommonNews("존재하지 않는 캐릭터 입니다.");
	public static final S_CommonNews CONNECT_DELETE_FAIL	= new S_CommonNews("접속 중인 캐릭터는 삭제할 수 없습니다.");
	public static final S_CommonNews BLESS_ITEM_DELETE_FAIL	= new S_CommonNews("봉인된 아이템을 가진 캐릭터는 삭제할 수 없습니다.");
	public static final S_CommonNews CLAN_DELETE_FAIL		= new S_CommonNews("혈맹에 가입된 캐릭터는 삭제할 수 없습니다.");
	public static final S_CommonNews EIN_GRACE_DELETE_FAIL	= new S_CommonNews("아인하사드의 가호가 적용되어 있는 캐릭터는 삭제가 불가능 합니다.");
	public static final S_CommonNews IP_CHECK_FAIL			= new S_CommonNews("현재 IP로 다른 계정이 이미 접속중입니다.");
	public static final S_CommonNews IP_COUNT_MAX			= new S_CommonNews("\n\n\n\n동일 IP에서 접속가능한 개수를 초과하였습니다.");
	public static final S_CommonNews CLIENT_COUNT_MAX		= new S_CommonNews("\n\n\n\n접속 가능한 최대 클라이언트 개수를 초과하였습니다.");
	public static final S_CommonNews ACCOUNT_BAN_CHECK		= new S_CommonNews("\n\n\n\n현재 이 계정은 압류되어있습니다.\n\n차단될 사유가없다면 운영자에게 문의하세요");
	public static final S_CommonNews IP_BAN_CHECK			= new S_CommonNews("\n\n\n\n해당 IP는 운영자에 의해 차단되었습니다.\n\n     서버 운영자에게 문의하시기바랍니다.");
	public static final S_CommonNews IP_VPN_CHECK			= new S_CommonNews("\n\n\n\nVPN 또는 해외 IP는 접속이 불가합니다.\n\n정상IP일 경우 고객센터로 문의하시기 바랍니다.");
	public static final S_CommonNews HDD_BAN_CHECK			= new S_CommonNews("\n\n\n\n하드밴 처리된 클라이언트입니다.\n\n이용하실 수 없습니다.");
	public static final S_CommonNews MAX_USER				= new S_CommonNews("\n\n★ 허용된 접속 인원을 초과하였습니다.\n\n잠시 후에 접속을 시도해주세요.\n\n죄송합니다.");
	public static final S_CommonNews RE_LOGIN				= new S_CommonNews("이미 접속 중 입니다. 접속을 강제 종료합니다.");
	public static final S_CommonNews OTHER_CONNECTOR		= new S_CommonNews("\n\n\n\n정상적인 접속경로가 아닙니다.\n\n접속기를 새로 받아주십시오.");
	public static final S_CommonNews OTHER_CLIENT_VERSION	= new S_CommonNews("\n\n\n\n클라이언트 버전이 낮습니다.\n\n1.클라이언트 업데이트\n2.접속기 새로 받기");
	public static final S_CommonNews CONNECT_LOCK			= new S_CommonNews("\n\n\n\n현재 서버 점검중이므로 접속을 제한합니다.");
	public static final S_CommonNews SESSOION_EMPTY			= new S_CommonNews("\n\n\n\n로그인 세션이 만료 되었습니다.\n\n재접속하시기 바랍니다.");
*/

	public static final S_CommonNews CHARACTER_EMPTY = new S_CommonNews("This character does not exist.");
	public static final S_CommonNews CONNECT_DELETE_FAIL = new S_CommonNews("A character cannot be deleted while connected.");
	public static final S_CommonNews BLESS_ITEM_DELETE_FAIL = new S_CommonNews("Characters with sealed items cannot be deleted.");
	public static final S_CommonNews CLAN_DELETE_FAIL = new S_CommonNews("Characters that have joined a clan cannot be deleted.");
	public static final S_CommonNews EIN_GRACE_DELETE_FAIL = new S_CommonNews("Characters with Einhasad's protection applied cannot be deleted.");
	public static final S_CommonNews IP_CHECK_FAIL = new S_CommonNews("Another account is already accessing with the current IP.");
	public static final S_CommonNews IP_COUNT_MAX = new S_CommonNews("\n\n\n\nExceeded the number of connections available from the same IP.");
	public static final S_CommonNews CLIENT_COUNT_MAX = new S_CommonNews("\n\n\n\nThe maximum number of clients that can be connected has been exceeded.");
	public static final S_CommonNews ACCOUNT_BAN_CHECK = new S_CommonNews("\n\n\n\nCurrently this account is suspended.\n\nIf there is no reason to be blocked, contact the operator");
	public static final S_CommonNews IP_BAN_CHECK = new S_CommonNews("\n\n\n\nThat IP has been blocked by the operator.\n\n     Please contact the server operator.");
	public static final S_CommonNews IP_VPN_CHECK = new S_CommonNews("\n\n\n\nVPN or overseas IP is not accessible.\n\nIf the IP address is normal, please contact the customer center.");
	public static final S_CommonNews HDD_BAN_CHECK = new S_CommonNews("\n\n\n\nThis is a hardbanned client.\n\nNot available.");
	public static final S_CommonNews MAX_USER = new S_CommonNews("\n\n★ You have exceeded the number of allowed connections.\n\nPlease try again later.\n\nSorry.");
	public static final S_CommonNews RE_LOGIN = new S_CommonNews("You are already connected. Force close the connection.");
	public static final S_CommonNews OTHER_CONNECTOR = new S_CommonNews("\n\n\n\nThis is not a normal access path.\n\nPlease download a new connector.");
	public static final S_CommonNews OTHER_CLIENT_VERSION = new S_CommonNews("\n\n\n\nThe client version is low.\n\n1.Update the client\n2.Get a new connector");
	public static final S_CommonNews CONNECT_LOCK = new S_CommonNews("\n\n\n\nAccess is restricted as the server is currently undergoing maintenance.");
	public static final S_CommonNews SESSOION_EMPTY = new S_CommonNews("\n\n\n\nYour login session has expired.\n\nPlease reconnect.");

	public S_CommonNews(String account, GameClient ct){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT id, message FROM notice WHERE id > (SELECT notice FROM accounts WHERE login='" + account + "') LIMIT 1");
			rs		= pstm.executeQuery();
			String noticeId		= StringUtil.EmptyString;
			String noticeMsg	= StringUtil.EmptyString;
			if (rs.next()) {
				noticeId = rs.getString("id");
				noticeMsg = rs.getString("message");
				StringTokenizer st = new StringTokenizer(noticeMsg, StringUtil.MinusString);
				while (st.hasMoreElements()) {
					sb.append(st.nextToken()).append("\n");
				}
				writeC(Opcodes.S_NEWS);		// opcode
				writeS(sb.toString());		// Data
				sb.setLength(0);
				updateAccountNotice(account, noticeId);
			}
		} catch (Exception e){
			System.out.println("notice Error : " + e.getMessage());
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public S_CommonNews(String msg) {
		writeC(Opcodes.S_NEWS);
		writeS(msg);
	}
	
	/**
	 * 계정명으로 읽어야할 공지 갯수 리턴
	 * @param account 계정명
	 * @return 공지 갯수
	 */
	public static int getNoticeCount(String account){
		int count = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT MAX(id) AS cnt FROM notice WHERE id > (SELECT notice FROM accounts WHERE login='"+ account + "')");
			rs = pstm.executeQuery();
			if(rs.next())count = rs.getInt("cnt");
		} catch(Exception e){
			count = 0;
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return count;
	}
	
	/**
	 * 계정명을 대상으로 읽은 공지사항 번호를 변경
	 * @param account
	 */
	public void updateAccountNotice(String account, String noticeId){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET notice='" + noticeId + "' WHERE login='" + account + "'");
			pstm.execute();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	@Override
	public byte[] getContent() {
		if (_data == null) {
			_data= _bao.toByteArray();
		}
		return _data;
	}
	@Override
	public String getType() {
		return S_COMMON_NEWS;
	}
}
