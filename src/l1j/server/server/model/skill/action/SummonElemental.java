package l1j.server.server.model.skill.action;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.skill.L1SkillInfo;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CommonUtil;

public class SummonElemental extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int attr = pc.getElfAttr();
			if (attr != 0) {
				if ((pc.getMap().isRecallPets() && !pc.isInWarArea()) || pc.isGm()) {
					int petcost = 0;
					Object[] petlist = pc.getPetList().values().toArray();
					for (Object pet : petlist) {
						petcost += ((L1NpcInstance) pet).getPetcost();
					}
					if (petcost == 0) {
						int summonid = 0;
						int[] summons = _skillId == SUMMON_LESSER_ELEMENTAL ? L1SkillInfo.SUMMON_LESSER_ARRAY : L1SkillInfo.SUMMON_LESSER_GREAT_ARRAY;//땅,불,물,바람
						int npcattr = 1;
						for (int i = 0; i < summons.length; i++) {
							if (npcattr == attr) {
								summonid = summons[i];
								break;
							}
							npcattr *= 2;
						}
						if (attr == 3 || attr == 3 || attr == 9) {
							summonid = _skillId == SUMMON_LESSER_ELEMENTAL ? 45303 : 81050;
						} else if (attr == 6 || attr == 10) {
							summonid = _skillId == SUMMON_LESSER_ELEMENTAL ? 45304 : 81051;
						} else if (attr == 12) {
							summonid = _skillId == SUMMON_LESSER_ELEMENTAL ? 45305 : 81052;
						}
						if (summonid == 0) {
							summonid = summons[CommonUtil.random(4)];
						}
						L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
						L1SummonInstance summon = new L1SummonInstance(npcTemp, pc, 0);
						summon.setPetcost(pc.getAbility().getTotalCha() + 7);
					}
				} else {
					pc.sendPackets(L1ServerMessage.sm79);
				}
			}
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new SummonElemental().setValue(_skillId, _skill);
	}

}

