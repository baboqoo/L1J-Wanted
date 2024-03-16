package l1j.server.server.serverpackets.inventory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.common.bin.TimeCollectionCommonBinLoader;
import l1j.server.common.data.NPCDialogInfoT;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.L1TimeCollectionHandler;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.loader.L1TimeCollectionLoader;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_TimeCollectionDataLoadNoti extends ServerBasePacket {
	private static final String S_TIME_COLLECTION_DATA_LOAD_NOTI = "[S] S_TimeCollectionDataLoadNoti";
	private byte[] _byte = null;
	public static final int DATA_LOAD	= 0x0a5f;
	
	public S_TimeCollectionDataLoadNoti(L1PcInstance pc, L1TimeCollectionHandler handler){
		write_init();
		
		ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, L1TimeCollection>> map = L1TimeCollectionLoader.getAllData();
		if (map != null && !map.isEmpty()) {
			ConcurrentHashMap<Integer, L1TimeCollection> value = null;
			for (Map.Entry<Integer, ConcurrentHashMap<Integer, L1TimeCollection>> entry : map.entrySet()) {
				value	= entry.getValue();
				if (value == null || value.isEmpty()) {
					continue;
				}
				write_groupData(pc, handler, entry.getKey(), value);
			}
		}
		
		NPCDialogInfoT npcInfoT = TimeCollectionCommonBinLoader.getData().get_NPCDialogInfo();
		if (npcInfoT != null) {
			for (NPCDialogInfoT.LinkerT linkerT : npcInfoT.get_Linker()) {
				L1NpcInstance npc = linkerT.get_Index() == 1 ? L1TimeCollectionLoader.TIME_COLLECTION_SHOP_NPC : L1TimeCollectionLoader.TIME_COLLECTION_CRAFT_NPC;
				if (npc == null) {
					continue;
				}
				write_npcInfo(linkerT.get_LinkReq(), linkerT.get_Index(), npc.getId());
			}
		}
		
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(DATA_LOAD);
	}
	
	void write_groupData(L1PcInstance pc, L1TimeCollectionHandler handler, int groupId, ConcurrentHashMap<Integer, L1TimeCollection> set_list) {
		TimeCollectionGroupDataStream os = null;
		try {
			os = new TimeCollectionGroupDataStream(pc, handler, groupId, set_list);
			writeRaw(0x0a);// groupData
			writeBytesWithLength(os.getBytes());
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
	}
	
	void write_npcInfo(int LinkReq, int index, int npcId) {
		TCNPCDialogInfoStream os = null;
		try {
			os = new TCNPCDialogInfoStream(LinkReq, index, npcId);
			writeRaw(0x12);// npcInfo
			writeBytesWithLength(os.getBytes());
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
		return S_TIME_COLLECTION_DATA_LOAD_NOTI;
	}
}

