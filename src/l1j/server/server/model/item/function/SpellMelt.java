package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SpellMeltTable;
import l1j.server.server.datatables.SpellMeltTable.MeltData;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.spell.S_AvailableSpellNoti;
import l1j.server.server.serverpackets.spell.S_AddSpellPassiveNoti;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1PassiveSkills;
import l1j.server.server.templates.L1Skills;

public class SpellMelt extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
//AUTO SRM: 	private static final S_SystemMessage FAIL_MESSAGE = new S_SystemMessage("해당 스킬은 용해할 수 없습니다."); // CHECKED OK
	private static final S_SystemMessage FAIL_MESSAGE = new S_SystemMessage(S_SystemMessage.getRefText(1097), true);

	public SpellMelt(L1Item item) {
		super(item);
	}
	
	private int spellType, id;

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc	= (L1PcInstance) cha;
			spellType		= packet.readC();// 0:패시브, 1:엑티브
			id				= packet.readH();
			MeltData melt	= getMeltData();
			if (melt == null) {
				pc.sendPackets(FAIL_MESSAGE);
				return;
			}
			L1ItemInstance item = pc.getInventory().storeItem(melt.meltItemId, 1);// 증표 생성
			if (item == null) {
				System.out.println(String.format(
						"[SpellMelt] STORE_ITEM_TEMPLATE_EMPTY : ITEM_ID(%d), CHAR_NAME(%s)",
						melt.meltItemId, pc.getName()));
				return;
			}
			
			switch (spellType) {
			case 0:// 패시브
				L1PassiveSkills passive = SkillsTable.getPassiveTemplate(melt.passiveId);
				if (passive == null) {
					System.out.println(String.format(
							"[SpellMelt] PASSIVE_TEMPLATE_EMPTY : ITEM_ID(%d), PASSIVE_ID(%d), CHAR_NAME(%s)",
							melt.meltItemId, melt.passiveId, pc.getName()));
					return;
				}
				pc.getPassiveSkill().remove(passive.getPassive());// 패시브 능력치 제거
				pc.getSkill().spellPassiveLost(passive);// db업데이트
				break;
			default:// 액티브
				L1Skills skill = SkillsTable.getTemplate(melt.skillId);
				if (skill == null) {
					System.out.println(String.format(
							"[SpellMelt] ACTIVE_TEMPLATE_EMPTY : ITEM_ID(%d), ACTIVE_ID(%d), CHAR_NAME(%s)",
							melt.meltItemId, melt.skillId, pc.getName()));
					return;
				}
				pc.getSkill().spellActiveLost(skill);// db업데이트
				break;
			}
			
			pc.sendPackets(spellType == 1 ? new S_AvailableSpellNoti(id, false) : new S_AddSpellPassiveNoti(id, false), true);
			pc.sendPackets(new S_ServerMessage(403, item.getItem().getDesc()), true);// 획득 멘트
			pc.sendPackets(new S_ServerMessage(7896, melt.skillName), true);// 성공 멘트
			pc.getInventory().removeItem(this, 1);// 용해제 제거
		}
	}
	
	private MeltData getMeltData(){
		return spellType == 1 ? SpellMeltTable.getActiveData(id) : SpellMeltTable.getPassiveData(id);
	}
}


