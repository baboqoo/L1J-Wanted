package l1j.server.server.serverpackets.einhasadpoint;

import l1j.server.GameSystem.einhasadfaith.bean.EinhasadFaithInfo;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_EinhasadFaithListNoti extends ServerBasePacket {
	private static final String S_EINHASAD_FAITH_LIST_NOTI = "[S] S_EinhasadFaithListNoti";
	private byte[] _byte = null;
	public static final int LIST	= 0x0a79;
	
	public S_EinhasadFaithListNoti(java.util.LinkedList<EinhasadFaithInfo> faithInfos) {
		write_init();
		write_faithInfoList(faithInfos);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(LIST);
	}
	
	void write_faithInfoList(java.util.LinkedList<EinhasadFaithInfo> faithInfos) {
		for (EinhasadFaithInfo info : faithInfos) {
			int remain = info.getExpiredTime() == null ? 0 : (int)(info.getExpiredTime().getTime() / 1000);
			writeRaw(0x0a);
			writeBytesWithLength(getFaithInfo(
					info.getIndexId() == 0 ? EinhasadFaithInfoStream.FaithInfoType.Group : EinhasadFaithInfoStream.FaithInfoType.Index, 
					info.getGroupId(), 
					info.getIndexId(), 
					remain));
		}
	}
	
	byte[] getFaithInfo(EinhasadFaithInfoStream.FaithInfoType type, int groupId, int indexId, int expiredTime){
		EinhasadFaithInfoStream os = null;
		try {
			os = new EinhasadFaithInfoStream(type, groupId, indexId, expiredTime);
			return os.getBytes();
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
		return null;
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
		return S_EINHASAD_FAITH_LIST_NOTI;
	}
}

