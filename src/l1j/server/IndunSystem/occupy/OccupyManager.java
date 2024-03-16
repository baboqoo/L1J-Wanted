package l1j.server.IndunSystem.occupy;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class OccupyManager {
	private final Map<OccupyType, OccupyHandler> _handlers;
	
	private static class newInstance {
		public static final OccupyManager INSTANCE = new OccupyManager();
	}
	public static OccupyManager getInstance() {
		return newInstance.INSTANCE;
	}
	private OccupyManager(){
		_handlers	= new ConcurrentHashMap<OccupyType, OccupyHandler>();
	}
	
//AUTO SRM: 	private static final S_SystemMessage COMMAND_MESSAGE		= new S_SystemMessage(".점령전 [시작/종료] [하이네/윈다우드] [대기:분]"); // CHECKED OK
	private static final S_SystemMessage COMMAND_MESSAGE		= new S_SystemMessage(S_SystemMessage.getRefText(40), true);
//AUTO SRM: 	private static final S_SystemMessage COMMAND_OTHER_CASTLE	= new S_SystemMessage("성이름 [하이네/윈다우드]"); // CHECKED OK
	private static final S_SystemMessage COMMAND_OTHER_CASTLE	= new S_SystemMessage(S_SystemMessage.getRefText(41), true);
//AUTO SRM: 	private static final S_SystemMessage COMMAND_STAY_TIME		= new S_SystemMessage("대기시간: 1~30(단위:분)"); // CHECKED OK
	private static final S_SystemMessage COMMAND_STAY_TIME		= new S_SystemMessage(S_SystemMessage.getRefText(42), true);
	//private static final String COMMAND_START	= "시작";
	//private static final String COMMAND_END		= "종료";
	private static final String COMMAND_START 	= "start";
	private static final String COMMAND_END		= "stop";
	
	public void command(L1PcInstance pc, String param){
		try {
			StringTokenizer tok	= new StringTokenizer(param);
			String onoff		= tok.nextToken();
			String name			= tok.nextToken();
			
			if(!(name.equals(OccupyType.HEINE.getDesc()) || name.equals(OccupyType.WINDAWOOD.getDesc()))){
				pc.sendPackets(COMMAND_OTHER_CASTLE);
				return;
			}
			
			OccupyType occupyType = name.equals(OccupyType.HEINE.getDesc()) ? OccupyType.HEINE : OccupyType.WINDAWOOD;
			if (onoff.equals(COMMAND_START)) {
				int minute			= Integer.parseInt(tok.nextToken());
				if (minute <= 0 || minute > 30) {
					pc.sendPackets(COMMAND_STAY_TIME);
					return;
				}
				create(pc, occupyType, minute);
			} else if (onoff.equals(COMMAND_END)) {
				OccupyHandler handler = getHandler(occupyType);
				if (handler == null || !handler.isRunning()) {
					//pc.sendPackets(new S_SystemMessage(String.format("%s이 가동중이지 않습니다.", occupyType.getDesc())), true);
					pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(6), occupyType.getDesc()), true);
					return;
				}
				handler.setRunning(false);
				//pc.sendPackets(new S_SystemMessage(String.format("%s을 종료하였습니다.", occupyType.getDesc())), true);
				pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(7), occupyType.getDesc()), true);
			} else {
				pc.sendPackets(COMMAND_MESSAGE);
			}
		} catch (Exception e) {
			pc.sendPackets(COMMAND_MESSAGE);
		}
	}
	
	public boolean create(L1PcInstance pc, OccupyType type, int minut){
		OccupyHandler handler = getHandler(type);
		if (handler != null) {
			//pc.sendPackets(new S_SystemMessage(String.format("%s이 이미 가동 중입니다.", type.getDesc())), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(8), type.getDesc()), true);
			return false;
		}
		handler = createHandler(type);
		if (handler == null) {
			//pc.sendPackets(new S_SystemMessage(String.format("%s 핸들러를 생성할 수 없습니다.", type.getDesc())), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(9), type.getDesc()), true);
			return false;
		}
		handler.start(minut);
		//pc.sendPackets(new S_SystemMessage(String.format("%s이 %d분 뒤 시작합니다.", type.getDesc(), minut)), true);
		pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(10), type.getDesc(), String.valueOf(minut)), true);
		_handlers.put(type, handler);// 핸들러 등록
		return true;
	}
	
	// 핸들러 생성
	private OccupyHandler createHandler(OccupyType type){
		switch(type){
		case HEINE:		return new l1j.server.IndunSystem.occupy.action.Heine(type);
		case WINDAWOOD:	return new l1j.server.IndunSystem.occupy.action.Windawood(type);
		default:		return null;
		}
	}
	
	// 핸들러 목록
	public Map<OccupyType, OccupyHandler> getHandlers(){
		return _handlers;
	}
	
	// 핸들러 취득
	public OccupyHandler getHandler(OccupyType type){
		return _handlers.get(type);
	}
	
	// 핸들러 제거
	public void removeHandler(OccupyType type){
		_handlers.remove(type);
	}
}


