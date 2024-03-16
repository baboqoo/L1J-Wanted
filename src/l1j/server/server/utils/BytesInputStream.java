package l1j.server.server.utils;

import java.io.IOException;
import java.io.InputStream;

public class BytesInputStream extends InputStream{
	private byte[] 	_buf;
	private int 	_idx;
	private int 	_limit;
	private int 	_mark;
	private boolean _isClosed;
	
	public BytesInputStream(){
		_buf 		= null;
		_idx 		= -1;
		_limit 		= -1;
		_mark		= -1;
		_isClosed 	= true;
	}
	
	public BytesInputStream(byte[] data){
		this(data, 0, data.length);
	}
	
	public BytesInputStream(byte[] data, int offset, int length){
		if (data == null)
			throw new NullPointerException();
		if (offset < 0 || offset + length > data.length || length < 0)
			throw new IndexOutOfBoundsException();
		
		_buf 		= data;
		_idx 		= offset;
		_limit 		= offset + length;
		_mark		= offset;
		_isClosed 	= false;
	}
	
	public void setBuff(byte[] data, int offset, int length) throws IOException{
		if (data == null)
			throw new NullPointerException();
		if (offset < 0 || offset + length > data.length || length < 0)
			throw new IndexOutOfBoundsException();
		
		_buf 		= data;
		_idx 		= offset;
		_limit 		= offset + length;
		_mark		= offset;
		_isClosed 	= false;		
	}
	
	public int readD() throws IOException{
		if (_isClosed)
			throw new IOException("BytesInputStream Closed...");
		if (_idx + 3 >= _limit) {
			return -1; // EOF
		}
		int result = _buf[_idx++] 		& 0xff;
		result |= _buf[_idx++] << 8 	& 0xff00;
		result |= _buf[_idx++] << 16 	& 0xff0000;
		result |= _buf[_idx++] << 24 	& 0xff000000;
		return result;
	}
	
	public int readH() throws IOException{
		if (_isClosed)
			throw new IOException("BytesInputStream Closed...");
		if (_idx + 1 >= _limit) {
			return -1; // EOF
		}
		int result = _buf[_idx++] 	& 0xff;
		result |= _buf[_idx++] << 8 & 0xff00;
		return result;
	}
	
	@Override
	public int read() throws IOException{
		if (_isClosed)
			throw new IOException("BytesInputStream Closed...");
		if (_idx >= _limit) {
			return -1; // EOF
		}
		return _buf[_idx++] & 0xff;
	}
	
	public int read(byte data[], int offset, int length) throws IOException{
		if (data == null)
			throw new NullPointerException();
		if (offset < 0 || offset + length > data.length || length < 0)
			throw new IndexOutOfBoundsException();
		if (_isClosed)
			throw new IOException("BytesInputStream Closed...");
		if (_idx >= _limit) {
			return -1; // EOF
		}
		if (length > _limit - _idx) {
			length = _limit - _idx;
		}
		System.arraycopy(_buf, _idx, data, offset, length);
		_idx += length;
		return length;
	}
	
	public String readS() throws IOException{
		return readS(CharsetUtil.MS_949_STR);
	}
	
	public String readS(String encoding) throws IOException{
		if (_isClosed)
			throw new IOException("BytesInputStream Closed...");
		if (_idx >= _limit) {
			return null; // eof
		}
		String s = null;
		s = new String(_buf, _idx, _buf.length - _idx, encoding);
		s = s.substring(0, s.indexOf('\0'));
		_idx += HexHelper.getTxtToBytesLength(s);
		return s;
	}
	
	public String readS(int length) throws IOException{
		return readS(CharsetUtil.MS_949_STR, length);
	}
	
	public String readS(String encoding, int length) throws IOException{
		if (_isClosed)
			throw new IOException("BytesInputStream Closed...");
		if (_idx >= _limit) {
			return null; // eof
		}
		String s = null;
		s = new String(_buf, _idx, length, encoding);
		_idx += HexHelper.getTxtToBytesLength(s);
		return s;
	}
	
	public String readSForMultiBytes() throws IOException{
		byte d1				= 0;
		byte d2				= 0;
		byte[] tmpB			= new byte[2];
		StringBuilder sb 	= new StringBuilder();
		if (_isClosed)
			throw new IOException("BytesInputStream Closed...");
		while(true) {
			if (_idx + 2 >= _limit) {
				break;
			}
			
			d1 = _buf[_idx++];
			d2 = _buf[_idx++];
			if (d1 == 0 && d2 == 0) {
				break;
			}
			if (d1 >= 0x7F || d2 >= 0x7F) {/** korean language. **/
				tmpB[0] = d2;
				tmpB[1] = d1;
				sb.append(new String(tmpB, 0, 2, CharsetUtil.MS_949_STR));
			} else {/** english and number. **/
				tmpB[0] = d1;
				sb.append(new String(tmpB, 0, 1, CharsetUtil.MS_949_STR));
			}
		}
		return sb.toString();
	}
	
	public long skip(long amount) throws IOException{
		if (_isClosed)
			throw new IOException("BytesInputStream Closed...");
		if (amount <= 0) {
			return 0;
		}
		if (amount > _limit - _idx) {
			amount = _limit - _idx;
		}
		_idx += (int)amount;
		return amount;
	}
	
	public int available() throws IOException{
		if (_isClosed)
			throw new IOException("BytesInputStream Closed...");
		return _limit - _idx;
	}
	
	public void close(){
		_isClosed = true;
	}
	
	public void mark(){
		_mark = _idx;
	}
	
	public void resetIdx() throws IOException{
		if (_isClosed)
			throw new IOException("BytesInputStream Closed...");
		_idx = _mark;
	}
	
	public void reset(){
		_buf 		= null;
		_idx 		= -1;
		_limit 		= -1;
		_mark		= -1;
		_isClosed 	= true;
	}
	
	public boolean markSupported(){
		return true;
	}
}
