package l1j.server.GameSystem.ai.brain.action;

import l1j.server.GameSystem.ai.brain.AiBrainHandler;
import l1j.server.GameSystem.ai.constuct.AiBrainStatus;
import l1j.server.GameSystem.astar.World;
import l1j.server.server.model.Instance.L1AiUserInstance;

/**
 * AI 이동 담당 핸들러
 * @author LinOffice
 */
public class Move extends AiBrainHandler {
	public Move(L1AiUserInstance ai) {
		super(ai);
	}
	
	@Override
	protected void action() throws Exception {
		if (_ai == null) {
			return;
		}
		if (_ai.noTargetTeleport()) {
			return;// 일정 시간 타겟없을때 텔레포트
		}
		_ai.searchTarget();// 타겟검색
		if (!_ai.getTargetItemList().isEmpty()) {
			_ai.setAiBrainStatus(AiBrainStatus.PICK_UP);
    		return;
		}
		if (!_ai.getTargetList().toTargetArrayList().isEmpty()) {
			_ai.setAiBrainStatus(AiBrainStatus.ATTACK);
    		return;
    	}
		if (_ai.getAiMoveCount() == 0) {
			radomLoc();
		}
		int dir			= _ai.getDir(_ai.getAiLocX(), _ai.getAiLocY());
        boolean tail	= World.isThroughObject(_ai.getX(), _ai.getY(), _ai.getMapId(), dir);
        boolean door	= World.isDoorMove(_ai.getX(), _ai.getY(), _ai.getMapId(), dir);
        if (!tail || door || dir == -1) {
        	radomLoc();
    		return;
        }
    	_ai.setDirectionMove(dir);
	}
	
	/**
	 * 이동할 죄표 지정
	 */
	void radomLoc(){
		_ai.setAiLocX(_ai.getX() + (int)(Math.random() * 20.0 - 10.0));
		_ai.setAiLocY(_ai.getY() + (int)(Math.random() * 20.0 - 10.0));
	}
}
