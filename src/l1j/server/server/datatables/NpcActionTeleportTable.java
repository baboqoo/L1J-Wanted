package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.construct.L1Alignment;
import l1j.server.server.model.npc.action.bean.L1NpcActionTeleport;
import l1j.server.server.model.npc.action.bean.L1NpcActionTeleport.NeedItem;
import l1j.server.server.model.npc.action.bean.L1NpcActionTeleport.TelType;
import l1j.server.server.model.npc.action.bean.L1NpcActionTeleport.TeleportInfo;
import l1j.server.server.model.npc.action.bean.L1NpcActionTeleport.TeleportInfoRandom;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 엔피씨 텔레포트 정보
 * @author LinOffice
 */
public class NpcActionTeleportTable {
	private static NpcActionTeleportTable _instance;
	public static NpcActionTeleportTable getInstance(){
		if (_instance == null) {
			_instance = new NpcActionTeleportTable();
		}
		return _instance;
	}
	
	private static final FastMap<Integer, FastMap<String, L1NpcActionTeleport>> DATA = new FastMap<Integer, FastMap<String,L1NpcActionTeleport>>();
	
	public static L1NpcActionTeleport getTeleport(int npcId, String action){
		if (!DATA.containsKey(npcId)) {
			return null;
		}
		FastMap<String, L1NpcActionTeleport> map = DATA.get(npcId);
		if (!map.containsKey(action)) {
			return null;
		}
		return map.get(action);
	}
	
	private NpcActionTeleportTable(){
		load();
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		TeleportInfo telInfo	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM npcaction_teleport ORDER BY npcId ASC, actionName ASC");
			rs		= pstm.executeQuery();
			while(rs.next()){
				int npcId						= rs.getInt("npcId");
				String actionName				= rs.getString("actionName");
				int needLevel					= rs.getInt("needLevel");
				int limitLevel					= rs.getInt("limitLevel");
				int needTimerId					= rs.getInt("needTimerId");
				FastTable<NeedItem> needItem	= getNeedItemParse(rs.getString("needItem"));
				FastTable<Integer> needBuff		= getNeedBuffParse(rs.getString("needBuff"));
				boolean needPcroomBuff			= Boolean.parseBoolean(rs.getString("needPcroomBuff"));
				telInfo							= new TeleportInfo(rs.getInt("telX"), rs.getInt("telY"), rs.getInt("telMapId"), rs.getInt("telRange"), rs.getInt("telTownId"), getTelTypeParse(rs.getString("telType")));
				if (telInfo.getTelType() == TelType.RANDOM) {
					telInfo.setRandomMap(getRandomMapParse(rs.getString("randomMap")));
				}
				
				String successActionName		= rs.getString("successActionName");
				String failLevelActionName		= rs.getString("failLevelActionName");
				String failItemActionName		= rs.getString("failItemActionName");
				String failBuffActionName		= rs.getString("failBuffActionName");
				L1Alignment failAlign			= getAlignTypeParse(rs.getString("failAlignment"));
				
				FastMap<String, L1NpcActionTeleport> map = DATA.get(npcId);
				if (map == null) {
					map = new FastMap<String, L1NpcActionTeleport>();
					DATA.put(npcId, map);
				}
				map.put(actionName, new L1NpcActionTeleport(npcId, actionName, needLevel, limitLevel, needTimerId, needItem, needBuff, needPcroomBuff, telInfo, successActionName, failLevelActionName, failItemActionName, failBuffActionName, failAlign));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void reload(){
		for (FastMap<String, L1NpcActionTeleport> map : DATA.values()) {
			for (L1NpcActionTeleport tel : map.values()) {
				if (tel.getNeedItem() != null) {
					tel.getNeedItem().clear();
				}
				if (tel.getNeedBuff() != null) {
					tel.getNeedBuff().clear();
				}
			}
			map.clear();
		}
		DATA.clear();
		load();
	}
	
	private TelType getTelTypeParse(String str){
		if (StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		switch(str){
		case "inter":	return TelType.INTER;
		case "random":	return TelType.RANDOM;
		default:		return TelType.NORMAL;
		}
	}
	
	private L1Alignment getAlignTypeParse(String str){
		if (StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		switch(str){
		case "caotic":	return L1Alignment.CAOTIC;
		case "neutral":	return L1Alignment.NEUTRAL;
		case "lawful":	return L1Alignment.LAWFUL;
		default:		return null;
		}
	}
	
	private String trim(String str){
		str = str.replaceAll(StringUtil.EmptyOneString, StringUtil.EmptyString);
		str = str.replaceAll("ITEMID:", StringUtil.EmptyString);
		str = str.replaceAll("COUNT:", StringUtil.EmptyString);
		str = str.replaceAll("REMOVE:", StringUtil.EmptyString);
		return str;
	}
	
	private FastTable<NeedItem> getNeedItemParse(String str){
		if (StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		str = trim(str);
		String[] array = str.split("===============");
		FastTable<NeedItem> needItemList = new FastTable<NeedItem>();
		for (int i=0; i<array.length; i++) {
			if (StringUtil.isNullOrEmpty(array[i])) {
				continue;
			}
			String[] itemArray = array[i].trim().split(StringUtil.LineString);
			needItemList.add(new NeedItem(
					Integer.parseInt(itemArray[0].trim()), 
					Integer.parseInt(itemArray[1].trim()), 
					Boolean.parseBoolean(itemArray[2].trim())));
		}
		return needItemList;
	}
	
	private FastTable<Integer> getNeedBuffParse(String str){
		if (StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		String[] array = str.split(StringUtil.CommaString);
		FastTable<Integer> needBuffList = new FastTable<Integer>();
		for (int i=0; i<array.length; i++) {
			needBuffList.add(Integer.parseInt(array[i].trim()));
		}
		return needBuffList;
	}
	
	private LinkedList<TeleportInfoRandom> getRandomMapParse(String str) {
		if (StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		String[] array = str.split(StringUtil.LineString);
		LinkedList<TeleportInfoRandom> randoms = new LinkedList<L1NpcActionTeleport.TeleportInfoRandom>();
		for (int i=0; i<array.length; i++) {
			int number = 0, x = 0, y = 0, range = 0, prob = 0;
			StringTokenizer st = new StringTokenizer(array[i].trim(), StringUtil.CommaString);
			while (st.hasMoreElements()) {
				String token = st.nextToken().trim();
				if (token.startsWith("NUMBER:")) {
					token = token.replace("NUMBER:", StringUtil.EmptyString);
					number = Integer.parseInt(token.trim());
				} else if (token.startsWith("X:")) {
					token = token.replace("X:", StringUtil.EmptyString);
					x = Integer.parseInt(token.trim());
				} else if (token.startsWith("Y:")) {
					token = token.replace("Y:", StringUtil.EmptyString);
					y = Integer.parseInt(token.trim());
				} else if (token.startsWith("RANGE:")) {
					token = token.replace("RANGE:", StringUtil.EmptyString);
					range = Integer.parseInt(token.trim());
				} else if (token.startsWith("PROB:")) {
					token = token.replace("PROB:", StringUtil.EmptyString);
					prob = Integer.parseInt(token.trim());
				}
			}
			randoms.add(new TeleportInfoRandom(x, y, number, range, prob));
		}
		return randoms;
	}
}

