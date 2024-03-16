package l1j.server.server.encryptions.lin380.types;

public class UByte8 {

	public byte[] fromArray(long[] buff){
		byte[] byteBuff = new byte[buff.length << 2];
		for (int i = 0; i < buff.length; i++) {
			byteBuff[(i * 4 + 0)] = ((byte)(int)(buff[i] & 0xFF));
			byteBuff[(i * 4 + 1)] = ((byte)(int)(buff[i] >> 8 & 0xFF));
			byteBuff[(i * 4 + 2)] = ((byte)(int)(buff[i] >> 16 & 0xFF));
			byteBuff[(i * 4 + 3)] = ((byte)(int)(buff[i] >> 24 & 0xFF));
		}
		return byteBuff;
	}

	public byte[] fromArray(char[] buff){
		byte[] byteBuff = new byte[buff.length];
		for (int i = 0; i < buff.length; i++) {
			byteBuff[i] = ((byte)(buff[i] & 0xFF));
		}
		return byteBuff;
	}

	public byte[] fromArray(char[] buff, byte[] data) {
		for (int i = 0; i < buff.length; i++) {
			data[i] = ((byte)(buff[i] & 0xFF));
		}
		return data;
	}

	public byte fromUChar8(char c){
		return (byte)(c & 0xFF);
	}

	public byte[] fromULong32(long l){
		byte[] byteBuff = new byte[4];
		byteBuff[0] = ((byte)(int)(l & 0xFF));
		byteBuff[1] = ((byte)(int)(l >> 8 & 0xFF));
		byteBuff[2] = ((byte)(int)(l >> 16 & 0xFF));
		byteBuff[3] = ((byte)(int)(l >> 24 & 0xFF));
		return byteBuff;
	}

}

