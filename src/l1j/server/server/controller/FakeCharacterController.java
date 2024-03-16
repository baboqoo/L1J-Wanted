package l1j.server.server.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.Gender;
import l1j.server.server.GameServerSetting;
import l1j.server.server.IdFactory;
import l1j.server.server.RepeatTask;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1FakePcInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

class L1FakeCharacterInfo {
	public L1FakeCharacterInfo() {
		_x = 0;
		_y = 0;
		_heading = 0;
		_mapId = 0;
		_staticBot = false;
	}
	
	public L1FakeCharacterInfo(String name, int x, int y, int heading, int mapId, boolean staticBot) {
		_name = name;
		_x = x;
		_y = y;
		_heading = heading;
		_mapId = mapId;
		_staticBot = staticBot;
	}

	public String	_name;
	public int		_x;
	public int		_y;
	public int		_heading;
	public int		_mapId;
	public boolean	_staticBot;
}

public class FakeCharacterController extends RepeatTask {
	private static final int[] WEAPON_LIST = new int[] { 35/*군주*/, 35/*기사*/, 175/*요정*/, 120/*법사*/, 73/*다엘*/, 48/*용기사*/, 120/*환술사*/, 147/*전사*/, 35/*검사*/ };
	private static final int[] TYPE_RATIO = new int[] { 5/*군주*/, 20/*기사*/, 10/*요정*/, 10/*법사*/, 10/*다엘*/, 5/*용기사*/, 5/*환술사*/, 15/*전사*/, 20/*검사*/ };
	
	private static final int STATUS_NONE = 0;
	private static final int STATUS_SPAWN_NORMAL = 1;
	private static final int STATUS_SPAWN_POLYMORPH = 2;
	

	private static Logger _log = Logger.getLogger(FakeCharacterController.class.getName());

	private static FakeCharacterController _instance;

	private List<L1FakeCharacterInfo> _fakeCharacterList = new ArrayList<L1FakeCharacterInfo>();
	
	private int _status = STATUS_NONE;

	public static FakeCharacterController getInstance() {
		if (_instance == null)
			_instance = new FakeCharacterController();
		return _instance;
	}
	
	public FakeCharacterController() {
		super(2000);
		_status = STATUS_NONE;
	}

	@Override
	public void execute() {
		if (_status == STATUS_NONE)
			return;
		if (!_fakeCharacterList.isEmpty()) {
			L1FakeCharacterInfo info = _fakeCharacterList.get(0);
			_fakeCharacterList.remove(0);
			if (_status == STATUS_SPAWN_NORMAL)
				loadBot(info._name, info._x, info._y, info._heading, info._mapId, info._staticBot, false);
			else
				loadBot(info._name, info._x, info._y, info._heading, info._mapId, info._staticBot, true);
		}
	}
	
	public void start(int status) {
		if (_status != STATUS_NONE)
			return;
		if (status < STATUS_NONE || status > STATUS_SPAWN_POLYMORPH)
			status = STATUS_NONE;
		_status = status;
	}
	
	public void startBot() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM bots");
			rs = pstm.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				int x = rs.getInt("x");
				int y = rs.getInt("y");
				int heading = rs.getInt("heading");
				int mapId = rs.getInt("mapId");
				boolean staticBot = rs.getBoolean("type");
				
				if (name != null){
				    if (!CharacterTable.doesCharNameExist(name)){
				        int gender = CommonUtil.random(2);
				        int type = 0;
				        int typeRatio = CommonUtil.random(100);
				        for (int i = 0; i < TYPE_RATIO.length; ++i) {
					        if (TYPE_RATIO[i] > typeRatio) {
						        type = i;
						        break;
					        }
					        typeRatio -= TYPE_RATIO[i];
				        }
				        createBot(name, gender, type, x, y, heading, mapId);
				    }				
				    loadBot(name, x, y, heading, mapId, staticBot, true);
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void loadBot(String name, int x, int y, int heading, int mapId, boolean staticBot, boolean doPoly) {
		L1FakePcInstance pc = L1FakePcInstance.load(name);
		if (pc == null)
			return;
		pc.getInventory().loadItems();
		pc.SetSpawnInfo(x, y, heading, mapId);

		pc.getLocation().set(x, y);
		pc.getMoveState().setHeading(heading);
		pc.setMap((short) mapId);

		if (doPoly)
			pc.setPolymorph();
		
		pc.setAlignment(Config.SERVER.STANDBY_SERVER ? 0 : CommonUtil.random(32767));
		pc.noPlayerCK = true;

		pc.getMap().setPassable(pc.getLocation(), false);

		L1World.getInstance().storeObject(pc);
		L1World.getInstance().addVisibleObject(pc);
		if (!staticBot)
			pc.startAction();
	}
	
	private void addBot(String name, int x, int y, int heading, int mapId, boolean staticBot) {
		L1FakeCharacterInfo info = new L1FakeCharacterInfo(name, x, y, heading, mapId, staticBot);
		_fakeCharacterList.add(info);
	}

	public void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM bots");
			rs = pstm.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				int x = rs.getInt("x");
				int y = rs.getInt("y");
				int heading = rs.getInt("heading");
				int mapId = rs.getInt("mapId");
				boolean staticBot = rs.getBoolean("type");

				addBot(name, x, y, heading, mapId, staticBot);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void deleteBot(String name) {
		L1PcInstance target = L1World.getInstance().getPlayer(name);
		target.logout();
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			if (target != null) {
				L1Clan clan = target.getClan();
				if (clan != null) {
					clan.removeClanMember(name);
					// clan.delMemberName(name);
				}
			}
			if (CharacterTable.doesCharNameExist(name))
			    CharacterTable.getInstance().deleteCharacter(StringUtil.EmptyString, name);

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM bots WHERE name = ?");
			pstm.setString(1, name);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void endBot() {
		for (L1FakePcInstance fakepc : L1World.getInstance().getAllFakePc()) {
			if (fakepc == null)
				continue;
			fakepc.logout();
		}
	}


	public boolean createNewBot(String name, int x, int y, int heading, int mapId, boolean staticBot) {
		if (CharacterTable.doesCharNameExist(name) || L1World.getInstance().getPlayer(name) != null || doesFakeCharNameExist(name))
			return false;

		int sex = CommonUtil.random(2);

		int type = 0;
		int typeRatio = CommonUtil.random(100);
		for (int i = 0; i < TYPE_RATIO.length; ++i) {
			if (TYPE_RATIO[i] > typeRatio) {
				type = i;
				break;
			}
			typeRatio -= TYPE_RATIO[i];
		}

		L1PcInstance targetPc = createBot(name, sex, type, x, y, heading, mapId);
		targetPc.logout();

		Connection con = null;
		PreparedStatement pstm = null;

		try {
			CharacterTable.getInstance().storeNewCharacter(targetPc);

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

		if (staticBot) {
			loadBot(name, x, y, heading, mapId, staticBot, false);
			return true;
		} else if (_status == STATUS_SPAWN_NORMAL)
			loadBot(name, x, y, heading, mapId, staticBot, false);
		else
			loadBot(name, x, y, heading, mapId, staticBot, true);
		return true;
	}

	public L1PcInstance createBot(String name, int gender, int type, int x, int y, int heading, int mapId) {
		L1FakePcInstance newPc = new L1FakePcInstance(x, y, heading, mapId);
		newPc.setAccountName(StringUtil.EmptyString);
		newPc.setId(IdFactory.getInstance().nextId());
		newPc.setName(name);
		newPc.setHighLevel(1);
		newPc.setExp(0);
		newPc.addBaseMaxHp((short) 5000);
		newPc.setCurrentHp(5000);
		newPc.setDead(false);
		newPc.setActionStatus(0);
		newPc.addBaseMaxMp((short) 2);
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

		int klass = gender == 0 ? L1CharacterInfo.MALE_LIST[type] : L1CharacterInfo.FEMALE_LIST[type];

		newPc.setCurrentWeapon(0);
		newPc.setClassId(klass);
		newPc.setSpriteId(klass);
		newPc.setGender(Gender.fromInt(gender));
		newPc.setType(type + 1);

		newPc.getMoveState().setHeading(heading);
		newPc.setX(x);
		newPc.setY(y);
		newPc.setMap((short) mapId);

		newPc.setFood(GameServerSetting.MIN_FOOD_VALUE);
		newPc.setAlignment(Config.SERVER.STANDBY_SERVER ? 0 : CommonUtil.random(32767));
		newPc.setTitle(StringUtil.EmptyString);
		newPc.setClanName(StringUtil.EmptyString);
		newPc.setClanMemberNotes(StringUtil.EmptyString);
		newPc.setAccessLevel((short) 0);
		newPc.setGm(false);
		newPc.setMonitor(false);
		newPc.setOnlineStatus(1);
		newPc.setBanned(false);

		newPc.refresh();
		newPc.getMoveState().setMoveSpeed(0);
		newPc.getMoveState().setBraveSpeed(0);
		newPc.setGmInvis(false);

		L1ItemInstance item = ItemTable.getInstance().createItem(WEAPON_LIST[type]);
		newPc.getInventory().storeItem(item);
		newPc.getInventory().setEquipped(item, true);
		
		newPc.noPlayerCK = true;

		newPc.getMap().setPassable(newPc.getLocation(), false);

		L1World.getInstance().storeObject(newPc);
		L1World.getInstance().addVisibleObject(newPc);

		newPc.setNetConnection(null);
		newPc.startObjectAutoUpdate();
		
		deleteitemDB(newPc);

		return newPc;
	}
	
	/** 무인 디비에 동일 이름 검색 **/
	public static boolean doesFakeCharNameExist(String name) {
		boolean result = true;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM bots WHERE name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			result = rs.next();
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}
	
	/** 아이템 디비 **/
	public static void deleteitemDB(L1FakePcInstance pc) {
		java.sql.Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("DELETE FROM character_items WHERE char_id=?");
            pstm.setInt(1, pc.getId());
            pstm.execute();
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm, con);
        }
    }

}

