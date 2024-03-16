package l1j.server.server.clientpackets.proto;

import l1j.server.common.bin.companion.CompanionT;
import l1j.server.server.GameClient;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.templates.L1Pet;

public class A_CompanionTMCommand extends ProtoHandler {
	protected A_CompanionTMCommand(){}
	private A_CompanionTMCommand(byte[] data, GameClient client) {
		super(data, client);
	}
	
	private CompanionT.eCommand _command;
	private int _target_id;
	
	void parse() {
		while(!isEnd()) {
			int tag = readC();
			switch (tag) {
			case 0x08:
				_command = CompanionT.eCommand.fromInt(readBit());
				break;
			case 0x10:
				_target_id = readBit();
				break;
			default:
				return;
			}
		}
	}
	
	boolean isValidation() {
		return _command != null;
	}

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
        parse();
        if (!isValidation()) {
        	return;
        }
        
        switch (_command) {
        case TM_Aggressive:
        	companion.set_command(_command);
        	break;
        case TM_GetItem:
        case TM_Defensive:
        	companion.allTargetClear();
        	companion.set_command(_command);
        	break;
        case TM_Attack:
        	L1Object obj = L1World.getInstance().findObject(_target_id);
        	if (obj != null || obj instanceof L1Character) {
        		companion.setTarget((L1Character)obj);
			}
        	break;
        case TM_PullBack:
        	companion.allTargetClear();
        	break;
        case Joke:
        	companion.broadcastPacket(new S_DoActionGFX(companion.getId(), 67), true);
        	break;
        case Happy:
        	companion.broadcastPacket(new S_DoActionGFX(companion.getId(), 68), true);
        	break;
        case Dismiss:
        	companion.delete();
        	break;
        default:
        	System.out.println(String.format("[A_CompanionTMCommand] UNDEFINED_COMMAND : COMMAND(%s)", _command.name()));
        	break;
        }
	}

	@Override
	protected ProtoHandler copyInstance(byte[] data, GameClient client) {
		return new A_CompanionTMCommand(data, client);
	}

}

