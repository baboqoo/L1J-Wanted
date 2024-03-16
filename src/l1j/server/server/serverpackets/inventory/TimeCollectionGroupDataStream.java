package l1j.server.server.serverpackets.inventory;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.L1TimeCollectionHandler;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.utils.BinaryOutputStream;

public class TimeCollectionGroupDataStream extends BinaryOutputStream {
	
	protected TimeCollectionGroupDataStream(L1PcInstance pc, L1TimeCollectionHandler handler, int groupId, ConcurrentHashMap<Integer, L1TimeCollection> set_list) {
		super();
		write_groupId(groupId);
		for (L1TimeCollection obj : set_list.values()) {
			write_setData(pc, obj, handler.getUser(obj.getSet().get_ID()));
		}
	}
	
	void write_groupId(int groupId) {
		writeC(0x08);// groupId
		writeC(groupId);
	}
	
	void write_setData(L1PcInstance pc, L1TimeCollection obj, L1TimeCollectionUser user) {
		TimeCollectionSetDataStream os = null;
		try {
			os = new TimeCollectionSetDataStream(pc, obj, user);
			writeC(0x12);// setData
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
	
}

