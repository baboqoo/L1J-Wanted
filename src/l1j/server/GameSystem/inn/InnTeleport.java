package l1j.server.GameSystem.inn;

import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;

public class InnTeleport {
	private static int[] talkInCheckLoc			= InnLoc.TALKING_ISLAND_INN_CHECK.getLoc();
	private static int[] adenInCheckLoc			= InnLoc.ADEN_INN_CHECK.getLoc();
	private static int[] giranInCheckLoc		= InnLoc.GIRAN_INN_CHECK.getLoc();
	private static int[] orenInCheckLoc			= InnLoc.OREN_INN_CHECK.getLoc();
	private static int[] gludioInCheckLoc		= InnLoc.GLUDIO_INN_CHECK.getLoc();
	private static int[] heineInCheckLoc		= InnLoc.HEINE_INN_CHECK.getLoc();
	private static int[] silverKnightInCheckLoc	= InnLoc.SILVER_KNIGHT_INN_CHECK.getLoc();
	private static int[] windawoodInCheckLoc	= InnLoc.WINDAWOOD_INN_CHECK.getLoc();
	private static int[] ruunInCheckLoc			= InnLoc.RUUN_INN_CHECK.getLoc();
	
	public static boolean checkInn(L1PcInstance pc, int locx, int locy){
		/** 여관 -> 본토 **/
		if ((locx >= 32744 && locx <= 32746) && locy == 32808 && InnHelper.TALKING_ISLAND.isHallMap(pc.getMapId())) {// 말하는섬 홀
			int[] outLoc = InnLoc.TALKING_ISLAND_OUT.getLoc();
			return actionMove(pc, outLoc[0], outLoc[1], (byte)4, (short)outLoc[2]);
		}
		if (locx == 32729 && locy == 32790 && InnHelper.RUUN.isHallMap(pc.getMapId())) {// 루운성 홀
			int[] outLoc = InnLoc.RUUN_OUT.getLoc();
			return actionMove(pc, outLoc[0], outLoc[1], (byte)4, (short)outLoc[2]);
		}
		if ((locx >= 32745 && locx <= 32746) && locy == 32807) {
			int[] outLoc = InnHelper.getOutLoc(pc.getMapId());
			if (outLoc != null) {
				return actionMove(pc, outLoc[0], outLoc[1], (byte)4, (short)outLoc[2]);
			}
		}
		if (locx == 32744 && locy == 32803 && InnHelper.GLUDIO.isRoomMap(pc.getMapId())) {// 글루딘 방
			int[] outLoc = InnLoc.GLUDIO_OUT.getLoc();
			return actionMove(pc, outLoc[0], outLoc[1], (byte)4, (short)outLoc[2]);
		}
		if (locx == 32736 && locy == 32794 && InnHelper.RUUN.isRoomMap(pc.getMapId())) {// 루운성 방
			int[] outLoc = InnLoc.RUUN_OUT.getLoc();
			return actionMove(pc, outLoc[0], outLoc[1], (byte)4, (short)outLoc[2]);
		}
		if ((locx >= 32745 && locx <= 32746) && locy == 32803) {
			if (InnHelper.TALKING_ISLAND.isRoomMap(pc.getMapId())) {// 말하는 섬 방
				if (locx != 32745) {
					int[] outLoc = InnHelper.getOutLoc(pc.getMapId());
					if (outLoc != null) {
						return actionMove(pc, outLoc[0], outLoc[1], (byte)4, (short)outLoc[2]);
					}
				}
			} else {
				int[] outLoc = InnHelper.getOutLoc(pc.getMapId());
				if (outLoc != null) {
					return actionMove(pc, outLoc[0], outLoc[1], (byte)4, (short)outLoc[2]);
				}
			}
		}
		
	    /** 본토 -> 여관 **/
	    if ((locx == talkInCheckLoc[0] && locy == talkInCheckLoc[1] && pc.getMapId() == talkInCheckLoc[2])// 말하는 섬 여관
			    || (locx == adenInCheckLoc[0] && locy == adenInCheckLoc[1] && pc.getMapId() == adenInCheckLoc[2])// 아덴 여관
			    || (locx == giranInCheckLoc[0] && locy == giranInCheckLoc[1] && pc.getMapId() == giranInCheckLoc[2])// 기란 여관
			    || (locx == orenInCheckLoc[0] && locy == orenInCheckLoc[1] && pc.getMapId() == orenInCheckLoc[2])// 오렌 여관
			    || (locx == gludioInCheckLoc[0] && locy == gludioInCheckLoc[1] && pc.getMapId() == gludioInCheckLoc[2])// 글루딘 여관
			    || (locx == heineInCheckLoc[0] && locy == heineInCheckLoc[1] && pc.getMapId() == heineInCheckLoc[2])// 하이네 여관
			    || (locx == silverKnightInCheckLoc[0] && locy == silverKnightInCheckLoc[1] && pc.getMapId() == silverKnightInCheckLoc[2])// 은기사 여관
			    || (locx == windawoodInCheckLoc[0] && locy == windawoodInCheckLoc[1] && pc.getMapId() == windawoodInCheckLoc[2])// 우드백 여관
			    || (locx == ruunInCheckLoc[0] && locy == ruunInCheckLoc[1] && pc.getMapId() == ruunInCheckLoc[2])) {// 루운성 여관
		    return isInn(pc, locx, locy);
		}
		return false;
	}
	
	static boolean isInn(L1PcInstance pc, int locx, int locy) {
		L1ItemInstance[] key = null;
	    L1PcInventory inv = pc.getInventory();
	    if (inv.checkItem(L1ItemId.INN_ROOM_KEY)) {
	    	key = inv.findItemsId(L1ItemId.INN_ROOM_KEY);
	    } else if (inv.checkItem(L1ItemId.INN_HALL_KEY)) {
	    	key = inv.findItemsId(L1ItemId.INN_HALL_KEY);
	    }
	    if (key == null || key.length <= 0) {
	    	return false;
	    }
	    
    	short keyMap = 0;
    	long currentTime = System.currentTimeMillis();
    	L1ItemInstance innKey = null;
    	int innKeyMap = 0;
	    for (int i = 0; i < key.length; i++) {
	    	innKey		= key[i];
	    	innKeyMap	= innKey.getKey();
	    	if (innKeyMap <= 0 || innKey.getEndTime().getTime() <= currentTime) {
	    		continue;
	    	}
		    if (((locx == talkInCheckLoc[0] && locy == talkInCheckLoc[1] && pc.getMapId() == talkInCheckLoc[2]) && (InnHelper.TALKING_ISLAND.isRoomMap(innKeyMap) || InnHelper.TALKING_ISLAND.isHallMap(innKeyMap)))// 말하는 섬 여관
				|| ((locx == adenInCheckLoc[0] && locy == adenInCheckLoc[1] && pc.getMapId() == adenInCheckLoc[2]) && (InnHelper.ADEN.isRoomMap(innKeyMap) || InnHelper.ADEN.isHallMap(innKeyMap)))// 아덴 여관
				|| ((locx == giranInCheckLoc[0] && locy == giranInCheckLoc[1] && pc.getMapId() == giranInCheckLoc[2]) && (InnHelper.GIRAN.isRoomMap(innKeyMap) || InnHelper.GIRAN.isHallMap(innKeyMap)))// 기란 여관
				|| ((locx == orenInCheckLoc[0] && locy == orenInCheckLoc[1] && pc.getMapId() == orenInCheckLoc[2]) && (InnHelper.OREN.isRoomMap(innKeyMap) || InnHelper.OREN.isHallMap(innKeyMap)))// 오렌 여관
				|| ((locx == gludioInCheckLoc[0] && locy == gludioInCheckLoc[1] && pc.getMapId() == gludioInCheckLoc[2]) && (InnHelper.GLUDIO.isRoomMap(innKeyMap) || InnHelper.GLUDIO.isHallMap(innKeyMap)))// 글루딘 여관
				|| ((locx == heineInCheckLoc[0] && locy == heineInCheckLoc[1] && pc.getMapId() == heineInCheckLoc[2]) && (InnHelper.HEINE.isRoomMap(innKeyMap) || InnHelper.HEINE.isHallMap(innKeyMap)))// 하이네 여관
				|| ((locx == silverKnightInCheckLoc[0] && locy == silverKnightInCheckLoc[1] && pc.getMapId() == silverKnightInCheckLoc[2]) && (InnHelper.SILVER_KNIGHT.isRoomMap(innKeyMap) || InnHelper.SILVER_KNIGHT.isHallMap(innKeyMap)))// 은기사 여관
				|| ((locx == windawoodInCheckLoc[0] && locy == windawoodInCheckLoc[1] && pc.getMapId() == windawoodInCheckLoc[2]) && (InnHelper.WINDAWOOD.isRoomMap(innKeyMap) || InnHelper.WINDAWOOD.isHallMap(innKeyMap)))// 우드백 여관
				|| ((locx == ruunInCheckLoc[0] && locy == ruunInCheckLoc[1] && pc.getMapId() == ruunInCheckLoc[2]) && (InnHelper.RUUN.isRoomMap(innKeyMap) || InnHelper.RUUN.isHallMap(innKeyMap)))) {// 루운성 여관
		    	keyMap = (short) innKeyMap;
			    break;
		    }
	    }
	    key = null;
	    if (keyMap != 0) {
	    	int[] inLoc = InnHelper.getInLoc(keyMap);
	    	return actionMove(pc, inLoc[0], inLoc[1], (InnHelper.RUUN.isRoomMap(keyMap) || InnHelper.RUUN.isHallMap(keyMap)) ? (byte)4 : (byte)6, (short)keyMap);
		}
		return false;
	}
	
	static boolean actionMove(L1PcInstance pc, int x, int y, byte heading, short mapId){
		if (pc.isNotTeleport() || pc.getTeleport().isTeleport()) {
			return false;
		}
		pc.getTeleport().initPortal(x, y, mapId, heading);
		return true;
	}
}

