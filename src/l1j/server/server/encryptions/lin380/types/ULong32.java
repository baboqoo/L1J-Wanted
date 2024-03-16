package l1j.server.server.encryptions.lin380.types;

public class ULong32 {

	public static final long MAX_UNSIGNEDLONG_VALUE = 2147483647L;

	public static long fromArray(char[] buff){
		return fromLong64((buff[3] & 0xFF) << '\030' | (buff[2] & 0xFF) << '\020' | (buff[1] & 0xFF) << '\b' | buff[0] & 0xFF);
	}

	public static long fromArray(byte[] buff){
		return fromLong64((buff[3] & 0xFF) << 24 | (buff[2] & 0xFF) << 16 | (buff[1] & 0xFF) << 8 | buff[0] & 0xFF);
	}

	public static long fromLong64(long l){
		return l << 32 >>> 32 & 0xFFFFFFFF;
	}

	public static long fromInt32(int i){
		return i << 32 >>> 32 & 0xFFFFFFFF;
	}

	public static long add(long l1, long l2){
		return fromInt32((int)l1 + (int)l2);
	}

	public static long sub(long l1, long l2){
		return fromInt32((int)l1 - (int)l2);
	}

}

