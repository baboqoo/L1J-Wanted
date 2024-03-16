package l1j.server.server.storage.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.beginnerquest.BeginnerQuestUserTable;
import l1j.server.GameSystem.deathpenalty.DeathPenaltyTable;
import l1j.server.GameSystem.eventpush.EventPushLoader;
import l1j.server.GameSystem.fatigue.L1Fatigue;
import l1j.server.GameSystem.huntingquest.user.HuntingQuestUserTable;
import l1j.server.GameSystem.tjcoupon.TJCouponLoader;
import l1j.server.RobotSystem.L1RobotAI;
import l1j.server.common.data.Gender;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.datatables.CharacterEinhasadStatTable;
import l1j.server.server.datatables.ClanJoinningTable;
import l1j.server.server.datatables.EquipSetTable;
import l1j.server.server.datatables.RevengeTable;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.L1CharacterConfig;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.Instance.L1FakePcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.loader.L1FavorBookUserLoader;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionUserLoader;
import l1j.server.server.storage.CharacterStorage;
import l1j.server.server.utils.SQLUtil;

public class MySqlCharacterStorage implements CharacterStorage {
	private static Logger _log = Logger.getLogger(MySqlCharacterStorage.class.getName());

	@Override
	public L1PcInstance loadCharacter(String charName) {
		L1PcInstance pc = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
			pstm.setString(1, charName);
			rs = pstm.executeQuery();
			if (!rs.next()) {
				return null;// SELECT가 결과를 돌려주지 않았다.
			}
			
			pc = new L1PcInstance();
			L1CharacterConfig config = pc.getConfig();
			
			pc.setAccountName(rs.getString("account_name"));
			pc.setId(rs.getInt("objid"));
			pc.setName(rs.getString("char_name"));
			pc.setHighLevel(rs.getInt("HighLevel"));
			pc.setExp(rs.getLong("Exp"));
			pc.addBaseMaxHp(rs.getShort("MaxHp"));
			short currentHp = rs.getShort("CurHp");
			if (currentHp < 1) {
				currentHp = 1;
			}
			pc.setCurrentHp(currentHp);
			pc.setDead(false);
			pc.setActionStatus(0);
			pc.addBaseMaxMp(rs.getShort("MaxMp"));
			short currentMp = rs.getShort("CurMp");
			pc.setCurrentMp(currentMp);
			pc._db_current_hp = currentHp;
			pc._db_current_mp = currentMp;
			
			L1Ability ability	= pc.getAbility();
			ability.setBaseStr(rs.getByte("BaseStr"));
			ability.setStr(rs.getByte("Str"));
			ability.setBaseCon(rs.getByte("BaseCon"));
			ability.setCon(rs.getByte("Con"));
			ability.setBaseDex(rs.getByte("BaseDex"));
			ability.setDex(rs.getByte("Dex"));
			ability.setBaseCha(rs.getByte("BaseCha"));
			ability.setCha(rs.getByte("Cha"));
			ability.setBaseInt(rs.getByte("BaseIntel"));
			ability.setInt(rs.getByte("Intel"));
			ability.setBaseWis(rs.getByte("BaseWis"));
			ability.setWis(rs.getByte("Wis"));
			
			pc.setCurrentWeapon(rs.getInt("Status"));
			int classId = rs.getInt("Class");
			pc.setClassId(classId);
			pc.setSpriteId(classId);
			pc.setGender(Gender.fromInt(rs.getInt("gender")));
			pc.setType(rs.getInt("Type"));
			int head = rs.getInt("Heading");
			if (head > 7) {
				head = 0;
			}
			pc.getMoveState().setHeading(head);
			pc.setX(rs.getInt("locX"));
			pc.setY(rs.getInt("locY"));
			pc.setMap(rs.getShort("MapID"));
			pc.setFood(rs.getInt("Food"));
			pc.setAlignment(rs.getInt("Alignment"));
			pc.setTitle(rs.getString("Title"));
			pc.setClanid(rs.getInt("ClanID"));
			pc.setClanName(rs.getString("Clanname"));
			int bloodPledgeRank = rs.getInt("ClanRank");
			if (bloodPledgeRank > 0) {
				pc.setBloodPledgeRank(eBloodPledgeRankType.fromInt(bloodPledgeRank));
			}
			pc.setClanContribution(rs.getInt("ClanContribution"));
			pc.setClanWeekContribution(rs.getInt("ClanWeekContribution"));
			pc.setPledgeJoinDate(rs.getInt("pledgeJoinDate"));
			pc.setPledgeRankDate(rs.getInt("pledgeRankDate"));
			
			pc.setClanMemberNotes(rs.getString("notes"));
			pc.setBonusStats(rs.getInt("BonusStatus"));
			pc.setElixirStats(rs.getInt("ElixirStatus"));
			pc.setElfAttr(rs.getInt("ElfAttr"));
			pc.setPKcount(rs.getInt("PKcount"));
			pc.setExpRes(rs.getLong("ExpRes"));
			pc.setPartnerId(rs.getInt("PartnerID"));
			pc.setAccessLevel(rs.getShort("AccessLevel"));
			if (pc.getAccessLevel() == Config.ALT.GMCODE) {
				pc.setGm(true);
				pc.setMonitor(false);
			} else if (pc.getAccessLevel() == 100) {
				pc.setGm(false);
				pc.setMonitor(true);
			} else {
				pc.setGm(false);
				pc.setMonitor(false);
			}
			pc.setOnlineStatus(rs.getInt("OnlineStatus"));
			pc.setHomeTownId(rs.getInt("HomeTownID"));
			pc.setContribution(rs.getInt("Contribution"));
			pc.setHellTime(rs.getInt("HellTime"));
			pc.setBanned(rs.getBoolean("Banned"));
			pc.setKarma(rs.getInt("Karma"));
			pc.setLastPk(rs.getTimestamp("LastPk"));
			pc.setDeleteTime(rs.getTimestamp("DeleteTime"));
			pc.setReturnStat_exp(rs.getLong("ReturnStat"));
			pc.setLastLoginTime(rs.getTimestamp("lastLoginTime"));
			pc.setLastLogoutTime(rs.getTimestamp("lastLogoutTime"));
			pc.setKills(rs.getInt("PC_Kill"));
			pc.setDeaths(rs.getInt("PC_Death"));
			pc.setBookMarkCount(rs.getInt("Mark_Count"));// 기억
			
			pc.setSpecialWareHouseSize(rs.getInt("SpecialSize"));// 특수창고
			
			config.setHuntPrice(rs.getInt("HuntPrice"));
			config.setHuntCount(rs.getInt("HuntCount"));
			config.setReasonToHunt(rs.getString("HuntText"));
			
			pc.setRingSlotLevel(rs.getInt("RingAddSlot"));
			pc.setEarringSlotLevel(rs.getInt("EarringAddSlot"));
			pc.setBadgeSlotLevel(rs.getInt("BadgeAddSlot"));
			pc.setShoulderSlotLevel(rs.getInt("ShoulderAddSlot"));
			
			pc.setEMETime(rs.getTimestamp("EMETime"));
			pc.setEMETime2(rs.getTimestamp("EMETime2"));
			pc.setPUPLETime(rs.getTimestamp("PUPLETime"));
			pc.setTOPAZTime(rs.getTimestamp("TOPAZTime"));
			
			pc.setEinhasadGraceTime(rs.getTimestamp("EinhasadGraceTime"));
			
			pc.setEinPoint(rs.getInt("EinPoint"));
			pc.setEinCurEnchantLevel(rs.getInt("EinCardLess"));
			pc.setEinTotalStat(rs.getInt("EinCardState"));
			pc.setEinBonusCardOpenValue(rs.getInt("EinCardBonusValue"));
			
			pc.getSkill().setThirdSkillDelay(rs.getTimestamp("ThirdSkillTime"));
			pc.getSkill().setFiveSkillDelay(rs.getTimestamp("FiveSkillTime"));
			pc.setSurvivalTime(rs.getTimestamp("SurvivalTime"));
			
			int potentialTargetId	= rs.getInt("potentialTargetId");
			if (potentialTargetId > 0) {
				config.set_potential_enchant(potentialTargetId, rs.getInt("potentialBonusGrade"), rs.getInt("potentialBonusId"));
			}
			
			if (pc.getAccountName().equals("AIRobot")) {
				pc.setRobotAi(new L1RobotAI(pc));
			}
			
			pc.createFatigue(rs.getInt("fatigue_point"), rs.getTimestamp("fatigue_rest_time"));
			
			pc.refresh();
			pc.getMoveState().setMoveSpeed(0);
			pc.getMoveState().setBraveSpeed(0);
			pc.setGmInvis(false);
			config.setAnonymityName();// 익명
			config.setProfileUrl();// 앱센터 클래스 이미지
			
			_log.finest("restored char data: ");
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			pc = null;
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return pc;
	}

	@Override
	public void createCharacter(L1PcInstance pc) {
		L1CharacterConfig config	= pc.getConfig();
		L1Ability ability			= pc.getAbility();
		L1SkillStatus skill			= pc.getSkill();
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			int i = 0;
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO characters SET account_name=?,objid=?,char_name=?," 
					+ "level=?,HighLevel=?,Exp=?,MaxHp=?,CurHp=?,MaxMp=?,CurMp=?,Ac=?,Str=?," 
					+ "BaseStr=?,Con=?,BaseCon=?,Dex=?,BaseDex=?,Cha=?,BaseCha=?,Intel=?,BaseIntel=?," 
					+ "Wis=?,BaseWis=?,Status=?,Class=?,gender=?,Type=?,Heading=?,LocX=?,LocY=?,MapID=?,Food=?," 
					+ "Alignment=?,Title=?,ClanID=?,Clanname=?,ClanRank=?,ClanContribution=?,ClanWeekContribution=?,notes=?,BonusStatus=?,ElixirStatus=?,ElfAttr=?," 
					+ "PKcount=?,ExpRes=?,PartnerID=?,AccessLevel=?,OnlineStatus=?,HomeTownID=?,Contribution=?," 
					+ "HellTime=?,Banned=?,Karma=?,LastPk=?,DeleteTime=?,ReturnStat=?,lastLoginTime=now(),"
					+ "BirthDay=?,Mark_Count=?,"
					+ "SpecialSize=?,HuntPrice=?, HuntCount=?, HuntText=?, "
					+ "RingAddSlot=?, EarringAddSlot=?, BadgeAddSlot=?, ShoulderAddSlot=?, "
					+ "EMETime=?, EMETime2=?, PUPLETime=?, TOPAZTime=?, EinhasadGraceTime=?, "
					+ "EinPoint=?, EinCardLess=?, EinCardState=?, "
					+ "ThirdSkillTime=?, FiveSkillTime=?, SurvivalTime=?");
			pstm.setString(++i, pc.getAccountName());
			pstm.setInt(++i, pc.getId());
			pstm.setString(++i, pc.getName());
			pstm.setInt(++i, pc.getLevel());
			pstm.setInt(++i, pc.getHighLevel());
			pstm.setLong(++i, pc.getExp());
			pstm.setInt(++i, pc.getBaseMaxHp());
			int hp = pc.getCurrentHp();
			if(hp < 1)hp = 1;
			pstm.setInt(++i, hp);
			pstm.setInt(++i, pc.getBaseMaxMp());
			pstm.setInt(++i, pc.getCurrentMp());
			pstm.setInt(++i, pc.getAC().getAc());
			pstm.setInt(++i, ability.getStr());
			pstm.setInt(++i, ability.getBaseStr());
			pstm.setInt(++i, ability.getCon());
			pstm.setInt(++i, ability.getBaseCon());
			pstm.setInt(++i, ability.getDex());
			pstm.setInt(++i, ability.getBaseDex());
			pstm.setInt(++i, ability.getCha());
			pstm.setInt(++i, ability.getBaseCha());
			pstm.setInt(++i, ability.getInt());
			pstm.setInt(++i, ability.getBaseInt());
			pstm.setInt(++i, ability.getWis());
			pstm.setInt(++i, ability.getBaseWis());
			pstm.setInt(++i, pc.getCurrentWeapon());
			pstm.setInt(++i, pc.getClassId());
			pstm.setInt(++i, pc.getGender().toInt());
			pstm.setInt(++i, pc.getType());
			pstm.setInt(++i, pc.getMoveState().getHeading());
			pstm.setInt(++i, pc.getX());
			pstm.setInt(++i, pc.getY());
			pstm.setInt(++i, pc.getMapId());
			pstm.setInt(++i, pc.getFood());
			pstm.setInt(++i, pc.getAlignment());
			pstm.setString(++i, pc.getTitle());
			pstm.setInt(++i, pc.getClanid());
			pstm.setString(++i, pc.getClanName());
			pstm.setInt(++i, pc.getBloodPledgeRank() != null ? pc.getBloodPledgeRank().toInt() : 0);
			pstm.setInt(++i, pc.getClanContribution());
			pstm.setInt(++i, pc.getClanWeekContribution());
			pstm.setString(++i, pc.getClanMemberNotes());
			pstm.setInt(++i, pc.getBonusStats());
			pstm.setInt(++i, pc.getElixirStats());
			pstm.setInt(++i, pc.getElfAttr());
			pstm.setInt(++i, pc.getPKcount());
			pstm.setLong(++i, pc.getExpRes());
			pstm.setInt(++i, pc.getPartnerId());
			pstm.setShort(++i, pc.getAccessLevel());
			pstm.setInt(++i, pc.getOnlineStatus());
			pstm.setInt(++i, pc.getHomeTownId());
			pstm.setInt(++i, pc.getContribution());
			pstm.setInt(++i, pc.getHellTime());
			pstm.setBoolean(++i, pc.isBanned());
			pstm.setInt(++i, pc.getKarma());
			pstm.setTimestamp(++i, pc.getLastPk());
			pstm.setTimestamp(++i, pc.getDeleteTime());
			pstm.setLong(++i, pc.getReturnStat_exp());		
			pstm.setInt(++i, pc.getBirthDay());
			pstm.setInt(++i, pc.getBookMarkCount());
			
			pstm.setInt(++i, pc.getSpecialWareHouseSize());
			pstm.setInt(++i, config == null ? 0 : config.getHuntPrice());
			pstm.setInt(++i, config == null ? 0 : config.getHuntCount());
			pstm.setString(++i, config == null ? null : config.getReasonToHunt());
			
			pstm.setInt(++i, pc.getRingSlotLevel());
			pstm.setInt(++i, pc.getEarringSlotLevel());
			pstm.setInt(++i, pc.getBadgeSlotLevel());
			pstm.setInt(++i, pc.getShoulderSlotLevel());
			
			pstm.setTimestamp(++i, pc.getEMETime());
			pstm.setTimestamp(++i, pc.getEMETime2());
			pstm.setTimestamp(++i, pc.getPUPLETime());
			pstm.setTimestamp(++i, pc.getTOPAZTime());
			
			pstm.setTimestamp(++i, pc.getEinhasadGraceTime());
			pstm.setInt(++i, pc.getEinPoint());
			pstm.setInt(++i, pc.getEinCurEnchantLevel());
			pstm.setInt(++i, pc.getEinTotalStat());
			
			pstm.setTimestamp(++i, skill == null ? null : skill.getThirdSkillDelay());
			pstm.setTimestamp(++i, skill == null ? null : skill.getFiveSkillDelay());
			pstm.setTimestamp(++i, pc.getSurvivalTime());
			
			pstm.execute();
			_log.finest("stored char data: " + pc.getName());
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	@Override
	public void deleteCharacter(String accountName, String charName) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters WHERE account_name=? AND char_name=?");
			pstm.setString(1, accountName);
			pstm.setString(2, charName);
			rs = pstm.executeQuery();
			int objid = 0;
			if (!rs.next()) {
				/*
				 * SELECT가 값을 돌려주지 않았다
				 * 존재하지 않는지, 혹은 다른 어카운트가 소유하고 있는 캐릭터명이 지정되었다고 하는 것이 된다.
				 */
				_log.warning("invalid delete char request: account=" + accountName + " char=" + charName);
				throw new RuntimeException("could not delete character");
			} else {
				objid = rs.getInt("objid");
			}
			
			if (objid == 0)
				throw new RuntimeException("could not delete character");
			SQLUtil.close(rs, pstm);
			
			// 캐릭터가 삭제될 때 관련 table delete
			pstm = con.prepareStatement(
					"DELETE T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23 "
							+ "FROM characters AS T1 "
							+ "LEFT JOIN character_buddys AS T2 ON T1.objid = T2.char_id "
							+ "LEFT JOIN character_buff AS T3 ON T1.objid = T3.char_obj_id "
							+ "LEFT JOIN character_config AS T4 ON T1.objid = T4.object_id "
							+ "LEFT JOIN character_items AS T5 ON T1.objid = T5.char_id "
							+ "LEFT JOIN character_quests AS T6 ON T1.objid = T6.char_id "
							+ "LEFT JOIN character_skills_active AS T7 ON T1.objid = T7.char_obj_id "
							+ "LEFT JOIN character_skills_passive AS T8 ON T1.objid = T8.char_obj_id "
							+ "LEFT JOIN character_teleport AS T9 ON T1.objid = T9.char_id "
							+ "LEFT JOIN character_revenge AS T10 ON T1.objid = T10.char_id "
							+ "LEFT JOIN character_einhasadstat AS T11 ON T1.objid = T11.objid "
							+ "LEFT JOIN character_hunting_quest AS T12 ON T1.objid = T12.objID "
							+ "LEFT JOIN character_death_exp AS T13 ON T1.objid = T13.char_id "
							+ "LEFT JOIN character_death_item AS T14 ON T1.objid = T14.char_id "
							+ "LEFT JOIN character_eventpush AS T15 ON T1.objid = T15.objId "
							+ "LEFT JOIN tj_coupon AS T16 ON T1.objid = T16.charId "
							+ "LEFT JOIN character_exclude AS T17 ON T1.objid = T17.char_id "
							+ "LEFT JOIN character_equipset AS T18 ON T1.objid = T18.charId "
							+ "LEFT JOIN character_beginner_quest AS T19 ON T1.objid = T19.charId "
							+ "LEFT JOIN character_favorbook AS T20 ON T1.objid = T20.charObjId "
							+ "LEFT JOIN character_timecollection AS T21 ON T1.objid = T21.charObjId "
							+ "LEFT JOIN character_einhasadfaith AS T22 ON T1.objid = T22.objId "
							+ "LEFT JOIN character_shop_limit AS T23 ON T1.objid = T23.characterId "
							+ "WHERE T1.objid = ?"
					);
			pstm.setInt(1, objid);
			pstm.execute();
			SQLUtil.close(pstm);
			
			// cashed delete
			RevengeTable.getInstance().removeInfo(objid);
			CharacterEinhasadStatTable.getInstance().removeInfo(objid);
			HuntingQuestUserTable.getInstance().removeInfo(objid);
			DeathPenaltyTable.getInstance().removeInfo(objid);
			EventPushLoader.getInstance().removeInfo(objid);
			TJCouponLoader.remove(objid);
			EquipSetTable.getInstance().delete(objid);
			BeginnerQuestUserTable.getInstance().removeInfo(objid);
			L1FavorBookUserLoader.getInstance().remove(objid);
			L1TimeCollectionUserLoader.getInstance().remove(objid);
			ClanJoinningTable.getInstance().delete(objid);
			/*pstm = con.prepareStatement("DELETE FROM character_monsterbooklist WHERE id = ?");
			pstm.setInt(1, objid);
			pstm.execute();
			SQLUtil.close(pstm);
			pstm = con.prepareStatement("DELETE FROM tb_user_monster_book WHERE char_id = ?");
			pstm.setInt(1, objid);
			pstm.execute();
			SQLUtil.close(pstm);
			pstm = con.prepareStatement("DELETE FROM tb_user_week_quest WHERE char_id = ?");
			pstm.setInt(1, objid);
			pstm.execute();
			SQLUtil.close(pstm);*/
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	@Override
	public void updateAccountName(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			int i = 0;
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET account_name=? WHERE objid=?");
			pstm.setString(++i, pc.getAccountName());
			pstm.setInt(++i, pc.getId());
			pstm.execute();
			_log.finest("update account data:" + pc.getName());
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	@Override
	public void storeCharacter(L1PcInstance pc) {
		L1Ability ability			= pc.getAbility();
		L1SkillStatus skill			= pc.getSkill();
		L1Fatigue fatigue			= pc.getFatigue();
		L1CharacterConfig config	= pc.getConfig();
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			int i = 0;
			con = L1DatabaseFactory.getInstance().getConnection();
			
			pstm = con.prepareStatement("UPDATE characters SET level=?, HighLevel=?, Exp=?, "
					+ "MaxHp=?, CurHp=?, MaxMp=?, CurMp=?, Ac=?, "
					+ "Str=?, BaseStr=?, Con=?, BaseCon=?, Dex=?, BaseDex=?, Cha=?, BaseCha=?, Intel=?, BaseIntel=?, Wis=?, BaseWis=?, Status=?, "
					+ "Class=?, gender=?, Type=?, " 
					+ "Heading=?, LocX=?, LocY=?, MapID=?, Food=?, Alignment=?, Title=?, "
					+ "ClanID=?, Clanname=?, ClanRank=?, ClanContribution=?, ClanWeekContribution=?, pledgeJoinDate=?, pledgeRankDate=?, " 
					+ "notes=?, BonusStatus=?, ElixirStatus=?, ElfAttr=?, PKcount=?, ExpRes=?, PartnerID=?, AccessLevel=?, " 
					+ "OnlineStatus=?, HomeTownID=?, Contribution=?, HellTime=?, Banned=?, Karma=?, LastPk=?," 
					+ "DeleteTime=?, ReturnStat=?, lastLogoutTime=NOW()," 
					+ "PC_Kill=?, PC_Death=?, Mark_Count=?,"
					+ "SpecialSize=?, HuntPrice=?, HuntCount=?, HuntText=?, "
					+ "RingAddSlot=?, EarringAddSlot=?, BadgeAddSlot=?, ShoulderAddSlot=?, "
					+ "fatigue_point=?, fatigue_rest_time=?, EMETime=?, EMETime2=?, PUPLETime=?, TOPAZTime=?, EinhasadGraceTime=?, "
					+ "EinPoint=?, EinCardLess=?, EinCardState=?, EinCardBonusValue=?, "
					+ "ThirdSkillTime=?, FiveSkillTime=?, SurvivalTime=?, "
					+ "potentialTargetId=?, potentialBonusGrade=?, potentialBonusId=? "
					+ "WHERE objid=?");
			pstm.setInt(++i, pc.getLevel());
			pstm.setInt(++i, pc.getHighLevel());
			pstm.setLong(++i, pc.getExp());
			int max_hp = pc.getBaseMaxHp();
			if (max_hp < 1) {
				max_hp = 1;
			}
			pstm.setInt(++i, max_hp);
			int hp = pc.getCurrentHp();
			if (hp < 1) {
				hp = 1;
			}
			pstm.setInt(++i, hp);
			pstm.setInt(++i, pc.getBaseMaxMp());
			pstm.setInt(++i, pc.getCurrentMp());
			pstm.setInt(++i, pc.getAC().getAc());
			pstm.setInt(++i, ability.getStr());
			pstm.setInt(++i, ability.getBaseStr());
			pstm.setInt(++i, ability.getCon());
			pstm.setInt(++i, ability.getBaseCon());
			pstm.setInt(++i, ability.getDex());
			pstm.setInt(++i, ability.getBaseDex());
			pstm.setInt(++i, ability.getCha());
			pstm.setInt(++i, ability.getBaseCha());
			pstm.setInt(++i, ability.getInt());
			pstm.setInt(++i, ability.getBaseInt());
			pstm.setInt(++i, ability.getWis());
			pstm.setInt(++i, ability.getBaseWis());
			pstm.setInt(++i, pc.getCurrentWeapon());
			pstm.setInt(++i, pc.getClassId());
			pstm.setInt(++i, pc.getGender().toInt());
			pstm.setInt(++i, pc.getType());
			pstm.setInt(++i, pc.getMoveState().getHeading());
			pstm.setInt(++i, pc.getX());
			pstm.setInt(++i, pc.getY());
			pstm.setInt(++i, pc.getMapId());
			pstm.setInt(++i, pc.getFood());
			pstm.setInt(++i, pc.getAlignment());
			pstm.setString(++i, pc.getTitle());
			pstm.setInt(++i, pc.getClanid());
			pstm.setString(++i, pc.getClanName());
			pstm.setInt(++i, pc.getBloodPledgeRank() != null ? pc.getBloodPledgeRank().toInt() : 0);
			pstm.setInt(++i, pc.getClanContribution());
			pstm.setInt(++i, pc.getClanWeekContribution());
			pstm.setInt(++i, pc.getPledgeJoinDate());
			pstm.setInt(++i, pc.getPledgeRankDate());
			pstm.setString(++i, pc.getClanMemberNotes());
			pstm.setInt(++i, pc.getBonusStats());
			pstm.setInt(++i, pc.getElixirStats());
			pstm.setInt(++i, pc.getElfAttr());
			pstm.setInt(++i, pc.getPKcount());
			pstm.setLong(++i, pc.getExpRes());
			pstm.setInt(++i, pc.getPartnerId());
			pstm.setShort(++i, pc.getAccessLevel());
			pstm.setInt(++i, pc.getOnlineStatus());
			pstm.setInt(++i, pc.getHomeTownId());
			pstm.setInt(++i, pc.getContribution());
			pstm.setInt(++i, pc.getHellTime());
			pstm.setBoolean(++i, pc.isBanned());
			pstm.setInt(++i, pc.getKarma());
			pstm.setTimestamp(++i, pc.getLastPk());
			pstm.setTimestamp(++i, pc.getDeleteTime());
			pstm.setLong(++i, pc.getReturnStat_exp());
			pstm.setInt(++i, pc.getKills());
			pstm.setInt(++i, pc.getDeaths());
			pstm.setInt(++i, pc.getBookMarkCount());
			
			pstm.setInt(++i, pc.getSpecialWareHouseSize());
			pstm.setInt(++i, config == null ? 0 : config.getHuntPrice());	
			pstm.setInt(++i, config == null ? 0 : config.getHuntCount());
			pstm.setString(++i, config == null ? null : config.getReasonToHunt());
			pstm.setInt(++i, pc.getRingSlotLevel());
			pstm.setInt(++i, pc.getEarringSlotLevel());
			pstm.setInt(++i, pc.getBadgeSlotLevel());
			pstm.setInt(++i, pc.getShoulderSlotLevel());
			
			pstm.setInt(++i, fatigue == null ? 0 : fatigue.getPoint());
			pstm.setTimestamp(++i, fatigue == null ? null : fatigue.getRestTime());
			
			pstm.setTimestamp(++i, pc.getEMETime());
			pstm.setTimestamp(++i, pc.getEMETime2());
			pstm.setTimestamp(++i, pc.getPUPLETime());
			pstm.setTimestamp(++i, pc.getTOPAZTime());
			
			pstm.setTimestamp(++i, pc.getEinhasadGraceTime());
			pstm.setInt(++i, pc.getEinPoint());
			pstm.setInt(++i, pc.getEinCurEnchantLevel());
			pstm.setInt(++i, pc.getEinTotalStat());
			pstm.setInt(++i, pc.getEinBonusCardOpenValue());
			
			pstm.setTimestamp(++i, skill == null ? null : skill.getThirdSkillDelay());
			pstm.setTimestamp(++i, skill == null ? null : skill.getFiveSkillDelay());
			pstm.setTimestamp(++i, pc.getSurvivalTime());
			
			pstm.setInt(++i, config == null ? 0 : config.get_potential_target_id());
			pstm.setInt(++i, config == null ? 0 : config.get_potential_bonus_grade());
			pstm.setInt(++i, config == null ? 0 : config.get_potential_bonus_id());
			
			pstm.setInt(++i, pc.getId());
			pstm.execute();
			_log.finest("stored char data:" + pc.getName());
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void storeMemo(L1PcInstance pc, String memo) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET Memo=? WHERE objid=?");
			pstm.setString(1, memo);
			pstm.setInt(2, pc.getId());
			pstm.execute();
			_log.finest("stored char data:" + pc.getName());
		} catch (SQLException e) {
			System.out.println(e);
			_log.log(Level.SEVERE, "MySqlCharacterStorage[]Error2", e);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public String getMemo(String name) {
		String memo = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT Memo FROM characters WHERE char_name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			if (rs.next()) {
				memo = rs.getString(1);
			}
		} catch (SQLException e) {
			System.out.println(e);
			_log.log(Level.SEVERE, "MySqlCharacterStorage[]Error2", e);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return memo;
	}
	

	@Override
	public L1FakePcInstance loadFakeCharacter(String charName) {
		L1FakePcInstance pc = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
			pstm.setString(1, charName);
			rs = pstm.executeQuery();
			if (!rs.next()) {
				return null;// SELECT가 결과를 돌려주지 않았다.
			}
			
			pc = new L1FakePcInstance();
			pc.setAccountName(rs.getString("account_name"));
			pc.setId(rs.getInt("objid"));
			pc.setName(rs.getString("char_name"));
			pc.setHighLevel(rs.getInt("HighLevel"));
			pc.setExp(rs.getInt("Exp"));
			pc.addBaseMaxHp(rs.getShort("MaxHp"));
			short currentHp = rs.getShort("CurHp");
			if(currentHp < 1)currentHp = 1;
			pc.setCurrentHp(currentHp);
			pc.setDead(false);
			pc.setActionStatus(0);
			pc.addBaseMaxMp(rs.getShort("MaxMp"));
			pc.setCurrentMp(rs.getShort("CurMp"));
			
			pc.getAbility().setBaseStr(rs.getByte("BaseStr"));
			pc.getAbility().setStr(rs.getByte("Str"));
			pc.getAbility().setBaseCon(rs.getByte("BaseCon"));
			pc.getAbility().setCon(rs.getByte("Con"));
			pc.getAbility().setBaseDex(rs.getByte("BaseDex"));
			pc.getAbility().setDex(rs.getByte("Dex"));
			pc.getAbility().setBaseCha(rs.getByte("BaseCha"));
			pc.getAbility().setCha(rs.getByte("Cha"));
			pc.getAbility().setBaseInt(rs.getByte("BaseIntel"));
			pc.getAbility().setInt(rs.getByte("Intel"));
			pc.getAbility().setBaseWis(rs.getByte("BaseWis"));
			pc.getAbility().setWis(rs.getByte("Wis"));
			
			int status = rs.getInt("Status");
			pc.setCurrentWeapon(status);
			int classId = rs.getInt("Class");
			pc.setClassId(classId);
			pc.setSpriteId(classId);
			pc.setGender(Gender.fromInt(rs.getInt("gender")));
			pc.setType(rs.getInt("Type"));
			int head = rs.getInt("Heading");
			if(head > 7)head = 0;
			pc.getMoveState().setHeading(head);
			pc.setX(rs.getInt("locX"));
			pc.setY(rs.getInt("locY"));
			pc.setMap(rs.getShort("MapID"));
			pc.setFood(rs.getInt("Food"));
			pc.setAlignment(rs.getInt("Alignment"));
			pc.setTitle(rs.getString("Title"));
			pc.setClanid(rs.getInt("ClanID"));
			pc.setClanName(rs.getString("Clanname"));
			pc.setBonusStats(rs.getInt("BonusStatus"));
			pc.setElixirStats(rs.getInt("ElixirStatus"));
			pc.setElfAttr(rs.getInt("ElfAttr"));
			pc.setPKcount(rs.getInt("PKcount"));
			pc.setExpRes(rs.getInt("ExpRes"));
			pc.setPartnerId(rs.getInt("PartnerID"));
			pc.setAccessLevel((short)0);
			pc.setGm(false);
			pc.setMonitor(false);
			pc.setOnlineStatus(rs.getInt("OnlineStatus"));
			pc.setHomeTownId(rs.getInt("HomeTownID"));
			pc.setContribution(rs.getInt("Contribution"));
			pc.setHellTime(rs.getInt("HellTime"));
			pc.setBanned(rs.getBoolean("Banned"));
			pc.setKarma(rs.getInt("Karma"));
			pc.setLastPk(rs.getTimestamp("LastPk"));
			pc.setDeleteTime(rs.getTimestamp("DeleteTime"));
			pc.setReturnStat_exp(rs.getInt("ReturnStat"));
			pc.setLastLoginTime(rs.getTimestamp("lastLoginTime"));		
			pc.setKills(rs.getInt("PC_Kill"));
			pc.setDeaths(rs.getInt("PC_Death"));
			pc.setBookMarkCount(rs.getInt("Mark_Count"));
			
			pc.setSpecialWareHouseSize(rs.getInt("SpecialSize"));//특수창고
			
			pc.refresh();
			pc.getMoveState().setMoveSpeed(0);
			pc.getMoveState().setBraveSpeed(0);
			pc.setGmInvis(false);

			_log.finest("restored char data: ");
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			pc = null;
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return pc;
	}
}

