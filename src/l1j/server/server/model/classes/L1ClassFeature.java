package l1j.server.server.model.classes;

import l1j.server.server.construct.L1CharacterInfo;

public abstract class L1ClassFeature implements L1CharacterInfo {
	
	public static L1ClassFeature newClassFeature(int classId) {
		switch(classId){
		case CLASSID_PRINCE:
		case CLASSID_PRINCESS:
			return new L1RoyalClassFeature();
		case CLASSID_ELF_MALE:
		case CLASSID_ELF_FEMALE:
			return new L1ElfClassFeature();
		case CLASSID_KNIGHT_MALE:
		case CLASSID_KNIGHT_FEMALE:
			return new L1KnightClassFeature();
		case CLASSID_WIZARD_MALE:
		case CLASSID_WIZARD_FEMALE:
			return new L1WizardClassFeature();
		case CLASSID_DARK_ELF_MALE:
		case CLASSID_DARK_ELF_FEMALE:
			return new L1DarkElfClassFeature();
		case CLASSID_DRAGONKNIGHT_MALE:
		case CLASSID_DRAGONKNIGHT_FEMALE:
			return new L1DragonKnightClassFeature();
		case CLASSID_ILLUSIONIST_MALE:
		case CLASSID_ILLUSIONIST_FEMALE:
			return new L1IllusionistClassFeature();
		case CLASSID_WARRIOR_MALE:
		case CLASSID_WARRIOR_FEMALE:
			return new L1WarriorClassFeature();
		case CLASSID_FENCER_MALE:
		case CLASSID_FENCER_FEMALE:
			return new L1FencerClassFeature();
		case CLASSID_LANCER_MALE:
		case CLASSID_LANCER_FEMALE:
			return new L1LancerClassFeature();
		default:
			throw new IllegalArgumentException();
		}
	}

	public abstract int getMagicLevel(int level);
	public abstract String getClassDesc();
	public abstract String getClassFlag();
}
