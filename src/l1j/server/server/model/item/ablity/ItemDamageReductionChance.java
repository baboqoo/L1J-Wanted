package l1j.server.server.model.item.ablity;

public class ItemDamageReductionChance implements ItemAbility {// 대미지 감소 확률
	private static class newInstance {
		public static final ItemDamageReductionChance INSTANCE = new ItemDamageReductionChance();
	}
	public static ItemDamageReductionChance getInstance(){
		return newInstance.INSTANCE;
	}
	private ItemDamageReductionChance(){}
	
	@Override
	public Object info(l1j.server.server.model.Instance.L1ItemInstance item, l1j.server.server.model.Instance.L1PcInstance pc) {
		int[] reduc = new int[2];
		int itemId = item.getItemId();
		int enchantlevel = item.getEnchantLevel();
		if (itemId == 22226 && enchantlevel >= 7) {// 스냅퍼의 체력 반지
			reduc[0] = 20;
			reduc[1] = enchantlevel >= 9 ? 4 : enchantlevel - 6;
			return reduc;
		}
		if (itemId == 222332 && enchantlevel >= 6) {// 축복받은 스냅퍼의 체력 반지
			reduc[0] = 20;
			reduc[1] = enchantlevel >= 9 ? 5 : enchantlevel - 5;
			return reduc;
		}
		if (itemId == 22229 && enchantlevel >= 5) {// 룸티스의 붉은빛 귀걸이
			switch(enchantlevel){
			case 5:
				reduc[0] = 20;
				reduc[1] = 2;
				break;
			case 6:
				reduc[0] = 20;
				reduc[1] = 3;
				break;
			case 7:
				reduc[0] = 20;
				reduc[1] = 4;
				break;
			case 8:
				reduc[0] = 25;
				reduc[1] = 5;
				break;
			default:
				reduc[0] = 25;
				reduc[1] = 7;
				break;
			}
			return reduc;
		}
		if (itemId == 222337 && enchantlevel >= 4) {// 축복받은 룸티스의 붉은빛 귀걸이
			switch(enchantlevel){
			case 4:
				reduc[0] = 20;
				reduc[1] = 2;
				break;
			case 5:
				reduc[0] = 20;
				reduc[1] = 3;
				break;
			case 6:
				reduc[0] = 20;
				reduc[1] = 4;
				break;
			case 7:
				reduc[0] = 20;
				reduc[1] = 5;
				break;
			case 8:
				reduc[0] = 30;
				reduc[1] = 6;
				break;
			default:
				reduc[0] = 30;
				reduc[1] = 8;
				break;
			}
			return reduc;
		}
		if (itemId == 22263) {// 반역자의 방패
			reduc[0] = 50;
			reduc[1] = enchantlevel << 1;
			return reduc;
		}
		if (itemId == 22264 || itemId == 2000031) {// 반역자의 투구
			reduc[0] = 20;
			reduc[1] = enchantlevel;
			return reduc;
		}
		return reduc;
	}
	
	@Override
	public ItemAbility copyInstance() {
		return new ItemDamageReductionChance();
	}
	
}

