package l1j.server.GameSystem.ai.brain.action;

import l1j.server.GameSystem.ai.brain.AiBrainHandler;
import l1j.server.GameSystem.ai.constuct.AiBrainStatus;
import l1j.server.GameSystem.astar.World;
import l1j.server.server.ActionCodes;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.action.S_AttackStatus;

/**
 * AI 아이템 획득 핸들러
 * @author LinOffice
 */
public class PickUp extends AiBrainHandler {
	public PickUp(L1AiUserInstance ai) {
		super(ai);
	}
	
	@Override
	protected void action() throws Exception {
		if (_ai == null) {
			return;
		}
		if (_ai.getTargetItemList().isEmpty()) {
			_ai.searchTarget();// 타겟 검색
		}
		L1ItemInstance targetItem = _ai.getAiTargetItem();
		if (targetItem == null) {
			targetItem = changeTargetItem(targetItem);// 타겟 없음
		}
        if (!_ai.isPickUp(targetItem)) {
        	targetItem = changeTargetItem(targetItem);
        }
        if (targetItem == null) {
        	changeStatus();
        	return;
        }
        onTargetItem(targetItem);
	}
	
	/**
	 * 아이템 변경
	 * @param targetItem
	 * @return L1ItemInstance
	 */
	L1ItemInstance changeTargetItem(L1ItemInstance targetItem){
		if (targetItem != null) {
			_ai.addPassItem(targetItem);
			_ai.removeTargetItemList(targetItem);
		}
    	_ai.setAiTargetItem(_ai.getTargetItem());// 타겟 세팅
    	return _ai.getAiTargetItem();
	}
	
	/**
	 * 상태 변경
	 */
	void changeStatus(){
		_ai.searchTarget();// 타겟 검색
    	if (!_ai.getTargetList().toTargetArrayList().isEmpty()) {
    		_ai.setAiBrainStatus(AiBrainStatus.ATTACK);
    		return;
    	}
    	_ai.setAiBrainStatus(AiBrainStatus.MOVE);
	}
	
	/**
	 * 타겟 아이템 성공
	 * @param targetItem
	 */
	void onTargetItem(L1ItemInstance targetItem) {
		if (_ai.getLocation().getTileLineDistance(targetItem.getLocation()) <= 1) {// 토글 가능 위치
			pickupTargetItem(targetItem);
			return;
		}
		toMoveTargetItem(targetItem);
	}
	
	/**
	 * 아이템 위치로 이동
	 * @param targetItem
	 */
	void toMoveTargetItem(L1ItemInstance targetItem){
		int dir			= _ai.getDir(targetItem.getX(), targetItem.getY());
        boolean tail	= World.isThroughObject(_ai.getX(), _ai.getY(), _ai.getMapId(), dir);
        boolean door	= World.isDoorMove(_ai.getX(), _ai.getY(), _ai.getMapId(), dir);
    	if (!tail || door || dir == -1) {
    		_ai.addPassItem(targetItem);
    		_ai.removeTargetItemList(targetItem);
    		_ai.setAiTargetItem(null);
    		return;
    	}
    	_ai.setDirectionMove(dir);
	}
	
	/**
	 * 아이템 줍기
	 * @param targetItem
	 */
	void pickupTargetItem(L1ItemInstance targetItem) {
		_ai.removeTargetItemList(targetItem);
		_ai.setAiTargetItem(null);
		_ai.broadcastPacket(new S_AttackStatus(_ai, targetItem.getId(), ActionCodes.ACTION_Pickup), true);// 줍기
		L1World.getInstance().getInventory(targetItem.getX(), targetItem.getY(), targetItem.getMapId()).removeItem(targetItem);// 삭제
		_ai.setAiSleepTime(800);
	}
}

