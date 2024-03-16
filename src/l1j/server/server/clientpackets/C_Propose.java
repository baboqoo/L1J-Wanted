package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.utils.FaceToFace;

public class C_Propose extends ClientBasePacket {
	private static final String C_PROPOSE = "[C] C_Propose";
	
	private static final int PROPOSE	= 0;
	private static final int DIVORCE	= 1;

	public C_Propose(byte abyte0[], GameClient client) {
		super(abyte0);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.isGhost()) {
			return;
		}
		int code = readC();
		switch(code){
		case PROPOSE:// /propose(/청혼)
			L1PcInstance target = FaceToFace.faceToFace(pc);
			if (target == null) {
				return;
			}
			if (pc.getPartnerId() != 0) {
				pc.sendPackets(L1ServerMessage.sm657);// \f1당신은 벌써 결혼했습니다.
				return;
			}
			if (target.getPartnerId() != 0) {
				pc.sendPackets(L1ServerMessage.sm658);// \f1 그 상대는 벌써 결혼했습니다.
				return;
			}
			if (pc.getGender() == target.getGender()) {
				pc.sendPackets(L1ServerMessage.sm661);// \f1결혼상대는 이성이 아니면 안됩니다.
				return;
			}
			if (!ringCheck(pc)) {
				pc.sendPackets(L1ServerMessage.sm659);// \f1당신은 결혼반지를 가지고 있지 않습니다.
				return;
			}
			if (!ringCheck(target)) {
				pc.sendPackets(L1ServerMessage.sm660);// \f1당신이 청혼한 사람은 결혼반지를 가지고 있지 않습니다.
				return;
			}
			if (pc.getX() >= 33974 && pc.getX() <= 33976 && pc.getY() >= 33362 && pc.getY() <= 33365 && pc.getMapId() == 4 
					&& target.getX() >= 33974 && target.getX() <= 33976 && target.getY() >= 33362 && target.getY() <= 33365 && target.getMapId() == 4) {
				target.setTempID(pc.getId());// 상대의 오브젝트 ID를 보존해 둔다
				target.sendPackets(new S_MessageYN(654, pc.getName()), true);// %0%s당신과 결혼 하고 싶어하고 있습니다. %0과 결혼합니까? (Y/N)
			}
			break;
		case DIVORCE:// /divorce(/이혼)
			if (pc.getPartnerId() == 0) {
				pc.sendPackets(L1ServerMessage.sm662);// \f1당신은 결혼하지 않았습니다.
				return;
			}
			pc.sendPackets(S_MessageYN.MARRY_DIVORCE_YN);// 이혼을 하면(자) 링은 사라져 버립니다. 이혼을 바랍니까? (Y/N)
			break;
		}
	}
	
	private boolean ringCheck(L1PcInstance pc){
		return (pc.getInventory().checkItemOne(L1ItemId.MARRY_RING_ITEMS));
	}

	@Override
	public String getType() {
		return C_PROPOSE;
	}
}

