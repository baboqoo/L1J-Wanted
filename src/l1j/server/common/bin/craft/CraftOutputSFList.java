package l1j.server.common.bin.craft;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class CraftOutputSFList implements ProtoMessage {
	public static CraftOutputSFList newInstance() {
		return new CraftOutputSFList();
	}

	private CraftQuestFlag _on_flag;
	private CraftQuestFlag _off_flag;
	private int _prob_count;
	private int _non_prob_count;
	private java.util.LinkedList<CraftOutputItem> _output_prob_items;
	private java.util.LinkedList<CraftOutputItem> _output_items;
	private java.util.LinkedList<CraftEvent> _events;
	private int _event_count;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CraftOutputSFList() {
	}

	public CraftQuestFlag get_on_flag() {
		return _on_flag;
	}

	public void set_on_flag(CraftQuestFlag val) {
		_bit |= 0x1;
		_on_flag = val;
	}

	public boolean has_on_flag() {
		return (_bit & 0x1) == 0x1;
	}

	public CraftQuestFlag get_off_flag() {
		return _off_flag;
	}

	public void set_off_flag(CraftQuestFlag val) {
		_bit |= 0x2;
		_off_flag = val;
	}

	public boolean has_off_flag() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_prob_count() {
		return _prob_count;
	}

	public void set_prob_count(int val) {
		_bit |= 0x4;
		_prob_count = val;
	}

	public boolean has_prob_count() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_non_prob_count() {
		return _non_prob_count;
	}

	public void set_non_prob_count(int val) {
		_bit |= 0x8;
		_non_prob_count = val;
	}

	public boolean has_non_prob_count() {
		return (_bit & 0x8) == 0x8;
	}

	public java.util.LinkedList<CraftOutputItem> get_output_prob_items() {
		return _output_prob_items;
	}

	public void add_output_prob_items(CraftOutputItem val) {
		if (!has_output_prob_items()) {
			_output_prob_items = new java.util.LinkedList<CraftOutputItem>();
			_bit |= 0x10;
		}
		_output_prob_items.add(val);
	}

	public boolean has_output_prob_items() {
		return (_bit & 0x10) == 0x10;
	}

	public java.util.LinkedList<CraftOutputItem> get_output_items() {
		return _output_items;
	}

	public void add_output_items(CraftOutputItem val) {
		if (!has_output_items()) {
			_output_items = new java.util.LinkedList<CraftOutputItem>();
			_bit |= 0x20;
		}
		_output_items.add(val);
	}

	public boolean has_output_items() {
		return (_bit & 0x20) == 0x20;
	}

	public java.util.LinkedList<CraftEvent> get_events() {
		return _events;
	}
	
	public CraftEvent get_event(int event_id) {
		if (_events == null || event_id <= 0) {
			return null;
		}
		for (CraftEvent event : _events) {
			if (event.get_event_id() == event_id) {
				return event;
			}
		}
		return null;
	}

	public void add_events(CraftEvent val) {
		if (!has_events()) {
			_events = new java.util.LinkedList<CraftEvent>();
			_bit |= 0x40;
		}
		_events.add(val);
	}

	public boolean has_events() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_event_count() {
		return _event_count;
	}

	public void set_event_count(int val) {
		_bit |= 0x80;
		_event_count = val;
	}

	public boolean has_event_count() {
		return (_bit & 0x80) == 0x80;
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
		if (has_on_flag()){
			size += ProtoOutputStream.computeMessageSize(1, _on_flag);
		}
		if (has_off_flag()){
			size += ProtoOutputStream.computeMessageSize(2, _off_flag);
		}
		if (has_prob_count()){
			size += ProtoOutputStream.computeInt32Size(3, _prob_count);
		}
		if (has_non_prob_count()){
			size += ProtoOutputStream.computeInt32Size(4, _non_prob_count);
		}
		if (has_output_prob_items()){
			for(CraftOutputItem val : _output_prob_items){
				size += ProtoOutputStream.computeMessageSize(5, val);
			}
		}
		if (has_output_items()){
			for(CraftOutputItem val : _output_items){
				size += ProtoOutputStream.computeMessageSize(6, val);
			}
		}
		if (has_events()){
			for(CraftEvent val : _events){
				size += ProtoOutputStream.computeMessageSize(7, val);
			}
		}
		if (has_event_count()){
			size += ProtoOutputStream.computeInt32Size(8, _event_count);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_on_flag()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_off_flag()) {
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
		if (has_events()) {
			for (CraftEvent val : _events) {
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
		if (has_on_flag()){
			output.writeMessage(1, _on_flag);
		}
		if (has_off_flag()){
			output.writeMessage(2, _off_flag);
		}
		if (has_prob_count()){
			output.wirteInt32(3, _prob_count);
		}
		if (has_non_prob_count()){
			output.wirteInt32(4, _non_prob_count);
		}
		if (has_output_prob_items()){
			for (CraftOutputItem val : _output_prob_items){
				output.writeMessage(5, val);
			}
		}
		if (has_output_items()){
			for (CraftOutputItem val : _output_items){
				output.writeMessage(6, val);
			}
		}
		if (has_events()){
			for (CraftEvent val : _events){
				output.writeMessage(7, val);
			}
		}
		if (has_event_count()){
			output.wirteInt32(8, _event_count);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x0000000A: {
				set_on_flag((CraftQuestFlag) input.readMessage(CraftQuestFlag.newInstance()));
				break;
			}
			case 0x00000012: {
				set_off_flag((CraftQuestFlag) input.readMessage(CraftQuestFlag.newInstance()));
				break;
			}
			case 0x00000018: {
				set_prob_count(input.readInt32());
				break;
			}
			case 0x00000020: {
				set_non_prob_count(input.readInt32());
				break;
			}
			case 0x0000002A: {
				add_output_prob_items((CraftOutputItem) input.readMessage(CraftOutputItem.newInstance()));
				break;
			}
			case 0x00000032: {
				add_output_items((CraftOutputItem) input.readMessage(CraftOutputItem.newInstance()));
				break;
			}
			case 0x0000003A: {
				add_events((CraftEvent) input.readMessage(CraftEvent.newInstance()));
				break;
			}
			case 0x00000040: {
				set_event_count(input.readInt32());
				break;
			}
			default: {
				System.out.println(String.format("[CraftOutputSFList] NEW_TAG : TAG(%d)", tag));
				return this;
			}
			}
		}
		return this;
	}

	@Override
	public void dispose() {
		if (has_on_flag() && _on_flag != null) {
			_on_flag.dispose();
			_on_flag = null;
		}
		if (has_off_flag() && _off_flag != null) {
			_off_flag.dispose();
			_off_flag = null;
		}
		if (has_output_prob_items()) {
			for (CraftOutputItem val : _output_prob_items)
				val.dispose();
			_output_prob_items.clear();
			_output_prob_items = null;
		}
		if (has_output_items()) {
			for (CraftOutputItem val : _output_items)
				val.dispose();
			_output_items.clear();
			_output_items = null;
		}
		if (has_events()) {
			for (CraftEvent val : _events)
				val.dispose();
			_events.clear();
			_events = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	
}

