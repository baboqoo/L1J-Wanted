package l1j.server.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BytesOutputStream extends OutputStream{
	private byte[] 	_buf;		// 버퍼
	private int		_idx;		// 현재 버퍼가 가르키고 있는 인덱스
	private int		_capacity;	// 버퍼의 크기 / 확장될 크기
	private boolean _isClosed;	// 스트림이 닫혔는지?
	private boolean	_isShared;	// 스트림의 데이터가 공유가능한지
	
	public BytesOutputStream(){
		this(4096);
	}
	
	public BytesOutputStream(int capacity){
		_isShared	= false;
		_isClosed	= false;
		_capacity 	= capacity;
		_buf		= new byte[_capacity];
	}
	
	/** 스트림의 크기를 재조정한다. **/
	private void realloc(int capacity){
		_capacity 	= capacity;
		byte[] tmp 	= new byte[_capacity];
		System.arraycopy(_buf, 0, tmp, 0, _idx);
		_buf 		= tmp;
		_isShared 	= false;
	}
	
	/** 데이터를 쓴다. **/
	@Override
	public void write(int i) throws IOException {
		if (_isClosed)
			throw new IOException("BytesOutputStream Closed...");
		if (_idx >= _capacity) {
			realloc(_capacity*2+1);
		}
		_buf[_idx++] = (byte)(i & 0xff);
	}
	
	/** 데이터를 쓴다. **/
	public void write(byte[] data, int offset, int length) throws IOException{
		if (data == null)
			throw new NullPointerException();
		if (offset < 0 || offset + length > data.length || length < 0)
			throw new IndexOutOfBoundsException();
		if (_isClosed)
			throw new IOException("BytesOutputStream Closed...");
		int capacity = _capacity;
		while (_idx + length > capacity) {
			capacity = capacity*2+1;
		}
		if (capacity > _capacity) {
			realloc(capacity);
		}
		System.arraycopy(data, offset, _buf, _idx, length);
		_idx += length;
	}
	
	/** short형(2byte) 데이터를 쓴다. **/
	public void writeH(int i) throws IOException{
		write(i 		& 0xFF);
	    write(i >> 8 	& 0xFF);
	}
	
	/** int형(4byte) 데이터를 쓴다. **/
	public void writeD(int i) throws IOException{
		write(i 		& 0xFF);
	    write(i >> 8 	& 0xFF);
	    write(i >> 16 	& 0xFF);
	    write(i >> 24 	& 0xFF);
	}
	
	public void writeBit(long value) throws Exception{
		if (value < 0L) {
			String str = Integer.toBinaryString((int)value);
			value = Long.valueOf(str, 2).longValue();
		}
		int i = 0;
		while (value >> 7 * (i + 1) > 0L) {
			write((int)((value >> 7 * i++) & 0x0000007F | 0x80));
		}
		write((int)((value >> 7 * i) & 0x0000007F));
	}
	
	public void writeS(String text) throws IOException{
		writeS(text, CharsetUtil.MS_949_STR);
	}
	
	public void writeS(String text, String encoding) throws IOException{
		if (text != null) {
			byte[] b = text.getBytes(encoding);
			write(b, 0, b.length);
		}
		write(0);
	}
	
	public void writeSForMultiBytes(String text) throws IOException{
		writeSForMultiBytes(text, CharsetUtil.MS_949_STR);
	}
	
	public void writeSForMultiBytes(String text, String encoding) throws IOException{
		if (text != null) {
			byte[] b = text.getBytes(encoding);
			int i = 0;
			while (i < b.length) {
				if ((b[i] & 0xff) >= 0x7f) {
					write(b[i + 1]);
					write(b[i]);
					i += 2;
				} else {
					write(b[i]);
					write(0);
					i += 1;
				}
			}
		}
		write(0);
		write(0);
	}
	
	/** 새로운 outputStream에 쓴다. **/
	public void writeTo(OutputStream out) throws IOException{
		out.write(_buf, 0, _idx);
	}
	
	/** InputStream으로 만든다. **/
	public InputStream toInputStream(){
		_isShared = true;
		return new BytesInputStream(_buf, 0, _idx);
	}
	
	/** 초기화 **/
	public void reset() throws IOException{
		if (_isClosed) {
			_isClosed = false;
		}
		if (_isShared) {
			_buf = new byte[_capacity];
			_isShared = false;
		}
		_idx = 0;
	}
	
	/** 스트림을 닫는다. **/
	public void close(){
		_isClosed = true;
	}
	
	/** 스트림의 내용을 배열로 반환한다. **/
	public byte[] toArray(){
		byte[] result = new byte[_idx];
		System.arraycopy(_buf, 0, result, 0, _idx);
		return result;
	}
}


