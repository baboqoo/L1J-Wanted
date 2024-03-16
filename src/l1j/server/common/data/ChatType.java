package l1j.server.common.data;

import java.util.Arrays;
import java.util.List;

public enum ChatType{
	CHAT_NORMAL(0),
	CHAT_WHISPER(1),
	CHAT_SHOUT(2),
	CHAT_WORLD(3),
	CHAT_PLEDGE(4),
	CHAT_HUNT_PARTY(11),
	CHAT_TRADE(12),
	CHAT_PLEDGE_PRINCE(13),
	CHAT_CHAT_PARTY(14),
	CHAT_PLEDGE_ALLIANCE(15),
	CHAT_PLEDGE_NOTICE(17),
	CHAT_CLASS(22),
	CHAT_TEAM(29),
	CHAT_ARENA_TEAM(30),
	CHAT_ARENA_OBSERVER(31),
	CHAT_ROOM_ARENA_ALL(32),
	;
	private int value;
	ChatType(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(ChatType v){
		return value == v.value;
	}
	public static ChatType fromInt(int i){
		switch(i){
		case 0:
			return CHAT_NORMAL;
		case 1:
			return CHAT_WHISPER;
		case 2:
			return CHAT_SHOUT;
		case 3:
			return CHAT_WORLD;
		case 4:
			return CHAT_PLEDGE;
		case 11:
			return CHAT_HUNT_PARTY;
		case 12:
			return CHAT_TRADE;
		case 13:
			return CHAT_PLEDGE_PRINCE;
		case 14:
			return CHAT_CHAT_PARTY;
		case 15:
			return CHAT_PLEDGE_ALLIANCE;
		case 17:
			return CHAT_PLEDGE_NOTICE;
		case 22:
			return CHAT_CLASS;
		case 29:
			return CHAT_TEAM;
		case 30:
			return CHAT_ARENA_TEAM;
		case 31:
			return CHAT_ARENA_OBSERVER;
		case 32:
			return CHAT_ROOM_ARENA_ALL;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments ChatType, %d", i));
		}
	}
	
	private static final List<ChatType> CANNOT_CHAT_PASS_LIST = Arrays.asList(new ChatType[] {
			CHAT_PLEDGE, CHAT_HUNT_PARTY, CHAT_PLEDGE_PRINCE, CHAT_CHAT_PARTY, CHAT_PLEDGE_ALLIANCE, CHAT_PLEDGE_NOTICE
	});
	private static final List<ChatType> CANNOT_CHAT_BATTLE_LIST = Arrays.asList(new ChatType[] {
			CHAT_PLEDGE, CHAT_HUNT_PARTY, CHAT_PLEDGE_PRINCE, CHAT_PLEDGE_ALLIANCE, CHAT_PLEDGE_NOTICE
	});
	
	public static boolean isCannotPass(ChatType chat){
		return CANNOT_CHAT_PASS_LIST.contains(chat);
	}
	
	public static boolean isCannotBattle(ChatType chat){
		return CANNOT_CHAT_BATTLE_LIST.contains(chat);
	}
}

