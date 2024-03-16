package l1j.server.server.clientpackets.proto;

import l1j.server.Config;
import l1j.server.GameSystem.smelting.SmeltingLoader;
import l1j.server.server.GameClient;
import l1j.server.server.serverpackets.smelting.S_SynthesisSmeltingDesign;
import l1j.server.server.utils.HexHelper;

public class A_SynthesisSmeltingDesign extends ProtoHandler {
	protected A_SynthesisSmeltingDesign(){}
	private A_SynthesisSmeltingDesign(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private byte[] _hash_value;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x0a
		int hash_length	= readC();
		_hash_value	= readByte(hash_length);
		if (_pc.isGm()) {
			System.out.println(String.format("SMELTING_HASH\r\n%s", HexHelper.toSourceString(_hash_value, _hash_value.length)));
		}
        if (!isHashVersion()) {
        	sendDesign();
        	return;
        }
        _pc.sendPackets(S_SynthesisSmeltingDesign.SAME_HASH);
	}
	
	/**
	 * 해시 버전 검증
	 * @return boolean
	 */
	boolean isHashVersion() {
		// 버전 길이 검증
		if (Config.SMELTING.SMELTING_HASH.length != _hash_value.length) {
			return false;
		}
		// 버전 동일 검증
		for (int i=0; i<Config.SMELTING.SMELTING_HASH.length; i++) {
			if (Config.SMELTING.SMELTING_HASH[i] != _hash_value[i]) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * smelting.dat 생성
	 */
	void sendDesign(){
		for (S_SynthesisSmeltingDesign pck : SmeltingLoader.getSmeltingDesignList()) {
			_pc.sendPackets(pck);
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_SynthesisSmeltingDesign(data, client);
	}

}

