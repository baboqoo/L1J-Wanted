package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.item.potential.L1Potential;
import l1j.server.server.serverpackets.alchemy.S_AlchemyDesign;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.FileUtil;
import l1j.server.server.utils.SQLUtil;

/**
 * 인형 정보
 * @author LinOffice
 */
public class MagicDollInfoTable {
	private static Logger _log = Logger.getLogger(MagicDollInfoTable.class.getName());
	private static MagicDollInfoTable _instance;
	public static MagicDollInfoTable getInstance(){
		if (_instance == null) {
			_instance = new MagicDollInfoTable();
		}
		return _instance;
	}
	
	private MagicDollInfoTable(){
		load();
		alchemyLoad();
	}
	
	private static final Map<Integer, L1DollInfo> DOLL_INFO						= new HashMap<>();
	private static final ArrayList<S_AlchemyDesign> ALCHEMY_DESIGN_PACKETS	= new ArrayList<>();
	private static final Map<Integer, L1Potential> POTENTIALS					= new HashMap<>();
	private static final Map<Integer, ArrayList<L1Potential>> POTENTIALS_GRADE	= new HashMap<>();
	
	public static class L1DollInfo {
		private int _itemId, _dollNpcId, _blessItemId, _grade, _bonusItemId, _bonusCount, _bonusInterval, _damageChance, _attackSkillEffectId;
		private boolean _haste;
		
		public L1DollInfo(ResultSet rs) throws SQLException {
			_itemId					= rs.getInt("itemId");
			_dollNpcId				= rs.getInt("dollNpcId");
			_blessItemId			= rs.getInt("blessItemId");
			_grade					= rs.getInt("grade");
			_bonusItemId			= rs.getInt("bonusItemId");
			_bonusCount				= rs.getInt("bonusCount");
			_bonusInterval			= rs.getInt("bonusInterval");
			_damageChance			= rs.getInt("damageChance");
			_attackSkillEffectId	= rs.getInt("attackSkillEffectId");
			_haste					= Boolean.valueOf(rs.getString("haste"));
		}

		public int getItemId() {
			return _itemId;
		}
		public int getDollNpcId() {
			return _dollNpcId;
		}
		public int getBlessItemId() {
			return _blessItemId;
		}
		public int getGrade() {
			return _grade;
		}
		public int getBonusItemId() {
			return _bonusItemId;
		}
		public int getBonusCount() {
			return _bonusCount;
		}
		public int getBonusInterval() {
			return _bonusInterval;
		}
		public int getDamageChance() {
			return _damageChance;
		}
		public int getAttackSkillEffectId() {
			return _attackSkillEffectId;
		}
		public boolean isHaste() {
			return _haste;
		}
	}
	
	public static L1DollInfo getDollInfo(int itemId){
		return DOLL_INFO.containsKey(itemId) ? DOLL_INFO.get(itemId) : null;
	}
	
	public static ArrayList<S_AlchemyDesign> getAlchemyDesignList(){
    	return ALCHEMY_DESIGN_PACKETS;
    }
	
	public static L1Potential getPotential(int bonusId){
		return POTENTIALS.get(bonusId);
	}
	
	/**
	 * 단계별 랜덤한 잠재력 번호를 반환한다.
	 * @param grade
	 * @return bonusId
	 */
	public static int getPotentialGradeRandomBonusId(int grade) {
		ArrayList<L1Potential> list = POTENTIALS_GRADE.get(grade);
		if (list == null) {
			return 0;
		}
		return list.get(CommonUtil.random(list.size())).getBonusId();
	}
	
	private void load(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM magicdoll_info");
			rs		= pstm.executeQuery();
			L1DollInfo info;
			while(rs.next()){
				info = new L1DollInfo(rs);
				DOLL_INFO.put(info._itemId, info);
			}
			SQLUtil.close(rs, pstm);
			
			pstm	= con.prepareStatement("SELECT * FROM magicdoll_potential");
			rs		= pstm.executeQuery();
			L1Potential potential = null;
			while(rs.next()){
				potential = new L1Potential(rs);
				POTENTIALS.put(potential.getBonusId(), potential);
				if (!potential.isUse()) {
					continue;
				}
				ArrayList<L1Potential> list = POTENTIALS_GRADE.get(potential.getInfo().get_bonus_grade());
				if (list == null) {
					list = new ArrayList<>();
					POTENTIALS_GRADE.put(potential.getInfo().get_bonus_grade(), list);
				}
				list.add(potential);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	private void alchemyLoad(){
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				try {
		    		byte[] buff = FileUtil.readFileBytes("./data/alchemy/alchemyInfo.dat");
		        	if (buff == null || buff.length == 0) {
		        		return;
		        	}
		        	ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(buff.length);
		        	for (byte infoByte : buff) {
		        		queue.offer(binaryValue(infoByte));
		        	}
		        	ALCHEMY_DESIGN_PACKETS.addAll(S_AlchemyDesign.getAlchemyPacketList(queue));        	
		    	} catch(Exception e) {
		    		_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		    	}
			}
		}, 2000L);
    }
	
	private int binaryValue(byte infoByte){
    	String changeByte = Integer.toBinaryString(infoByte & 0xFF);
    	return Integer.parseInt(changeByte.toString(), 2);
    }
	
	public static void reload() {
		DOLL_INFO.clear();
		POTENTIALS.clear();
		POTENTIALS_GRADE.clear();
		for (S_AlchemyDesign pck : ALCHEMY_DESIGN_PACKETS) {
			pck.clear();
		}
		ALCHEMY_DESIGN_PACKETS.clear();
		_instance = new MagicDollInfoTable();
	}
}

