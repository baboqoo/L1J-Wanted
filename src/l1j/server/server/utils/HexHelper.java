package l1j.server.server.utils;

public class HexHelper {
	public static final int[] HEX_TABLE = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85,
		0x86, 0x87, 0x88, 0x89, 0x8a, 0x8b, 0x8c, 0x8d, 0x8e, 0x8f, 0x90,
		0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b,
		0x9c, 0x9d, 0x9e, 0x9f, 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6,
		0xa7, 0xa8, 0xa9, 0xaa, 0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0, 0xb1,
		0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb, 0xbc,
		0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2, 0xc3, 0xc4, 0xc5, 0xc6, 0xc7,
		0xc8, 0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd1, 0xd2,
		0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xdb, 0xdc, 0xdd,
		0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8,
		0xe9, 0xea, 0xeb, 0xec, 0xed, 0xee, 0xef, 0xf0, 0xf1, 0xf2, 0xf3,
		0xf4, 0xf5, 0xf6, 0xf7, 0xf8, 0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe,
		0xff
	};
	
	public static int parseRawByte(byte[] buff, final int off){
		return off >= buff.length ? 0 : buff[off] & 0xff;
	}
	
	public static int parseInt32(byte[] buff, final int off){
		return (parseRawByte(buff, off)) | 
				(parseRawByte(buff, off + 1) << 8) | 
				(parseRawByte(buff, off + 2) << 16) | 
				(parseRawByte(buff, off + 3) << 24);
	}
	
	public static int[] parseInt32Array(byte[] buff, int len){
		int size = (len >> 0x00000002) + ((len & 3) == 0 ? 0 : 1);
		int[] arr = new int[size];
		int position = size << 0x00000002;
		for (int i=size - 1; i>=0; --i) {
			position += ~0x00000003;
			arr[i] = parseInt32(buff, position);
		}
		return arr;
	}
	
	public static byte[] parseHexStringToByteArray(String s, String tok){
		String[] spl = s.split(tok);
		byte[] buff = new byte[spl.length];
		for (int i=spl.length - 1; i>=0; --i) {
			buff[i] = (byte)(Integer.parseInt(spl[i], 16) & 0xff);
		}
		return buff;
	}
	
	public static int[] parseHexStringToInt32Array(String s, String tok){
		byte[] b = parseHexStringToByteArray(s, tok);
		return parseInt32Array(b, b.length);
	}
	
	public static boolean compareArrays(int[] buff1, int[] buff2, int len){
		for (int i=len - 1; i>=0; --i) {
			if (buff1[i] != buff2[i]) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean compareArrays(int[] buff1, byte[] buff2, int len){
		return compareArrays(buff1, parseInt32Array(buff2, buff2.length), len);
	}
	
	public static boolean compareArrays(byte[] buff1, byte[] buff2, int len){
		return compareArrays(parseInt32Array(buff1, len), parseInt32Array(buff2, len), len);
	}
	
	public static String toSourceString(byte[] data, int len){
		StringBuilder sb = new StringBuilder(len * 6);
		for (int i=0; i<len; ++i) {
			sb.append(String.format("%02X ", data[i] & 0xff));
		}
		return sb.toString();
	}
	
	public static String toString(byte[] data, int len){
		StringBuilder sb 	= new StringBuilder((len << 2) + 16);
		int cnt				= 0;
		for (int i = 0; i < len; ++i) {
			if (cnt % 16 == 0) {
				sb.append(String.format("%04X : ", i));
			}
			sb.append(String.format("%02X ", data[i] & 0xff));
			cnt++;
			
			if (cnt == 16) {
				sb.append("\t");
				int p = i - 15;
				for (int j = 0; j < 16; j++) {
					sb.append(toHexChar(data[p++]));
				}
				sb.append("\n");
				cnt = 0;
			}
		}
		
		int rest = len & 15;
		if (rest > 0) {
			for (int i=0; i< 17 - rest; i++) {
				sb.append("   ");
			}
			int p = len - rest;
			for (int j=0; j<rest; j++) {
				sb.append(toHexChar(data[p++]));
			}
		}
		sb.append("\n");
		return sb.toString();
	}
	
	public static char toHexChar(int i){
		if (i > 0x1f && i < 0x80) {
			return (char)i;
		}
		return '.';
	}
	
	public static int getTxtToBytesLength(String s){
		int result	= 1;
		int size	= s.length();
		for (int i=0; i<size; i++) {
			if (s.charAt(i) >= 0x7F) {
				result += 2;
			} else {
				result++;
			}
		}
		return result;
	}
	
	public static String DataToPacket(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (int i=0; i<len; i++) {
			if ((counter & 15) == 0) {
				result.append(i).append(":\t");
			}
			result.append(HexToDex(data[i] & 0xFF, 2) + StringUtil.EmptyOneString);
			counter++;
			if (counter == 16) {
				result.append("\n");
				counter = 0; 
			} 
		}
		if ((data.length & 15) > 0) { 
			result.append("\n");
		}
		return result.toString();
	}
	
	/*public static String DataToPacket(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (int i=0;i< len;i++) { 
			if ((counter & 15) == 0) {
				result.append(HexToDex(i,4)+": ");
			}
			result.append(HexToDex(data[i] & 0xff, 2) + StringUtil.EmptyOneString);
			counter++;
			if (counter == 16) {
				result.append("   ");
				int charpoint = i-15;
				for (int a=0; a<16;a++) {
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80) {
						result.append((char)t1);
					} else {
						result.append('.');
					}
				}
				result.append("\n"); counter = 0; 
			} 
		}
		int rest = data.length & 15;
		if (rest > 0) { 
			for (int i=0; i<17-rest;i++) {
				result.append("   "); 
			}
			int charpoint = data.length-rest;
			for (int a=0; a<rest;a++) { 
				int t1 = data[charpoint++]; 
				if (t1 > 0x1f && t1 < 0x80) {
					result.append((char)t1);
				} else {
					result.append('.'); 
				}
			}
			result.append("\n");
		}
		return result.toString();
	}*/
	
	private static String HexToDex(int data, int digits) {
		String number = Integer.toHexString(data);
		for (int i=number.length(); i< digits; i++) {
			number = StringUtil.ZeroString + number;
		}
		return number;
	}
}

