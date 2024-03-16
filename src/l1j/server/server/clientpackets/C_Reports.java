package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.datatables.ReportTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class C_Reports extends ClientBasePacket {
	private static final String C_REPORTS = "[C] C_Reports";
	
	public C_Reports(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);	
		if (client == null) {
			return;
		}
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		readP(1); // 타입
		L1Object obj = L1World.getInstance().findObject(readD());
		if (obj instanceof L1PcInstance == false) {
			return;
		}
		ReportTable.getInstance().report(pc, (L1PcInstance) obj);
	}

	@Override
	public String getType() {
		return C_REPORTS;
	}
}
