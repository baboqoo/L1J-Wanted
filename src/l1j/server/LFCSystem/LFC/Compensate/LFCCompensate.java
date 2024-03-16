package l1j.server.LFCSystem.LFC.Compensate;

import l1j.server.server.model.Instance.L1PcInstance;

public interface LFCCompensate {
	public void setPartition(int i);
	public int 	getPartition();
	public void setIdentity(int i);
	public void setQuantity(int i);
	public void setLevel(int i);
	public void compensate(L1PcInstance pc);
}

