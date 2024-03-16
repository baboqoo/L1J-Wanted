package l1j.server.server.construct;

import l1j.server.server.serverpackets.S_DeathPenaltyStatus;
import l1j.server.server.serverpackets.ServerBasePacket;

public enum L1RegionStatus {
	NORMAL(0,	0,		0),
	SAFETY(128,	0,		0),
	COMBAT(0,	128,	8),
	;
	private int globalDeathPenaltyStatus;
	private int expDeathPenaltyStatus;
	private int itemDeathPenaltyStatus;
	private ServerBasePacket deathPenaltyStatusPck;
	
	L1RegionStatus(int globalDeathPenaltyStatus, int expDeathPenaltyStatus, int itemDeathPenaltyStatus) {
		this.globalDeathPenaltyStatus	= globalDeathPenaltyStatus;
		this.expDeathPenaltyStatus		= expDeathPenaltyStatus;
		this.itemDeathPenaltyStatus		= itemDeathPenaltyStatus;
		this.deathPenaltyStatusPck		= new S_DeathPenaltyStatus(this);
	}
	
	public int getGlobalDeathPenaltyStatus() {
		return globalDeathPenaltyStatus;
	}
	public int getExpDeathPenaltyStatus() {
		return expDeathPenaltyStatus;
	}
	public int getItemDeathPenaltyStatus() {
		return itemDeathPenaltyStatus;
	}
	public ServerBasePacket getDeathPenaltyStatusPck() {
		return deathPenaltyStatusPck;
	}
	
	public static L1RegionStatus fromString(String str){
		switch(str){
		case "SAFETY":	return SAFETY;
		case "COMBAT":	return COMBAT;
		default:		return NORMAL;
		}
	}
}

