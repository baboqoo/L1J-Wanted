package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.CharSoldierTable;
import l1j.server.server.datatables.SoldierTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CloseList;
import l1j.server.server.serverpackets.S_SoldierGive;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Soldier;

public class C_SoldierGive extends ClientBasePacket {

	private static final String C_SOLDIER_GIVE = "[C] C_SoldierGive";

	public C_SoldierGive(byte abyte0[], GameClient clientthread) {
		super(abyte0);
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		try {
			// SoldierGiveSelect.java 여야 하지만 길어서 셀렉트는 생략.
			int objid = readD(); // 말 건 npc id
			int type = readH(); // 선택

			pc._soldierType = 1;
			int castle_id = pc.getClan().getCastleId();
			L1Soldier soldier = SoldierTable.getInstance().getSoldierTable(castle_id);

			int count = 0;

			switch (type) {
			case 0:
				if (soldier.getSoldier1() <= 0) {
//AUTO SRM: 					S_SystemMessage sm4 = new S_SystemMessage(soldier.getSoldier1Name() + "는 고용 된 용병이 없습니다."); // CHECKED OK
					S_SystemMessage sm4 = new S_SystemMessage(soldier.getSoldier1Name()  + S_SystemMessage.getRefText(123), true);
					pc.sendPackets(sm4, true);
					S_CloseList cl4 = new S_CloseList(objid);
					pc.sendPackets(cl4, true);
					return;
				}
				count = soldier.getSoldier1();
				break;
			case 1:
				if (soldier.getSoldier2() <= 0) {
//AUTO SRM: 					S_SystemMessage sm4 = new S_SystemMessage(soldier.getSoldier2Name() + "는 고용 된 용병이 없습니다."); // CHECKED OK
					S_SystemMessage sm4 = new S_SystemMessage(soldier.getSoldier2Name()  + S_SystemMessage.getRefText(123), true);
					pc.sendPackets(sm4, true);
					S_CloseList cl4 = new S_CloseList(objid);
					pc.sendPackets(cl4, true);
					return;
				}
				count = soldier.getSoldier2();
				break;
			case 2:
				if (soldier.getSoldier3() <= 0) {
//AUTO SRM: 					S_SystemMessage sm4 = new S_SystemMessage(soldier.getSoldier3Name() + "는 고용 된 용병이 없습니다."); // CHECKED OK
					S_SystemMessage sm4 = new S_SystemMessage(soldier.getSoldier3Name()  + S_SystemMessage.getRefText(123), true);
					pc.sendPackets(sm4, true);
					S_CloseList cl4 = new S_CloseList(objid);
					pc.sendPackets(cl4, true);
					return;
				}
				count = soldier.getSoldier3();
				break;
			case 3:
				if (soldier.getSoldier4() <= 0) {
//AUTO SRM: 					S_SystemMessage sm4 = new S_SystemMessage(soldier.getSoldier4Name() + "는 고용 된 용병이 없습니다."); // CHECKED OK
					S_SystemMessage sm4 = new S_SystemMessage(soldier.getSoldier4Name()  + S_SystemMessage.getRefText(123), true);
					pc.sendPackets(sm4, true);
					S_CloseList cl4 = new S_CloseList(objid);
					pc.sendPackets(cl4, true);
					return;
				}
				count = soldier.getSoldier4();
				break;
			default:
//AUTO SRM: 				S_SystemMessage sm = new S_SystemMessage("현재 이 부분은 에러사항이며 수정중에 있습니다."); // CHECKED OK
				S_SystemMessage sm = new S_SystemMessage(S_SystemMessage.getRefText(124), true);
				pc.sendPackets(sm, true);
				return;
			}

			for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 1)) {
				if (obj instanceof L1PcInstance) {
					int sumX = pc.getX() - obj.getX();
					int sumY = pc.getY() - obj.getY();

					L1PcInstance target = (L1PcInstance) obj;
					if (sumX == 1 && sumY == -2) {
						send(pc, target, objid, type, count);
						break;
					}
					if (sumX == 0 && sumY == -1) {
						send(pc, target, objid, type, count);
						break;
					}
					if (sumX == -1 && sumY == -1) {
						send(pc, target, objid, type, count);
						break;
					}
					send(pc, pc, objid, type, count);
				} else {
					send(pc, pc, objid, type, count);
				}
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	private void send(L1PcInstance pc, L1PcInstance target, int objid, int type, int count) {
		int a = CharSoldierTable.getInstance().SoldierCalculate(target.getId());
		int iscount = target.getAbility().getTotalCha() / 6 - a;
		if (count < iscount) {
			iscount = count;
		}
		// int iscount = count;
//AUTO SRM: 		S_SystemMessage sm = new S_SystemMessage(target.getName() + "님은 배치 되어 있는 용병이 " + a + "명 있습니다."); // CHECKED OK
		S_SystemMessage sm = new S_SystemMessage(target.getName()  + S_SystemMessage.getRefText(125) + a  + S_SystemMessage.getRefText(126), true);
		pc.sendPackets(sm, true);
		pc._soldierType = type;
		S_SoldierGive sg = new S_SoldierGive(target, objid, type, count, iscount);
		pc.sendPackets(sg, true);
	}

	@Override
	public String getType() {
		return C_SOLDIER_GIVE;
	}
}


