package l1j.server.GameSystem.fatigue;

import l1j.server.server.model.Instance.L1PcInstance;

public class L1FatigueTimer implements Runnable {
	private L1PcInstance owner;
	private boolean active;
	
	public L1FatigueTimer(L1PcInstance owner) {
		this.owner	= owner;
		this.active	= true;
	}
	
	@Override
	public void run() {
		try {
			if (!active || owner == null || owner.getNetConnection() == null || owner.getFatigue() == null) {
				return;
			}
			owner.getFatigue().end();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void cancel(){
		active = false;
	}
}

