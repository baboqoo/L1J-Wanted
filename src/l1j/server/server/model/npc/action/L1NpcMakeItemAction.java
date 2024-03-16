package l1j.server.server.model.npc.action;

import java.util.ArrayList;
import java.util.List;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1ObjectAmount;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.serverpackets.S_HowManyMake;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IterableElementList;
import l1j.server.server.utils.StringUtil;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class L1NpcMakeItemAction extends L1NpcXmlAction {
	private final List<L1ObjectAmount<Integer>> _materials = new ArrayList<L1ObjectAmount<Integer>>();
	private final List<L1ObjectAmount<Integer>> _items = new ArrayList<L1ObjectAmount<Integer>>();
	private final boolean _isAmountInputable;
	private final L1NpcAction _actionOnSucceed;
	private final L1NpcAction _actionOnFail;

	public L1NpcMakeItemAction(Element element) {
		super(element);

		_isAmountInputable = L1NpcXmlParser.getBoolAttribute(element, "AmountInputable", true);
		NodeList list = element.getChildNodes();
		for (Element elem : new IterableElementList(list)) {
			if (elem.getNodeName().equalsIgnoreCase("Material")) {
				int id = Integer.valueOf(elem.getAttribute("ItemId"));
				int amount = Integer.valueOf(elem.getAttribute("Amount"));
				int enchant = 0;
				int chance = 100;
				try {
					enchant = Integer.valueOf(elem.getAttribute("Enchant"));
				} catch (Exception e) { }
				try {
					chance = Integer.valueOf(elem.getAttribute("Chance"));
				} catch (Exception e) { }
				_materials.add(new L1ObjectAmount<Integer>(id, amount, enchant, chance));
				continue;
			}
			if (elem.getNodeName().equalsIgnoreCase("Item")) {
				int id = Integer.valueOf(elem.getAttribute("ItemId"));
				int amount = Integer.valueOf(elem.getAttribute("Amount"));
				int enchant = 0;
				int chance = 100;
				try {
					enchant = Integer.valueOf(elem.getAttribute("Enchant"));
				} catch (Exception e) { }
				try {
					chance = Integer.valueOf(elem.getAttribute("Chance"));
				} catch (Exception e) { }
				_items.add(new L1ObjectAmount<Integer>(id, amount, enchant, chance));
				continue;
			}
		}

		if (_items.isEmpty() || _materials.isEmpty())
			throw new IllegalArgumentException();

		Element elem = L1NpcXmlParser.getFirstChildElementByTagName(element, "Succeed");
		_actionOnSucceed = elem == null ? null : new L1NpcListedAction(elem);
		elem = L1NpcXmlParser.getFirstChildElementByTagName(element, "Fail");
		_actionOnFail = elem == null ? null : new L1NpcListedAction(elem);
	}

	private boolean makeItems(L1PcInstance pc, String desc, int amount) {
		// 제작버그 관련 추가
		if (amount <= 0 || amount >= 1000)
			return false;

		boolean isEnoughMaterials = true;
		L1Item temp = null;
		for (L1ObjectAmount<Integer> material : _materials) {
			if(!pc.getInventory().checkMaterialList(material.getObject(), material.getEnchant(), material.getAmount() * amount)) {
				temp = ItemTable.getInstance().getTemplate(material.getObject());
				if(material.getEnchant() != 0)
					//pc.sendPackets(new S_ServerMessage(337, (material.getEnchant()<0 ? material.getEnchant() : StringUtil.PlusString+material.getEnchant()) + StringUtil.EmptyOneString + temp.getDescKr() + "("
					pc.sendPackets(new S_ServerMessage(337, (material.getEnchant()<0 ? material.getEnchant() : StringUtil.PlusString+material.getEnchant()) + StringUtil.EmptyOneString + temp.getDesc() + "("
							+ ((material.getAmount() * amount) - pc.getInventory().countItems(temp.getItemId())) + ")"), true);
				else
					//pc.sendPackets(new S_ServerMessage(337, temp.getDescKr() + "("
					pc.sendPackets(new S_ServerMessage(337, temp.getDesc() + "("
							+ ((material.getAmount() * amount) - pc.getInventory().countItems(temp.getItemId())) + ")"), true);
				isEnoughMaterials = false;
			}
		}
		if (!isEnoughMaterials)
			return false;

		int countToCreate = 0;
		int weight = 0;

		for (L1ObjectAmount<Integer> makingItem : _items) {
			temp = ItemTable.getInstance().getTemplate(makingItem.getObject());
			if (temp.isMerge()) {
				if (!pc.getInventory().checkItem(makingItem.getObject()))
					countToCreate += 1;
			} else
				countToCreate += makingItem.getAmount() * amount;
			weight += temp.getWeight() * (makingItem.getAmount() * amount) / 1000;
			long _CountToCreate = countToCreate;
			// 제작 버그 관련 추가
			if (_CountToCreate < 0 || _CountToCreate > 1000)
				return false;
		}
		if (pc.getInventory().getSize() + countToCreate > 200) {
			pc.sendPackets(L1ServerMessage.sm263);
			return false;
		}
		if (pc.getMaxWeight() < pc.getInventory().getWeight() + weight) {
			pc.sendPackets(L1ServerMessage.sm82);
			return false;
		}

		for (L1ObjectAmount<Integer> material : _materials) {
			if(material.getEnchant() != 0)
				pc.getInventory().consumeMaterialList(material.getObject(), material.getAmount() * amount, material.getEnchant());
			else
				pc.getInventory().consumeItem(material.getObject(), material.getAmount() * amount);
		}
		L1ItemInstance item = null;
		for (L1ObjectAmount<Integer> makingItem : _items) {
			if (makingItem.getChance() >= CommonUtil.random(100)) {
				item = pc.getInventory().storeItem(makingItem.getObject(), makingItem.getAmount() * amount, makingItem.getEnchant());
				if (item != null) {
					item.setEnchantLevel(makingItem.getEnchant());
					//String itemName = ItemTable.getInstance().getTemplate(makingItem.getObject()).getDescKr();
					String itemName = ItemTable.getInstance().getTemplate(makingItem.getObject()).getDesc();
					if (makingItem.getAmount() * amount > 1)
						itemName = itemName + " (" + makingItem.getAmount() * amount + ")";
					//
					if(makingItem.getEnchant() != 0)
						itemName = (makingItem.getEnchant()<0 ? makingItem.getEnchant() : StringUtil.PlusString + makingItem.getEnchant()) + StringUtil.EmptyOneString + itemName;
					//
					pc.sendPackets(new S_ServerMessage(143, desc, itemName), true);
				}
			} else {
				return false;
			}
		}
		return true;
	}

	private int countNumOfMaterials(L1PcInventory inv) {
		int count = Integer.MAX_VALUE;
		for (L1ObjectAmount<Integer> material : _materials) {
			int numOfSet = inv.countItems(material.getObject()) / material.getAmount();
			count = Math.min(count, numOfSet);
		}
		return count;
	}

	@Override
	public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj, byte[] args) {
		int numOfMaterials = countNumOfMaterials(pc.getInventory());
		if (1 < numOfMaterials && _isAmountInputable) {
			pc.sendPackets(new S_HowManyMake(obj.getId(), numOfMaterials, actionName), true);
			return null;
		}
		return executeWithAmount(actionName, pc, obj, 1);
	}

	@Override
	public L1NpcHtml executeWithAmount(String actionName, L1PcInstance pc, L1Object obj, int amount) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		L1NpcHtml result = null;
		if (pc.getInventory().getSize() > 190) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("소지하고 있는 아이템이 너무 많습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1144), true), true);
			return L1NpcHtml.HTML_CLOSE;
		}
		if (pc.getInventory().getWeightPercent() > 82) { // 이부분 수정하면 오류난다
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 사용 할 수 없습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1145), true), true);
			return L1NpcHtml.HTML_CLOSE;
		}
		if (makeItems(pc, npc.getNpcTemplate().getDesc(), amount)) {
			if (_actionOnSucceed != null)
				result = _actionOnSucceed.execute(actionName, pc, obj, new byte[0]);
			if (result == null) {
				result = new L1NpcHtml(StringUtil.EmptyString);
				result.setSuccess(true);
			}
		} else {
			if (_actionOnFail != null)
				result = _actionOnFail.execute(actionName, pc, obj, new byte[0]);
			if (result == null) {
				result = new L1NpcHtml(StringUtil.EmptyString);
				result.setSuccess(false);
			}
		}		
		return result;
	}

}



