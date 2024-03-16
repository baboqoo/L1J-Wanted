package l1j.server.server.serverpackets.einhasadpoint;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.einhasadpoint.EinhasadFaithInfoStream.FaithInfoType;

public class S_EinhasadFaithDisableIndexNoti extends ServerBasePacket {
	private static final String S_EINHASAD_FAITH_DISABLE_INDEX_NOTI = "[S] S_EinhasadFaithDisableIndexNoti";
	private byte[] _byte = null;
	public static final int DISABLE	= 0x0a7d;
	
	public static final S_EinhasadFaithDisableIndexNoti FAIL_NEED_REFRESH		= new S_EinhasadFaithDisableIndexNoti(EinhasadFaithResultCode.EINHASAD_FAITH_FAIL_NEED_REFRESH);
	public static final S_EinhasadFaithDisableIndexNoti FAIL_WRONG_REQUEST		= new S_EinhasadFaithDisableIndexNoti(EinhasadFaithResultCode.EINHASAD_FAITH_FAIL_WRONG_REQUEST);
	public static final S_EinhasadFaithDisableIndexNoti FAIL_IS_NOT_GAMESERVER	= new S_EinhasadFaithDisableIndexNoti(EinhasadFaithResultCode.EINHASAD_FAITH_FAIL_IS_NOT_GAMESERVER);
	
	public S_EinhasadFaithDisableIndexNoti(EinhasadFaithResultCode resultCode) {
		write_init();
		write_resultCode(resultCode);
		writeH(0x00);
	}
	
	public S_EinhasadFaithDisableIndexNoti(EinhasadFaithInfoStream.FaithInfoType type, int groupId, int indexId) {
		write_init();
		write_resultCode(EinhasadFaithResultCode.EINHASAD_FAITH_SUCCESS);
		if (type == FaithInfoType.Index) {
			write_disableIndex(type, groupId, indexId, 0);
		} else {
			write_disableGroup(type, groupId, indexId, 0);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(DISABLE);
	}
	
	void write_resultCode(EinhasadFaithResultCode resultCode) {
		writeRaw(0x08);
		writeRaw(resultCode.toInt());
	}
	
	void write_disableIndex(EinhasadFaithInfoStream.FaithInfoType type, int groupId, int indexId, int expiredTime) {
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
	
	void write_disableGroup(EinhasadFaithInfoStream.FaithInfoType type, int groupId, int indexId, int expiredTime) {
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
		return S_EINHASAD_FAITH_DISABLE_INDEX_NOTI;
	}
}

