package l1j.server.server.serverpackets.einhasadpoint;

import l1j.server.common.data.eEinhasadStatType;
import l1j.server.server.model.L1Ability;
import l1j.server.server.utils.BinaryOutputStream;

public class EinhasadStatDetailStream extends BinaryOutputStream {
	
	protected EinhasadStatDetailStream(eEinhasadStatType statType, L1Ability ability) {
		super();
		write_index(statType.toInt());
		switch(statType){
		case BLESS:
			write_value(ability.getStatBless());
			write_abilValue1(ability.getBlessEfficiency());
			write_abilValue2(ability.getBlessExp());
			break;
		case LUCKY:
			write_value(ability.getStatLucky());
			write_abilValue1(ability.getLuckyItem());
			write_abilValue2(ability.getLuckyAdena());
			break;
		case VITAL:
			write_value(ability.getStatVital());
			write_abilValue1(ability.getVitalPotion());
			write_abilValue2(ability.getVitalHeal());
			break;
		case ITEM_SPELL_PROB:
			write_value(ability.getStatItemSpellProb());
			write_abilValue1(ability.getItemSpellProbArmor());
			write_abilValue2(ability.getItemSpellProbWeapon());
			break;
		case ABSOLUTE_REGEN:
			write_value(ability.getStatAbsoluteRegen());
			write_abilValue1(ability.getAbsoluteRegenHp());
			write_abilValue2(ability.getAbsoluteRegenMp());
			break;
		case POTION:
			write_value(ability.getStatPotion());
			write_abilValue1(ability.getPotionCritical());
			write_abilValue2(ability.getPotionDelay());
			break;
		default:
			write_value(0);
			write_abilValue1(0);
			write_abilValue2(0);
			break;
		}
	}
	
	void write_index(int index) {
		writeC(0x08);// index
		writeC(index);
	}
	
	void write_value(int value) {
		writeC(0x10);// value
		writeC(value);
	}
	
	void write_abilValue1(int abilValue1) {
		writeC(0x18);// abilValue1
		writeBit(abilValue1);
	}
	
	void write_abilValue2(int abilValue2) {
		writeC(0x20);// abilValue2
		writeBit(abilValue2);
	}
}

