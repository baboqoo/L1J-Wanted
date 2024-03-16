package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

/**
 * 맵 밸런스 클래스
 * 맵 캐시화 처리시(서버가동) 등록되므로 리로드 없음
 * @author LinOffice
 */
public class MapBalanceTable {
	private static final FastMap<Short, MapBalanceData> DATA = new FastMap<Short, MapBalanceData>();
	
	public static enum BalanceType {
		BOTH, ATTACK, MAGIC;
		public static BalanceType getType(String str){
			switch(str){
			case "attack":	return ATTACK;
			case "magic":	return MAGIC;
			default:		return BOTH;
			}
		}
	}
	
	public class MapBalanceData {
		protected final short mapId;
		protected final BalanceType damageType, reductionType;
		protected final float damageValue, reductionValue;
		protected final float expValue, dropValue, adenaValue;
		private MapBalanceData(ResultSet rs) throws SQLException {
			mapId			= rs.getShort("mapId");
			damageType		= BalanceType.getType(rs.getString("damageType"));
			damageValue		= rs.getFloat("damageValue");
			reductionType	= BalanceType.getType(rs.getString("reductionType"));
			reductionValue	= rs.getFloat("reductionValue");
			expValue		= rs.getFloat("expValue");
			dropValue		= rs.getFloat("dropValue");
			adenaValue		= rs.getFloat("adenaValue");
		}
		
		public float getDamageValue(BalanceType type){
			try {
				if (damageType.equals(BalanceType.BOTH)) {
					return damageValue;
				}
				if (damageType.equals(type)) {
					return damageValue;
				}
				return 1.0F;
			} catch(Exception e) {
				return 1.0F;
			}
		}
		
		public float getReductionValue(BalanceType type){
			try {
				if (reductionType.equals(BalanceType.BOTH)) {
					return reductionValue;
				}
				if (reductionType.equals(type)) {
					return reductionValue;
				}
				return 1.0F;
			} catch(Exception e) {
				return 1.0F;
			}
		}
		
		public float getExpValue() {
			return expValue;
		}
		
		public float getDropValue() {
			return dropValue;
		}
		
		public float getAdenaValue() {
			return adenaValue;
		}
	}
	
	public static MapBalanceData getBalance(short mapId){
		return DATA.get(mapId);
	}
	
	private static MapBalanceTable _instance;
	public static MapBalanceTable getInstance(){
		if (_instance == null) {
			_instance = new MapBalanceTable();
		}
		return _instance;
	}
	
	private MapBalanceTable(){
		load();
	}
	
	private void load(){
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		MapBalanceData balance		= null;
		try {
			con			= L1DatabaseFactory.getInstance().getConnection();
			pstm		= con.prepareStatement("SELECT * FROM map_balance");
			rs			= pstm.executeQuery();
			while(rs.next()){
				balance	= new MapBalanceData(rs);
				DATA.put(balance.mapId, balance);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}

