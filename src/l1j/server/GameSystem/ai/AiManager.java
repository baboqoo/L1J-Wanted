package l1j.server.GameSystem.ai;

import java.util.ArrayDeque;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.GameSystem.ai.area.GikamFirstFloor;
import l1j.server.GameSystem.ai.area.GikamSecondFloor;
import l1j.server.GameSystem.ai.constuct.AiArea;
import l1j.server.GameSystem.ai.constuct.AiPledge;
import l1j.server.GameSystem.ai.constuct.AiType;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.StringUtil;

/**
 * AI 생성 클래스
 * @author LinOffice
 */
public class AiManager {
//AUTO SRM: 	private static final ServerBasePacket COMMAND_MSG		= new S_SystemMessage(".거울전쟁 [시작/종료/리로드] [1:기감1층, 2:기감2층, 5490:낚시] [1:붉은기사단, 2:검은기사단, 3:황금성단]"); // CHECKED OK
	private static final ServerBasePacket COMMAND_MSG		= new S_SystemMessage(S_SystemMessage.getRefText(1), true);
//AUTO SRM: 	private static final ServerBasePacket RELOAD_MSG		= new S_SystemMessage("거울전쟁 AI가 리로드 되었습니다."); // CHECKED OK
	private static final ServerBasePacket RELOAD_MSG		= new S_SystemMessage(S_SystemMessage.getRefText(2), true);
//AUTO SRM: 	private static final ServerBasePacket NO_ACTIVE_MSG		= new S_SystemMessage("거울전쟁은 현재 사용 중지 상태입니다."); // CHECKED OK
	private static final ServerBasePacket NO_ACTIVE_MSG		= new S_SystemMessage(S_SystemMessage.getRefText(3), true);
//AUTO SRM: 	private static final ServerBasePacket RESTART_MSG		= new S_SystemMessage("거울전쟁이 실행중입니다."); // CHECKED OK
	private static final ServerBasePacket RESTART_MSG		= new S_SystemMessage(S_SystemMessage.getRefText(4), true);
//AUTO SRM: 	private static final ServerBasePacket EMPTY_MSG			= new S_SystemMessage("거울전쟁이 실행중이지 않습니다."); // CHECKED OK
	private static final ServerBasePacket EMPTY_MSG			= new S_SystemMessage(S_SystemMessage.getRefText(5), true);
//AUTO SRM: 	private static final ServerBasePacket FAILE_PLEDGE_MSG	= new S_SystemMessage("해당 혈맹은 이미 가동 중입니다."); // CHECKED OK
	private static final ServerBasePacket FAILE_PLEDGE_MSG	= new S_SystemMessage(S_SystemMessage.getRefText(6), true);
//AUTO SRM: 	private static final ServerBasePacket FAILE_RELOAD_MSG	= new S_SystemMessage("거울전쟁 AI가 가동중에는 리로드할 수 없습니다.") // CHECKED OK;
	private static final ServerBasePacket FAILE_RELOAD_MSG	= new S_SystemMessage(S_SystemMessage.getRefText(7), true);
	
	//private static final String COMMAND_START	= "시작";
	//private static final String COMMAND_END		= "종료";
	//private static final String COMMAND_RELOAD	= "리로드";
	private static final String COMMAND_START	= "start";
	private static final String COMMAND_END		= "stop";
	private static final String COMMAND_RELOAD	= "reload";

	
	private static class newInstance {
		protected static final AiManager INSTANCE = new AiManager();
	}
	public static AiManager getInstance(){
		return newInstance.INSTANCE;
	}
	
	private final FastMap<AiArea, AiHandler> _handlers;
	private AiManager(){
		_handlers = new FastMap<AiArea, AiHandler>();
	}
	
	public void commands(L1PcInstance pc, String param){
		try {
			ArrayDeque<String> argsQ = parseToStringArray(param);
			if (argsQ == null || argsQ.isEmpty()) {
				throw new Exception(StringUtil.EmptyString);
			}
			String cmd	= argsQ.poll();
			switch(cmd){
			case COMMAND_START:
				commandStart(pc, Integer.parseInt(argsQ.poll()), Integer.parseInt(argsQ.poll()));
				break;
			case COMMAND_END:
				commandEnd(pc, Integer.parseInt(argsQ.poll()));
				break;
			case COMMAND_RELOAD:
				reload(pc);
				break;
			default:
				pc.sendPackets(COMMAND_MSG);
				break;
			}
		} catch(Exception e) {
			pc.sendPackets(COMMAND_MSG);
		}
	}
	
	static ArrayDeque<String> parseToStringArray(String s){
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
	
	void commandStart(L1PcInstance pc, int aiType, int teamType){
		if (!Config.DUNGEON.CLONE_WAR_ACTIVE) {
			pc.sendPackets(NO_ACTIVE_MSG);
			return;
		}
		AiArea aiArea		= AiArea.fromInt(aiType);
		if (aiArea == AiArea.FISHING) {
			commandFish(pc, true);
			return;
		}
		AiHandler handler	= getHandler(aiArea);
		if (handler != null) {
			pc.sendPackets(RESTART_MSG);
			return;
		}
		AiPledge pledge = AiPledge.fromInt(teamType);
		if (isPledgeActive(pledge)) {
			pc.sendPackets(FAILE_PLEDGE_MSG);
			return;
		}
		handler = createHandler(aiArea, pledge);
		handler.start();
		_handlers.put(aiArea, handler);
	}
	
	public void timeStart(AiArea aiArea){
		AiHandler handler = getHandler(aiArea);
		if (handler != null) {
			return;// 이미 가동중
		}
		AiPledge clanType = null;// 등장 클랜
		while (clanType == null) {
			AiPledge temp = AiPledge.fromInt(CommonUtil.random(3) + 1);
			if (!isPledgeActive(temp)) {
				clanType = temp;
				break;
			}
		}
		handler = createHandler(aiArea, clanType);
		handler.start();
		_handlers.put(aiArea, handler);
	}
	
	AiHandler createHandler(AiArea aiArea, AiPledge pledge){
		switch(aiArea){
		case GIKAM_FIRST_FLOOR:
			return new GikamFirstFloor(aiArea, pledge, 3600);
		case GIKAM_SECOND_FLOOR:
			return new GikamSecondFloor(aiArea, pledge, 3600);
		default:
			return null;
		}
	}
	
	void commandEnd(L1PcInstance pc, int aiType){
		AiArea aiArea		= AiArea.fromInt(aiType);
		if (aiArea == AiArea.FISHING) {
			commandFish(pc, false);
			return;
		}
		AiHandler handler	= getHandler(aiArea);
		if (handler == null) {
			pc.sendPackets(EMPTY_MSG);
			return;
		}
		handler.end();
		delete(aiArea);
	}
	
	public AiHandler getHandler(AiArea aiArea){
		return _handlers.get(aiArea);
	}
	
	boolean isPledgeActive(AiPledge pledge){
		for (AiHandler handler : _handlers.values()) {
			if (handler._pledge == pledge) {
				return true;
			}
		}
		return false;
	}
	
	void commandFish(L1PcInstance pc, boolean flag) {
		if (flag) {
			FastMap<String, FastTable<L1AiUserInstance>> map = AiLoader.getInstance().getTypeUsers(AiType.FISHING);
			if (map == null || map.isEmpty()) {
				return;
			}
			for (FastTable<L1AiUserInstance> pledge : map.values()) {
				if (pledge == null || pledge.isEmpty()) {
					continue;
				}
				for (L1AiUserInstance user : pledge) {
					user.login(AiArea.FISHING);
				}
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("낚시 AI를 배치하였습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(8), true), true);
		} else {
			for (L1AiUserInstance user : L1World.getInstance().getAllAiUsers()) {
				if (user == null || user.getAiType() != AiType.FISHING) {
					continue;
				}
				user.logout();
			}
//AUTO SRM: 			pc.sendPackets(new S_SystemMessage("낚시 AI를 종료하였습니다."), true); // CHECKED OK
			pc.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(9), true), true);
		}
	}
	
	protected void delete(AiArea aiArea){
		_handlers.remove(aiArea);
	}
	
	void reload(L1PcInstance pc){
		if (!L1World.getInstance().getAllAiUsers().isEmpty()) {
			pc.sendPackets(FAILE_RELOAD_MSG);
			return;
		}
		AiLoader.reload();
		pc.sendPackets(RELOAD_MSG);
	}
}


