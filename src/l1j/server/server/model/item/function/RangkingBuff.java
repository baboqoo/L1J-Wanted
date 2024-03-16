package l1j.server.server.model.item.function;

import java.sql.Timestamp;
import java.util.Calendar;

import l1j.server.Config;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.StringUtil;

public class RangkingBuff extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public RangkingBuff(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			switch(this.getItemId()){
			case 5558:rankingBuffItem(pc, L1SkillId.GRACE_OF_TOP);break;
			case 6670:rankingBuffItem(pc, L1SkillId.GRACE_CROWN_1ST);break;
			case 6671:rankingBuffItem(pc, L1SkillId.GRACE_KNIGHT_1ST);break;
			case 6672:rankingBuffItem(pc, L1SkillId.GRACE_ELF_1ST);break;
			case 6673:rankingBuffItem(pc, L1SkillId.GRACE_WIZARD_1ST);break;
			case 6674:rankingBuffItem(pc, L1SkillId.GRACE_DARKELF_1ST);break;
			case 6675:rankingBuffItem(pc, L1SkillId.GRACE_DRAGONKNIGHT_1ST);break;
			case 6676:rankingBuffItem(pc, L1SkillId.GRACE_ILLUSIONIST_1ST);break;
			case 6677:rankingBuffItem(pc, L1SkillId.GRACE_WARRIOR_1ST);break;
			case 6678:rankingBuffItem(pc, L1SkillId.GRACE_FENCER_1ST);break;
			case 6679:rankingBuffItem(pc, L1SkillId.GRACE_LANCER_1ST);break;
			case 6690:rankingBuffItem(pc, L1SkillId.GRACE_CROWN_2ST);break;
			case 6691:rankingBuffItem(pc, L1SkillId.GRACE_KNIGHT_2ST);break;
			case 6692:rankingBuffItem(pc, L1SkillId.GRACE_ELF_2ST);break;
			case 6693:rankingBuffItem(pc, L1SkillId.GRACE_WIZARD_2ST);break;
			case 6694:rankingBuffItem(pc, L1SkillId.GRACE_DARKELF_2ST);break;
			case 6695:rankingBuffItem(pc, L1SkillId.GRACE_DRAGONKNIGHT_2ST);break;
			case 6696:rankingBuffItem(pc, L1SkillId.GRACE_ILLUSIONIST_2ST);break;
			case 6697:rankingBuffItem(pc, L1SkillId.GRACE_WARRIOR_2ST);break;
			case 6698:rankingBuffItem(pc, L1SkillId.GRACE_FENCER_2ST);break;
			case 6699:rankingBuffItem(pc, L1SkillId.GRACE_LANCER_2ST);break;
			case 6710:rankingBuffItem(pc, L1SkillId.GRACE_CROWN_3ST);break;
			case 6711:rankingBuffItem(pc, L1SkillId.GRACE_KNIGHT_3ST);break;
			case 6712:rankingBuffItem(pc, L1SkillId.GRACE_ELF_3ST);break;
			case 6713:rankingBuffItem(pc, L1SkillId.GRACE_WIZARD_3ST);break;
			case 6714:rankingBuffItem(pc, L1SkillId.GRACE_DARKELF_3ST);break;
			case 6715:rankingBuffItem(pc, L1SkillId.GRACE_DRAGONKNIGHT_3ST);break;
			case 6716:rankingBuffItem(pc, L1SkillId.GRACE_ILLUSIONIST_3ST);break;
			case 6717:rankingBuffItem(pc, L1SkillId.GRACE_WARRIOR_3ST);break;
			case 6718:rankingBuffItem(pc, L1SkillId.GRACE_FENCER_3ST);break;
			case 6719:rankingBuffItem(pc, L1SkillId.GRACE_LANCER_3ST);break;
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void rankingBuffItem(L1PcInstance pc, int skillid) {
		Calendar currentDate = Calendar.getInstance();
		Timestamp lastUsed = this.getLastUsed();
		if (lastUsed == null || currentDate.getTimeInMillis() > lastUsed.getTime() + (1000 * Config.RANKING.RANK_GRACE_ITEM_REUSE_INTERVAL * 1)) {
			if ((skillid >= L1SkillId.GRACE_CROWN_1ST && skillid <= L1SkillId.GRACE_LANCER_1ST 
					|| skillid >= L1SkillId.GRACE_CROWN_2ST && skillid <= L1SkillId.GRACE_LANCER_2ST 
					|| skillid >= L1SkillId.GRACE_CROWN_3ST && skillid <= L1SkillId.GRACE_LANCER_3ST) 
					&& !pc.getInventory().consumeItem(L1ItemId.GEMSTONE, 1000)) {// 클래스가호
				pc.sendPackets(L1ServerMessage.sm337_GEMSTONE);
				return;
			}
			if (pc.getSkill().hasSkillEffect(skillid)) {
				pc.getSkill().removeSkillEffect(skillid);
			}
			L1BuffUtil.skillAction(pc, skillid);
			this.setLastUsed(new Timestamp(currentDate.getTimeInMillis()));
		} else {
			long i = (lastUsed.getTime() + (1000 * Config.RANKING.RANK_GRACE_ITEM_REUSE_INTERVAL * 1)) - currentDate.getTimeInMillis();
			Calendar cal = (Calendar) currentDate.clone();
			cal.setTimeInMillis(cal.getTimeInMillis() + i);
			//pc.sendPackets(new S_SystemMessage(i / 60000 + "분 동안(" + cal.getTime().getHours() + StringUtil.ColonString + cal.getTime().getMinutes() + " 까지)은 사용할 수 없습니다."), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(111), String.valueOf(i / 60000), String.valueOf(cal.getTime().getHours()), StringUtil.ColonString, String.valueOf(cal.getTime().getMinutes())), true);
		}
	}
}


