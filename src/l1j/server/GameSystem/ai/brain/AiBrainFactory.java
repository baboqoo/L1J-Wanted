package l1j.server.GameSystem.ai.brain;

import javolution.util.FastMap;
import l1j.server.GameSystem.ai.constuct.AiBrainStatus;
import l1j.server.server.model.Instance.L1AiUserInstance;

/**
 * AI 액션 팩토리
 * @author LinOffice
 */
public class AiBrainFactory {
	private final L1AiUserInstance _owner;
	private final FastMap<AiBrainStatus, AiBrainHandler> _handlers;
	
	/**
	 * 생성자
	 * @param owner
	 */
	protected AiBrainFactory(L1AiUserInstance owner){
		_owner		= owner;
		_handlers	= put();
	}
	
	/**
	 * 액션 핸들러 조사
	 * @param status
	 * @return AiBrainHandler
	 */
	protected AiBrainHandler getHandler(AiBrainStatus status){
		return _handlers.get(status);
	}
	
	/**
	 * 액션 생성
	 * @return FastMap<AiBrainStatus, AiBrainHandler>
	 */
	FastMap<AiBrainStatus, AiBrainHandler> put(){
		FastMap<AiBrainStatus, AiBrainHandler> handlers	=	new FastMap<AiBrainStatus, AiBrainHandler>();
		switch (_owner.getAiType()) {
		case AI_BATTLE:
			handlers.put(AiBrainStatus.NONE, 				new l1j.server.GameSystem.ai.brain.action.None(_owner));
			handlers.put(AiBrainStatus.MOVE, 				new l1j.server.GameSystem.ai.brain.action.Move(_owner));
			handlers.put(AiBrainStatus.ATTACK, 				new l1j.server.GameSystem.ai.brain.action.Attack(_owner));
			handlers.put(AiBrainStatus.PICK_UP, 			new l1j.server.GameSystem.ai.brain.action.PickUp(_owner));
			break;
		default:
			return null;
		}
		return handlers;
	}
	
	/**
	 * 메모리 해제
	 */
	protected void release(){
		_handlers.clear();
	}
}

