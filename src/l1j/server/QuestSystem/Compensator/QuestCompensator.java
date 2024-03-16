package l1j.server.QuestSystem.Compensator;

import java.sql.ResultSet;

import l1j.server.server.model.Instance.L1PcInstance;

/** 하나의 보상 묶음을 일반화 시켜줄 인터페이스. **/
public interface QuestCompensator {
	/** 인스턴스 세팅을 자동화 해줄 메서드  **/
	public void 	set(ResultSet rs) throws Exception;
	
	/** 오류가 난 레코드 항목을 반환 **/
	public String	getLastRecord();
	
	/** 난이도를 반환한다. **/
	public int getDifficulty();
	
	/** 보상을 실행한다. **/
	public void compensate(L1PcInstance pc);
}

