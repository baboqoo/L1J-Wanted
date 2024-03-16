package l1j.server.server.serverpackets;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.construct.L1ServerType;
import l1j.server.server.model.L1Clan;
import l1j.server.server.utils.BinaryOutputStream;

public class S_TeamIdServerNoMappingInfo extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_TEAM_ID_SERVER_NO_MAPPING_INFO = "[S] S_TeamIdServerNoMappingInfo";
	public static final int MAPPING						= 0x0336;
	
	//private static final int[] WORLD_WAR_TEAM_IDS	= { 51, 52, 59 };
	private static final int[] OCCUPY_TEAM_IDS		= { 401, 402, 403 };
	
	public static final S_TeamIdServerNoMappingInfo ON			= new S_TeamIdServerNoMappingInfo(true, false);
	public static final S_TeamIdServerNoMappingInfo OCCUPY_ON	= new S_TeamIdServerNoMappingInfo(true, true);
	public static final S_TeamIdServerNoMappingInfo OFF			= new S_TeamIdServerNoMappingInfo(false, false);
	
	// data file siegeemblem.json
	// 13, 20000 붉은기사단, 20001 팅
	// 51, 52, 59 월드 공성전
	// 101 ~ 110 / 2001 ~ 2010 지배의 탑 서버 혈맹
	// 401 붉은 기사단, 402 황금 성단, 403 검은 기사단, - 하이네 수호탑 점령전
	// 2011 붉은기사단, 2012 별, 2013 폭탄, 2014 물음표, 2015 투구, 2016 신규붉은기사단
	public S_TeamIdServerNoMappingInfo(boolean onoff, boolean occupy) {
		write_init();
		if (onoff) {
			int[] team_ids = occupy ? OCCUPY_TEAM_IDS : L1Clan.INTER_TEAM_IDS;
			for (int i=0; i<team_ids.length; i++) {
				write_mapping_info(L1ServerType.ARRAY[i].getId(), team_ids[i]);
			}
		}
		writeH(0x00);
	}
	
	// test
	public S_TeamIdServerNoMappingInfo(int team_id) {
		write_init();
		write_mapping_info(Config.VERSION.SERVER_NUMBER, team_id);
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(MAPPING);
	}
	
	void write_mapping_info(int server_no, int team_id) {
		writeC(0x0a);// mapping_info
		writeBytesWithLength(get_mapping_info(server_no, team_id));
	}
	
	byte[] get_mapping_info(int server_no, int team_id){
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			os.writeC(0x08);// server_no
			os.writeBit(server_no);
			
			os.writeC(0x10);// team_id
			os.writeBit(team_id);
			return os.getBytes();
		} catch(Exception e){
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
		return null;
	}
	
	public static void init(){}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_TEAM_ID_SERVER_NO_MAPPING_INFO;
	}
}
