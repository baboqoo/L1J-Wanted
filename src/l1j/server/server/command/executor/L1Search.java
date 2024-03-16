package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class L1Search implements L1CommandExecutor{
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Search();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Search() {}
	
//AUTO SRM: 	private static final S_SystemMessage MESSAGE = new S_SystemMessage(".검색 [0~6] [name]을 입력 해주세요.\n0=잡템, 1=무기, 2=갑옷, 3=엔피시, 4=변신, 5=엔피시(spriteId), 6=스킬(skill)");
	
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (pc == null) {
				return false;
			}
			StringTokenizer tok = new StringTokenizer(arg);
			int type = Integer.parseInt(tok.nextToken());
			String desc = tok.nextToken();
			if (type < 0 || type > 6) {
				//pc.sendPackets(MESSAGE);
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(657), true), true);
				return false;
			}
			searchObject(pc, type, desc);
			return true;
		} catch (Exception e) {
			//pc.sendPackets(MESSAGE);
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(657), true), true);
			return false;
		}
	}
	
	private void searchObject( L1PcInstance gm, int type, String desc ) {
		StringBuilder sb		= new StringBuilder();
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		int count				= 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			switch (type) {
			case 0:// etcitem
				pstm = con.prepareStatement(sb.append("SELECT CONCAT('[', item_id, ']-[', desc_kr, ']-', desc_id) AS searching FROM etcitem WHERE desc_kr LIKE '%").append(desc).append("%'").toString());
				break;
			case 1:// weapon
				pstm = con.prepareStatement(sb.append("SELECT CONCAT('[', item_id, ']-[', desc_kr, ']-', desc_id) AS searching FROM weapon WHERE desc_kr LIKE '%").append(desc).append("%'").toString());
				break;
			case 2:// armor
				pstm = con.prepareStatement(sb.append("SELECT CONCAT('[', item_id, ']-[', desc_kr, ']-', desc_id) AS searching FROM armor WHERE desc_kr LIKE '%").append(desc).append("%'").toString());
				break;
			case 3:// npc
				pstm = con.prepareStatement(sb.append("SELECT CONCAT('[', npcid, ']-[', desc_kr, ']-', note) AS searching FROM npc WHERE desc_kr LIKE '%").append(desc).append("%'").toString());
				break;
			case 4:// polymorphs
				pstm = con.prepareStatement(sb.append("SELECT CONCAT('[', polyid, ']-[', name, ']-', id) AS searching FROM polymorphs WHERE name LIKE '%").append(desc).append("%'").toString());
				break;
			case 5:// npc(spriteId)
				pstm = con.prepareStatement(sb.append("SELECT CONCAT('[', spriteId, ']-[', desc_kr, ']-', note) AS searching FROM npc WHERE desc_kr LIKE '%").append(desc).append("%'").toString());
				break;
			case 6:// skill
				pstm = con.prepareStatement(sb.append("SELECT CONCAT('[', skill_id, ']-[', name, ']-', skill_level) AS searching FROM skills WHERE name LIKE '%").append(desc).append("%'").toString());
				break;
			default:
				break;
			}
			sb.setLength(0);
			rs = pstm.executeQuery();
			while (rs.next()) {
				count++;
				sb.append(rs.getString(1)).append(StringUtil.LineString);
				if (count % 10 == 0) {
					gm.sendPackets(new S_SystemMessage(sb.toString()), true);
					sb.setLength(0);
				}
			}
//AUTO SRM: 			gm.sendPackets(new S_SystemMessage(sb.append("총 [").append(count).append("]개 검색이 되었습니다.").toString()), true); // CHECKED OK
			gm.sendPackets(new S_SystemMessage(sb.append(S_SystemMessage.getRefText(658)).append(count).append(S_SystemMessage.getRefText(659)).toString(), true), true);
			sb = null;
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

}


