package l1j.server.server.model.map;

import l1j.server.server.construct.L1InterServer;
import l1j.server.server.datatables.MapBalanceTable.MapBalanceData;
import l1j.server.server.types.Point;

public abstract class L1Map {
	private static L1NullMap _nullMap = new L1NullMap();

	protected L1Map() {
	}

	public abstract int getId();

	public abstract int getBaseMapId();
	
	public abstract int getX();

	public abstract int getY();

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract int getTile(int x, int y);

	public abstract int getOriginalTile(int x, int y);

	public abstract boolean isInMap(Point pt);

	public abstract boolean isInMap(int x, int y);

	public abstract boolean isPassable(Point pt);

	public abstract boolean isPassable(int x, int y);

	public abstract boolean isPassable(Point pt, int heading);

	public abstract boolean isUserPassable(Point pt);

	public abstract boolean isUserPassable(int x, int y, int heading);

	public abstract boolean isPassable(int x, int y, int heading);

	public abstract boolean isAttackable(int x, int y, int heading, boolean pss);

	public abstract boolean isAttackableDoor(int x, int y, int heading);

	public abstract void setPassable(Point pt, boolean isPassable);

	public abstract void setPassable(int x, int y, boolean isPassable);

	public abstract void setDoorPassable(Point pt, boolean directionX, boolean isPassable);

	public abstract void setDoorPassable(int x, int y, boolean directionX, boolean isPassable);

	public abstract boolean isSafetyZone(Point pt);

	public abstract boolean isSafetyZone(int x, int y);

	public abstract boolean isCombatZone(Point pt);

	public abstract boolean isCombatZone(int x, int y);

	public abstract boolean isNormalZone(Point pt);

	public abstract boolean isNormalZone(int x, int y);

	public abstract boolean isUnderwater();

	public abstract boolean isMarkable();

	public abstract boolean isTeleportable();

	public abstract boolean isEscapable();

	public abstract boolean isUseResurrection();

	public abstract boolean isUsePainwand();

	public abstract boolean isEnabledDeathPenalty();

	public abstract boolean isTakePets();

	public abstract boolean isRecallPets();

	public abstract boolean isUsableItem();

	public abstract boolean isUsableSkill();
	
	public abstract boolean isDungeon();
	
	public abstract int getDmgModiPc2Npc();
	
	public abstract int getDmgModiNpc2Pc();
	
	public abstract boolean isDecreaseHp();
	
	public abstract boolean isDominationTeleport();
	
	public abstract boolean isBeginZone();
	
	public abstract boolean isRedKnightZone();
	
	public abstract boolean isRuunCastleZone();
	
	public abstract boolean isInterWarZone();
	
	public abstract boolean isGeradBuffZone();
	
	public abstract boolean isGrowBuffZone();
	
	public abstract L1InterServer getInter();
	
	public abstract String getScript();
	
	public abstract MapBalanceData getBalance();

	public abstract boolean isFishingZone(int x, int y);
	
	public abstract boolean isCloseZone(int x, int y);
	
	public abstract L1V1Map copyMap(int a);

	public static L1Map newNull() {
		return _nullMap;
	}

	public abstract String toString(Point pt);

	public boolean isNull() {
		return false;
	}
	
	public static boolean isTeleportable(int x, int y, int mapId) {
		if ( mapId == 4 && x >= 33469 && x <= 33528 && y >= 32839 && y <= 32869 ) {
			if ((x >= 33479 && x <= 32528 && y >= 32849 && y <= 32859) || (x == 33478 && y >= 32850 && y <= 32859) || (x == 33477 && y >= 32851 && y <= 32859)
					|| (x == 33476 && y >= 32852 && y <= 32858) || (x == 33475 && y >= 32853 && y <= 32857)) {
				return true;
			}
			return false;
		}

		return true;
	}
}

class L1NullMap extends L1Map {
	public L1NullMap() {
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getTile(int x, int y) {
		return 0;
	}

	@Override
	public int getOriginalTile(int x, int y) {
		return 0;
	}

	@Override
	public boolean isInMap(int x, int y) {
		return false;
	}

	@Override
	public boolean isInMap(Point pt) {
		return false;
	}

	@Override
	public boolean isPassable(int x, int y) {
		return false;
	}

	@Override
	public boolean isUserPassable(Point pt) {
		return false;
	}
	
	@Override
	public boolean isUserPassable(int x, int y, int heading) {
		return false;
	}

	@Override
	public boolean isAttackable(int x, int y, int heading, boolean pss) {
		return false;
	}

	@Override
	public boolean isAttackableDoor(int x, int y, int heading) {
		return false;
	}

	@Override
	public boolean isPassable(Point pt) {
		return false;
	}

	@Override
	public boolean isPassable(int x, int y, int heading) {
		return false;
	}

	@Override
	public boolean isPassable(Point pt, int heading) {
		return false;
	}

	@Override
	public void setPassable(int x, int y, boolean isPassable) {
	}

	@Override
	public void setPassable(Point pt, boolean isPassable) {
	}

	public void setDoorPassable(Point pt, boolean directionX, boolean isPassable) {
	}

	public void setDoorPassable(int x, int y, boolean directionX, boolean isPassable) {
	}

	@Override
	public boolean isSafetyZone(int x, int y) {
		return false;
	}

	@Override
	public boolean isSafetyZone(Point pt) {
		return false;
	}

	@Override
	public boolean isCombatZone(int x, int y) {
		return false;
	}

	@Override
	public boolean isCombatZone(Point pt) {
		return false;
	}

	@Override
	public boolean isNormalZone(int x, int y) {
		return false;
	}

	@Override
	public boolean isNormalZone(Point pt) {
		return false;
	}

	@Override
	public boolean isUnderwater() {
		return false;
	}

	@Override
	public boolean isMarkable() {
		return false;
	}

	@Override
	public boolean isTeleportable() {
		return false;
	}

	@Override
	public boolean isEscapable() {
		return false;
	}

	@Override
	public boolean isUseResurrection() {
		return false;
	}

	@Override
	public boolean isUsePainwand() {
		return false;
	}

	@Override
	public boolean isEnabledDeathPenalty() {
		return false;
	}

	@Override
	public boolean isTakePets() {
		return false;
	}

	@Override
	public boolean isRecallPets() {
		return false;
	}

	@Override
	public boolean isUsableItem() {
		return false;
	}

	@Override
	public boolean isUsableSkill() {
		return false;
	}
	
	@Override
	public boolean isDungeon() {
		return false;
	}
	
	@Override
	public int getDmgModiPc2Npc() {
		return 0;
	}
	
	@Override
	public int getDmgModiNpc2Pc() {
		return 0;
	}
	
	@Override
	public boolean isDecreaseHp() {
		return false;
	}
	
	@Override
	public boolean isDominationTeleport() {
		return false;
	}
	
	@Override
	public boolean isBeginZone() {
		return false;
	}
	
	@Override
	public boolean isRedKnightZone() {
		return false;
	}
	
	@Override
	public boolean isRuunCastleZone() {
		return false;
	}
	
	@Override
	public boolean isInterWarZone() {
		return false;
	}
	
	@Override
	public boolean isGeradBuffZone() {
		return false;
	}
	
	@Override
	public boolean isGrowBuffZone() {
		return false;
	}
	
	@Override
	public L1InterServer getInter() {
		return null;
	}
	
	@Override
	public String getScript() {
		return null;
	}
	
	@Override
	public MapBalanceData getBalance() {
		return null;
	}

	@Override
	public boolean isFishingZone(int x, int y) {
		return false;
	}
	
	@Override
	public boolean isCloseZone(int x, int y) {
		return false;
	}

	@Override
	public String toString(Point pt) {
		return "null";
	}

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public L1V1Map copyMap(int id){
		return null;
	}
	
	@Override
	public int getBaseMapId() {
		return 0;
	}
}
