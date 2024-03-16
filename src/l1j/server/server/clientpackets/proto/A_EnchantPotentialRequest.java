package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.common.bin.PotentialCommonBinLoader;
import l1j.server.common.bin.potential.CommonPotentialInfo;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.item.potential.L1Potential;
import l1j.server.server.serverpackets.alchemy.S_EnchantPotentialResult;
import l1j.server.server.utils.CommonUtil;

public class A_EnchantPotentialRequest extends ProtoHandler {
	protected A_EnchantPotentialRequest(){}
	private A_EnchantPotentialRequest(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private static java.util.LinkedList<CommonPotentialInfo.MaterialInfoT> MATERIALS;
	
	private int _target_id;
	private boolean _is_event;
	
	void parse() {
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x08:
				_target_id = read4(read_size());
				break;
			case 0x10:
				_is_event = readBool();
				break;
			default:
				return;
			}
		}
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		parse();
		if (_target_id == 0) {
			return;
		}
		L1ItemInstance dollItem		= _pc.getInventory().getItem(_target_id);
		if (dollItem == null) {
			return;
		}
		int dollItemid				= dollItem.getItemId();
		L1Potential curPotential	= dollItem.getPotential();
		int curbonus_grade			= curPotential == null ? 0 : curPotential.getInfo().get_bonus_grade();
		int curbonus_desc			= curPotential == null ? 0 : curPotential.getInfo().get_bonus_desc();
		int doll_grade				= MagicDollInfoTable.getDollInfo(dollItemid).getGrade(); // 인형등급
		int bonus_grade				= 0;// 획득할 잠재력 레벨
		
		if (MATERIALS == null) {
			MATERIALS = PotentialCommonBinLoader.getBin().get_material_list();
		}
		CommonPotentialInfo.MaterialInfoT material = MATERIALS.get(curbonus_grade);// 단계별 재료
		if (material == null) {
			System.out.println(String.format("[A_EnchantPotentialRequest] MATERIAL_INFO_NOT_FOUND : GRADE(%d)", curbonus_grade));
			return;
		}
		int material_name_id	= material.get_nameId();
		int material_amount		= material.get_amount();
		
		// 이벤트
		if (_is_event) {
			CommonPotentialInfo.EventInfoT event = PotentialCommonBinLoader.getBin().get_event_config();
			if (!event.get_event_item_list().contains(dollItem.getItem().getItemNameId())) {
				System.out.println(String.format("[A_EnchantPotentialRequest] NOT_EVENT_DOLL_ITEM : NAME_ID(%d)", dollItem.getItem().getItemNameId()));
				return;
			}
			CommonPotentialInfo.MaterialInfoT event_material = event.get_event_material(curbonus_grade);
			if (event_material == null) {
				System.out.println(String.format("[A_EnchantPotentialRequest] EVENT_MATERIAL_NOT_FOUND : GRADE(%d)", curbonus_grade));
				return;
			}
			bonus_grade			= event_material.get_potential_grade();
			material_name_id	= event_material.get_nameId();
			material_amount		= event_material.get_amount();
		}
		
		if (!_pc.getInventory().consumeItemNameId(material_name_id, material_amount)) {// 재료 소모
			return;
		}
		
		if (bonus_grade == 0) {
			int random	= CommonUtil.random(100) + 1;// 획득확률
			switch(doll_grade){
			case 3:// 3단계인형
				bonus_grade = getRarityGrade(curbonus_grade, random);
				break;
			case 4:// 4단계인형
				bonus_grade = getHeroGrade(curbonus_grade, random);
				break;
			case 5:case 6:// 5, 6단계인형
				bonus_grade = getLegendGrade(curbonus_grade, random);
				break;
			default:
				return;
			}
		}
		
		int bonus_id	= MagicDollInfoTable.getPotentialGradeRandomBonusId(bonus_grade);// 적용될 잠재력 번호
		if (bonus_id == 0) {
			System.out.println(String.format("[A_EnchantPotentialRequest] BONUS_ID_EMPTY : GRADE(%d)", bonus_grade));
			return;
		}
		
		// 하이퍼
		if (bonus_grade >= 5 && CommonUtil.random(100) + 1 <= 1) {
			bonus_grade = 6;
			curbonus_desc = 0;
		}
		
		_pc.getConfig().set_potential_enchant(_target_id, bonus_grade, bonus_id);;
		_pc.sendPackets(new S_EnchantPotentialResult(_target_id, bonus_grade, bonus_id, curbonus_desc), true);
	}
	
	// 희귀
	private int getRarityGrade(int potentialGrade, int random){
		if (potentialGrade >= 3) {
			return 3;// 희귀등급이상일때
		}
		if (potentialGrade >= 2) {// 고급등급이상일때
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL3_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL3_STEP2_PROB) {
				return 2;
			}
			return 3;
		} else {// 일반이하일때
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL3_STEP1_PROB) {
				return 1;
			}
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL3_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL3_STEP2_PROB) {
				return 2;
			}
			return 3;
		}
	}
	
	// 영웅
	private int getHeroGrade(int potentialGrade, int random){
		if (potentialGrade >= 4) {
			return 4;// 영웅등급이상일때
		}
		if (potentialGrade >= 3) {// 희귀등급이상일때
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL4_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL4_STEP2_PROB + Config.ALCHEMY.POTENTIAL_LEVEL4_STEP3_PROB) {
				return 3;
			}
			return 4;
		} else if(potentialGrade >= 2) {// 고급등급이상일때
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL4_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL4_STEP2_PROB) {
				return 2;
			}
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL4_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL4_STEP2_PROB + Config.ALCHEMY.POTENTIAL_LEVEL4_STEP3_PROB) {
				return 3;
			}
			return 4;
		} else {// 일반이하일때
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL4_STEP1_PROB) {
				return 1;
			}
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL4_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL4_STEP2_PROB) {
				return 2;
			}
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL4_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL4_STEP2_PROB + Config.ALCHEMY.POTENTIAL_LEVEL4_STEP3_PROB) {
				return 3;
			}
			return 4;
		}
	}
	
	// 전설
	private int getLegendGrade(int potentialGrade, int random){
		if (potentialGrade >= 5) {
			return 5;// 전설등급이상일때
		}
		if (potentialGrade >= 4) {// 영웅등급이상일때
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP2_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP3_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP4_PROB) {
				return 4;
			}
			return 5;
		} else if (potentialGrade >= 3) {// 희귀등급이상일때
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP2_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP3_PROB) {
				return 3;
			}
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP2_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP3_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP4_PROB) {
				return 4;
			}
			return 5;
		} else if (potentialGrade >= 2) {// 고급등급이상일때
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP2_PROB) {
				return 2;
			}
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP2_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP3_PROB) {
				return 3;
			}
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP2_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP3_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP4_PROB) {
				return 4;
			}
			return 5;
		} else {// 일반고급이하일때
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB) {
				return 1;
			}
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP2_PROB) {
				return 2;
			}
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP2_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP3_PROB) {
				return 3;
			}
			if (random <= Config.ALCHEMY.POTENTIAL_LEVEL5_STEP1_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP2_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP3_PROB + Config.ALCHEMY.POTENTIAL_LEVEL5_STEP4_PROB) {
				return 4;
			}
			return 5;
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_EnchantPotentialRequest(data, client);
	}

}

