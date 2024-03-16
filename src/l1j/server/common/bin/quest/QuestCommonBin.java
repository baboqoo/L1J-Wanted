package l1j.server.common.bin.quest;

import java.util.HashMap;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class QuestCommonBin implements ProtoMessage {
	public static QuestCommonBin newInstance() {
		return new QuestCommonBin();
	}
	
	private HashMap<Integer, QuestCommonBinExtend> _quest_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private QuestCommonBin() {
	}

	public HashMap<Integer, QuestCommonBinExtend> get_quest_list() {
		return _quest_list;
	}

	public QuestCommonBinExtend get_quest(int quest_number) {
		return _quest_list.get(quest_number);
	}

	public void add_quest(QuestCommonBinExtend val) {
		if (!has_quest_list()) {
			_quest_list = new HashMap<Integer, QuestCommonBinExtend>();
			_bit |= 0x1;
		}
		_quest_list.put(val.get_quest_number(), val);
	}

	public boolean has_quest_list() {
		return (_bit & 0x1) == 0x1;
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
		if (has_quest_list()){
			for (QuestCommonBinExtend val : _quest_list.values()) {
				size += ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_quest_list()) {
			for (QuestCommonBinExtend val : _quest_list.values()) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_quest_list()){
			for (QuestCommonBinExtend val : _quest_list.values()) {
				output.writeMessage(1, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000012: {
				add_quest((QuestCommonBinExtend) input.readMessage(QuestCommonBinExtend.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[QuestCommonBin] NEW_TAG : TAG(%d)", tag));
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
	
	public static class QuestCommonBinExtend implements ProtoMessage {
		public static QuestCommonBinExtend newInstance() {
			return new QuestCommonBinExtend();
		}
		
		private int _quest_number;
		private QuestT _quest;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private QuestCommonBinExtend() {
		}
		
		public int get_quest_number(){
			return _quest_number;
		}
		
		public void set_quest_number(int val) {
			_quest_number = val;
			_bit |= 0x1;
		}
		
		public boolean has_quest_number() {
			return (_bit & 0x1) == 0x1;
		}

		public QuestT get_quest() {
			return _quest;
		}

		public void set_quest(QuestT val) {
			_quest = val;
			_bit |= 0x2;
		}

		public boolean has_quest() {
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
			if (has_quest_number()){
				size += ProtoOutputStream.computeUInt32Size(1, _quest_number);
			}
			if (has_quest()) {
				size += ProtoOutputStream.computeMessageSize(2, _quest);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_quest_number()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_quest()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_quest_number()){
				output.writeUInt32(1, _quest_number);
			}
			if (has_quest()){
				output.writeMessage(2, _quest);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
				case 0x00000008:
					set_quest_number(input.readInt32());
					break;
				case 0x00000012:
					set_quest((QuestT) input.readMessage(QuestT.newInstance()));
					break;
				default:
					System.out.println(String.format("[QuestCommonBinExtend] NEW_TAG : TAG(%d)", tag));
					return this;
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
}

