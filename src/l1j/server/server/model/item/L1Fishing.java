package l1j.server.server.model.item;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

import l1j.server.server.datatables.ItemTable;

@XmlAccessorType(XmlAccessType.FIELD)
public class L1Fishing {
	private static Logger _log = Logger.getLogger(L1Fishing.class.getName());

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "FishingList")
	private static class FishingList implements Iterable<L1Fishing> {
		@XmlElement(name = "Fishing")
		private List<L1Fishing> _list;

		public Iterator<L1Fishing> iterator() {
			return _list.iterator();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Reward {
		@XmlAttribute(name = "ItemId")
		private int _itemId;

		@XmlAttribute(name = "Count")
		private int _count;

		@XmlAttribute(name = "EffectId")
		private int _effectId;

		@XmlAttribute(name = "Broad")
		private boolean _broad;

		private int _prob;
		@XmlAttribute(name = "Prob")
		private void setProb(double prob) {
			_prob = (int) (prob * 10000);
		}

		public int getItemId() {
			return _itemId;
		}
		public int getCount() {
			return _count;
		}
		public int getEffectId() {
			return _effectId;
		}
		public boolean isBroad() {
			return _broad;
		}
		public double getProb() {
			return _prob;
		}
	}

	private static final String PATH = "./data/xml/Item/Fishing.xml";

	private static final HashMap<Integer, L1Fishing> ROD_MAP = new HashMap<Integer, L1Fishing>();
	private static final HashMap<Integer, L1Fishing> RIL_MAP = new HashMap<Integer, L1Fishing>();

	/**
	 * 낚싯대 여부
	 * @param rodId
	 * @return boolean
	 */
	public static boolean isRod(int rodId) {
		return ROD_MAP.containsKey(rodId);
	}
	
	/**
	 * 낚싯대 기준 정보 조사
	 * @param rodId
	 * @return L1Fishing
	 */
	public static L1Fishing fromRod(int rodId) {
		return ROD_MAP.get(rodId);
	}
	
	/**
	 * 낚시릴 여부
	 * @param rilId
	 * @return boolean
	 */
	public static boolean isRil(int rilId) {
		return RIL_MAP.containsKey(rilId);
	}
	
	/**
	 * 낚시릴 기준 정보 조사
	 * @param rilId
	 * @return L1Fishing
	 */
	public static L1Fishing fromRil(int rilId) {
		return RIL_MAP.get(rilId);
	}

	@XmlAttribute(name = "RodId")
	private int _rodId;
	
	@XmlAttribute(name = "Interval")
	private int _interval;
	
	@XmlAttribute(name = "Bait")
	private boolean _bait;
	
	@XmlAttribute(name = "RilId")
	private int _rilId;
	
	@XmlAttribute(name = "RilChargeCount")
	private int _rilChargeCount;
	
	@XmlAttribute(name = "MaxChargeCount")
	private int _maxChargeCount;
	
	@XmlAttribute(name = "RewardExp")
	private int _rewardExp;

	public int getRodId() {
		return _rodId;
	}

	public int getInterval() {
		return _interval;
	}
	
	public boolean isBait() {
		return _bait;
	}
	
	public int getRilId() {
		return _rilId;
	}
	
	public int getRilChargeCount() {
		return _rilChargeCount;
	}
	
	public int getMaxChargeCount() {
		return _maxChargeCount;
	}
	
	public int getRewardExp() {
		return _rewardExp;
	}

	@XmlElement(name = "Reward")
	private CopyOnWriteArrayList<L1Fishing.Reward> _rewards;

	public List<L1Fishing.Reward> getRewards() {
		return _rewards;
	}

	private void init() {
		ItemTable temp = ItemTable.getInstance();
		if (temp.getTemplate(getRodId()) == null) {
			_log.warning(String.format("[L1Fishing] ROD_ID_TEMPLATE_EMPTY : %d", getRodId()));
		}
		if (getRilId() > 0 && temp.getTemplate(getRilId()) == null) {
			_log.warning(String.format("[L1Fishing] RIL_ID_TEMPLATE_EMPTY : %d", getRilId()));
		}
		int totalProb = 0;
		for (L1Fishing.Reward each : getRewards()) {
			totalProb += each.getProb();
			if (temp.getTemplate(each.getItemId()) == null) {
				getRewards().remove(each);
				_log.warning(String.format("[L1Fishing] REWARD_ID_TEMPLATE_EMPTY : %d", each.getItemId()));
			}
		}
		if (totalProb > 1000000) {
			_log.warning(String.format("[L1Fishing] TOTAL_PROB_OVER : %d", getRodId()));
		}
	}

	public static void load() {
		try {
			JAXBContext context = JAXBContext.newInstance(L1Fishing.FishingList.class);
			Unmarshaller um = context.createUnmarshaller();

			File file = new File(PATH);
			L1Fishing.FishingList list = (L1Fishing.FishingList) um.unmarshal(file);

			for (L1Fishing each : list) {
				each.init();
				ROD_MAP.put(each.getRodId(), each);
				if (each.getRilId() > 0) {
					RIL_MAP.put(each.getRilId(), each);
				}
			}
		} catch (Exception e) {
			//_log.log(Level.SEVERE, PATH + "의 로드에 실패.", e);
			_log.log(Level.SEVERE, "Error loading path: " + PATH, e);
			System.exit(0);
		}
	}
	
}
