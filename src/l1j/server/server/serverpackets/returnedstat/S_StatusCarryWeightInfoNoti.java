package l1j.server.server.serverpackets.returnedstat;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_StatusCarryWeightInfoNoti extends ServerBasePacket {
	public static final int WEIGHT = 0x01e5;

    public S_StatusCarryWeightInfoNoti(L1PcInstance pc) {
    	write_init();
        write_carrypercent(pc.getInventory().getWeightPercent());
        write_carrycurrent(pc.getInventory().getWeight());
        write_carrymax(pc.getMaxWeight());
        writeH(0x00);
    }
    
    void write_init() {
    	writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(WEIGHT);
    }
    
    void write_carrypercent(int carrypercent) {
    	writeRaw(0x08);// carrypercent
        writeRaw(carrypercent);
    }
    
    void write_carrycurrent(int carrycurrent) {
    	writeRaw(0x10);// carrycurrent
        write7B(carrycurrent);
    }
    
    void write_carrymax(int carrymax) {
    	writeRaw(0x18);// carrymax
        write7B(carrymax);
    }

    @Override
    public byte[] getContent() {
        return _bao.toByteArray();
    }

    @Override
    public String getType() {
        return S_WEIGHT;
    }

    private static final String S_WEIGHT = "[S] S_Weight";
}

