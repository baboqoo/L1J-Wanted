package l1j.server.server.clientpackets.proto;

import l1j.server.common.bin.CraftCommonBinLoader;
import l1j.server.common.bin.craft.Craft;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.craft.S_CraftLimitedInfo;

public class A_CraftLimitedInfo extends ProtoHandler {
	protected A_CraftLimitedInfo(){}
	private A_CraftLimitedInfo(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private int craftId;
	
	void parse(){
		if (_total_length < 2) {
			return;
		}
		while(!isEnd()){
			int code = readC();
			switch(code){
			case 0x08:
				craftId = readBit();
				break;
			default:
				return;
			}
		}
	}
	
	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		parse();
		if (craftId <= 0) {
			return;
		}
		Craft bin = CraftCommonBinLoader.getCraft(craftId);
		if (bin == null) {
			_pc.sendPackets(S_CraftLimitedInfo.RP_ERROR_CRAFT_ID);
			System.out.println(String.format("[A_CraftLimitedInfo] BIN_DATA_NOT_FOUND : CRAFT_ID(%d)", craftId));
			return;
		}
		_pc.sendPackets(new S_CraftLimitedInfo(_pc, S_CraftLimitedInfo.eCraftIdInfoReqResultType.RP_SUCCESS, bin), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CraftLimitedInfo(data, client);
	}

}

