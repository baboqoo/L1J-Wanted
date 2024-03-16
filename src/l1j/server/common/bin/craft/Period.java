package l1j.server.common.bin.craft;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.server.utils.StringUtil;

public class Period implements ProtoMessage {
	/**
	 * 제작 시간 검증
	 * @param hour
	 * @return boolean
	 */
	static final String WEEK_DAY_FLAG_REGEX = "SUN|MON|TUE|WED|THU|FRI|SAT";
	public boolean isValidation(String today, int hour) {
		String[] array		= _start_time.split(StringUtil.EmptyOneString);
		String enable_day	= array[array.length - 1];
		if (enable_day.matches(WEEK_DAY_FLAG_REGEX) && !today.equals(enable_day)) {
			return false;
		}
		int start_hour		= Integer.parseInt(array[2]);
		int end_hour		= start_hour + (_duration_sec / 3600);
		return hour >= start_hour && hour <= end_hour;
	}
	
	public static Period newInstance() {
		return new Period();
	}

	private String _start_time;
	private int _duration_sec;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private Period() {
	}

	public String get_start_time() {
		return _start_time;
	}

	public void set_start_time(String val) {
		_bit |= 0x1;
		_start_time = val;
	}

	public boolean has_start_time() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_duration_sec() {
		return _duration_sec;
	}

	public void set_duration_sec(int val) {
		_bit |= 0x2;
		_duration_sec = val;
	}

	public boolean has_duration_sec() {
		return (_bit & 0x2) == 0x2;
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_start_time()){
			size += ProtoOutputStream.computeStringSize(1, _start_time);
		}
		if (has_duration_sec()){
			size += ProtoOutputStream.computeUInt32Size(2, _duration_sec);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_start_time()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_duration_sec()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_start_time()){
			output.writeString(1, _start_time);
		}
		if (has_duration_sec()){
			output.writeUInt32(2, _duration_sec);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x0000000A: {
				set_start_time(input.readString());
				break;
			}
			case 0x00000010: {
				set_duration_sec(input.readUInt32());
				break;
			}
			default: {
				System.out.println(String.format("[Craft.Period] NEW_TAG : TAG(%d)", tag));
				return this;
			}
			}
		}
		return this;
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}

