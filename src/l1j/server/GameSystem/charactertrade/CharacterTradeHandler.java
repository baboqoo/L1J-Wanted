package l1j.server.GameSystem.charactertrade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.charactertrade.bean.CharacterTradeObject;
import l1j.server.GameSystem.charactertrade.loader.CharacterTradeLoader;
import l1j.server.GameSystem.dungeontimer.L1DungeonTimer;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerUser;
import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.clientpackets.C_CharacterSelect;
import l1j.server.server.clientpackets.C_RestartComplete;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class CharacterTradeHandler {
	private static final String MARBLE_STR = "[MARBLE]";
	
	public static boolean load(L1PcInstance pc, L1ItemInstance item){// 케릭터 생성
		if (!pc.getMap().isSafetyZone(pc.getLocation())) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("세이프티존에서 사용하실 수 있습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(15), true), true);
			return false;
		}
		
		CharacterTradeLoader loader = CharacterTradeLoader.getInstance();
		CharacterTradeObject obj = loader.get(item.getId());
		if (obj == null) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("구슬에 저장된 케릭터가 없습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(16), true), true);
			return false;			
		}
		
		Account account	= pc.getAccount();
		int characterCount = account.countCharacters();
		if (characterCount >= Config.SERVER.CHARACTER_SLOT_MAX_COUNT || account.getCharSlot() - characterCount <= 0) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("캐릭터 슬롯이 부족합니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(17), true), true);
			return false;
		}
		
		Connection con			= null;
		try {
			con	= L1DatabaseFactory.getInstance().getConnection();
			String charName = isValidCharacter(con, MARBLE_STR, obj.getCharId());
			if (charName == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("구슬에 저장된 케릭터 이름을 불러올 수 없습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(18), true), true);
				return false;	
			}
			updateDungeonTime(pc);
			updateAccount(con, pc.getAccountName(), obj.getCharId());
			loader.marbleDelete(con, obj.getMarbleId());
			loader.remove(obj.getMarbleId());
			account.getArca().createActivation(obj.getCharId());
			obj.dispose();
			pc.getInventory().removeItem(item);
			updateCharacterStatus(pc);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}
		return true;
	}
	
	public static boolean save(L1PcInstance pc, L1ItemInstance item, int objid){// 구슬에 담기
		if (!pc.getMap().isSafetyZone(pc.getLocation())) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("세이프티존에서 사용하실 수 있습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(15), true), true);
			return false;
		}
		if (pc.getId() == objid) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("자기 자신에게는 사용할 수 없습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(19), true), true);
			return false;
		}
		
		Connection con = null;
		try {
			con	= L1DatabaseFactory.getInstance().getConnection();
			if (isTargetLevel(con, pc.getAccountName(), objid) < 70) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("70레벨 이상의 케릭터만 저장할 수 있습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(20), true), true);
				return false;
			}
			String charName = isValidCharacter(con, pc.getAccountName(), objid);
			if (charName == null) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("저장시킬 케릭터의 이름을 불러올 수 없습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(21), true), true);
				return false;
			}
			
			updateAccount(con, MARBLE_STR, objid);
			pc.getAccount().getArca().removeActivation(objid);
			pc.getInventory().removeItem(item, 1);
			L1ItemInstance create = pc.getInventory().storeItem(CharacterTradeManager.MARBLE_LOAD_ID, 1);// 구슬 생성
			CharacterTradeObject obj = new CharacterTradeObject(create.getId(), objid, charName);
			CharacterTradeLoader.getInstance().set(obj);
			updateCharacterStatus(pc);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}
		return true;
	}
	
	private static void updateCharacterStatus(L1PcInstance pc) throws Exception{
		GameClient clnt = pc.getNetConnection();
		C_RestartComplete.restartProcess(pc);
		Account acc		= clnt.getAccount();
		clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
		if (acc.countCharacters() > 0) {
			C_CharacterSelect.sendCharPacks(clnt);
		}
	}
	
	private static void updateDungeonTime(L1PcInstance pc){// 던전 이용 시간 변경
		L1DungeonTimer timer = pc.getDungoenTimer();
		long currentTime = System.currentTimeMillis();
		for (L1DungeonTimerUser user : timer.getTimers().values()) {
			user.setRemainSecond(timer.getTimerValue(user.getInfo()));
			if (user.getResetTime() != null) {
				user.getResetTime().setTime(currentTime);
			} else {
				user.setResetTime(new Timestamp(currentTime));
			}
		}
		timer.upsert();
	}
	
	private static void updateAccount(Connection con, String dst_account, int objid){
		PreparedStatement pstm	= null;
		try {
			pstm	= con.prepareStatement("UPDATE characters SET account_name='" + dst_account + "', TamEndTime=null WHERE objid='" + objid + "'");
			pstm.execute();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
		}
	}
	
	private static String isValidCharacter(Connection con, String account, int objid){
		PreparedStatement 	pstm	= null;
		ResultSet 			rs		= null;
		try {
			pstm	= con.prepareStatement("SELECT char_name FROM characters WHERE account_name='" + account + "' AND objid='" + objid + "'");
			rs		= pstm.executeQuery();
			if (rs.next()) {
				return rs.getString("char_name");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm);
		}
		return null;
	}
	
	private static int isTargetLevel(Connection con, String account, int objid){
		PreparedStatement 	pstm	= null;
		ResultSet 			rs		= null;
		try {
			pstm	= con.prepareStatement("SELECT level FROM characters WHERE account_name='" + account + "' AND objid='" + objid + "'");
			rs		= pstm.executeQuery();
			if (rs.next()) {
				return rs.getInt("level");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm);
		}
		return 0;
	}
}


