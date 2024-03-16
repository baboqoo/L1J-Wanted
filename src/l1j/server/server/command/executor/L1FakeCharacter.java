package l1j.server.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.GameServerSetting;
import l1j.server.server.IdFactory;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.controller.FakeCharacterController;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class L1FakeCharacter implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1FakeCharacter();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1FakeCharacter() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(arg);
			String name = stringtokenizer.nextToken();

			if (CharacterTable.doesCharNameExist(name) || L1World.getInstance().getPlayer(name) != null
					|| FakeCharacterController.doesFakeCharNameExist(name)) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("이미 존재하는 캐릭터 이름입니다"), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(171), true), true);
				return false;
			}
			L1PcInstance newPc = new L1PcInstance();
			newPc.setAccountName(StringUtil.EmptyString);
			newPc.setId(IdFactory.getInstance().nextId());
			newPc.setName(name);
			newPc.setHighLevel(1);
			newPc.setExp(0);
			newPc.addBaseMaxHp((short)2000);//14
			newPc.setCurrentHp(2000);//14
			newPc.setDead(false);
			newPc.setActionStatus(0);
			newPc.addBaseMaxMp((short)2);
			newPc.setCurrentMp(2);			
			newPc.getAbility().setBaseStr(16);
			newPc.getAbility().setStr(16);
			newPc.getAbility().setBaseCon(16);
			newPc.getAbility().setCon(16);
			newPc.getAbility().setBaseDex(11);
			newPc.getAbility().setDex(11);
			newPc.getAbility().setBaseCha(13);
			newPc.getAbility().setCha(13);
			newPc.getAbility().setBaseInt(12);
			newPc.getAbility().setInt(12);
			newPc.getAbility().setBaseWis(11);
			newPc.getAbility().setWis(11);
			
			int chance = CommonUtil.random(L1CharacterInfo.MALE_LIST.length);
			int classId = CommonUtil.nextBoolean() ? L1CharacterInfo.MALE_LIST[chance] : L1CharacterInfo.FEMALE_LIST[chance];
			
			newPc.setClassId(classId);
			newPc.setSpriteId(classId);
			newPc.setType(classId);

			newPc.setCurrentWeapon(0);
			newPc.getMoveState().setHeading(pc.getMoveState().getHeading());
			newPc.setX(pc.getX());
			newPc.setY(pc.getY());
			newPc.setMap(pc.getMap());

			newPc.setFood(GameServerSetting.MIN_FOOD_VALUE);
			newPc.setAlignment(Config.SERVER.STANDBY_SERVER ? 0 : CommonUtil.random(32767));

			newPc.setTitle(StringUtil.EmptyString);
			newPc.setClanid(0);
			newPc.setClanName(StringUtil.EmptyString);
			newPc.setAccessLevel((short)0);
			newPc.setGm(false);
			newPc.setMonitor(false);
			newPc.setOnlineStatus(1);
			newPc.setBanned(false);

			newPc.refresh();
			newPc.getMoveState().setMoveSpeed(0);
			newPc.getMoveState().setBraveSpeed(0);
			newPc.setGmInvis(false);
			newPc.noPlayerCK = true;

			L1ItemInstance  item = ItemTable.getInstance().createItem(35);//수련자의 한손검
			L1ItemInstance item1 = ItemTable.getInstance().createItem(175);//수련자의 활
			L1ItemInstance item2 = ItemTable.getInstance().createItem(120);//수련자의 지팡이
			L1ItemInstance item3 = ItemTable.getInstance().createItem(73);// 수련자의 이도류
			L1ItemInstance item4 = ItemTable.getInstance().createItem(147);// 수련자의 도끼
			L1ItemInstance item5 = ItemTable.getInstance().createItem(48);//수련자의 양손검
			L1ItemInstance item6 = ItemTable.getInstance().createItem(105);//수련자의 창

			if (newPc.isKnight() || newPc.isCrown() || newPc.isFencer() || newPc.isLancer()) {// 기사.군주, 검사
				newPc.getInventory().storeItem(item);
				newPc.getInventory().setEquipped(item, true);
			} else if (newPc.isDragonknight()){ //용기사
				newPc.getInventory().storeItem(item5);
				newPc.getInventory().setEquipped(item5, true);
			} else if (newPc.isElf()){ //요정
				newPc.getInventory().storeItem(item1);
				newPc.getInventory().setEquipped(item1, true);
			} else if (newPc.isWizard() || newPc.isIllusionist()){//마법사. 환술사
				newPc.getInventory().storeItem(item2);
				newPc.getInventory().setEquipped(item2, true);
			} else if (newPc.isDarkelf()){ //다크엘프
				newPc.getInventory().storeItem(item3);
				newPc.getInventory().setEquipped(item3, true);
			} else if (newPc.isWarrior()){ //전사
				newPc.getInventory().storeItem(item4);
				newPc.getInventory().setEquipped(item4, true);
			} else if (newPc.isLancer()){ //창기사
				newPc.getInventory().storeItem(item6);
				newPc.getInventory().setEquipped(item6, true);
			}

			L1World.getInstance().storeObject(newPc);
			L1World.getInstance().addVisibleObject(newPc);

			newPc.setNetConnection(null);

			newPc.startObjectAutoUpdate();
			
			save(name, newPc.getX(), newPc.getY(), newPc.getMoveState().getHeading(), newPc.getMapId(), false);
			
			deleteitemDB(newPc);
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage((new StringBuilder()). append(cmdName + " [캐릭이름]로 입력해 주세요. "). toString()), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage((new StringBuilder()). append(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(157)). toString(), true), true);
			return false;
		}
	}
	
	/** 디비저장 **/
	public void save(String name, int x, int y, int heading, int mapId, boolean staticBot) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO bots SET name = ?, x = ?, y = ?, heading = ?, mapId = ?, type = ?");
			pstm.setString(1, name);
			pstm.setInt(2, x);
			pstm.setInt(3, y);
			pstm.setInt(4, heading);
			pstm.setInt(5, mapId);
			pstm.setBoolean(6, staticBot);

			pstm.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/** 아이템 디비 삭제 **/
	public static void deleteitemDB(L1PcInstance pc) {
		java.sql.Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("DELETE FROM character_items WHERE char_id=?");
            pstm.setInt(1, pc.getId());
            pstm.execute();
        } catch (SQLException e) {
        } finally {
            SQLUtil.close(pstm, con);
        }
    }

}

