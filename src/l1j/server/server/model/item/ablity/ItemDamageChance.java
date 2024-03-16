package l1j.server.server.model.item.ablity;

public class ItemDamageChance implements ItemAbility {// 추가 대미지 확률
	private static class newInstance {
		public static final ItemDamageChance INSTANCE = new ItemDamageChance();
	}
	public static ItemDamageChance getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemDamageChance(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int[] chance = new int[2];
		int itemId = item.getItemId();
		int enchantlevel = item.getEnchantLevel();
		if ((itemId == 222340 || itemId == 222341) && enchantlevel >= 4) {// 룸티스의 검은빛 귀걸이
			switch(enchantlevel){
			case 4:chance[0] = itemId == 222341 ? 20 : 0;chance[1] = itemId == 222341 ? 2 : 0;break;
			case 5:chance[0] = itemId == 222341 ? 20 : 20;chance[1] = itemId == 222341 ? 3 : 2;break;
			case 6:chance[0] = itemId == 222341 ? 20 : 20;chance[1] = itemId == 222341 ? 4 : 3;break;
			case 7:chance[0] = itemId == 222341 ? 20 : 20;chance[1] = itemId == 222341 ? 5 : 4;break;
			case 8:chance[0] = itemId == 222341 ? 30 : 25;chance[1] = itemId == 222341 ? 6 : 5;break;
			default:chance[0] = itemId == 222341 ? 30 : 25;chance[1] = itemId == 222341 ? 8 : 7;break;
			}
		}
		return chance;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemDamageChance();
	}

}

