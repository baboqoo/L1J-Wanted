package l1j.server.server.clientpackets;

import java.math.BigInteger;
import java.util.logging.Logger;

import l1j.server.server.utils.CharsetUtil;

public abstract class ClientBasePacket {
	private static Logger _log = Logger.getLogger(ClientBasePacket.class.getName());
	private byte _decrypt[];
	private int _off;
	
	public ClientBasePacket() {}

	public ClientBasePacket(byte abyte0[]) {
		_log.finest("type=" + getType() + ", len=" + abyte0.length);
		_decrypt = abyte0;
		_off = 1;
	}
	
	public ClientBasePacket(byte abyte0[], int off) {
		_log.finest("type=" + getType() + ", len=" + abyte0.length);
		_decrypt = abyte0;
		_off = off;
	}
	
	public int getOffset() {
		return _off;
	}
	
	public void clear() {
		_decrypt = null;
		_off = 0;
	}
	
	public int readLenght(int obj) {
		if (obj < 0) {
			int length = 0;
			BigInteger b = new BigInteger("18446744073709551615");
			while (BigInteger.valueOf(obj).and(b).shiftRight((length + 1) * 7).longValue() > 0) {
				length++;
			}
			length++;
			return length;
		} else {
			if (obj <= 127) {
				return 1;
			}
			if (obj <= 16383) {
				return 2;
			}
			if (obj <= 2097151) {
				return 3;
			}
			if (obj <= 268435455) {
				return 4;
			}
			if ((long) obj <= 34359738367L) {
				return 5;
			}
		}
		return 0;
	}
	
	public int readBit() {
		int i = 0, j = 0;
		while ((_decrypt[_off] & 0xFF) >= 128) {
			i |= (_decrypt[(_off++)] & 0xFF ^ 0x80) << 7 * j++;
		}
		return i |= (_decrypt[(_off++)] & 0xFF) << 7 * j;
	}
	
	public long readLong() {
		int size = read_size();
		if (size == 0) {
			return 0;
		}
		long i = _decrypt[_off++] & 0x7f;
		if (size == 1) {
			return i;
		}
		if (size >= 2) {
			i |= (_decrypt[_off++] << 8 & 0x7f00) >> 1;
		}
		if (size >= 3) {
			i |= (_decrypt[_off++] << 16 & 0x7f0000) >> 2;
		}
		if (size >= 4) {
			i |= (_decrypt[_off++] << 24 & 0x7f000000) >> 3;
		}
		if (size >= 5) {
			i |= ((long) _decrypt[_off++] << 32 & 0x7f00000000L) >> 4;
		}
		return i;
	}
	
	public int readD() {
		int i = 0;
		try {
			i = _decrypt[_off++] & 0xff;
			i |= _decrypt[_off++] << 8 & 0xff00;
			i |= _decrypt[_off++] << 16 & 0xff0000;
			i |= _decrypt[_off++] << 24 & 0xff000000;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	
	public int readC() {
		int i = _decrypt[_off++] & 0xff;
		return i;
	}
	
	public void readP(int a) {
		_off += a;
	}
	
	public boolean readBool() {
		return readC() == 1;
	}
	
	public int readKH() {
		int i = (_decrypt[_off++] & 0xff) + ~0x0000007F;
		i |= (_decrypt[_off++] & 0xff) << 7;
		return i;
	}
	
	public int readKCH() {
		int i = (_decrypt[_off++] & 0xff) + ~0x0000007F;
		i |= ((_decrypt[_off++] & 0xff) + ~0x0000007F) << 7;
		i |= (_decrypt[_off++] & 0xff) << 14;
		return i;
	}
	
	public int readK() {
		int i = (_decrypt[_off++] & 0xff) + ~0x0000007F;
		i |= ((_decrypt[_off++] & 0xff) + ~0x0000007F) << 7;
		i |= ((_decrypt[_off++] & 0xff) + ~0x0000007F) << 14;
		i |= (_decrypt[_off++] & 0xff) << 21;
		return i;
	}
	
	public int readK(int len) {
		int i = 0;
		switch (len) {
		case 1:
			i = (_decrypt[(_off++)] & 0xFF) + ~0x0000007F;
			break;
		case 2:
			i = (_decrypt[(_off++)] & 0xFF) + ~0x0000007F;
			i |= (_decrypt[(_off++)] & 0xFF) << 7;
			break;
		case 3:
			i = (_decrypt[(_off++)] & 0xFF) + ~0x0000007F;
			i |= (_decrypt[(_off++)] & 0xFF) + ~0x0000007F << 7;
			i |= (_decrypt[(_off++)] & 0xFF) << 14;
			break;
		case 4:
			i = (_decrypt[(_off++)] & 0xFF) + ~0x0000007F;
			i |= (_decrypt[(_off++)] & 0xFF) + ~0x0000007F << 7;
			i |= (_decrypt[(_off++)] & 0xFF) + ~0x0000007F << 14;
			i |= (_decrypt[(_off++)] & 0xFF) << 21;
			break;
		case 5:
			i = (_decrypt[(_off++)] & 0xFF) + ~0x0000007F;
			i |= (_decrypt[(_off++)] & 0xFF) + ~0x0000007F << 7;
			i |= (_decrypt[(_off++)] & 0xFF) + ~0x0000007F << 14;
			i |= (_decrypt[(_off++)] & 0x7F) << 21;
			i |= (_decrypt[(_off++)] & 0xFF) << 28;
			break;
		case 6:
			i = (_decrypt[(_off++)] & 0xFF) + ~0x0000007F;
			i |= (_decrypt[(_off++)] & 0xFF) + ~0x0000007F << 7;
			i |= (_decrypt[(_off++)] & 0xFF) + ~0x0000007F << 14;
			i |= (_decrypt[(_off++)] & 0x7F) << 21;
			i |= (_decrypt[(_off++)] & 0x7F) << 28;
			i |= (_decrypt[(_off++)] & 0xFF) << 35;
		}
		return i;
	}

	public int read4(int size) {
		if (size == 0) {
			return 0;
		}
		int i = _decrypt[_off++] & 0x7f;
		if (size == 1) {
			return i;
		}
		if (size >= 2) {
			i |= (_decrypt[_off++] << 8 & 0x7f00) >> 1;
		}
		if (size >= 3) {
			i |= (_decrypt[_off++] << 16 & 0x7f0000) >> 2;
		}
		if (size >= 4) {
			i |= (_decrypt[_off++] << 24 & 0x7f000000) >> 3;
		}
		if (size >= 5) {
			i |= ((long) _decrypt[_off++] << 32 & 0x7f00000000L) >> 4;
		}
		return i;
	}
	
	public int read_size() {
		int i = 0;
		while(true){
			if ((_decrypt[_off + i] & 0xff) < 0x80) {
				break;
			} else {
				i++;
			}
		}
		return i + 1;
	}

	public int readH() {
		int i = _decrypt[_off++] & 0xff;
		i |= _decrypt[_off++] << 8 & 0xff00;
		return i;
	}

	public int readCH() {
		int i = _decrypt[_off++] & 0xff;
		i |= _decrypt[_off++] << 8 & 0xff00;
		i |= _decrypt[_off++] << 16 & 0xff0000;
		return i;
	}

	public double readF() {
		long l = _decrypt[_off++] & 0xff;
		l |= _decrypt[_off++] << 8 & 0xff00;
		l |= _decrypt[_off++] << 16 & 0xff0000;
		l |= _decrypt[_off++] << 24 & 0xff000000;
		l |= (long) _decrypt[_off++] << 32 & 0xff00000000L;
		l |= (long) _decrypt[_off++] << 40 & 0xff0000000000L;
		l |= (long) _decrypt[_off++] << 48 & 0xff000000000000L;
		l |= (long) _decrypt[_off++] << 56 & 0xff00000000000000L;
		return Double.longBitsToDouble(l);
	}
	
	public String readString() {
		int length = readC();
		String s = null;
		try {
			s = new String(_decrypt, _off, length, CharsetUtil.EUC_KR_STR);//EUC-KR
			s = s.substring(0, s.indexOf('\0'));
			_off += s.getBytes(CharsetUtil.EUC_KR_STR).length + 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public String readRS() {
		String s = null;
		try {
			s = new String(_decrypt, _off, _decrypt.length - _off, CharsetUtil.EUC_KR_STR);// EUC-KR
			s = s.substring(0, s.indexOf('\t'));
			_off += s.getBytes(CharsetUtil.EUC_KR_STR).length + 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public String readS() {
		String s = null;
		try {
			s = new String(_decrypt, _off, _decrypt.length - _off, CharsetUtil.EUC_KR_STR);//EUC-KR
			s = s.substring(0, s.indexOf('\0'));
			_off += s.getBytes(CharsetUtil.EUC_KR_STR).length + 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public String readS(int len) {
		String s = null;
		try {
			s = new String(_decrypt, _off, len, CharsetUtil.EUC_KR_STR);
			_off += s.getBytes(CharsetUtil.EUC_KR_STR).length;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public String readS2(int length) {
		String s = null;
		try {
			s = new String(_decrypt, _off, length, CharsetUtil.EUC_KR_STR);
			s = s.substring(0, s.indexOf('\0'));
			_off += s.getBytes(CharsetUtil.EUC_KR_STR).length + 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public String readSAccount() {
		String s = null;
		try {
			s = new String(_decrypt, _off, _decrypt.length - _off, CharsetUtil.EUC_KR_STR);
			String target = "?";
			int target_num = s.indexOf(target)+1;
			s = s.substring(target_num, (s.substring(target_num).indexOf("*")+target_num));
			_off += s.getBytes(CharsetUtil.EUC_KR_STR).length + 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public String readSPassword() {
		String s = null;
		try {
			s = new String(_decrypt, _off, _decrypt.length - _off, CharsetUtil.EUC_KR_STR);
			String target = "";
			int target_num = s.indexOf(target)+1;
			s = s.substring(target_num, (s.substring(target_num).indexOf("*")+target_num));
			_off += s.getBytes(CharsetUtil.EUC_KR_STR).length + 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
    
	@SuppressWarnings("finally")
	public String readSS(){ 
		String text = null; 
		int loc = 0; 
		int start = 0; 
		try{ 
			start = _off; 
			while (readH() != 0) {
				loc += 2; 
			}
			StringBuffer test = new StringBuffer(); 
			do { 
				if ((_decrypt[start]&0xff)>=127 || (_decrypt[start+1]&0xff)>=127) { 
				/** 한글 **/ 
					byte[] t = new byte[2]; 
					t[0] = _decrypt[start+1]; 
					t[1] = _decrypt[start]; 
					test.append(new String(t, 0, 2, CharsetUtil.EUC_KR_STR)); 
				} else {
					test.append(new String(_decrypt, start, 1, CharsetUtil.EUC_KR_STR));/** 영문&숫자 **/ 
				}
				start+=2; 
				loc-=2; 
			} while (0 < loc);
			text = test.toString(); 
		} catch (Exception e) { 
			text = null; 
		} finally { 
			return text; 
		} 
	}
	
    public String readS2() {
        String s = null;
        try {
            int size = this._decrypt[this._off++] & 0xFF;
            s = new String(this._decrypt, this._off, size, CharsetUtil.EUC_KR_STR);
            this._off += size;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return s;
    }
    
    public String readS22() {
        String s = null;
        try {
            int size = this._decrypt[this._off++] & 0xFF;
            s = new String(this._decrypt, this._off, size, CharsetUtil.MS_949_STR);
            this._off += size;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return s;
    }
    
    public byte[] readByte(int len) {
		byte[] result = new byte[len];
		try {
			System.arraycopy(_decrypt, _off, result, 0, len);
			_off += len;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
    
	public byte[] readByte() {
		byte[] result = new byte[_decrypt.length - _off];
		try {
			System.arraycopy(_decrypt, _off, result, 0, _decrypt.length - _off);
			_off = _decrypt.length;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public byte[] readSByte() {
		byte[] result = new byte[_decrypt.length - _off];
		try {
			System.arraycopy(_decrypt, _off, result, 0, _decrypt.length - _off);
			int i = 1;
			for(; i < result.length + 1; i++){
				if(result[i - 1] == 0x00){
					result = new byte[i];
					System.arraycopy(_decrypt, _off, result, 0, result.length);
					break;
				}
			}
			_off += i;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int readLength() {
		byte[] BYTE = new byte[_decrypt.length - _off];
		return BYTE.length;
	}

	/**
	 * 다음 읽을 데이타가 존재하는지 확인해주는 함수.
	 * 
	 * @return
	 */
	public boolean isRead(int size) {
		return _off + size <= _decrypt.length;
	}

	/**
	 * 클라이언트 패킷의 종류를 나타내는 캐릭터 라인을 돌려준다. ("[C] C_DropItem" 등 )
	 */
	public String getType() {
		return "[C] " + this.getClass().getSimpleName();
	}
}

