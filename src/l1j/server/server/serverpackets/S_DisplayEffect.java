package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_DisplayEffect extends ServerBasePacket {
	private static final String S_DISPLAY_EFFECT = "[S] S_DisplayEffect";
	private byte[] _byte = null;
	
	public static final int PUPLE_BORDER_DISPLAY			= 0x01;	// 보라색 테투리 디스플레이 효과
	public static final int QUAKE_DISPLAY					= 0x02;	// 지진 디스플레이 효과
	public static final int FIREWORK_DISPLAY				= 0x03;	// 폭죽 디스플레이 효과
	public static final int RINDVIOR_LIGHT_DISPLAY			= 0x04;	// 린드비오르 번개 이펙트 디스플레이 효과
	public static final int	BLUE_BORDER_DISPLAY				= 0x06;	// 파란색 테투리 디스플레이 효과
	public static final int VALAKAS_BORDER_DISPLAY 			= 0x07;	// 발라카스 화염 테투리 디스플레이 효과
	public static final int RINDVIOR_BORDER_DISPLAY 		= 0x08;	// 린드비오르 푸른 회색 테투리 디스플레이 효과
	public static final int BLACK_DISPLAY 					= 0x0A;	// 화면 어두워지는 디스플레이 효과
	
	/** 화면 자체 이펙트를 전송한다. **/
	public S_DisplayEffect(int value){
		writeC(Opcodes.S_EVENT);
		writeC(0x53);
		writeD(value);
		writeH(0x00);
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_DISPLAY_EFFECT;
	}

}

