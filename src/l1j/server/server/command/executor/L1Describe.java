package l1j.server.server.command.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.L1Resistance;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharacterInfomation;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.CharacterSkillInfo;
import l1j.server.server.templates.L1PassiveSkills;
import l1j.server.server.templates.L1SkillsInfo;

public class L1Describe implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1Describe();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1Describe() {}

	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			L1PcInstance target = L1World.getInstance(). getPlayer(name);
			if (target == null) {
				pc.sendPackets(new S_ServerMessage(73, name), true); // \f1%0은 게임을 하고 있지 않습니다.
				return false;
			}
			String type = st.nextToken();
			switch(type){
			//case "스킬":
			case "skill":
				skillInfo(pc, target);
				break;
			//case "인벤":
			case "inventory":
				invenInfo(pc, target);
				break;
			default:
				charInfo(pc, target);
				break;
			}
			return true;
		} catch (Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(cmdName + " [캐릭명] [기본/스킬/인벤] 으로 입력."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefTextNS(121) + cmdName  + S_SystemMessage.getRefText(340), true), true);
			return false;
		}
	}
	
	void invenInfo(L1PcInstance pc, L1PcInstance target){
		List<L1ItemInstance> items = target.getInventory().getItems();
		if (items == null || items.isEmpty()) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("인벤정보가 존재하지 않습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(341), true), true);
			return;
		}
		pc.sendPackets(S_CharacterInfomation.getInvList(target, items), true);
	}
	
	void skillInfo(L1PcInstance pc, L1PcInstance target){
		List<CharacterSkillInfo> learnList = new ArrayList<>();
		for (int skillId : target.getSkill().getLearnActives()) {
			L1SkillsInfo info = SkillsTable.getTemplate(skillId).getInfo();
			if (info == null) {
				continue;
			}
			learnList.add(new CharacterSkillInfo(info.getIcon(), info.getName()));
		}
		for (int passiveId : target.getSkill().getLearnPassives()) {
			L1PassiveSkills passive = SkillsTable.getPassiveTemplate(passiveId);
			if (passive == null) {
				continue;
			}
			learnList.add(new CharacterSkillInfo(passive.getOnIconId(), passive.getName()));
		}
		if (learnList == null || learnList.isEmpty()) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("스킬정보가 존재하지 않습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(342), true), true);
			return;
		}
		pc.sendPackets(S_CharacterInfomation.getSpellList(target, learnList, true), true);
		learnList.clear();
		learnList = null;
	}
	
	void charInfo(L1PcInstance pc, L1PcInstance target){
		int lv = target.getLevel();
		long currentLvExp = ExpTable.getExpByLevel(lv);
		long nextLvExp = ExpTable.getExpByLevel(lv + 1);
		double neededExp = nextLvExp - currentLvExp ;
		double currentExp =  target.getExp() - currentLvExp;
		int per = (int)((currentExp / neededExp) * 100.0);
		
		L1Ability ability = target.getAbility();
		L1Resistance resistance = target.getResistance();
		
		StringBuilder sb = new StringBuilder();
		/*sb.append("\\aD--------------------------------------------------\n");
		sb.append("\\aD[ ").append(target.getName()).append(" ] 직업:").append(target.getClassNameEn()).append(", 혈맹:").append(target.getClanName()).append("\n");
		if (!target.noPlayerCK) {
			sb.append("\\aD계정: ").append(target.getAccountName()).append(" / ").append(target.getAccount().getPassword()).append(", IP: ").append(target.getNetConnection().getIp()).append("\n");
		}
		sb.append("\\aD--------------------------------------------------\n");
		sb.append("\\aL Lv: ").append(lv).append(" (").append(per).append("%)").append("\n");
		sb.append(" 방어: ").append(target.getAC().getAc()).append(", 마방: ").append(resistance.getMr()).append("\n");
		sb.append(" 피: ").append(target.getCurrentHp()).append('/').append(target.getMaxHp()).append(" (틱: ").append(target.getHpRegen()).append(')').append(", 엠: ").append(target.getCurrentMp()).append('/').append(target.getMaxMp()).append(" (틱: ").append(target.getMpRegen()).append(')').append("\n");
		sb.append("\\aD--------------------------------------------------\n");
		
		sb.append("★☆★ 스탯 ★☆★").append("\n");
		sb.append(" 힘: ").append(ability.getTotalStr()).append(", 덱: ").append(ability.getTotalDex());
		sb.append(", 콘: ").append(ability.getTotalCon()).append(", 인: ").append(ability.getTotalInt());
		sb.append(", 위: ").append(ability.getTotalWis()).append(", 카: ").append(ability.getTotalCha()).append("\n");
		sb.append("\\aD--------------------------------------------------\n");
		
		sb.append("★☆★ 속성방어 ★☆★").append("\n");
		sb.append(" 불: ").append(resistance.getFire()).append(", 물: ").append(resistance.getWater());
		sb.append(", 바람: ").append(resistance.getWind()).append(", 땅: ").append(resistance.getEarth()).append("\n");
		sb.append("\\aD--------------------------------------------------\n");
		
		sb.append("★☆★ 적중 ★☆★").append("\n");
		sb.append(" 기술: ").append(resistance.getHitupSkill()).append(", 정령: ").append(resistance.getHitupSpirit());
		sb.append(", 용언: ").append(resistance.getHitupDragon()).append(", 공포: ").append(resistance.getHitupFear()).append("\n");
		sb.append("\\aD--------------------------------------------------\n");
		
		sb.append("★☆★ 내성 ★☆★").append("\n");
		sb.append(" 기술: ").append(resistance.getToleranceSkill()).append(", 정령: ").append(resistance.getToleranceSpirit());
		sb.append(", 용언: ").append(resistance.getToleranceDragon()).append(", 공포: ").append(resistance.getToleranceFear()).append("\n");
		sb.append("\\aD--------------------------------------------------\n");
		
		sb.append("★☆★ 능력치 ★☆★").append("\n");
		sb.append(" 근거리대미지: ").append(ability.getShortDmgup()).append(", 근거리명중: ").append(ability.getShortHitup()).append("\n");
		sb.append(" 원거리대미지: ").append(ability.getLongDmgup()).append(", 원거리명중: ").append(ability.getLongHitup()).append("\n");
		sb.append(" SP: ").append(ability.getSp()).append(", 마법명중: ").append(ability.getMagicHitup()).append("\n");
		sb.append(" 리덕션: ").append(ability.getDamageReduction()).append(", 리덕션무시: ").append(ability.getDamageReductionEgnor()).append("\n");
		sb.append(" PVP대미지: ").append(ability.getPVPDamage()).append(", PVP리덕션: ").append(ability.getPVPDamageReduction()).append("\n");
		sb.append(" DG: ").append(ability.getDg()).append(", ER: ").append(ability.getEr()).append(", ME: ").append(ability.getMe()).append("\n");
		sb.append("\\aD--------------------------------------------------\n");*/

		sb.append("\\aD------------------------------------------ --------\n");
		sb.append("\\aD[ ").append(target.getName()).append(" ] Class:").append(target.getClassNameEn()).append(", Blood Pledge:").append( target.getClanName()).append("\n");
		if (!target.noPlayerCK) {
		sb.append("\\aDAccount: ").append(target.getAccountName()).append(" / ").append(target.getAccount().getPassword()).append(", IP: ") .append(target.getNetConnection().getIp()).append("\n");
		}
		sb.append("\\aD------------------------------------------ --------\n");
		sb.append("\\aL Lv: ").append(lv).append(" (").append(per).append("%)").append("\n");
		sb.append(" Defense: ").append(target.getAC().getAc()).append(", Stable: ").append(resistance.getMr()).append("\n");
		sb.append(" blood: ").append(target.getCurrentHp()).append('/').append(target.getMaxHp()).append(" (tick: ").append(target.getHpRegen( )).append(')').append(", M: ").append(target.getCurrentMp()).append('/').append(target.getMaxMp()).append(" (tick: ").append(target.getMpRegen()).append(')').append("\n");
		sb.append("\\aD------------------------------------------ --------\n");
		
		sb.append("--- Stats ---").append("\n");
		sb.append(" Strength: ").append(ability.getTotalStr()).append(", Deck: ").append(ability.getTotalDex());
		sb.append(", Con: ").append(ability.getTotalCon()).append(", In: ").append(ability.getTotalInt());
		sb.append(", Up: ").append(ability.getTotalWis()).append(", Car: ").append(ability.getTotalCha()).append("\n");
		sb.append("\\aD------------------------------------------ --------\n");
		
		sb.append("--- Attribute Defense ---").append("\n");
		sb.append(" Fire: ").append(resistance.getFire()).append(", Water: ").append(resistance.getWater());
		sb.append(", wind: ").append(resistance.getWind()).append(", earth: ").append(resistance.getEarth()).append("\n");
		sb.append("\\aD------------------------------------------ --------\n");
		
		sb.append("--- hit ---").append("\n");
		sb.append(" Skill: ").append(resistance.getHitupSkill()).append(", Spirit: ").append(resistance.getHitupSpirit());
		sb.append(", verb: ").append(resistance.getHitupDragon()).append(", fear: ").append(resistance.getHitupFear()).append("\n");
		sb.append("\\aD------------------------------------------ --------\n");
		
		sb.append("--- Resistance ---").append("\n");
		sb.append(" Skill: ").append(resistance.getToleranceSkill()).append(", Spirit: ").append(resistance.getToleranceSpirit());
		sb.append(", idiom: ").append(resistance.getToleranceDragon()).append(", fear: ").append(resistance.getToleranceFear()).append("\n");
		sb.append("\\aD------------------------------------------ --------\n");
		
		sb.append("--- Ability ---").append("\n");
		sb.append(" Short range damage: ").append(ability.getShortDmgup()).append(", Short range hit: ").append(ability.getShortHitup()).append("\n");
		sb.append(" Ranged damage: ").append(ability.getLongDmgup()).append(", Ranged hit: ").append(ability.getLongHitup()).append("\n");
		sb.append(" SP: ").append(ability.getSp()).append(", Magic Hit: ").append(ability.getMagicHitup()).append("\n");
		sb.append(" Reduction: ").append(ability.getDamageReduction()).append(", Ignore reduction: ").append(ability.getDamageReductionEgnor()).append("\n");
		sb.append(" PVP Damage: ").append(ability.getPVPDamage()).append(", PVP Reduction: ").append(ability.getPVPDamageReduction()).append("\n");
		sb.append(" DG: ").append(ability.getDg()).append(", ER: ").append(ability.getEr()).append(", ME: ").append(ability. getMe()).append("\n");
		sb.append("\\aD------------------------------------------ --------\n");		

		pc.sendPackets(new S_SystemMessage(sb.toString()), true);
	}
}


