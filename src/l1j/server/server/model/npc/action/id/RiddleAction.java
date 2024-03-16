package l1j.server.server.model.npc.action.id;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcIdAction;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class RiddleAction implements L1NpcIdAction {
	private static class newInstance {
		public static final L1NpcIdAction INSTANCE = new RiddleAction();
	}
	public static L1NpcIdAction getInstance(){
		return newInstance.INSTANCE;
	}
	private RiddleAction(){}
	
	@Override
	public String excute(L1PcInstance pc, String s, String s2, L1Object obj, int npcId) {
		riddle(pc, s);
		return null;
	}
	
	private void riddle(L1PcInstance pc, String s){
		int consumeItemId = 0;
		switch(s){
		case "str55":case "dex55":case "con55":case "int55":case "wis55":consumeItemId = 60034;break;
		case "str70":case "dex70":case "con70":case "int70":case "wis70":consumeItemId = 600033;break;
		case "str80":case "dex80":case "con80":case "int80":case "wis80":consumeItemId = 600034;break;
		case "str85":case "dex85":case "con85":case "int85":case "wis85":consumeItemId = 600035;break;
		case "str90":case "dex90":case "con90":case "int90":case "wis90":consumeItemId = 600135;break;
		case "str91":case "dex91":case "con91":case "int91":case "wis91":consumeItemId = 600136;break;
		case "str92":case "dex92":case "con92":case "int92":case "wis92":consumeItemId = 600137;break;
		case "str93":case "dex93":case "con93":case "int93":case "wis93":consumeItemId = 600144;break;
		case "str94":case "dex94":case "con94":case "int94":case "wis94":consumeItemId = 600145;break;
		case "str95":case "dex95":case "con95":case "int95":case "wis95":consumeItemId = 600146;break;
		case "str96":case "dex96":case "con96":case "int96":case "wis96":consumeItemId = 600180;break;
		case "str97":case "dex97":case "con97":case "int97":case "wis97":consumeItemId = 600181;break;
		case "str98":case "dex98":case "con98":case "int98":case "wis98":consumeItemId = 600182;break;
		case "str99":case "dex99":case "con99":case "int99":case "wis99":consumeItemId = 600183;break;
		}
		if (pc.getInventory().consumeItem(consumeItemId, 1)) {
			L1ItemInstance item = null;
			switch(s){
			case "str55":item = pc.getInventory().storeItem(60036, 1);break;
			case "dex55":item = pc.getInventory().storeItem(60037, 1);break;
			case "con55":item = pc.getInventory().storeItem(60038, 1);break;
			case "int55":item = pc.getInventory().storeItem(60039, 1);break;
			case "wis55":item = pc.getInventory().storeItem(60040, 1);break;
			case "str70":item = pc.getInventory().storeItem(60041, 1);break;
			case "dex70":item = pc.getInventory().storeItem(60042, 1);break;
			case "con70":item = pc.getInventory().storeItem(60043, 1);break;
			case "int70":item = pc.getInventory().storeItem(60044, 1);break;
			case "wis70":item = pc.getInventory().storeItem(60045, 1);break;
			case "str80":item = pc.getInventory().storeItem(600036, 1);break;
			case "dex80":item = pc.getInventory().storeItem(600037, 1);break;
			case "con80":item = pc.getInventory().storeItem(600038, 1);break;
			case "int80":item = pc.getInventory().storeItem(600039, 1);break;
			case "wis80":item = pc.getInventory().storeItem(600040, 1);break;
			case "str85":item = pc.getInventory().storeItem(600041, 1);break;
			case "dex85":item = pc.getInventory().storeItem(600042, 1);break;
			case "con85":item = pc.getInventory().storeItem(600043, 1);break;
			case "int85":item = pc.getInventory().storeItem(600044, 1);break;
			case "wis85":item = pc.getInventory().storeItem(600045, 1);break;
			case "str90":item = pc.getInventory().storeItem(600046, 1);break;
			case "dex90":item = pc.getInventory().storeItem(600047, 1);break;
			case "con90":item = pc.getInventory().storeItem(600048, 1);break;
			case "int90":item = pc.getInventory().storeItem(600049, 1);break;
			case "wis90":item = pc.getInventory().storeItem(600050, 1);break;
			case "str91":item = pc.getInventory().storeItem(600100, 1);break;
			case "dex91":item = pc.getInventory().storeItem(600101, 1);break;
			case "con91":item = pc.getInventory().storeItem(600102, 1);break;
			case "int91":item = pc.getInventory().storeItem(600103, 1);break;
			case "wis91":item = pc.getInventory().storeItem(600104, 1);break;
			case "str92":item = pc.getInventory().storeItem(600105, 1);break;
			case "dex92":item = pc.getInventory().storeItem(600106, 1);break;
			case "con92":item = pc.getInventory().storeItem(600107, 1);break;
			case "int92":item = pc.getInventory().storeItem(600108, 1);break;
			case "wis92":item = pc.getInventory().storeItem(600109, 1);break;
			case "str93":item = pc.getInventory().storeItem(600110, 1);break;
			case "dex93":item = pc.getInventory().storeItem(600111, 1);break;
			case "con93":item = pc.getInventory().storeItem(600112, 1);break;
			case "int93":item = pc.getInventory().storeItem(600113, 1);break;
			case "wis93":item = pc.getInventory().storeItem(600114, 1);break;
			case "str94":item = pc.getInventory().storeItem(600115, 1);break;
			case "dex94":item = pc.getInventory().storeItem(600116, 1);break;
			case "con94":item = pc.getInventory().storeItem(600117, 1);break;
			case "int94":item = pc.getInventory().storeItem(600118, 1);break;
			case "wis94":item = pc.getInventory().storeItem(600119, 1);break;
			case "str95":item = pc.getInventory().storeItem(600120, 1);break;
			case "dex95":item = pc.getInventory().storeItem(600121, 1);break;
			case "con95":item = pc.getInventory().storeItem(600122, 1);break;
			case "int95":item = pc.getInventory().storeItem(600123, 1);break;
			case "wis95":item = pc.getInventory().storeItem(600124, 1);break;
			case "str96":item = pc.getInventory().storeItem(600150, 1);break;
			case "dex96":item = pc.getInventory().storeItem(600151, 1);break;
			case "con96":item = pc.getInventory().storeItem(600152, 1);break;
			case "int96":item = pc.getInventory().storeItem(600153, 1);break;
			case "wis96":item = pc.getInventory().storeItem(600154, 1);break;
			case "str97":item = pc.getInventory().storeItem(600155, 1);break;
			case "dex97":item = pc.getInventory().storeItem(600156, 1);break;
			case "con97":item = pc.getInventory().storeItem(600157, 1);break;
			case "int97":item = pc.getInventory().storeItem(600158, 1);break;
			case "wis97":item = pc.getInventory().storeItem(600159, 1);break;
			case "str98":item = pc.getInventory().storeItem(600160, 1);break;
			case "dex98":item = pc.getInventory().storeItem(600161, 1);break;
			case "con98":item = pc.getInventory().storeItem(600162, 1);break;
			case "int98":item = pc.getInventory().storeItem(600163, 1);break;
			case "wis98":item = pc.getInventory().storeItem(600164, 1);break;
			case "str99":item = pc.getInventory().storeItem(600165, 1);break;
			case "dex99":item = pc.getInventory().storeItem(600166, 1);break;
			case "con99":item = pc.getInventory().storeItem(600167, 1);break;
			case "int99":item = pc.getInventory().storeItem(600168, 1);break;
			case "wis99":item = pc.getInventory().storeItem(600169, 1);break;
			}
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(403, item.getLogNameRef()), true);
			}
		} else {
			if (pc.isGm())
				pc.sendPackets(new S_SystemMessage("Dialog " + "riddle2"), true);																

			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "riddle2"), true);
		}
	}
}

