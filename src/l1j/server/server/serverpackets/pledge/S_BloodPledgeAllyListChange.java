package l1j.server.server.serverpackets.pledge;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_BloodPledgeAllyListChange extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_ALLY_LIST_CHANGE = "[S] S_BloodPledgeAllyListChange";
	private byte[] _byte = null;
	public static final int CHANGE = 0x040b;
	
	public static final S_BloodPledgeAllyListChange DISMISS	= new S_BloodPledgeAllyListChange(true);
	
	public S_BloodPledgeAllyListChange(boolean dismiss_ally) {
		write_init();
		write_dismiss_ally(dismiss_ally);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CHANGE);
	}
	
	void write_add_pledge(L1Clan add_pledge) {
		PledgeInfoStream os = null;
		try {
			os = new PledgeInfoStream(add_pledge);
			writeC(0x0a);
			writeBytesWithLength(os.getBytes());
			os.clear();
			os = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void write_remove_pledge(L1Clan remove_pledge) {
		PledgeInfoStream os = null;
		try {
			os = new PledgeInfoStream(remove_pledge);
			writeC(0x12);
			writeBytesWithLength(os.getBytes());
			os.clear();
			os = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void write_dismiss_ally(boolean dismiss_ally) {
		writeRaw(0x18);// dismiss_ally
		writeB(dismiss_ally);
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
		return S_BLOODPLEDGE_ALLY_LIST_CHANGE;
	}
}

