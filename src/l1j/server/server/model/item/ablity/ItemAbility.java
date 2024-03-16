package l1j.server.server.model.item.ablity;

/**
 * 아이템의 세부 옵션 interface
 * @author LinOffice
 */
public interface ItemAbility {
	/**
	 * 아이템의 세부 옵션 설정
	 * @param item
	 * @param pc
	 * @return Object(Request To Cast)
	 */
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc);
	
	/**
	 * 업무 수행 인스턴스 생성
	 * @return ItemAblity
	 */
	public ItemAbility copyInstance();
}

