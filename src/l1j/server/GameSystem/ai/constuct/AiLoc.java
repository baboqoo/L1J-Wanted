package l1j.server.GameSystem.ai.constuct;

import l1j.server.GameSystem.ai.AiLoader;

/**
 * Ai 좌표
 * @author LinOffice
 */
public enum AiLoc {
	GIKAM_FIRST_FLOOR_LOC(	new int[] { 32777, 32779, AiArea.GIKAM_FIRST_FLOOR.getMapId() }		),
	GIKAM_SECOND_FLOOR_LOC(	new int[] { 32768, 32778, AiArea.GIKAM_SECOND_FLOOR.getMapId() }	),
	;
	private int[] _loc;
	AiLoc(int[] loc){
		_loc = loc;
	}
	public static int[] getLoc(AiArea type){
		switch(type){
		case GIKAM_FIRST_FLOOR:
			return GIKAM_FIRST_FLOOR_LOC._loc;
		case GIKAM_SECOND_FLOOR:
			return GIKAM_SECOND_FLOOR_LOC._loc;
		case FISHING:
			return AiLoader.getInstance().getFishLocation();
		default:
			return null;
		}
	}
}

