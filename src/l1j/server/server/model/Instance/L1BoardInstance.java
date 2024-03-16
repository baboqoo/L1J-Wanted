package l1j.server.server.model.Instance;

import l1j.server.server.controller.DollRaceController;
import l1j.server.server.serverpackets.S_Board;
import l1j.server.server.serverpackets.S_BoardRead;
import l1j.server.server.serverpackets.S_EnchantRanking;
import l1j.server.server.serverpackets.S_Ranking;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Npc;

public class L1BoardInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	public L1BoardInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		if (this.getNpcTemplate().getNpcId() == 999999) {// 버그베어 승률 게시판
			raceBoard(player);
		} else {
			player.sendPackets(new S_Board(this), true);
		}
	}

	public void onAction(L1PcInstance player, int number) {
		player.sendPackets(new S_Board(this, number), true);
	}

	public void onActionRead(L1PcInstance player, int number) {
		int npcId = this.getNpcTemplate().getNpcId();
		if (npcId == 500001) {// 랭킹 게시판
			player.sendPackets(new S_Ranking(player, number), true);
		} else if (npcId == 4200013) {// 인챈 게시판
			player.sendPackets(new S_EnchantRanking(player, number), true);
		} else {
			if (npcId == 500002 || npcId == 9200036) {//건의사항
				if (!player.isGm()) {
//AUTO SRM: 					player.sendPackets(new S_SystemMessage("운영자만 열람할 수 있습니다."), true); // CHECKED OK
					player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1017), true), true);
					return;
				}
			}
			player.sendPackets(new S_BoardRead(this, number), true);
		}
	}
	
	void raceBoard(L1PcInstance player) {
		int dollState = DollRaceController.getInstance().getDollState();
		switch (dollState) {
		case 0:// 표판매중
			player.sendPackets(new S_Board(this), true);
			break;
		case 1:// 경기중
//AUTO SRM: 			player.sendPackets(new S_SystemMessage("경기 중에는 보실 수 없습니다."), true); // CHECKED OK
			player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1018), true), true);
			break;
		case 2:// 다음경기준비중
//AUTO SRM: 			player.sendPackets(new S_SystemMessage("다음 경기를 준비 중 입니다."), true); // CHECKED OK
			player.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1019), true), true);
			break;
		}
	}
}


