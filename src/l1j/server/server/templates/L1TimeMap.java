/**
 * 타이머 관련 맵 객체
 * 2008. 12. 04
*/

package l1j.server.server.templates;

public class L1TimeMap{
	private int id, time, doorId;

	/** 
	 * 기본 생성자
	 * @param	(int)	id		맵 아이디
	 * @param	(int)	time	설정된 시간(s)
	*/
	public L1TimeMap(int id, int time){
		this(id, time, 0);
	}
	/** 
	 * 기본 생성자
	 * @param	(int)	id		맵 아이디
	 * @param	(int)	time	설정된 시간(s)
	 * @param	(int)	DoorId	설정된 문 아이디
	*/
	public L1TimeMap(int id, int time, int doorId){
		this.id		= id;
		this.time	= time;
		this.doorId	= doorId;
	}
	/** 
	 * 맵 아이디 리턴
	 * @return	(int)	맵 아이디
	*/
	public int getId(){
		return id;
	}
	/** 
	 * 설정 시간 리턴
	 * @return	(int)	설정시간
	*/
	public int getTime(){
		return time;
	}
	/** 
	 * 설정된 문 아이디 리턴
	 * @return	(int)	문 아이디
	*/
	public int getDoor(){
		return doorId;
	}
	/** 
	 * 남은시간 계산
	 * @return	(boolean)	시간이 종료되었으면 true, 남아있다면 false
	*/
	public boolean count(){
		return time-- <= 0;
	}
}
