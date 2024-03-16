package l1j.server.GameSystem.ai.brain.action;

import l1j.server.GameSystem.ai.brain.AiBrainHandler;
import l1j.server.GameSystem.ai.constuct.AiBrainStatus;
import l1j.server.server.model.Instance.L1AiUserInstance;

/**
 * AI default 핸들러
 * @author LinOffice
 */
public class None extends AiBrainHandler {
	public None(L1AiUserInstance ai) {
		super(ai);
	}
	
	@Override
	protected void action() throws Exception {
		if (_ai == null) {
			return;
		}
		switch (_ai.getAiType()) {
		case AI_BATTLE:
			_ai.setAiBrainStatus(AiBrainStatus.MOVE);
			_ai.setAiSleepTime(2000);
			break;
		default:
			break;
		}
	}
}

