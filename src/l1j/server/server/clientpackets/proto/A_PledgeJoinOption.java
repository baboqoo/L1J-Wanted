package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Clan;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeJoinOption;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeJoinOption.ePLEDGE_JOIN_OPTION_RESULT;

public class A_PledgeJoinOption extends ProtoHandler {
	protected A_PledgeJoinOption(){}
	private A_PledgeJoinOption(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.getClanid() == 0) {
			return;
		}
		L1Clan clan = _pc.getClan();
		if (clan == null) {
			_pc.sendPackets(new S_BloodPledgeJoinOption(ePLEDGE_JOIN_OPTION_RESULT.eRESULT_ERROR_HAVE_NO_PLEDGE, null), true);
			return;
		}
		readP(1);// 0x08
		eOPTION_TYPE type = eOPTION_TYPE.fromInt(readC());
		if (type == null) {
			return;
		}
		ePLEDGE_JOIN_OPTION_RESULT result = _pc.isCrown() ? ePLEDGE_JOIN_OPTION_RESULT.eRESULT_OK : ePLEDGE_JOIN_OPTION_RESULT.eRESULT_ERROR_NOT_LORD;
		_pc.sendPackets(new S_BloodPledgeJoinOption(result, clan), true);
	}
	
	public enum eOPTION_TYPE{
		eOPTION_TYPE_ENABLE_JOIN(1),
		eOPTION_TYPE_JOIN_TYPE(2),
		eOPTION_TYPE_HASHED_PASSWORD(3),
		eOPTION_TYPE_ALL(4),
		;
		private int value;
		eOPTION_TYPE(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eOPTION_TYPE v){
			return value == v.value;
		}
		public static eOPTION_TYPE fromInt(int i){
			switch(i){
			case 1:
				return eOPTION_TYPE_ENABLE_JOIN;
			case 2:
				return eOPTION_TYPE_JOIN_TYPE;
			case 3:
				return eOPTION_TYPE_HASHED_PASSWORD;
			case 4:
				return eOPTION_TYPE_ALL;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eOPTION_TYPE, %d", i));
			}
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PledgeJoinOption(data, client);
	}

}

