package l1j.server.GameSystem.inter;

import l1j.server.server.Account;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1InterServerModel {
	private int charId;
	private String charName;
	private Account account;
	private int interX;
	private int interY;
	private short interMapId;
	private int interHead;
	private int rollbackX;
	private int rollbackY;
	private short rollbackMapId;
	private L1PcInstance oldPc;
	private L1InterServer inter;
	
	public L1InterServerModel(int charId, String charName, Account account, 
			int interX, int interY, short interMapId, int interHead, 
			int rollbackX, int rollbackY, short rollbackMapId,
			L1PcInstance oldPc,
			L1InterServer inter) {
		this.charId			= charId;
		this.charName		= charName;
		this.account		= account;
		this.interX			= interX;
		this.interY			= interY;
		this.interMapId		= interMapId;
		this.interHead		= interHead;
		this.rollbackX		= rollbackX;
		this.rollbackY		= rollbackY;
		this.rollbackMapId	= rollbackMapId;
		this.oldPc			= oldPc;
		this.inter			= inter;
	}
	
	public int getCharId(){
		return charId;
	}
	public String getCharName(){
		return charName;
	}
	public Account getAccount() {
		return account;
	}
	public int getInterX() {
		return interX;
	}
	public void setInterX(int x){
		interX = x;
	}
	public int getInterY() {
		return interY;
	}
	public void setInterY(int y){
		interY = y;
	}
	public short getInterMapId() {
		return interMapId;
	}
	public void setInterMapId(short mapId){
		interMapId = mapId;
	}
	public int getInterHead() {
		return interHead;
	}
	public int getRollbackX() {
		return rollbackX;
	}
	public int getRollbackY() {
		return rollbackY;
	}
	public short getRollbackMapId() {
		return rollbackMapId;
	}
	public L1PcInstance getOldPc() {
		return oldPc;
	}
	public L1InterServer getInter() {
		return inter;
	}
}

