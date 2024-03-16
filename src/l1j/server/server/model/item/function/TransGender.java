package l1j.server.server.model.item.function;

import l1j.server.common.data.Gender;
import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.C_CharacterSelect;
import l1j.server.server.clientpackets.C_RestartComplete;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.polymorph.S_Polymorph;
import l1j.server.server.templates.L1Item;

public class TransGender extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public TransGender(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if(cha instanceof L1PcInstance){
			L1PcInstance pc = (L1PcInstance) cha;
			if (!pc.getMap().isSafetyZone(pc.getLocation())) {
//AUTO SRM: 				pc.sendPackets(new S_SystemMessage("세이프티존에서 사용하실 수 있습니다."), true); // CHECKED OK
				pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(15), true), true);
				return;
			}
			trans(pc, this);
		}
	}
	
	private void trans(L1PcInstance pc, L1ItemInstance useItem){
		pc.setGender(pc.getGender().equals(Gender.MALE) ? Gender.FEMALE : Gender.MALE);
		pc.setClassId(pc.getGender().equals(Gender.MALE) ? L1CharacterInfo.MALE_LIST[pc.getType()] : L1CharacterInfo.FEMALE_LIST[pc.getType()]);
		pc.setSpriteId(pc.getClassId());
		pc.broadcastPacketWithMe(new S_Polymorph(pc.getId(), pc.getClassId(), pc.getCurrentWeapon()), true);
		pc.getInventory().removeItem(useItem, 1);
		
		try {
			pc.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 500L);
	}
}


