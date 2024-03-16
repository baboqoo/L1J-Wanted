package l1j.server.server.model.item;

import l1j.server.server.model.L1PcInventory;

/**
 * 오만의 탑 부적 담당 클래스
 * @author LinOffice
 */
public class L1OmanTowerAmulet {
	private L1PcInventory inventory;
	private boolean normal1, normal2, normal3, normal4, normal5, normal6, normal7, normal8, normal9, normal10;// 일반 부적
	private boolean amulet1, amulet2, amulet3, amulet4, amulet5, amulet6, amulet7, amulet8, amulet9, amulet10, blessAmulet;// 지배 부적
	
	/**
	 * 오만의 탑 각 층 텔레포트 가능 여부
	 * @param floor
	 * @return true or false
	 */
	public boolean isOmanTeleportable(int floor){
		switch(floor){
		case 101:return amulet1 || blessAmulet;
		case 102:return amulet2 || blessAmulet;
		case 103:return amulet3 || blessAmulet;
		case 104:return amulet4 || blessAmulet;
		case 105:return amulet5 || blessAmulet;
		case 106:return amulet6 || blessAmulet;
		case 107:return amulet7 || blessAmulet;
		case 108:return amulet8 || blessAmulet;
		case 109:return amulet9 || blessAmulet;
		case 110:return amulet10 || blessAmulet;
		case 111:return inventory.getCollection().isSasinGraceStatus();
		default:return false;
		}
	}
	
	/**
	 * 오만의 탑 각 층 입장 가능 여부
	 * @param floor
	 * @return true or false
	 */
	public boolean isOmanEnterable(int floor) {
		switch(floor){
		case 101:return normal1 || amulet1 || blessAmulet;
		case 102:return normal2 || amulet2 || blessAmulet;
		case 103:return normal3 || amulet3 || blessAmulet;
		case 104:return normal4 || amulet4 || blessAmulet;
		case 105:return normal5 || amulet5 || blessAmulet;
		case 106:return normal6 || amulet6 || blessAmulet;
		case 107:return normal7 || amulet7 || blessAmulet;
		case 108:return normal8 || amulet8 || blessAmulet;
		case 109:return normal9 || amulet9 || blessAmulet;
		case 110:return normal10 || amulet10 || blessAmulet;
		default:return false;
		}
	}
	
	public L1OmanTowerAmulet(L1PcInventory inventory) {
		this.inventory = inventory;
	}
	
	public void setNormal1(boolean val) {
		this.normal1 = val;
	}
	public void setNormal2(boolean val) {
		this.normal2 = val;
	}
	public void setNormal3(boolean val) {
		this.normal3 = val;
	}
	public void setNormal4(boolean val) {
		this.normal4 = val;
	}
	public void setNormal5(boolean val) {
		this.normal5 = val;
	}
	public void setNormal6(boolean val) {
		this.normal6 = val;
	}
	public void setNormal7(boolean val) {
		this.normal7 = val;
	}
	public void setNormal8(boolean val) {
		this.normal8 = val;
	}
	public void setNormal9(boolean val) {
		this.normal9 = val;
	}
	public void setNormal10(boolean val) {
		this.normal10 = val;
	}
	
	public void setAmulet1(boolean amulet1) {
		this.amulet1 = amulet1;
	}
	public void setAmulet2(boolean amulet2) {
		this.amulet2 = amulet2;
	}
	public void setAmulet3(boolean amulet3) {
		this.amulet3 = amulet3;
	}
	public void setAmulet4(boolean amulet4) {
		this.amulet4 = amulet4;
	}
	public void setAmulet5(boolean amulet5) {
		this.amulet5 = amulet5;
	}
	public void setAmulet6(boolean amulet6) {
		this.amulet6 = amulet6;
	}
	public void setAmulet7(boolean amulet7) {
		this.amulet7 = amulet7;
	}
	public void setAmulet8(boolean amulet8) {
		this.amulet8 = amulet8;
	}
	public void setAmulet9(boolean amulet9) {
		this.amulet9 = amulet9;
	}
	public void setAmulet10(boolean amulet10) {
		this.amulet10 = amulet10;
	}
	public void setBlessAmulet(boolean blessAmulet) {
		this.blessAmulet = blessAmulet;
	}
	
	public void enable(int itemId) {
		switch (itemId) {
		case 560028:
			setBlessAmulet(true);
			break;
		case 830022:
		case 840022:
			setAmulet1(true);
			break;
		case 830023:
		case 840023:
			setAmulet2(true);
			break;
		case 830024:
		case 840024:
			setAmulet3(true);
			break;
		case 830025:
		case 840025:
			setAmulet4(true);
			break;
		case 830026:
		case 840026:
			setAmulet5(true);
			break;
		case 830027:
		case 840027:
			setAmulet6(true);
			break;
		case 830028:
		case 840028:
			setAmulet7(true);
			break;
		case 830029:
		case 840029:
			setAmulet8(true);
			break;
		case 830030:
		case 840030:
			setAmulet9(true);
			break;
		case 830031:
		case 840031:
			setAmulet10(true);
			break;
		case 830012:
			setNormal1(true);
			break;
		case 830013:
			setNormal2(true);
			break;
		case 830014:
			setNormal3(true);
			break;
		case 830015:
			setNormal4(true);
			break;
		case 830016:
			setNormal5(true);
			break;
		case 830017:
			setNormal6(true);
			break;
		case 830018:
			setNormal7(true);
			break;
		case 830019:
			setNormal8(true);
			break;
		case 830020:
			setNormal9(true);
			break;
		case 830021:
			setNormal10(true);
			break;
		default:
			break;
		}
	}
	
	public void noEnable(int itemId) {
		switch (itemId) {
		case 560028:
			if (inventory.checkItem(560028)) {
				return;
			}
			setBlessAmulet(false);
			break;
		case 830022:
		case 840022:
			if (inventory.checkItem(830022) || inventory.checkItem(840022)) {
				return;
			}
			setAmulet1(false);
			break;
		case 830023:
		case 840023:
			if (inventory.checkItem(830023) || inventory.checkItem(840023)) {
				return;
			}
			setAmulet2(false);
			break;
		case 830024:
		case 840024:
			if (inventory.checkItem(830024) || inventory.checkItem(840024)) {
				return;
			}
			setAmulet3(false);
			break;
		case 830025:
		case 840025:
			if (inventory.checkItem(830025) || inventory.checkItem(840025)) {
				return;
			}
			setAmulet4(false);
			break;
		case 830026:
		case 840026:
			if (inventory.checkItem(830026) || inventory.checkItem(840026)) {
				return;
			}
			setAmulet5(false);
			break;
		case 830027:
		case 840027:
			if (inventory.checkItem(830027) || inventory.checkItem(840027)) {
				return;
			}
			setAmulet6(false);
			break;
		case 830028:
		case 840028:
			if (inventory.checkItem(830028) || inventory.checkItem(840028)) {
				return;
			}
			setAmulet7(false);
			break;
		case 830029:
		case 840029:
			if (inventory.checkItem(830029) || inventory.checkItem(840029)) {
				return;
			}
			setAmulet8(false);
			break;
		case 830030:
		case 840030:
			if (inventory.checkItem(830030) || inventory.checkItem(840030)) {
				return;
			}
			setAmulet9(false);
			break;
		case 830031:
		case 840031:
			if (inventory.checkItem(830031) || inventory.checkItem(840031)) {
				return;
			}
			setAmulet10(false);
			break;
		case 830012:
			if (inventory.checkItem(830012)) {
				return;
			}
			setNormal1(false);
			break;
		case 830013:
			if (inventory.checkItem(830013)) {
				return;
			}
			setNormal2(false);
			break;
		case 830014:
			if (inventory.checkItem(830014)) {
				return;
			}
			setNormal3(false);
			break;
		case 830015:
			if (inventory.checkItem(830015)) {
				return;
			}
			setNormal4(false);
			break;
		case 830016:
			if (inventory.checkItem(830016)) {
				return;
			}
			setNormal5(false);
			break;
		case 830017:
			if (inventory.checkItem(830017)) {
				return;
			}
			setNormal6(false);
			break;
		case 830018:
			if (inventory.checkItem(830018)) {
				return;
			}
			setNormal7(false);
			break;
		case 830019:
			if (inventory.checkItem(830019)) {
				return;
			}
			setNormal8(false);
			break;
		case 830020:
			if (inventory.checkItem(830020)) {
				return;
			}
			setNormal9(false);
			break;
		case 830021:
			if (inventory.checkItem(830021)) {
				return;
			}
			setNormal10(false);
			break;
		default:
			break;
		}
	}

}

