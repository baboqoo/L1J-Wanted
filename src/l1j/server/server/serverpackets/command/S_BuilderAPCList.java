package l1j.server.server.serverpackets.command;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BuilderAPCList extends ServerBasePacket {
	private static final String S_BUILDER_APC_LIST = "[S] S_BuilderAPCList";
	private byte[] _byte = null;
	
	public static final int LIST = 0x0a13;
	
	public S_BuilderAPCList(){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(LIST);
		
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc instanceof L1AiUserInstance) {
				continue;
			}
			
			byte[] nameBytes	= pc.getName().getBytes();
			int level			= pc.getLevel();
			int classType		= pc.getType();
			
			int length = 9 + nameBytes.length + getBitSize(pc.getId());
			
			writeC(0x0a);
			writeC(length);
			
			writeC(0x0a);
			writeBytesWithLength(nameBytes);
			
			writeC(0x10);
			writeC(level);
			
			writeC(0x18);
			writeC(classType);
			
			writeC(0x20);// rank
			writeC(pc.getConfig().get_all_rank());
			
			writeC(0x28);
			writeBit(pc.getId());
		}
		
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
		return S_BUILDER_APC_LIST;
	}
}

