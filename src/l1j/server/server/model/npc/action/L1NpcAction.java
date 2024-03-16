package l1j.server.server.model.npc.action;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtml;

public interface L1NpcAction {
	public boolean acceptsRequest(String actionName, L1PcInstance pc, L1Object obj);
	public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj, byte args[]);
	public L1NpcHtml executeWithAmount(String actionName, L1PcInstance pc, L1Object obj, int amount);
}
