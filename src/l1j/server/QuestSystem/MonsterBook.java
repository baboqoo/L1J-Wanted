package l1j.server.QuestSystem;

public class MonsterBook {
	public static final String[] _records = new String[]{
		"npc_id", 			"book_id", 			"book_step_first", 	"book_step_second", 
		"book_step_third", 	"book_clear_num2",	"tel_x", 			"tel_y", 			"tel_mapId",
		"week_difficulty",	"week_success_count"
	};
	
	public static final String _table = "select * from tb_monster_book inner join tb_monster_book_clearInfo on tb_monster_book.book_id=tb_monster_book_clearInfo.book_id";
	
	private int _npcId;
	private int _bookId;
	private int _bookStepFirst;
	private int _bookStepSecond;
	private int _bookStepThird;
	private int _bookClearNum;
	private int _weekDifficulty;
	private int _weekSuccessCount;
	private int _telX;
	private int _telY;
	private int _telMapId;
	
	public MonsterBook(){
		_weekDifficulty 	= -1;
		_weekSuccessCount 	= 0;
	}
	
	public void set(int idx, Object obj) throws Exception{
		switch(idx){
		case 0:
			setNpcId((int)obj);
			break;
		case 1:
			setBookId((int)obj);
			break;
		case 2:
			setStepFirst((int)obj);
			break;
		case 3:
			setStepSecond((int)obj);
			break;
		case 4:
			setStepThird((int)obj);
			break;
		case 5:
			setClearNum((int)obj);
			break;
		case 6:
			setTelX((int)obj);
			break;
		case 7:
			setTelY((int)obj);
			break;
		case 8:
			setTelMapId((int)obj);
			break;
		case 9:
			setWeekDifficulty(((int)obj) - 1);
			break;
		case 10:
			setWeekSuccessCount((int)obj);
			break;
		default:
			throw new Exception("MonsterBook set()... Invalid Index..." + idx);
		}
	}
	
	public void setNpcId(int i){
		_npcId = i;
	}
	
	public int getNpcId(){
		return _npcId;
	}
	
	public void setBookId(int i){
		_bookId = i;
	}
	
	public int getBookId(){
		return _bookId;
	}
	
	public void setStepFirst(int i){
		_bookStepFirst = i;
	}
	
	public int getStepFirst(){
		return _bookStepFirst;
	}
	
	public void setStepSecond(int i){
		_bookStepSecond = i;
	}
	
	public int getStepSecond(){
		return _bookStepSecond;
	}
	
	public void setStepThird(int i){
		_bookStepThird = i;
	}
	
	public int getStepThird(){
		return _bookStepThird;
	}
	
	public void setClearNum(int i){
		_bookClearNum = i;
	}
	
	public int getClearNum(){
		return _bookClearNum;
	}
	
	public void setWeekDifficulty(int i){
		_weekDifficulty = i;
	}
	
	public int getWeekDifficulty(){
		return _weekDifficulty;
	}
	
	public void setWeekSuccessCount(int i){
		_weekSuccessCount = i;
	}
	
	public int getWeekSuccessCount(){
		return _weekSuccessCount;
	}
	
	public void setTelX(int i){
		_telX = i;
	}
	
	public int getTelX(){
		return _telX;
	}
	
	public void setTelY(int i){
		_telY = i;
	}
	
	public int getTelY(){
		return _telY;
	}
	
	public void setTelMapId(int i){
		_telMapId = i;
	}
	
	public int getTelMapId(){
		return _telMapId;
	}
	
	public int getLevelToClearNum(int i){
		switch(i){
		case 1:
			return getStepFirst();
		case 2:
			return getStepSecond();
		case 3:
			return getStepThird();
		}
		
		return -1;
	}
}

