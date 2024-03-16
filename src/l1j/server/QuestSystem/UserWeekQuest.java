package l1j.server.QuestSystem;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import l1j.server.QuestSystem.Compensator.WeekQuestCompensator;
import l1j.server.QuestSystem.Loader.MonsterBookCompensateLoader;
import l1j.server.QuestSystem.Loader.WeekQuestLoader;
import l1j.server.QuestSystem.Templates.UserWeekQuestProgress;
import l1j.server.QuestSystem.Templates.WeekQuestDateCalculator;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_WeekQuest;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.BytesOutputStream;

public class UserWeekQuest {
//AUTO SRM: 	private static final S_SystemMessage _updateMessage = new S_SystemMessage("주간 퀘스트가 갱신되었습니다. 재접속을 하여 혜택을 받으세요."); // CHECKED OK
	private static final S_SystemMessage _updateMessage = new S_SystemMessage(S_SystemMessage.getRefText(67), true);
	
	private L1PcInstance 				_owner;
	private UserWeekQuestProgress[][] 	_wq;
	private Object						_lock;
	
	public UserWeekQuest(L1PcInstance pc){
		_owner 	= pc;
		_wq 	= new UserWeekQuestProgress[][]{
				{null, null, null},
				{null, null, null},
				{null, null, null},
		};
		
		_lock = new Object();
	}
	
	/** 주간퀘스트 정보를 매핑한다. **/
	public void setWeekQuestInformation(ResultSet rs) throws Exception{
		int bookId; 
		int difficulty; 
		int section; 
		int step;
		Timestamp stamp;
		boolean isCompleted;
		
		// 먼저 db정보를 긁어온다.
		while (rs.next()){
			bookId 		= rs.getInt("bookId"); 
			difficulty 	= rs.getInt("difficulty"); 
			section		= rs.getInt("section"); 
			step		= rs.getInt("step");
			stamp		= rs.getTimestamp("stamp");
			isCompleted = rs.getBoolean("completed");
			
			_wq[difficulty - 1][section] = new UserWeekQuestProgress(bookId, difficulty, section, step, stamp, isCompleted);
		}
		
		MonsterBook book					= null;
		WeekQuestDateCalculator wqcal 		= WeekQuestDateCalculator.getInstance();
		WeekQuestLoader wqLoader			= WeekQuestLoader.getInstance();
		
		// 로딩된 몬스터 북을 검사한다.
		for (int i = 0; i < 3; i++){			
			// 만약 로딩된 정보가 없다면, 새로 할당한다.
			if (_wq[i][0] == null){
				for (int j = 0; j < 3; j++)
					_wq[i][j] = new UserWeekQuestProgress(0, 0, 0, 0, null, false);
			}
			
			// 각 난이도 별 0번을 기준으로 하여, 업데이트가 되어야 한다면, 주간 퀘스트를 갱신한다.
			if (wqcal.isUpdateWeekQuest(_wq[i][0].getStamp())){
				for (int j = 0; j < 3; j++){
					book = wqLoader.getBook(i, j);
					// 새로운 북 정보를 갱신한다.
					_wq[i][j].setBookId(book.getBookId());
					_wq[i][j].setDifficulty(i + 1);
					_wq[i][j].setSection(j);
					_wq[i][j].setStamp(wqcal.getUpdateStamp());
					_wq[i][j].setStep(0);				
					_wq[i][j].setCompleted(false);
				}
			}
		}
	}
	
	public byte[] getSerialize() throws Exception{
		BytesOutputStream mbos = new BytesOutputStream();
		byte[] section1 = null;
		byte[] section2 = null;
		byte[] section3 = null;
		int successfully = 0;
		
		mbos.write(0x20);
		mbos.write(0x37);
		for (int i = 0; i < 3; i++){
			successfully 	= 1;
			section1 		= _wq[i][0].getSerialize();
			section2 		= _wq[i][1].getSerialize();
			section3 		= _wq[i][2].getSerialize();
			
			mbos.write(0x12);
			mbos.write(section1.length + section2.length + section3.length + 4);
			mbos.write(0x08);
			mbos.write(i);
			mbos.write(0x18);
			
			// 라인이 클리어 상태라면, 보상 버튼 활성화
			if (isLineClear(i)){
				successfully = 3;
				
				// 이미 보상을 받았다면 라인 클리어 상태로,
				if (isLineCompleted(i))
					successfully = 5;
			}
			mbos.write(successfully);
			mbos.write(section1);
			mbos.write(section2);
			mbos.write(section3);
		}
		byte[] b = mbos.toArray();
		mbos.close();
		return b;
	}
	
	/** 섹션별로 복수의 북id가 있다면 true, 아닐 경우 false. **/
	public boolean checkDuplicateBookId(MonsterBook book, int difficulty, int section) throws Exception{
		if (difficulty < 0 || difficulty > 2)
			throw new Exception("invalid difficulty " + difficulty);
		
		for (int i=section - 1; i >= 0; i--){
			if (_wq[difficulty][i].getBookId() == book.getBookId())
				return true;
		}
		return false;
	}
	
	/** 난이도 별 주퀘 리스트를 반환한다. **/
	public UserWeekQuestProgress[] getProgressList(int difficulty){
		UserWeekQuestProgress[] progresses = new UserWeekQuestProgress[3];
		progresses[0] = _wq[difficulty][0];
		progresses[1] = _wq[difficulty][1];
		progresses[2] = _wq[difficulty][2];
		
		return progresses;
	}
	
	/** 모든 주퀘 리스트를 반환한다. **/
	public ArrayList<UserWeekQuestProgress> getProgressList(){
		ArrayList<UserWeekQuestProgress> list 	= new ArrayList<UserWeekQuestProgress>(9);
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++)
				list.add(_wq[i][j]);
		}
		
		return list;
	}
	
	/** 주퀘 리스트를 전송한다. **/
	public void sendList(){
		// 주퀘 리스트 전송
		S_WeekQuest wq = new S_WeekQuest();
		wq.writeWQList(_owner);
		_owner.sendPackets(wq, true);
	}
	
	/** 텔레포트 요청에 응한다. **/
	public void teleport(int difficulty, int section){
		if (difficulty < 0 || difficulty > 2)
			return;
		
		_owner.getQuest().getMonsterBook().teleport(_wq[difficulty][section].getBookId());
	}
	
	/** 몬스터를 사냥했다. **/
	public void addMonster(MonsterBook book){
		if (book == null)
			return;
		
		UserWeekQuestProgress progress 	= null;
		int difficulty					= book.getWeekDifficulty();
		if (difficulty < 0 || difficulty > 2)
			return;
		
		if (WeekQuestDateCalculator.getInstance().isUpdateWeekQuest(_wq[difficulty][0].getStamp())){
			_owner.sendPackets(_updateMessage, false);
			return;
		}
		
		// 이미 라인이 클리어 됐거나, 보상을받았다면 리턴,
		if (isLineClear(difficulty) || isLineCompleted(difficulty))
			return;
		
		for (int i = 0; i < 3; i++){
			progress = _wq[difficulty][i];
			if (progress.getBookId() != book.getBookId())
				continue;
			
			synchronized(_lock){
				progress.addStep(1);
				
				// 갱신된 퀘스트 상태를 전송한다.
				S_WeekQuest wq = new S_WeekQuest();
				wq.writeWQUpdate(difficulty, i, progress.getStep());
				_owner.sendPackets(wq);
				
				// 퀘스트 완료 되었다면, 완료 메시지를 전송한다.
				if (isLineClear(difficulty)){
					wq = new S_WeekQuest();
					wq.writeWQLineClear(difficulty, 3);
					_owner.sendPackets(wq);			
				}
				return;
			}
		}
	}
	
	/** 보상을 실행한다. **/
	public void complete(int difficulty, int section){
		synchronized(_lock){
			if (difficulty < 0 || difficulty > 2)
				return;
			
			if (WeekQuestDateCalculator.getInstance().isUpdateWeekQuest(_wq[difficulty][0].getStamp())){
				_owner.sendPackets(_updateMessage, false);
				return;
			}
			
			// 라인을 클리어 하지 못했거나, 라인 보상을 받았다면 처리하지 않는다.
			if (!isLineClear(difficulty) || isLineCompleted(difficulty)){
				StringBuilder sb = new StringBuilder(128);
				//sb.append("이미 주간 퀘스트 보상을 받은 사용자 : ").append(_owner.getName()).append("이(가) 보상 시도를 합니다.");
				sb.append("User who has already received weekly quest reward: ").append(_owner.getName()).append(" attempting reward.");
				System.out.println(sb.toString());
				return;
			}
			_wq[difficulty][0].setCompleted(true);
			_wq[difficulty][1].setCompleted(true);
			_wq[difficulty][2].setCompleted(true);
			S_WeekQuest wq = new S_WeekQuest();
			wq.writeWQLineClear(difficulty, 5);
			_owner.sendPackets(wq);
			
			WeekQuestCompensator compensator = MonsterBookCompensateLoader.getInstance().getWeekCompensator(section);
			compensator.compensate(_owner);
		}
	}
	
	/** 라인이 모두 클리어 됐는지 **/
	public boolean isLineClear(int difficulty){
		if (_wq[difficulty][0].isClear() && _wq[difficulty][1].isClear() && _wq[difficulty][2].isClear())
			return true;
		return false;
	}
	
	/** 라인 보상을 받았는지 **/
	public boolean isLineCompleted(int difficulty){
		if (_wq[difficulty][0].isCompleted() || _wq[difficulty][1].isCompleted() || _wq[difficulty][2].isCompleted())
			return true;
		return false;		
	}
}


