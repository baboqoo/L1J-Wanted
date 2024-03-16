package l1j.server.QuestSystem;

import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.QuestSystem.Compensator.NormalQuestCompensator;
import l1j.server.QuestSystem.Loader.MonsterBookCompensateLoader;
import l1j.server.QuestSystem.Loader.MonsterBookLoader;
import l1j.server.QuestSystem.Templates.UserMonsterBookProgress;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_AchievementCompleteNoti;
import l1j.server.server.serverpackets.S_AchievementTeleport;
import l1j.server.server.serverpackets.S_AddCompletedAchievementBatch;
import l1j.server.server.serverpackets.S_AddCriteriaProgressBatch;
import l1j.server.server.serverpackets.S_CompletedAchievementReward;
import l1j.server.server.serverpackets.S_CriteriaUpdateNoti;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.utils.DelaySender;

public class UserMonsterBook {
	private HashMap<Integer, UserMonsterBookProgress> 	_mb;
	private L1PcInstance 								_owner;
	private Object										_lock;
	
	public UserMonsterBook(L1PcInstance pc){
		_owner 	= pc;
		_mb		= new HashMap<Integer, UserMonsterBookProgress>(10);
		_lock	= new Object();
	}
	
	public void setMonsterBook(int bookid, UserMonsterBookProgress progress){
		_mb.put(bookid, progress);
	}
	
	public ArrayList<UserMonsterBookProgress> getProgressList(){
		ArrayList<UserMonsterBookProgress> list = new ArrayList<UserMonsterBookProgress>(_mb.size());
		list.addAll(_mb.values());
		return list;
	}
	
	public void addMonster(MonsterBook book){
		if (book == null)
			return;

		int bookId = book.getBookId();
		synchronized(_lock){			
			UserMonsterBookProgress progress = _mb.get(bookId);
			
			if (progress == null){
				progress = new UserMonsterBookProgress(bookId, 1, 1, 0);
				setMonsterBook(bookId, progress);
				_owner.sendPackets(new S_CriteriaUpdateNoti(bookId, progress.getStep()), true);
			} else {
				progress.addStep(1);
				_owner.sendPackets(new S_CriteriaUpdateNoti(bookId, progress.getStep()), true);
							
				// 각각 달성이 완료되었을 때,
				if (progress.getStep() == book.getStepThird() || progress.getStep() == book.getStepSecond() || progress.getStep() == book.getStepFirst()){
					
					if (bookId >= 558) //번호 오류로 인해 추가
						_owner.sendPackets(new S_AchievementCompleteNoti(bookId + 10, progress.getLevel() + 4), true);
					else
					    _owner.sendPackets(new S_AchievementCompleteNoti(bookId, progress.getLevel() + 4), true);
					progress.setLevel(progress.getLevel() + 1);
				}
			}
		}
	}
	
	public void teleport(int bookId){
		MonsterBook book = MonsterBookLoader.getInstance().getTemplate(bookId);
		if (book == null)
			return;
		
		if (!_owner.getInventory().checkItem(140100, 1)){
			_owner.sendPackets(new S_AchievementTeleport(S_AchievementTeleport.eResultCode.TELEPORT_FAIL_NOT_ENOUGH_ADENA, bookId), true);
			return;
		}
		
		L1Location baseloc 		= new L1Location(book.getTelX(), book.getTelY(), book.getTelMapId());
		L1Location loc			= L1Location.randomLocation(baseloc, 0, 5, true);
		_owner.getInventory().consumeItem(140100, 1);
		_owner.getTeleport().start(loc, _owner.getMoveState().getHeading(), true);
	}
	
	public void complete(int bookId){
		UserMonsterBookProgress progress = null;
		progress = _mb.get(bookId);
		
		if (progress == null)
			return;
		MonsterBook book = null;
		book = MonsterBookLoader.getInstance().getTemplate(progress.getBookId());
		if (book == null)
			return;
		
		if (progress.getCompleted() >= 3)
			return;
		
		switch (progress.getCompleted()){
		case 0:
			if (book.getStepFirst() > progress.getStep())
				return;
			break;
		case 1:
			if (book.getStepSecond() > progress.getStep())
				return;
			break;
		case 2:
			if (book.getStepThird() > progress.getStep())
				return;
			break;
		}
		
		if (bookId >= 558)
			_owner.sendPackets(new S_AchievementCompleteNoti(progress.getBookId() + 10, 0x05 + progress.getCompleted()), true);
		else
			_owner.sendPackets(new S_AchievementCompleteNoti(progress.getBookId(), 0x05 + progress.getCompleted()), true);
			
		if (bookId >= 558)
			book = MonsterBookLoader.getInstance().getTemplate(progress.getBookId() + 10);
		_owner.sendPackets(new S_CompletedAchievementReward(S_CompletedAchievementReward.eResultCode.REQUEST_REWARD_SUCCESS, book.getClearNum() + progress.getCompleted()), true);
			
		progress.setCompleted(progress.getCompleted() + 1);
		
		ArrayList<NormalQuestCompensator> list = MonsterBookCompensateLoader.getInstance().getNormalCompensators(progress.getCompleted());
		int listSize = list.size();
		for (int i = 0; i < listSize; i++)
			list.get(i).compensate(_owner);
		_owner.sendPackets(new S_Effect(_owner.getId(), 3944), true);	
	}
	
	public void sendList(){
		UserMonsterBookProgress progress 		= null;
		ArrayList<UserMonsterBookProgress> list = getProgressList();
		MonsterBook book						= null;
		int size								= list.size();
		int level								= 0;
		int clearNum							= 0;
		_owner.sendPackets(new S_AddCompletedAchievementBatch(0, 0, null), true);
		
		java.util.LinkedList<S_AddCriteriaProgressBatch.CriteriaProgress> criteria_progress = null;
		S_AddCriteriaProgressBatch.CriteriaProgress criteria = null;
		DelaySender ds = new DelaySender(_owner.getNetConnection());
		
		for (int i = 0; i < size; i++){
			progress 	= list.get(i);
			level 		= progress.getLevel();
			
			book = MonsterBookLoader.getInstance().getTemplate(progress.getBookId());
			if (book == null) {
				continue;
			}
			
			clearNum 	= book.getClearNum();
			
			if (criteria_progress == null) {
				criteria_progress = new java.util.LinkedList<S_AddCriteriaProgressBatch.CriteriaProgress>();
			}
			criteria = new S_AddCriteriaProgressBatch.CriteriaProgress(progress.getBookId(), progress.getStep());
			criteria_progress.add(criteria);

			for (int j = 0; j < level - 1; j++){
				if (progress.getBookId() >= 558)
					ds.add(new S_AchievementCompleteNoti(progress.getBookId() + 10, 0x05 + j));
				else
					ds.add(new S_AchievementCompleteNoti(progress.getBookId(), 0x05 + j));
				
				if (progress.isCompleted(j + 1)){
					if (progress.getBookId() >= 558)
						ds.add(new S_CompletedAchievementReward(S_CompletedAchievementReward.eResultCode.REQUEST_REWARD_SUCCESS, clearNum + 30 + j));
					else
						ds.add(new S_CompletedAchievementReward(S_CompletedAchievementReward.eResultCode.REQUEST_REWARD_SUCCESS, clearNum + j));
				}
			}
		}
		_owner.sendPackets(new S_AddCriteriaProgressBatch(0, 0, criteria_progress), true);
		if (criteria_progress != null) {
			criteria_progress.clear();
			criteria_progress = null;
		}
		ds.run();
	}
	
	public static int bookIdCalculator(int i) {
		int result = (i / 3) + (i % 3 == 0 ? 0 : 1);
		return result;
	}
}

