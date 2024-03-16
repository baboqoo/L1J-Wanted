package l1j.server.common.bin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.common.DescKLoader;
import l1j.server.common.bin.potential.CommonPotentialInfo;
import l1j.server.common.bin.potential.PotentialCommonBin;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * potential-common.bin 파일 로더
 * @author LinOffice
 */
public class PotentialCommonBinLoader {
	private static Logger _log = Logger.getLogger(PotentialCommonBinLoader.class.getName());
	private static CommonPotentialInfo DATA;
	private static PotentialCommonBin bin;
	
	public static CommonPotentialInfo getBin() {
		return DATA;
	}
	
	public static CommonPotentialInfo.BonusInfoT getBonusInfo(int bonus_id) {
		return DATA.get_bonus(bonus_id);
	}
	
	private static PotentialCommonBinLoader _instance;
	public static PotentialCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new PotentialCommonBinLoader();
		}
		return _instance;
	}

	private PotentialCommonBinLoader() {
		if (Config.COMMON.COMMON_POTENTIAL_BIN_UPDATE) {
			loadFile();
		} else {
			load();
		}
	}
	
	private void loadFile(){
		bin = PotentialCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/potential-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(PotentialCommonBin) %d", bin.getInitializeBit()));
		
		regist();
	}
	
	private static final String TRUNCATE_QUERY = "truncate table bin_potential_common";
	private static final String INSERT_QUERY = "INSERT INTO bin_potential_common SET "
			+ "id=?, grade=?, desc_id=?, desc_kr=?, material_list=?, event_config=?";
	
	private void regist(){
		try {
			DATA = bin.get_potential().get_potential();
			Connection con			= null;
			PreparedStatement pstm	= null;
			try {
				con		= L1DatabaseFactory.getInstance().getConnection();
				pstm	= con.prepareStatement(TRUNCATE_QUERY);
				pstm.execute();
				SQLUtil.close(pstm);
				if (DATA == null) {
					return;
				}
				
				con.setAutoCommit(false);
				pstm	= con.prepareStatement(INSERT_QUERY);
				String material_list	= DATA.get_material_list_toString();
				String event_config		= DATA.get_event_config_toString();
				for (CommonPotentialInfo.BonusInfoT bonus : DATA.get_bonus_list()) {
					int idx = 0;
					pstm.setInt(++idx, bonus.get_bonus_id());
					pstm.setInt(++idx, bonus.get_bonus_grade());
					pstm.setInt(++idx, bonus.get_bonus_desc());
					pstm.setString(++idx, DescKLoader.getDesc(bonus.get_bonus_desc()));
					pstm.setString(++idx, material_list);
					pstm.setString(++idx, event_config);
					pstm.addBatch();
					pstm.clearParameters();
				}
				pstm.executeBatch();
				pstm.clearBatch();
				con.commit();
			} catch(SQLException e) {
				try {
					con.rollback();
				} catch(SQLException sqle){
					_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
				}
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} catch(Exception e) {
				try {
					con.rollback();
				} catch(SQLException sqle){
					_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
				}
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				try {
					con.setAutoCommit(true);
				} catch (SQLException e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
				SQLUtil.close(pstm, con);
			}
			System.out.println("potential-common.bin [update completed]. TABLE : bin_potential_common");
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	private void load() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM bin_potential_common");
			rs = pstm.executeQuery();
			DATA = CommonPotentialInfo.newInstance();
			while (rs.next()) {
				CommonPotentialInfo.BonusInfoT bonus = CommonPotentialInfo.BonusInfoT.newInstance();
				bonus.set_bonus_id(rs.getInt("id"));
				bonus.set_bonus_grade(rs.getInt("grade"));
				bonus.set_bonus_desc(rs.getInt("desc_id"));
				DATA.add_bonus_list(bonus);
				
				if (!DATA.has_material_list()) {
					String materialList	= rs.getString("material_list");
					StringTokenizer st = new StringTokenizer(materialList, StringUtil.LineString);
					while (st.hasMoreElements()) {
						String[] array = st.nextToken().split(StringUtil.CommaString);
						if (array == null || array.length != 3) {
							continue;
						}
						array[0] = array[0].replace("GRADE: ", StringUtil.EmptyString);
						array[1] = array[1].replace("NAME_ID: ", StringUtil.EmptyString);
						array[2] = array[2].replace("AMOUNT: ", StringUtil.EmptyString);
						CommonPotentialInfo.MaterialInfoT material = CommonPotentialInfo.MaterialInfoT.newInstance();
						material.set_potential_grade(Integer.parseInt(array[0].trim()));
						material.set_nameId(Integer.parseInt(array[1].trim()));
						material.set_amount(Integer.parseInt(array[2].trim()));
						DATA.add_material_list(material);
					}
				}
				if (!DATA.has_event_config()) {
					String eventConfig	= rs.getString("event_config");
					CommonPotentialInfo.EventInfoT event = CommonPotentialInfo.EventInfoT.newInstance();
					StringTokenizer st = new StringTokenizer(eventConfig, StringUtil.LineString);
					while (st.hasMoreElements()) {
						String token = st.nextToken();
						if (token.startsWith("ITEM_LIST: ")) {
							token = token.replace("ITEM_LIST: ", StringUtil.EmptyString);
							StringTokenizer temp = new StringTokenizer(token, StringUtil.CommaString);
							while (temp.hasMoreElements()) {
								event.add_event_item_list(Integer.parseInt(temp.nextToken().trim()));
							}
						} else if (token.startsWith("MATERIAL: ")) {
							token = token.replace("MATERIAL: ", StringUtil.EmptyString);
							StringTokenizer temp = new StringTokenizer(token, StringUtil.CommaString);
							while (temp.hasMoreElements()) {
								String[] array = temp.nextToken().split(" & ");
								if (array == null || array.length != 3) {
									continue;
								}
								array[0] = array[0].replace("GRADE = ", StringUtil.EmptyString);
								array[1] = array[1].replace("NAME_ID = ", StringUtil.EmptyString);
								array[2] = array[2].replace("AMOUNT = ", StringUtil.EmptyString);
								CommonPotentialInfo.MaterialInfoT material = CommonPotentialInfo.MaterialInfoT.newInstance();
								material.set_potential_grade(Integer.parseInt(array[0].trim()));
								material.set_nameId(Integer.parseInt(array[1].trim()));
								material.set_amount(Integer.parseInt(array[2].trim()));
								event.add_event_material(material);
							}
						}
					}
					DATA.set_event_config(event);
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static void reload() {
		if (Config.COMMON.COMMON_POTENTIAL_BIN_UPDATE) {
			_instance.loadFile();
		} else {
			_instance.load();
		}
	}
}

