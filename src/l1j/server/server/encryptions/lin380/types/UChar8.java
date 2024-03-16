package l1j.server.server.encryptions.lin380.types;

public class UChar8 {

	public static char[] fromArray(long[] buff){
		char[] charBuff = new char[buff.length << 2];
		for (int i = 0; i < buff.length; i++) {
			charBuff[(i * 4 + 0)] = ((char)(int)(buff[i] & 0xFF));
			charBuff[(i * 4 + 1)] = ((char)(int)(buff[i] >> 8 & 0xFF));
			charBuff[(i * 4 + 2)] = ((char)(int)(buff[i] >> 16 & 0xFF));
			charBuff[(i * 4 + 3)] = ((char)(int)(buff[i] >> 24 & 0xFF));
		}
		return charBuff;
	}

	public static char[] fromArray(byte[] buff){
		char[] charBuff = new char[buff.length];
		for (int i = 0; i < buff.length; i++) {
			charBuff[i] = ((char)(buff[i] & 0xFF));
		}
		return charBuff;
	}

	public static char[] fromArray(byte[] buff, int length){
		char[] charBuff = new char[length];
		for (int i = 0; i < length; i++) {
			charBuff[i] = ((char)(buff[i] & 0xFF));
		}
		return charBuff;
	}

	public static char fromUByte8(byte b){
		return (char)(b & 0xFF);
	}

	public static char[] fromULong32(long l){
		char[] charBuff = new char[4];
		charBuff[0] = ((char)(int)(l & 0xFF));
		charBuff[1] = ((char)(int)(l >> 8 & 0xFF));
		charBuff[2] = ((char)(int)(l >> 16 & 0xFF));
		charBuff[3] = ((char)(int)(l >> 24 & 0xFF));
		return charBuff;
	}

}

