package l1j.server.server.serverpackets;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.common.data.RegionT;
import l1j.server.server.Opcodes;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.utils.BinaryOutputStream;

public class S_WorldPut extends ServerBasePacket {
	private static final String S_WORLD_PUT = "[S] S_WorldPut";
	private byte[] _byte = null;
	public static final int WORLD = 0x0076;
	
	private static final ConcurrentHashMap<Integer, S_WorldPut> DATA = new ConcurrentHashMap<>();
	public static S_WorldPut get(L1Map map){
		S_WorldPut pck = DATA.get(map.getBaseMapId());
		if (pck == null) {
			pck = new S_WorldPut(map);
			DATA.put(map.getBaseMapId(), pck);
		}
		return pck;
	}

	private S_WorldPut(L1Map map) {
		write_init();
		write_worldnumber(map.getBaseMapId());
		write_homeserverno(Config.VERSION.SERVER_NUMBER);
		write_isunderwather(map.isUnderwater());
		write_isunderground(map.isDungeon());
		write_eventflag1(0);
		write_eventflag2(0);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(WORLD);
	}
	
	void write_worldnumber(int worldnumber) {
		writeRaw(0x08);// worldnumber
		writeBit(worldnumber);
	}
	
	void write_homeserverno(int homeserverno) {
		writeRaw(0x10);// homeserverno
		writeBit(homeserverno);
	}
	
	void write_isunderwather(boolean isunderwather) {
		writeRaw(0x18);// isunderwather
		writeB(isunderwather);
	}
	
	void write_isunderground(boolean isunderground) {
		writeRaw(0x20);
		writeB(isunderground);
	}
	
	void write_eventflag1(int eventflag1) {
		writeRaw(0x28);// eventflag1
		writeC(eventflag1);
	}
	
	void write_eventflag2(int eventflag2) {
		writeRaw(0x30);// eventflag2
		writeRaw(eventflag2);
	}
	
	void write_ghost_region(RegionT ghost_region) {
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			os.writeC(0x08);
			os.writeBit(ghost_region.get_OriginalMapID());
			
			os.writeC(0x10);
			os.writeBit(ghost_region.get_SX());
			
			os.writeC(0x18);
			os.writeBit(ghost_region.get_SY());
			
			os.writeC(0x20);
			os.writeBit(ghost_region.get_EX());
			
			os.writeC(0x28);
			os.writeBit(ghost_region.get_EY());
			
			os.writeC(0x30);
			os.writeBit(ghost_region.get_MapID());
			
			writeRaw(0x3a);// ghost_region
			writeBytesWithLength(os.getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
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
		return S_WORLD_PUT;
	}
}

