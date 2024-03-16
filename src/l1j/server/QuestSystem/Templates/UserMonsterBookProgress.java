package l1j.server.QuestSystem.Templates;

import l1j.server.QuestSystem.MonsterBook;
import l1j.server.QuestSystem.Loader.MonsterBookLoader;

public class UserMonsterBookProgress {
	/** 북 id **/
	private int _bookId;
	
	/** 현재 진행 레벨 **/
	private int _level;
	
	/** 현재 잡은 수 **/
	private int _step;
	
	/** 보상 받은 단계 **/
	private int _completed;
	
	public UserMonsterBookProgress(int bookId, int level, int step, int completed){
		_bookId 	= bookId;
		_level		= level;
		_step		= step;
		_completed	= completed;
	}

	public void setBookId(int i){
		_bookId = i;
	}
	
	public void setLevel(int i){
		_level = i;
	}
	
	public void addLevel(int i){
		setLevel(getLevel() + i);
	}
	
	public void setStep(int i){
		_step = i;
		
		MonsterBook book = MonsterBookLoader.getInstance().getTemplate(getBookId());
		if (book == null)
			return;
		
		if (book.getStepThird() <= _step)
			setLevel(3);
		else if (book.getStepSecond() <= _step)
			setLevel(2);
		else
			setLevel(1);
	}
	
	public void addStep(int i){
		setStep(getStep() + i);
	}
	
	public void setCompleted(int i){
		_completed = i;
	}
	
	public int getBookId(){
		return _bookId;
	}
	
	public int getLevel(){
		return _level;
	}
	
	public int getStep(){
		return _step;
	}
	
	public int getCompleted(){
		return _completed;
	}
	
	public boolean isCompleted(int level){
		if (_completed - level >= 0)
			return true;
		return false;
	}
}

