package l1j.server.server.templates;

import l1j.server.server.serverpackets.S_FreePVPRegionNoti;

public class L1FreePVPRegion {
	private int worldNumber;
	private boolean isFreePvpZone;
	private java.util.LinkedList<S_FreePVPRegionNoti.Box> box;
	private S_FreePVPRegionNoti pck;
	
	public L1FreePVPRegion(int worldNumber, boolean isFreePvpZone, java.util.LinkedList<S_FreePVPRegionNoti.Box> box) {
		this.worldNumber = worldNumber;
		this.isFreePvpZone = isFreePvpZone;
		this.box = box;
	}
	
	public int getWorldNumber() {
		return worldNumber;
	}
	public boolean isFreePvpZone() {
		return isFreePvpZone;
	}
	public java.util.LinkedList<S_FreePVPRegionNoti.Box> getBox() {
		return box;
	}
	public S_FreePVPRegionNoti getPck() {
		return pck;
	}
	public void setPck(S_FreePVPRegionNoti val) {
		pck = val;
	}
}

