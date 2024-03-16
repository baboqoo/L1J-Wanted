package l1j.server.server.serverpackets.command;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BuilderUserList extends ServerBasePacket {
	private static final String S_BUILDER_USER_LIST = "[S] S_BuilderUserList";
	private byte[] _byte = null;
	public static final int LIST = 0x024e;
	
	public S_BuilderUserList(){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(LIST);
		
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc instanceof L1AiUserInstance) {
				continue;
			}
			
			byte[] nameBytes	= pc.getName().getBytes();
			int level			= pc.getLevel();
			int classType		= pc.getType();
			byte[] accountName	= pc.getAccountName().getBytes();
			int ip				= pc.getNetConnection() == null ? 0 : pc.getNetConnection().getIpBigEndian();
			byte[] clanBytes	= pc.getClanName().getBytes();
			
			if (accountName == null) {
				continue;
			}
			
			int length = 12 + nameBytes.length + accountName.length + getBitSize(ip) + getBitSize(Config.VERSION.SERVER_NUMBER);
			if (clanBytes != null) {
				length += clanBytes.length;
			}
			
			writeC(0x0a);
			writeC(length);
			
			writeC(0x0a);
			writeBytesWithLength(nameBytes);
			
			writeC(0x10);
			writeC(level);
			
			writeC(0x18);
			writeC(classType);
			
			writeC(0x22);
			writeBytesWithLength(accountName);
			
			writeC(0x28);
			writeBit(ip);
			
			writeC(0x32);
			if (clanBytes == null) {
				writeC(0x00);
			} else {
				writeBytesWithLength(clanBytes);
			}
			
			writeC(0x38);
			writeBit(Config.VERSION.SERVER_NUMBER);
		}
		
		writeC(0x10);
		writeC(1);
		
		writeC(0x18);
		writeC(1);
		
        writeH(0x00);
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
		return S_BUILDER_USER_LIST;
	}
}

