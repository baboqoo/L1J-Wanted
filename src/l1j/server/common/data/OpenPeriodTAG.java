package l1j.server.common.data;

import l1j.server.common.bin.ProtoInputStream;
import l1j.server.common.bin.ProtoMessage;
import l1j.server.common.bin.ProtoOutputStream;

public class OpenPeriodTAG implements ProtoMessage{
	public static OpenPeriodTAG newInstance(){
		return new OpenPeriodTAG();
	}
	private OpenPeriodTAG.PeriodT _Period;
	private OpenPeriodTAG.WeeksT _Weeks;
	private OpenPeriodTAG.MonthsT _Months;
	private OpenPeriodTAG.DaysT _Days;
	private OpenPeriodTAG.TimesT _Times;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private OpenPeriodTAG(){
	}
	public OpenPeriodTAG.PeriodT get_Period(){
		return _Period;
	}
	public void set_Period(OpenPeriodTAG.PeriodT val){
		_bit |= 0x1;
		_Period = val;
	}
	public boolean has_Period(){
		return (_bit & 0x1) == 0x1;
	}
	public OpenPeriodTAG.WeeksT get_Weeks(){
		return _Weeks;
	}
	public void set_Weeks(OpenPeriodTAG.WeeksT val){
		_bit |= 0x2;
		_Weeks = val;
	}
	public boolean has_Weeks(){
		return (_bit & 0x2) == 0x2;
	}
	public OpenPeriodTAG.MonthsT get_Months(){
		return _Months;
	}
	public void set_Months(OpenPeriodTAG.MonthsT val){
		_bit |= 0x4;
		_Months = val;
	}
	public boolean has_Months(){
		return (_bit & 0x4) == 0x4;
	}
	public OpenPeriodTAG.DaysT get_Days(){
		return _Days;
	}
	public void set_Days(OpenPeriodTAG.DaysT val){
		_bit |= 0x8;
		_Days = val;
	}
	public boolean has_Days(){
		return (_bit & 0x8) == 0x8;
	}
	public OpenPeriodTAG.TimesT get_Times(){
		return _Times;
	}
	public void set_Times(OpenPeriodTAG.TimesT val){
		_bit |= 0x10;
		_Times = val;
	}
	public boolean has_Times(){
		return (_bit & 0x10) == 0x10;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_Period()){
			size += ProtoOutputStream.computeMessageSize(1, _Period);
		}
		if (has_Weeks()){
			size += ProtoOutputStream.computeMessageSize(2, _Weeks);
		}
		if (has_Months()){
			size += ProtoOutputStream.computeMessageSize(3, _Months);
		}
		if (has_Days()){
			size += ProtoOutputStream.computeMessageSize(4, _Days);
		}
		if (has_Times()){
			size += ProtoOutputStream.computeMessageSize(5, _Times);
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(ProtoOutputStream output) throws java.io.IOException{
		if (has_Period()){
			output.writeMessage(1, _Period);
		}
		if (has_Weeks()){
			output.writeMessage(2, _Weeks);
		}
		if (has_Months()){
			output.writeMessage(3, _Months);
		}
		if (has_Days()){
			output.writeMessage(4, _Days);
		}
		if (has_Times()){
			output.writeMessage(5, _Times);
		}
	}
	@Override
	public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					set_Period((OpenPeriodTAG.PeriodT)input.readMessage(OpenPeriodTAG.PeriodT.newInstance()));
					break;
				}
				case 0x00000012:{
					set_Weeks((OpenPeriodTAG.WeeksT)input.readMessage(OpenPeriodTAG.WeeksT.newInstance()));
					break;
				}
				case 0x0000001A:{
					set_Months((OpenPeriodTAG.MonthsT)input.readMessage(OpenPeriodTAG.MonthsT.newInstance()));
					break;
				}
				case 0x00000022:{
					set_Days((OpenPeriodTAG.DaysT)input.readMessage(OpenPeriodTAG.DaysT.newInstance()));
					break;
				}
				case 0x0000002A:{
					set_Times((OpenPeriodTAG.TimesT)input.readMessage(OpenPeriodTAG.TimesT.newInstance()));
					break;
				}
				default:{
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class TimeT implements ProtoMessage{
		public static TimeT newInstance(){
			return new TimeT();
		}
		private byte[] _StartTime;
		private int _Duration;
		private int _DurationSec;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private TimeT(){
		}
		public byte[] get_StartTime(){
			return _StartTime;
		}
		public void set_StartTime(byte[] val){
			_bit |= 0x1;
			_StartTime = val;
		}
		public boolean has_StartTime(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_Duration(){
			return _Duration;
		}
		public void set_Duration(int val){
			_bit |= 0x2;
			_Duration = val;
		}
		public boolean has_Duration(){
			return (_bit & 0x2) == 0x2;
		}
		public int get_DurationSec(){
			return _DurationSec;
		}
		public void set_DurationSec(int val){
			_bit |= 0x4;
			_DurationSec = val;
		}
		public boolean has_DurationSec(){
			return (_bit & 0x4) == 0x4;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_StartTime()){
				size += ProtoOutputStream.computeBytesSize(1, _StartTime);
			}
			if (has_Duration()){
				size += ProtoOutputStream.computeInt32Size(2, _Duration);
			}
			if (has_DurationSec()){
				size += ProtoOutputStream.computeInt32Size(3, _DurationSec);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_StartTime()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_StartTime()){
				output.writeBytes(1, _StartTime);
			}
			if (has_Duration()){
				output.wirteInt32(2, _Duration);
			}
			if (has_DurationSec()){
				output.wirteInt32(3, _DurationSec);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						set_StartTime(input.readBytes());
						break;
					}
					case 0x00000010:{
						set_Duration(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_DurationSec(input.readInt32());
						break;
					}
					default:{
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
	
	public static class TimesT implements ProtoMessage{
		public static TimesT newInstance(){
			return new TimesT();
		}
		private java.util.LinkedList<OpenPeriodTAG.TimeT> _Time;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private TimesT(){
		}
		public java.util.LinkedList<OpenPeriodTAG.TimeT> get_Time(){
			return _Time;
		}
		public void add_Time(OpenPeriodTAG.TimeT val){
			if(!has_Time()){
				_Time = new java.util.LinkedList<OpenPeriodTAG.TimeT>();
				_bit |= 0x1;
			}
			_Time.add(val);
		}
		public boolean has_Time(){
			return (_bit & 0x1) == 0x1;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_Time()){
				for(OpenPeriodTAG.TimeT val : _Time){
					size += ProtoOutputStream.computeMessageSize(1, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (has_Time()){
				for(OpenPeriodTAG.TimeT val : _Time){
					if (!val.isInitialized()){
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
			if (has_Time()){
				for (OpenPeriodTAG.TimeT val : _Time){
					output.writeMessage(1, val);
				}
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						add_Time((OpenPeriodTAG.TimeT)input.readMessage(OpenPeriodTAG.TimeT.newInstance()));
						break;
					}
					default:{
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
	public static class DayT implements ProtoMessage{
		public static DayT newInstance(){
			return new DayT();
		}
		private int _Date;
		private OpenPeriodTAG.TimesT _Times;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private DayT(){
		}
		public int get_Date(){
			return _Date;
		}
		public void set_Date(int val){
			_bit |= 0x1;
			_Date = val;
		}
		public boolean has_Date(){
			return (_bit & 0x1) == 0x1;
		}
		public OpenPeriodTAG.TimesT get_Times(){
			return _Times;
		}
		public void set_Times(OpenPeriodTAG.TimesT val){
			_bit |= 0x2;
			_Times = val;
		}
		public boolean has_Times(){
			return (_bit & 0x2) == 0x2;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_Date()){
				size += ProtoOutputStream.computeInt32Size(1, _Date);
			}
			if (has_Times()){
				size += ProtoOutputStream.computeMessageSize(2, _Times);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_Date()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_Date()){
				output.wirteInt32(1, _Date);
			}
			if (has_Times()){
				output.writeMessage(2, _Times);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_Date(input.readInt32());
						break;
					}
					case 0x00000012:{
						set_Times((OpenPeriodTAG.TimesT)input.readMessage(OpenPeriodTAG.TimesT.newInstance()));
						break;
					}
					default:{
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
	public static class DaysT implements ProtoMessage{
		public static DaysT newInstance(){
			return new DaysT();
		}
		private java.util.LinkedList<OpenPeriodTAG.DayT> _Day;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private DaysT(){
		}
		public java.util.LinkedList<OpenPeriodTAG.DayT> get_Day(){
			return _Day;
		}
		public void add_Day(OpenPeriodTAG.DayT val){
			if(!has_Day()){
				_Day = new java.util.LinkedList<OpenPeriodTAG.DayT>();
				_bit |= 0x1;
			}
			_Day.add(val);
		}
		public boolean has_Day(){
			return (_bit & 0x1) == 0x1;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_Day()){
				for(OpenPeriodTAG.DayT val : _Day){
					size += ProtoOutputStream.computeMessageSize(1, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (has_Day()){
				for(OpenPeriodTAG.DayT val : _Day){
					if (!val.isInitialized()){
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
			if (has_Day()){
				for (OpenPeriodTAG.DayT val : _Day){
					output.writeMessage(1, val);
				}
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						add_Day((OpenPeriodTAG.DayT)input.readMessage(OpenPeriodTAG.DayT.newInstance()));
						break;
					}
					default:{
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
	public static class PeriodT implements ProtoMessage{
		public static PeriodT newInstance(){
			return new PeriodT();
		}
		private byte[] _StartDate;
		private byte[] _EndDate;
		private OpenPeriodTAG.MonthsT _Months;
		private OpenPeriodTAG.WeeksT _Weeks;
		private OpenPeriodTAG.DaysT _Days;
		private OpenPeriodTAG.TimesT _Times;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private PeriodT(){
		}
		public byte[] get_StartDate(){
			return _StartDate;
		}
		public void set_StartDate(byte[] val){
			_bit |= 0x1;
			_StartDate = val;
		}
		public boolean has_StartDate(){
			return (_bit & 0x1) == 0x1;
		}
		public byte[] get_EndDate(){
			return _EndDate;
		}
		public void set_EndDate(byte[] val){
			_bit |= 0x2;
			_EndDate = val;
		}
		public boolean has_EndDate(){
			return (_bit & 0x2) == 0x2;
		}
		public OpenPeriodTAG.MonthsT get_Months(){
			return _Months;
		}
		public void set_Months(OpenPeriodTAG.MonthsT val){
			_bit |= 0x4;
			_Months = val;
		}
		public boolean has_Months(){
			return (_bit & 0x4) == 0x4;
		}
		public OpenPeriodTAG.WeeksT get_Weeks(){
			return _Weeks;
		}
		public void set_Weeks(OpenPeriodTAG.WeeksT val){
			_bit |= 0x8;
			_Weeks = val;
		}
		public boolean has_Weeks(){
			return (_bit & 0x8) == 0x8;
		}
		public OpenPeriodTAG.DaysT get_Days(){
			return _Days;
		}
		public void set_Days(OpenPeriodTAG.DaysT val){
			_bit |= 0x10;
			_Days = val;
		}
		public boolean has_Days(){
			return (_bit & 0x10) == 0x10;
		}
		public OpenPeriodTAG.TimesT get_Times(){
			return _Times;
		}
		public void set_Times(OpenPeriodTAG.TimesT val){
			_bit |= 0x20;
			_Times = val;
		}
		public boolean has_Times(){
			return (_bit & 0x20) == 0x20;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_StartDate()){
				size += ProtoOutputStream.computeBytesSize(1, _StartDate);
			}
			if (has_EndDate()){
				size += ProtoOutputStream.computeBytesSize(2, _EndDate);
			}
			if (has_Months()){
				size += ProtoOutputStream.computeMessageSize(3, _Months);
			}
			if (has_Weeks()){
				size += ProtoOutputStream.computeMessageSize(4, _Weeks);
			}
			if (has_Days()){
				size += ProtoOutputStream.computeMessageSize(5, _Days);
			}
			if (has_Times()){
				size += ProtoOutputStream.computeMessageSize(6, _Times);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_StartDate()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_EndDate()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_StartDate()){
				output.writeBytes(1, _StartDate);
			}
			if (has_EndDate()){
				output.writeBytes(2, _EndDate);
			}
			if (has_Months()){
				output.writeMessage(3, _Months);
			}
			if (has_Weeks()){
				output.writeMessage(4, _Weeks);
			}
			if (has_Days()){
				output.writeMessage(5, _Days);
			}
			if (has_Times()){
				output.writeMessage(6, _Times);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						set_StartDate(input.readBytes());
						break;
					}
					case 0x00000012:{
						set_EndDate(input.readBytes());
						break;
					}
					case 0x0000001A:{
						set_Months((OpenPeriodTAG.MonthsT)input.readMessage(OpenPeriodTAG.MonthsT.newInstance()));
						break;
					}
					case 0x00000022:{
						set_Weeks((OpenPeriodTAG.WeeksT)input.readMessage(OpenPeriodTAG.WeeksT.newInstance()));
						break;
					}
					case 0x0000002A:{
						set_Days((OpenPeriodTAG.DaysT)input.readMessage(OpenPeriodTAG.DaysT.newInstance()));
						break;
					}
					case 0x00000032:{
						set_Times((OpenPeriodTAG.TimesT)input.readMessage(OpenPeriodTAG.TimesT.newInstance()));
						break;
					}
					default:{
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
	
	public static class WeekT implements ProtoMessage{
		public static WeekT newInstance(){
			return new WeekT();
		}
		private OpenPeriodTAG.WeekT.eDayOfWeek _WeekDay;
		private OpenPeriodTAG.DaysT _Days;
		private OpenPeriodTAG.TimesT _Times;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private WeekT(){
		}
		public OpenPeriodTAG.WeekT.eDayOfWeek get_WeekDay(){
			return _WeekDay;
		}
		public void set_WeekDay(OpenPeriodTAG.WeekT.eDayOfWeek val){
			_bit |= 0x1;
			_WeekDay = val;
		}
		public boolean has_WeekDay(){
			return (_bit & 0x1) == 0x1;
		}
		public OpenPeriodTAG.DaysT get_Days(){
			return _Days;
		}
		public void set_Days(OpenPeriodTAG.DaysT val){
			_bit |= 0x2;
			_Days = val;
		}
		public boolean has_Days(){
			return (_bit & 0x2) == 0x2;
		}
		public OpenPeriodTAG.TimesT get_Times(){
			return _Times;
		}
		public void set_Times(OpenPeriodTAG.TimesT val){
			_bit |= 0x4;
			_Times = val;
		}
		public boolean has_Times(){
			return (_bit & 0x4) == 0x4;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_WeekDay()){
				size += ProtoOutputStream.computeEnumSize(1, _WeekDay.toInt());
			}
			if (has_Days()){
				size += ProtoOutputStream.computeMessageSize(2, _Days);
			}
			if (has_Times()){
				size += ProtoOutputStream.computeMessageSize(3, _Times);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_WeekDay()){
				output.writeEnum(1, _WeekDay.toInt());
			}
			if (has_Days()){
				output.writeMessage(2, _Days);
			}
			if (has_Times()){
				output.writeMessage(3, _Times);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_WeekDay(OpenPeriodTAG.WeekT.eDayOfWeek.fromInt(input.readEnum()));
						break;
					}
					case 0x00000012:{
						set_Days((OpenPeriodTAG.DaysT)input.readMessage(OpenPeriodTAG.DaysT.newInstance()));
						break;
					}
					case 0x0000001A:{
						set_Times((OpenPeriodTAG.TimesT)input.readMessage(OpenPeriodTAG.TimesT.newInstance()));
						break;
					}
					default:{
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
		public enum eDayOfWeek{
			SUN(0),
			MON(1),
			TUE(2),
			WED(3),
			THU(4),
			FRI(5),
			SAT(6),
			;
			private int value;
			eDayOfWeek(int val){
				value = val;
			}
			public int toInt(){
				return value;
			}
			public boolean equals(eDayOfWeek v){
				return value == v.value;
			}
			public static eDayOfWeek fromInt(int i){
				switch(i){
				case 0:
					return SUN;
				case 1:
					return MON;
				case 2:
					return TUE;
				case 3:
					return WED;
				case 4:
					return THU;
				case 5:
					return FRI;
				case 6:
					return SAT;
				default:
					throw new IllegalArgumentException(String.format("invalid arguments eDayOfWeek, %d", i));
				}
			}
		}
	}
	
	public static class WeeksT implements ProtoMessage{
		public static WeeksT newInstance(){
			return new WeeksT();
		}
		private java.util.LinkedList<OpenPeriodTAG.WeekT> _Week;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private WeeksT(){
		}
		public java.util.LinkedList<OpenPeriodTAG.WeekT> get_Week(){
			return _Week;
		}
		public void add_Week(OpenPeriodTAG.WeekT val){
			if(!has_Week()){
				_Week = new java.util.LinkedList<OpenPeriodTAG.WeekT>();
				_bit |= 0x1;
			}
			_Week.add(val);
		}
		public boolean has_Week(){
			return (_bit & 0x1) == 0x1;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_Week()){
				for(OpenPeriodTAG.WeekT val : _Week){
					size += ProtoOutputStream.computeMessageSize(1, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (has_Week()){
				for(OpenPeriodTAG.WeekT val : _Week){
					if (!val.isInitialized()){
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
			if (has_Week()){
				for (OpenPeriodTAG.WeekT val : _Week){
					output.writeMessage(1, val);
				}
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						add_Week((OpenPeriodTAG.WeekT)input.readMessage(OpenPeriodTAG.WeekT.newInstance()));
						break;
					}
					default:{
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
	public static class MonthT implements ProtoMessage{
		public static MonthT newInstance(){
			return new MonthT();
		}
		private OpenPeriodTAG.MonthT.eMonthOfYear _Name;
		private OpenPeriodTAG.WeeksT _Weeks;
		private OpenPeriodTAG.DaysT _Days;
		private OpenPeriodTAG.TimesT _Times;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private MonthT(){
		}
		public OpenPeriodTAG.MonthT.eMonthOfYear get_Name(){
			return _Name;
		}
		public void set_Name(OpenPeriodTAG.MonthT.eMonthOfYear val){
			_bit |= 0x1;
			_Name = val;
		}
		public boolean has_Name(){
			return (_bit & 0x1) == 0x1;
		}
		public OpenPeriodTAG.WeeksT get_Weeks(){
			return _Weeks;
		}
		public void set_Weeks(OpenPeriodTAG.WeeksT val){
			_bit |= 0x2;
			_Weeks = val;
		}
		public boolean has_Weeks(){
			return (_bit & 0x2) == 0x2;
		}
		public OpenPeriodTAG.DaysT get_Days(){
			return _Days;
		}
		public void set_Days(OpenPeriodTAG.DaysT val){
			_bit |= 0x4;
			_Days = val;
		}
		public boolean has_Days(){
			return (_bit & 0x4) == 0x4;
		}
		public OpenPeriodTAG.TimesT get_Times(){
			return _Times;
		}
		public void set_Times(OpenPeriodTAG.TimesT val){
			_bit |= 0x8;
			_Times = val;
		}
		public boolean has_Times(){
			return (_bit & 0x8) == 0x8;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_Name()){
				size += ProtoOutputStream.computeEnumSize(1, _Name.toInt());
			}
			if (has_Weeks()){
				size += ProtoOutputStream.computeMessageSize(2, _Weeks);
			}
			if (has_Days()){
				size += ProtoOutputStream.computeMessageSize(3, _Days);
			}
			if (has_Times()){
				size += ProtoOutputStream.computeMessageSize(4, _Times);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_Name()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(ProtoOutputStream output) throws java.io.IOException{
			if (has_Name()){
				output.writeEnum(1, _Name.toInt());
			}
			if (has_Weeks()){
				output.writeMessage(2, _Weeks);
			}
			if (has_Days()){
				output.writeMessage(3, _Days);
			}
			if (has_Times()){
				output.writeMessage(4, _Times);
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x00000008:{
						set_Name(OpenPeriodTAG.MonthT.eMonthOfYear.fromInt(input.readEnum()));
						break;
					}
					case 0x00000012:{
						set_Weeks((OpenPeriodTAG.WeeksT)input.readMessage(OpenPeriodTAG.WeeksT.newInstance()));
						break;
					}
					case 0x0000001A:{
						set_Days((OpenPeriodTAG.DaysT)input.readMessage(OpenPeriodTAG.DaysT.newInstance()));
						break;
					}
					case 0x00000022:{
						set_Times((OpenPeriodTAG.TimesT)input.readMessage(OpenPeriodTAG.TimesT.newInstance()));
						break;
					}
					default:{
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
		public enum eMonthOfYear{
			JAN(0),
			FEB(1),
			MAR(2),
			APR(3),
			MAY(4),
			JUN(5),
			JUL(6),
			AUG(7),
			SEP(8),
			OCT(9),
			NOV(10),
			DEC(11),
			;
			private int value;
			eMonthOfYear(int val){
				value = val;
			}
			public int toInt(){
				return value;
			}
			public boolean equals(eMonthOfYear v){
				return value == v.value;
			}
			public static eMonthOfYear fromInt(int i){
				switch(i){
				case 0:
					return JAN;
				case 1:
					return FEB;
				case 2:
					return MAR;
				case 3:
					return APR;
				case 4:
					return MAY;
				case 5:
					return JUN;
				case 6:
					return JUL;
				case 7:
					return AUG;
				case 8:
					return SEP;
				case 9:
					return OCT;
				case 10:
					return NOV;
				case 11:
					return DEC;
				default:
					throw new IllegalArgumentException(String.format("invalid arguments eMonthOfYear, %d", i));
				}
			}
		}
	}
	
	public static class MonthsT implements ProtoMessage{
		public static MonthsT newInstance(){
			return new MonthsT();
		}
		private java.util.LinkedList<OpenPeriodTAG.MonthT> _Month;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private MonthsT(){
		}
		public java.util.LinkedList<OpenPeriodTAG.MonthT> get_Month(){
			return _Month;
		}
		public void add_Month(OpenPeriodTAG.MonthT val){
			if(!has_Month()){
				_Month = new java.util.LinkedList<OpenPeriodTAG.MonthT>();
				_bit |= 0x1;
			}
			_Month.add(val);
		}
		public boolean has_Month(){
			return (_bit & 0x1) == 0x1;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_Month()){
				for(OpenPeriodTAG.MonthT val : _Month){
					size += ProtoOutputStream.computeMessageSize(1, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (has_Month()){
				for(OpenPeriodTAG.MonthT val : _Month){
					if (!val.isInitialized()){
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
			if (has_Month()){
				for (OpenPeriodTAG.MonthT val : _Month){
					output.writeMessage(1, val);
				}
			}
		}
		@Override
		public ProtoMessage readFrom(ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						add_Month((OpenPeriodTAG.MonthT)input.readMessage(OpenPeriodTAG.MonthT.newInstance()));
						break;
					}
					default:{
						return this;
					}
				}
			}
			return this;
		}

		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}

