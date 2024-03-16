package l1j.server.server.serverpackets.eventpush;

public enum EventPushResultCode {
	EVENT_PUSH_SUCCESS(1),
	EVENT_PUSH_SYSTEM_LOAD_LIST_FAIL(2),
	EVENT_PUSH_LIST_IS_NOT_LOAD(3),
	EVENT_PUSH_CAN_NOT_FIND_USER(4),
	EVENT_PUSH_CAN_NOT_CHANGE_STATE(5),
	EVENT_PUSH_FAIL_REQ_IS_INVALID(6),
	EVENT_PUSH_FAIL_ALREADY_DONE(7),
	EVENT_PUSH_FAIL_REFRESH_LIST(8),
	EVENT_PUSH_FAIL_CAN_NOT_FIND_EVENT_PUSH(9),
	EVENT_PUSH_FAIL_RECEIVE_ITEM_SAFE_PLACE(10),
	EVENT_PUSH_SYSTEM_RESULT_CODE_INVALID(11),
	EVENT_PUSH_SYSTEM_RESULT_FAIL(12),
	EVENT_PUSH_RECEIVE_FAIL_WEIGHT(13),
	EVENT_PUSH_RECEIVE_FAIL_INVENTORY_SIZE(14),
	EVENT_PUSH_RECEIVE_FAIL_ALREADY_FULL(15),
	EVENT_PUSH_RECEIVE_FAIL_INVALID_ITEM(16),
	EVENT_PUSH_CAN_NOT_USE_INTER_SERVER(17),
	EVENT_PUSH_RECEIVE_FAIL_CAN_GET_SAFETY_ZONE(18),
	;
	private int value;
	EventPushResultCode(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(EventPushResultCode v){
		return value == v.value;
	}
	public static EventPushResultCode fromInt(int i){
		switch(i){
		case 1:
			return EVENT_PUSH_SUCCESS;
		case 2:
			return EVENT_PUSH_SYSTEM_LOAD_LIST_FAIL;
		case 3:
			return EVENT_PUSH_LIST_IS_NOT_LOAD;
		case 4:
			return EVENT_PUSH_CAN_NOT_FIND_USER;
		case 5:
			return EVENT_PUSH_CAN_NOT_CHANGE_STATE;
		case 6:
			return EVENT_PUSH_FAIL_REQ_IS_INVALID;
		case 7:
			return EVENT_PUSH_FAIL_ALREADY_DONE;
		case 8:
			return EVENT_PUSH_FAIL_REFRESH_LIST;
		case 9:
			return EVENT_PUSH_FAIL_CAN_NOT_FIND_EVENT_PUSH;
		case 10:
			return EVENT_PUSH_FAIL_RECEIVE_ITEM_SAFE_PLACE;
		case 11:
			return EVENT_PUSH_SYSTEM_RESULT_CODE_INVALID;
		case 12:
			return EVENT_PUSH_SYSTEM_RESULT_FAIL;
		case 13:
			return EVENT_PUSH_RECEIVE_FAIL_WEIGHT;
		case 14:
			return EVENT_PUSH_RECEIVE_FAIL_INVENTORY_SIZE;
		case 15:
			return EVENT_PUSH_RECEIVE_FAIL_ALREADY_FULL;
		case 16:
			return EVENT_PUSH_RECEIVE_FAIL_INVALID_ITEM;
		case 17:
			return EVENT_PUSH_CAN_NOT_USE_INTER_SERVER;
		case 18:
			return EVENT_PUSH_RECEIVE_FAIL_CAN_GET_SAFETY_ZONE;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments EventPushResultCode, %d", i));
		}
	}
}

