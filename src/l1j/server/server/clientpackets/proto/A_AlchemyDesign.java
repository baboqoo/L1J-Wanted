package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.MagicDollInfoTable;
import l1j.server.server.serverpackets.alchemy.S_AlchemyExtraInfo;
import l1j.server.server.serverpackets.alchemy.S_AlchemyDesign;
import l1j.server.server.utils.HexHelper;

public class A_AlchemyDesign extends ProtoHandler {
	protected A_AlchemyDesign(){}
	private A_AlchemyDesign(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private byte[] _hash_value;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x0a
		int length	= readC();
		_hash_value	= readByte(length);
		/*if (_pc.isGm()) {
			System.out.println(String.format("ALCHEMY_HASH\r\n%s", HexHelper.toSourceString(_hash_value, _hash_value.length)));
		}*/
		_pc.sendPackets(new S_AlchemyExtraInfo(_pc.getDoll()), true);
        if (!isHashVersion()) {
        	sendDesign();
        	return;
        }
        _pc.sendPackets(S_AlchemyDesign.SAME_HASH);
	}
	
	/**
	 * 해시 버전 검증
	 * @return boolean
	 */
	boolean isHashVersion() {
		// 버전 길이 검증
		if (Config.ALCHEMY.ALCHEMY_HASH.length != _hash_value.length) {
			return false;
		}
		// 버전 동일 검증
		for (int i=0; i<Config.ALCHEMY.ALCHEMY_HASH.length; i++) {
			if (Config.ALCHEMY.ALCHEMY_HASH[i] != _hash_value[i]) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * alchemy.dat 생성
	 */
	void sendDesign(){
		for (S_AlchemyDesign pck : MagicDollInfoTable.getAlchemyDesignList()) {
			_pc.sendPackets(pck);
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_AlchemyDesign(data, client);
	}

}

