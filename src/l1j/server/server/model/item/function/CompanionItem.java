package l1j.server.server.model.item.function;

import java.sql.Timestamp;
import java.util.Calendar;

import l1j.server.Config;
import l1j.server.common.bin.CompanionCommonBinLoader;
import l1j.server.common.bin.companion.CompanionT;
import l1j.server.server.IdFactory;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.CharacterCompanionTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Effect;
import l1j.server.server.serverpackets.companion.S_CompanionStatusNoti;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.StringUtil;

public class CompanionItem extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public CompanionItem(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int itemId = this.getItemId();
			switch(itemId){
			case 3200001:
				resetStat(pc);
				break;
			case L1ItemId.PET_AMULET:
				useAmulet(pc);
				break;
			case 3200003:case 3200004:case 3200005:
				heal_potion(pc, itemId);
				break;
			case 3200015:case 3200016:case 3200017:
				addElixir(pc, itemId);
				break;
			case 3200101:
				addExp(pc);
				break;
			case 3200102:
				addFriendship(pc, 10);
				break;
			case 3200103:
				addFriendship(pc, 100);
				break;
			case 3200104:
				enableCombo(pc);
				break;
			case 3200013:
				changeName(pc);
				break;
			case 3200009:
				doBuff(pc, L1SkillId.PET_BUFF_YEGABAM, 1800);
				break;
			case 3200006:
				doBuff(pc, L1SkillId.PET_BUFF_GROW, 1800);
				break;
			case 3200020:
				doBuff(pc, L1SkillId.PET_BUFF_EIN, 1800);
				break;
			case 3200021:
				doBuff(pc, L1SkillId.PET_BUFF_SKY, 1800);
				break;
			case 3200007:
				doBuff(pc, L1SkillId.PET_BUFF_BLOOD, 1200);
				break;
			case 3200008:
				useYasuBlood(pc);
				break;
			case 3200010:
				useIcebox(pc);
				break;
			default:
				if (itemId >= 3200051 && itemId <= 3200071) {
					creatAmulet(pc, itemId);
				}
				break;
			}
		}
	}
	
	L1PetInstance getCompanion(L1PcInstance pc) {
	    L1PetInstance companion = pc.getPet();
	    if (companion == null) {
	    	return null;
	    }
	    if (companion.isDead()) {
	    	return null;
	    }
	    return companion;
	}
	
	void creatAmulet(L1PcInstance pc, int itemId){
	    L1Npc npc = NpcTable.getInstance().getTemplate(getItem().getEtcValue());
	    if (npc == null) {
	    	return;
	    }
	    CompanionT.ClassInfoT.ClassT classT = CompanionCommonBinLoader.getClass(npc.getClassId());
	    if (classT == null) {
	    	System.out.println(String.format("[CompanionItem] COMPANION_CLASS_DATA_NOT_FOUND : CLASS_ID(%d)", npc.getClassId()));
	    	return;
	    }
	    
	    // 목걸이 생성
	    L1ItemInstance amulet = ItemTable.getInstance().createItem(L1ItemId.PET_AMULET);
	    if (amulet == null) {
	    	return;
	    }
	    L1Pet pet = CharacterCompanionTable.getInstance().NewPetAdd(npc, classT, amulet.getId(), IdFactory.getInstance().nextId());
	    if (pet == null) {
	    	return;
	    }
	    amulet.setIdentified(true);
	    pc.getInventory().storeItem(amulet);
	    pc.getInventory().removeItem(this);
	    pc.sendPackets(new S_ServerMessage(403, amulet.getViewName()), true);
	    pc.sendPackets(new S_ServerMessage(5280, pet.getName()), true);
	}
	
	void useAmulet(L1PcInstance pc){
		L1PetInstance companion = getCompanion(pc);
	    Object[] summonlist = pc.getPetList().values().toArray();
		for (Object Object : summonlist) {
			if (Object instanceof L1SummonInstance) {
				pc.sendPackets(L1ServerMessage.sm5276);// 지금은 펫을 소환할 수 업습니다.
				return;
			}
		}
		
		// 이미 소환중
	    if (companion != null) {
	        if (companion.getAmuletId() != this.getId()) {
	            if (!companion.isDead()) {
	            	companion.delete(); 
	            }
	            return;
	        }
	        if (companion.isDead()) {
	            pc.sendPackets(L1ServerMessage.sm5277);// 지금은 부활할 수 없습니다
	            return;
	        }
	        companion.delete();
	        return;
	    }
	    
	    // 소환
	    L1Pet info = CharacterCompanionTable.getInstance().getTemplate(this.getId());
	    if (info == null) {
	    	return;
	    }
	    L1PetInstance.summoned(pc, info);
	}
	
	void heal_potion(L1PcInstance pc, int itemId){
		L1PetInstance companion = getCompanion(pc);
	    if (companion == null) {
	    	return;
	    }
	    if (pc.getInventory().consumeItem(this, 1)) {
	        int min_val	= itemId == 3200003 ? 15 : itemId == 3200004 ? 40 : 80;
	        int max_val	= itemId == 3200003 ? 35 : itemId == 3200004 ? 60 : 100;
	        int	healHp	= min_val + CommonUtil.random(max_val - min_val + 1);
	        companion.setCurrentHp(companion.getCurrentHp() + companion.get_potionHP() + healHp);
	        companion.broadcastPacket(new S_Effect(companion.getId(), 17514), true);
	    }
	}
	
	void enableCombo(L1PcInstance pc){
		L1PetInstance companion = getCompanion(pc);
	    if (companion == null) {
	    	return;
	    }
	    if (!companion.isCombo() && pc.getInventory().removeItem(this, 1) == 1) {
	    	companion.startCombo();
	    }
	}
	
	void addFriendship(L1PcInstance pc, int value){
		L1PetInstance companion = getCompanion(pc);
	    if (companion == null) {
	    	return;
	    }
	    companion.add_friend_ship_marble(value);
	    pc.getInventory().removeItem(this, 1);
	}
	
	void addExp(L1PcInstance pc){
		L1PetInstance companion = getCompanion(pc);
	    if (companion == null) {
	    	return;
	    }
	    if (pc.getInventory().removeItem(this, 1) == 1) {
	        int addExp = (int)(ExpTable.getNeedExpNextLevel(52) * 0.005D);
	        companion.getExpHandler().addExp(addExp);
	    }
	}
	
	void addElixir(L1PcInstance pc, int itemId){
		L1PetInstance companion = getCompanion(pc);
	    if (companion == null) {
	    	return;
	    }
	    int max_count = IntRange.ensure((companion.getLevel() - 45) / 5, 1, Config.COMPANION.ELIXIR_MAX_VALUE);
	    if (companion.getLevel() < Config.COMPANION.ELIXIR_USE_LEVEL || max_count <= companion.getPetInfo().get_elixir_use_count()) {
	        pc.sendPackets(L1ServerMessage.sm4473);
	        return;
	    }
	    if (itemId == 3200015) {
	        if (companion.getAbility().getTotalStr() < Config.COMPANION.STAT_MAX_VALUE && pc.getInventory().removeItem(this, 1) == 1) {
	        	companion.getPetInfo().addAddStr(1);
	        	companion.getAbility().addAddedStr(1);
	            companion.getPetInfo().add_elixir_use_count(1);
	            companion.doStatBonus(true);
	            CharacterCompanionTable.getInstance().ChangeStat(companion.getPetInfo());
	            companion.send_companion_card();
	        } else {
	            pc.sendPackets(L1ServerMessage.sm4473);
	        }
	    } else if (itemId == 3200016) {
	        if ((companion.getAbility().getTotalCon() < Config.COMPANION.STAT_MAX_VALUE) && (pc.getInventory().removeItem(this, 1) == 1)) {
	        	companion.getPetInfo().addAddCon(1);
	        	companion.getAbility().addAddedCon(1);
	        	companion.getPetInfo().add_elixir_use_count(1);
	        	companion.doStatBonus(true);
	            CharacterCompanionTable.getInstance().ChangeStat(companion.getPetInfo());
	            companion.send_companion_card();
	        } else {
	            pc.sendPackets(L1ServerMessage.sm4473);
	        }
	    } else if (itemId == 3200017) {
	        if ((companion.getAbility().getTotalInt() < Config.COMPANION.STAT_MAX_VALUE) && (pc.getInventory().removeItem(this, 1) == 1)) {
	        	companion.getPetInfo().addAddInt(1);
	        	companion.getAbility().addAddedInt(1);
	        	companion.getPetInfo().add_elixir_use_count(1);
	        	companion.doStatBonus(true);
	            CharacterCompanionTable.getInstance().ChangeStat(companion.getPetInfo());
	            companion.send_companion_card();
	        } else {
	            pc.sendPackets(L1ServerMessage.sm4473);
	        }
	    }
	}
	
	void resetStat(L1PcInstance pc){
		L1PetInstance companion = getCompanion(pc);
	    if (companion == null) {
	    	return;
	    }
	    if (companion.getLevel() < 10) {
	    	pc.sendPackets(L1SystemMessage.PET_RETURN_STAT_LEVEL);
	    } else if (pc.getInventory().removeItem(this, 1) == 1) {
	    	companion.resetStat();
	    }
	}
	
	void changeName(L1PcInstance pc){
		L1PetInstance companion = getCompanion(pc);
	    if (companion == null) {
	    	return;
	    }
	    pc.sendPackets(S_MessageYN.CHAR_NAME_CHANGE_YN);
	}
	
	void useYasuBlood(L1PcInstance pc){
		L1PetInstance companion = getCompanion(pc);
	    if (companion == null) {
	    	return;
	    }
	    int petItemObjId = companion.getAmuletId();
	    L1Pet template = CharacterCompanionTable.getInstance().getTemplate(petItemObjId);
	    if (pc.getInventory().removeItem(this, 1) == 1) {
	    	template.add_friend_ship_guage(50000);
	    	if (template.get_friend_ship_guage() >= Config.COMPANION.COMBO_ENABLE_POINT) { // 100프로 달성
	    		template.add_friend_ship_marble(Config.COMPANION.FRIENDSHIP_MARBLE_VALUE);
		        if (pc != null) {
		        	pc.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.FRIENDSHIP, companion), true);
		        }
		        template.set_friend_ship_guage(0);
		        companion.startCombo(); //투지 발동
		    } else {
		    	if (pc != null) {
		    		pc.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.FRIENDSHIP_GUAGE, companion), true);
		    	}
		    }
	    }
	}
	
	void doBuff(L1PcInstance pc, int buff_id, int duration) {
		L1PetInstance companion = getCompanion(pc);
	    if (companion == null) {
	    	return;
	    }
	    if (!companion.getSkill().hasSkillEffect(buff_id)) {
			companion.doBuffBonus(buff_id, duration, true);
			pc.getInventory().removeItem(this, 1);
	    } else {
	    	pc.sendPackets(L1ServerMessage.sm1413);
	    }
	}
	
	@SuppressWarnings("deprecation")
	void useIcebox(L1PcInstance pc) {
		Calendar rightNow = Calendar.getInstance();
		int ItemTemp = 3600000 * 24;
		Timestamp LastUsed = this.getLastUsed();
		if (LastUsed == null || rightNow.getTimeInMillis() > LastUsed.getTime() + ItemTemp) {
			L1ItemInstance Item = pc.getInventory().storeItem(3200008, 1);//야수의 피
			pc.sendPackets(new S_ServerMessage(403, Item.getItem().getDesc() + "(1)"), true);
			this.setChargeCount(this.getChargeCount() - 1);
			pc.getInventory().updateItem(this, L1PcInventory.COL_CHARGE_COUNT);
			if (this.getChargeCount() == 0) {
				pc.getInventory().removeItem(this);
			}
			this.setLastUsed(new Timestamp(rightNow.getTimeInMillis()));
		} else {
			long i = (LastUsed.getTime() + ItemTemp) - rightNow.getTimeInMillis();
			Calendar cal = (Calendar)rightNow.clone();
			cal.setTimeInMillis(cal.getTimeInMillis() + i);
			StringBuffer sb = new StringBuffer();
			/*sb.append(i / 60000);
			sb.append("분 후(");
			if (cal.getTime().getHours() < 10) {
				sb.append(StringUtil.ZeroString).append(cal.getTime().getHours()).append(StringUtil.ColonString);
			} else {
				sb.append(cal.getTime().getHours()).append(StringUtil.ColonString);
			}
			if (cal.getTime().getMinutes() < 10) {
				sb.append(StringUtil.ZeroString).append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
			} else {
				sb.append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
			}*/

			sb.append("Available in ");
			sb.append(i / 60000);
			sb.append(" minutes (");			
			if (cal.getTime().getHours() < 10) {
				sb.append(StringUtil.ZeroString).append(cal.getTime().getHours()).append(StringUtil.ColonString);
			} else {
				sb.append(cal.getTime().getHours()).append(StringUtil.ColonString);
			}
			if (cal.getTime().getMinutes() < 10) {
				sb.append(StringUtil.ZeroString).append(cal.getTime().getMinutes()).append(").");
			} else {
				sb.append(cal.getTime().getMinutes()).append(").");
			}

			pc.sendPackets(new S_SystemMessage(sb.toString(), true), true);
		}
		rightNow.clear();
	}
}

