package l1j.server.QuestSystem.Compensator.Element;

import l1j.server.Config;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1PcInstance;

public class ExpCompensator implements CompensatorElement{
	private int _exp;
	private int _compLevel;
	
	public ExpCompensator(int exp, int compLevel){
		_exp 		= exp;
		_compLevel 	= compLevel;
	}

	@Override
	public void compensate(L1PcInstance pc) {
		try {
			if (_exp <= 0)
				return;
			
			_exp -= _compLevel;
			double exppenalty = ExpTable.getPenalty(pc.getLevel());
			pc.addExp((int)(_exp * Config.RATE.RATE_XP * exppenalty));
			
			pc.save();
			pc.refresh();
			pc.setExpRes(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getExp(){
		return _exp;
	}
}

