package l1j.server.server.clientpackets.proto;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.serverpackets.S_Exclude;
import l1j.server.server.templates.L1CharName;

public class A_Exclude extends ProtoHandler {
	protected A_Exclude(){}
	private A_Exclude(byte[] data, GameClient client) {
		super(data, client);
	}

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		readP(1);// 0x08
		SpamTable spam	= SpamTable.getInstance();
		L1ExcludingList exList = spam.getExcludeTable(_pc.getId());
		A_Exclude.eExcludeMode mode = A_Exclude.eExcludeMode.fromInt(readC());// 0:리스트, 1:추가, 2:삭제
		if (mode == A_Exclude.eExcludeMode.eEXCLUDE_MODE_LIST) {
			_pc.sendPackets(new S_Exclude(exList.getExcludeList(0), 0), true);
			_pc.sendPackets(new S_Exclude(exList.getExcludeList(1), 1), true);
			return;
		}
		
		readP(1);// 0x10
		A_Exclude.eExcludeType type = A_Exclude.eExcludeType.fromInt(readC());
		while(isRead(1)){
			int dummy = readC();// 0x1a
			if (dummy != 0x1a) {
				break;
			}
			int nameLength = readC();
			if (nameLength == 0 || nameLength > 12) {
				break;
			}
			String charName = readS(nameLength);
			if (charName.equalsIgnoreCase(_pc.getName())) {
				_pc.sendPackets(L1SystemMessage.EXCLUDE_ME_FAIL);
				break;
			}
			if (exList.contains(type.value, charName)) {
				spam.deleteExclude(_pc, type.value, charName);
				exList.remove(type.value, charName);
				_pc.sendPackets(new S_Exclude(mode, type, charName), true);
			} else {
				for (L1CharName cn : CharacterTable.getInstance().getCharNameList()) {
					if (charName.equalsIgnoreCase(cn.getName())) {
						int objId = cn.getId();
						String name = cn.getName();
						exList.add(type.value, name);
						spam.insertExclude(_pc, type.value, objId, name);
						_pc.sendPackets(new S_Exclude(mode, type, charName), true);
						return;
					}
				}
			}
		}
	}
	
	public enum eExcludeMode{
		eEXCLUDE_MODE_LIST(0),
		eEXCLUDE_MODE_ADD(1),
		eEXCLUDE_MODE_DEL(2),
		;
		private int value;
		eExcludeMode(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eExcludeMode v){
			return value == v.value;
		}
		public static eExcludeMode fromInt(int i){
			switch(i){
			case 0:
				return eEXCLUDE_MODE_LIST;
			case 1:
				return eEXCLUDE_MODE_ADD;
			case 2:
				return eEXCLUDE_MODE_DEL;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eExcludeMode, %d", i));
			}
		}
	}
	
	public enum eExcludeType{
		eEXCLUDE_TYPE_CHAT(0),
		eEXCLUDE_TYPE_MAIL(1),
		eEXCLUDE_TYPE_MAX(2),
		;
		private int value;
		eExcludeType(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eExcludeType v){
			return value == v.value;
		}
		public static eExcludeType fromInt(int i){
			switch(i){
			case 0:
				return eEXCLUDE_TYPE_CHAT;
			case 1:
				return eEXCLUDE_TYPE_MAIL;
			case 2:
				return eEXCLUDE_TYPE_MAX;
			default:
				throw new IllegalArgumentException(String.format("invalid arguments eExcludeType, %d", i));
			}
		}
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_Exclude(data, client);
	}

}

