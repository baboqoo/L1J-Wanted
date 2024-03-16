package l1j.server.server.model.skill.action;

import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillActionHandler;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;

public class ElementalFallDown extends L1SkillActionHandler {

	@Override
	public int start(L1Character attacker, L1Character cha, int time, L1Magic magic) {
		if (attacker instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) attacker;
			int i = -50;
			switch (pc.getElfAttr()){
			case 0:pc.sendPackets(L1ServerMessage.sm79);break;
			case 1:
				cha.getResistance().addEarth(i);
				cha.setAddAttrKind(1);
				break;
			case 2:
				cha.getResistance().addFire(i);
				cha.setAddAttrKind(2);
				break;
			case 4:
				cha.getResistance().addWater(i);
				cha.setAddAttrKind(4);
				break;
			case 8:
				cha.getResistance().addWind(i);
				cha.setAddAttrKind(8);
				break;
			default:
				break;
			}
			cha.getResistance().addToleranceSpirit(-10);
		}
		return 0;
	}

	@Override
	public void stop(L1Character cha) {
		int i = 50;
		switch (cha.getAddAttrKind()) {
		case 1:cha.getResistance().addEarth(i);break;
		case 2:cha.getResistance().addFire(i);break;
		case 4:cha.getResistance().addWater(i);break;
		case 8:cha.getResistance().addWind(i);break;
		default:break;
		}
		cha.getResistance().addToleranceSpirit(10);
		cha.setAddAttrKind(0);
	}
	
	@Override
	public void icon(L1PcInstance pc, int time) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void wrap(L1PcInstance pc, boolean flag) {
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		pc.sendPackets(new S_OwnCharAttrDef(pc), true);
		super.wrap(pc, flag);
	}
	
	@Override
	public L1SkillActionHandler copyInstance() {
		return new ElementalFallDown().setValue(_skillId, _skill);
	}

}

