package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Ability extends ServerBasePacket {
	private static final String S_ABILITY = "[S] S_Ability";
	private byte[] _byte = null;
	
	public static final S_Ability ABLITY_TELEPORT_ON			= new S_Ability(1, true);
	public static final S_Ability ABLITY_POLY_ON				= new S_Ability(2, true);
	public static final S_Ability ABLITY_INFRAVISION_ON			= new S_Ability(3, true);
	public static final S_Ability ABLITY_SUMMON_ON				= new S_Ability(5, true);
	public static final S_Ability ABLITY_DOMINATION_POLY_ON		= new S_Ability(7, true);
	
	public static final S_Ability ABLITY_TELEPORT_OFF			= new S_Ability(1, false);
	public static final S_Ability ABLITY_POLY_OFF				= new S_Ability(2, false);
	public static final S_Ability ABLITY_INFRAVISION_OFF		= new S_Ability(3, false);
	public static final S_Ability ABLITY_SUMMON_OFF				= new S_Ability(5, false);
	public static final S_Ability ABLITY_DOMINATION_POLY_OFF	= new S_Ability(7, false);

	public S_Ability(int type, boolean equipped) {
		buildPacket(type, equipped);
	}

	private void buildPacket(int type, boolean equipped) {
		writeC(Opcodes.S_CHANGE_ABILITY);
		writeC(type); // 1:ROTC 5:ROSC
		writeC(equipped ? 0x01 : 0x00);
		writeC(0x02);
		writeH(0x0000);
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
		return S_ABILITY;
	}
}

