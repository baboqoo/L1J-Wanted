package l1j.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.Gender;
import l1j.server.server.GameClient;
import l1j.server.server.controller.action.UserRanking;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_CharPacks;
import l1j.server.server.serverpackets.S_CharPass;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_TamPointNoti;
import l1j.server.server.serverpackets.gamegate.S_ClientGateAppToken;
import l1j.server.server.serverpackets.gamegate.S_GameGatePSSBucketName;
import l1j.server.server.utils.SQLUtil;

public class C_CharacterSelect {
	private static final String C_CHARACTER_SELECT = "[C] C_CharacterSelect";
	private static Logger _log = Logger.getLogger(C_CharacterSelect.class.getName());

	public C_CharacterSelect(GameClient client) {// 케릭터 선택창
		try {
			if (client == null || client.getAccount() == null) {
				return;
			}
			deleteCharacter(client);// 삭제 대기 케릭터 체크
			if (!client.isEnterReady()) {
				//System.out.println("버그접속시도차단  :  " + client.getIp());
				System.out.println("Bug connection attempt blocked: " + client.getIp());
				return;
			}
			
			//if(Config.캐릭터비번사용여부){
			//	client.sendPacket(client.getAccount().getCPW() == null ? S_CharPass.CHAR_PWD_EMPTY : S_CharPass.CHAR_PWD_READY);
			//}
			client.sendPacket(S_CharPass.CHAR_PWD_READY);
			client.sendPacket(S_LoginResult.LOGIN_OK);
			client.sendPacket(S_CharPass.CHAR_LOGIN_NOTICE);
			
			if (client.loginInfoToken != null) {
				S_ClientGateAppToken info		= new S_ClientGateAppToken(client.loginInfoToken);
				client.sendPacket(info);
				info.clear();
				info = null;
			}
			
			client.sendPacket(S_GameGatePSSBucketName.CONFIG);
			
			int amountOfChars = client.getAccount().countCharacters();
			S_CharAmount amount = new S_CharAmount(amountOfChars, client.getAccount().getCharSlot());
			client.sendPacket(amount);
			amount.clear();
			amount = null;
			
			client.sendPacket(S_CharPass.CHAR_SELECT_READY);
			if (amountOfChars > 0) {
				sendCharPacks(client);// 케릭터 정보 불러오기
			}
			client.sendPacket(S_CharPass.CHAR_SELECT_START);
			//client.sendPacket(l1j.server.server.serverpackets.S_Unknown2.LOGIN_TIME_SETTING); // 로그인시 언노처리
			
			client.getAccount().getArca().lessTimeUpdate();
			S_TamPointNoti tam_point = new S_TamPointNoti(client.getAccount().getArca().getPoint());
			client.sendPacket(tam_point);
			tam_point.clear();
			tam_point = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendCharPacks(GameClient client) {
		Connection conn			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			conn = L1DatabaseFactory.getInstance().getConnection();
			pstm = conn.prepareStatement("SELECT * FROM characters WHERE account_name=? ORDER BY objid");
			pstm.setString(1, client.getAccountName());
			rs = pstm.executeQuery();
			Timestamp deleteTime = null;
			S_CharPacks cpk = null;
			while(rs.next()){
				String name		= rs.getString("char_name");
				String clanname	= rs.getString("Clanname");
				int type		= rs.getInt("Type");
				Gender gender	= Gender.fromInt(rs.getInt("gender"));
				int alignment	= rs.getInt("Alignment");

				int currenthp	= rs.getInt("CurHp");
				if (currenthp < 1) {
					currenthp = 1;
				} else if (currenthp > 32767) {
					currenthp = 32767;
				}

				int currentmp	= rs.getInt("CurMp");
				if (currentmp < 1) {
					currentmp = 1;
				} else if (currentmp > 32767) {
					currentmp = 32767;
				}

				int lvl;
				if (Config.SERVER.CHARACTER_CONFIG_IN_SERVER_SIDE) {
					lvl = rs.getInt("level");
					if (lvl < 1) {
						lvl = 1;
					} else if(lvl > 127) {
						lvl = 127;
					}
				} else {
					lvl = 1;
				}
				
				int ac			= rs.getInt("Ac");
				if (ac < -128) {
					ac	= -128;
				}
				
				int str			= rs.getByte("Str");
				int dex			= rs.getByte("Dex");
				int con			= rs.getByte("Con");
				int wis			= rs.getByte("Wis");
				int cha			= rs.getByte("Cha");
				int intel		= rs.getByte("Intel");
				int birth		= rs.getInt("BirthDay");
				
				UserRanking.send_top_ranker_noti(client, name);
				
				deleteTime = rs.getTimestamp("DeleteTime");
				int time = 0;
				if (deleteTime != null && (deleteTime.getTime() - System.currentTimeMillis()) > 0) {
					time =  (int) ((deleteTime.getTime() / 1000) - (System.currentTimeMillis() / 1000));
				}
				cpk = new S_CharPacks(name, clanname, type, gender.toInt(), alignment, currenthp, currentmp, ac, lvl, str, dex, con, wis, cha, intel, birth, time);
				client.sendPacket(cpk);
				cpk.clear();
				cpk = null;
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, conn);
		}
	}
	
	private void deleteCharacter(GameClient client) {
		Connection conn			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			conn	= L1DatabaseFactory.getInstance().getConnection();
			pstm	= conn.prepareStatement("SELECT char_name, Clanname, DeleteTime FROM characters WHERE account_name=? AND DeleteTime IS NOT NULL ORDER BY objid");
			pstm.setString(1, client.getAccountName());
			rs		= pstm.executeQuery();
			Timestamp deleteTime	= null;
			L1Clan clan				= null;
			while(rs.next()){
				String name		= rs.getString("char_name");
				String clanName	= rs.getString("Clanname");
				deleteTime		= rs.getTimestamp("DeleteTime");
				if (deleteTime != null && (((System.currentTimeMillis() - deleteTime.getTime()) / 1000) / 3600) >= 0) {
					clan = L1World.getInstance().getClan(clanName);
					if (clan != null) {
						clan.removeClanMember(name);// 혈맹 탈퇴
					}
					CharacterTable.getInstance().deleteCharacter(client.getAccountName(), name);// 삭제
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, conn);
		}
	}
	
	public String getType() {
		return C_CHARACTER_SELECT;
	}
}
