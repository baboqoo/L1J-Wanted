package l1j.server.QuestSystem.Compensator.Element;

import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 보상을 실행하는 인터페이스
 * **/
public interface CompensatorElement {
	
	/** 보상을 실행한다. **/
	public void compensate(L1PcInstance pc);
}

