package l1j.server.server.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {
	public static final String ENCRYPT_KEY = "[LinOfficeAesCoder]";
	private byte[] keyBytes;
	public AesUtil(String key){
		keyBytes = new byte[16];
		try {
			byte[] keyBytes = key.getBytes(CharsetUtil.UTF_8_STR);
			System.arraycopy(keyBytes, 0, keyBytes, 0, 16);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String encrypt(String source) {
		try {
			byte[] buff = source.getBytes(CharsetUtil.UTF_8_STR);
			buff = encrypt(buff);
			return fromHex(buff);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] encrypt(final byte[] buff) throws Exception{
		SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		return cipher.doFinal(addPadding(buff));
	}
	
	public String decrypt(final String source){
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] dArray = removePadding(cipher.doFinal(toBytes(source)));
			return new String(dArray, CharsetUtil.UTF_8_STR);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] decrypt(final byte[] buff) throws Exception{
		SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		return removePadding(cipher.doFinal(buff));
	}
	
	private byte[] removePadding(final byte[] pBytes){
		int pCount = pBytes.length;
		int idx = 0;
		boolean loop = true;
		while (loop) {
			if (idx == pCount || pBytes[idx] == 0x00) {
				loop = false;
				--idx;
			}
			++idx;
		}
		
		byte[] tBytes = new byte[idx];
		System.arraycopy(pBytes,  0,  tBytes,  0, idx);
		return tBytes;
	}
	
	private byte[] addPadding(final byte[] pBytes){
		int pCount = pBytes.length;
		int tCount = pCount + (16 - (pCount % 16));
		byte[] tBytes = new byte[tCount];
		System.arraycopy(pBytes, 0, tBytes, 0, pCount);
		return tBytes;
	}
	
	public static byte[] toBytes(final String pString){
		StringBuffer buff = new StringBuffer(pString);
		int bCount = buff.length() >> 1;
		byte[] bArray = new byte[bCount];
		for (int bIndex = 0; bIndex < bCount; ++bIndex) {
			bArray[bIndex] = (byte)Long.parseLong(buff.substring(bIndex << 1, (bIndex << 1) + 2), 16);
		}
		return bArray;
	}
	
	public static String fromHex(byte[] pBytes){
		int pCount = pBytes.length;
		StringBuilder sb = new StringBuilder((pCount << 1) + 8);
		for (int pIndex = 0; pIndex < pCount; ++pIndex) {
			if (((int)pBytes[pIndex] & 0xff) < 0x10) {
				sb.append(0);
			}
			sb.append(Long.toString((int)pBytes[pIndex] & 0xff, 16));
		}
		return sb.toString();
	}
}

