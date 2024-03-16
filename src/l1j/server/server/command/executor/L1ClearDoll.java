package l1j.server.server.command.executor;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class L1ClearDoll implements L1CommandExecutor {
	private static class newInstance {
		public static final L1CommandExecutor INSTANCE = new L1ClearDoll();
	}
	public static L1CommandExecutor getInstance() {
		return newInstance.INSTANCE;
	}
	private L1ClearDoll() {}

	@Override
	public boolean execute(L1PcInstance pc, String cmdName, String arg) {
		if (pc == null) {
			return false;
		}
		int count=0, ccount=0;
		for(Object obj : L1World.getInstance().getObject()){
			if(obj instanceof L1DollInstance){
				L1DollInstance doll = (L1DollInstance) obj;
				if(doll.getMaster() == null || ((L1PcInstance) doll.getMaster()).getNetConnection() == null){
					count++;
					doll.deleteMe();
				} else
				  ccount++;
			}
		}
//AUTO SRM: 		pc.sendPackets(new S_SystemMessage("인형청소 갯수 - 주인X: " + count + "  주인접종: " + ccount), true); // CHECKED OK
		pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(321) + count  + S_SystemMessage.getRefText(322) + ccount, true), true);
		return true;
	}
}


