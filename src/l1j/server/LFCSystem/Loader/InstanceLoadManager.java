package l1j.server.LFCSystem.Loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.LFCSystem.InstanceObject;
import l1j.server.LFCSystem.InstanceSpace;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;

public class InstanceLoadManager {
	private static Logger 		_log 		= Logger.getLogger(InstanceLoadManager.class.getName());
	private static final String _fileName 	= "./config/lfc.properties";
	private static Properties 	_settings;
	
	private static InstanceLoadManager _instance;
	public static InstanceLoadManager getInstance(){
		if (_instance == null)
			_instance = new InstanceLoadManager();
		return _instance;
	}
	
	private static ArrayDeque<Integer> parseToIntArray(String s){
		String[] arr 	= s.split(StringUtil.EmptyOneString);
		int size		= arr.length;
		ArrayDeque<Integer> argsQ = new ArrayDeque<Integer>(size);
		for (int i = 0; i < size; i++){
			try {
				argsQ.offer(Integer.parseInt(arr[i]));
			} catch(Exception e){
				break;
			}
		}
		return argsQ;
	}
	
//AUTO SRM: 	private static final S_SystemMessage _basicMenus = new S_SystemMessage("[1. 상태], [2. 리로드], [3. 강제종료]"); // CHECKED OK
	private static final S_SystemMessage _basicMenus = new S_SystemMessage(S_SystemMessage.getRefText(58), true);
	public static void commands(L1PcInstance gm, String param){
		try {
			ArrayDeque<Integer> argsQ = parseToIntArray(param);
			if (argsQ == null || argsQ.isEmpty())
				throw new Exception(StringUtil.EmptyString);
			
			switch(argsQ.poll()){
			case 1: 	statusCommands(gm, argsQ); break;
			case 2: 	reloadCommands(gm, argsQ); break;
			case 3:		closeCommands(gm, argsQ); break;
			default: 	throw new Exception(StringUtil.EmptyString);
			}
			
		} catch(Exception e){
			gm.sendPackets(_basicMenus, false);
		}
	}

//AUTO SRM: 	private static final S_SystemMessage _statusMenus = new S_SystemMessage( "[사용방법].인스턴스 1\n" + "[1. 남은공간], [2. 열린맵], [3. 상세정보] \n" + "[맵아이디]"); // CHECKED OK
	private static final S_SystemMessage _statusMenus = new S_SystemMessage(S_SystemMessage.getRefText(59) + S_SystemMessage.getRefText(60) + S_SystemMessage.getRefText(61), true);
	
	private static void statusCommands(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		if (argsQ.isEmpty()){
			gm.sendPackets(_statusMenus, false);
			return;
		}
		
		StringBuilder sb = new StringBuilder(256);
		switch(argsQ.poll()){
		case 1:{
			//sb.append("총 ").append(MIS_COPYMAP_SIZE).append("개의 공간 중\n");
			sb.append("Total ").append(MIS_COPYMAP_SIZE).append(" of spaces\n");
			Iterator<Integer> it = InstanceSpace.getInstance().getOpensMaps().iterator();
			Integer i;
			int cnt = 0;
			while(it.hasNext()){
				i = it.next();
				if (!(i == null || i < MIS_COPYMAP_START_ID || i > MIS_COPYMAP_START_ID + MIS_COPYMAP_SIZE)){
					if (InstanceSpace.getInstance().getOpenObject(i) != null)
						cnt++;
				}
			}
			//sb.append(cnt).append("개 사용 중입니다.");
			sb.append(cnt).append("Units in use.");
			break;
		}
		case 2:{
			Iterator<Integer> it = InstanceSpace.getInstance().getOpensMaps().iterator();
			Integer i;
			//sb.append("열린 맵 : ");
			sb.append("Open map: ");
			while(it.hasNext()){
				i = it.next();
				if (!(i == null || i < MIS_COPYMAP_START_ID || i > MIS_COPYMAP_START_ID + MIS_COPYMAP_SIZE)){
					if (InstanceSpace.getInstance().getOpenObject(i) != null)
						sb.append(i).append(StringUtil.EmptyOneString);
				}
			}
			break;
		}
		case 3:{
			InstanceObject obj = null;
			if (argsQ.isEmpty()){
				gm.sendPackets(_statusMenus, false);
				return;
			}
			obj = InstanceSpace.getInstance().getOpenObject(argsQ.poll());
			if (obj == null)
				//sb.append("해당 맵은 열려 있지 않습니다.");
				sb.append("The map is not open.");
			else
				sb.append(obj.toString());
			break;
		}
		default:
			gm.sendPackets(_statusMenus, false);
			break;
		}
		
		gm.sendPackets(new S_SystemMessage(sb.toString()));
	}
	
//AUTO SRM: 	private static final S_SystemMessage _reloadMenus = new S_SystemMessage( "[사용방법].인스턴스 2\n" + "[1. 컨픽][2. 타입][3. 보상]\n" + "[4. 전체]" ); // CHECKED OK
	private static final S_SystemMessage _reloadMenus = new S_SystemMessage(S_SystemMessage.getRefText(62) + S_SystemMessage.getRefText(63) + S_SystemMessage.getRefText(64), true);
	private static void reloadCommands(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		if (argsQ.isEmpty()){
			gm.sendPackets(_reloadMenus, false);
			return;
		}
		
		String msg = null;
		switch(argsQ.poll()){
		case 1:
			loadConfig();
			msg = "[Config reload Completed.]";
			break;
			
		case 2:
			LFCTypeLoader.reload();
			msg = "[TypesLoader reload Completed.]";
			break;
			
		case 3:
			LFCCompensateLoader.reload();
			msg = "[Compensatros reload Completed.]";
			break;
			
		case 4:
			if(_instance != null)
				_instance.reload();
			msg = "[InstanceSystem reload Completed.]";
			break;
			
		default:
			gm.sendPackets(_reloadMenus, false);
			return;
		}
		
		gm.sendPackets(new S_SystemMessage(msg, true));
	}
	
//AUTO SRM: 	private static final S_SystemMessage _closeMenus = new S_SystemMessage( "[사용방법].인스턴스 3 [맵아이디]" ); // CHECKED OK
	private static final S_SystemMessage _closeMenus = new S_SystemMessage(S_SystemMessage.getRefText(65), true);
	private static void closeCommands(L1PcInstance gm, ArrayDeque<Integer> argsQ){
		if (argsQ.isEmpty()){
			gm.sendPackets(_closeMenus, false);
			return;
		}
		
		StringBuilder 		sb	= new StringBuilder(128);
		int 				mid	= argsQ.poll();
		InstanceObject 	obj = InstanceSpace.getInstance().getOpenObject(mid);
		sb.append("[").append(mid).append("]");
		if (obj == null)
			//sb.append("해당 맵은 열려 있지 않습니다.");
			sb.append("The map is not open.");
		else {
			obj.closeForGM();
			//sb.append("해당 맵을 강제로 종료시켰습니다.\n").append("강제 종료는 보상을 지급하지 않습니다.");
			sb.append("The map was forced to close.\n").append("Forced closure does not provide rewards.");
		}
		gm.sendPackets(new S_SystemMessage(sb.toString()));
	}
	
	private InstanceLoadManager(){}
	public void load(){
		loadConfig();
		LFCTypeLoader.getInstance();
		LFCCompensateLoader.getInstance();
		InstanceSpace.getInstance();
	}
	
	public void reload(){
		loadConfig();
		LFCTypeLoader.reload();
		LFCCompensateLoader.reload();
		InstanceSpace.reload();
	}
	
	public void release(){
		LFCTypeLoader.release();
		LFCCompensateLoader.release();
		InstanceSpace.release();
	}
	
	private static void loadConfig(){
		try {
			_settings = new Properties();
			InputStream is = new FileInputStream(new File(_fileName));
			_settings.load(is);
			is.close();
			
			loadSystemConfig(_settings);
			loadEffectConfig(_settings);
			loadSpawnConfig(_settings);
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static int MIS_ERRBACK_MAPID;
	public static int MIS_ERRBACK_X;
	public static int MIS_ERRBACK_Y;
	public static int MIS_COPYMAP_START_ID;
	public static int MIS_COPYMAP_SIZE;
	
	private static void loadSystemConfig(Properties settings){
		String column	= StringUtil.EmptyString;
		try {
			column	= "ErrorBackMapId";
			MIS_ERRBACK_MAPID		= Integer.parseInt(settings.getProperty(column, "4"));
			
			column	= "ErrorBackX";
			MIS_ERRBACK_X			= Integer.parseInt(settings.getProperty(column, "33090"));
			
			column	= "ErrorBackY";
			MIS_ERRBACK_Y			= Integer.parseInt(settings.getProperty(column, "33402"));
			
			column	= "CopyMap_Start_Id";
			MIS_COPYMAP_START_ID 	= Integer.parseInt(settings.getProperty(column, "15000"));
			
			column	= "CopyMap_Size";
			MIS_COPYMAP_SIZE 		= Integer.parseInt(settings.getProperty(column, "200"));
		} catch(Exception e){
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			e.printStackTrace();
		}
	}
	
	public static int MIS_EFF_THORNDMG_MIN;
	public static int MIS_EFF_THORNDMG_MAX;
	
	private static void loadEffectConfig(Properties settings){
		String column	= StringUtil.EmptyString;
		try {
		
			column					= "ThornDamageMin";
			MIS_EFF_THORNDMG_MIN	= Integer.parseInt(settings.getProperty(column, "15"));
			column					= "ThornDamageMax";
			MIS_EFF_THORNDMG_MAX	= Integer.parseInt(settings.getProperty(column, "25"));
			
		} catch(Exception e){
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Integer> MIS_SP_BUFF_IDS;
	public static int MIS_SP_BOX_ID;
	public static int MIS_SP_TOWER_ID;
	public static int MIS_SP_BOUNDARY_ID;
	public static int MIS_SP_THORN_ID;
	public static int MIS_SP_THORNSHADOW_ID;
	public static int MIS_SP_THORN_TIME;
	public static int MIS_SP_RTRAP_TIME;
	public static int MIS_SP_RTRAP_RAITO;
	public static int MIS_SP_RCTRAP_RATIO;
	
	private static void loadSpawnConfig(Properties settings){
		String column = StringUtil.EmptyString;
		
		try {
			MIS_SP_BUFF_IDS			= new ArrayList<Integer>(6);
			int buffId				= 0;
			int idx					= 1;
			while(true){
				buffId = Integer.parseInt(settings.getProperty(String.format("buff%d", idx), "-1"));
				if (buffId == -1)
					break;
				
				MIS_SP_BUFF_IDS.add(buffId);
				idx++;
			}
			
			column					= "BoxId";
			MIS_SP_BOX_ID			= Integer.parseInt(settings.getProperty(column, "100000000"));
			column					= "TowerId";
			MIS_SP_TOWER_ID			= Integer.parseInt(settings.getProperty(column, "100000001"));
			column					= "BoundaryId";
			MIS_SP_BOUNDARY_ID		= Integer.parseInt(settings.getProperty(column, "100000002"));
			column					= "ThornId";
			MIS_SP_THORN_ID			= Integer.parseInt(settings.getProperty(column, "100000003"));
			column					= "ThornShadowId";
			MIS_SP_THORNSHADOW_ID	= Integer.parseInt(settings.getProperty(column, "100000004"));			
			column					= "ThornSpawnTime";
			MIS_SP_THORN_TIME		= Integer.parseInt(settings.getProperty(column, "30"));
			column					= "RandomTrapSpawnTime";
			MIS_SP_RTRAP_TIME		= Integer.parseInt(settings.getProperty(column, "30"));
			column					= "RandomTrapRatio";
			MIS_SP_RTRAP_RAITO		= Integer.parseInt(settings.getProperty(column, "50"));
			column					= "RandomTrapChaosRatio";
			MIS_SP_RCTRAP_RATIO		= Integer.parseInt(settings.getProperty(column, "50"));
		} catch(Exception e){
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			e.printStackTrace();
		}
	}
	
	
}


