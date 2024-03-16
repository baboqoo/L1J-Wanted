package l1j.server.common.bin;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class ProtoInputStream {
	public static ProtoInputStream newInstance(final byte[] buf) {
		return newInstance(buf, 0);
	}

	public static ProtoInputStream newInstance(final byte[] buf, int off) {
		return newInstance(buf, off, buf.length);
	}
	
	public static ProtoInputStream newInstance(final byte[] buf, int off, int limit){
		return new ProtoInputStream(buf, off, limit);
	}
	
	public static ProtoInputStream newInstance(String path){
		FileInputStream 	fs = null;
		BufferedInputStream is = null;
		ProtoInputStream 	ps = null;
		try {
			fs 			= new FileInputStream(path);
			is 			= new BufferedInputStream(fs);
			byte[] buff = new byte[(int)fs.getChannel().size()];
			is.read(buff, 0, buff.length);
			ps = newInstance(buff, 0);
			/*if (path.equalsIgnoreCase("./data/Contents/charged_time_map-common.bin")) {
				l1j.server.server.utils.FileUtil.createHexPacketFile(buff, "CHARGED.txt");
			}*/
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (fs != null) {
				try {
					fs.close();
					fs = null;
				} catch(Exception e) {}
			}
			
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch(Exception e) {}
			}
		}
		return ps;
	}
	
	private final byte[] buff;
	private int offset;
	private int limit;
	
	private ProtoInputStream(final byte[] buf, int off, int lim) {
		buff	= buf;
		offset	= off;
		limit	= lim;
	}

	public int getCurrentOffset(){
		return offset;
	}
	
	public int getCurrentLimit(){
		return limit;
	}
	
	public boolean isAtEnd() {
		return offset >= limit;
	}
	
	public int readTag() throws IOException {
		if (buff == null || isAtEnd())
			return 0;
		return readRawVarInt32();
	}

	public byte readRawByte() throws IOException {
		if (isAtEnd())
			return 0;
		return buff[offset++];
	}

	public byte[] readRawBytes(final int size) throws IOException {
		if(size <= 0)
			return null;
		
		if (isAtEnd())
			throw new IOException(createExceptionMessage(String.format("readRawBytes() -> Insufficient size to read. size : %d offset : %d", size, offset)));

		if (offset + size <= limit) {
			final byte[] bytes = new byte[size];
			System.arraycopy(buff, offset, bytes, 0, size);
			offset += size;
			return bytes;
		}

		return readRawBytes(limit - offset);
	}

	public int readRawVarInt32() throws IOException {
		byte tmp = readRawByte();
		if (tmp >= 0) {
			return tmp;
		}
		int result = tmp & 0x7f;
		if ((tmp = readRawByte()) >= 0) {
			result |= tmp << 7;
		} else {
			result |= (tmp & 0x7f) << 7;
			if ((tmp = readRawByte()) >= 0) {
				result |= tmp << 14;
			} else {
				result |= (tmp & 0x7f) << 14;
				if ((tmp = readRawByte()) >= 0) {
					result |= tmp << 21;
				} else {
					result |= (tmp & 0x7f) << 21;
					result |= (tmp = readRawByte()) << 28;
					if (tmp < 0) {
						for (int i = 0; i < 5; i++) {
							if (readRawByte() >= 0)
								return result;
						}
						throw new IOException(createExceptionMessage("readRawVarInt32() -> Int32 value overflow."));
					}
				}
			}
		}
		return result;
	}

	public long readRawVarInt64() throws IOException {
		int shift = 0;
		long result = 0;
		while (shift < 64) {
			final byte b = readRawByte();
			result |= (long) (b & 0x7F) << shift;
			if ((b & 0x80) == 0)
				return result;

			shift += 7;
		}
		throw new IOException(createExceptionMessage("readRawVarInt64() -> Int64 value overflow."));
	}

	public int readRawLittleEndian32() throws IOException{
		final byte b1 = readRawByte();
	    final byte b2 = readRawByte();
	    final byte b3 = readRawByte();
	    final byte b4 = readRawByte();
	    return (((int)b1 & 0xff)      ) |
	    		(((int)b2 & 0xff) <<  8) |
	    		(((int)b3 & 0xff) << 16) |
	    		(((int)b4 & 0xff) << 24);
	}
	
	public long readRawLittleEndian64() throws IOException {
		final byte b1 = readRawByte();
	    final byte b2 = readRawByte();
	    final byte b3 = readRawByte();
	    final byte b4 = readRawByte();
	    final byte b5 = readRawByte();
	    final byte b6 = readRawByte();
	    final byte b7 = readRawByte();
	    final byte b8 = readRawByte();
	    return (((long)b1 & 0xff)      ) |
	    	(((long)b2 & 0xff) <<  8) |
	        (((long)b3 & 0xff) << 16) |
	    	(((long)b4 & 0xff) << 24) |
	        (((long)b5 & 0xff) << 32) |
	        (((long)b6 & 0xff) << 40) |
	        (((long)b7 & 0xff) << 48) |
	        (((long)b8 & 0xff) << 56);
	}
	
	public double readDouble() throws IOException{
		return Double.longBitsToDouble(readRawLittleEndian64());
	}
	
	public float readFloat() throws IOException{
		return Float.intBitsToFloat(readRawLittleEndian32());
	}
	
	public long readUInt64() throws IOException{
		return readRawVarInt64();
	}
	
	public long readInt64() throws IOException{
		return readRawVarInt64();
	}
	
	public int readUInt32() throws IOException{
		return readRawVarInt32();
	}
	
	public int readInt32() throws IOException{
		return readRawVarInt32();
	}
	
	public long readFixed64() throws IOException{
		return readRawLittleEndian64();
	}
	
	public int readFixed32() throws IOException{
		return readRawLittleEndian32();
	}
	
	public boolean readBool() throws IOException{
		return readRawVarInt32() != 0;
	}
	
	public String readString(Charset charset) throws UnsupportedEncodingException, IOException{
		final int size = readRawVarInt32();
		if (size <= 0)
			return null;
		
		if (size <= (buff.length - offset)) {
			final String result = new String(buff, offset, size, charset);
			offset += size;
			return result;
		}
		return new String(readRawBytes(size), charset);
	}
	
	public String readString() throws UnsupportedEncodingException, IOException{
		final int size = readRawVarInt32();
		if (size <= 0)
			return null;
		
		if (size <= (buff.length - offset)) {
			final String result = new String(buff, offset, size, "MS949");
			offset += size;
			return result;
		}
		return new String(readRawBytes(size), "MS949");
	}
	
	public int readEnum() throws IOException{
		return readRawVarInt32();
	}
	
	public int readSFixed32() throws IOException{
		return readRawLittleEndian32();
	}
	
	public long readSFixed64() throws IOException{
		return readRawLittleEndian64();
	}
	
	public int readSInt32() throws IOException{
		return decodeZigZag32(readRawVarInt32());
	}
	
	public long readSInt64() throws IOException{
		return decodeZigZag64(readRawVarInt64());
	}
	
	public byte[] readBytes() throws IOException{
		return readRawBytes(readRawVarInt32());
	}
	
	public ProtoMessage readMessage(ProtoMessage message) throws IOException{
		byte[] bytes = readBytes();
		return bytes == null ? null : message.readFrom(newInstance(bytes));
	}
	
	private String createExceptionMessage(String sourceMessage) {
		return String.format("%s [occurred buff in position : %d]", sourceMessage, offset);
	}
	
	public static int decodeZigZag32(final int n) {
		return (n >>> 1) ^ -(n & 1);
	}

	public static long decodeZigZag64(final long n) {
		return (n >>> 1) ^ -(n & 1);
	}
}

