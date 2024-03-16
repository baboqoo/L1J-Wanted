package l1j.server.server.serverpackets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.HexHelper;
import l1j.server.server.utils.StringUtil;

public abstract class ServerBasePacket {
	private int OpKey; // opcode Key
	private boolean isKey = true;
	private static Logger _log = Logger.getLogger(ServerBasePacket.class.getName());

	protected ByteArrayOutputStream _bao = new ByteArrayOutputStream();

	protected ServerBasePacket() {}

	public void clear() {
		try {
			_bao.reset();
			_bao.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		_bao = null;
	}
	
	protected void write(byte[] b, int off, int len) {
		_bao.write(b, off, len);
	}

	protected void writeD(int value) {
		_bao.write(value & 0xFF);
		_bao.write(value >> 8 & 0xFF);
		_bao.write(value >> 16 & 0xFF);
		_bao.write(value >> 24 & 0xFF);
	}
	
	protected void writeD(long value) {
		_bao.write((int)(value & 0xff));
		_bao.write((int)(value >> 8 & 0xff));
		_bao.write((int)(value >> 16 & 0xff));
		_bao.write((int)(value >> 24 & 0xff));
	}

	protected void writeH(int value) {
		_bao.write(value & 0xFF);
		_bao.write(value >> 8 & 0xFF);
	}

	protected void writeC(int value) {
		_bao.write(value & 0xFF);
		// 옵코드 wirteC
		if (isKey) {
			OpKey = value;
			isKey = false;
		}
	}
	
	protected void writeRaw(int value) {
		_bao.write(value & 0xFF);
	}

	protected int writeLenght(long value) {
		if (value < 0L) {
			String stringValue = Integer.toBinaryString((int) value);
			value = Long.valueOf(stringValue, 2).longValue();
		}
		if (value <= 127L) {
			return 1;
		}
		if (value <= 16383L) {
			return 2;
		}
		if (value <= 2097151L) {
			return 3;
		}
		if (value <= 268435455L) {
			return 4;
		}
		if (value <= 34359738367L) {
			return 5;
		}
		return 0;
	}
	
	protected void writeSU16(String text) {
		try {
			if (text != null) {
				_bao.write(text.getBytes(CharsetUtil.UTF_16LE_STR));
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		_bao.write(0);
		_bao.write(0);
	}

	
	protected void writeK(int value) {
		int valueK = (int) (value >> 0x00000007);
		if (valueK == 0) {
			_bao.write(value);
		} else if (valueK <= 127) {
			_bao.write((value & 0x7f) + 128);
			_bao.write(valueK);
		} else if (valueK <= 16383) {
			_bao.write((value & 0x7f) + 128);
			_bao.write((valueK & 0x7f) + 128);
			_bao.write(valueK >> 0x00000007);
		} else if (valueK <= 2097151) {
			_bao.write((value & 0x7f) + 128);
			_bao.write((valueK & 0x7f) + 128);
			_bao.write(((valueK >> 0x00000007) & 0x7f) + 128);
			_bao.write(valueK >> 0x0000000E);
		} else {
			_bao.write((value & 0x7f) + 128);
			_bao.write((valueK & 0x7f) + 128);
			_bao.write(((valueK >> 0x00000007) & 0x7f) + 128);
			_bao.write(((valueK >> 0x0000000E) & 0x7f) + 128);
			_bao.write(valueK >> 0x00000015);
		}
	}
	
	public byte[] getBytes1() {
		return _bao.toByteArray();
	}

	protected int bitLengh(int obj) {
		int length = 0;
		if (obj < 0) {
			BigInteger b = new BigInteger("18446744073709551615");
			while(BigInteger.valueOf(obj).and(b).shiftRight((length + 1) * 7).longValue() > 0){
				length++;
			}
			length++;
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
		return length;
	}
	
	public void writeInt32NoTag(final int value) {
		if (value >= 0) {
			writeRawVarInt32(value);
		} else {
			writeRawVarInt64(value);
		}
	}
	
	private void writeRawVarInt32(int value) {
		while(true){
			if ((value & ~0X7f) == 0) {
				writeRawByte(value);
				return;
			}
			writeRawByte((value & 0x7F) | 0x80);
			value >>>= 7;
		}
	}
	
	private void writeRawVarInt64(long value) {
		while(true){
			if ((value & ~0x7F) == 0) {
				writeRawByte((int) value);
				return;
			}
			writeRawByte(((int) value & 0x7F) | 0x80);
			value >>>= 7;
		}
	}
	
	private void writeRawByte(final int value) {
		writeRawByte((byte)value);
	}
	
	private void writeRawByte(final byte value) {
		_bao.write(value);
	}
	
	/* 한국 옵코드 추가 패킷 */
	protected void write4bit(int value){
	    if (value <= 127) {
	      this._bao.write(value & 0x7F);
	    } else if (value <= 16383){
	      this._bao.write(value & 0x7F | 0x80);
	      this._bao.write(value >> 7 & 0x7F);
	    } else if (value <= 2097151) {
	      this._bao.write(value & 0x7F | 0x80);
	      this._bao.write(value >> 7 & 0x7F | 0x80);
	      this._bao.write(value >> 14 & 0x7F);
	    } else if (value <= 268435455) {
	      this._bao.write(value & 0x7F | 0x80);
	      this._bao.write(value >> 7 & 0x7F | 0x80);
	      this._bao.write(value >> 14 & 0x7F | 0x80);
	      this._bao.write(value >> 21 & 0x7F);
	    } else if (value <= 34359738367L) {
	      this._bao.write(value & 0x7F | 0x80);
	      this._bao.write(value >> 7 & 0x7F | 0x80);
	      this._bao.write(value >> 14 & 0x7F | 0x80);
	      this._bao.write(value >> 21 & 0x7F | 0x80);
	      this._bao.write(value >> 28 & 0x7F);
	    }
	}
	
	protected void writeBit(long value) {
		if (value < 0L) {
			String stringValue = Integer.toBinaryString((int) value);
			value = Long.valueOf(stringValue, 2).longValue();
		}
		int i = 0;
		while (value >> 7 * (i + 1) > 0L) {
			_bao.write((int) ((value >> 7 * i++) & 0x0000007F | 0x80));
		}
		_bao.write((int) ((value >> 7 * i) & 0x0000007F));
	}
	
	protected void writeBit(int value1, int value2) {
		String str1=Integer.toBinaryString(value1);
		String str2=Integer.toBinaryString(value2);
		if (value1 <= 32767) {
			str1=StringUtil.ZeroString+str1;
		}
		String str3=str2+str1;
		writeBit(Long.valueOf(str3, 2).longValue());
	}
	
	protected void writeLongLocationReverse(int x, int y) {
		writeBit((y << 16) & 0xFFFF0000 | (x & 0x0000FFFF));
	}

	protected int getBitSize(long value) {
		if (value < 0L) {
			String stringValue = Integer.toBinaryString((int) value);
			value = Long.valueOf(stringValue, 2).longValue();
		}
		int size = 0;
		while (value >> (size + 1) * 7 > 0L) {
			size++;
		}
		size++;
		return size;
	}

	protected void write7B(long value) {
		int i = 0;
		BigInteger b = new BigInteger("18446744073709551615");
		while (BigInteger.valueOf(value).and(b).shiftRight((i + 1) * 7).longValue() > 0) {
			_bao.write(BigInteger.valueOf(value).and(b).shiftRight(7 * i++).remainder(BigInteger.valueOf(0x80)).or(BigInteger.valueOf(0x80)).intValue());
		}
		_bao.write(BigInteger.valueOf(value).and(b).shiftRight(7 * i++).remainder(BigInteger.valueOf(0x80)).intValue());
	}

	protected int size7B(int obj) {
		int length = 0;
		if (obj < 0) {
			BigInteger b = new BigInteger("18446744073709551615");
			while (BigInteger.valueOf(obj).and(b).shiftRight((length + 1) * 7).longValue() > 0) {
				length++;
			}
			length++;
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
		return length;
	}
	
	protected void writeP(int value) {
		_bao.write(value);
	}

	protected void writeDouble(double org) {
		long value = Double.doubleToRawLongBits(org);
		_bao.write((int) (value & 0xFF));
		_bao.write((int) (value >> 8 & 0xFF));
		_bao.write((int) (value >> 16 & 0xFF));
		_bao.write((int) (value >> 24 & 0xFF));
		_bao.write((int) (value >> 32 & 0xFF));
		_bao.write((int) (value >> 40 & 0xFF));
		_bao.write((int) (value >> 48 & 0xFF));
		_bao.write((int) (value >> 56 & 0xFF));
	}
	
	protected void writeFloat(float org) {
		int value = Float.floatToRawIntBits(org);
		_bao.write((int) (value & 0xFF));
		_bao.write((int) (value >> 8 & 0xFF));
		_bao.write((int) (value >> 16 & 0xFF));
		_bao.write((int) (value >> 24 & 0xFF));
	}

	protected void writeS(String text) {
		try {
			if (text != null) {
				_bao.write(text.getBytes(CharsetUtil.EUC_KR_STR));
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		_bao.write(0);
	}

	protected void writeSS(String text) {
		try {
			if (text != null) {
				byte[] test = text.getBytes(CharsetUtil.EUC_KR_STR);
				for (int i = 0; i < test.length;) {
					if ((test[i] & 0xff) >= 0x7F) {
						/** 한글 **/
						_bao.write(test[i + 1]);
						_bao.write(test[i]);
						i += 2;
					} else {
						/** 영문&숫자 **/
						_bao.write(test[i]);
						_bao.write(0);
						i += 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		_bao.write(0);
		_bao.write(0);
	}

	protected void writeByte(byte[] array) {
		try {
			if (array != null) {
				_bao.write(array);
			}
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	protected int getLength() {
		return _bao.size() + 2;
	}
	
	protected byte[] getBytes() {
		return _bao.toByteArray();
	}
	
	protected void writeS2(String text) {
		try {
			if (text != null && !text.isEmpty()) {
				byte[] name = text.getBytes(CharsetUtil.EUC_KR_STR);
				_bao.write(name.length & 0xFF);
				if (name.length > 0) {
					_bao.write(name);
				}
			} else {
				_bao.write(0 & 0xFF);
			}
		} catch (Exception e) {
			//_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	protected void writeStringWithLength(String s) {
	    try {
	        if (StringUtil.isNullOrEmpty(s)) {
	        	writeRaw(0);
	        } else {
	            byte[] data = s.getBytes(CharsetUtil.EUC_KR_STR);
	            writeBytesWithLength(data);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	protected void writeBytesWithLength(byte[] bytes) {
	    if (bytes == null || bytes.length <= 0) {
	    	writeRaw(0);
	    } else {
	    	if (bytes.length > 127) {
	    		writeBit(bytes.length);
	    	} else {
	    		writeRaw(bytes.length);
	    	}
	        writeByte(bytes);
	    }
	}
	
	protected void writeZero(int loop) {
	    for (int a = 0; a < loop; a++) {
	    	writeRaw(0); 
	    }
	}
	
	protected void writeS_UTF16(String s) {
	    try {
	        if (s != null) {
	            byte[] array = s.getBytes(CharsetUtil.UTF_16LE_STR);
	            writeH(array.length + 2);
	            _bao.write(s.getBytes(CharsetUtil.UTF_16LE_STR));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    writeH(0);
	}
	
	protected void byteWrite(long value) {
		long temp = value >> 0x00000007;
		if (temp > 0) {
			writeRaw(HexHelper.HEX_TABLE[(int) value & 0x0000007F]);
			while(temp >= 128){
				writeRaw(HexHelper.HEX_TABLE[(int) temp & 0x0000007F]);
				temp = temp >> 0x00000007;
			}
			if (temp > 0) {
				writeRaw((int) temp);
			}
		} else {
			if (value == 0) {
				writeRaw(0);
			} else {
				writeRaw(HexHelper.HEX_TABLE[(int) value]);
				writeRaw(0);
			}
		}
	}
	
	protected int byteWriteCount(long value) {
		long temp = value >> 0x00000007;
		int count = 0;
		if (temp > 0) {
			count++;
			while(temp >= 128){
				count++;
				temp = temp >> 0x00000007;
			}
			if (temp > 0) {
				count++;
			}
		} else {
			if (value == 0) {
				count++;
			} else {
				count += 2;
			}
		}
		return count;
	}
	
	protected void writeB(boolean b){
		writeRaw(b ? 0x1 : 0x0);
	}
	
	protected void writeB(Object o){
		writeRaw(o != null ? 0x1 : 0x0);
	}
	
	/**
     * 길이 만큼 큐에서 꺼내 쓴다.
     * @param queue
     * @param length
     */
    protected void writeFromQueue(ArrayBlockingQueue<Integer> queue, int length){
    	writeRaw(length);
    	if (length > 127) {
    		writeRaw(queue.poll());
    	}
    	for (int i=0; i<length; i++) {
    		writeRaw(queue.poll());
    	}
    }

	public abstract byte[] getContent() throws IOException;

	/**
	 * 서버 패킷의 종류를 나타내는 캐릭터 라인을 돌려준다. ("[S] S_WhoAmount" 등 )
	 */
	public String getType() {
		return StringUtil.EmptyString;
	}

	@Override
	public String toString() {
		// getType() 의 리턴이 "" 이라면 빈값 아니면 패킷이름 + 코드값 출력
		// [옵코드] 패킷명
		return getType().equals(StringUtil.EmptyString) ? StringUtil.EmptyString : String.format("[%d] %s", OpKey, getType());
	}
	
	public void close() {
		try {
			_bao.close();
		} catch (Exception e) {
		}
	}
	  
}

