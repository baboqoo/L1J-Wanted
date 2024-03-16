package l1j.server.GameSystem.craft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.craft.bean.L1CraftInfo;
import l1j.server.common.bin.CraftCommonBinLoader;
import l1j.server.common.bin.craft.Craft;
import l1j.server.server.serverpackets.craft.S_CraftIdList;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 제작 관리 테이블 로더
 * @author LinOffice
 */
public class CraftInfoLoader { 
	private static CraftInfoLoader instance;
	public static CraftInfoLoader getInstance() {
		if (instance == null) {
			instance = new CraftInfoLoader();
		}
		return instance;
	}
	
	private static final HashMap<Integer, L1CraftInfo> INFO_DATA	= new HashMap<>();
	private static final HashMap<Integer, int[]> NPC_DATA			= new HashMap<>();
	private static final FastTable<Integer> BLOCK_DATA				= new FastTable<>();
	
	/**
	 * 엔피씨에 할당된 제작리스트 반환
	 * @param npcId
	 * @return int[]
	 */
	public static int[] getCraftIds(int npcId) {
		return NPC_DATA.get(npcId);
	}
	
	/**
	 * 제작 정보 반환
	 * @param craftId
	 * @return L1CraftInfo
	 */
	public static L1CraftInfo getInfo(int craftId) {
		return INFO_DATA.get(craftId);
	}
	
	/**
	 * 차단된 제작 체크
	 * @param craftId
	 * @return boolean
	 */
	public static boolean isBlock(int craftId){
		return BLOCK_DATA.contains(craftId);
	}
	
	/**
	 * 생성자
	 */
	private CraftInfoLoader() {
		loadCraftInfos();
	}
	
	public static void reload() {
		for (L1CraftInfo craft : INFO_DATA.values()) {
			craft.dispose();
		}
		INFO_DATA.clear();
		NPC_DATA.clear();
		BLOCK_DATA.clear();
		
		instance = null;
		instance = new CraftInfoLoader();
		S_CraftIdList.reload();
	}
	
	private void loadCraftInfos() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			
			// 제작 DB 정보 로드
			pstm	= con.prepareStatement("SELECT * FROM craft_info"); 
			rs		= pstm.executeQuery();
			while(rs.next()){
				int craftId				= rs.getInt("craft_id");
				Craft bin				= CraftCommonBinLoader.getCraft(craftId);
				if (bin == null) {
					System.out.println(String.format(
							"[CraftInfoLoader] NOT_USED_CRAFT : CRAFT_ID(%d), NAME(%s)",
							craftId, rs.getString("name")
							));
					continue;
				}
				L1CraftInfo craftInfo 	= new L1CraftInfo(
						craftId,
						rs.getString("name"),
						rs.getInt("output_name_id"),
						rs.getInt("probability_million"),
						craftPreserveNameIds(rs.getString("preserve_name_ids")),
						craftPreserveCount(rs.getString("success_preserve_count")),
						craftPreserveCount(rs.getString("failure_preserve_count")),
						Boolean.parseBoolean(rs.getString("is_success_count_type")),
						bin);
				INFO_DATA.put(craftId, craftInfo);
			}
			SQLUtil.close(rs, pstm);
			
			// 제작 엔피씨 로드
			pstm	= con.prepareStatement("SELECT * FROM craft_npcs"); 
			rs		= pstm.executeQuery();
			while(rs.next()){
				int npcId		= rs.getInt("npc_id");
				int[] craftList	= craftListToIntArray(rs.getString("craft_id_list"), npcId);
				if (craftList == null || craftList.length == 0) {
					System.out.println(String.format("[CraftInfoLoader] CRAFT_NPC_LIST_ERROR : NPC_ID(%d)", npcId));
					continue;
				}
				NPC_DATA.put(npcId, craftList);
			}
			SQLUtil.close(rs, pstm);
			
			// 제작 불가 로드
			pstm	= con.prepareStatement("SELECT craft_id FROM craft_block"); 
			rs		= pstm.executeQuery();
			while(rs.next()){
				BLOCK_DATA.add(rs.getInt("craft_id"));
			}
			SQLUtil.close(rs, pstm);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private ArrayList<Integer> craftPreserveNameIds(String str){
		try {
			if (StringUtil.isNullOrEmpty(str)) {
				return null;
			}
			str = str.replaceAll(StringUtil.LineString, StringUtil.EmptyString).replaceAll(StringUtil.EmptyOneString, StringUtil.EmptyString);
			StringTokenizer stCraftIdList = new StringTokenizer(str, StringUtil.CommaString);
			int size = stCraftIdList.countTokens();
			if (size <= 0) {
				return null;
			}
			ArrayList<Integer> preList = new ArrayList<Integer>(size);
			for (int i=0; i<size; i++) {
				int id = Integer.parseInt(stCraftIdList.nextToken(), 10);
				if (id == 0) {
					return null;
				}
				preList.add(id);
			}
			return preList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private HashMap<Integer, Integer> craftPreserveCount(String str){
		try {
			if (StringUtil.isNullOrEmpty(str)) {
				return null;
			}
			str = str.replaceAll(StringUtil.LineString, StringUtil.EmptyString).replaceAll(StringUtil.EmptyOneString, StringUtil.EmptyString);
			StringTokenizer stCraftCount = new StringTokenizer(str, StringUtil.CommaString);
			int size = stCraftCount.countTokens();
			if (size <= 0) {
				return null;
			}
			HashMap<Integer, Integer> preMap = new HashMap<Integer, Integer>(size);
			for (int i=0; i<size; i++) {
				String[] array = stCraftCount.nextToken().split(StringUtil.ColonString);
				if (array.length != 2) {
					continue;
				}
				int descId	= Integer.parseInt(array[0].trim());
				int count	= Integer.parseInt(array[1].trim());
				if (descId <= 0 || count <= 0) {
					continue;
				}
				preMap.put(descId, count);
			}
			return preMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private int[] craftListToIntArray(String craftIdList, int npc_id) {
		try {
			craftIdList = trim(craftIdList);
			StringTokenizer stCraftIdList = new StringTokenizer(craftIdList, StringUtil.CommaString);
			int size = stCraftIdList.countTokens();
			if (size <= 0) {
				return null;
			}
			
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < size; i++) {
				int craftId = Integer.parseInt(stCraftIdList.nextToken(), 10);
				if (craftId == 0) {
					return null;
				}
				Craft bin	= CraftCommonBinLoader.getCraft(craftId);
				if (bin == null) {
					continue;
				}
				list.add(craftId);
			}
			return list.stream().mapToInt(Integer::intValue).toArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String trim(String craftList) {
		craftList.trim();
		craftList = craftList.replaceAll("\\p{Z}", StringUtil.EmptyString);
		craftList = craftList.replaceAll("\\p{Space}", StringUtil.EmptyString);
		craftList = craftList.replaceAll("=", StringUtil.EmptyString);
		return craftList;
	}

}

