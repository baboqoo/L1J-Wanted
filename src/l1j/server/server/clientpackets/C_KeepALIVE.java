package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;

public class C_KeepALIVE extends ClientBasePacket {
	private static final String C_KEEP_ALIVE = "[C] C_KeepALIVE";
	
	public C_KeepALIVE(byte decrypt[], GameClient client) {// 1분에 한번씩 송신되는 패킷 1분주기 체크가 필요할 시 사용하면 된다.(핵을 통해 시간 단축 가능)
		super(decrypt);
	}

	@Override
	public String getType() {
		return C_KEEP_ALIVE;
	}
}
