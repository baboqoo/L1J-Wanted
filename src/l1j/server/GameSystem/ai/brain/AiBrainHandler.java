package l1j.server.GameSystem.ai.brain;

import l1j.server.server.model.Instance.L1AiUserInstance;

/**
 * AI 추상 액션 핸들러
 * @author LinOffice
 */
public abstract class AiBrainHandler {
	protected final L1AiUserInstance _ai;
	
	/**
	 * 부모 생성자
	 * @param ai
	 */
	protected AiBrainHandler(L1AiUserInstance ai){
		_ai = ai;
	}
	
	/**
	 * 액션 실행
	 * @throws Exception
	 */
	protected abstract void action() throws Exception;
}

