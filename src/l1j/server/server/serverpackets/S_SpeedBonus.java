package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.utils.BinaryOutputStream;

public class S_SpeedBonus extends ServerBasePacket {
	private static final String S_SPEED_BONUS = "[S] S_SpeedBonus";
	private byte[] _byte = null;
	public static final int SPEED	= 0x040c;
	
	public S_SpeedBonus(int objectnumber, byte[] bonus) {
		write_init();
		write_objectnumber(objectnumber);
		writeRaw(0x12);// bonus
		writeBytesWithLength(bonus);
        writeH(0x00);
	}
	
	public S_SpeedBonus(int objectnumber, byte[] move, byte[] attack) {
		write_init();
		write_objectnumber(objectnumber);
		writeRaw(0x12);// bonus
		writeRaw(move.length + attack.length);
		writeByte(move);
		writeByte(attack);
        writeH(0x00);
	}
	
	public S_SpeedBonus(int objectnumber, byte[] move, byte[] attack, byte[] spell) {
		write_init();
		write_objectnumber(objectnumber);
		writeRaw(0x12);// bonus
		writeRaw(move.length + attack.length + spell.length);
		writeByte(move);
		writeByte(attack);
		writeByte(spell);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SPEED);
	}
	
	void write_objectnumber(int objectnumber) {
		writeRaw(0x08);// objectnumber
		writeBit(objectnumber);
	}
	
	public static byte[] getBonus(S_SpeedBonus.eKind kind, int value) {
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			os.writeC(0x0a);
			os.writeC(3 + os.getBitSize(value));
			
			os.writeC(0x08);// kind
			os.writeC(kind.value);
			
			os.writeC(0x10);// value
			os.writeBit(value);
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public enum eKind{
		MOVE_SPEED(0),
		ATTACK_SPEED(1),
		SPELL_SPEED(2),
		ABSOLUTE_MOVE_SPEED(3),
		ABSOLUTE_ATTACK_SPEED(4),
		ABSOLUTE_SPELL_SPEED(5),
		;
		private int value;
		eKind(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eKind v){
			return value == v.value;
		}
		public static eKind fromInt(int i){
			switch(i){
			case 0:
				return MOVE_SPEED;
			case 1:
				return ATTACK_SPEED;
			case 2:
				return SPELL_SPEED;
			case 3:
				return ABSOLUTE_MOVE_SPEED;
			case 4:
				return ABSOLUTE_ATTACK_SPEED;
			case 5:
				return ABSOLUTE_SPELL_SPEED;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eKind, %d", i));
			}
		}
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_SPEED_BONUS;
	}
}

