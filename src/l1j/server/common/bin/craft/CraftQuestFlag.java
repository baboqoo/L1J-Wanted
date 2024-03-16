package l1j.server.common.bin.craft;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CraftQuestFlag implements ProtoMessage {
	public static CraftQuestFlag newInstance() {
		return new CraftQuestFlag();
	}

	private long _flag1;
	private long _flag2;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CraftQuestFlag() {
	}

	public long get_flag1() {
		return _flag1;
	}

	public void set_flag1(long val) {
		_bit |= 0x1;
		_flag1 = val;
	}

	public boolean has_flag1() {
		return (_bit & 0x1) == 0x1;
	}

	public long get_flag2() {
		return _flag2;
	}

	public void set_flag2(long val) {
		_bit |= 0x2;
		_flag2 = val;
	}

	public boolean has_flag2() {
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
		if (has_flag1()){
			size += ProtoOutputStream.computeInt64Size(1, _flag1);
		}
		if (has_flag2()){
			size += ProtoOutputStream.computeInt64Size(2, _flag2);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_flag1()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_flag2()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_flag1()){
			output.writeInt64(1, _flag1);
		}
		if (has_flag2()){
			output.writeInt64(2, _flag2);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000008: {
				set_flag1(input.readInt64());
				break;
			}
			case 0x00000010: {
				set_flag2(input.readInt64());
				break;
			}
			default: {
				System.out.println(String.format("[CraftQuestFlag] NEW_TAG : TAG(%d)", tag));
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

