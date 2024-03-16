package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.L1Doll;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.datatables.MagicDollInfoTable.L1DollInfo;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.alchemy.S_SummonPetNoti;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;

public class MagicDoll extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public MagicDoll(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			call((L1PcInstance) cha);
		}
	}
	
	private void call(L1PcInstance pc) {
		if (pc.isInWarArea() || L1InterServer.isOccupyInter(pc.getMap().getInter())){
			pc.sendPackets(L1ServerMessage.sm563);// \f1 여기에서는 사용할 수 없습니다.
			return;
		}
		long curtime = System.currentTimeMillis() / 1000;
		if (pc.getQuizTime3() + 3 > curtime) {
			return;
		}
		
		boolean isAppear = true;
		if (pc.getDoll() != null) {
			if (pc.getDoll().getItemObjId() == this.getId()) {
				isAppear = false;
			}
			pc.getDoll().deleteDoll(true);// 소환중인 인형 존재시 제거
		}

		if (!isAppear) {
			return;
		}
		
		int consumCount = this.getBless() == 0 ? 100 : 50;
		switch(getItemId()){
		case L1Doll.DOLLITEMID_HATCHLING_1:
		case L1Doll.DOLLITEMID_HATCHLING_2:
		case L1Doll.DOLLITEMID_HATCHLING_3:
		case L1Doll.DOLLITEMID_HATCHLING_4:
			consumCount = 500;
			break;
		case L1Doll.DOLLITEMID_KNIGHTS:
			consumCount = 1;
			break;
		case L1Doll.DOLLITEMID_OCTORABBIT:
			consumCount = 100;
			break;
		}
		if (!pc.getInventory().consumeItem(L1ItemId.GEMSTONE, consumCount)) {
			pc.sendPackets(L1ServerMessage.sm337_GEMSTONE);
			return;
		}
		
		if (getItemId() == L1Doll.DOLLITEMID_KNIGHTS) {
			pc.getQuest().questItemUse(getItemId());// 기사단의 인형
		}

		L1DollInfo info = MagicDollInfoTable.getDollInfo(getItemId());
		if (info == null) {
			System.out.println(String.format("[MagicDoll] dollInfo null -> itemId: %d", getItemId()));
			return;
		}
		
		L1Npc template = NpcTable.getInstance().getTemplate(info.getDollNpcId());
		if (template == null) {
			System.out.println(String.format("[MagicDoll] npcTemplate null -> itemId: %d, npcId: %d", getItemId(), info.getDollNpcId()));
			return;
		}
		L1DollInstance doll = new L1DollInstance(template, pc, info, this);
		pc.send_effect(doll.getId(), 5935);
		pc.sendPackets(new S_SummonPetNoti(this, 1800, pc), true);
		pc.sendPackets(new S_OwnCharStatus(pc), true);
		pc.setQuizTime3(curtime);
	}
	
}

