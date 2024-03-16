package l1j.server.server.clientpackets.proto;

import l1j.server.common.bin.CompanionCommonBinLoader;
import l1j.server.common.bin.companion.CompanionT;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.CharacterCompanionTable;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.companion.S_CompanionSkillEnchant;
import l1j.server.server.serverpackets.companion.S_CompanionSkillNoti;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.utils.CommonUtil;

public class A_CompanionSkillEnchant extends ProtoHandler {
	protected A_CompanionSkillEnchant(){}
	private A_CompanionSkillEnchant(byte[] data, GameClient client) {
		super(data, client);
		parse();
	}
	
	private int _skill_id;
	
	void parse() {
		readP(1);// 0x08
		_skill_id = readBit();
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		L1PetInstance companion = _pc.getPet();
        if (companion == null) {
        	return;
        }
        L1Pet info = companion.getPetInfo();
        if (info == null) {
        	return;
        }
        
        // 등록 검증
        if (!companion.getWild().containsKey(_skill_id)) {
        	System.out.println(String.format("[A_CompanionSkillEnchant] WILD_INFO_NOT_FOUNT : SKILL_ID(%d)", _skill_id));
        	return;
        }
        
        int enchant_level = companion.getWild().get(_skill_id) + 1;
        if (enchant_level < 1 || enchant_level > 10) {
        	return;
        }
        
        CompanionT.WildSkillT.SkillT wildSkillT = CompanionCommonBinLoader.getSkillT(_skill_id);
        if (wildSkillT == null) {
        	System.out.println(String.format("[A_CompanionSkillEnchant] CompanionT.WildSkillT.SkillT NOT_FOUNT : SKILL_ID(%d)", _skill_id));
        	return;
        }
        
        CompanionT.ClassInfoT.ClassT.SkillT skillT = info.getClassT().get_Skill(_skill_id);
        if (skillT == null) {
        	System.out.println(String.format("[A_CompanionSkillEnchant] CompanionT.ClassInfoT.ClassT.SkillT NOT_FOUNT : SKILL_ID(%d)", _skill_id));
        	return;
        }
        
        // 단계 검증
        if (info.getTier() != skillT.get_tier()) {
        	System.out.println(String.format("[A_CompanionSkillEnchant] NOT_EQUALS_TIER : CHAR_NAME(%s), CUR_TIER(%d), REQ_TIER(%d), SKILL_ID(%d)", 
        			_pc.getName(), info.getTier(), skillT.get_tier(), _skill_id));
        	return;
        }
        
        CompanionT.SkillEnchantTierT enchantT = CompanionCommonBinLoader.getSkillEnchant(skillT.get_tier());
        if (enchantT == null) {
        	System.out.println(String.format("[A_CompanionSkillEnchant] CompanionT.SkillEnchantTierT NOT_FOUNT : TIER(%d)", skillT.get_tier()));
        	return;
        }
        
        CompanionT.SkillEnchantTierT.EnchantCostT enchantCostT = enchantT.get_EnchantCost();
        int cost = enchantCostT.get_friendship().get(enchant_level - 1);
        if (companion.get_friend_ship_marble() < cost) {
        	_pc.sendPackets(L1ServerMessage.sm5317);
        	return;
        }
        if (!_pc.getInventory().consumeItemNameId(enchantCostT.get_catalystItem(), 1)) {// 야성 강화 촉매제
        	_pc.sendPackets(L1ServerMessage.sm5318);
        	return;
        }

        companion.add_friend_ship_marble(-cost);
        if (CommonUtil.random(100) + 1 <= get_prob(skillT.get_tier())) {
        	companion.getWild().put(_skill_id, enchant_level);// 강화단계 설정
        	_pc.sendPackets(new S_CompanionSkillNoti(_skill_id, enchant_level), true);
        	_pc.sendPackets(new S_CompanionSkillEnchant(_skill_id, S_CompanionSkillEnchant.eResult.SUCCESS), true);
        	companion.updateSkill(_skill_id, enchant_level);
            CharacterCompanionTable.getInstance().WildSave(info);
            companion.broadcastPacket(new S_DoActionGFX(companion.getId(), 68), true);
        } else {
        	_pc.sendPackets(new S_CompanionSkillEnchant(_skill_id, S_CompanionSkillEnchant.eResult.NOTHING), true);
        }
	}
	
	int get_prob(int tier) {
		switch (tier) {
		case 1:
			return 50;
		case 2:
			return 30;
		case 3:
			return 10;
		case 4:
			return 7;
		case 5:
			return 5;
		default:
			return 0;
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CompanionSkillEnchant(data, client);
	}

}

