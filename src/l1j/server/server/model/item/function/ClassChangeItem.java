package l1j.server.server.model.item.function;

import java.sql.Connection;
import java.sql.PreparedStatement;

import l1j.server.L1DatabaseFactory;
import l1j.server.common.data.Gender;
import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.C_CharacterSelect;
import l1j.server.server.clientpackets.C_RestartComplete;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.L1BaseStatus;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.construct.skill.L1PassiveId;
import l1j.server.server.datatables.EquipSetTable;
import l1j.server.server.model.L1Ability;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1EquipmentSet;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.equip.S_EquipmentSet;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.polymorph.S_Polymorph;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;

public class ClassChangeItem extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public ClassChangeItem(L1Item item) {
		super(item);
	}
	
	private static final S_SystemMessage[] MESSAGES = {
//AUTO SRM: 		new S_SystemMessage("세이프티존에서 사용하실 수 있습니다."), new S_SystemMessage("혈맹에 가입한 케릭터는 사용할 수 없습니다."), new S_SystemMessage("같은 클래스로는 변경할 수 없습니다."), new S_SystemMessage("클래스변경으로 접속종료됩니다."), new S_SystemMessage("레벨이 다운된 캐릭입니다. 레벨업 후 이용하세요."), }; // CHECKED OK
		new S_SystemMessage(S_SystemMessage.getRefText(15), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1265), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1266), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(1267), true), 
		new S_SystemMessage(S_SystemMessage.getRefText(873), true)};

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (!pc.getMap().isSafetyZone(pc.getLocation())) {
				pc.sendPackets(MESSAGES[0]);
				return;
			}
			if (pc.getClanid() != 0) {
				pc.sendPackets(MESSAGES[1]);
				return;
			}
			if (!pc.isGm() && pc.getLevel() != pc.getHighLevel()) {
				pc.sendPackets(MESSAGES[4]);
				return;
			}
			int etcValue = getItem().getEtcValue();
			if (pc.getType() == etcValue) {
				pc.sendPackets(MESSAGES[2]);
				return;
			}
			change(pc, etcValue);
		}
	}
	
	void change(L1PcInstance pc, int changeType){
		pc.setType(changeType);
		pc.setClassId(pc.getGender() == Gender.MALE ? L1CharacterInfo.MALE_LIST[changeType] : L1CharacterInfo.FEMALE_LIST[changeType]);
		try {
			int class_size = L1CharacterInfo.MALE_LIST.length;
			java.util.LinkedList<L1ItemInstance> del_items = null;
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				int item_id = item.getItemId();
				if ((item_id >= 6670 && item_id <= 6670 + class_size)
						|| (item_id >= 6690 && item_id <= 6690 + class_size)
						|| (item_id >= 6710 && item_id <= 6710 + class_size)) {
					if (del_items == null) {
						del_items = new java.util.LinkedList<L1ItemInstance>();
					}
					del_items.add(item);
				}
				if (item.isEquipped()) {
					pc.getInventory().setEquipped(item, false);
				}
			}
			if (del_items != null) {
				for (L1ItemInstance del_item : del_items) {
					pc.getInventory().removeItem(del_item);
				}
			}
			pc.sendPackets(new S_CharVisualUpdate(pc), true);
		} catch (Exception e) {
			//System.out.println(">>>>>>>>>>>>>>>>>>>클래스변경중 아이템착용해제 에러 : " + this.getItemId());
			System.out.println(">>>>>>>>>>>>>>>>>>> Error while changing classes, unequipping item: " + this.getItemId());
		}
		
		if (pc.isPassiveStatus(L1PassiveId.GLORY_EARTH)) {
			pc.getPassiveSkill().remove(L1PassiveId.GLORY_EARTH);
		}
		deleteSpell(pc);
		
		L1EquipmentSet set = pc.getEquipmentSet();
		set.reset();
		EquipSetTable.getInstance().updateAll(pc, set);
		pc.sendPackets(new S_EquipmentSet(set), true);
		
		pc.setSpriteId(pc.getClassId());
		pc.broadcastPacketWithMe(new S_Polymorph(pc.getId(), pc.getClassId(), pc.getCurrentWeapon()), true);
		pc.getInventory().removeItem(this, 1);
		
		L1Ability ability = pc.getAbility();
		L1BaseStatus base = ability.getClassChangeMinStat(pc.getClassId());
		
		ability.init();
		ability.setBaseStr(base.get_base_str());
		ability.setBaseDex(base.get_base_dex());
		ability.setBaseCon(base.get_base_con());
		ability.setBaseWis(base.get_base_wis());
		ability.setBaseCha(base.get_base_cha());
		ability.setBaseInt(base.get_base_int());
		
		// 레벨구간에 의한 엘릭서 수치가 다르다면 변경해준다.(스초진행시 버그 체크 방지)
		int level = pc.getLevel();
		int limit_elixir_count = ((level - 48) >> 1) + (level >= 80 ? 1 : 0) + (level >= 90 ? 1 : 0) + (level >= 100 ? 2 : 0);
		if (pc.getElixirStats() > limit_elixir_count) {
			pc.setElixirStats(limit_elixir_count);
		}
		
		try {
			pc.save();
			pc.saveInventory();
		} catch (Exception e) {
			//System.out.println(">>>>>>>>>>>>>>>>>>>클래스변경중 DB저장 에러 : " + pc.getName());
			System.out.println(">>>>>>>>>>>>>>>>>>> Error while saving to the database during class change: " + pc.getName());

		}
		pc.sendPackets(MESSAGES[3]);
		pc.getTeleport().start(32723 + CommonUtil.random(10), 32851 + CommonUtil.random(10), (short) 5166, 5, true);
		pc.returnStat();
		
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				try {
					GameClient clnt = pc.getNetConnection();
					C_RestartComplete.restartProcess(pc);
					Account acc		= clnt.getAccount();
					int count		= acc.countCharacters();
					clnt.sendPacket(new S_CharAmount(count, acc.getCharSlot()));
					if (count > 0) {
						C_CharacterSelect.sendCharPacks(clnt);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}, 500L);
	}
	
	void deleteSpell(L1PcInstance pc) {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("DELETE FROM character_skills_active WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
			SQLUtil.close(pstm);
			
			pstm	= con.prepareStatement("DELETE FROM character_skills_passive WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
		} catch (Exception e) {
			//System.out.println("클래스변경 스킬삭제 에러 : " + e);
			System.out.println("Error while deleting skills during class change: " + e);
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
}


