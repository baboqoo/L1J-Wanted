package l1j.server.GameSystem.charactertrade.bean;

import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.StringUtil;

public class InvenInfo {
	public int 		id;
	public int 		bless;
	public int 		count;
	public int 		iden;
	public int		enchant;
	public int		attr;
	public L1Item	item;
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder(128);
		if (item.getItemType() == L1ItemType.WEAPON || item.getItemType() == L1ItemType.ARMOR) {
			sb.append(getEnchantString());
		}
		sb.append(item.getDesc());
		if (count > 1) {
			sb.append(" (").append(count).append(StringUtil.EmptyString);
		}
		return sb.toString();
	}
	
	private String getEnchantString(){
		StringBuilder sb = new StringBuilder(64);
		if (item.getItemType() == L1ItemType.WEAPON && attr > 0) {
			sb.append(L1ItemInstance.ATTR_DESCRIPTIONS[attr]);
		}
		if (enchant >= 0) {
			sb.append(StringUtil.PlusString).append(enchant);
		} else {
			sb.append(String.valueOf(enchant));
		}
		sb.append(StringUtil.EmptyOneString);
		return sb.toString();
	}
}

