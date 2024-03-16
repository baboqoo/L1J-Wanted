package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_EffectList extends ServerBasePacket {
	private static final String S_EFFECT_LIST = "[S] S_EffectList";
	private byte[] _byte = null;
	
	public S_EffectList(int code, eNotiType type, int effectListId, int duration, eDurationShowType showType, boolean isGood, int expireDuration) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(code);
		
		writeC(0x08);
		writeC(type.value);
		
		writeC(0x10);
		writeBit(effectListId);
		
		writeC(0x18);
		writeBit(duration);
		
		writeC(0x20);
		writeC(showType.value);
		
		writeC(0x28);
		writeB(isGood);
		
		writeC(0x30);
		writeBit(expireDuration);
		
		writeH(0x00);
	}
	
	public enum eNotiType{
		NEW(1),
		END(2),
		;
		private int value;
		eNotiType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eNotiType v){
			return value == v.value;
		}
		public static eNotiType fromInt(int i){
			switch(i){
			case 1:
				return NEW;
			case 2:
				return END;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eNotiType, %d", i));
			}
		}
	}
	public enum eDurationShowType{
		TYPE_EFF_NONE(0),
		TYPE_EFF_PERCENT(1),
		TYPE_EFF_MINUTE(2),
		TYPE_EFF_EINHASAD_COOLTIME_MINUTE(3),
		TYPE_EFF_LEGACY_TIME(4),
		TYPE_EFF_DAY_HOUR_MIN(5),
		TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC(6),
		TYPE_EFF_UNLIMIT(7),
		TYPE_EFF_COUNT(8),
		TYPE_EFF_RATE(9),
		TYPE_EFF_HIDDEN(10),
		;
		private int value;
		eDurationShowType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eDurationShowType v){
			return value == v.value;
		}
		public static eDurationShowType fromInt(int i){
			switch(i){
			case 0:
				return TYPE_EFF_NONE;
			case 1:
				return TYPE_EFF_PERCENT;
			case 2:
				return TYPE_EFF_MINUTE;
			case 3:
				return TYPE_EFF_EINHASAD_COOLTIME_MINUTE;
			case 4:
				return TYPE_EFF_LEGACY_TIME;
			case 5:
				return TYPE_EFF_DAY_HOUR_MIN;
			case 6:
				return TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC;
			case 7:
				return TYPE_EFF_UNLIMIT;
			case 8:
				return TYPE_EFF_COUNT;
			case 9:
				return TYPE_EFF_RATE;
			case 10:
				return TYPE_EFF_HIDDEN;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eDurationShowType, %d", i));
			}
		}
	}

	@Override
	public byte[] getContent() {
		if(_byte == null)_byte = _bao.toByteArray();
		return _byte;
	}

	@Override
	public String getType() {
		return S_EFFECT_LIST;
	}
}

