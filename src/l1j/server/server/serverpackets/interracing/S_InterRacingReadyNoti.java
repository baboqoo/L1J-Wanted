package l1j.server.server.serverpackets.interracing;

import l1j.server.common.data.RacerInfoT;
import l1j.server.common.data.RacerTicketT;
import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_InterRacingReadyNoti extends ServerBasePacket {
	private static final String S_INTER_RACING_READY_NOTI = "[S] S_InterRacingReadyNoti";
	private byte[] _byte = null;
	public static final int NOTI = 0x08fe;
	
	public S_InterRacingReadyNoti(RacerInfoT race, java.util.LinkedList<RacerInfoT> racer, 
			boolean canBuy, java.util.LinkedList<RacerTicketT> racerTicket, int remainWaitingTime){
		write_init();
		if (race != null) {
			write_race(race);
		}
		write_racer(racer);
		write_canBuy(canBuy);
		write_racerTicket(racerTicket);
		write_remainWaitingTime(remainWaitingTime);
        writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(NOTI);
	}
	
	void write_race(RacerInfoT race) {
		writeRaw(0x0a);
		writeBytesWithLength(get_RacerInfo(race));
	}
	
	void write_racer(java.util.LinkedList<RacerInfoT> racer) {
		if (racer == null || racer.isEmpty()) {
			return;
		}
		for (RacerInfoT race : racer) {
			writeRaw(0x12);
			writeBytesWithLength(get_RacerInfo(race));
		}
	}
	
	void write_canBuy(boolean canBuy) {
		writeRaw(0x18);
		writeB(canBuy);
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
	
	void write_remainWaitingTime(int remainWaitingTime) {
		writeRaw(0x28);
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
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_INTER_RACING_READY_NOTI;
	}
}

