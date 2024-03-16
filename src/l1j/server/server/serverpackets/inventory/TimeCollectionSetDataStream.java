package l1j.server.server.serverpackets.inventory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollection;
import l1j.server.server.model.item.collection.time.bean.L1TimeCollectionUser;
import l1j.server.server.utils.BinaryOutputStream;

public class TimeCollectionSetDataStream extends BinaryOutputStream {
	
	protected TimeCollectionSetDataStream() {
		super();
	}
	
	protected TimeCollectionSetDataStream(L1PcInstance pc, L1TimeCollection obj, L1TimeCollectionUser user) {
		super();
		write_setId(obj.getSet().get_ID());
		if (user != null) {
			write_useRecycle(0);
			ConcurrentHashMap<Integer, L1ItemInstance> slots = user.getSlots();
			if (slots != null && !slots.isEmpty()) {
				for (Map.Entry<Integer, L1ItemInstance> entry :  slots.entrySet()) {
					write_slotData(pc, entry.getKey(), entry.getValue());
				}
			}
			write_buffType(user.getBuffType().toInt());
			boolean isBuff = user.isBuffActive();
			write_completeTime(isBuff ? (int)(user.getBuffTime().getTime() / 1000) : 0);
			write_enchantSum(user.getSumEnchant());
			write_state(isBuff ? TIME_COLLECTION_STATE.TIME_COLLECTION_COMPLETE : TIME_COLLECTION_STATE.TIME_COLLECTION_NONE);
			if (obj.getSet().get_ExtraTimeId() > 0) {
				write_useRefill(user.getRefillCount());
			}
		}
	}
	
	protected void write_setId(int setId) {
		writeC(0x08);// setId
		writeC(setId);
	}
	
	protected void write_useRecycle(int useRecycle) {
		writeC(0x10);// useRecycle
		writeC(useRecycle);
	}
	
	protected void write_slotData(L1PcInstance pc, int slotNo, L1ItemInstance item) {
		TimeCollectionUserSlotDataStream os = null;
		try {
			os = new TimeCollectionUserSlotDataStream(pc, slotNo, item);
			writeC(0x1a);// slotData
			writeBytesWithLength(os.getBytes());
		} catch(Exception e) {
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
	
	protected void write_buffType(int buffType) {
		writeC(0x20);// buffType
		writeC(buffType);
	}
	
	protected void write_completeTime(int completeTime) {
		writeC(0x28);// completeTime
		writeBit(completeTime);
	}
	
	protected void write_enchantSum(int enchantSum) {
		writeC(0x30);// enchantSum
		writeBit(enchantSum);
	}
	
	protected void write_state(TIME_COLLECTION_STATE state) {
		writeC(0x38);// state
		writeC(state.toInt());
	}
	
	protected void write_useRefill(int useRefill) {
		writeC(0x40);// useRefill
		writeC(useRefill);
	}
	
}

