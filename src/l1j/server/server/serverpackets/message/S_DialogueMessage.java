package l1j.server.server.serverpackets.message;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_DialogueMessage extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_DIALOGUE_MESSAGE = "[S] S_DialogueMessage";
	public static final int DIALOGUE_MESSAGE	= 0x0244;
	
	public S_DialogueMessage(int gfx, int guestnumber, int duraion) {
		write_init();
		writeRaw(0x0a);
		writeRaw(getBitSize(gfx) + getBitSize(guestnumber) + getBitSize(duraion) + 3);
		write_talker_id(gfx);
		write_dialogue_id(guestnumber);
		write_duration(duraion);
		writeH(0x00);
	}
	
	/** 인던 메세지 **/
	public static final	int[] ORIM_START						= { 1996, 1997, 1998, 1999, 2000 };
	public static final int[] ORIM_END							= { 2001, 2002, 2003, 2004 };
	public static final int[] ORIM_HADIN_DIE					= { 2036, 2037, 2038, 2039 };
	
	public static final	int[] SAGE_START						= { 2018, 2019, 2020 };
	public static final	int[] SAGE_LOSUS_DIE					= { 2021, 2022, 2023 };
	public static final	int SAGE_BELETH_DIE						= 2024;
	public static final	int SAGE_END							= 2025;
	
	public static final	int[] FANTASY_START						= { 2026, 2027, 2028, 2029 };
	public static final	int UNICON_START						= 2030;
	public static final	int UNICON_END							= 2031;
	public static final	int[] NIGHTMARE_END						= { 2032, 2033 };
	public static final	int[] SUCUBUS_END						= { 2034, 2035 };
	
	public static final int[] ELDER_START						= { 2070, 2071, 2072 };
	public static final int[] ELDER_END							= { 2073, 2074, 2075 };
	
	public static final S_DialogueMessage ORIM_START_MENT		= new S_DialogueMessage(1);
	public static final S_DialogueMessage ORIM_END_MENT			= new S_DialogueMessage(2);
	public static final S_DialogueMessage ORIM_GERANG_MENT		= new S_DialogueMessage(7);
	
	public static final S_DialogueMessage SAGE_START_MENT		= new S_DialogueMessage(3);
	public static final S_DialogueMessage SAGE_LOSUS_DIE_MENT	= new S_DialogueMessage(4);
	public static final S_DialogueMessage SAGE_BELETH_DIE_MENT	= new S_DialogueMessage(5);
	public static final S_DialogueMessage SAGE_END_MENT			= new S_DialogueMessage(6);
	
	public static final S_DialogueMessage FANTASY_START_MENT	= new S_DialogueMessage(8);
	public static final S_DialogueMessage UNICON_START_MENT		= new S_DialogueMessage(9);
	public static final S_DialogueMessage UNICON_END_MENT		= new S_DialogueMessage(10);
	public static final S_DialogueMessage NIGHTMARE_END_MENT	= new S_DialogueMessage(11);
	public static final S_DialogueMessage SUCUBUS_END_MENT		= new S_DialogueMessage(12);
	
	public static final S_DialogueMessage AURAKIA_START_MENT	= new S_DialogueMessage(13);
	public static final S_DialogueMessage AURAKIA_END_MENT		= new S_DialogueMessage(14);
	
	public S_DialogueMessage(int setting) {
		write_init();
		
		if (setting == 1) {
			int length = getBitSize(22906) + getBitSize(6000) + 3;
			for (int i = 0; i < ORIM_START.length; i++) {
				writeRaw(0x0a);
				writeBit(length + getBitSize(ORIM_START[i])); // 길이
				write_talker_id(22906);
				write_dialogue_id(ORIM_START[i]);
				write_duration(6000);
			}
		} else if (setting == 2) {
			int length = getBitSize(22906) + getBitSize(6000) + 3;
			for (int i = 0; i < ORIM_END.length; i++) {
				writeRaw(0x0a);
				writeBit(length + getBitSize(ORIM_END[i])); // 길이
				write_talker_id(22906);
				write_dialogue_id(ORIM_END[i]);
				write_duration(6000);
			}
		} else if (setting == 3) {
			int length = getBitSize(3515) + getBitSize(6000) + 3;
			for (int i = 0; i < SAGE_START.length; i++) {
				writeRaw(0x0a);
				writeBit(length + getBitSize(SAGE_START[i])); // 길이
				write_talker_id(3515);
				write_dialogue_id(SAGE_START[i]);
				write_duration(6000);
			}
		} else if (setting == 4) {
			int length = getBitSize(3515) + getBitSize(6000) + 3;
			for (int i = 0; i < SAGE_LOSUS_DIE.length; i++) {
				writeRaw(0x0a);
				writeBit(length + getBitSize(SAGE_LOSUS_DIE[i])); // 길이
				write_talker_id(3515);
				write_dialogue_id(SAGE_LOSUS_DIE[i]);
				write_duration(6000);
			}
		} else if (setting == 5) {
			writeRaw(0x0a);
			writeBit(getBitSize(3515) + getBitSize(SAGE_BELETH_DIE) + getBitSize(6000) + 3); // 길이
			write_talker_id(3515);
			write_dialogue_id(SAGE_BELETH_DIE);
			write_duration(6000);
		} else if (setting == 6) {
			writeRaw(0x0a);
			writeBit(getBitSize(3515) + getBitSize(SAGE_END) + getBitSize(6000) + 3); // 길이
			write_talker_id(3515);
			write_dialogue_id(SAGE_END);
			write_duration(6000);
		} else if (setting == 7) {
			int length = getBitSize(22906) + getBitSize(6000) + 3;
			for (int i = 0; i < ORIM_HADIN_DIE.length; i++) {
				writeRaw(0x0a);
				writeBit(length + getBitSize(ORIM_HADIN_DIE[i])); // 길이
				write_talker_id(22906);
				write_dialogue_id(ORIM_HADIN_DIE[i]);
				write_duration(6000);
			}
		} else if (setting == 8) {
			for (int i = 0; i < FANTASY_START.length; i++) {
				int gfx = i == 0 || i == 1 ? 22997 : 23014;
				writeRaw(0x0a);
				writeBit(getBitSize(gfx) + getBitSize(FANTASY_START[i]) + getBitSize(6000) + 3); // 길이
				write_talker_id(gfx);
				write_dialogue_id(FANTASY_START[i]);
				write_duration(6000);	
			}
		} else if (setting == 9) {
			writeRaw(0x0a);
			writeBit(getBitSize(23015) + getBitSize(UNICON_START) + getBitSize(6000) + 3); // 길이
			write_talker_id(23015);
			write_dialogue_id(UNICON_START);
			write_duration(6000);
		} else if (setting == 10) {
			writeRaw(0x0a);
			writeBit(getBitSize(22997) + getBitSize(UNICON_END) + getBitSize(6000) + 3); // 길이
			write_talker_id(22997);
			write_dialogue_id(UNICON_END);
			write_duration(6000);
		} else if (setting == 11) {
			int length = getBitSize(23015) + getBitSize(6000) + 3;
			for (int i = 0; i < NIGHTMARE_END.length; i++) {
				writeRaw(0x0a);
				writeBit(length + getBitSize(NIGHTMARE_END[i])); // 길이
				write_talker_id(23015);
				write_dialogue_id(NIGHTMARE_END[i]);
				write_duration(6000);
			}
		} else if (setting == 12) {
			int length = getBitSize(22997) + getBitSize(6000) + 3;
			for (int i = 0; i < SUCUBUS_END.length; i++) {
				writeRaw(0x0a);
				writeBit(length + getBitSize(SUCUBUS_END[i])); // 길이
				write_talker_id(22997);
				write_dialogue_id(SUCUBUS_END[i]);
				write_duration(6000);
			}
		} else if (setting == 13) {
			int length = getBitSize(3515) + getBitSize(6000) + 3;
			for (int i = 0; i < ELDER_START.length; i++) {
				writeRaw(0x0a);
				writeBit(length + getBitSize(ELDER_START[i])); // 길이
				write_talker_id(3515);
				write_dialogue_id(ELDER_START[i]);
				write_duration(6000);
			}
		} else if (setting == 14) {
			int length = getBitSize(3515) + getBitSize(6000) + 3;
			for (int i = 0; i < ELDER_END.length; i++) {
				writeRaw(0x0a);
				writeBit(length + getBitSize(ELDER_END[i])); // 길이
				write_talker_id(3515);
				write_dialogue_id(ELDER_END[i]);
				write_duration(6000);
			}
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(DIALOGUE_MESSAGE);
	}
	
	void write_talker_id(int talker_id) {
		writeRaw(0x08);// talker_id
		writeBit(talker_id);
	}
	
	void write_dialogue_id(int dialogue_id) {
		writeRaw(0x10);// dialogue_id
		writeBit(dialogue_id);
	}
	
	void write_sound_file(String sound_file) {
		writeRaw(0x1a);// sound_file
		writeStringWithLength(sound_file);
	}
	
	void write_duration(int duration) {
		writeRaw(0x20);// duration
		writeBit(duration);
	}
	
	public static void init(){}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_DIALOGUE_MESSAGE;
	}
}
