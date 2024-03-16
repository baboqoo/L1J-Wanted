package l1j.server.server.model;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.serverpackets.message.S_MessageYN;
import l1j.server.server.utils.StringUtil;

public class L1Question implements Runnable{
	private static L1Question _instance;
	public static String maintext;
	public static int good;
	public static int bad;
	public static boolean mainstart;

	public static final int EXECUTE_STATUS_NONE		= 0;
	public static final int EXECUTE_STATUS_PREPARE	= 1;
	public static final int EXECUTE_STATUS_PROGRESS	= 3;
	public static final int EXECUTE_STATUS_FINALIZE	= 4;
	
	private int _executeStatus = EXECUTE_STATUS_NONE;

	public static L1Question getInstance(String text) {
		if (_instance == null) {
			_instance = new L1Question(text);			
		}
		return _instance;
	}

	private L1Question(String text){
		good = 0;
		bad = 0;
		maintext = text;
		GeneralThreadPool.getInstance().execute(this);
	}

	@Override
	public void run(){
		try {
			switch(_executeStatus){
			case EXECUTE_STATUS_NONE:
				mainstart = true;
//AUTO SRM: 				L1World.getInstance().broadcastServerMessage(" \\fY잠시 후 설문조사가 시작됩니다. (제한시간 30초)", true); // CHECKED OK
				L1World.getInstance().broadcastServerMessage(S_SystemMessage.getRefText(1105), true);
//AUTO SRM: 				L1World.getInstance().broadcastServerMessage(" YES = 찬성, NO = 반대, 그외 무효~!", true); // CHECKED OK
				L1World.getInstance().broadcastServerMessage(S_SystemMessage.getRefText(1106), true);
				_executeStatus = EXECUTE_STATUS_PREPARE;
				GeneralThreadPool.getInstance().schedule(this, 3000L);
				break;

			case EXECUTE_STATUS_PREPARE:
				L1World.getInstance().broadcastPacketToAll(new S_MessageYN(622, maintext), true);
				_executeStatus = EXECUTE_STATUS_PROGRESS;					
				GeneralThreadPool.getInstance().schedule(this, 30000L);
				break;

			case EXECUTE_STATUS_PROGRESS:
//AUTO SRM: 				L1World.getInstance().broadcastServerMessage(" 잠시 후 설문조사 결과가 발표됩니다.", true); // CHECKED OK
				L1World.getInstance().broadcastServerMessage(S_SystemMessage.getRefText(1107), true);
				_executeStatus = EXECUTE_STATUS_FINALIZE;
				GeneralThreadPool.getInstance().schedule(this, 3000L);
				break;

			case EXECUTE_STATUS_FINALIZE:
//AUTO SRM: 				L1World.getInstance().broadcastServerMessage(" \\fW[결과] 찬성 : " + good + "표, 반대 : " + bad + "표", true); // CHECKED OK
				L1World.getInstance().broadcastServerMessage(S_SystemMessage.getRefText(1108) + good  + S_SystemMessage.getRefText(1109) + bad  + S_SystemMessage.getRefText(1110), true);
				_instance = null;		
				mainstart = false;
				maintext = StringUtil.EmptyString;			
				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}


