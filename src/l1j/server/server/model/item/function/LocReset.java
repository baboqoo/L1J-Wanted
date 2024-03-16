package l1j.server.server.model.item.function;

import java.sql.Connection;
import java.sql.PreparedStatement;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class LocReset extends L1ItemInstance {
	private static final long serialVersionUID = 1L;
	
	public LocReset(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			resetLoc((L1PcInstance) cha);
		}
	}
	
	private void resetLoc(L1PcInstance pc){
		Connection con			=	null;
		PreparedStatement pstm	=	null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET LocX=33432, LocY=32807, MapID=4 WHERE account_name=? AND MapID NOT IN (99,997,5166,39,34,701,2000)"); // 운영자의방,감옥
			pstm.setString(1, pc.getAccountName());
			pstm.execute();
			
			pc.getInventory().removeItem(this, 1);
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("계정내 모든 캐릭터의 좌표가 기란마을로 이동되었습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(1082), true), true);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
}


