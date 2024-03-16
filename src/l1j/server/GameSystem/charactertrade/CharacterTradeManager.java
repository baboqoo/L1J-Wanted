package l1j.server.GameSystem.charactertrade;

import java.util.ArrayDeque;

import l1j.server.GameSystem.charactertrade.bean.CharacterTradeObject;
import l1j.server.GameSystem.charactertrade.loader.CharacterTradeLoader;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class CharacterTradeManager {
	public static final int MARBLE_STORE_ID	= 3000468;// 케릭터 저장 구슬
	public static final int MARBLE_LOAD_ID	= 3000469;// 캐릭터 봉인 구슬
	
	private static class newInstance {
		public static final CharacterTradeManager INSTANCE = new CharacterTradeManager();
	}
	public static CharacterTradeManager getInstance(){
		return newInstance.INSTANCE;
	}
	private CharacterTradeManager(){}
	
	private static ArrayDeque<String> parseToStringArray(String s){
		String[] arr 	= s.split(StringUtil.EmptyOneString);
		int size		= arr.length;
		ArrayDeque<String> argsQ = new ArrayDeque<String>(size);
		for (int i=0; i<size; i++) {
			try {
				argsQ.offer(arr[i]);
			} catch(Exception e) {
				break;
			}
		}
		return argsQ;
	}
	
	public static void commands(L1PcInstance pc, String param){
		try {
			ArrayDeque<String> argsQ = parseToStringArray(param);
			if (argsQ == null || argsQ.isEmpty()) {
				throw new Exception(StringUtil.EmptyString);
			}
			int cmd	= Integer.parseInt(argsQ.poll());
			switch(cmd){
			case 1:infoAskCommands(pc, argsQ.poll());break;
			case 2:inventoryAskCommands(pc, argsQ.poll());break;
			case 3:skillsAskCommands(pc, argsQ.poll());break;
			default:
				throw new Exception(StringUtil.EmptyString);
			}
			
		} catch(Exception e) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage(".구슬\n[1. 캐릭터정보] [2. 인벤토리] [3. 스킬]\n[캐릭터 이름]"), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(22), true), true);
		}
	}
	
	public static void infoAskCommands(L1PcInstance pc, String cName){
		CharacterTradeObject obj = CharacterTradeLoader.getInstance().get(cName);
		if (obj == null) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("캐릭터 정보를 찾을 수 없습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(23), true), true);
			return;
		}
		
		if (obj.getCharPck() != null) {
			pc.sendPackets(obj.getCharPck(), false);
		}
	}
	
	public static void inventoryAskCommands(L1PcInstance pc, String cName){
		CharacterTradeObject obj = CharacterTradeLoader.getInstance().get(cName);
		if (obj == null) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("캐릭터 정보를 찾을 수 없습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(23), true), true);
			return;
		}
		
		if (obj.getInvenPck() != null) {
			pc.sendPackets(obj.getInvenPck(), false);
		}
	}
	
	public static void skillsAskCommands(L1PcInstance pc, String cName){
		CharacterTradeObject obj = CharacterTradeLoader.getInstance().get(cName);
		if (obj == null) {
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("캐릭터 정보를 찾을 수 없습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(23), true), true);
			return;
		}
		
		if (obj.getSpellPck() != null) {
			pc.sendPackets(obj.getSpellPck(), false);
		}
	}
	
	public void load(){
		CharacterTradeLoader.getInstance();
	}
	
	public void  release(){
		CharacterTradeLoader.release();
	}
}


