package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.GameSystem.dungeontimer.L1DungeonTimer;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerInfo;
import l1j.server.GameSystem.dungeontimer.bean.L1DungeonTimerUser;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.StringUtil;

public class S_UserPlayInfoNoti extends ServerBasePacket {
	private static final String S_UER_PLAY_INFO_NOTI = "[S] S_UserPlayInfoNoti";
	private byte[] _byte = null;
	public static final int PLAY_INFO = 0x0323;
	
	public S_UserPlayInfoNoti(L1PcInstance pc) {
		L1DungeonTimerInfo info = null;
		L1DungeonTimer handler	= pc.getDungoenTimer();
		write_init();
		for (L1DungeonTimerUser timer : handler.getTimers().values()) {
			if (timer.getTimerId() == 3) {// 버림받은 자들의 땅(PC) 제외
				continue;
			}
			info = timer.getInfo();
			if (info.getMaxChargeCount() > 0) {
				write_charged_time_map_info_group(info, timer, handler);
				continue;
			}
			if (info.getSerialId() == 0 || StringUtil.isNullOrEmpty(info.getSerialDescId())) {
				continue;
			}
			write_map_time_limit_info(info, timer, handler);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(PLAY_INFO);
	}
	
	void write_map_time_limit_info(L1DungeonTimerInfo info, L1DungeonTimerUser timer, L1DungeonTimer handler) {
		int time_limit_stay		= handler.getTimerValue(info);
		int time_remained		= time_limit_stay - timer.getRemainSecond();
		MAP_TIME_LIMIT_INFO os = new MAP_TIME_LIMIT_INFO();
		os.write_time_limit_serial(info.getSerialId());
		os.write_description(info.getSerialDescId().getBytes());
		os.write_time_remained(time_remained);
		os.write_time_limit_stay(time_limit_stay);
		
		writeRaw(0x0a);// map_time_limit_info
		writeBytesWithLength(os.getBytes());
		try {
			os.close();
			os = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void write_charged_time_map_info(L1DungeonTimerInfo info, L1DungeonTimerUser timer, L1DungeonTimer handler) {
		int charged_count		= timer.getChargeCount();// 충전 횟수
		int maxTime				= handler.getTimerValue(info);
		int extra_charged_time	= maxTime - timer.getRemainSecond();
		int charged_time		= charged_count * 3600;
		if (extra_charged_time < 0) {
			charged_time		+= extra_charged_time;
			extra_charged_time	= 0;
		}
		CHARGED_TIME_MAP_INFO os = new CHARGED_TIME_MAP_INFO();
		os.write_charged_time(charged_time);
		os.write_charged_count(charged_count);
		os.write_max_charge_count(info.getMaxChargeCount());
		os.write_extra_charged_time(extra_charged_time);
		os.write_group_id(info.getGroup());
		
		writeRaw(0x12);// charged_time_map_info
		writeBytesWithLength(os.getBytes());
		try {
			os.close();
			os = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void write_charged_time_map_info_group(L1DungeonTimerInfo info, L1DungeonTimerUser timer, L1DungeonTimer handler) {
		int charged_count		= timer.getChargeCount();// 충전 횟수
		int maxTime				= handler.getTimerValue(info);
		int extra_charged_time	= maxTime - timer.getRemainSecond();
		int charged_time		= charged_count * 3600;
		if (extra_charged_time < 0) {
			charged_time		+= extra_charged_time;
			extra_charged_time	= 0;
		}
		CHARGED_TIME_MAP_INFO os = new CHARGED_TIME_MAP_INFO();
		os.write_charged_time(charged_time);
		os.write_charged_count(charged_count);
		os.write_max_charge_count(info.getMaxChargeCount());
		os.write_extra_charged_time(extra_charged_time);
		os.write_group_id(info.getGroup());
		
		writeRaw(0x1a);// charged_time_map_info_group
		writeBytesWithLength(os.getBytes());
		try {
			os.close();
			os = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static class MAP_TIME_LIMIT_INFO extends BinaryOutputStream {
		public MAP_TIME_LIMIT_INFO() {
			super();
		}
		
		void write_time_limit_serial(int time_limit_serial) {
			writeC(0x08);// time_limit_serial
			writeC(time_limit_serial);
		}
		
		void write_description(byte[] description) {
			writeC(0x12);// description
			writeBytesWithLength(description);
		}
		
		void write_time_remained(int time_remained) {
			writeC(0x18);// time_remained
			writeBit(time_remained);
		}
		
		void write_time_limit_stay(int time_limit_stay) {
			writeC(0x20);// time_limit_stay
			writeBit(time_limit_stay);
		}
	}
	
	public static class CHARGED_TIME_MAP_INFO extends BinaryOutputStream {
		public CHARGED_TIME_MAP_INFO() {
			super();
		}
		
		void write_charged_time(int charged_time) {
			writeC(0x08);// charged_time
			writeBit(charged_time);
		}
		
		void write_charged_count(int charged_count) {
			writeC(0x10);// charged_count
			writeC(charged_count);
		}
		
		void write_max_charge_count(int max_charge_count) {
			writeC(0x18);// max_charge_count
			writeC(max_charge_count);
		}
		
		void write_extra_charged_time(int extra_charged_time) {
			writeC(0x20);// extra_charged_time
			writeBit(extra_charged_time);
		}
		
		void write_group_id(S_UserPlayInfoNoti.eChargedTimeMapGroup group_id) {
			writeC(0x28);// group_id
			writeC(group_id.toInt());
		}
	}
	
	
	public enum eChargedTimeMapGroup{
		NONE(0),
		HIDDEN_FIELD(1),
		SILVER_KNIGHT_DUNGEON(2),
		HIDDEN_FIELD_BOOST(3),
		;
		private int value;
		eChargedTimeMapGroup(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eChargedTimeMapGroup v){
			return value == v.value;
		}
		public static eChargedTimeMapGroup fromInt(int i){
			switch(i){
			case 0:
				return NONE;
			case 1:
				return HIDDEN_FIELD;
			case 2:
				return SILVER_KNIGHT_DUNGEON;
			case 3:
				return HIDDEN_FIELD_BOOST;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eChargedTimeMapGroup, %d", i));
			}
		}
		public static eChargedTimeMapGroup fromString(String str){
			switch(str){
			case "NONE":
				return NONE;
			case "HIDDEN_FIELD":
				return HIDDEN_FIELD;
			case "SILVER_KNIGHT_DUNGEON":
				return SILVER_KNIGHT_DUNGEON;
			case "HIDDEN_FIELD_BOOST":
				return HIDDEN_FIELD_BOOST;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eChargedTimeMapGroup, %s", str));
			}
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
		return S_UER_PLAY_INFO_NOTI;
	}
}
