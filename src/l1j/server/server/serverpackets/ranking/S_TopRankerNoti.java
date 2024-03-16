package l1j.server.server.serverpackets.ranking;

import l1j.server.server.Opcodes;
import l1j.server.server.controller.action.UserRanking;
import l1j.server.server.model.L1CharacterConfig;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1UserRanking;

public class S_TopRankerNoti extends ServerBasePacket {
	private static final String S_TOP_RANKER_NOTI = "[S] S_TopRankerNoti";
	private byte[] _byte = null;
	public static final int TOP_RANKER_NOTI	= 0x0089;

	public S_TopRankerNoti(L1UserRanking rank) {
		L1UserRanking classRank = UserRanking.getClassRank(rank.getClassId(), rank.getName());
		Ranker total_ranker = new Ranker(rank, 0, 0);
		Ranker class_ranker = null;
		
		write_init();
		write_total_ranker(total_ranker);
		if (classRank != null) {
			class_ranker = new Ranker(classRank, -1, -1);
			write_classRank(class_ranker);
		}
		write_almost_upper_total(false);
		write_almost_lower_total(false);
		write_almost_upper_class(false);
		write_almost_lower_class(false);
		writeH(0x00);
		
		try {
			if (total_ranker != null) {
				total_ranker.close();
				total_ranker = null;
			}
			if (class_ranker != null) {
				class_ranker.close();
				class_ranker = null;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public S_TopRankerNoti(L1PcInstance pc) {
		Ranker total_ranker = null;
		Ranker class_ranker = null;
		L1CharacterConfig config = pc.getConfig();
		if (config.get_all_rank() > 0) {
			byte[] name = pc.getName().getBytes();
			total_ranker = new Ranker(config.get_ranker_rating(), config.get_all_rank(), config.get_previous_all_rank(), pc.getType(), name, 0, 0);
			class_ranker = new Ranker(config.get_class_ranker_rating(), config.get_class_rank(), config.get_previous_class_rank(), pc.getType(), name, -1, -1);
		}
		
		write_init();
		if (total_ranker != null) {
			write_total_ranker(total_ranker);
		}
		if (class_ranker != null) {
			write_classRank(class_ranker);
		}
		write_almost_upper_total(false);
		write_almost_lower_total(false);
		write_almost_upper_class(false);
		write_almost_lower_class(false);
		writeH(0x00);
		
		try {
			if (total_ranker != null) {
				total_ranker.close();
				total_ranker = null;
			}
			if (class_ranker != null) {
				class_ranker.close();
				class_ranker = null;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(TOP_RANKER_NOTI);
	}
	
	void write_total_ranker(Ranker total_ranker) {
		writeC(0x0A);
		writeBytesWithLength(total_ranker.getBytes());
	}
	
	void write_classRank(Ranker class_ranker) {
		writeC(0x12);
		writeBytesWithLength(class_ranker.getBytes());
	}
	
	void write_almost_upper_total(boolean almost_upper_total) {
		writeC(0x18);// almost_upper_total
		writeB(almost_upper_total);
	}
	
	void write_almost_lower_total(boolean almost_lower_total) {
		writeC(0x20);// almost_lower_total
		writeB(almost_lower_total);
	}
	
	void write_almost_upper_class(boolean almost_upper_class) {
		writeC(0x28);// almost_upper_class
		writeB(almost_upper_class);
	}
	
	void write_almost_lower_class(boolean almost_lower_class) {
		writeC(0x30);// almost_lower_class
		writeB(almost_lower_class);
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
		return S_TOP_RANKER_NOTI;
	}

}
