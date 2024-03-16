package l1j.server.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

public class BinaryOutputStream extends OutputStream {
	private final ByteArrayOutputStream _bao;

	public BinaryOutputStream() {
		_bao = new ByteArrayOutputStream();
	}
	
	public void reset() {
		_bao.reset();
	}

	@Override
	public void write(int b) throws IOException {
		_bao.write(b);
	}
	
	public void write(byte[] b, int off, int len) {
		_bao.write(b, off, len);
	}

	public void writeD(int value) {
		_bao.write(value & 0xFF);
		_bao.write(value >> 8 & 0xFF);
		_bao.write(value >> 16 & 0xFF);
		_bao.write(value >> 24 & 0xFF);
	}

	public void writeH(int value) {
		_bao.write(value & 0xFF);
		_bao.write(value >> 8 & 0xFF);
	}

	public void writeC(int value) {
		_bao.write(value & 0xFF);
	}

	public void writeP(int value) {
		_bao.write(value);
	}

	public void writeL(long value) {
		_bao.write((int) (value & 0xFF));
	}

	public void writeDouble(double org) {
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
	
	public void writeFloat(float org) {
		int value = Float.floatToRawIntBits(org);
		_bao.write((int) (value & 0xFF));
		_bao.write((int) (value >> 8 & 0xFF));
		_bao.write((int) (value >> 16 & 0xFF));
		_bao.write((int) (value >> 24 & 0xFF));
	}

	public void writeS2(String text) {
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
		} catch (Exception e) {}
	}
	
	public void writeS(String text) {
		try {
			if (text != null) {
				_bao.write(text.getBytes(CharsetUtil.EUC_KR_STR));
			}
		} catch (Exception e) {}
		_bao.write(0);
	}
	
	public void writeBit(long value) {
		if (value < 0L) {
			String str = Integer.toBinaryString((int) value);
			value = Long.valueOf(str, 2).longValue();
		}
		int i = 0;
		while(value >> 7 * (i + 1) > 0L) {
			this._bao.write((int) ((value >> 7 * i++) & 0x0000007F | 0x80));
		}
		this._bao.write((int) ((value >> 7 * i) & 0x0000007F));
	}
	
	public void writeBit(int value1, int value2) {
		String str1 = Integer.toBinaryString(value1);
		String str2 = Integer.toBinaryString(value2);
		if (value1 <= 32767) {
			str1 = StringUtil.ZeroString + str1;
		}
		String str3 = str2 + str1;
		writeBit(Long.valueOf(str3, 2).longValue());
	}
	
	public void writeLongLocationReverse(int x, int y) {
		writeBit((y << 16) & 0xFFFF0000 | (x & 0x0000FFFF));
	}
	
	public void write4bit(int value){
		if (value <= 127) {
	    	this._bao.write(value & 0x7F);
	    } else if (value <= 16383) {
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
	
	public void write7B(long value) {
		int i = 0;
		BigInteger b = new BigInteger("18446744073709551615");
		while (BigInteger.valueOf(value).and(b).shiftRight((i + 1) * 7).longValue() > 0) {
			_bao.write(BigInteger.valueOf(value).and(b).shiftRight(7 * i++).remainder(BigInteger.valueOf(0x80)).or(BigInteger.valueOf(0x80)).intValue());
		}
		_bao.write(BigInteger.valueOf(value).and(b).shiftRight(7 * i++).remainder(BigInteger.valueOf(0x80)).intValue());
	}
	
	public void writeK(int value) {
		int valueK = (int) (value >> 0x00000007);
		if (valueK == 0) {
			_bao.write(value);
		} else if (valueK <= 127) {
			_bao.write((value & 0x7f) + 128);
			_bao.write(valueK);
		} else if (valueK <= 255) {
			_bao.write((value & 0x7f) + 128);
			_bao.write((valueK & 0x7f) + 128);
			_bao.write(valueK >> 0x00000007);
		} else {
			_bao.write((value & 0x7f) + 128);
			_bao.write((valueK & 0x7f) + 128);
			_bao.write(((valueK >> 0x00000007) & 0x7f) + 128);
			_bao.write(valueK >> 0x0000000E);
		}
	}
	
	public int writeLenght(int obj) {
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

	public void writeInt32NoTag(final int value) throws IOException {
		if (value >= 0) {
			writeRawVarInt32(value);
		} else {
	    	writeRawVarInt64(value);
	    }
	}
	
	private void writeRawVarInt32(int value) throws IOException{
		while(true){
			if ((value & ~0X7f) == 0) {
				writeRawByte(value);
				return;
			}
			writeRawByte((value & 0x7F) | 0x80);
			value >>>= 7;
		}
	}
	
	private void writeRawVarInt64(long value) throws IOException{
		while(true){
			if ((value & ~0x7F) == 0) {
				writeRawByte((int) value);
				return;
			}
			writeRawByte(((int) value & 0x7F) | 0x80);
			value >>>= 7;
		}
	}
	
	private void writeRawByte(final int value) throws IOException{
		writeRawByte((byte)value);
	}
	private void writeRawByte(final byte value) throws IOException{
		_bao.write(value);
	}
	  
	public void writeByte(byte[] text) {
		try {
			if (text != null) {
				_bao.write(text);
			}
		} catch (Exception e) {
		}
	}
	
	public void clear() {
	    try {
	    	this._bao.reset();
	    	this._bao.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}

	public int getLength() {
		return _bao.size() + 2;
	}
	
	public int getSize() {
	    return this._bao.size();
	}

	public byte[] getBytes() {
		return _bao.toByteArray();
	}
	
	public void byteWrite(long value) {
		long temp = value >> 0x00000007;
		if (temp > 0) {
			writeC(HexHelper.HEX_TABLE[(int) value & 0x0000007F]);
			while (temp >= 128) {
				writeC(HexHelper.HEX_TABLE[(int) temp & 0x0000007F]);
				temp = temp >> 0x00000007;
			}
			if (temp > 0) {
				writeC((int) temp);
			}
		} else {
			if (value == 0) {
				writeC(0);
			} else {
				writeC(HexHelper.HEX_TABLE[(int) value]);
				writeC(0);
			}
		}
	}
	
	public void writeStringWithLength(String s) {
		try {
			if (StringUtil.isNullOrEmpty(s)) {
				writeC(0);
			} else {
				byte[] data = s.getBytes(CharsetUtil.EUC_KR_STR);
				writeBytesWithLength(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeBytesWithLength(byte[] bytes) {
	    if ((bytes == null) || (bytes.length <= 0)) {
	    	writeC(0);
	    } else {
	    	if (bytes.length > 127) {
	    		writeBit(bytes.length);
	    	} else {
	    		writeC(bytes.length);
	    	}
	        writeByte(bytes);
	    }
	}
	
	public int getBitSize(long value) {
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
	
	public void writeB(boolean b){
		writeC(b ? 0x1 : 0x0);
	}
	
	public void writeB(Object o){
		writeC(o != null ? 0x1 : 0x0);
	}
	
}

