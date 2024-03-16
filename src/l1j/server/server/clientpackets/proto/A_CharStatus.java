package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.L1Status;
import l1j.server.server.serverpackets.returnedstat.S_StatusRenewalInfo;

public class A_CharStatus extends ProtoHandler {
	protected A_CharStatus(){}
	private A_CharStatus(byte[] data, GameClient client) {
		super(data, client);
		parse();
	}
	
	private int level, classType, infoType, optionValue, optionMask, str, cha, inte, dex, con, wis;
	
	void parse() {
		while (!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x08:
				level = readC();
				break;
			case 0x10:
				classType = readC();
				break;
			case 0x18:
				infoType = readC();
				break;
			case 0x20:
				optionValue = readC();
				break;
			case 0x28:
				optionMask = readC();
				break;
			case 0x30:
				str = readC();
				break;
			case 0x38:
				inte = readC();
				break;
			case 0x40:
				wis = readC();
				break;
			case 0x48:
				dex = readC();
				break;
			case 0x50:
				con = readC();
				break;
			case 0x58:
				cha = readC();
				break;
			default:
				return;
			}
		}
	}

	@Override
	protected void doWork() throws Exception {
		try {
			if (_pc != null && _pc.getReturnStatus() != 0) {
				_pc.sendPackets(new S_StatusRenewalInfo(infoType, str, inte, wis, dex, con, cha, classType, _pc), true);
				return;
			}
			if (str != 0 && optionMask != 1) {
				_client.sendPacket(new S_StatusRenewalInfo(infoType, str, con, L1Status.STR, classType, _pc));
			}
			if (dex != 0) {
				_client.sendPacket(new S_StatusRenewalInfo(infoType, dex, 0, L1Status.DEX, classType, _pc));
			}
			if (con != 0 && optionMask != 16) {
				_client.sendPacket(new S_StatusRenewalInfo(infoType, con, str, L1Status.CON, classType, _pc));
			}
			if (inte != 0) {
				_client.sendPacket(new S_StatusRenewalInfo(infoType, inte, 0, L1Status.INT, classType, _pc));
			}
			if (wis != 0) {
				_client.sendPacket(new S_StatusRenewalInfo(infoType, wis, 0, L1Status.WIS, classType, _pc));
			}
			if (cha != 0) {
				_client.sendPacket(new S_StatusRenewalInfo(infoType, cha, 0, L1Status.CHA, classType, _pc));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CharStatus(data, client);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("level : ").append(level);
		sb.append(", classType : ").append(classType);
		sb.append(", infoType : ").append(infoType);
		sb.append(", optionValue : ").append(optionValue);
		sb.append(", optionMask : ").append(optionMask);
		sb.append(", str : ").append(str);
		sb.append(", cha : ").append(cha);
		sb.append(", inte : ").append(inte);
		sb.append(", dex : ").append(dex);
		sb.append(", con : ").append(con);
		sb.append(", wis : ").append(wis);
		return sb.toString();
	}
	
	public enum INFO_TYPE{
		STAT_NORMAL(1),
		STAT_CALC(2),
		STAT_LEVUP(4),
		STAT_CALC_START(8),
		STAT_CALC_INGAME(16),
		;
		private int value;
		INFO_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(INFO_TYPE v){
			return value == v.value;
		}
		public static INFO_TYPE fromInt(int i){
			switch(i){
			case 1:
				return STAT_NORMAL;
			case 2:
				return STAT_CALC;
			case 4:
				return STAT_LEVUP;
			case 8:
				return STAT_CALC_START;
			case 16:
				return STAT_CALC_INGAME;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments INFO_TYPE, %d", i));
			}
		}
	}
	public enum OPTION_VALUE{
		OPTION_NO_RESPONSE(1),
		;
		private int value;
		OPTION_VALUE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(OPTION_VALUE v){
			return value == v.value;
		}
		public static OPTION_VALUE fromInt(int i){
			switch(i){
			case 1:
				return OPTION_NO_RESPONSE;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments OPTION_VALUE, %d", i));
			}
		}
	}
	public enum OPTION_MASK{
		OPTION_STR_MASK(1),
		OPTION_INT_MASK(2),
		OPTION_WIS_MASK(4),
		OPTION_DEX_MASK(8),
		OPTION_CON_MASK(16),
		OPTION_CHA_MASK(32),
		;
		private int value;
		OPTION_MASK(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(OPTION_MASK v){
			return value == v.value;
		}
		public static OPTION_MASK fromInt(int i){
			switch(i){
			case 1:
				return OPTION_STR_MASK;
			case 2:
				return OPTION_INT_MASK;
			case 4:
				return OPTION_WIS_MASK;
			case 8:
				return OPTION_DEX_MASK;
			case 16:
				return OPTION_CON_MASK;
			case 32:
				return OPTION_CHA_MASK;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments OPTION_MASK, %d", i));
			}
		}
	}

}

