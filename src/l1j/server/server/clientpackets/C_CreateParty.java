package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class C_CreateParty extends ClientBasePacket {
	private static final String C_CREATE_PARTY = "[C] C_CreateParty";

	public C_CreateParty(byte decrypt[], GameClient client) throws Exception {
		super(decrypt);

		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		if (pc.getNetConnection().getInter() == L1InterServer.INSTANCE_DUNGEON) {
			return;
		}
		int type = readC();		
		if (type == 0 || type == 1) {
			int targetId = readD();
			L1Object temp = L1World.getInstance().findObject(targetId);
			if (temp instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) temp;
				if (targetPc.getNetConnection() == null || targetPc.getNetConnection().getInter() == L1InterServer.INSTANCE_DUNGEON) {
					return;
				}
				if (pc.getId() == targetPc.getId()) {
					return;
				}
				if (targetPc.isInParty()) {
					pc.sendPackets(L1ServerMessage.sm415);// 벌써 다른 파티에 소속해 있기 (위해)때문에 초대할 수 없습니다
					return;
				}
				if (pc.isInParty()) {
					if (pc.getParty().isLeader(pc)) {
						targetPc.setPartyID(pc.getId());
						targetPc.sendPackets(new S_MessageYN(953, pc.getName()), true);// \f2%0\f>%s로부터 \fU파티 \f> 에 초대되었습니다. 응합니까? (Y/N)
					} else {
						pc.sendPackets(L1ServerMessage.sm416);// 파티의 리더만을 초대할 수 있습니다.
					}
				} else {
					targetPc.setPartyID(pc.getId());
					switch (type) {
					case 0:
						targetPc.sendPackets(new S_MessageYN(953, pc.getName()), true);// \f2%0\f>%s로부터 \fU파티 \f> 에 초대되었습니다. 응합니까? (Y/N)
						break;
					case 1:
						targetPc.sendPackets(new S_MessageYN(954, pc.getName()), true);// \f2%0\f>%s \fU자동분배파티\f> 초대하였습니다. 허락하시겠습니까? (Y/N)
						break;
					}
				}
			}
		} else if (type == 2) { // 채팅 파티
			String name = readS();
			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
			if (targetPc == null) {
				pc.sendPackets(new S_ServerMessage(109, name), true);// %0라는 이름의 사람은 없습니다.
				return;
			}
			if (pc.getId() == targetPc.getId()) {
				return;
			}
			if (targetPc.isInChatParty()) {
				pc.sendPackets(L1ServerMessage.sm415);// 벌써 다른 파티에 소속해 있기 (위해)때문에 초대할 수 없습니다
				return;
			}

			if (pc.isInChatParty()) {
				if (pc.getChatParty().isLeader(pc)) {
					targetPc.setPartyID(pc.getId());
					targetPc.sendPackets(new S_MessageYN(951, pc.getName()), true);// \f2%0\f>%s로부터\fU채팅 파티 \f>에 초대되었습니다. 응합니까? (Y/N)
				} else {
					pc.sendPackets(L1ServerMessage.sm416);// 파티의 리더만을 초대할 수 있습니다.
				}
			} else {
				targetPc.setPartyID(pc.getId());
				targetPc.sendPackets(new S_MessageYN(951, pc.getName()), true);// \f2%0\f>%s로부터\fU채팅 파티 \f>에 초대되었습니다. 응합니까? (Y/N)
			}
		} else if (type == 3) {
			int targetId = readD();
			L1Object temp = L1World.getInstance().findObject(targetId);
			if (temp instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) temp;
				if (pc.getId() == targetPc.getId()) {
					return;
				}
				if (pc.isInParty()) {
					if (targetPc.isInParty()) {
						if (pc.getParty().isLeader(pc)) {
							if (pc.getLocation().getTileLineDistance(targetPc.getLocation()) < 16) {
								pc.getParty().passLeader(targetPc);
							} else {
								pc.sendPackets(L1ServerMessage.sm1695);// 파티장을 위임시킬 동료가 근처에 없습니다
							}
						} else {
							pc.sendPackets(L1ServerMessage.sm1697);// 파티장이 아니어서 권한을 행사할 수 없습니다
						}
					} else {
						pc.sendPackets(L1ServerMessage.sm1696);// 현재 파티에 있는 구성원이 아닙니다
					}
				}
			}
		} else if (type == 4 || type == 5) {
			String name = readS();
			if (type == 4) {
				type = 0;
			} else if (type == 5)	{
				type = 1;
			}
			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
			if (targetPc == null) {
				pc.sendPackets(new S_ServerMessage(109, name), true);// %0라는 이름의 사람은 없습니다.
				return;
			}
			if (targetPc.getNetConnection() == null || targetPc.getNetConnection().getInter() == L1InterServer.INSTANCE_DUNGEON) {
				return;
			}
			if (pc.getId() == targetPc.getId()) {
				return;
			}
			if (targetPc.isInParty()) {
				pc.sendPackets(L1ServerMessage.sm415);// 벌써 다른 파티에 소속해 있기 (위해)때문에 초대할 수 없습니다
				return;
			}
			if (pc.isInParty()) {
				if (pc.getParty().isLeader(pc)) {
					targetPc.setPartyID(pc.getId());
					targetPc.sendPackets(new S_MessageYN(953, pc.getName()), true);// \f2%0\f>%s로부터 \fU파티 \f> 에 초대되었습니다. 응합니까? (Y/N)
				} else {
					pc.sendPackets(L1ServerMessage.sm416);// 파티의 리더만을 초대할 수 있습니다.
				}
			} else {
				targetPc.setPartyID(pc.getId());
				switch (type) {
				case 0:
					targetPc.sendPackets(new S_MessageYN(953, pc.getName()), true);// \f2%0\f>%s로부터 \fU파티 \f> 에 초대되었습니다. 응합니까? (Y/N)
					break;
				case 1:
					targetPc.sendPackets(new S_MessageYN(954, pc.getName()), true);// \f2%0\f>%s \fU자동분배파티\f> 초대하였습니다. 허락하시겠습니까? (Y/N)
					break;
				}
			}
		}
	}

	@Override
	public String getType() {
		return C_CREATE_PARTY;
	}

}

