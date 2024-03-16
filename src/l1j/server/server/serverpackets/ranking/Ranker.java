package l1j.server.server.serverpackets.ranking;

import l1j.server.server.controller.action.UserRanking;
import l1j.server.server.templates.L1UserRanking;
import l1j.server.server.utils.BinaryOutputStream;

public class Ranker extends BinaryOutputStream {
	
	public Ranker() {
		super();
	}
	
	public Ranker(L1UserRanking rank, int uid, int score) {
		super();
		int rating = UserRanking.getRankRating(rank.getCurRank());
		if (rating > 0) {
			write_rating(rating);
		}
		write_rank(rank.getCurRank());
		write_previous_rank(rank.getOldRank());
		write_class(rank.getClassId());
		write_name(rank.getName().getBytes());
		if (uid >= 0) {
			write_uid(uid);
		}
		if (score >= 0) {
			write_score(score);
		}
	}
	
	public Ranker(int rating, int rank, int previous_rank, int classid, byte[] name, int uid, int score) {
		super();
		if (rating > 0) {
			write_rating(rating);
		}
		write_rank(rank);
		write_previous_rank(previous_rank);
		write_class(classid);
		write_name(name);
		if (uid >= 0) {
			write_uid(uid);
		}
		if (score >= 0) {
			write_score(score);
		}
	}
	
	void write_rating(int rating) {
		writeC(0x08);// rating
		writeC(rating);
	}
	
	void write_rank(int rank) {
		writeC(0x10);// rank
		writeBit(rank);
	}
	
	void write_previous_rank(int previous_rank) {
		writeC(0x18);// previous_rank
		writeBit(previous_rank);
	}
	
	void write_class(int class_id) {
		writeC(0x20);// class
		writeC(class_id);
	}
	
	void write_name(byte[] name) {
		writeC(0x2A);// name
		writeBytesWithLength(name);
	}
	
	void write_uid(int uid) {
		writeC(0x30);// uid
		writeC(uid);
	}
	
	void write_score(int score) {
		writeC(0x38);// score
		writeC(score);
	}
	
	void write_class_rating(int class_rating) {
		writeC(0x40);// class_rating
		writeC(class_rating);
	}
	
	void write_server_no(int server_no) {
		writeC(0x48);// server_no
		writeBit(server_no);
	}
	
	void write_class_rank(int class_rank) {
		writeC(0x50);// class_rank
		writeBit(class_rank);
	}
}

