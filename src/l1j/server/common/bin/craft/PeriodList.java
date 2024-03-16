package l1j.server.common.bin.craft;

import java.util.Calendar;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;
import l1j.server.server.utils.CommonUtil;

public class PeriodList implements ProtoMessage {
	public boolean isValidtion() {
		if (_period == null || _period.isEmpty()) {
			return true;
		}
		Calendar cal	= Calendar.getInstance();
		int day			= cal.get(Calendar.DAY_OF_WEEK) - 1;
		//String today	= CommonUtil.WEEK_DAY_FLAG_ARRAY[day];
		String today	= CommonUtil.WEEK_DAY_ARRAY[day];
		int hour		= cal.get(Calendar.HOUR_OF_DAY);
		for (Period val : _period) {
			if (val.isValidation(today, hour)) {
				return true;
			}
		}
		return false;
	}
	
	public static PeriodList newInstance() {
		return new PeriodList();
	}

	private java.util.LinkedList<Period> _period;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private PeriodList() {
	}

	public java.util.LinkedList<Period> get_period() {
		return _period;
	}

	public void add_period(Period val) {
		if (!has_period()) {
			_period = new java.util.LinkedList<Period>();
			_bit |= 0x1;
		}
		_period.add(val);
	}

	public boolean has_period() {
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
		if (has_period()){
			for(Period val : _period){
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
		if (has_period()) {
			for (Period val : _period) {
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
		if (has_period()){
			for (Period val : _period){
				output.writeMessage(1, val);
			}
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x0000000A: {
				add_period((Period) input.readMessage(Period.newInstance()));
				break;
			}
			default: {
				System.out.println(String.format("[Craft.PeriodList] NEW_TAG : TAG(%d)", tag));
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

