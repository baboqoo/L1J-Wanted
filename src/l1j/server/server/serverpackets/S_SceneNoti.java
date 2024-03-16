package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SceneNoti extends ServerBasePacket {
	private static final String S_SCENE_NOTI = "[S] S_SceneNoti";
	private byte[] _byte = null;
	public static final int SCENE = 0x0098;
	
	public static enum ScriptName {
		ASTAROT_WAKE("astarot_wake"),
		BAPHOMET_WAKE("baphomet_wake"),
		BARLOG_WAKE("barlog_wake"),
		BIGDRAKE_WAKE("bigdrake_wake"),
		DEMON_WAKE("demon_wake"),
		DK_WAKE("dk_wake"),
		HIDDEN_WAKE("hidden_wake"),
		HIDDEN_WAKE1("hidden_wake1"),
		KING_WAKE("king_wake"),
		MAINFOG("mainfog"),
		PBARLOG_WAKE("Pbarlog_wake"),
		PHOENIX_WAKE("phoenix_wake"),
		REAPER_WAKE("reaper_wake"),
		STAGE2_WAKE("stage2"),
		STAGE3_WAKE("stage3"),
		STAGE4_WAKE("stage4"),
		STAGE5_WAKE("stage5"),
		CT_BOSS("CT_boss"),
		CT_LASTBOSS2("CT_lastboss2"),
		CT_LASTBOSS3("CT_lastboss3"),
		CT_NIGHTMARE_BOSS("CT_nightmare_boss"),
		CT_FOG("CT_fog"),
		KIKAM_FULL("54map_kikam_full"),
		KIKAM_SMOKE("54map_smoke"),
		KIKAM_BOSS("54map_kikam_boss"),
		MAP54_DISABLE("54map_disable");
		private String _value;
		ScriptName(String val) {
			_value = val;
		}
		
		@Override
		public String toString(){
			return _value;
		}
	}
	
	public static final S_SceneNoti ASTAROT_WAKE			= new S_SceneNoti(ScriptName.ASTAROT_WAKE._value, true, true);
	public static final S_SceneNoti BAPHOMET_WAKE			= new S_SceneNoti(ScriptName.BAPHOMET_WAKE._value, true, true);
	public static final S_SceneNoti BARLOG_WAKE				= new S_SceneNoti(ScriptName.BARLOG_WAKE._value, true, true);
	public static final S_SceneNoti BIGDRAKE_WAKE			= new S_SceneNoti(ScriptName.BIGDRAKE_WAKE._value, true, true);
	public static final S_SceneNoti DEMON_WAKE				= new S_SceneNoti(ScriptName.DEMON_WAKE._value, true, true);
	public static final S_SceneNoti DK_WAKE					= new S_SceneNoti(ScriptName.DK_WAKE._value, true, true);
	public static final S_SceneNoti HIDDEN_WAKE				= new S_SceneNoti(ScriptName.HIDDEN_WAKE._value, true, true);
	public static final S_SceneNoti HIDDEN_WAKE1			= new S_SceneNoti(ScriptName.HIDDEN_WAKE1._value, true, true);
	public static final S_SceneNoti KING_WAKE				= new S_SceneNoti(ScriptName.KING_WAKE._value, true, true);
	public static final S_SceneNoti MAINFOG					= new S_SceneNoti(ScriptName.MAINFOG._value, true, true);
	public static final S_SceneNoti PBARLOG_WAKE			= new S_SceneNoti(ScriptName.PBARLOG_WAKE._value, true, true);
	public static final S_SceneNoti PHOENIX_WAKE			= new S_SceneNoti(ScriptName.PHOENIX_WAKE._value, true, true);
	public static final S_SceneNoti REAPER_WAKE				= new S_SceneNoti(ScriptName.REAPER_WAKE._value, true, true);
	public static final S_SceneNoti STAGE2_WAKE				= new S_SceneNoti(ScriptName.STAGE2_WAKE._value, true, true);
	public static final S_SceneNoti STAGE3_WAKE				= new S_SceneNoti(ScriptName.STAGE3_WAKE._value, true, true);
	public static final S_SceneNoti STAGE4_WAKE				= new S_SceneNoti(ScriptName.STAGE4_WAKE._value, true, true);
	public static final S_SceneNoti STAGE5_WAKE				= new S_SceneNoti(ScriptName.STAGE5_WAKE._value, true, true);
	
	public static final S_SceneNoti CT_BOSS_ENABLE			= new S_SceneNoti(true, ScriptName.CT_BOSS._value, 0, 0, 0);
	public static final S_SceneNoti CT_LASTBOSS2_ENABLE		= new S_SceneNoti(true, ScriptName.CT_LASTBOSS2._value, 0, 0, 0);
	public static final S_SceneNoti CT_LASTBOSS3_ENABLE		= new S_SceneNoti(true, ScriptName.CT_LASTBOSS3._value, 0, 0, 0);
	public static final S_SceneNoti CT_NIGHTMARE_ENABLE		= new S_SceneNoti(true, ScriptName.CT_NIGHTMARE_BOSS._value, 0, 0, 0);
	public static final S_SceneNoti CT_FOG_ENABLE			= new S_SceneNoti(true, ScriptName.CT_FOG._value, 0, 0, 0);
	
	public static final S_SceneNoti CT_BOSS_DISABLE			= new S_SceneNoti(false, ScriptName.CT_BOSS._value, 0, 0, 0);
	public static final S_SceneNoti CT_LASTBOSS2_DISABLE	= new S_SceneNoti(false, ScriptName.CT_LASTBOSS2._value, 0, 0, 0);
	public static final S_SceneNoti CT_LASTBOSS3_DISABLE	= new S_SceneNoti(false, ScriptName.CT_LASTBOSS3._value, 0, 0, 0);
	public static final S_SceneNoti CT_NIGHTMARE_DISABLE	= new S_SceneNoti(false, ScriptName.CT_NIGHTMARE_BOSS._value, 0, 0, 0);
	public static final S_SceneNoti CT_FOG_DISABLE			= new S_SceneNoti(false, ScriptName.CT_FOG._value, 0, 0, 0);
	
	public static final S_SceneNoti KIKAM_FULL				= new S_SceneNoti(false, ScriptName.KIKAM_FULL._value, 0, 0, 0);
	public static final S_SceneNoti KIKAM_SMOKE_STOP		= new S_SceneNoti(false, ScriptName.KIKAM_SMOKE._value, 0, 0, 0);
	public static final S_SceneNoti KIKAM_BOSS_STOP			= new S_SceneNoti(false, ScriptName.KIKAM_BOSS._value, 0, 0, 0);
	public static final S_SceneNoti MAP54_DISABLE_END		= new S_SceneNoti(true, ScriptName.MAP54_DISABLE._value, 0, 0, 0);
	public static final S_SceneNoti KIKAM_FULL_END			= new S_SceneNoti(true, ScriptName.KIKAM_FULL._value, 0, 0, 0);
	
	public static final S_SceneNoti DISABLE_73600000		= new S_SceneNoti(false, 0, 73600000, 0, 0);
	public static final S_SceneNoti DISABLE_73600001		= new S_SceneNoti(false, 0, 73600001, 0, 0);
	public static final S_SceneNoti DISABLE_73600002		= new S_SceneNoti(false, 0, 73600002, 0, 0);
	public static final S_SceneNoti DISABLE_73600003		= new S_SceneNoti(false, 0, 73600003, 0, 0);
	public static final S_SceneNoti DISABLE_73600004		= new S_SceneNoti(false, 0, 73600004, 0, 0);
	public static final S_SceneNoti DISABLE_73600005		= new S_SceneNoti(false, 0, 73600005, 0, 0);
	public static final S_SceneNoti DISABLE_73600006		= new S_SceneNoti(false, 0, 73600006, 0, 0);
	public static final S_SceneNoti DISABLE_73600007		= new S_SceneNoti(false, 0, 73600007, 0, 0);
	
	// script name string type
	// astarot_wake, baphomet_wake, barlog_wake, bigdrake_wake, demon_wake, dk_wake
	// hidden_wake, hidden_wake1, king_wake, mainfog, Pbarlog_wake, phoenix_wake
	// reaper_wake, stage2, stage3, stage4, stage5
	// 54map_disable, 54map_kikam_boss, 54map_kikam_full, 54map_smoke
	// CT_boss, CT_fog, CT_lastboss2, CT_lastboss3, CT_nightmare_boss
	
	// script name integer type
	// 540000, 540001, 540002, 540003, 540004, 540005, 540006, 540007, 540008, 540009
	// 54900000 낚시터 맵 이벤트
	// 55570000 ~ 55570022 무한대전
	// 73600000 빨간 배리어
	// 73600001 흔들림
	// 73600002 아우라키아 정원 맵변화
	// 73600003
	// 73600004 아우라키아 구속구
	// 73600005 아우라키아 구속구 대미지 1단계
	// 73600006 아우라키아 구속구 대미지 2단계
	// 73600007 아우라키아 구속구 대미지 3단계
	
	public S_SceneNoti(String value, boolean is_enable, boolean type) {
		write_init();
		if (!type) {
			write_scene_id(value);
			write_enable(is_enable);
		} else {
			write_enable(is_enable);
			write_script_name(value);
		}
		writeH(0x00);
	}
	
	public S_SceneNoti(boolean is_enable, String script_name, long script_start_time, int pos_x, int pos_y) {
		write_init();
		if (is_enable) {
			write_script_name(script_name);
		} else {
			write_disable_script_name(script_name);
		}
		if (script_start_time > 0) {
			write_region(null);
			write_script_start_time(script_start_time);
			if (pos_x > 0 && pos_y > 0) {
				write_pos_x(pos_x);
				write_pos_y(pos_y);
			}
		}
		writeH(0x00);
	}
	
	public S_SceneNoti(boolean is_enable, long script_start_time, int script_number, int pos_x, int pos_y) {
		write_init();
		if (is_enable) {
			write_region(null);
			write_script_start_time(script_start_time);
			if (pos_x > 0 && pos_y > 0) {
				write_pos_x(pos_x);
				write_pos_y(pos_y);
			}
			write_script_number(script_number);
		} else {
			write_disable_script_number(script_number);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SCENE);
	}
	
	void write_scene_id(String scene_id) {
		writeRaw(0x0a);// scene_id
		writeStringWithLength(scene_id);
	}
	
	void write_enable(boolean enable) {
		writeRaw(0x10);// enable
		writeB(enable);
	}
	
	void write_script_name(String script_name) {
		writeRaw(0x1a);// script_name
		writeStringWithLength(script_name);
	}
	
	void write_disable_script_name(String disable_script_name) {
		writeRaw(0x22);// disable_script_name
		writeStringWithLength(disable_script_name);
	}
	
	void write_region(String region) {
		writeRaw(0x2a);// region
		writeStringWithLength(region);
	}
	
	void write_script_start_time(long script_start_time) {
		writeRaw(0x30);// script_start_time
		writeBit(script_start_time);
	}
	
	void write_pos_x(int pos_x) {
		writeRaw(0x38);// pos_x
		writeBit(pos_x);
	}
	
	void write_pos_y(int pos_y) {
		writeRaw(0x40);// pos_y
		writeBit(pos_y);
	}
	
	void write_script_number(int script_number) {
		writeRaw(0x48);// script_number
		writeBit(script_number);
	}
	
	void write_disable_script_number(int disable_script_number) {
		writeRaw(0x50);// disable_script_number
		writeBit(disable_script_number);
	}
	
	void write_object_id(int object_id) {
		writeRaw(0x58);// object_id
		writeBit(object_id);
	}
	
	void write_is_follow_object(boolean is_follow_object) {
		writeRaw(0x60);// is_follow_object
		writeB(is_follow_object);
	}
	
	void write_action_number(int action_number) {
		writeRaw(0x68);// action_number
		writeBit(action_number);
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
		return S_SCENE_NOTI;
	}
}

