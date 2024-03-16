package l1j.server.common.bin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import l1j.server.server.Opcodes;

public class ProtoOutputStream {
	public static final int DEFAULT_BUFFER_SIZE = 4096;
	public static final int LITTLE_ENDIAN_32_SIZE = 4;
	public static final int LITTLE_ENDIAN_64_SIZE = 8;
	
	public static ProtoOutputStream newInstance(){
		return newInstance(DEFAULT_BUFFER_SIZE);
	}
	
	public static ProtoOutputStream newInstance(ProtoMessage imessage, int proto_code) {
		ProtoOutputStream stream = newInstance(imessage.getSerializedSize() + 5, proto_code);
		try {
			imessage.writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	
	public static ProtoOutputStream newLocalInstance(int capacity){
		byte[] b = new byte[capacity];
		return new ProtoOutputStream(b, 0, capacity);
	}
	
	public static ProtoOutputStream newInstance(int capacity){
		return new ProtoOutputStream(new byte[capacity + 3]);
	}
	
	public static ProtoOutputStream newInstance(int capacity, int messageId){
		return new ProtoOutputStream(new byte[capacity + 3], messageId);
	}
	
	public static ProtoOutputStream newInstance(byte[] buff, int messageId){
		ProtoOutputStream stream = newInstance(buff.length, messageId);
		System.arraycopy(buff, 0, stream.buff, 3, buff.length);
		stream.offset = buff.length + 3;
		return stream;
	}
	
	private byte[] 			buff;
	private byte[]			protoBytes;
	private int				offset;
	private int				limit;
	private boolean			isClosed;
	private ProtoOutputStream(byte[] buff){
		this(buff, 3, buff.length);
		isClosed 	= false;
		protoBytes 	= null;
		buff[0] = (byte) Opcodes.S_EXTENDED_PROTOBUF;
	}
	
	private ProtoOutputStream(byte[] buff, int offset, int limit){
		this.buff = buff;
		this.offset = offset;
		this.limit = limit;
		isClosed 	= false;
		protoBytes 	= null;
	}
	
	private ProtoOutputStream(byte[] buff, int messageId){
		this(buff);
		buff[1] = (byte)(messageId 		& 0xff);
		buff[2] = (byte)(messageId >> 8 	& 0xff);
	}
	
	public int get_current_size(){
		return offset;
	}
	
	public void realloc(int capacity){
		limit 		= capacity;
		byte[] tmp 	= new byte[limit];
		System.arraycopy(buff, 0, tmp, 0, offset);
		buff 		= tmp;
		isClosed	= false;
	}
	
	public void dispose(){
		isClosed = true;
		buff = null;
		protoBytes = null;
	}
	
	public void close(){
		isClosed = true;
	}
	
	public boolean isCreated(){
		return protoBytes != null;
	}
	
	public byte[] getBytes(){
		byte[] b = new byte[offset];
		System.arraycopy(buff, 0, b, 0, offset);
		return b;
	}
	
	public byte[] getProtoBytes(){
		return protoBytes;
	}
	
	public void createProtoBytes(int messageId){
		createProtoBytes();
		protoBytes[1] = (byte)(messageId 		& 0xff);
		protoBytes[2] = (byte)(messageId >> 8 & 0xff);
	}
	
	public void createProtoBytes(){
		protoBytes = new byte[offset + 2];
		System.arraycopy(buff, 0, protoBytes, 0, offset);
	}
	public void createProtoBytesNonEmpty(){
		protoBytes = new byte[offset];
		System.arraycopy(buff, 0, protoBytes, 0, offset);
	}
	
	public void writeRawByte(final byte value) throws IOException{
		if(isClosed)
			throw new IOException(createExceptionMessage("closed stream."));
		
		if(offset >= limit)
			realloc(limit * 2 + 1);
		
		buff[offset++] = value;
	}
	
	public void writeRawByte(final int value) throws IOException{
		writeRawByte((byte)value);
	}
	
	public void writeRawBytes(final byte[] value, int off, int len) throws IOException{
		if(isClosed)
			throw new IOException(createExceptionMessage("closed stream."));

		int capacity = limit;
		while(offset + len > capacity)
			capacity = capacity * 2 + 1;
		if(capacity > limit)
			realloc(capacity);
		
		System.arraycopy(value, off, buff, offset, len);
		offset += len;
	}
	
	public void writeRawBytes(final byte[] value) throws IOException{
		writeRawBytes(value, 0, value.length);
	}
	
	public void writeTag(final int fieldNumber, final int wireType) throws IOException{
		writeRawVarInt32(WireFormat.makeTag(fieldNumber, wireType));
	}
	
	public void writeRawVarInt32(int value) throws IOException{
		while(true){
			if((value & ~0X7f) == 0){
				writeRawByte(value);
				return;
			}
			
			writeRawByte((value & 0x7F) | 0x80);
			value >>>= 7;
		}
	}
	
	public void writeRawVarInt64(long value) throws IOException{
		while(true){
			if((value & ~0x7F) == 0){
				writeRawByte((int)value);
				return;
			}
			
			writeRawByte(((int)value & 0x7F) | 0x80);
			value >>>= 7;
		}
	}
	
	public void writeRawLittleEndian32(final int value) throws IOException{
		writeRawByte((value		 ) & 0xFF);
		writeRawByte((value >> 	8) & 0xFF);
		writeRawByte((value >> 16) & 0xFF);
		writeRawByte((value >> 24) & 0xFF);
		
	}
	
	public void writeRawLittleEndian64(final long value) throws IOException{
		writeRawByte((int)(value	  ) & 0xFF);
		writeRawByte((int)(value >>  8) & 0xFF);
		writeRawByte((int)(value >> 16) & 0xFF);
		writeRawByte((int)(value >> 24) & 0xFF);
		writeRawByte((int)(value >> 32) & 0xFF);
		writeRawByte((int)(value >> 40) & 0xFF);
		writeRawByte((int)(value >> 48) & 0xFF);
		writeRawByte((int)(value >> 56) & 0xFF);
	}
	
	public void writeDouble(final int fieldNumber, final double value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64);
		writeDoubleNoTag(value);
	}
	
	public void writeFloat(final int fieldNumber, final float value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);
		writeFloatNoTag(value);
	}
	
	public void writeInt64(final int fieldNumber, final long value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
		writeInt64NoTag(value);
	}
	
	public void wirteUInt64(final int fieldNumber, final long value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
		writeUInt64NoTag(value);
	}
	
	public void wirteInt32(final int fieldNumber, final int value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
		writeInt32NoTag(value);
	}
	
	public void writeFixed64(final int fieldNumber, final long value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64);
		writeFixed64NoTag(value);
	}
	
	public void writeFixed32(final int fieldNumber, final int value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);
		writeFixed32NoTag(value);
	}
	
	public void writeBool(final int fieldNumber, final boolean value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
		writeBoolNoTag(value);
	}
	
	public void writeString(final int fieldNumber, final String value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
		writeStringNoTag(value);
	}
	
	public void writeUInt32(final int fieldNumber, final int value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
		writeUInt32NoTag(value);
	}
	
	public void writeEnum(final int fieldNmber, final int value) throws IOException{
		writeTag(fieldNmber, WireFormat.WIRETYPE_VARINT);
		writeEnumNoTag(value);
	}

	public void writeSFixed32(final int fieldNumber, final int value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED32);
		writeSFixed32NoTag(value);
	}

	public void writeSFixed64(final int fieldNumber, final long value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_FIXED64);
		writeSFixed64NoTag(value);		
	}
	
	public void writeSInt32(final int fieldNumber, final int value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
		writeSInt32NoTag(value);
	}
	
	public void writeSInt64(final int fieldNumber, final long value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_VARINT);
		writeSInt64NoTag(value);
	}
	
	public void writeBytes(final int fieldNumber, final byte[] value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
		writeBytesNoTag(value);
	}
	
	public void writeMessage(final int fieldNumber, final ProtoMessage value) throws IOException{
		writeTag(fieldNumber, WireFormat.WIRETYPE_LENGTH_DELIMITED);
		writeMessageNoTag(value);
	}
	
	public void writeDoubleNoTag(final double value) throws IOException{
		writeRawLittleEndian64(Double.doubleToRawLongBits(value));
	}
	
	public void writeFloatNoTag(final float value) throws IOException{
		writeRawLittleEndian32(Float.floatToRawIntBits(value));
	}
	
	public void writeUInt64NoTag(final long value) throws IOException{
		writeRawVarInt64(value);
	}
	
	public void writeInt64NoTag(final long value) throws IOException {
		writeRawVarInt64(value);
	}
	 
	public void writeInt32NoTag(final int value) throws IOException{
		if(value >= 0)
			writeRawVarInt32(value);
		else
			writeRawVarInt64(value);
	}
	
	public void writeFixed64NoTag(final long value) throws IOException{
		writeRawLittleEndian64(value);
	}
	
	public void writeFixed32NoTag(final int value) throws IOException{
		writeRawLittleEndian32(value);
	}
	
	public void writeBoolNoTag(final boolean value) throws IOException{
		writeRawByte(value ? 1 : 0);
	}
	
	public void writeStringNoTag(final String value) throws IOException{
		if(value == null){
			writeRawByte(0x00);
			return;
		}
		
		final byte[] bytes = value.getBytes("MS949");
		writeRawVarInt32(bytes.length);
		writeRawBytes(bytes);
	}
	
	public void writeUInt32NoTag(final int value) throws IOException{
		writeRawVarInt32(value);
	}
	
	public void writeEnumNoTag(final int value) throws IOException{
		writeInt32NoTag(value);
	}
	
	public void writeSFixed32NoTag(final long value) throws IOException{
		writeRawLittleEndian64(value);
	}
	
	public void writeSFixed64NoTag(final long value) throws IOException{
		writeRawLittleEndian64(value);
	}
	
	public void writeSInt32NoTag(final int value) throws IOException{
		writeRawVarInt32(encodeZigZag32(value));
	}
	
	public void writeSInt64NoTag(final long value) throws IOException{
		writeRawVarInt64(encodeZigZag64(value));
	}
	
	public void writeBytesNoTag(final byte[] value) throws IOException{
		if(value == null){
			writeRawByte(0x00);
			return;
		}
		
		writeRawVarInt32(value.length);
		writeRawBytes(value);
	}
	
	public void writeMessageNoTag(final ProtoMessage value) throws IOException{
		if(value == null){
			writeRawByte(0x00);
			return;
		}
		
		writeRawVarInt32(value.getSerializedSize());
		value.writeTo(this);
	}
	
	private String createExceptionMessage(String sourceMessage) {
		return String.format("%s [occurred buff in position : %d, closed : %s]", sourceMessage, offset, isClosed);
	}
	
	public static int encodeZigZag32(final int n){
		return (n << 1) ^ (n >> 31);
	}
	
	public static long encodeZigZag64(final long n){
		return (n << 1) ^ (n >> 63);
	}
	
	public static int computeDoubleSize(final int fieldNumber, final double value){
		return computeTagSize(fieldNumber) + computeDoubleSizeNoTag(value);
	}
	
	public static int computeFloatSize(final int fieldNumber, final float value){
		return computeTagSize(fieldNumber) + computeFloatSizeNoTag(value);
	}
	
	public static int computeUInt64Size(final int fieldNumber, final long value){
		return computeTagSize(fieldNumber) + computeUInt64SizeNoTag(value);
	}
	
	public static int computeInt64Size(final int fieldNumber, final long value){
		return computeTagSize(fieldNumber) + computeInt64SizeNoTag(value);
	}
	
	public static int computeInt32Size(final int fieldNumber, final int value){
		return computeTagSize(fieldNumber) + computeInt32SizeNoTag(value);
	}
	
	public static int computeFixed64Size(final int fieldNumber, final long value){
		return computeTagSize(fieldNumber) + computeFixed64SizeNoTag(value);
	}
	
	public static int computeFixed32Size(final int fieldNumber, final int value){
		return computeTagSize(fieldNumber) + computeFixed32SizeNoTag(value);
	}
	
	public static int computeBoolSize(final int fieldNumber, final boolean value){
		return computeTagSize(fieldNumber) + computeBoolSizeNoTag(value);
	}
	
	public static int computeStringSize(final int fieldNumber, final String value){
		return computeTagSize(fieldNumber) + computeStringSizeNoTag(value);
	}
	
	public static int computeUInt32Size(final int fieldNumber, final int value){
		return computeTagSize(fieldNumber) + computeUInt32SizeNoTag(value);
	}
	
	public static int computeEnumSize(final int fieldNumber, final int value){
		return computeTagSize(fieldNumber) + computeEnumSizeNoTag(value);
	}
	
	public static int computeSFixed32Size(final int fieldNumber, final int value){
		return computeTagSize(fieldNumber) + computeSFixed32SizeNoTag(value);
	}
	
	public static int computeSFixed64Size(final int fieldNumber, final long value){
		return computeTagSize(fieldNumber) + computeSFixed64SizeNoTag(value);
	}
	
	public static int computeSInt32Size(final int fieldNumber, final int value) {
		return computeTagSize(fieldNumber) + computeSInt32SizeNoTag(value);
	}
	
	public static int computeSInt64Size(final int fieldNumber, final long value){
		return computeTagSize(fieldNumber) + computeSInt64SizeNoTag(value);
	}
	
	public static int computeBytesSize(final int fieldNumber, final byte[] value){
		return computeTagSize(fieldNumber) + computeBytesSizeNoTag(value);
	}
	
	public static int computeMessageSize(final int fieldNumber, final ProtoMessage value){
		return computeTagSize(fieldNumber) + computeMessageSizeNoTag(value);
	}
	
	public static int computeDoubleSizeNoTag(final double value){
		return LITTLE_ENDIAN_64_SIZE;
	}
	
	public static int computeFloatSizeNoTag(final float value){
		return LITTLE_ENDIAN_32_SIZE;
	}
	
	public static int computeUInt64SizeNoTag(final long value) {
		return computeRawVarInt64Size(value);
	}
	
	public static int computeInt64SizeNoTag(final long value) {
		return computeRawVarInt64Size(value);
	}
	
	public static int computeInt32SizeNoTag(final int value){
		return value >= 0 ? computeRawVarInt32Size(value) : 10;
	}
	
	public static int computeFixed64SizeNoTag(final long value) {
		return LITTLE_ENDIAN_64_SIZE;
	}
	
	public static int computeFixed32SizeNoTag(final int value) {
		return LITTLE_ENDIAN_32_SIZE;
	}
	
	public static int computeBoolSizeNoTag(final boolean value){
		return 1;
	}
	
	public static int computeStringSizeNoTag(final String value){
		if(value == null)
			return 1;
		
		try{
			final byte[] bytes = value.getBytes("MS949");
			return computeRawVarInt32Size(bytes.length) + bytes.length;
		}catch(UnsupportedEncodingException e){
			throw new RuntimeException("UTF-8 not supported.", e);
		}
	}
	
	public static int computeTagSize(final int fieldNumber){
		return computeRawVarInt32Size(WireFormat.makeTag(fieldNumber, 0));
	}
	
	public static int computeUInt32SizeNoTag(final int value){
		return computeRawVarInt32Size(value);
	}
	
	public static int computeEnumSizeNoTag(final int value){
		return computeInt32SizeNoTag(value);
	}
	
	public static int computeSFixed32SizeNoTag(final int value){
		return LITTLE_ENDIAN_32_SIZE;
	}
	
	public static int computeSFixed64SizeNoTag(final long value){
		return LITTLE_ENDIAN_64_SIZE;
	}
	
	public static int computeSInt32SizeNoTag(final int value){
		return computeRawVarInt32Size(encodeZigZag32(value));
	}
	
	public static int computeSInt64SizeNoTag(final long value) {
		return computeRawVarInt64Size(encodeZigZag64(value));
	}
	
	public static int computeBytesSizeNoTag(final byte[] value){
		if(value == null)
			return 1;
		
		return computeRawVarInt32Size(value.length) + value.length;
	}
	
	public static int computeMessageSizeNoTag(final ProtoMessage value){
		if(value == null)
			return 1;
		
		final int size = value.getSerializedSize();
		return computeRawVarInt32Size(size) + size;
	}
	
	public static int computeRawVarInt32Size(final int value){
		return (value & (0xffffffff <<  7)) == 0 ? 1 :
			(value & (0xffffffff << 14)) == 0 ? 2 :
			(value & (0xffffffff << 21)) == 0 ? 3 :
			(value & (0xffffffff << 28)) == 0 ? 4 : 5;
	}
	
	public static int computeRawVarInt64Size(final long value){
		return (value & (0xffffffffffffffffL <<  7)) == 0 ? 1 :
			(value & (0xffffffffffffffffL <<  14)) == 0 ? 2 :
			(value & (0xffffffffffffffffL <<  21)) == 0 ? 3 :
			(value & (0xffffffffffffffffL <<  28)) == 0 ? 4 :
			(value & (0xffffffffffffffffL <<  35)) == 0 ? 5 :
			(value & (0xffffffffffffffffL <<  42)) == 0 ? 6 :
			(value & (0xffffffffffffffffL <<  49)) == 0 ? 7 :
			(value & (0xffffffffffffffffL <<  56)) == 0 ? 8 :
			(value & (0xffffffffffffffffL <<  63)) == 0 ? 9 : 10;							
	}
}

