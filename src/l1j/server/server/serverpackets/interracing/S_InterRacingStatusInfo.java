package l1j.server.server.serverpackets.interracing;

import l1j.server.common.data.RacerInfoT;
import l1j.server.common.data.RacerMapViewDataT;
import l1j.server.common.data.RacerTicketT;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_InterRacingStatusInfo extends ServerBasePacket {
	private static final String S_INTER_RACING_STATUS_INFO = "[S] S_InterRacingStatusInfo";
	private byte[] _byte = null;
	public static final int INFO = 0x0903;
	
	public S_InterRacingStatusInfo(RacerInfoT race, 
			boolean canBuy, boolean canSell,
			java.util.LinkedList<RacerTicketT> racerTicket,
			java.util.LinkedList<RacerMapViewDataT> racerMapView,
			int remainWaitingTime){
		write_init();
		write_race(race);
		write_canBuy(canBuy);
		write_canSell(canSell);
		write_racerTicket(racerTicket);
		write_racerMapView(racerMapView);
		write_remainWaitingTime(remainWaitingTime);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INFO);
	}
	
	void write_race(RacerInfoT race) {
		writeRaw(0x0a);
		writeBytesWithLength(get_RacerInfo(race));
	}
	
	void write_canBuy(boolean canBuy) {
		writeRaw(0x10);
		writeB(canBuy);
	}
	
	void write_canSell(boolean canSell) {
		writeRaw(0x18);
		writeB(canSell);
	}
	
	void write_racerTicket(java.util.LinkedList<RacerTicketT> racerTicket) {
		if (racerTicket == null || racerTicket.isEmpty()) {
			return;
		}
		for (RacerTicketT ticket : racerTicket) {
			writeRaw(0x22);
			writeBytesWithLength(get_racerTicket(ticket));
		}
	}
	
	void write_racerMapView(java.util.LinkedList<RacerMapViewDataT> racerMapView) {
		if (racerMapView == null || racerMapView.isEmpty()) {
			return;
		}
		for (RacerMapViewDataT view : racerMapView) {
			writeRaw(0x2a);
			writeBytesWithLength(get_racerMapView(view));
		}
	}
	
	void write_remainWaitingTime(int remainWaitingTime) {
		writeRaw(0x30);
		writeBit(remainWaitingTime);
	}
	
	byte[] get_RacerInfo(RacerInfoT race) {
		RacerInfoStream os = null;
		try {
			os = new RacerInfoStream(race);
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
	
	byte[] get_racerTicket(RacerTicketT ticket) {
		RacerTicketStream os = null;
		try {
			os = new RacerTicketStream(ticket);
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
	
	byte[] get_racerMapView(RacerMapViewDataT view) {
		RacerMapViewDataStream os = null;
		try {
			os = new RacerMapViewDataStream(view);
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
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_INTER_RACING_STATUS_INFO;
	}
}

