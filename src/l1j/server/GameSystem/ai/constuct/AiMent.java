package l1j.server.GameSystem.ai.constuct;

/**
 * Ai 멘트 flag
 * @author LinOffice
 */
public enum AiMent {
	LOGIN, LOGOUT, KILL, DEATH
	;
	public static AiMent fromString(String str){
		switch(str){
		case "login":	return LOGIN;
		case "logout":	return LOGOUT;
		case "kill":	return KILL;
		case "death":	return DEATH;
		default:		return null;
		}
	}
}

