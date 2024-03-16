package l1j.server.server.clientpackets.proto;

import java.util.Map;

import l1j.server.common.bin.CompanionCommonBinLoader;
import l1j.server.common.bin.companion.CompanionT;
import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.CharacterCompanionTable;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.companion.S_CompanionSkillNoti;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Pet;

public class A_CompanionSkillNextTierOpen extends ProtoHandler {
	protected A_CompanionSkillNextTierOpen(){}
	private A_CompanionSkillNextTierOpen(byte[] data, GameClient client) {
		super(data, client);
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
        int tier = info.getTier();
        if (tier >= CompanionCommonBinLoader.getSkillEnchantSize()) {
        	return;
        }
        
        CompanionT.SkillEnchantTierT skillEnchantT = CompanionCommonBinLoader.getSkillEnchant(tier + 1);
        if (skillEnchantT == null) {
        	return;
        }
        CompanionT.SkillEnchantTierT.OpenCostT openCostT = skillEnchantT.get_OpenCost();
        if (openCostT.get_level() > companion.getLevel()) {
			//_pc.sendPackets(new S_SystemMessage(String.format("%d레벨 이상부터 사용 가능합니다.", openCostT.get_level())), true);
			_pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(37), String.valueOf(openCostT.get_level())), true);
        	return;
        }
        if (openCostT.get_minEnchant() > 0 && check_enchant(openCostT.get_minEnchant(), companion.getWild(), tier, info)) {
			//_pc.sendPackets(new S_SystemMessage(String.format("%d단계 야성의 인챈트가 부족합니다. (필요 인챈트:%d)", tier, openCostT.get_minEnchant())), true);
			_pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(38), String.valueOf(tier), String.valueOf(openCostT.get_minEnchant())), true);
        	return;
        }
        if (!_pc.getInventory().consumeItem(L1ItemId.ADENA, skillEnchantT.get_OpenCost().get_adena())) {
        	_pc.sendPackets(L1ServerMessage.sm189);
        	return;
        }
        info.setTier(info.getTier() + 1);// 단계 상승
        info.openWild(companion);
        CharacterCompanionTable.getInstance().tierSave(info);
        _pc.sendPackets(new S_CompanionSkillNoti(companion.getWild()), true);
	}
	
	boolean check_enchant(int min_enchant, Map<Integer, Integer> enchant_map, int tier, L1Pet info) {
		CompanionT.ClassInfoT.ClassT.SkillT skillT = info.getClassT().get_Skill().get(tier - 1);
		for (int id : enchant_map.keySet()) {
			if (!skillT.get_skillId().contains(id)) {
				continue;
			}
			if (min_enchant > enchant_map.get(id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CompanionSkillNextTierOpen(data, client);
	}

}


