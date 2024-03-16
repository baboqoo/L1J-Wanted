package l1j.server.server.serverpackets.interracing;

import l1j.server.common.data.RacerInfoT;
import l1j.server.common.data.RacerMapViewDataT;
import l1j.server.server.utils.BinaryOutputStream;

public class RacerMapViewDataStream extends BinaryOutputStream {
	
	public RacerMapViewDataStream(RacerMapViewDataT view) {
		super();
		write_race(view.get_racerInfo());
		write_name(view.get_name());
		write_condition(view.get_condition());
	}
	
	void write_race(RacerInfoT race) {
		writeC(0x0a);
		writeBytesWithLength(get_RacerInfo(race));
	}
	
	void write_name(String name) {
		writeC(0x12);
		writeStringWithLength(name);
	}
	
	void write_condition(int condition) {
		writeC(0x18);
		writeBit(condition);
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
}

