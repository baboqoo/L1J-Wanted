package l1j.server.server.model.item.function;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import l1j.server.Config;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1SkillStatus;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1HealingPotion {
	private static Logger _log = Logger.getLogger(L1HealingPotion.class.getName());
	private static final Random random = new Random(System.nanoTime());

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "ItemEffectList")
	private static class ItemEffectList implements Iterable<L1HealingPotion> {
		@XmlElement(name = "Item")
		private List<L1HealingPotion> _list;

		public Iterator<L1HealingPotion> iterator() {
			return _list.iterator();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Effect {
		@XmlAttribute(name = "Min")
		private int _min;
		private int getMin() {
			return _min;
		}		
		
		@XmlAttribute(name = "Max")
		private int _max;
		private int getMax() {
			return _max;
		}
				
		@XmlAttribute(name = "GfxId")
		private int _gfxid;
		private int getGfxId() {
			return _gfxid;
		}
				
		@XmlAttribute(name = "MapId")
		private int _mapid;
		private int getMapId() {
			return _mapid;
		}
	}

	private static final String _path = "./data/xml/Item/HealingPotion.xml";

	private static HashMap<Integer, L1HealingPotion> _dataMap = new HashMap<Integer, L1HealingPotion>();

	public static L1HealingPotion get(int id) {
		return _dataMap.get(id);
	}

	@XmlAttribute(name = "ItemId")
	private int _itemId;
	private int getItemId() {
		return _itemId;
	}

	@XmlAttribute(name = "Remove")
	private int _remove;
	private int getRemove() {
		return _remove;
	}

	@XmlElement(name = "Effect")
	private CopyOnWriteArrayList<Effect> _effects;
	private List<Effect> getEffects() {
		return _effects;
	}

	private static void loadXml(HashMap<Integer, L1HealingPotion> dataMap) {
//		PerformanceTimer timer = new PerformanceTimer();
//		System.out.print("■ 물약회복량 데이터 .......................... ");
		try {
			JAXBContext context = JAXBContext.newInstance(L1HealingPotion.ItemEffectList.class);
			Unmarshaller um = context.createUnmarshaller();
			File file = new File(_path);
			ItemEffectList list = (ItemEffectList) um.unmarshal(file);
			for (L1HealingPotion each : list) {
				L1Item item = ItemTable.getInstance().getTemplate(each.getItemId());
				if (item == null) {
					//System.out.print("아이템 ID " + each.getItemId() + " 의 템플릿이 발견되지 않았습니다.");
					System.out.print("Item ID " + each.getItemId() + " template not found.");
				} else {
					((L1EtcItem) item).setHealingPotion(each);// L1Item에 등록
					dataMap.put(each.getItemId(), each);
				}
			}
		} catch (Exception e) {
			//_log.log(Level.SEVERE, _path + "의 로드에 실패.", e);
			_log.log(Level.SEVERE, "Failed to load " + _path, e);
			System.exit(0);
		}
//		System.out.println("■ 로딩 정상 완료 " + timer.get() + "ms");
	}

	public static void load() {
		loadXml(_dataMap);
	}
	
	public static void reload() {
		HashMap<Integer, L1HealingPotion> dataMap = new HashMap<Integer, L1HealingPotion>();
		loadXml(dataMap);
		_dataMap = dataMap;
	}

	public boolean use(L1PcInstance pc, L1ItemInstance item) {
		int maxChargeCount	= item.getItem().getMaxChargeCount();
		int chargeCount		= item.getChargeCount();
		if (maxChargeCount > 0 && chargeCount <= 0) {
			pc.sendPackets(L1ServerMessage.sm79);
			return false;
		}
		Effect effect = null;
		for (Effect each : getEffects()) {
			if (each.getMapId() != 0 && pc.getMap().getBaseMapId() != each.getMapId()) {
				continue;// 맵제한
			}
			effect = each;
			break;
		}
		if (effect == null) {
			pc.sendPackets(L1ServerMessage.sm563);// \f1 여기에서는 사용할 수 없습니다.
			return false;
		}
		pc.cancelAbsoluteBarrier();
		pc.send_effect(effect.getGfxId());
		//pc.sendPackets(L1ServerMessage.sm77);// \f1회복의 기운이 느껴집니다.
		
		int min	= effect.getMin(), max = effect.getMax();
        if (item.getItemId() == 3000085 && pc.getLevel() > 64) {// 위대한 치유의 주문
        	min = 80;
        	max = 120;
		}
		
		int chance = max - min;
		int healHp = min;
		
		L1Ability ablity = pc.getAbility();
		int potionCritical = (ablity.getPotionCritical() / 10) + ablity.getHpPotionCriticalProb();
		if (potionCritical > 0 && random.nextInt(1000) <= potionCritical) {
			healHp += chance;
		} else if (chance > 0) {
			healHp += random.nextInt(chance) + 1;
		}
		double recoveryRate = (double) ablity.getPotionRecoveryRatePct() / 100 + 1;
		if (recoveryRate > 0) {
			healHp = (int)(healHp * recoveryRate);
		}
		
		L1SkillStatus skill = pc.getSkill();
		if (pc.isPolluteWater()) {
			healHp >>= 1;// 폴루트워터 물약 회복량 반감
		}
		if (skill.hasSkillEffect(L1SkillId.STATUS_PHANTOM_REQUIEM)) {// 팬텀레퀴엠 물약 회복량 반감
			healHp = (int)(healHp * Config.SPELL.PHANTOM_REQUIEM_POTION_RATE);
		}
		if (pc.isDesperado()) {// 데스페라도 물약 회복량 반감
			double penalty = 0.5D;
			int diffLevel = pc.getDesperadoAttackerLevel() - pc.getLevel();
			if (diffLevel > 0) {
				penalty -= diffLevel * 0.05D;
			}
			
			int itemPotionRegist = ablity.getItemPotionRegist();
			if (itemPotionRegist > 0) {
				penalty -= (double) itemPotionRegist * 0.01D;// 공포 상쇄
			}
			if (penalty >= 0.7D) {
				penalty = 0.7D;
			} else if (penalty <= 0.4D) {
				penalty = 0.4D;
			}
			healHp *= penalty;
		}
		
		int itemPotionPercent = ablity.getItemPotionPercent();
		if (itemPotionPercent > 0) {
			healHp += (int)((healHp * 0.01D) * itemPotionPercent) + ablity.getItemPotionValue();
		}
		
		int potionPlus = ablity.getStatusPotionPlus() + ablity.getVitalPotion();
		if (potionPlus > 0) {
			float addHp = ((float) healHp / 100) * potionPlus;
			healHp += (int) addHp;
		}
		
		if (pc.isPassiveStatus(L1PassiveId.SURVIVE)) {
			healHp += getSurvive(pc);
		}
		if (skill.hasSkillEffect(L1SkillId.STATUS_PHANTOM_DEATH)) {
			deathPotion(pc, (int)(healHp * Config.SPELL.PHANTOM_DEATH_POTION_RATE));
		} else if (pc.isDeathPotion()) {
			deathPotion(pc, (int)healHp);
		} else {
			pc.setCurrentHp(pc.getCurrentHp() + (int) healHp);
		}
		
		if (getRemove() > 0) {
			if (chargeCount > 0) {
				item.setChargeCount(chargeCount - getRemove());
				pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
			} else {
				pc.getInventory().removeItem(item, getRemove());
			}
		}
		return true;
	}
	
	void deathPotion(L1PcInstance pc, int val) {
		pc.setCurrentHp(pc.getCurrentHp() - (int) val);
		if (pc.isInvisble()) {
			pc.delInvis();
		}
	}
	
	int getSurvive(L1PcInstance pc){
		int hpPercent = pc.getCurrentHpPercent();
		if (hpPercent <= 45 && random.nextInt(100) + 1 <= Config.SPELL.SURVIVE_PROB) {
			int add = pc.getAbility().getTotalCon() + (45 - hpPercent);
			if (pc.getInventory().consumeItem(L1ItemId.GEMSTONE, add)) {
				if (hpPercent <= 10) {
					pc.send_effect(18570);// 흰색
				} else if (hpPercent <= 20) {
					pc.send_effect(18568);// 주홍색
				} else {
					pc.send_effect(18566);// 빨강색
				}
				return add;
			}
		}
		return 0;
	}

}

