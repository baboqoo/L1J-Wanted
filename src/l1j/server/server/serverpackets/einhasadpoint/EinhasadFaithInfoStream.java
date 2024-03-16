package l1j.server.server.serverpackets.einhasadpoint;

import l1j.server.server.utils.BinaryOutputStream;

public class EinhasadFaithInfoStream extends BinaryOutputStream {
	
	protected EinhasadFaithInfoStream(EinhasadFaithInfoStream.FaithInfoType type, int groupId, int indexId, int expiredTime) {
		super();
		write_type(type);
		write_groupId(groupId);
		write_indexId(indexId);
		write_isEnable(expiredTime > 0);
		write_expiredTime(expiredTime);
	}
	
	void write_type(EinhasadFaithInfoStream.FaithInfoType type) {
		writeC(0x08);// type
		writeC(type.value);
	}
	
	void write_groupId(int groupId) {
		writeC(0x10);// groupId
		writeBit(groupId);
	}
	
	void write_indexId(int indexId) {
		writeC(0x18);// indexId
		writeBit(indexId);
	}
	
	void write_isEnable(boolean isEnable) {
		writeC(0x20);// isEnable
		writeB(isEnable);
	}
	
	void write_expiredTime(int expiredTime) {
		writeC(0x28);// expiredTime
		writeBit(expiredTime);
	}
	
	public enum FaithInfoType{
		Group(1),
		Index(2),
		;
		private int value;
		FaithInfoType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(FaithInfoType v){
			return value == v.value;
		}
		public static FaithInfoType fromInt(int i){
			switch(i){
			case 1:
				return Group;
			case 2:
				return Index;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments FaithInfoType, %d", i));
			}
		}
	}
}

