package l1j.server.GameSystem.ai.constuct;

/**
 * Ai 혈맹
 * @author LinOffice
 */
public enum AiPledge {
	RED_KNIGHT(		"Red Knights",	23197	),
	BLACK_KNIGHT(	"Black Knights",	23195	),
	GOLD_KNIGHT(	"Golden Knights",		23201	),
	;
	private String _pledge_name;
	private int _apc_pledge_icon;
	AiPledge(String pledge_name, int apc_pledge_icon) {
		_pledge_name		= pledge_name;
		_apc_pledge_icon	= apc_pledge_icon;
	}
	public String getPledgeName(){
		return _pledge_name;
	}
	public int getApcPledgeIcon(){
		return _apc_pledge_icon;
	}
	public static AiPledge fromString(String clanName){
		switch(clanName){
		case "Red Knights":	return RED_KNIGHT;
		case "Black Knights":	return BLACK_KNIGHT;
		case "Golden Knights":	return GOLD_KNIGHT;
		default:		return null;
		}
	}
	public static AiPledge fromInt(int teamType){
		switch(teamType){
		case 1:	return RED_KNIGHT;
		case 2:	return BLACK_KNIGHT;
		case 3:	return GOLD_KNIGHT;
		default:return null;
		}
	}
}

