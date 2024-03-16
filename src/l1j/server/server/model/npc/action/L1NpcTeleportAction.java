package l1j.server.server.model.npc.action;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_TurnOnTeleportFlagNoti;

import org.w3c.dom.Element;

public class L1NpcTeleportAction extends L1NpcXmlAction {
	private static final String PCBANG_STRING = "T_pcbang";
	private final L1Location _loc;
	private final int _heading;
	private final int _price;
	private final boolean _effect;

	public L1NpcTeleportAction(Element element) {
		super(element);
		int x		= L1NpcXmlParser.getIntAttribute(element, "X", -1);
		int y		= L1NpcXmlParser.getIntAttribute(element, "Y", -1);
		int mapId	= L1NpcXmlParser.getIntAttribute(element, "Map", -1);
		_loc		= new L1Location(x, y, mapId);
		_heading	= L1NpcXmlParser.getIntAttribute(element, "Heading", 5);
		_price		= L1NpcXmlParser.getIntAttribute(element, "Price", 0);
		_effect		= L1NpcXmlParser.getBoolAttribute(element, "Effect", true);
	}

	@Override
	public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj, byte[] args) {
		if (actionName.equalsIgnoreCase(PCBANG_STRING)) {
            if (!pc.isPCCafe() && !pc.getInventory().checkItem(L1ItemId.PIXIE_GOLD_FEATHER, _price)){
            	pc.sendPackets(L1ServerMessage.sm337_FEATHER);
				return L1NpcHtml.HTML_CLOSE;
			}
		} else {
			if (!pc.getInventory().checkItem(L1ItemId.ADENA, _price)) {
				pc.sendPackets(L1ServerMessage.sm189);
				return L1NpcHtml.HTML_CLOSE;
			}
		}

		if (actionName.equalsIgnoreCase(PCBANG_STRING)) {
			if (!pc.isPCCafe()) {
				pc.getInventory().consumeItem(L1ItemId.PIXIE_GOLD_FEATHER, _price);
			}
		} else {
		    pc.getInventory().consumeItem(L1ItemId.ADENA, _price);
		}
		
		pc.getSkill().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 2000);
		pc.sendPackets(S_TurnOnTeleportFlagNoti.TURN_ON_TELEPORT_NOTI);
		pc.getTeleport().start(_loc, _heading, _effect);
		return null; // 팅방지
	}
}

