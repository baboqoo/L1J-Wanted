package l1j.server.server.controller.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.GameClient;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.construct.L1CharacterInfo.L1Class;
import l1j.server.server.model.L1CharacterConfig;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.ranking.S_MyRanking;
import l1j.server.server.serverpackets.ranking.S_TopRanker;
import l1j.server.server.serverpackets.ranking.S_TopRankerNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti.SkillIconNotiType;
import l1j.server.server.templates.L1UserRanking;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 랭킹 시스템 컨트롤러
 * @author LinOffice
 */
public class UserRanking implements ControllerInterface {
	private static Logger _log = Logger.getLogger(UserRanking.class.getName());
	private static UserRanking _instance;
	public static UserRanking getInstance() {
		if (_instance == null) {
			_instance = new UserRanking();
		}
		return _instance;
	}
	
	private static long _version;
	private static Timestamp LOAD_TIME;
	private static String LOAD_TIME_FORMAT;
	
	public static String getLoadTime() {
		return LOAD_TIME_FORMAT;
	}
	
	private static java.util.LinkedList<L1UserRanking> list				= new java.util.LinkedList<>();
	private static java.util.LinkedList<L1UserRanking> listPrince		= new java.util.LinkedList<>();
	private static java.util.LinkedList<L1UserRanking> listKnight		= new java.util.LinkedList<>();
	private static java.util.LinkedList<L1UserRanking> listElf			= new java.util.LinkedList<>();
	private static java.util.LinkedList<L1UserRanking> listWizard		= new java.util.LinkedList<>();
	private static java.util.LinkedList<L1UserRanking> listDarkElf		= new java.util.LinkedList<>();
	private static java.util.LinkedList<L1UserRanking> listDragonKnight	= new java.util.LinkedList<>();
	private static java.util.LinkedList<L1UserRanking> listIllusionist	= new java.util.LinkedList<>();
	private static java.util.LinkedList<L1UserRanking> listWarrior		= new java.util.LinkedList<>();
	private static java.util.LinkedList<L1UserRanking> listFencer		= new java.util.LinkedList<>();
	private static java.util.LinkedList<L1UserRanking> listLancer		= new java.util.LinkedList<>();
	
	private static final Object LOCK = new Object();
	
	private static HashMap<Integer, java.util.LinkedList<S_TopRanker>> top_ranker_ack = new HashMap<>();
	
	/**
	 * 랭킹 리스트 출력(버튼 클릭)
	 * @param pc
	 * @param class_id
	 * @param version
	 */
	public static void send_top_ranker_ack(L1PcInstance pc, int class_id, long version) {
		if (!Config.RANKING.RANKING_SYSTEM_ACTIVE) {
			pc.sendPackets(S_TopRanker.NOW_NOT_SERVICE);
			return;
		}
		if (_version == version) {
			pc.sendPackets(new S_TopRanker(null, 0, 0, 0, version, S_TopRanker.RankResultCode.RC_NO_CHANGE), true);
			return;
		}
		java.util.LinkedList<S_TopRanker> list = top_ranker_ack.get(class_id);
		if (list == null || list.isEmpty()) {
			pc.sendPackets(S_TopRanker.NOW_NOT_SERVICE);
			return;
		}
		for (S_TopRanker ack : list) {
			pc.sendPackets(ack);
		}
	}
	
	/**
	 * 랭킹 정보 알림(로그인)
	 * @param client
	 * @param name
	 */
	public static void send_top_ranker_noti(GameClient client, String name) {
		if (!Config.RANKING.RANKING_SYSTEM_ACTIVE) {
			return;
		}
		L1UserRanking rank = getTotalRank(name);
		if (rank == null) {
			return;
		}
	    S_TopRankerNoti rankPck = new S_TopRankerNoti(rank);
	    client.sendPacket(rankPck);
	    rankPck.clear();
	    rankPck = null;
	}
	
	/**
	 * 랭킹 정보 알림(갱신)
	 * @param pc
	 */
	public static void send_top_ranker_noti(L1PcInstance pc) {
		if (!Config.RANKING.RANKING_SYSTEM_ACTIVE) {
			return;
		}
		pc.sendPackets(new S_TopRankerNoti(pc), true);
	}
	
	/**
	 * 나의 랭킹 출력(스테이터스 폼, 텔레포트)
	 * @param pc
	 * @param version
	 */
	public static void send_my_ranking_ack(L1PcInstance pc, long version) {
		if (pc.isGm()) {// 관리자는 랭킹에 집계 되지 않는다.
			return;
		}
		if (!Config.RANKING.RANKING_SYSTEM_ACTIVE) {
			pc.sendPackets(S_MyRanking.NOW_NOT_SERVICE_MY);
			return;
		}
		if (_version == version) {
			pc.sendPackets(S_MyRanking.NO_CHANGE_MY);
			return;
		}
		if (Config.RANKING.RANKING_INCLUDE_LEVEL > pc.getLevel() || pc.getConfig().get_all_rank() == 0) {
			pc.sendPackets(S_MyRanking.NO_RANK_MY);
			return;
		}
		if (pc.getConfig().get_my_rank_ack() == null) {
			update_my_rank_ack(pc);
		}
		pc.sendPackets(pc.getConfig().get_my_rank_ack());
	}
	
	/**
	 * 랭킹 순위 리스트 패킷 갱신
	 */
 	static void update_top_ranker_ack() {
 		// 기존 데이터 파기
 		if (!top_ranker_ack.isEmpty()) {
 			for (java.util.LinkedList<S_TopRanker> list : top_ranker_ack.values()) {
 				for (S_TopRanker val : list) {
 					val.close();
 				}
 				list.clear();
 			}
 			top_ranker_ack.clear();
 		}
 		// 새로운 데이터 갱신
 		for (int classId = L1CharacterInfo.CLASS_SIZE; classId >= 0; classId--) {
 			java.util.LinkedList<S_TopRanker> val = top_ranker_ack.get(classId);
 			if (val == null) {
				val = new java.util.LinkedList<S_TopRanker>();
				top_ranker_ack.put(classId, val);
	 		}
 			java.util.LinkedList<L1UserRanking> list = getList(classId);
 			int list_size = list.size();
 			// 1페이지당 100개씩 분류된다.
 			if (list_size > 100) {
 				List<L1UserRanking> page1_list	= list.subList(0, 100);
 				List<L1UserRanking> page2_list	= list.subList(100, list.size());
 				val.add(new S_TopRanker(page1_list, classId, 2, 1, _version, S_TopRanker.RankResultCode.RC_SUCCESS));
 				val.add(new S_TopRanker(page2_list, classId, 2, 2, _version, S_TopRanker.RankResultCode.RC_SUCCESS));
 			} else {
 				val.add(new S_TopRanker(list, classId, 1, 1, _version, S_TopRanker.RankResultCode.RC_SUCCESS));
 			}
 		}
	}
	
	/**
	 * 나의 랭킹 패킷 갱신
	 * @param pc
	 */
	static void update_my_rank_ack(L1PcInstance pc) {
		pc.getConfig().reset_my_rank_ack();
		pc.getConfig().set_my_rank_ack(new S_MyRanking(pc, _version, S_MyRanking.RankMyResultCode.RC_SUCCESS));
	}
	
	public static java.util.LinkedList<L1UserRanking> getListFromString(String val){
		synchronized (LOCK) {
			switch(val) {
			case "prince":		return listPrince;
			case "knight":		return listKnight;
			case "elf":			return listElf;
			case "wizard":		return listWizard;
			case "darkElf":		return listDarkElf;
			case "dragonKnight":return listDragonKnight;
			case "illusionist":	return listIllusionist;
			case "warrior":		return listWarrior;
			case "fencer":		return listFencer;
			case "lancer":		return listLancer;
			default:			return list;
			}
		}
	}
	
	/**
	 * 클래스 랭킹 rating 조사
	 * @param curRank
	 * @return class_rating
	 */
	public static int getClassRankRating(int curRank) {
		return curRank == 1 ? 7 : 0;
	}
	
	/**
	 * 전체 랭킹 rating 조사
	 * @param curRank
	 * @return rating
	 */
	public static int getRankRating(int curRank){
		if (curRank == 1) {
			return 9;
		}
		if (curRank == 2) {
			return 8;
		}
		if (curRank == 3) {
			return 7;
		}
		if (curRank >= 4 && curRank <= 10) {
			return 6;
		}
		if (curRank >= 11 && curRank <= 20) {
			return 5;
		}
		if (curRank >= 21 && curRank <= 30) {
			return 4;
		}
		if (curRank >= 31 && curRank <= 50) {
			return 3;
		}
		if (curRank >= 51 && curRank <= 60) {
			return 2;
		}
		if (curRank >= 61 && curRank <= 100) {
			return 1;
		}
		return 0;
	}
	
	private UserRanking(){
		_version = System.currentTimeMillis();
		if (LOAD_TIME == null) {
			LOAD_TIME			= new Timestamp(_version);
			LOAD_TIME_FORMAT	= StringUtil.getFormatDate(LOAD_TIME);
		} else {
			LOAD_TIME.setTime(_version);
			LOAD_TIME_FORMAT	= StringUtil.getFormatDate(LOAD_TIME);
		}
		load();
		update_top_ranker_ack();
	}

	@Override
	public void execute() {
		do_update();
	}
	
	@Override
	public void execute(L1PcInstance pc) {
	}
	
	void do_update() {
		if (!Config.RANKING.RANKING_SYSTEM_ACTIVE) {
			return;
		}
		try {
			load();
			LoggerInstance.getInstance().addRank(list);
			
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null || pc.getNetConnection() == null || pc.isGm()) {
					continue;
				}
				int rating = getTotalRating(pc.getName());
				int classlangking = getClassCurrentRank(pc.getType(), pc.getName());
				if (classlangking != 1 && pc.getInventory().checkItem(6670 + pc.getType())) {
					pc.getInventory().consumeItem(6670 + pc.getType(), 1);
				}
				if (classlangking != 2 && pc.getInventory().checkItem(6690 + pc.getType())) {
					pc.getInventory().consumeItem(6690 + pc.getType(), 1);
				}
				if (classlangking != 3 && pc.getInventory().checkItem(6710 + pc.getType())) {
					pc.getInventory().consumeItem(6710 + pc.getType(), 1);
				}
				if (pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_1)) {
					if (rating != 9) {
						setBonus(pc, L1SkillId.RANKING_BUFF_1, false);
					}
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_2)) {
					if (rating != 8) {
						setBonus(pc, L1SkillId.RANKING_BUFF_2, false);
					}
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_3)) {
					if (rating != 7) {
						setBonus(pc, L1SkillId.RANKING_BUFF_3, false);
					}
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_4_10)) {
					if (rating != 6) {
						setBonus(pc, L1SkillId.RANKING_BUFF_4_10, false);
					}
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_11_20)) {
					if (rating != 5) {
						setBonus(pc, L1SkillId.RANKING_BUFF_11_20, false);
					}
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_21_30)) {
					if (rating != 4) {
						setBonus(pc, L1SkillId.RANKING_BUFF_21_30, false);
					}
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_31_60)) {
					if (rating != 3 && rating != 2) {
						setBonus(pc, L1SkillId.RANKING_BUFF_31_60, false);
					}
				} else if (pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_61_100)) {
					if (rating != 1) {
						setBonus(pc, L1SkillId.RANKING_BUFF_61_100, false);
					}
				}
				
				if (rating != 0 
						&& !pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_1) 
						&& !pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_2) 
						&& !pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_3) 
						&& !pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_4_10) 
						&& !pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_11_20) 
						&& !pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_21_30) 
						&& !pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_31_60) 
						&& !pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_61_100)) {
					setBuffSetting(pc);
				} else if (rating == 0) {
					pc.getConfig().init_rank();
				}
				send_top_ranker_noti(pc);
			}
			
			update_top_ranker_ack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void setBonus(L1PcInstance pc, int spell_id, boolean flag) {
		switch (spell_id) {
		case L1SkillId.RANKING_BUFF_1:// 1위
			pc.addMaxHp(flag ? 300 : -300);
			pc.getAC().addAc(flag ? -4 : 4);
			pc.getAbility().addPVPDamageReduction(flag ? 3 : -3);
			pc.getAbility().addPVPDamage(flag ? 3 : -3);
			setStatBuff(pc, flag ? 1 : -1);
			if (flag) {
				if (!pc.getInventory().checkItem(5558)) {
					pc.getInventory().storeItem(5558, 1);
				}
			} else {
				pc.getInventory().consumeItem(5558, 1);
			}
			pc._isWeightNoActionPenalty = flag;
			break;
		case L1SkillId.RANKING_BUFF_2:// 2위
			pc.addMaxHp(flag ? 250 : -250);
			pc.getAC().addAc(flag ? -4 : 4);
			pc.getAbility().addPVPDamageReduction(flag ? 2 : -2);
			pc.getAbility().addPVPDamage(flag ? 2 : -2);
			setStatBuff(pc, flag ? 1 : -1);
			if (flag) {
				if (!pc.getInventory().checkItem(5558)) {
					pc.getInventory().storeItem(5558, 1);
				}
			} else {
				pc.getInventory().consumeItem(5558, 1);
			}
			pc._isWeightNoActionPenalty = flag;
			break;
		case L1SkillId.RANKING_BUFF_3:// 3위
			pc.addMaxHp(flag ? 250 : -250);
			pc.getAC().addAc(flag ? -3 : 3);
			pc.getAbility().addPVPDamageReduction(flag ? 2 : -2);
			pc.getAbility().addPVPDamage(flag ? 2 : -2);
			setStatBuff(pc, flag ? 1 : -1);
			if (flag) {
				if (!pc.getInventory().checkItem(5558)) {
					pc.getInventory().storeItem(5558, 1);
				}
			} else {
				pc.getInventory().consumeItem(5558, 1);
			}
			pc._isWeightNoActionPenalty = flag;
			break;
		case L1SkillId.RANKING_BUFF_4_10:// 4 ~ 10위
			pc.addMaxHp(flag ? 200 : -200);
			pc.getAC().addAc(flag ? -3 : 3);
			pc.getAbility().addPVPDamageReduction(flag ? 1 : -1);
			pc.getAbility().addPVPDamage(flag ? 2 : -2);
			setStatBuff(pc, flag ? 1 : -1);
			break;
		case L1SkillId.RANKING_BUFF_11_20:// 11 ~ 20위
			pc.addMaxHp(flag ? 200 : -200);
			pc.getAC().addAc(flag ? -3 : 3);
			pc.getAbility().addPVPDamageReduction(flag ? 1 : -1);
			pc.getAbility().addPVPDamage(flag ? 2 : -2);
			break;
		case L1SkillId.RANKING_BUFF_21_30:// 21 ~ 30위
			pc.addMaxHp(flag ? 200 : -200);
			pc.getAC().addAc(flag ? -3 : 3);
			pc.getAbility().addPVPDamageReduction(flag ? 1 : -1);
			pc.getAbility().addPVPDamage(flag ? 2 : -2);
			break;
		case L1SkillId.RANKING_BUFF_31_60:// 31 ~ 60위
			pc.addMaxHp(flag ? 200 : -200);
			pc.getAC().addAc(flag ? -3 : 3);
			pc.getAbility().addPVPDamageReduction(flag ? 1 : -1);
			pc.getAbility().addPVPDamage(flag ? 1 : -1);
			break;
		case L1SkillId.RANKING_BUFF_61_100:// 61 ~ 100위
			pc.add_exp_boosting_ratio(flag ? 10 : -10);
			pc.addMaxHp(flag ? 200 : -200);
			pc.getAC().addAc(flag ? -3 : 3);
			pc.getAbility().addPVPDamageReduction(flag ? 1 : -1);
			break;
		}
		if (flag) {
			pc.getSkill().setSkillEffect(spell_id, -1);
		} else {
			pc.getSkill().killSkillEffectTimer(spell_id);
		}
		pc.sendPackets(new S_SpellBuffNoti(flag ? SkillIconNotiType.RESTAT : SkillIconNotiType.END, spell_id, pc.getType(), -1), true);
		pc.sendPackets(new S_OwnCharStatus(pc), true);
	}
	
	public static java.util.LinkedList<L1UserRanking> getList(int classId) {
		synchronized (LOCK) {
			switch(classId){
			case 10:return list;
			case 0:return listPrince;
			case 1:return listKnight;
			case 2:return listElf;
			case 3:return listWizard;
			case 4:return listDarkElf;
			case 5:return listDragonKnight;
			case 6:return listIllusionist;
			case 7:return listWarrior;
			case 8:return listFencer;
			case 9:return listLancer;
			default:return null;
			}
		}
	}
	
	public static L1UserRanking getTotalRank(String name){
		synchronized (LOCK) {
			for (L1UserRanking user : list) {
				if (user.getName().equalsIgnoreCase(name)) {
					return user;
				}
		    }
			return null;
		}
	}
	
	public static L1UserRanking getClassRank(int classId, String name){
		java.util.LinkedList<L1UserRanking> list = getList(classId);
		if (list == null || list.isEmpty()) {
			return null;
		}
		for (L1UserRanking rank : list) {
			if (rank.getName().equalsIgnoreCase(name)) {
				return rank;
			}
		}
		return null;
	}
	
	/**
	 * 전채 랭킹 rating 조사
	 * @param name
	 * @return rating
	 */
	public static int getTotalRating(String name) {
		L1UserRanking rank = getTotalRank(name);
		if (rank != null) {
			return getRankRating(rank.getCurRank());
		}
		return 0;
	}
	
	/**
	 * 클래스 랭킹의 순위 조사
	 * @param classId
	 * @param name
	 * @return 순위
	 */
	public static int getClassCurrentRank(int classId, String name) {
		L1UserRanking rank = getClassRank(classId, name);
		if(rank != null){
			int curRank = rank.getCurRank();
			if (curRank >= 1 && curRank <= 10) {
				return curRank;
			}
		}
		return 0;
	}
	
	public void setBuffSetting(L1PcInstance pc) {
		L1UserRanking rank = getTotalRank(pc.getName());
		if (rank != null) {
			int curRank					= rank.getCurRank();
			L1UserRanking classRank		= getClassRank(rank.getClassId(), rank.getName());
			L1CharacterConfig config	= pc.getConfig();
			config.set_all_rank(curRank);
			config.set_previous_all_rank(rank.getOldRank());
			config.set_class_rank(classRank.getCurRank());
			config.set_previous_class_rank(classRank.getOldRank());
			config.set_ranker_rating(getRankRating(curRank));
			config.set_class_ranker_rating(getClassRankRating(classRank.getCurRank()));
			update_my_rank_ack(pc);
			
			// 클래스 랭커 가호
			if (classRank.getCurRank() == 1 && !pc.getInventory().checkItem(6670 + pc.getType())) {
				pc.getInventory().storeItem(6670 + pc.getType(), 1);
			}
			if (classRank.getCurRank() == 2 && !pc.getInventory().checkItem(6690 + pc.getType())) {
				pc.getInventory().storeItem(6690 + pc.getType(), 1);
			}
			if (classRank.getCurRank() == 3 && !pc.getInventory().checkItem(6710 + pc.getType())) {
				pc.getInventory().storeItem(6710 + pc.getType(), 1);
			}
			
			if (curRank == 1) {
				setBonus(pc, L1SkillId.RANKING_BUFF_1, true);
			} else if (curRank == 2) {
				setBonus(pc, L1SkillId.RANKING_BUFF_2, true);
			} else if (curRank == 3) {
				setBonus(pc, L1SkillId.RANKING_BUFF_3, true);
			} else if (curRank >= 4 && curRank <= 10) {
				setBonus(pc, L1SkillId.RANKING_BUFF_4_10, true);
			} else if (curRank >= 11 && curRank <= 20) {
				setBonus(pc, L1SkillId.RANKING_BUFF_11_20, true);
			} else if (curRank >= 21 && curRank <= 30) {
				setBonus(pc, L1SkillId.RANKING_BUFF_21_30, true);
			} else if (curRank >= 31 && curRank <= 60) {
				setBonus(pc, L1SkillId.RANKING_BUFF_31_60, true);
			} else if (curRank >= 61 && curRank <= 100) {
				setBonus(pc, L1SkillId.RANKING_BUFF_61_100, true);
			}
		} else {
			pc.getConfig().init_rank();
		}
		if (!pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_1)
				&& !pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_2)
				&& !pc.getSkill().hasSkillEffect(L1SkillId.RANKING_BUFF_3)) {
			pc.getInventory().consumeItem(5558, 1);
		}
	}
		
	private void setStatBuff(L1PcInstance pc, int flag) {
		if (pc.isElf()) {
			pc.getAbility().addAddedDex(1 * flag);
		} else if (pc.isWizard() || pc.isIllusionist()) {
			pc.getAbility().addAddedInt(1 * flag);
		} else {
			pc.getAbility().addAddedStr(1 * flag);
		}
	}
	
	/**
	 * 순위에 해당하는 경험치 조사
	 * @param ranking
	 * @return exp
	 */
	public long getRankingExp(int ranking){
		long exp = 0;
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT MIN(A.Exp) AS getExp FROM (SELECT Exp FROM characters WHERE AccessLevel=0 ORDER BY Exp DESC LIMIT ?) A");
			pstm.setInt(1, ranking);
			rs		= pstm.executeQuery();
			if (rs.next()) {
				exp = (long)rs.getInt("getExp");
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return exp;
	}

	private void load() {
		java.util.LinkedList<L1UserRanking> templist				= new java.util.LinkedList<L1UserRanking>();
		java.util.LinkedList<L1UserRanking> templistPrince			= new java.util.LinkedList<L1UserRanking>();
		java.util.LinkedList<L1UserRanking> templistKnight			= new java.util.LinkedList<L1UserRanking>();
		java.util.LinkedList<L1UserRanking> templistElf				= new java.util.LinkedList<L1UserRanking>();
		java.util.LinkedList<L1UserRanking> templistWizard			= new java.util.LinkedList<L1UserRanking>();
		java.util.LinkedList<L1UserRanking> templistDarkElf			= new java.util.LinkedList<L1UserRanking>();
		java.util.LinkedList<L1UserRanking> templistDragonKnight	= new java.util.LinkedList<L1UserRanking>();
		java.util.LinkedList<L1UserRanking> templistIllusionist		= new java.util.LinkedList<L1UserRanking>();
		java.util.LinkedList<L1UserRanking> templistWarrior			= new java.util.LinkedList<L1UserRanking>();
		java.util.LinkedList<L1UserRanking> templistFencer			= new java.util.LinkedList<L1UserRanking>();
		java.util.LinkedList<L1UserRanking> templistLancer			= new java.util.LinkedList<L1UserRanking>();
		for (int a = L1CharacterInfo.CLASS_SIZE; a >= 0; a--) {
			Connection con			= null;
			PreparedStatement pstm	= null;
			ResultSet rs			= null;
			int i = 0;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				if (a == L1CharacterInfo.CLASS_SIZE) {
					pstm = con.prepareStatement("SELECT char_name, Type FROM characters WHERE level >= " + Config.RANKING.RANKING_INCLUDE_LEVEL + " AND AccessLevel = 0 ORDER BY Exp DESC LIMIT " + Config.RANKING.RANKING_TOTAL_CALC_RANGE);
				} else {
					pstm = con.prepareStatement("SELECT char_name, Type FROM characters WHERE Type = " + a + " AND level >= " + Config.RANKING.RANKING_INCLUDE_LEVEL + " AND AccessLevel = 0 ORDER BY Exp DESC LIMIT " + Config.RANKING.RANKING_CLASS_CALC_RANGE);
				}
				rs = pstm.executeQuery();
				while(rs.next()){
					String name = rs.getString("char_name");
					int type = rs.getInt("Type");
					L1UserRanking rank = new L1UserRanking();
					rank.setName(name);
					rank.setCurRank(++i);
					
					L1UserRanking oldRank = a == L1CharacterInfo.CLASS_SIZE ? getTotalRank(name) : getClassRank(a, name);
					rank.setOldRank(oldRank == null ? rank.getCurRank() : oldRank.getCurRank());
					rank.setClassId(type);
					
					if (a == L1Class.CROWN.getType()) {
						templistPrince.add(rank);
					} else if (a == L1Class.KNIGHT.getType()) {
						templistKnight.add(rank);
					} else if (a == L1Class.ELF.getType()) {
						templistElf.add(rank);
					} else if (a == L1Class.WIZARD.getType()) {
						templistWizard.add(rank);
					} else if (a == L1Class.DARKELF.getType()) {
						templistDarkElf.add(rank);
					} else if (a == L1Class.DRAGONKNIGHT.getType()) {
						templistDragonKnight.add(rank);
					} else if (a == L1Class.ILLUSIONIST.getType()) {
						templistIllusionist.add(rank);
					} else if (a == L1Class.WARRIOR.getType()) {
						templistWarrior.add(rank);
					} else if (a == L1Class.FENCER.getType()) {
						templistFencer.add(rank);
					} else if (a == L1Class.LANCER.getType()) {
						templistLancer.add(rank);
					} else {
						templist.add(rank);
					}
					
					if (a != L1CharacterInfo.CLASS_SIZE) {
						for (L1UserRanking subRank : templist) {
							if (subRank.getName().equals(rank.getName())) {
								rank.setSubCurRank(subRank.getCurRank());
								rank.setSubOldRank(subRank.getOldRank());
								break;
							}
						}
					}
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
		}
		
		synchronized (LOCK) {
			list.clear();
			listPrince.clear();
			listKnight.clear();
			listElf.clear();
			listWizard.clear();
			listDarkElf.clear();
			listDragonKnight.clear();
			listIllusionist.clear();
			listWarrior.clear();
			listFencer.clear();
			listLancer.clear();
			
			list.addAll(templist);
			listPrince.addAll(templistPrince);
			listKnight.addAll(templistKnight);
			listElf.addAll(templistElf);
			listWizard.addAll(templistWizard);
			listDarkElf.addAll(templistDarkElf);
			listDragonKnight.addAll(templistDragonKnight);
			listIllusionist.addAll(templistIllusionist);
			listWarrior.addAll(templistWarrior);
			listFencer.addAll(templistFencer);
			listLancer.addAll(templistLancer);
		}
	}
	
	public static void reload() {
		_version = System.currentTimeMillis();
		if (LOAD_TIME == null) {
			LOAD_TIME			= new Timestamp(_version);
			LOAD_TIME_FORMAT	= StringUtil.getFormatDate(LOAD_TIME);
		} else {
			LOAD_TIME.setTime(_version);
			LOAD_TIME_FORMAT	= StringUtil.getFormatDate(LOAD_TIME);
		}
		getInstance().do_update();
	}

}

