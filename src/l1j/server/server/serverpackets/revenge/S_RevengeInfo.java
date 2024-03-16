package l1j.server.server.serverpackets.revenge;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.RevengeTable;
import l1j.server.server.model.L1Revenge;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1RevengeTemp;

public class S_RevengeInfo extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_REVENGE_INFO = "[S] S_RevengeInfo";
	public static final int INFO	= 0x041c;
	
	public S_RevengeInfo(L1PcInstance pc) {
		write_init();
		write_result(eRevengeResult.SUCCESS);
		write_list_duration(Config.REVENGE.REVENGE_DURATION_SECOND);
		write_pursuit_cost(Config.REVENGE.REVENGE_ACTION_COST);
		
		L1Revenge revenge = RevengeTable.getRevenge(pc.getId());
		if (revenge != null && revenge.getRevenges() != null && !revenge.getRevenges().isEmpty()) {
			write_revenge_info(revenge);
		}
		writeH(0x00);
	}
	
	void write_init() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(INFO);
	}
	
	void write_result(eRevengeResult result) {
		writeC(0x08);// result
		writeC(result.toInt());
	}
	
	void write_list_duration(int list_duration) {
		writeC(0x10);// list_duration
		writeBit(list_duration);
	}
	
	void write_pursuit_cost(int pursuit_cost) {
		writeC(0x18);// pursuit_cost
		writeBit(pursuit_cost);
	}
	
	void write_revenge_info(L1Revenge revenge) {
		long currentTime = System.currentTimeMillis();
		for (L1RevengeTemp temp : revenge.getRevenges().values()) {
			if (temp == null || temp.getUnregisterDuration().getTime() <= currentTime) {// 24시간 지났으면 제외
				continue;
			}
			int register_timestamp		= (int)(temp.getRegisterTimestamp().getTime() / 1000);
			int	unregister_duration		= (int)((temp.getUnregisterDuration().getTime() / 1000) -  (currentTime / 1000));
			L1PcInstance target	= L1World.getInstance().getPlayer(temp.getUserName());
			int action_timestamp = 0, action_duration = 0;
			if (target != null && temp.getActionType() == S_RevengeInfo.eAction.PURSUIT && temp.getActionDuration() != null && temp.getActionTimestamp() != null && temp.getActionDuration().getTime() > currentTime) {
				action_timestamp		= (int)(temp.getActionTimestamp().getTime() / 1000);
				action_duration			= (int)((temp.getActionDuration().getTime() / 1000) -  (currentTime / 1000));
			}
			RevengeInfoStream os = null;
			try {
				os = new RevengeInfoStream();
				os.write_register_timestamp(register_timestamp);
				os.write_unregister_duration(unregister_duration);
				os.write_action_type(temp.getActionType());
				os.write_action_result(S_RevengeInfo.eResult.NONE);
				os.write_action_timestamp(action_timestamp);
				os.write_action_duration(action_duration);
				os.write_action_remain_count(temp.getActionRemainCount());
				os.write_action_count(temp.getActionCount());
				os.write_crimescene_server_no(Config.VERSION.SERVER_NUMBER);
				os.write_user_uid(temp.getUserUid());
				os.write_server_no(Config.VERSION.SERVER_NUMBER);
				os.write_game_class(temp.getGameClass());
				os.write_user_name(temp.getUserName());
				os.write_pledge_id(temp.getPledgeId());
				os.write_pledge_name(temp.getPledgeName());
				os.write_active(target != null);
				os.write_activate_duration(temp.getActionRemainCount() > 0 ? unregister_duration : 0);
				os.write_anonymity_name(target != null && target.getConfig().getAnonymityType() != null);
				
				writeC(0x22);
				writeBytesWithLength(os.getBytes());
			} catch (Exception e) {
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
	}
	
	public enum eResult{
		NONE(0),
		LOSE(1),
		WIN(2),
		;
		private int value;
		eResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eResult v){
			return value == v.value;
		}
		public static eResult fromInt(int i){
			switch(i){
			case 0:
				return NONE;
			case 1:
				return LOSE;
			case 2:
				return WIN;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
			}
		}
	}
	public enum eAction{
		UNKNOWN(0),
		TAUNT(1),
		PURSUIT(2),
		ANONYMITY_TAUNT(3),
		;
		private int value;
		eAction(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eAction v){
			return value == v.value;
		}
		public static eAction fromInt(int i){
			switch(i){
			case 0:
				return UNKNOWN;
			case 1:
				return TAUNT;
			case 2:
				return PURSUIT;
			case 3:
				return ANONYMITY_TAUNT;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eAction, %d", i));
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
		return S_REVENGE_INFO;
	}
}
