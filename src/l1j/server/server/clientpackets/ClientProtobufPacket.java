package l1j.server.server.clientpackets;

import l1j.server.server.utils.CharsetUtil;

public class ClientProtobufPacket {
	private byte _decrypt[];
	public int _off = 0;

	public void clear() {
		_decrypt = null;
		_off = 0;
	}

	public ClientProtobufPacket(byte abyte0[]) {
		_decrypt = abyte0;
		_off = 0;
	}
	
	/**
	 * 테그값 1바이트
	 * @return
	 */
	public int readTag() {
		return _decrypt[_off++] & 0xff;
	}
	
	/**
	 * 이름값 채팅 등등 문자열
	 */
	public String readS(){
		String s = null;
		int len = _decrypt[_off++] & 0xff;
		try {
			s = new String(_decrypt, _off, len, CharsetUtil.EUC_KR_STR);
			_off += s.getBytes(CharsetUtil.EUC_KR_STR).length;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 이름값 채팅 등등 문자열
	 */
	public String readS(int len){
		String s = null;
		try {
			s = new String(_decrypt, _off, len, CharsetUtil.EUC_KR_STR);
			_off += s.getBytes(CharsetUtil.EUC_KR_STR).length;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public int readBit(){
		int i = 0, j = 0;
	    while ((this._decrypt[this._off] & 0xFF) >= 128) {
	    	i |= (this._decrypt[(this._off++)] & 0xFF ^ 0x80) << 7 * j++;
	    }
	    return i |= (this._decrypt[(this._off++)] & 0xFF) << 7 * j;
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

