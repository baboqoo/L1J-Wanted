package l1j.server.server.serverpackets.interracing;

import l1j.server.common.data.RacerTicketT;
import l1j.server.server.utils.BinaryOutputStream;

public class RacerTicketStream extends BinaryOutputStream {
	
	public RacerTicketStream(RacerTicketT ticket) {
		super();
		write_laneId(ticket.get_laneId());
		write_name(ticket.get_name());
		write_condition(ticket.get_condition());
		write_winRate(ticket.get_winRate());
		write_price(ticket.get_price());
		write_racerId(ticket.get_racerId());
	}
	
	void write_laneId(int laneId) {
		writeC(0x08);
		writeBit(laneId);
	}
	
	void write_name(String name) {
		writeC(0x12);
		writeStringWithLength(name);
	}
	
	void write_condition(int condition) {
		writeC(0x18);
		writeBit(condition);
	}
	
	void write_winRate(double winRate) {
		writeC(0x21);
		writeDouble(winRate);
	}
	
	void write_price(int price) {
		writeC(0x28);
		writeBit(price);
	}
	
	void write_racerId(int racerId) {
		writeC(0x30);
		writeBit(racerId);
	}
}

