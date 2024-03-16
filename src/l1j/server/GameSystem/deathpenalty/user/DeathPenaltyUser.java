package l1j.server.GameSystem.deathpenalty.user;

import l1j.server.Config;
import l1j.server.GameSystem.deathpenalty.bean.DeathPenaltyObject;

/**
 * 유저에게 할당되는 사망 패널티 핸들러
 * 겸험치, 아이템 손실 정보
 * @author LinOffice
 */
public class DeathPenaltyUser {
	private final java.util.LinkedList<DeathPenaltyObject> list;
	
	public DeathPenaltyUser(){
		list = new java.util.LinkedList<>();
	}
	
	public void add(DeathPenaltyObject obj){
		if (list.size() >= Config.PENALTY.REPAIR_STORAGE_LIMIT_SIZE) {
			list.removeFirst();// 가장 오래된 데이터 제거
		}
		list.add(obj);
	}
	
	public void remove(DeathPenaltyObject obj){
		list.remove(obj);
	}
	
	public java.util.LinkedList<DeathPenaltyObject> get_list(){
		return list;
	}
}

