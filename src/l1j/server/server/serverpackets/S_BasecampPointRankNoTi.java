package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.utils.BinaryOutputStream;

public class S_BasecampPointRankNoTi extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_BASECAMP_POINTRANK_NOTI = "[S] S_BasecampPointRankNoTi";
	public static final int RANK	= 0x0085;
	
	public S_BasecampPointRankNoTi(java.util.LinkedList<S_BasecampPointRankNoTi.PointRankInfoT> top_rankers, S_BasecampPointRankNoTi.PointRankInfoT my_rank, long team_points) {
		write_init();
		if (top_rankers != null && !top_rankers.isEmpty()) {
			write_top_rankers(top_rankers);
		}
		if (my_rank != null) {
			write_my_rank(my_rank);
		}
		write_team_points(team_points);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(RANK);
	}
	
	void write_top_rankers(java.util.LinkedList<S_BasecampPointRankNoTi.PointRankInfoT> top_rankers) {
		for (S_BasecampPointRankNoTi.PointRankInfoT val : top_rankers) {
			writeRaw(0x0a);
			writeBytesWithLength(get_rank(val));
		}
	}
	
	void write_my_rank(S_BasecampPointRankNoTi.PointRankInfoT my_rank) {
		writeRaw(0x12);
		writeBytesWithLength(get_rank(my_rank));
	}
	
	void write_team_points(long team_points) {
		writeRaw(0x18);
		writeBit(team_points);
	}
	
	byte[] get_rank(S_BasecampPointRankNoTi.PointRankInfoT rank){
		BinaryOutputStream os = new BinaryOutputStream();
		try {
			os.writeC(0x08);// user_rank
			os.writeBit(rank._user_rank);
			
			os.writeC(0x12);// user_name
			os.writeBytesWithLength(rank._user_name);
				
			os.writeC(0x18);// user_points
			os.writeBit(rank._user_points);
			return os.getBytes();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(os != null){
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static class PointRankInfoT {
		private int _user_rank;
		private byte[] _user_name;
		private long _user_points;
		public PointRankInfoT(int _user_rank, byte[] _user_name, long _user_points) {
			this._user_rank = _user_rank;
			this._user_name = _user_name;
			this._user_points = _user_points;
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
		return S_BASECAMP_POINTRANK_NOTI;
	}
}
