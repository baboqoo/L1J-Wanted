package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Paralysis extends ServerBasePacket {
	public static final int TYPE_PARALYSIS				= 1;
	public static final int TYPE_PARALYSIS2				= 2;
	public static final int TYPE_SLEEP					= 3;
	public static final int TYPE_FREEZE					= 4;
	public static final int TYPE_STUN					= 5;
	public static final int TYPE_BIND					= 6;
	public static final int TYPE_TELEPORT				= 7;
	public static final int TYPE_RIP					= 8;
	public static final int TYPE_PERADO					= 9;
	public static final int TYPE_FORCE_STUN				= 10;
	public static final int TYPE_RESTRICT				= 11;
	public static final int TYPE_PHANTOM				= 12;
	public static final int TYPE_OSIRIS					= 13;
	
	public static final S_Paralysis PARALYSIS_ON		= new S_Paralysis(TYPE_PARALYSIS, true);
	public static final S_Paralysis PARALYSIS_OFF		= new S_Paralysis(TYPE_PARALYSIS, false);
	public static final S_Paralysis SLEEP_ON			= new S_Paralysis(TYPE_SLEEP, true);
	public static final S_Paralysis SLEEP_OFF			= new S_Paralysis(TYPE_SLEEP, false);
	public static final S_Paralysis FREEZE_ON			= new S_Paralysis(TYPE_FREEZE, true);
	public static final S_Paralysis FREEZE_OFF			= new S_Paralysis(TYPE_FREEZE, false);
	public static final S_Paralysis STURN_ON			= new S_Paralysis(TYPE_STUN, true);
	public static final S_Paralysis STURN_OFF			= new S_Paralysis(TYPE_STUN, false);
	public static final S_Paralysis BIND_ON				= new S_Paralysis(TYPE_BIND, true);
	public static final S_Paralysis BIND_OFF			= new S_Paralysis(TYPE_BIND, false);
	public static final S_Paralysis TELEPORT_LOCK		= new S_Paralysis(TYPE_TELEPORT, true);// 텔레포트 락풀기
	public static final S_Paralysis TELEPORT_UNLOCK		= new S_Paralysis(TYPE_TELEPORT, false);// 텔레포트 락풀기
	public static final S_Paralysis GRIP_ON				= new S_Paralysis(TYPE_RIP, true);
	public static final S_Paralysis GRIP_OFF			= new S_Paralysis(TYPE_RIP, false);
	public static final S_Paralysis DESPERADO_ON		= new S_Paralysis(TYPE_PERADO, true);
	public static final S_Paralysis DESPERADO_OFF		= new S_Paralysis(TYPE_PERADO, false);
	public static final S_Paralysis FORCE_STUN_ON		= new S_Paralysis(TYPE_FORCE_STUN, true);
	public static final S_Paralysis FORCE_STUN_OFF		= new S_Paralysis(TYPE_FORCE_STUN, false);
	public static final S_Paralysis RESTRICT_ON			= new S_Paralysis(TYPE_RESTRICT, true);
	public static final S_Paralysis RESTRICT_OFF		= new S_Paralysis(TYPE_RESTRICT, false);
	public static final S_Paralysis PHANTOM_ON			= new S_Paralysis(TYPE_PHANTOM, true);
	public static final S_Paralysis PHANTOM_OFF			= new S_Paralysis(TYPE_PHANTOM, false);
	public static final S_Paralysis OSIRIS_ON			= new S_Paralysis(TYPE_OSIRIS, true);
	public static final S_Paralysis OSIRIS_OFF			= new S_Paralysis(TYPE_OSIRIS, false);

	// 28, 29 손바닥 모든 행동제약, 피바 변색
	// 32, 33 손바닥 모든 행동제약
	public S_Paralysis(int type, boolean flag) {
		writeC(Opcodes.S_PARALYSE);
		switch(type){
		case TYPE_PARALYSIS:		writeC(flag ? 2 : 3);break;		// 체가 완전하게 마비되었습니다.
		case TYPE_PARALYSIS2:		writeC(flag ? 4 : 5);break;		// 체가 완전하게 마비되었습니다.
		case TYPE_TELEPORT:			writeC(flag ? 6 : 7);break;		// 텔레포트 대기 상태의 해제
		case TYPE_SLEEP:			writeC(flag ? 10 : 11);break;	// 강력한 수마가 덮쳐 와, 자 버렸습니다.
		case TYPE_FREEZE:			writeC(flag ? 12 : 13);break;	// 체가 얼었습니다.
		case TYPE_STUN:				writeC(flag ? 22 : 23);break;	// 스턴 상태입니다.
		case TYPE_BIND:				writeC(flag ? 24 : 25);break;	// 다리가 속박된 것처럼 움직일 수 없습니다.
		case TYPE_RIP:				writeC(flag ? 26 : 27);break;	// 파워그립
		case TYPE_PERADO:			writeC(flag ? 30 : 31);break;	// 데스페라도
		case TYPE_FORCE_STUN:		writeC(flag ? 34 : 35);break;	// 포스스턴
		case TYPE_RESTRICT:			writeC(flag ? 36 : 37);break;	// 이터니티, 버닝샷(이동, 귀환 불가)
		case TYPE_PHANTOM:			writeC(flag ? 38 : 39);break;	// 펜텀
		case TYPE_OSIRIS:			writeC(flag ? 42 : 43);break;	// 오시리스
		default:					writeH(0x00);break;
		}
	}
	
	public static void init(){}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	
	@Override
	public String getType() {
		return S_PARALYSIS;
	}
	
	private static final String S_PARALYSIS = "[S] S_Paralysis";
}

