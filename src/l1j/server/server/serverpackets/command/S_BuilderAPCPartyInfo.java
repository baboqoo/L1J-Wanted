package l1j.server.server.serverpackets.command;

import java.util.Map;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.BinaryOutputStream;

public class S_BuilderAPCPartyInfo extends ServerBasePacket {
	private static final String S_BUILDER_APC_PARTY_INFO = "[S] S_BuilderAPCPartyInfo";
	private byte[] _byte = null;
	
	public static final int PARTY_INFO = 0x0a12;
	
	public S_BuilderAPCPartyInfo(L1Party party){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(PARTY_INFO);
		
		writeC(0x0a);
		writeBytesWithLength(syncPeriodicByteInfo(party));

		writeH(0x00);
	}
	
	private byte[] syncPeriodicByteInfo(L1Party party) {
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			for (L1PcInstance pc : party.getMembersArray()) {
				byte[] memberBytes = memberByteInfo(pc);
				int length = 6 + memberBytes.length;
				
				os.writeC(0x0a);
				os.writeC(length);
				
				os.writeC(0x0a);// prty member status
				os.writeBytesWithLength(memberBytes);
				
				os.writeC(0x10);// class
				os.writeC(pc.getType());
				
				os.writeC(0x18);// gender
				os.writeC(pc.getGender().toInt());
			}
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	private byte[] memberByteInfo(L1PcInstance member) {
		BinaryOutputStream os = null;
		try {
			os = new BinaryOutputStream();
			os.writeC(0x0a);
			os.writeS2(member.getName());
			
			os.writeC(0x10);
			os.writeBit(member.getId());
			
			os.writeC(0x18);
			os.writeBit(member.getCurrentHpPercent());
			
			os.writeC(0x20);
			os.writeBit(member.getCurrentMpPercent());
			
			os.writeC(0x28);
			os.writeC(member.isDead() ? 0 : 1);
			
			os.writeC(0x30);
			os.writeBit(member.getMapId());
			
			os.writeC(0x38);
			os.writeInt32NoTag(member.getLongLocationReverse());
			
			os.writeC(0x40);
			os.writeBit(member._partyMark);
			
			Map<Integer, byte[]> iconByte = member.getSkill().getPartyIconSkillBytes();
			if (iconByte != null && !iconByte.isEmpty()) {
				for (byte[] array : iconByte.values()) {
					os.writeByte(array);
				}
			}
			
			os.writeC(0x50);
			os.writeBit(Config.VERSION.SERVER_NUMBER);
			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
					os = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_BUILDER_APC_PARTY_INFO;
	}
}

