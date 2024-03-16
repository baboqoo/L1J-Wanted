package l1j.server.server.model.map;

import l1j.server.server.construct.L1InterServer;
import l1j.server.server.datatables.MapBalanceTable;
import l1j.server.server.datatables.MapFixKeyTable;
import l1j.server.server.datatables.MapBalanceTable.MapBalanceData;
import l1j.server.server.types.Point;
import l1j.server.server.utils.StringUtil;

public class L1V1Map extends L1Map {
	private int _mapId;
    private int _baseMapId;
	private int _worldTopLeftX;
	private int _worldTopLeftY;
	private int _worldBottomRightX;
	private int _worldBottomRightY;
	private byte _map[][];
	private byte _objectMap[][];
	public byte _doorMap[][];
	private boolean _isUnderwater;
	private boolean _isMarkable;
	private boolean _isTeleportable;
	private boolean _isEscapable;
	private boolean _isUseResurrection;
	private boolean _isUsePainwand;
	private boolean _isEnabledDeathPenalty;
	private boolean _isTakePets;
	private boolean _isRecallPets;
	private boolean _isUsableItem;
	private boolean _isUsableSkill;
	private boolean _isDungeon;
	private int _dmgModiPc2Npc;
	private int _dmgModiNpc2Pc;
	private boolean _isDecreaseHp;
	private boolean _isDominationTeleport;
	private boolean _isBeginZone;
	private boolean _isRedKnightZone;
	private boolean _isRuunCastleZone;
	private boolean _isInterWarZone;
	private boolean _isGeradBuffZone;
	private boolean _isGrowBuffZone;
	private L1InterServer _inter;
	private String _script;
	private MapBalanceData _balance;

	private static final byte BITFLAG_IS_IMPASSABLE_X = (byte) 0x80; 
	private static final byte BITFLAG_IS_IMPASSABLE_Y = (byte) 0x40; 

	protected L1V1Map() {
	}

	public L1V1Map(int mapId, byte map[][], int worldTopLeftX,
			int worldTopLeftY, boolean underwater, boolean markable,
			boolean teleportable, boolean escapable, boolean useResurrection,
			boolean usePainwand, boolean enabledDeathPenalty, boolean takePets,
			boolean recallPets, boolean usableItem, boolean usableSkill, boolean dungeon, 
			int dmgModiPc2Npc, int dmgModiNpc2Pc,
			boolean decreaseHp, boolean dominationTeleport, boolean beginZone, boolean redKnightZone, boolean ruunCastleZone,
			boolean interWarZone, boolean geradBuffZone, boolean growBuffZone, L1InterServer inter, String script) {
		_mapId		= mapId;
		_baseMapId	= mapId;
		_map		= map;
		_objectMap = new byte[map.length][map[0].length];
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[0].length; ++j) {
				_objectMap[i][j] = 0;
			}
		}
		_doorMap = new byte[map.length][map[0].length];
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[0].length; ++j) {
				_doorMap[i][j] = 0;
			}
		}

		_worldTopLeftX = worldTopLeftX;
		_worldTopLeftY = worldTopLeftY;

		_worldBottomRightX = worldTopLeftX + map.length - 1;
		_worldBottomRightY = worldTopLeftY + map[0].length - 1;

		_isUnderwater			= underwater;
		_isMarkable				= markable;
		_isTeleportable			= teleportable;
		_isEscapable			= escapable;
		_isUseResurrection		= useResurrection;
		_isUsePainwand			= usePainwand;
		_isEnabledDeathPenalty	= enabledDeathPenalty;
		_isTakePets				= takePets;
		_isRecallPets			= recallPets;
		_isUsableItem			= usableItem;
		_isUsableSkill			= usableSkill;
		_isDungeon				= dungeon;
		_dmgModiPc2Npc			= dmgModiPc2Npc;
		_dmgModiNpc2Pc			= dmgModiNpc2Pc;
		_isDecreaseHp			= decreaseHp;
		_isDominationTeleport	= dominationTeleport;
		_isBeginZone			= beginZone;
		_isRedKnightZone		= redKnightZone;
		_isRuunCastleZone		= ruunCastleZone;
		_isInterWarZone			= interWarZone;
		_isGeradBuffZone		= geradBuffZone;
		_isGrowBuffZone			= growBuffZone;
		_inter					= inter;
		_script					= script;
		_balance				= MapBalanceTable.getBalance((short)_mapId);
	}
	
	public L1V1Map(L1V1Map map) {
		_mapId		= map._mapId;
        _baseMapId	= map._mapId;
		_map		= new byte[map._map.length][];
		for (int i = 0; i < map._map.length; i++) {
			_map[i] = map._map[i].clone();
		}
		_objectMap = new byte[_map.length][_map[0].length];
		for (int i = 0; i < _map.length; ++i) {
			for (int j = 0; j < _map[0].length; ++j) {
				_objectMap[i][j] = 0;
			}
		}
		_doorMap = new byte[_map.length][_map[0].length];
		for (int i = 0; i < _map.length; ++i) {
			for (int j = 0; j < _map[0].length; ++j) {
				_doorMap[i][j] = 0;
			}
		}

		_worldTopLeftX			= map._worldTopLeftX;
		_worldTopLeftY			= map._worldTopLeftY;
		_worldBottomRightX		= map._worldBottomRightX;
		_worldBottomRightY		= map._worldBottomRightY;
		
		_isUnderwater			= map._isUnderwater;
		_isMarkable				= map._isMarkable;
		_isTeleportable			= map._isTeleportable;
		_isEscapable			= map._isEscapable;
		_isUseResurrection		= map._isUseResurrection;
		_isUsePainwand			= map._isUsePainwand;
		_isEnabledDeathPenalty	= map._isEnabledDeathPenalty;
		_isTakePets				= map._isTakePets;
		_isRecallPets			= map._isRecallPets;
		_isUsableItem			= map._isUsableItem;
		_isUsableSkill			= map._isUsableSkill;
		_isDungeon				= map._isDungeon;
		_dmgModiPc2Npc			= map._dmgModiPc2Npc;
		_dmgModiNpc2Pc			= map._dmgModiNpc2Pc;
		_isDecreaseHp			= map._isDecreaseHp;
		_isDominationTeleport	= map._isDominationTeleport;
		_isBeginZone			= map._isBeginZone;
		_isRedKnightZone		= map._isRedKnightZone;
		_isRuunCastleZone		= map._isRuunCastleZone;
		_isInterWarZone			= map._isInterWarZone;
		_inter					= map._inter;
		_script					= map._script;
		_balance				= map._balance;
	}
	
	public void reset(L1V1Map map){
		if (_map.length != map._map.length) {
			return;
		}
		
		for (int i = 0; i < map._map.length; i++) {
			_map[i] = map._map[i].clone();
		}
		for (int i = 0; i < _map.length; ++i) {
			for (int j = 0; j < _map[0].length; ++j) {
				_objectMap[i][j] = 0;
			}
		}
		for (int i = 0; i < _map.length; ++i) {
			for (int j = 0; j < _map[0].length; ++j) {
				_doorMap[i][j] = 0;
			}
		}
	}
	
	public L1V1Map clone(int id){
		L1V1Map map = new L1V1Map(this);
		map._mapId = id;
		return map;
	}

	public int accessTile(int x, int y) {
		if (!isInMap(x, y)) {
			return 0;
		}
		return _map[x - _worldTopLeftX][y - _worldTopLeftY];
	}
	
	private int accessObjectTile(int x, int y) {
		if (!isInMap(x, y)) {
			return 0;
		}
		return _objectMap[x - _worldTopLeftX][y - _worldTopLeftY];
	}

	private int accessDoorTile(int x, int y) {
		if (!isInMap(x, y)) {
			return 0;
		}
		return _doorMap[x - _worldTopLeftX][y - _worldTopLeftY];
	}


	private boolean isObjectExist(int x, int y) {
		return accessObjectTile(x, y) > 0;
	}

	private int accessOriginalTile(int x, int y) {
		return accessTile(x, y);
	}

	@SuppressWarnings("unused")
	private void setTile(int x, int y, int tile) {
		if (!isInMap(x, y)) {
			return;
		}
		_map[x - _worldTopLeftX][y - _worldTopLeftY] = (byte) tile;
	}

	public byte[][] getRawTiles() {
		return _map;
	}

	@Override
	public int getId() {
		return _mapId;
	}

	@Override
	public int getX() {
		return _worldTopLeftX;
	}

	@Override
	public int getY() {
		return _worldTopLeftY;
	}

	@Override
	public int getWidth() {
		return _worldBottomRightX - _worldTopLeftX + 1;
	}

	@Override
	public int getHeight() {
		return _worldBottomRightY - _worldTopLeftY + 1;
	}

	@Override
	public int getTile(int x, int y) {
//		short tile = _map[x - _worldTopLeftX][y - _worldTopLeftY];
//		if (0 != (tile & BITFLAG_IS_IMPASSABLE)) {
//			return 300;
//		}
		return accessOriginalTile(x, y);
	}

	@Override
	public int getOriginalTile(int x, int y) {
		return accessOriginalTile(x, y);
	}

	@Override
	public boolean isInMap(Point pt) {
		return isInMap(pt.getX(), pt.getY());
	}

	@Override
	public boolean isInMap(int x, int y) {
		if (_mapId == 4 && (x < 32520 || y < 32070 || (y < 32190 && x < 33950)))
			return false;
		return (_worldTopLeftX <= x && x <= _worldBottomRightX
				&& _worldTopLeftY <= y && y <= _worldBottomRightY);
	}

	@Override
	public boolean isPassable(Point pt) {
		return isPassable(pt.getX(), pt.getY());
	}

	@Override
	public boolean isUserPassable(Point pt) {
		return isUserPassable(pt.getX(), pt.getY() - 1, 4) || isUserPassable(pt.getX() + 1, pt.getY(), 6)
				|| isUserPassable(pt.getX(), pt.getY() + 1, 0) || isUserPassable(pt.getX() - 1, pt.getY(), 2);
	}
	
	@Override
	public boolean isPassable(int x, int y) {
		return isPassable(x, y - 1, 4) || isPassable(x + 1, y, 6)
				|| isPassable(x, y + 1, 0) || isPassable(x - 1, y, 2);
	}

	@Override
	public boolean isPassable(Point pt, int heading) {
		return isPassable(pt.getX(), pt.getY(), heading);
	}
	
	private boolean checkMoveTile(int x, int y, int heading, int newX, int newY){
		int tile1 = accessTile(x, y);
		int tile2 = accessTile(newX, newY);

		switch(heading){
		case 0:{ return (tile1 & 0x02) == 0x02; }
		case 1:{
			int tile3 = accessTile(x, y - 1);
			int tile4 = accessTile(x + 1, y);
			return (tile1 & 0x02) == 0x02 && (tile3 & 0x01) == 0x01 || (tile1 & 0x01) == 0x01 && (tile4 & 0x02) == 0x02; }
		case 2:{ return (tile1 & 0x01) == 0x01; }
		case 3:{
			int tile3 = accessTile(x, y + 1);
			return (tile3 & 0x03) == 0x03 || (tile1 & 0x01) == 0x01 && (tile2 & 0x02) == 0x02;
			}
		case 4:{ return (tile2 & 0x02) == 0x02; }
		case 5:{
			int tile3 = accessTile(x, y + 1);
			int tile4 = accessTile(x - 1, y);
			return (tile2 & 0x01) == 0x01 && ( tile3 & 0x02 ) == 0x02 || (tile2 & 0x02) == 0x02 && (tile4 & 0x01) == 0x01; }
		case 6:{ return (tile2 & 0x01) == 0x01; }
		case 7:{
			int tile3 = accessTile(x - 1, y);
			return (tile3 & 0x03) == 0x03 || (tile1 & 0x02) == 0x02 && (tile2 & 0x01) == 0x01;
			}
		default:break;
		}
		
		return false;		
	}

	@Override
	public boolean isUserPassable(int x, int y, int heading){
		int newX;
		int newY;

		switch(heading){
		case 0: newX = x; newY = y - 1; break;
		case 1: newX = x + 1; newY = y - 1; break;
		case 2: newX = x + 1; newY = y; break;
		case 3: newX = x + 1; newY = y + 1; break;
		case 4: newX = x; newY = y + 1; break;
		case 5: newX = x - 1; newY = y + 1; break;
		case 6: newX = x - 1; newY = y; break;
		case 7: newX = x - 1; newY = y - 1; break;
		default: return false;
		}

		if (!checkDoor(x, y, newX, newY)) {
			return false;
		}
		return checkMoveTile(x, y, heading, newX, newY);
	}
	
	private boolean checkDoor(int x, int y, int newX, int newY){
		if (x > newX){
			int doorTile1 = accessDoorTile(newX, y);
			int doorTile2 = accessDoorTile(newX, newY);
			
			if (((doorTile1 & BITFLAG_IS_IMPASSABLE_X) != 0) || ((doorTile2 & BITFLAG_IS_IMPASSABLE_X) != 0)) {
				return false;
			}
		} else if (x < newX){
			int doorTileOld = accessDoorTile(x, y);
			int doorTileNew = accessDoorTile(newX - 1, newY);
			
			if (((doorTileOld & BITFLAG_IS_IMPASSABLE_X) != 0) || ((doorTileNew & BITFLAG_IS_IMPASSABLE_X) != 0)) {
				return false;
			}
		}
		
		if (y < newY){
			int doorTile1 = accessDoorTile(x, newY);
			int doorTile2 = accessDoorTile(newX, newY);
			
			if (((doorTile1 & BITFLAG_IS_IMPASSABLE_Y) != 0) || ((doorTile2 & BITFLAG_IS_IMPASSABLE_Y) != 0)) {
				return false;
			}
		} else if (y > newY){
			int doorTileOld = accessDoorTile(x, y);
			int doorTileNew = accessDoorTile(newX, newY + 1);
			
			if (((doorTileOld & BITFLAG_IS_IMPASSABLE_Y) != 0) || ((doorTileNew & BITFLAG_IS_IMPASSABLE_Y) != 0)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkAttackTile(int x, int y, int heading, int newX, int newY, boolean pss){
		/*int tile1 = accessTile(x, y);// 현재 위치의 타일
		int tile2 = accessTile(newX, newY);// 이동 예정의 타일
		switch(heading){
		case 0:{
			return (tile1 & 0x08) > 0;
		}
		case 1:{
			int tile3 = accessTile(x, y - 1);// 0시 방향의 타일
			int tile4 = accessTile(x + 1, y);// 2시 방향의 타일
			return ((tile1 & 0x08) > 0 && (tile3 & 0x04) > 0) || ((tile1 & 0x04) > 0 && (tile4 & 0x08) > 0);
		}
		case 2:{
			return (tile1 & 0x04) > 0;
		}
		case 3:{
			int tile4 = accessTile(x, y + 1);// 4시 방향의 타일
			return ((tile1 & 0x04) > 0 && (tile2 & 0x08) > 0) || ((tile4 & 0x08) > 0 && (tile4 & 0x04) > 0);
		}
		case 4:{
			return (tile2 & 0x08) > 0;
		}
		case 5:{
			int tile3 = accessTile(x, y + 1);// 4시 방향의 타일
			int tile4 = accessTile(x - 1, y);// 6시 방향의 타일
			return ((tile3 & 0x08) > 0 && (tile2 & 0x04) > 0) || ((tile4 & 0x04) > 0 && (tile2 & 0x08) > 0);
		}
		case 6:{
			return (tile2 & 0x04) > 0;
		}
		case 7:{
			int tile3 = accessTile(x - 1, y);// 6시 방향의 타일
			return ((tile1 & 0x08) > 0 && (tile2 & 0x04) > 0) || ((tile3 & 0x04) > 0 && (tile3 & 0x08) > 0);
		}
		default:
			return false;
		}*/
		
		int tile1 = accessTile(x, y);// 현재 위치의 타일
		int tile2 = accessTile(newX, newY);// 이동 예정의 타일
		switch(heading){
		case 0:{
			return (tile1 & 0x08) > 0;
		}
		case 1:{
			int tile3 = accessTile(x, y - 1);// 0시 방향의 타일
			int tile4 = accessTile(x + 1, y);// 2시 방향의 타일
			return (pss && ((tile3 & 0x0A) > 0 || (tile4 & 0x0A) > 0)) || ((tile1 & 0x08) > 0 && (tile3 & 0x04) > 0) || ((tile1 & 0x04) > 0 && (tile4 & 0x08) > 0);
		}
		case 2:{
			return (tile1 & 0x04) > 0;
		}
		case 3:{
			int tile3 = accessTile(x + 1, y);// 2시 방향의 타일
			int tile4 = accessTile(x, y + 1);// 4시 방향의 타일
			return (pss && ((tile3 & 0x0A) > 0 || (tile4 & 0x0A) > 0)) || ((tile1 & 0x04) > 0 && (tile2 & 0x08) > 0) || ((tile4 & 0x08) > 0 && (tile4 & 0x04) > 0);
		}
		case 4:{
			return (tile2 & 0x08) > 0;
		}
		case 5:{
			int tile3 = accessTile(x, y + 1);// 4시 방향의 타일
			int tile4 = accessTile(x - 1, y);// 6시 방향의 타일
			return (pss && ((tile3 & 0x0A) > 0 || (tile4 & 0x0A) > 0)) || ((tile3 & 0x08) > 0 && (tile2 & 0x04) > 0) || ((tile4 & 0x04) > 0 && (tile2 & 0x08) > 0);
		}
		case 6:{
			return (tile2 & 0x04) > 0;
		}
		case 7:{
			int tile3 = accessTile(x - 1, y);// 6시 방향의 타일
			int tile4 = accessTile(x, y - 1);// 0시 방향의 타일
			return (pss && ((tile3 & 0x0A) > 0 || (tile4 & 0x0A) > 0)) || ((tile1 & 0x08) > 0 && (tile2 & 0x04) > 0) || ((tile3 & 0x04) > 0 && (tile3 & 0x08) > 0);
		}
		default:
			return false;
		}
	}

	@Override
	public boolean isAttackable(int x, int y, int heading, boolean pss){
		int newX, newY;
		switch(heading){
		case 0:	newX = x;		newY = y - 1; break;
		case 1:	newX = x + 1;	newY = y - 1; break;
		case 2:	newX = x + 1;	newY = y; break;
		case 3:	newX = x + 1;	newY = y + 1; break;
		case 4:	newX = x;		newY = y + 1; break;
		case 5:	newX = x - 1;	newY = y + 1; break;
		case 6:	newX = x - 1;	newY = y; break;
		case 7:	newX = x - 1;	newY = y - 1; break;
		default:return false;
		}
		if (!checkDoor(x, y, newX, newY)) {
			return false;
		}
		return checkAttackTile(x, y, heading, newX, newY, pss);
	}
	
	@Override
	public boolean isAttackableDoor(int x, int y, int heading){
		int newX, newY;
		switch(heading){
		case 0:	newX = x;		newY = y - 1; break;
		case 1:	newX = x + 1;	newY = y - 1; break;
		case 2:	newX = x + 1;	newY = y; break;
		case 3:	newX = x + 1;	newY = y + 1; break;
		case 4:	newX = x;		newY = y + 1; break;
		case 5:	newX = x - 1;	newY = y + 1; break;
		case 6:	newX = x - 1;	newY = y; break;
		case 7:	newX = x - 1;	newY = y - 1; break;
		default:return false;
		}
		return checkAttackTile(x, y, heading, newX, newY, false);
	}

	@Override
	public boolean isPassable(int x, int y, int heading) {
		int newX, newY;

		switch(heading){
		case 0:	newX = x;		newY = y - 1; break;
		case 1:	newX = x + 1;	newY = y - 1; break;
		case 2:	newX = x + 1;	newY = y; break;
		case 3:	newX = x + 1;	newY = y + 1; break;
		case 4:	newX = x;		newY = y + 1; break;
		case 5:	newX = x - 1;	newY = y + 1; break;
		case 6:	newX = x - 1;	newY = y; break;
		case 7:	newX = x - 1;	newY = y - 1; break;
		default: return false;
		}

		if (!checkDoor(x, y, newX, newY)) {
			return false;
		}
		if (isObjectExist(newX, newY)) {
			return false;
		}
		return checkMoveTile(x, y, heading, newX, newY);
	}

	@Override
	public void setPassable(Point pt, boolean isPassable) {
		setPassable(pt.getX(), pt.getY(), isPassable);
	}

	@Override
	public void setPassable(int x, int y, boolean isPassable) {
		if (!isInMap(x, y)) {
			return;
		}
		synchronized(this) {
			if (isPassable) {
				_objectMap[x - _worldTopLeftX][y - _worldTopLeftY] = (byte) 0;
			} else {
				_objectMap[x - _worldTopLeftX][y - _worldTopLeftY] = (byte) 1;
			}
		}
	}
	/*@Override
	public void setPassable(int x, int y, boolean isPassable) {
		if (isPassable) {
			setTile(x, y, (short) (accessTile(x, y) & (~BITFLAG_IS_IMPASSABLE)));
		} else {
			setTile(x, y, (short) (accessTile(x, y) | BITFLAG_IS_IMPASSABLE));
		}
	}*/

	@Override
	public void setDoorPassable(Point pt, boolean directionX, boolean isPassable) {
		setDoorPassable(pt.getX(), pt.getY(), directionX, isPassable);
	}

	@Override
	public void setDoorPassable(int x, int y, boolean directionX, boolean isPassable) {
		if (!isInMap(x, y))
			return;
		if (isPassable) {
			synchronized(this) {
				_doorMap[x - _worldTopLeftX][y - _worldTopLeftY] = 0;
			}
		} else {
			byte setBit = directionX ? BITFLAG_IS_IMPASSABLE_X : BITFLAG_IS_IMPASSABLE_Y;
			synchronized(this) {
				_doorMap[x - _worldTopLeftX][y - _worldTopLeftY] = setBit;
			}
		}
	}

	@Override
	public boolean isSafetyZone(Point pt) {
		return isSafetyZone(pt.getX(), pt.getY());
	}

	@Override
	public boolean isSafetyZone(int x, int y) {
		int tile = accessOriginalTile(x, y);
		return (tile & 0x30) == 0x10;
	}

	@Override
	public boolean isCombatZone(Point pt) {
		return isCombatZone(pt.getX(), pt.getY());
	}

	@Override
	public boolean isCombatZone(int x, int y) {
		int tile = accessOriginalTile(x, y);
		return (tile & 0x30) == 0x20;
	}

	@Override
	public boolean isNormalZone(Point pt) {
		return isNormalZone(pt.getX(), pt.getY());
	}

	@Override
	public boolean isNormalZone(int x, int y) {
		int tile = accessOriginalTile(x, y);
		return (tile & 0x30) == 0x00;
	}

	@Override
	public boolean isUnderwater() {
		return _isUnderwater;
	}

	@Override
	public boolean isMarkable() {
		return _isMarkable;
	}

	@Override
	public boolean isTeleportable() {
		return _isTeleportable;
	}

	@Override
	public boolean isEscapable() {
		return _isEscapable;
	}

	@Override
	public boolean isUseResurrection() {
		return _isUseResurrection;
	}

	@Override
	public boolean isUsePainwand() {
		return _isUsePainwand;
	}

	@Override
	public boolean isEnabledDeathPenalty() {
		return _isEnabledDeathPenalty;
	}

	@Override
	public boolean isTakePets() {
		return _isTakePets;
	}

	@Override
	public boolean isRecallPets() {
		return _isRecallPets;
	}

	@Override
	public boolean isUsableItem() {
		return _isUsableItem;
	}

	@Override
	public boolean isUsableSkill() {
		return _isUsableSkill;
	}
	
	@Override
	public boolean isDungeon() {
		return _isDungeon;
	}
	
	@Override
	public int getDmgModiPc2Npc() {
		return _dmgModiPc2Npc;
	}
	
	@Override
	public int getDmgModiNpc2Pc() {
		return _dmgModiNpc2Pc;
	}
	
	@Override
	public boolean isDecreaseHp() {
		return _isDecreaseHp;
	}
	
	@Override
	public boolean isDominationTeleport() {
		return _isDominationTeleport;
	}
	
	@Override
	public boolean isBeginZone() {
		return _isBeginZone;
	}
	
	@Override
	public boolean isRedKnightZone() {
		return _isRedKnightZone;
	}
	
	@Override
	public boolean isRuunCastleZone() {
		return _isRuunCastleZone;
	}
	
	@Override
	public boolean isInterWarZone() {
		return _isInterWarZone;
	}
	
	@Override
	public boolean isGeradBuffZone() {
		return _isGeradBuffZone;
	}
	
	@Override
	public boolean isGrowBuffZone() {
		return _isGrowBuffZone;
	}
	
	@Override
	public L1InterServer getInter() {
		return _inter;
	}
	
	@Override
	public String getScript() {
		return _script;
	}

	@Override
	public boolean isFishingZone(int x, int y) {
		return accessOriginalTile(x, y) == 28;
	}
	
	@Override
	public MapBalanceData getBalance() {
		return _balance;
	}
	
	@Override
	public boolean isCloseZone(int locX, int locY) {
		int tile = accessOriginalTile(locX, locY);
		// 벽인식이라고 체크 될때
		if (tile == 12 || (tile & 0x30) == tile) {
			String key = new StringBuilder().append(_mapId).append(locX).append(locY).toString();
			return !MapFixKeyTable.getInstance().isLockey(key); // key에 없을때 true;
		}
		return false;
	}

	@Override
	public String toString(Point pt) {
		return StringUtil.EmptyString + getOriginalTile(pt.getX(), pt.getY());
	}
	
	@Override
	public L1V1Map copyMap(int newMapId){
		return clone(newMapId);
	}
	
	@Override
	public int getBaseMapId() {
		return _baseMapId;
	}
}

