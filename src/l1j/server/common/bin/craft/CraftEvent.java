package l1j.server.common.bin.craft;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CraftEvent implements ProtoMessage {
	public static CraftEvent newInstance() {
		return new CraftEvent();
	}
	
	public CraftOutputItem get_event_output_item() {
		if (_non_prob_count > 0 && _output_items != null) {
			return _output_items.get(_non_prob_count - 1);
		}
		if (_prob_count > 0 && _output_prob_items != null) {
			return _output_prob_items.get(_prob_count - 1);
		}
		return null;
	}

	private int _event_id;
	private int _prob_count;
	private int _non_prob_count;
	private java.util.LinkedList<CraftOutputItem> _output_prob_items;
	private java.util.LinkedList<CraftOutputItem> _output_items;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CraftEvent() {
	}

	public int get_event_id() {
		return _event_id;
	}

	public void set_event_id(int val) {
		_bit |= 0x1;
		_event_id = val;
	}

	public boolean has_event_id() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_prob_count() {
		return _prob_count;
	}

	public void set_prob_count(int val) {
		_bit |= 0x2;
		_prob_count = val;
	}

	public boolean has_prob_count() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_non_prob_count() {
		return _non_prob_count;
	}

	public void set_non_prob_count(int val) {
		_bit |= 0x4;
		_non_prob_count = val;
	}

	public boolean has_non_prob_count() {
		return (_bit & 0x4) == 0x4;
	}

	public java.util.LinkedList<CraftOutputItem> get_output_prob_items() {
		return _output_prob_items;
	}

	public void add_output_prob_items(CraftOutputItem val) {
		if (!has_output_prob_items()) {
			_output_prob_items = new java.util.LinkedList<CraftOutputItem>();
			_bit |= 0x8;
		}
		_output_prob_items.add(val);
	}

	public boolean has_output_prob_items() {
		return (_bit & 0x8) == 0x8;
	}

	public java.util.LinkedList<CraftOutputItem> get_output_items() {
		return _output_items;
	}

	public void add_output_items(CraftOutputItem val) {
		if (!has_output_items()) {
			_output_items = new java.util.LinkedList<CraftOutputItem>();
			_bit |= 0x10;
		}
		_output_items.add(val);
	}

	public boolean has_output_items() {
		return (_bit & 0x10) == 0x10;
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
		if (has_event_id()){
			size += ProtoOutputStream.computeInt32Size(1, _event_id);
		}
		if (has_prob_count()){
			size += ProtoOutputStream.computeInt32Size(2, _prob_count);
		}
		if (has_non_prob_count()){
			size += ProtoOutputStream.computeInt32Size(3, _non_prob_count);
		}
		if (has_output_prob_items()){
			for(CraftOutputItem val : _output_prob_items){
				size += ProtoOutputStream.computeMessageSize(4, val);
			}
		}
		if (has_output_items()){
			for(CraftOutputItem val : _output_items){
				size += ProtoOutputStream.computeMessageSize(5, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_event_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_prob_count()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_non_prob_count()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_output_prob_items()) {
			for (CraftOutputItem val : _output_prob_items) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_output_items()) {
			for (CraftOutputItem val : _output_items) {
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
		if (has_event_id()){
			output.wirteInt32(1, _event_id);
		}
		if (has_prob_count()){
			output.wirteInt32(2, _prob_count);
		}
		if (has_non_prob_count()){
			output.wirteInt32(3, _non_prob_count);
		}
		if (has_output_prob_items()){
			for (CraftOutputItem val : _output_prob_items){
				output.writeMessage(4, val);
			}
		}
		if (has_output_items()){
			for (CraftOutputItem val : _output_items){
				output.writeMessage(5, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000008: {
				set_event_id(input.readInt32());
				break;
			}
			case 0x00000010: {
				set_prob_count(input.readInt32());
				break;
			}
			case 0x00000018: {
				set_non_prob_count(input.readInt32());
				break;
			}
			case 0x00000022: {
				add_output_prob_items((CraftOutputItem) input.readMessage(CraftOutputItem.newInstance()));
				break;
			}
			case 0x0000002A: {
				add_output_items((CraftOutputItem) input.readMessage(CraftOutputItem.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[CraftEvent] NEW_TAG : TAG(%d)", tag));
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

