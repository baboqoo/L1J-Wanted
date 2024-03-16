package l1j.server.QuestSystem.Compensator;

import java.sql.ResultSet;

import l1j.server.QuestSystem.Compensator.Element.BuffCompensator;
import l1j.server.QuestSystem.Compensator.Element.CompensatorElement;
import l1j.server.QuestSystem.Compensator.Element.ExpCompensator;
import l1j.server.QuestSystem.Compensator.Element.ItemCompensator;
import l1j.server.server.model.Instance.L1PcInstance;

public class NormalQuestCompensator implements QuestCompensator{
	public static final String		_table		= "tb_bookquest_compensate";
	
	private int 				_difficulty;
	private CompensatorElement 	_element;
	private String				_lastRecord;
	
	@Override
	public void set(ResultSet rs) throws Exception {
		int nTmp1	= 0;
		int nTmp2	= 0;
		int nTmp3	= 0;
		
		_lastRecord = "difficulty";
		_difficulty = rs.getInt(_lastRecord);
		
		_lastRecord = "type";
		String type = rs.getString(_lastRecord);
		
		_lastRecord = "num1";
		nTmp1		= rs.getInt(_lastRecord);
		
		_lastRecord = "num2";
		nTmp2		= rs.getInt(_lastRecord);
		
		if (type.equalsIgnoreCase("item")){
			_lastRecord = "id1";
			nTmp3	= rs.getInt(_lastRecord);
			_element = new ItemCompensator(nTmp3, nTmp1, nTmp2);
		} else if(type.equalsIgnoreCase("exp"))
			_element = new ExpCompensator(nTmp1, nTmp2);
		else if(type.equalsIgnoreCase("buff")){
			_lastRecord = "id1";
			nTmp3	= rs.getInt(_lastRecord);
			
			_lastRecord = "id2";
			int nTmp4 = rs.getInt(_lastRecord);
			_element = new BuffCompensator(nTmp3, nTmp1, nTmp2, nTmp4);
		} else
			throw new Exception("Invalid Type : " + type);
	}

	@Override
	public String getLastRecord() {
		return _lastRecord;
	}
	
	@Override
	public int getDifficulty(){
		return _difficulty;
	}

	@Override
	public void compensate(L1PcInstance pc) {
		if (_element != null)
			_element.compensate(pc);
	}

}

