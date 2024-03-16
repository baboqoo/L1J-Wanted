package l1j.server.server.serverpackets.einhasadpoint;

import java.sql.Timestamp;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.einhasadpoint.EinhasadFaithInfoStream.FaithInfoType;

public class S_EinhasadFaithEnableIndex extends ServerBasePacket {
	private static final String S_EINHASAD_FAITH_ENABLE_INDEX = "[S] S_EinhasadFaithEnableIndex";
	private byte[] _byte = null;
	public static final int ENABLE	= 0x0a7c;
	
	public static final S_EinhasadFaithEnableIndex FAIL_NEED_REFRESH		= new S_EinhasadFaithEnableIndex(EinhasadFaithResultCode.EINHASAD_FAITH_FAIL_NEED_REFRESH);
	public static final S_EinhasadFaithEnableIndex FAIL_WRONG_REQUEST		= new S_EinhasadFaithEnableIndex(EinhasadFaithResultCode.EINHASAD_FAITH_FAIL_WRONG_REQUEST);
	public static final S_EinhasadFaithEnableIndex FAIL_IS_NOT_GAMESERVER	= new S_EinhasadFaithEnableIndex(EinhasadFaithResultCode.EINHASAD_FAITH_FAIL_IS_NOT_GAMESERVER);
	
	public S_EinhasadFaithEnableIndex(EinhasadFaithResultCode resultCode) {
		write_init();
		write_resultCode(resultCode);
		writeH(0x00);
	}
	
	public S_EinhasadFaithEnableIndex(EinhasadFaithInfoStream.FaithInfoType type, int groupId, int indexId, Timestamp expiredTime) {
		write_init();
		write_resultCode(EinhasadFaithResultCode.EINHASAD_FAITH_SUCCESS);
		int remain = expiredTime == null ? 0 : (int)(expiredTime.getTime() / 1000);
		if (type == FaithInfoType.Index) {
			write_enableIndex(type, groupId, indexId, remain);
		} else {
			write_enableGroup(type, groupId, indexId, remain);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(ENABLE);
	}
	
	void write_resultCode(EinhasadFaithResultCode resultCode) {
		writeRaw(0x08);
		writeRaw(resultCode.toInt());
	}
	
	void write_enableIndex(EinhasadFaithInfoStream.FaithInfoType type, int groupId, int indexId, int expiredTime) {
		EinhasadFaithInfoStream os = null;
		try {
			os = new EinhasadFaithInfoStream(type, groupId, indexId, expiredTime);
			writeRaw(0x12);
			writeBytesWithLength(os.getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	void write_enableGroup(EinhasadFaithInfoStream.FaithInfoType type, int groupId, int indexId, int expiredTime) {
		EinhasadFaithInfoStream os = null;
		try {
			os = new EinhasadFaithInfoStream(type, groupId, indexId, expiredTime);
			writeRaw(0x1a);
			writeBytesWithLength(os.getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch(Exception e) {
					e.printStackTrace();
				}
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
		return S_EINHASAD_FAITH_ENABLE_INDEX;
	}
}

