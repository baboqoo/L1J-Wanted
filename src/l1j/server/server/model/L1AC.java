package l1j.server.server.model;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.utils.IntRange;

public class L1AC {
	private L1PcInstance pc;
	private boolean isPc;
	private int ac, baseAc;
	
	L1AC(L1Character owner) {
		if (owner instanceof L1PcInstance) {
			isPc	= true;
			pc		= (L1PcInstance) owner;
		}
	}

	public int getAc() {
		return ac + baseAc;
	}
	
	public void setBaseAc(int i) {
		baseAc = i;
	}

	public void addAc(int i) {
		setAc(ac + i);
	}

	public void setAc(int i) {
		ac = IntRange.ensure(i, -999, 999);
		if (isPc) {
			pc.sendPackets(new S_OwnCharAttrDef(pc), true);
		}
	}
	
}

