package l1j.server.server.model.npc.action.id;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.spell.S_SkillIconGFX;
import l1j.server.server.utils.StringUtil;

public class ElfAttrAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new ElfAttrAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private ElfAttrAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		return elfAttr(s, pc, obj.getId());
	}
	
	private String elfAttr(String s, L1PcInstance pc, int objid) {
		if (!pc.isElf()) {
			return StringUtil.EmptyString;
		}
		String htmlid = StringUtil.EmptyString;
		if (s.equalsIgnoreCase("fire")) {
			if (pc.getElfAttr() != 0) {
				return htmlid;
			}
			pc.setElfAttr(2);
			pc.sendPackets(S_SkillIconGFX.ATTR_FIRE);
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("water")) {
			if (pc.getElfAttr() != 0) {
				return htmlid;
			}
			pc.setElfAttr(4);
			pc.sendPackets(S_SkillIconGFX.ATTR_WATER);
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("air")) {
			if (pc.getElfAttr() != 0) {
				return htmlid;
			}
			pc.setElfAttr(8);
			pc.sendPackets(S_SkillIconGFX.ATTR_AIR);
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("earth")) {
			if (pc.getElfAttr() != 0) {
				return htmlid;
			}
			pc.setElfAttr(1);
			pc.sendPackets(S_SkillIconGFX.ATTR_EARTH);
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("gfw")) { // 불 + 물
			if (pc.getElfAttr() != 0) {
				return htmlid;
			}
			pc.setElfAttr(3);
			pc.sendPackets(S_SkillIconGFX.ATTR_FIRE);
			pc.sendPackets(S_SkillIconGFX.ATTR_WATER);
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("gfa")) { // 불 + 바람
			if (pc.getElfAttr() != 0) {
				return htmlid;
			}
			pc.setElfAttr(5);
			pc.sendPackets(S_SkillIconGFX.ATTR_FIRE);
			pc.sendPackets(S_SkillIconGFX.ATTR_AIR);
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("gfe")) { // 불 + 땅
			if (pc.getElfAttr() != 0) {
				return htmlid;
			}
			pc.setElfAttr(9);
			pc.sendPackets(S_SkillIconGFX.ATTR_FIRE);
			pc.sendPackets(S_SkillIconGFX.ATTR_EARTH);
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("gwa")) { // 물 + 바람
			if (pc.getElfAttr() != 0) {
				return htmlid;
			}
			pc.setElfAttr(6);
			pc.sendPackets(S_SkillIconGFX.ATTR_WATER);
			pc.sendPackets(S_SkillIconGFX.ATTR_AIR);
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("gwe")) { // 물 + 땅
			if (pc.getElfAttr() != 0) {
				return htmlid;
			}
			pc.setElfAttr(10);
			pc.sendPackets(S_SkillIconGFX.ATTR_WATER);
			pc.sendPackets(S_SkillIconGFX.ATTR_EARTH);
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("gae")) { // 바람 + 땅
			if (pc.getElfAttr() != 0) {
				return htmlid;
			}
			pc.setElfAttr(12);
			pc.sendPackets(S_SkillIconGFX.ATTR_AIR);
			pc.sendPackets(S_SkillIconGFX.ATTR_EARTH);
			htmlid = StringUtil.EmptyString;
		} else if (s.equalsIgnoreCase("count")) {
			String[] htmldata = { "500000" };
			int adena = 500000 * (pc.getContribution() + 1);
			if (adena > 10000000) {
				adena = 10000000;
			}
			htmldata[0] = String.valueOf(adena);
			
            if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + "ellyonne12"), true);											
			
			pc.sendPackets(new S_NPCTalkReturn(objid, "ellyonne12", htmldata), true);
			htmlid = null;
		} else if (s.matches("money|init")) {// 정령력 제거
			if (pc.getElfAttr() == 0) {
				return htmlid;
			}
			int adena = 500000 * (pc.getContribution() + 1);// 횟수에 따른 아데나 증가
			if (adena > 10000000) {
				adena = 10000000;// 최대 1000만 아데나
			}
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, adena)) {
				attrEmpty(pc);
				htmlid = StringUtil.EmptyString;
			} else {
				return "ellyonne13";
			}
		} else if (s.equalsIgnoreCase("bm")) {// 정령력 제거
			if (pc.getElfAttr() == 0) {
				return htmlid;
			}
			if (pc.getInventory().consumeItem(200000, 2)) {// 회상의 촛불
				attrEmpty(pc);
				pc.sendPackets(L1ServerMessage.sm678);
				htmlid = StringUtil.EmptyString;
			} else {
				return "ellyonne13";
			}
		}
		return htmlid;
	}
	
	private void attrEmpty(L1PcInstance pc){
		pc.setElfAttr(0);
		pc.sendPackets(S_SkillIconGFX.ATTR_EMPTY);
		pc.setContribution(pc.getContribution() + 1);
	}
}

