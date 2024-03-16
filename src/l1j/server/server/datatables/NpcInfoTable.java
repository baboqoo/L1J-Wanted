package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 엔피씨 상세 정보
 * @author LinOffice
 */
public class NpcInfoTable {
	private static Logger _log = Logger.getLogger(NpcInfoTable.class.getName());
	private static final FastMap<Integer, NpcInfoData> DATA = new FastMap<>();
	
	public static final String TYPE_SELF	= "self";
	public static final String TYPE_SCREEN	= "screen";
	public static final String TYPE_MAP		= "map";
	
	public static enum ScriptType {
		NONE, NUMBER, TEXT
	}
	
	private static NpcInfoTable _instance;
	public static NpcInfoTable getInstance() {
		if (_instance == null) {
			_instance = new NpcInfoTable();
		}
		return _instance;
	}
	
	public static NpcInfoData getNpcInfo(int npcId){
		return DATA.containsKey(npcId) ? DATA.get(npcId) : null;
	}

	private NpcInfoTable() {
		load();
	}

	private void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		NpcInfoData data		= null;
		ScriptInfo script		= null;
		NpcTable nt				= NpcTable.getInstance();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM npc_info");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int npcId = rs.getInt("npcId");
				L1Npc template = nt.getTemplate(npcId);
				if (template == null) {
					System.out.println(String.format("[NpcInfoTable] NPC_TEMPLATE_NOT_FOUND : NPC_ID(%d)", npcId));
					continue;
				}
				boolean recall		= Boolean.valueOf(rs.getString("recall"));
				int spawnActionId	= rs.getInt("spawnActionId");
				boolean reward		= Boolean.valueOf(rs.getString("reward"));
				String rewardRange	= rs.getString("rewardRange");
				int rewardItemId	= rs.getInt("rewardItemId");
				int rewardItemCount = rs.getInt("rewardItemCount");
				int rewardEinhasad	= rs.getInt("rewardEinhasad");
				int rewardNcoin		= rs.getInt("rewardNcoin");
				int rewardGfx		= rs.getInt("rewardGfx");
				String msgRange		= rs.getString("msgRange");
				
				String[] spawnMsgArray = null;
				String spawnMsg = rs.getString("spawnMsg");
				if (spawnMsg != null) {
					spawnMsgArray = spawnMsg.split(StringUtil.CommaString);
				}
				
				String[] dieMsgArray = null;
				String dieMsg = rs.getString("dieMsg");
				if (dieMsg != null) {
					dieMsgArray = dieMsg.split(StringUtil.CommaString);
				}
				
				boolean dieMsgPcList	= Boolean.valueOf(rs.getString("dieMsgPcList"));
				boolean autoLoot		= Boolean.valueOf(rs.getString("autoLoot"));
				int transformChance		= rs.getInt("transformChance");
				int transformId			= rs.getInt("transformId");
				int transformGfxId		= rs.getInt("transformGfxId");
				ScriptType scriptType	= getScriptType(rs.getString("scriptType"));
				if (scriptType != ScriptType.NONE) {
					script = getScriptInfo(rs.getString("scriptContent"), scriptType);
				}
				
				data = new NpcInfoData(npcId, recall, spawnActionId, 
						reward, rewardRange, rewardItemId, rewardItemCount, rewardEinhasad, rewardNcoin, rewardGfx, 
						msgRange, spawnMsgArray, dieMsgArray, dieMsgPcList, autoLoot, 
						transformChance, transformId, transformGfxId, script);
				template.setInfo(data);
				DATA.put(data._npcId, data);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private ScriptInfo getScriptInfo(String str, ScriptType scriptType){
		ScriptInfo script = new ScriptInfo();
		script._type = scriptType;
		StringTokenizer st = new StringTokenizer(str, "\r\n");
		while (st.hasMoreTokens()) {
			String temp = st.nextToken().trim();
			if (temp.startsWith("map:")) {
				temp = temp.substring(temp.indexOf(StringUtil.ColonString) + 1).trim();
				script._map = scriptType == ScriptType.NUMBER ? (Integer) Integer.parseInt(temp) : (String) temp;
			} else if (temp.startsWith("effect:")) {
				temp = temp.substring(temp.indexOf(StringUtil.ColonString) + 1).trim();
				script._effect = scriptType == ScriptType.NUMBER ? (Integer) Integer.parseInt(temp) : (String) temp;
			} else if (temp.startsWith("stay:")) {
				temp = temp.substring(temp.indexOf(StringUtil.ColonString) + 1).trim();
				script._stay = scriptType == ScriptType.NUMBER ? (Integer) Integer.parseInt(temp) : (String) temp;
			}
		}
		return script;
	}
	
	private ScriptType getScriptType(String str) {
		switch(str){
		case "number":	return ScriptType.NUMBER;
		case "text":	return ScriptType.TEXT;
		default:		return ScriptType.NONE;
		}
	}

	public static void reload() {
		DATA.clear();
		_instance.load();
	}
	
	public class NpcInfoData {
		public int _npcId;
		public boolean _recall;
		public int _spawnActionId;
		public boolean _reward;
		public String _rewardRange;
		public int _rewardItemId;
		public int _rewardItemCount;
		public int _rewardEinhasad;
		public int _rewardNcoin;
		public int _rewardGfx;
		public String _msgRange;
		public String[] _spawnMsg;
		public String[] _dieMsg;
		public boolean _dieMsgPcList;
		public boolean _autoLoot;
		public int _transformChance;
		public int _transformId;
		public int _transformGfxId;
		public ScriptInfo _scriptInfo;
		
		public NpcInfoData(int npcId, boolean recall, int spawnActionId,
				boolean reward, String rewardRange, int rewardItemId,
				int rewardItemCount, int rewardEinhasad, int rewardNcoin, int rewardGfx,
				String msgRange, String[] spawnMsg, String[] dieMsg, boolean dieMsgPcList, boolean autoLoot,
				int transformChance, int transformId, int transformGfxId, ScriptInfo scriptInfo) {
			this._npcId				= npcId;
			this._recall			= recall;
			this._spawnActionId		= spawnActionId;
			this._reward			= reward;
			this._rewardRange		= rewardRange;
			this._rewardItemId		= rewardItemId;
			this._rewardItemCount	= rewardItemCount;
			this._rewardEinhasad	= rewardEinhasad;
			this._rewardNcoin		= rewardNcoin;
			this._rewardGfx			= rewardGfx;
			this._msgRange			= msgRange;
			this._spawnMsg			= spawnMsg;
			this._dieMsg			= dieMsg;
			this._dieMsgPcList		= dieMsgPcList;
			this._autoLoot			= autoLoot;
			this._transformChance	= transformChance;
			this._transformId		= transformId;
			this._transformGfxId	= transformGfxId;
			this._scriptInfo		= scriptInfo;
		}
	}
	
	public class ScriptInfo {
		public ScriptType _type;
		public Object _map;
		public Object _effect;
		public Object _stay;
	}
}

