package l1j.server.server.clientpackets.proto;

import l1j.server.server.BadNamesList;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.CharacterCompanionTable;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.companion.S_CompanionNameChange;
import l1j.server.server.serverpackets.companion.S_CompanionStatusNoti;
import l1j.server.server.serverpackets.polymorph.S_Polymorph;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.utils.StringUtil;

public class A_CompanionNameChange extends ProtoHandler {
	protected A_CompanionNameChange(){}
	private A_CompanionNameChange(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private byte[] _desired_name;

	@Override
	protected void doWork() throws Exception {
		if (_pc == null || _pc.isGhost()) {
			return;
		}
		L1PetInstance companion = _pc.getPet();
        if (companion == null) {
        	return;
        }
        L1Pet info = companion.getPetInfo();
        if (info == null) {
        	return;
        }
        
        readP(1);// 0x0a
        int length = readC();
        _desired_name = readByte(length);
        if (_desired_name == null || _desired_name.length <= 0) {
        	_pc.sendPackets(new S_CompanionNameChange(S_CompanionNameChange.eResult.BadName, _desired_name), true);
        	return;
        }
        if (_desired_name.length >= 40) {
        	_pc.sendPackets(new S_CompanionNameChange(S_CompanionNameChange.eResult.BadName, _desired_name), true);
        	return;
        }
        String replace_name = new String(_desired_name, "MS949");
        if (BadNamesList.getInstance().isBadName(replace_name) || 5 < _desired_name.length - replace_name.length()) {
        	_pc.sendPackets(new S_CompanionNameChange(S_CompanionNameChange.eResult.BadName, _desired_name), true);
        	return;
        }
        for (int i=replace_name.length() - 1; i>=0; --i) {
			char c = replace_name.charAt(i);
			if (!Character.isLetterOrDigit(c) || StringUtil.INVALID_KO_CHARACTER.contains(c)) {
				_pc.sendPackets(new S_CompanionNameChange(S_CompanionNameChange.eResult.BadName, _desired_name), true);
				return;					
			}
		}
        if (CharacterCompanionTable.isNameExists(replace_name)) {
        	_pc.sendPackets(new S_CompanionNameChange(S_CompanionNameChange.eResult.SameNameExisted, _desired_name), true);
        	return;
        }
        
        info.setName(replace_name);
        companion.setName(replace_name);
        CharacterCompanionTable.getInstance().ChangeName(info);
        companion.broadcastPacket(new S_Polymorph(companion.getId(), companion.getSpriteId(), 0, replace_name, info.getNpc().getClassId()), true);
        _pc.sendPackets(new S_CompanionNameChange(S_CompanionNameChange.eResult.Success, _desired_name), true);
        companion.send_companion_card();
        _pc.sendPackets(new S_CompanionStatusNoti(S_CompanionStatusNoti.STATUS_UPDATE_FLAG.NAME_CHANGE, companion), true);
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CompanionNameChange(data, client);
	}

}

