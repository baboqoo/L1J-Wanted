package l1j.server.server.serverpackets.ranking;

import java.util.List;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.controller.action.UserRanking;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1UserRanking;

public class S_TopRanker extends ServerBasePacket {
	private static final String S_TOP_RANKER = "[S] S_TopRanker";
	private byte[] _byte = null;
	public static final int TOP_RANK	= 0x0088;
	
	public static final S_TopRanker NOW_NOT_SERVICE	= new S_TopRanker(null, 0, 0, 0, 0, S_TopRanker.RankResultCode.RC_NOW_NOT_SERVICE);
	public static final S_TopRanker UNKNOWN_ERROR	= new S_TopRanker(null, 0, 0, 0, 0, S_TopRanker.RankResultCode.RC_UNKNOWN_ERROR);

	public S_TopRanker(List<L1UserRanking> list, int class_id, int total_page, int current_page, long version, S_TopRanker.RankResultCode result) {
		write_init();
		write_result_code(result);
		write_version(version);
		if (result == S_TopRanker.RankResultCode.RC_SUCCESS) {
			write_class_id(class_id);
			write_total_page(total_page);
			write_current_page(current_page);
			
			int server_no = Config.VERSION.SERVER_NUMBER;
			if (list != null && !list.isEmpty()) {
				Ranker ranker = null;
				for (L1UserRanking user : list) {
					try {
						ranker = new Ranker();
						write_rankers(ranker, user, class_id, server_no);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							if (ranker != null) {
								ranker.close();
								ranker = null;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(TOP_RANK);
	}
	
	void write_result_code(S_TopRanker.RankResultCode result_code) {
		writeRaw(0x08);// result_code
		writeRaw(result_code.value);
	}
	
	void write_version(long version) {
		writeRaw(0x10);// version
		writeBit(version);
	}
	
	void write_class_id(int class_id) {
		writeRaw(0x18);// class_id
		writeRaw(class_id);
	}
	
	void write_total_page(int total_page) {
		writeRaw(0x20);// total_page
		writeRaw(total_page);
	}
	
	void write_current_page(int current_page) {
		writeRaw(0x28);// current_page
		writeRaw(current_page);
	}
	
	void write_rankers(Ranker ranker, L1UserRanking rank, int class_id, int server_no) {
		byte[] name			= rank.getName().getBytes();
		int all_rank		= rank.getCurRank();
		int previous_rank	= rank.getOldRank();
		int rating			= class_id == 10 ? UserRanking.getRankRating(all_rank) : UserRanking.getTotalRating(rank.getName());
		int class_rating	= class_id == 10 ? 0 : UserRanking.getClassRankRating(all_rank);
		if (rating > 0) {
			ranker.write_rating(rating);
		}
		ranker.write_rank(all_rank);
		ranker.write_previous_rank(previous_rank);
		ranker.write_class(rank.getClassId());
		ranker.write_name(name);
		if (class_id != L1CharacterInfo.CLASS_SIZE) {
			ranker.write_class_rating(class_rating);
		}
		ranker.write_server_no(server_no);
		
		writeRaw(0x32);// rankers
		writeBytesWithLength(ranker.getBytes());
	}
	
	public enum RankResultCode {
		RC_SUCCESS(0),
		RC_NO_CHANGE(1),
		RC_NOW_NOT_SERVICE(2),
		RC_UNKNOWN_ERROR(100);
		private int value;
		RankResultCode(int val) {
			value = val;
		}
		public int toInt() {
			return value;
		}
		public boolean equals(RankResultCode v) {
			return value == v.value;
		}
		public static RankResultCode fromInt(int i) {
			switch (i) {
			case 0:
				return RC_SUCCESS;
			case 1:
				return RC_NO_CHANGE;
			case 2:
				return RC_NOW_NOT_SERVICE;
			case 100:
				return RC_UNKNOWN_ERROR;
			default:
				return null;
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
		return S_TOP_RANKER;
	}

}
