package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.IndunSystem.indun.IndunType;
import l1j.server.common.bin.IndunCommonBinLoader;
import l1j.server.common.bin.indun.IndunInfoForClient;
import l1j.server.common.data.eArenaMapKind;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.indun.S_IndunMatchingCancel;

public class A_IndunMatchingAcceptRequest extends ProtoHandler {
	protected A_IndunMatchingAcceptRequest(){}
	private A_IndunMatchingAcceptRequest(byte[] data, GameClient client) {
		super(data, client);
	}
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		if (!Config.INTER.INTER_SERVER_ACTIVE || _client.isInterServer() || _pc.getConfig()._indunAutoMatching) {
			return;
		}
		readP(1);// 0x08
		eArenaMapKind map_kind	= eArenaMapKind.fromInt(readBit());
		IndunType type = IndunType.getIndunType(map_kind.toInt());
		if (type == null) {
			return;
		}
		readP(1);// 0x10
		A_IndunMatchingAcceptRequest.eResult result = A_IndunMatchingAcceptRequest.eResult.fromInt(readC());
		
		IndunInfoForClient indun = IndunCommonBinLoader.getCommonInfo(map_kind.toInt());
		if (indun == null) {
			System.out.println(String.format("[A_IndunAutomatchStart] INDUN_BIN_EMPTY : KIND(%d)", map_kind.toInt()));
			return;
		}
		
		switch (result) {
		case ACCEPT:
			break;
		default:
			break;
		}
		
		_pc.getConfig()._indunAutoMatching = false;
		_pc.getConfig()._indunAutoMatchingMapKind = null;
		_pc.sendPackets(S_IndunMatchingCancel.CANCEL);
	}
	
	public enum eResult{
		ACCEPT(1),
		CANCEL(2),
		CANCEL_TIMEOUT(3),
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
			case 1:
				return ACCEPT;
			case 2:
				return CANCEL;
			case 3:
				return CANCEL_TIMEOUT;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
			}
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_IndunMatchingAcceptRequest(data, client);
	}

}

