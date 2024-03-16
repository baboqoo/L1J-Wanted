package l1j.server.server.model.item.ablity.enchant;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 인첸트 아이템 옵션 담당 팩토리
 * @author LinOffice
 */
public class L1EnchantFactory {
	private ConcurrentHashMap<Integer, L1EnchantAblity> ablityList;
	
	public L1EnchantFactory() {
		this.ablityList	= new ConcurrentHashMap<>();
	}
	
	public void putAblity(int enchant, L1EnchantAblity detail){
		ablityList.put(enchant, detail);
	}
	
	/**
	 * 인챈트 수치 상세 능력치 반환
	 * @param enchant
	 * @return L1EnchantAblityDetail
	 */
	public L1EnchantAblity get(int enchant){
		if (enchant < 0) {
			return null;// 마이너스는 체크하지 않는다.
		}
		if (ablityList == null || ablityList.isEmpty()) {
			return null;
		}
		if (enchant > 15) {
			enchant = 15;// 체크할 최대값을제한(loof 최소화)
		}
		for (int i=enchant; i>=0; i--) {// 현재 인챈트부터 차례로 체크한다.(+10인챈트 -> +9인챈트옵션 적용)
			if (!ablityList.containsKey(i)) {
				continue;
			}
			return ablityList.get(i);
		}
		return null;
	}
	
	public void dispose(){
		ablityList.clear();
		ablityList = null;
	}
}

