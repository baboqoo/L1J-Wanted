package l1j.server.server.clientpackets.proto;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.GameClient;
import l1j.server.server.clientpackets.ClientProtobufPacket;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.party.S_PartyMemberStatus;

public class A_PartyControl extends ProtoHandler {
	protected A_PartyControl(){}
	private A_PartyControl(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost() || _pc.getNetConnection().getInter() == L1InterServer.INSTANCE_DUNGEON) {
			return;
		}
    	ePartyControlType contolType = null;
    	int objid = 0, mark = 0;
    	String name = null;
    	byte[] data = readByte(_total_length);
		ClientProtobufPacket cb = new ClientProtobufPacket(data);
		while(cb._off < _total_length){
			int tag = cb.readTag();
			switch(tag){
			case 0x08:// 파티타입
				contolType = ePartyControlType.fromInt(cb.readTag());
				break;
			case 0x10:// 오브젝트
				objid = cb.readBit();
				break;
			case 0x1a:// 이름
				int namelength = cb.readTag();
	        	name = cb.readS(namelength);
				break;
			case 0x20:// 표식
				mark = cb.readTag();
				break;
			}
		}
		
		if (contolType == null) {
			return;
		}
		
		switch (contolType) {
		case Command_InvatePartyNormalById:// 일반 파티 초대
		case Command_InvatePratyRandomById: {// 자동 분배 파티 초대
			L1Object temp = L1World.getInstance().findObject(objid);
			if (temp instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) temp;
				if (targetPc.getNetConnection() == null || targetPc.getNetConnection().getInter() == L1InterServer.INSTANCE_DUNGEON) {
					return;
				}
				if (_pc.getId() == targetPc.getId()) {
					return;
				}
				if (targetPc.isInParty()) {
					_pc.sendPackets(L1ServerMessage.sm415);// 벌써 다른 파티에 소속해 있기 (위해)때문에 초대할 수 없습니다
					return;
				}
				if (_pc.isInParty()) {
					if (_pc.getParty().isLeader(_pc)) {
						targetPc.setPartyID(_pc.getId());
						targetPc.sendPackets(new S_MessageYN(contolType == ePartyControlType.Command_InvatePartyNormalById ? 953 : 954, _pc.getName()), true);// \f2%0\f>%s로부터 \fU파티 \f> 에 초대되었습니다. 응합니까? (Y/N)
					} else {
						_pc.sendPackets(L1ServerMessage.sm416);// 파티의 리더만을 초대할 수 있습니다.
					}
				} else {
					targetPc.setPartyID(_pc.getId());
					targetPc.sendPackets(new S_MessageYN(contolType == ePartyControlType.Command_InvatePartyNormalById ? 953 : 954, _pc.getName()), true);
				}
			}
		}
			break;
		case Command_InvateChatParty: {// 채팅 파티
			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
			if (targetPc == null) {
				_pc.sendPackets(new S_ServerMessage(109, name), true);// %0라는 이름의 사람은 없습니다.
				return;
			}
			if (_pc.getId() == targetPc.getId()) {
				return;
			}
			if (targetPc.isInChatParty()) {
				_pc.sendPackets(L1ServerMessage.sm415);// 벌써 다른 파티에 소속해 있기 (위해)때문에 초대할 수 없습니다
				return;
			}

			if (_pc.isInChatParty()) {
				if (_pc.getChatParty().isLeader(_pc)) {
					targetPc.setPartyID(_pc.getId());
					targetPc.sendPackets(new S_MessageYN(951, _pc.getName()), true);// \f2%0\f>%s로부터\fU채팅 파티 \f>에 초대되었습니다. 응합니까? (Y/N)
				} else {
					_pc.sendPackets(L1ServerMessage.sm416);// 파티의 리더만을 초대할 수 있습니다.
				}
			} else {
				targetPc.setPartyID(_pc.getId());
				targetPc.sendPackets(new S_MessageYN(951, _pc.getName()), true);// \f2%0\f>%s로부터\fU채팅 파티 \f>에 초대되었습니다. 응합니까? (Y/N)
			}
		}
			break;
		case Command_TransforLeader: {// 파티장 위임
			L1Object temp = L1World.getInstance().findObject(objid);
			if (temp instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) temp;
				if (_pc.getId() == targetPc.getId()) {
					return;
				}
				if (_pc.isInParty()) {
					if (targetPc.isInParty()) {
						if (_pc.getParty().isLeader(_pc)) {
							if (_pc.getLocation().getTileLineDistance(targetPc.getLocation()) < 16) {
								_pc.getParty().passLeader(targetPc);
							} else {
								_pc.sendPackets(L1ServerMessage.sm1695);// 파티장을 위임시킬 동료가 근처에 없습니다
							}
						} else {
							_pc.sendPackets(L1ServerMessage.sm1697);// 파티장이 아니어서 권한을 행사할 수 없습니다
						}
					} else {
						_pc.sendPackets(L1ServerMessage.sm1696);// 현재 파티에 있는 구성원이 아닙니다
					}
				}
			}
		}
			break;
		case Command_InvatePartyNormal:// 일반 파티 초대
		case Command_InvatePartyRandom: {// 자동 분배 파티 초대
			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
			if (targetPc == null) {
				_pc.sendPackets(new S_ServerMessage(109, name), true);// %0라는 이름의 사람은 없습니다.
				return;
			}
			if (targetPc.getNetConnection() == null || targetPc.getNetConnection().getInter() == L1InterServer.INSTANCE_DUNGEON) {
				return;
			}
			if (_pc.getId() == targetPc.getId()) {
				return;
			}
			if (targetPc.isInParty()) {
				_pc.sendPackets(L1ServerMessage.sm415);// 벌써 다른 파티에 소속해 있기 (위해)때문에 초대할 수 없습니다
				return;
			}
			if (_pc.isInParty()) {
				if (_pc.getParty().isLeader(_pc)) {
					targetPc.setPartyID(_pc.getId());
					targetPc.sendPackets(new S_MessageYN(contolType == ePartyControlType.Command_InvatePartyNormal ? 953 : 954, _pc.getName()), true);// \f2%0\f>%s로부터 \fU파티 \f> 에 초대되었습니다. 응합니까? (Y/N)
				} else {
					_pc.sendPackets(L1ServerMessage.sm416);// 파티의 리더만을 초대할 수 있습니다.
				}
			} else {
				targetPc.setPartyID(_pc.getId());
				targetPc.sendPackets(new S_MessageYN(contolType == ePartyControlType.Command_InvatePartyNormal ? 953 : 954, _pc.getName()), true);
			}
		}
			break;
		case Command_TransforMark: {// 표식설정
			if (!_pc.isInParty()) {
				return;
			}
			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
			if (targetPc == null) {
				_pc.sendPackets(new S_ServerMessage(109, name), true);// %0라는 이름의 사람은 없습니다.
				return;
			}
			if (targetPc.isInParty()) {// 파티중
				if (_pc.getParty().isMember(targetPc)) {
					targetPc._partyMark = mark;
					targetPc.getParty().sendPacketToMembers(new S_PartyMemberStatus(targetPc.getName(), targetPc._partyMark), true);
				} else {
					_pc.sendPackets(L1ServerMessage.sm1696);// 현재 파티에 있는 구성원이 아닙니다
				}
			}
		}
			break;
		case Command_KickUser: {
			if (!_pc.isInParty()) {
				return;
			}
//AUTO SRM: 			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);//pc.sendPackets(new S_SystemMessage("추방 이름 = " + name), true);
			L1PcInstance targetPc = L1World.getInstance().getPlayer(name);//pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(2) + name, true), true);
			if (targetPc == null) {
				_pc.sendPackets(new S_ServerMessage(109, name), true);// %0라는 이름의 사람은 없습니다.
				return;
			}
			if (_pc.getId() == targetPc.getId()) {
				return;
			}
			if (targetPc.isInParty()) {// 파티중
				if (_pc.getParty().isMember(targetPc)) {
					targetPc.getParty().kickMember(targetPc);
				} else {
					_pc.sendPackets(L1ServerMessage.sm1696);// 현재 파티에 있는 구성원이 아닙니다
				}
			}
		}
			break;
		}
	}
	
	public enum ePartyControlType{
		Command_InvatePartyNormalById(0),
		Command_InvatePratyRandomById(1),
		Command_InvateChatParty(2),
		Command_TransforLeader(3),
		Command_InvatePartyNormal(4),
		Command_InvatePartyRandom(5),
		Command_TransforMark(6),
		Command_KickUser(7),
		;
		private int value;
		ePartyControlType(int val){
			value		= val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(ePartyControlType v){
			return value == v.value;
		}
		public static ePartyControlType fromInt(int i){
			switch(i){
			case 0:
				return Command_InvatePartyNormalById;
			case 1:
				return Command_InvatePratyRandomById;
			case 2:
				return Command_InvateChatParty;
			case 3:
				return Command_TransforLeader;
			case 4:
				return Command_InvatePartyNormal;
			case 5:
				return Command_InvatePartyRandom;
			case 6:
				return Command_TransforMark;
			case 7:
				return Command_KickUser;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments ePartyControlType, %d", i));
			}
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_PartyControl(data, client);
	}

}


