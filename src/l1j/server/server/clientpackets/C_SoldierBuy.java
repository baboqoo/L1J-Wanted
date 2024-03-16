package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.SoldierTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.templates.L1Soldier;

public class C_SoldierBuy extends ClientBasePacket {

	private static final String C_SOLDIER_BUY = "[C] C_SoldierBuy";

	public class SelectedSoldierInfo {
		int number;
		int count;
		int price;
	}

	public C_SoldierBuy(byte abyte0[], GameClient clientthread) {
		super(abyte0);
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		try {
			@SuppressWarnings("unused")
			int npcid = readD(); // 말 건 npc id
			int count = readH(); // 목록에서 몇개 선택?
			SelectedSoldierInfo[] soldierInfor = new SelectedSoldierInfo[count];

			int totalCount = 0, totalPrice = 0;
			for (int i = 0; i < soldierInfor.length; i++) {
				soldierInfor[i] = new SelectedSoldierInfo();
				soldierInfor[i].number = readH(); // 목록에서 몇번째 용병인가
				soldierInfor[i].count = readH(); // 용병 마릿수
				soldierInfor[i].price = readH(); // 가격

				totalCount += soldierInfor[i].count;
				totalPrice += soldierInfor[i].price;
			}

			int castle_id = pc.getClan().getCastleId();

			L1Soldier soldier = SoldierTable.getInstance().getSoldierTable(castle_id);
			L1Castle l1catle = CastleTable.getInstance().getCastleTable(castle_id);

			int totalsoldier = soldier.getSoldier1() + soldier.getSoldier2() + soldier.getSoldier3() + soldier.getSoldier4();
			int clanBossChar = pc.getAbility().getTotalCha();

			if (totalsoldier + totalCount > clanBossChar) {
				// 카리 부족으로 실패
				// 메세지 보내나?
//AUTO SRM: 				S_SystemMessage sm = new S_SystemMessage("카리스마 부족으로 더 이상 고용 할 수 없습니다."); // CHECKED OK
				S_SystemMessage sm = new S_SystemMessage(S_SystemMessage.getRefText(118), true);
				pc.sendPackets(sm, true);
//AUTO SRM: 				S_SystemMessage sm2 = new S_SystemMessage("고용 되어있는 수: " + totalsoldier + " 하려는 수: " + totalCount); // CHECKED OK
				S_SystemMessage sm2 = new S_SystemMessage(S_SystemMessage.getRefText(119) + totalsoldier  + S_SystemMessage.getRefText(120) + totalCount, true);
				pc.sendPackets(sm2, true);
				return;
			}

			// 모든 검사 했으니까, 용병 구입 처리
			for (int i = 0; i < count; i++) {
				switch (soldierInfor[i].number) {
				case 0:
					int sum1 = soldier.getSoldier1() + soldierInfor[i].count;
					soldier.setSoldier1(sum1);
					//S_SystemMessage sm = new S_SystemMessage(soldier.getSoldier1Name() + "를 " + soldierInfor[i].count + " 명 고용 하였습니다.");
					S_ServerMessage sm = new S_ServerMessage(S_ServerMessage.getStringIdx(115), soldier.getSoldier1Name(), String.valueOf(soldierInfor[i].count));
					pc.sendPackets(sm, true);
					break;
				case 1:
					int sum2 = soldier.getSoldier2() + soldierInfor[i].count;
					soldier.setSoldier2(sum2);
					//S_SystemMessage sm2 = new S_SystemMessage(soldier.getSoldier2Name() + "를 " + soldierInfor[i].count + " 명 고용 하였습니다.");
					S_ServerMessage sm2 = new S_ServerMessage(S_ServerMessage.getStringIdx(115), soldier.getSoldier2Name(), String.valueOf(soldierInfor[i].count));
					pc.sendPackets(sm2, true);
					break;
				case 2:
					int sum3 = soldier.getSoldier3() + soldierInfor[i].count;
					soldier.setSoldier3(sum3);
					//S_SystemMessage sm3 = new S_SystemMessage(soldier.getSoldier3Name() + "를 " + soldierInfor[i].count + " 명 고용 하였습니다.");
					S_ServerMessage sm3 = new S_ServerMessage(S_ServerMessage.getStringIdx(115), soldier.getSoldier3Name(), String.valueOf(soldierInfor[i].count));
					pc.sendPackets(sm3, true);
					break;
				case 3:
					int sum4 = soldier.getSoldier4() + soldierInfor[i].count;
					soldier.setSoldier4(sum4);
					//S_SystemMessage sm4 = new S_SystemMessage(soldier.getSoldier4Name() + "를 " + soldierInfor[i].count + " 명 고용 하였습니다.");
					S_ServerMessage sm4 = new S_ServerMessage(S_ServerMessage.getStringIdx(115), soldier.getSoldier4Name(), String.valueOf(soldierInfor[i].count));
					pc.sendPackets(sm4, true);
					break;
				default:
					// do nothing;
				}
			}
			int castleMoney = l1catle.getPublicMoney() - totalPrice;
			l1catle.setPublicMoney(castleMoney);
			CastleTable.getInstance().updateCastle(l1catle);
			SoldierTable.getInstance().updateSoldier(soldier);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_SOLDIER_BUY;
	}
}


