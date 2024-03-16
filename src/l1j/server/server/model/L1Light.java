package l1j.server.server.model;

import l1j.server.server.construct.item.L1ItemNormalType;
import l1j.server.server.construct.item.L1ItemType;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Light;

public class L1Light {
	private int _chaLightSize; 
	private int _ownLightSize;
	private L1Character character;
	
	L1Light(L1Character cha) { 
		character = cha; 
	}
	
	public int getChaLightSize() {
		if (character.isInvisble())
			return 0;
		return _chaLightSize;
	}

	public void setChaLightSize(int i) {
		_chaLightSize = i;
	}

	public int getOwnLightSize() {
		return _ownLightSize;
	}

	public void setOwnLightSize(int i) {
		_ownLightSize = i;
	}
	
	public void turnOnOffLight() {
		if (character == null) {
			return;
		}
		int lightSize = 0;
		if (character instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) character;
			lightSize = npc.getLightSize(); 
		}

		if (character.getSkill().hasSkillEffect(L1SkillId.LIGHT)) {
			lightSize = 14;
		}
		for (L1ItemInstance item : character.getInventory().getItems()) {
			if (item.getItem().getItemType() == L1ItemType.NORMAL && item.getItem().getType() == L1ItemNormalType.LIGHT.getId()) {
				int itemlightSize = item.getItem().getLightRange();
				if (itemlightSize != 0 && item.isNowLighting()) {
					if (itemlightSize > lightSize) {
						lightSize = itemlightSize;
					}
				}
			}
		}

		S_Light light = null;
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			light = new S_Light(character.getId(), lightSize);
			pc.sendPackets(light);
		}
		
		if (!character.isInvisble()) {
			if (light == null) {
				light = new S_Light(character.getId(), lightSize);
			}
			character.broadcastPacket(light);
		}

		setOwnLightSize(lightSize); 
		setChaLightSize(lightSize);
		if (light != null) {
			light.clear();
			light = null;
		}
	}
}

