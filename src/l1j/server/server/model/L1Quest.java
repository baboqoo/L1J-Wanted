package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestCollectItem;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestDropItem;
import l1j.server.GameSystem.beginnerquest.bean.L1QuestProgress;
import l1j.server.QuestSystem.UserMonsterBook;
import l1j.server.QuestSystem.UserWeekQuest;
import l1j.server.common.bin.quest.QuestT;
import l1j.server.common.bin.quest.QuestT.ObjectiveTypeT;
import l1j.server.server.construct.L1BeginnerQuest;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LoginUnknown;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.quest.S_QuestFinish;
import l1j.server.server.serverpackets.quest.S_QuestProgressUpdateNoti;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class L1Quest {
	private static Logger _log = Logger.getLogger(L1Quest.class.getName());

	public static final int QUEST_LEVEL15 = 1;
	public static final int QUEST_LEVEL30 = 2;
	public static final int QUEST_LEVEL45 = 3;
	public static final int QUEST_LEVEL50 = 4;

	public static final int QUEST_LYRA = 10;
	public static final int QUEST_OILSKINMANT = 11;

	public static final int QUEST_DOROMOND = 20;
	public static final int QUEST_RUBA = 21;
	public static final int QUEST_AREX = 22;

	public static final int QUEST_LUKEIN1 = 23;
	public static final int QUEST_TBOX1 = 24;
	public static final int QUEST_TBOX2 = 25;
	public static final int QUEST_TBOX3 = 26;
	public static final int QUEST_SIMIZZ = 27;
	public static final int QUEST_DOIL = 28;
	public static final int QUEST_RUDIAN = 29;
	public static final int QUEST_RESTA = 30;
	public static final int QUEST_CADMUS = 31;
	public static final int QUEST_KAMYLA = 32;
	public static final int QUEST_CRYSTAL = 33;
	public static final int QUEST_LIZARD = 34;
	public static final int QUEST_KEPLISHA = 35;
	public static final int QUEST_DESIRE = 36;
	public static final int QUEST_SHADOWS = 37;
	public static final int QUEST_ROI = 38;
	public static final int QUEST_MOONBOW = 39;
	public static final int QUEST_FIRSTQUEST = 40; // ## A70 말하는 두루마리 퀘스트 추가
	public static final int QUEST_ICEQUEENRING = 41;

	// 리뉴얼 퀘스트
	public static final int QUEST_HIDDENVALLEY = 43; // 숨겨진 계곡 초보자 도우미
	public static final int QUEST_HIGHDAILY = 44; // 토벌 대원 일일 퀘스트
	public static final int QUEST_HPASS = 45; // 기초 훈련 교관 서브 퀘스트
	public static final int QUEST_HIGHPASS = 46; // 최종 훈련 심사원 서브 퀘스트
	public static final int QUEST_HIGHDAILYB = 47; // 드래곤뼈 수집꾼 일일 퀘스트
	
	public static final int QUEST_RUN = 55; // 낡은 고서
	
	// 스냅퍼 방지/귀걸이
	public static final int QUEST_SLOT76 = 60;
	public static final int QUEST_SLOT81 = 61;
	public static final int QUEST_SLOT59 = 62;
		
	public static final int QUEST_SAI_RUNE70 = 63;

	public static final int QUEST_SLOT70	= 64;
	public static final int QUEST_SLOT83	= 65;
	public static final int QUEST_HAMO		= 80;
	
	public static final int QUEST_LEVEL95	= 95;
	public static final int QUEST_LEVEL100	= 100;
	
	public static final int QUEST_END		= 100;

	private L1PcInstance _owner = null;
	private HashMap<Integer, Integer> _quest = null;

	public L1Quest(L1PcInstance owner) {
		_owner = owner;
	}

	public int getStep(int quest_id) {
		if (_quest == null) {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			try {
				_quest = new HashMap<Integer, Integer>();
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT * FROM character_quests WHERE char_id=?");
				pstm.setInt(1, _owner.getId());
				rs = pstm.executeQuery();
				while(rs.next()) {
					_quest.put(new Integer(rs.getInt(2)), new Integer(rs.getInt(3)));
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(rs, pstm, con);
			}
		}
		Integer step = _quest.get(new Integer(quest_id));
		if (step == null) {
			return 0;
		} else {
			return step.intValue();
		}
	}

	public void setStep(int quest_id, int step) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			if (_quest.get(new Integer(quest_id)) == null) {
				pstm = con.prepareStatement("INSERT INTO character_quests SET char_id = ?, quest_id = ?, quest_step = ?");
				pstm.setInt(1, _owner.getId());
				pstm.setInt(2, quest_id);
				pstm.setInt(3, step);
				pstm.execute();
			} else {
				pstm = con.prepareStatement("UPDATE character_quests SET quest_step = ? WHERE char_id = ? AND quest_id = ?");
				pstm.setInt(1, step);
				pstm.setInt(2, _owner.getId());
				pstm.setInt(3, quest_id);
				pstm.execute();
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(pstm, con);

		}
		_quest.put(new Integer(quest_id), new Integer(step));
	}

	public void addStep(int quest_id, int add) {
		int step = getStep(quest_id);
		step += add;
		setStep(quest_id, step);
	}

	public void setEnd(int quest_id) {
		setStep(quest_id, QUEST_END);
	}

	public boolean isEnd(int quest_id) {
		if (getStep(quest_id) == QUEST_END) {
			return true;
		}
		return false;
	}
	
	/** 몬스터 북 **/
	private UserMonsterBook _monsterBook;
	public void setMonsterBook(UserMonsterBook book) {
		_monsterBook = book;
	}
	public UserMonsterBook getMonsterBook() {
		return _monsterBook;
	}
	
	/** 주간 퀘스트 **/
	private UserWeekQuest _weekQuest;
	public void setWeekQuest(UserWeekQuest quest) {
		_weekQuest = quest;
	}
	public UserWeekQuest getWeekQuest() {
		return _weekQuest;
	}
	
	/** 기사단 퀘스트 **/
	public Object syncQuest = new Object();
	private ConcurrentHashMap<Integer, L1QuestProgress> questProgressList = new ConcurrentHashMap<>();
	public ConcurrentHashMap<Integer, L1QuestProgress> getQuestProgressList(){
		return questProgressList;
	}
	public L1QuestProgress getQuestProgress(int id) {
		return questProgressList.get(id);
	}
	public void putQuestProgress(int questId, L1QuestProgress progress){
		if (questProgressList.containsKey(questId)) {
			return;
		}
		questProgressList.put(questId, progress);
	}
	
	/**
	 * 퀘스트 활성화 유효성 검사
	 * @param questId
	 * @return L1QuestInfo
	 */
	private L1QuestProgress activeValidationAndGet(int questId){
		L1QuestProgress progress = getQuestProgress(questId);
		// 퀘스트가 없거나 이미 완료된 퀘스트
		if (progress == null || progress.getFinishTime() != 0) {
			return null;
		}
		QuestT.PrerequisiteT.LevelT questLevel = progress.getBin().get_Prerequisite().get_Level();
		// 퀘스트 진행 레벨보다 낮은 경우
		if (questLevel.get_Minimum() > _owner.getLevel()) {
			return null;
		}
		// 퀘스트 진랭 레벨보다 높은 경우
		if (questLevel.get_Maximum() < _owner.getLevel()) {
			questSkip(progress);
			return null;
		}
		return progress;
	}
	
	/**
	 * 퀘스트를 완료 시킨다.
	 * @param info
	 */
	public void questSkip(L1QuestProgress progress){
		long currentTime	= System.currentTimeMillis();
		if (progress.getStartTime() == 0) {
			progress.setStartTime(currentTime);
		}
		progress.setFinishTime(currentTime);
		
		QuestT.ObjectiveListT requireList = progress.getBin().get_ObjectiveList();
		if (requireList != null && requireList.get_Objective() != null) {
			for (QuestT.ObjectiveT require : requireList.get_Objective()) {
				progress.setQuantity(require.get_ID(), require.get_RequiredQuantity());
			}
		}
		_owner.sendPackets(new S_QuestFinish(S_QuestFinish.eResultCode.FAIL_ALREADY_FINISHED, progress.getQuestId()));
	}
	
	/**
	 * 퀘스트 진행
	 * @param questId
	 */
	public void questProcess(int questId){
		L1QuestProgress progress = activeValidationAndGet(questId);
		if (progress == null) {
			return;
		}
		QuestT.ObjectiveListT requireList = progress.getBin().get_ObjectiveList();
		if (requireList != null && requireList.get_Objective() != null) {
			for (QuestT.ObjectiveT require : requireList.get_Objective()) {
				progress.setQuantity(require.get_ID(), require.get_RequiredQuantity());
			}
		}
		_owner.sendPackets(new S_QuestProgressUpdateNoti(progress), true);
	}
	
	/**
	 * 퀘스트 진행(아이템 사용)
	 * @param itemId
	 */
	public void questItemUse(int itemId){
		switch(itemId){
		case 40030:questProcess(L1BeginnerQuest.ITEM_USE);break;// 기사단의 속도향상물약
		case 60718:questProcess(L1BeginnerQuest.ARMOR_ENCHANT);break;// 기사단의 갑옷 마법 주문서
		case 60717:questProcess(L1BeginnerQuest.WEAPON_ENCHANT);break;// 기사단의 무기 마법 주문서
		case 410517:questProcess(L1BeginnerQuest.OPEN_EYE);break;// 기사단의 랜턴
		case 40096:questProcess(L1BeginnerQuest.POLY_USE);break;// 기사단의 변신주문서
		case 40095:questProcess(L1BeginnerQuest.RETRUN_PAPER);break;// 기사단의 귀환 주문서
		case 41245:questProcess(L1BeginnerQuest.RESOLV_USE);break;// 용해제
		case 410515:questProcess(L1BeginnerQuest.DOLL_USE);break;// 기사단의 마법인형
		default:break;
		}
	}
	
	/**
	 * 퀘스트 진행(레벨업)
	 * @param level
	 */
	public void questLevelup(int level) {
		try {
			if (Config.QUEST.BEGINNER_QUEST_ACTIVE && level <= Config.QUEST.BEGINNER_QUEST_LIMIT_LEVEL) {
				if (questProgressList.isEmpty()) {
					return;
				}
				
				// 레벨업시 현재 진행 중인 퀘스트의 유효성 검사를 같이 진행한다.
				for (L1QuestProgress progress : questProgressList.values()) {
					// 이미 완료된 퀘스트
					if (progress.getFinishTime() != 0) {
						continue;
					}
					QuestT.PrerequisiteT.LevelT levelT = progress.getBin().get_Prerequisite().get_Level();
					// 최소 진행 레벨보다 낮은 경우
					if (levelT.get_Minimum() > _owner.getLevel()) {
						continue;
					}
					// 최대 진행 레벨보다 높은 경우
					if (levelT.get_Maximum() < _owner.getLevel()) {
						questSkip(progress);
						continue;
					}
					
					// 레벨 달성 퀘스트
					QuestT.ObjectiveT require = progress.getBin().get_ObjectiveList().get_Objective().getFirst();
					if (require == null || require.get_Type() != ObjectiveTypeT.REACH_LEVEL) {
						continue;
					}
					questLevelProgress(progress, level, require.get_RequiredQuantity());
				}
				_owner.sendPackets(S_LoginUnknown.QUEST_UNKNOWN_2);
				_owner.sendPackets(S_LoginUnknown.QUEST_UNKNOWN_4);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 퀘스트 완료(레벨업)
	 * @param info
	 * @param level
	 * @param requiredLevel
	 */
	private void questLevelProgress(L1QuestProgress progress, int level, int requiredLevel){
		if (progress.getQuantity(1) >= requiredLevel) {
			return;
		}
		int quantity = level > requiredLevel ? requiredLevel : level;
		if (quantity <= 0) {
			return;
		}
		progress.setQuantity(1, quantity);
		_owner.sendPackets(new S_QuestProgressUpdateNoti(progress), true);
	}
	
	/**
	 * 퀘스트 진행(아이템 드랍)
	 * @param monster
	 * @param drop
	 */
	public void questDropItem(L1MonsterInstance monster, L1QuestDropItem drop){
		if (drop.getMainQuestId() != 0) {
			questDropItem(monster, drop.getMainQuestId(), drop.getMainItemNameId());
		}
		if (drop.getSubQuestId() != 0) {
			questDropItem(monster, drop.getSubQuestId(), drop.getSubItemNameId());
		}
	}
	
	/**
	 * 퀘스트 진행(아이템 드랍)
	 * @param monster
	 * @param questId
	 * @param createItemNameid
	 */
	private void questDropItem(L1MonsterInstance monster, int questId, int createItemNameid){
		if (activeValidationAndGet(questId) == null) {// 퀘스트 활성화 검사
			return;
		}

		L1Item item = ItemTable.getInstance().findItemByNameId(createItemNameid);
		if (item == null) {
			System.out.println(String.format("[L1Quest] QUEST_COLLECT_ITEM_TEMPLATE_EMPTY : NAME_ID(%d), QUEST_ID(%d)", createItemNameid, questId));
			return;
		}
		L1QuestCollectItem collectItem = item.getQuestCollectItem();
		if (collectItem == null) {
			System.out.println(String.format("[L1Quest] NOT_QUEST_COLLECT_ITEM : NAME_ID(%d), QUEST_ID(%d)", createItemNameid, questId));
		}
		L1PcInventory inv = _owner.getInventory();
		int current_count = inv.checkItemCount(item.getItemId());
		if (current_count >= collectItem.getRequiredQuantity()) {// 목표 달성 체크
			return;
		}
		int quantity = Config.QUEST.BEGINNER_QUEST_FAST_PROGRESS ? collectItem.getRequiredQuantity() : 2;
		if (quantity <= 0) {
			return;
		}
		L1ItemInstance result = inv.storeItem(item.getItemId(), quantity);
		if (result == null) {
			return;
		}
		_owner.sendPackets(new S_ServerMessage(813, monster.getNpcTemplate().getDesc(), result.getLogNameRef(quantity), _owner.getName()), true);
	}
	
	/**
	 * 퀘스트 아이템 수집
	 * @param collectItem
	 * @param count
	 */
	public void questCollectItem(L1QuestCollectItem collectItem, int count) {
		if (collectItem == null) {
			return;
		}
		L1QuestProgress progress = _owner.getQuest().getQuestProgress(collectItem.getQuestId());
		if (progress == null || progress.getFinishTime() != 0) {
			return;
		}
		int quantity = count > collectItem.getRequiredQuantity() ? collectItem.getRequiredQuantity() : count;
		if (quantity <= 0) {
			return;
		}
		progress.setQuantity(collectItem.getIndex(), quantity);
		_owner.sendPackets(new S_QuestProgressUpdateNoti(progress), true);
	}
}

