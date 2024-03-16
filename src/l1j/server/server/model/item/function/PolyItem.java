package l1j.server.server.model.item.function;

import l1j.server.common.data.Gender;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.datatables.PolyTable.PolyItemData;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class PolyItem extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	private PolyItemData _poly;

	public PolyItem(L1Item item) {
		this(item, null);
	}
	
	public PolyItem(L1Item item, PolyItemData poly) {
		super(item);
		_poly = poly;
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1InterServer inter = ((L1PcInstance) cha).getNetConnection().getInter();
			if (L1InterServer.isNotPolyInter(inter)) {
				return;
			}
			if (_poly != null){
				L1PolyMorph.doPoly(pc, _poly.getPolyId(), _poly.getDuration(), _poly.getType());
				if (_poly.isDelete()) {
					pc.getInventory().removeItem(this, 1);
				}
				return;
			}
			polyItem(pc);
		}
	}

	private void polyItem(L1PcInstance pc) {
		boolean isMale = pc.getGender() == Gender.MALE;
		int polyId	= 0;
		switch(this.getItemId()){
		case 8003:// 랭커변신
			if(pc.isCrown())			polyId = isMale ? L1CharacterInfo.RANKING_PRINCE_MALE : L1CharacterInfo.RANKING_PRINCE_FEMALE;
			else if(pc.isKnight())		polyId = isMale ? L1CharacterInfo.RANKING_KNIGHT_MALE : L1CharacterInfo.RANKING_KNIGHT_FEMALE;
			else if(pc.isElf())			polyId = isMale ? L1CharacterInfo.RANKING_ELF_MALE : L1CharacterInfo.RANKING_ELF_FEMALE;
			else if(pc.isWizard())		polyId = isMale ? L1CharacterInfo.RANKING_WIZARD_MALE : L1CharacterInfo.RANKING_WIZARD_FEMALE;
			else if(pc.isDarkelf())		polyId = isMale ? L1CharacterInfo.RANKING_DARK_ELF_MALE : L1CharacterInfo.RANKING_DARK_ELF_FEMALE;
			else if(pc.isDragonknight())polyId = isMale ? L1CharacterInfo.RANKING_DRAGONKNIGHT_MALE : L1CharacterInfo.RANKING_DRAGONKNIGHT_FEMALE;
			else if(pc.isIllusionist())	polyId = isMale ? L1CharacterInfo.RANKING_ILLUSIONIST_MALE : L1CharacterInfo.RANKING_ILLUSIONIST_FEMALE;
			else if(pc.isWarrior())		polyId = isMale ? L1CharacterInfo.RANKING_WARRIOR_MALE : L1CharacterInfo.RANKING_WARRIOR_FEMALE;
			else if(pc.isFencer())		polyId = isMale ? L1CharacterInfo.RANKING_FENCER_MALE : L1CharacterInfo.RANKING_FENCER_FEMALE;
			else if(pc.isLancer())		polyId = isMale ? L1CharacterInfo.RANKING_LANCER_MALE : L1CharacterInfo.RANKING_LANCER_FEMALE;
			break;
		case 210097:// 샤르나30
			if(pc.isCrown())			polyId = isMale ? 6822 : 6823;
			else if(pc.isKnight())		polyId = isMale ? 6824 : 6825;
			else if(pc.isElf())			polyId = isMale ? 6826 : 6827;
			else if(pc.isWizard())		polyId = isMale ? 6828 : 6829;
			else if(pc.isDarkelf())		polyId = isMale ? 6830 : 6831;
			else if(pc.isDragonknight())polyId = isMale ? 7139 : 7140;
			else if(pc.isIllusionist())	polyId = isMale ? 7141 : 7142;
			else if(pc.isWarrior())		polyId = isMale ? 12490 : 12494;
			break;
		case 210098:// 샤르나40
			if(pc.isCrown())			polyId = isMale ? 6832 : 6833;
			else if(pc.isKnight())		polyId = isMale ? 6834 : 6835;
			else if(pc.isElf())			polyId = isMale ? 6836 : 6837;
			else if(pc.isWizard())		polyId = isMale ? 6838 : 6839;
			else if(pc.isDarkelf())		polyId = isMale ? 6840 : 6841;
			else if(pc.isDragonknight())polyId = isMale ? 7143 : 7144;
			else if(pc.isIllusionist())	polyId = isMale ? 7145 : 7146;
			else if(pc.isWarrior())		polyId = isMale ? 12490 : 12494;
			break;
		case 210099:// 샤르나52
			if(pc.isCrown())			polyId = isMale ? 6842 : 6843;
			else if(pc.isKnight())		polyId = isMale ? 6844 : 6845;
			else if(pc.isElf())			polyId = isMale ? 6846 : 6847;
			else if(pc.isWizard())		polyId = isMale ? 6848 : 6849;
			else if(pc.isDarkelf())		polyId = isMale ? 6850 : 6851;
			else if(pc.isDragonknight())polyId = isMale ? 7147 : 7148;
			else if(pc.isIllusionist())	polyId = isMale ? 7149 : 7150;
			else if(pc.isWarrior())		polyId = isMale ? 12490 : 12494;
			break;
		case 210100:// 샤르나55
			if(pc.isCrown())			polyId = isMale ? 6852 : 6853;
			else if(pc.isKnight())		polyId = isMale ? 6854 : 6855;
			else if(pc.isElf())			polyId = isMale ? 6856 : 6857;
			else if(pc.isWizard())		polyId = isMale ? 6858 : 6859;
			else if(pc.isDarkelf())		polyId = isMale ? 6860 : 6861;
			else if(pc.isDragonknight())polyId = isMale ? 7151 : 7152;
			else if(pc.isIllusionist())	polyId = isMale ? 7153 : 7154;
			else if(pc.isWarrior())		polyId = isMale ? 12490 : 12494;
			break;
		case 210101:// 샤르나60
			if(pc.isCrown())			polyId = isMale ? 6862 : 6863;
			else if(pc.isKnight())		polyId = isMale ? 6864 : 6865;
			else if(pc.isElf())			polyId = isMale ? 6866 : 6867;
			else if(pc.isWizard())		polyId = isMale ? 6868 : 6869;
			else if(pc.isDarkelf())		polyId = isMale ? 6870 : 6871;
			else if(pc.isDragonknight())polyId = isMale ? 7155 : 7156;
			else if(pc.isIllusionist())	polyId = isMale ? 7157 : 7158;
			else if(pc.isWarrior())		polyId = isMale ? 12490 : 12494;
			break;
		case 210102:// 샤르나65
			if(pc.isCrown())			polyId = isMale ? 6872 : 6873;
			else if(pc.isKnight())		polyId = isMale ? 6874 : 6875;
			else if(pc.isElf())			polyId = isMale ? 6876 : 6877;
			else if(pc.isWizard())		polyId = isMale ? 6878 : 6879;
			else if(pc.isDarkelf())		polyId = isMale ? 6880 : 6881;
			else if(pc.isDragonknight())polyId = isMale ? 7159 : 7160;
			else if(pc.isIllusionist())	polyId = isMale ? 7161 : 7162;
			else if(pc.isWarrior())		polyId = isMale ? 12490 : 12494;
			break;
		case 210103:// 샤르나70
			if(pc.isCrown())			polyId = isMale ? 6882 : 6883;
			else if(pc.isKnight())		polyId = isMale ? 6884 : 6885;
			else if(pc.isElf())			polyId = isMale ? 6886 : 6887;
			else if(pc.isWizard())		polyId = isMale ? 6888 : 6889;
			else if(pc.isDarkelf())		polyId = isMale ? 6890 : 6891;
			else if(pc.isDragonknight())polyId = isMale ? 7163 : 7164;
			else if(pc.isIllusionist())	polyId = isMale ? 7165 : 7166;
			else if(pc.isWarrior())		polyId = isMale ? 12490 : 12494;
			break;
		case 210116:// 샤르나75
			if(pc.isCrown())			polyId = isMale ? 10987 : 10988;
			else if(pc.isKnight())		polyId = isMale ? 10989 : 10990;
			else if(pc.isElf())			polyId = isMale ? 10991 : 10992;
			else if(pc.isWizard())		polyId = isMale ? 10993 : 10994;
			else if(pc.isDarkelf())		polyId = isMale ? 10995 : 10996;
			else if(pc.isDragonknight())polyId = isMale ? 10997 : 10998;
			else if(pc.isIllusionist())	polyId = isMale ? 10999 : 11000;
			else if(pc.isWarrior())		polyId = isMale ? 12490 : 12494;
			break;
		case 210117:// 샤르나80
			if(pc.isCrown())			polyId = isMale ? 11001 : 11002;
			else if(pc.isKnight())		polyId = isMale ? 11003 : 11004;
			else if(pc.isElf())			polyId = isMale ? 11005 : 11006;
			else if(pc.isWizard())		polyId = isMale ? 11007 : 11008;
			else if(pc.isDarkelf())		polyId = isMale ? 11009 : 11010;
			else if(pc.isDragonknight())polyId = isMale ? 11011 : 11012;
			else if(pc.isIllusionist())	polyId = isMale ? 11013 : 11014;
			else if(pc.isWarrior())		polyId = isMale ? 12490 : 12494;
			break;
		default:break;
		}
		if (polyId == 0) {
			return;
		}
		L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
		pc.getInventory().removeItem(this, 1);
	}

}

