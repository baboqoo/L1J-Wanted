package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_PlayMovieNoti extends ServerBasePacket {
	private static final String S_PLAY_MOIVE_NOTI = "[S] S_PlayMovieNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0247;
	
	public S_PlayMovieNoti(String movie_file, int play_time_sec) {
		write_init();
		write_movie_file(movie_file);
		write_play_time_sec(play_time_sec);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_movie_file(String movie_file) {
		writeRaw(0x0A);
		writeStringWithLength(movie_file);
	}
	
	void write_play_time_sec(int play_time_sec) {
		writeRaw(0x10);
		writeBit(play_time_sec);
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
		return S_PLAY_MOIVE_NOTI;
	}
}

