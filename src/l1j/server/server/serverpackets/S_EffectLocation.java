package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Location;
import l1j.server.server.types.Point;

public class S_EffectLocation extends ServerBasePacket {

	private byte[] _byte = null;

	/**
	 * 지정된 위치에 효과를 표시하는 패킷을 구축한다.
	 * 
	 * @param pt - 효과를 표시하는 위치를 격납한 Point 오브젝트
	 * @param gfxId - 표시하는 효과의 ID
	 */
	public S_EffectLocation(Point pt, int gfxId) {
		this(pt.getX(), pt.getY(), gfxId);
	}

	/**
	 * 지정된 위치에 효과를 표시하는 패킷을 구축한다.
	 * 
	 * @param loc - 효과를 표시하는 위치를 격납한 L1Location 오브젝트
	 * @param gfxId - 표시하는 효과의 ID
	 */
	public S_EffectLocation(L1Location loc, int gfxId) {
		this(loc.getX(), loc.getY(), gfxId);
	}

	/**
	 * 지정된 위치에 효과를 표시하는 패킷을 구축한다.
	 * 
	 * @param x - 효과를 표시하는 위치의 X좌표
	 * @param y - 효과를 표시하는 위치의 Y좌표
	 * @param gfxId - 표시하는 효과의 ID
	 */
	public S_EffectLocation(int x, int y, int gfxId) {
		this(x, y, gfxId, 0);
	}
	
	/**
	 * 지정된 위치에 효과를 표시하는 패킷을 구축한다.
	 * 
	 * @param x - 효과를 표시하는 위치의 X좌표
	 * @param y - 효과를 표시하는 위치의 Y좌표
	 * @param gfxId - 표시하는 효과의 ID
	 * @param dir - 방향
	 */
	public S_EffectLocation(int x, int y, int gfxId, int dir) {
		writeC(Opcodes.S_EFFECT_LOC);
		writeH(x);
		writeH(y);
		writeH(gfxId);
		writeC(dir);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
}

