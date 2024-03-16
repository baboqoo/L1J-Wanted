package l1j.server.server.serverpackets.action;

import java.util.concurrent.atomic.AtomicInteger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_AttackCritical extends ServerBasePacket {
    private static final String S_ATTACK_CRITICAL = "[S] S_AttackCritical";
    private byte[] _byte = null;
    private static AtomicInteger _sequentialNumber = new AtomicInteger(0);

    /** 근거리 크리티컬 **/
    public S_AttackCritical(L1PcInstance pc, int objid, int criticalId) {
        writeC(Opcodes.S_ATTACK);
        writeC(1);
        writeD(pc.getId());
        writeD(objid);
        writeH(0x01);
        writeC(pc.getMoveState().getHeading());
        writeH(0x0000);
        writeH(0x0000);
        writeC(2);
        writeD(criticalId);
        writeH(0);
    }

    /** 원거리 크리티컬 **/
    public S_AttackCritical(L1Character cha, int targetobj, int x, int y, int criticalId, boolean isHit) {
        int aid = cha.getSpriteId() == 3860 || cha.getSpriteId() == 7959 ? 21 : 1;
        writeC(Opcodes.S_ATTACK);
        writeC(aid);
        writeD(cha.getId());
        writeD(targetobj);
        writeC(isHit ? 0x2c : 0x00);
        writeC(0x00);
        writeC(cha.getMoveState().getHeading());
        writeD(_sequentialNumber.incrementAndGet());
        writeH(criticalId);
        writeC(0x00); 
        writeH(x);
        writeH(y);
        writeH(cha.getX());
        writeH(cha.getY());
        writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeC(0x00);
		writeH(0x00);
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
        return S_ATTACK_CRITICAL;
    }
}

