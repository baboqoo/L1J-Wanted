package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_ThebeCaptureInfoNoti extends ServerBasePacket {
	private static final String S_THEBE_CAPTURE_INFO_NOTI = "[S] S_ThebeCaptureInfoNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x0086;
	
	public S_ThebeCaptureInfoNoti(java.util.LinkedList<S_ThebeCaptureInfoNoti.CapturePointT> points, int remain_time_for_next_capture_event){
		write_init();
		if (points != null && !points.isEmpty()) {
			write_points(points);
		}
		write_remain_time_for_next_capture_event(remain_time_for_next_capture_event);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_points(java.util.LinkedList<S_ThebeCaptureInfoNoti.CapturePointT> points) {
		for (S_ThebeCaptureInfoNoti.CapturePointT point : points) {
			writeRaw(0x0a);
			writeRaw(getBitSize(point._team_id) + getBitSize(point._capture_point) + getBitSize(point._homeserverno) + 3);
			
			writeRaw(0x08);// team_id
			writeBit(point._team_id);
			
			writeRaw(0x10);// capture_point
			writeBit(point._capture_point);
			
			writeRaw(0x18);// homeserverno
			writeBit(point._homeserverno);
		}
	}
	
	void write_remain_time_for_next_capture_event(int remain_time_for_next_capture_event) {
		writeRaw(0x10);
		writeBit(remain_time_for_next_capture_event);
	}
	
	public static class CapturePointT {
		private int _team_id;
		private int _capture_point;
		private int _homeserverno;
		public CapturePointT(int _team_id, int _capture_point, int _homeserverno) {
			this._team_id = _team_id;
			this._capture_point = _capture_point;
			this._homeserverno = _homeserverno;
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
		return S_THEBE_CAPTURE_INFO_NOTI;
	}
}

