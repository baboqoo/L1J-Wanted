package l1j.server.RobotSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.command.executor.L1Robot4;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;


public class RobotAIThread implements Runnable {	
	
	
	static private final int Scarecrow = 1;  // 허수아비
	static private final int Hunt = 2;       // 사냥
	
	// 인공지능 쓰레드를 처리해도되는지 확인용.
	@SuppressWarnings("unused")
	static private boolean running;
	// 쓰레드가 동작중 잠시 휴식할 시간값.
	static private long sleep;
	// 관리될 로봇들객체
	static private FastTable<L1PcInstance> huntList;
	// 관리될 로봇들객체
	static private FastTable<L1PcInstance> scarecrowList;
	// 텔레포트할 좌표목록
	static private List<RobotLocation> list_location;
	// 텔레포트할 좌표목록
	static private List<RobotMent> list_ment;
	// 생성될 케리명 목록
	static private List<RobotName> list_name;
	static public int list_name_idx;
	static private List<RobotFishing> list_fish;
	/**
	 * 초기화 처리 함수.
	 */
	static public void init(){
		// 변수 초기화.
		sleep = 10;
		list_name_idx = 0;
		huntList = new FastTable<L1PcInstance>();
		scarecrowList = new FastTable<L1PcInstance>();
		list_location = new ArrayList<RobotLocation>();
		list_ment = new ArrayList<RobotMent>();
		list_name = new ArrayList<RobotName>();
		list_fish = new ArrayList<RobotFishing>();
		// 로봇 인공지능용 쓰레드 활성화.
		new Thread(new RobotAIThread()).start();
		new Thread(new RobotScarecrow()).start();
		// 디비로부터 정보 추출.
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();

			pstm = con.prepareStatement("SELECT * FROM robot_location where count = '1'");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotLocation rl = new RobotLocation();
				rl.uid = rs.getInt("uid");
				rl.istown = rs.getBoolean("istown");
				rl.x = rs.getInt("x");
				rl.y = rs.getInt("y");
				rl.map = rs.getInt("map");
				rl.etc = rs.getString("etc");
				
				list_location.add(rl);
			}
			SQLUtil.close(rs, pstm);
			
			pstm = con.prepareStatement("SELECT * FROM robot_message");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotMent rm = new RobotMent();
				rm.uid = rs.getInt("uid");
				rm.type = rs.getString("type");
				rm.ment = rs.getString("ment");
				
				list_ment.add(rm);
			}
			SQLUtil.close(rs, pstm);
			
			pstm = con.prepareStatement("SELECT * FROM robot_name");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotName rn = new RobotName();
				rn.uid = rs.getInt("uid");
				rn.name = rs.getString("name");
				list_name.add(rn);
			}
			SQLUtil.close(rs, pstm);

			pstm = con.prepareStatement("SELECT * FROM robot_fishing");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotFishing rn = new RobotFishing();
				rn.x = rs.getInt("x");
				rn.y = rs.getInt("y");
				rn.map = rs.getInt("mapid");
				rn.heading = rs.getInt("heading");
				rn.fishX = rs.getInt("fishingX");
				rn.fishY = rs.getInt("fishingY");
				list_fish.add(rn);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public static boolean doesCharNameExist(String name) {
		boolean result = true;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT name FROM robot_name WHERE name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			result = rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return result;
	}
	
	/**
	 * 종료 처리 함수.
	 */
	static public void close(){
		running = false;
	}
	
	/**
	 * 관리 목록에 추가요청 처리 함수.
	 * @param robot
	 */
	static public void append(L1PcInstance robot, int type){
		switch (type) {
		case Scarecrow:
			synchronized (scarecrowList) {
				scarecrowList.add(robot);
			}
			break;
		case Hunt:
			synchronized (huntList) {
				huntList.add(robot);
			}
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 관리목록에서 제거요청 처리 함수.
	 * @param robot
	 */
	static public void remove(L1PcInstance robot, int type){
		switch (type) {
		case Scarecrow:
			synchronized (scarecrowList) {
				scarecrowList.remove(robot);
			}
			break;
		case Hunt:
			synchronized (huntList) {
				huntList.remove(robot);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 오토 사냥 캐릭 리스트
	 */
	private static Collection<L1PcInstance> huntValues;
	static public Collection<L1PcInstance> getHunt() {
		Collection<L1PcInstance> vs = huntValues;
		return (vs != null) ? vs : (huntValues = Collections.unmodifiableCollection(huntList));
	}
	
	@Override
	public void run(){
		try {
			long time = System.currentTimeMillis();
			while (true) {
				try {					
					time = System.currentTimeMillis();
					// 로봇들 인공지능 활성화.
					for(L1PcInstance robot : huntList) {
						if (robot == null)
							continue;
						robot.getRobotAi().toAI(time);
					}
				} catch (Exception e) {
//					System.out.println("로봇 쓰레드가 비정상적으로 종료 되어 AI 재시작중 중복오류 발생!");
//					e.printStackTrace();
				} finally {
					try {
						Thread.sleep(sleep);
					} catch (Exception e) {	}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				Thread.sleep(sleep);
			} catch (Exception e) {	}
		}
	}
	
	/** 허수아비 인공지능 **/
	static class RobotScarecrow implements Runnable {
		@Override
		public void run() {
			try {
				long time = System.currentTimeMillis();
				while (true) {
					try {						
						time = System.currentTimeMillis();
						// 로봇들 인공지능 활성화.				
						for(L1PcInstance robot : scarecrowList) {
							if (robot == null)
								continue;
							robot.getRobotAi().toAI(time);
						}
					} catch (Exception e) {
						//System.out.println("로봇 쓰레드가 비정상적으로 종료 되어 AI 재시작중 중복오류 발생!");
						System.out.println("The robot thread terminated abnormally and a duplication error occurred during AI restart!");
						e.printStackTrace();
					} finally {
						try {
							Thread.sleep(sleep);
						} catch (Exception e) {	}
					}
				}
			} catch (Exception e) {	
				e.printStackTrace();
			} finally {
				try {
					Thread.sleep(sleep);
				} catch (Exception e) {	}
			}
		}
	}
	
	/**
	 * 사냥터 텔레포트 좌표리턴.
	 * @param type
	 * @return
	 */
	static public RobotLocation getLocation() {
		if(list_location.size() == 0)
			return null;
		
		return list_location.get( L1Robot4.random(0, list_location.size()-1));
	}
	
	static public List<RobotMent> getRobotMent(){
		return list_ment;
	}
	
	static public List<RobotName> getRobotName(){
		return list_name;
	}
	static public List<RobotFishing> getRobotFish(){
		return list_fish;
	}
	
	static public String getName(){
		try {
			// 이름목록 순회.
			for( ; list_name_idx < list_name.size() ; ){
				String name = list_name.get(list_name_idx++).name;
				Connection con = null;
				PreparedStatement pstm = null;
				ResultSet rs = null;
				System.out.println(name);
				try {
					con = L1DatabaseFactory.getInstance().getConnection();
					pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
					pstm.setString(1, name);
					rs = pstm.executeQuery();
					if (!rs.next()) {
						return name;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					SQLUtil.close(rs, pstm, con);
				}
			}
		} catch (Exception e) { }
		// 디비에 동이한 이름 존재 확인.
		// 마지막이 이덱스까지 도달할경우 무시.
		return null;
	}
	
	static public void reload() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		list_location.clear();
		list_ment.clear();
		list_name.clear();
		
		try {
			con = L1DatabaseFactory.getInstance().getConnection();

			pstm = con.prepareStatement("SELECT * FROM robot_location where count = '1'");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotLocation rl = new RobotLocation();
				rl.uid = rs.getInt("uid");
				rl.istown = rs.getBoolean("istown");
				rl.x = rs.getInt("x");
				rl.y = rs.getInt("y");
				rl.map = rs.getInt("map");
				rl.etc = rs.getString("etc");
				
				list_location.add(rl);
			}
			SQLUtil.close(rs, pstm);

			
			pstm = con.prepareStatement("SELECT * FROM robot_message");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotMent rm = new RobotMent();
				rm.uid = rs.getInt("uid");
				rm.type = rs.getString("type");
				rm.ment = rs.getString("ment");
				
				list_ment.add(rm);
			}
			SQLUtil.close(rs, pstm);

			
			pstm = con.prepareStatement("SELECT * FROM robot_name");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotName rn = new RobotName();
				rn.uid = rs.getInt("uid");
				rn.name = rs.getString("name");
				list_name.add(rn);
			}
			SQLUtil.close(rs, pstm);

			pstm = con.prepareStatement("SELECT * FROM robot_fishing");
			rs = pstm.executeQuery();
			while (rs.next()) {
				RobotFishing rn = new RobotFishing();
				rn.x = rs.getInt("x");
				rn.y = rs.getInt("y");
				rn.map = rs.getInt("mapid");
				rn.heading = rs.getInt("heading");
				rn.fishX = rs.getInt("fishingX");
				rn.fishY = rs.getInt("fishingY");
				list_fish.add(rn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}

