package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_RedbloodEnemyPledgeNoti extends ServerBasePacket {
	private static final String S_REDBLOOD_ENEMY_PLEDGE_NOTI = "[S] S_RedbloodEnemyPledgeNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0068;
	
	public S_RedbloodEnemyPledgeNoti(S_RedbloodEnemyPledgeNoti.init_status status, String enemypledge) {
		write_init();
		write_status(status);
		write_enemypledge(enemypledge);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_status(S_RedbloodEnemyPledgeNoti.init_status status) {
		writeRaw(0x08);
		writeRaw(status.value);
	}
	
	void write_enemypledge(String enemypledge) {
		writeRaw(0x12);
		writeStringWithLength(enemypledge);
	}
	
	public enum init_status{
		siegeinsertattack(1),
		siegeinsertdefence(2),
		siegedeletewar(3),
		;
		private int value;
		init_status(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(init_status v){
			return value == v.value;
		}
		public static init_status fromInt(int i){
			switch(i){
			case 1:
				return siegeinsertattack;
			case 2:
				return siegeinsertdefence;
			case 3:
				return siegedeletewar;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments init_status, %d", i));
			}
		}
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
		return S_REDBLOOD_ENEMY_PLEDGE_NOTI;
	}
}

