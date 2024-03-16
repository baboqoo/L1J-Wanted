package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.CharacterEinhasadStatTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.einhasad.S_RestExpInfoNoti;
import l1j.server.server.serverpackets.einhasadpoint.S_EinhasadPointStatInfoNoti;
import l1j.server.server.templates.L1CharEinStat;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class ReturnStatus extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public ReturnStatus(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getLevel() < 55) {
				pc.sendPackets(L1SystemMessage.RETURN_STAT_LEVEL_FAIL);
				return;
			}
			switch(this.getItemId()){
			case 200000:
				resetStat(pc);
				break;
			case 200010:
				einStatReset(pc);
				break;
			}
		}
	}
	
	private void resetStat(L1PcInstance pc) {
		long curtime = System.currentTimeMillis() / 1000;
		if (pc.getQuizTime() + 10 > curtime) {
			pc.sendPackets(L1SystemMessage.DELAY_MSG);
			return;
		}
		if (pc.getNetConnection().isInterServer()) {
			pc.sendPackets(L1ServerMessage.sm647);
			return;
		}
		if (!pc.getMap().isSafetyZone(pc.getLocation())) {
			pc.sendPackets(L1ServerMessage.sm4608);
			return;
		}
		if (pc.getLevel() != pc.getHighLevel()) {
			pc.sendPackets(L1SystemMessage.LEVEL_DOWN_CHAR_FAIL);
			return;
		}
		pc.getInventory().removeItem(this, 1);
		pc.getTeleport().c_start(32723 + CommonUtil.random(10), 32851 + CommonUtil.random(10), (short) 5166, 5, true);
		pc.returnStat();
		pc.setQuizTime(curtime);
	}
	
	private void einStatReset(L1PcInstance pc) {
		CharacterEinhasadStatTable stat	=	CharacterEinhasadStatTable.getInstance();
		L1CharEinStat temp				=	stat.getEinSate(pc);
		if (temp == null) {
			return;
		}
		pc.getInventory().removeItem(this, 1);
		pc.getAbility().resetEinStat();
		stat.resetTemp(temp);
		pc.sendPackets(new S_EinhasadPointStatInfoNoti(pc, pc.getAbility()), true);
		pc.sendPackets(new S_RestExpInfoNoti(pc), true);
		pc.sendPackets(L1SystemMessage.EIN_STAT_RESET);
	}
}

