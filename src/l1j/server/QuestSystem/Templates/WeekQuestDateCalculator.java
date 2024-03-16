package l1j.server.QuestSystem.Templates;

import l1j.server.server.serverpackets.message.S_SystemMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.QuestSystem.Loader.WeekQuestLoader;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/** 주간 퀘스트 시간을 담당할 클래스 **/
public class WeekQuestDateCalculator {
	private static Logger _log = Logger.getLogger(WeekQuestDateCalculator.class.getName());
	
	private static WeekUpdator _updator = null;
	private static WeekQuestDateCalculator _instance;
	public static WeekQuestDateCalculator getInstance(){
		if (_instance == null)
			_instance = new WeekQuestDateCalculator();
		return _instance;
	}
	
	private Timestamp			_updateStamp;
	private ScheduledFuture<?> 	_future;
	private void setLastTime(){
		Connection con							= null;
		PreparedStatement pstm 					= null;
		try {
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("insert into tb_weekquest_updateInfo set id=1, lastTime=? on duplicate key update lastTime=?");
			pstm.setTimestamp(1, _updateStamp);
			pstm.setTimestamp(2, _updateStamp);
			pstm.executeUpdate();
		} catch (Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("[ERROR - WeekQuestDateCalculator] setLastTime()...").append(_updateStamp).append(" write error. \r\n").append(e.getLocalizedMessage());
			_log.log(Level.SEVERE, sb.toString(), e);
			System.out.println(sb.toString());
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	private Date getLastTime(){
		Connection con						= null;
		PreparedStatement pstm 				= null;
		ResultSet rs 						= null;
		String column						= StringUtil.EmptyString;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from tb_weekquest_updateInfo");
			rs = pstm.executeQuery();
			if (rs.next()){
				Timestamp ts = rs.getTimestamp("lastTime");
				return new Date(ts.getTime());
			}
		} catch (Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append("[ERROR - WeekQuestDateCalculator] getLastTime()...").append(column).append(" read error. \r\n").append(e.getLocalizedMessage());
			_log.log(Level.SEVERE, sb.toString(), e);
			System.out.println(sb.toString());
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	public void run(){
		long sleepTime = 0;
		long nowMillis = System.currentTimeMillis();
		
		// 이전 업데이트 정보를 불러온다.
		Date oldDate = getLastTime();
		
		// 이전 업데이트 정보가 없다.
		if (oldDate == null){												
			
			// 현재로 업데이트를 한다.
			setUpdate(nowMillis);
		} else {
			// 이전 업데이트 정보로부터 다음 업데이트 정보를 불러온다.
			Calendar cal = getNextWeekCalendar(oldDate.getTime());
			
			// 현재 시간이 다음 업데이트 시간보다 더 크다면 업데이트가 되지 않았다.
			if (nowMillis >= cal.getTimeInMillis()){ 	

				// 현재 업데이트 시간으로 갱신한다.
				setUpdate(nowMillis);
		
				
			// 아직 업데이트 할 때가 아니다.
			} else {									
				// 이전 업데이트 정보를 맵핑한다.
				_updateStamp = new Timestamp(oldDate.getTime());
				
				// 다음 업데이트까지 쉴 시간을 구하고 
				sleepTime = cal.getTimeInMillis() - nowMillis;
				
				// 쉰다.
				setNextUpdate(sleepTime);
			}
		}
	}
	
	private WeekQuestDateCalculator(){
	}
	
	/** 현재 calendar로 부터 다음주 까지의 필요 일수를 반환한다. **/
	private int getDayToNextWeek(Calendar cal){
		int week 		= cal.get(Calendar.DAY_OF_WEEK);// 요일을 구한다.
		int nextWeek 	= 0;
		if (Config.QUEST.WQ_UPDATE_TYPE == 0)// 만약 일일 타입이라면, 1일씩 반환한다.
			nextWeek = 1;
		else if (week >= Config.QUEST.WQ_UPDATE_WEEK)
			nextWeek = (Config.QUEST.WQ_UPDATE_WEEK + 7) - week;
		else
			nextWeek = Config.QUEST.WQ_UPDATE_WEEK - week;
		return nextWeek;
	}
	
	/** 주어진 시간으로부터 다음주 오늘의 calendar를 반환한다. **/
	private Calendar getNextWeekCalendar(long sysmillis){		
		//Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(Config.SERVER.TIME_ZONE));
		cal.setTime(new Date(sysmillis));
		
		//Calendar nextCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		Calendar nextCal = Calendar.getInstance(TimeZone.getTimeZone(Config.SERVER.TIME_ZONE));
		nextCal.setTime(cal.getTime());
		nextCal.add(Calendar.DATE, getDayToNextWeek(cal));
		nextCal.set(Calendar.HOUR_OF_DAY, Config.QUEST.WQ_UPDATE_TIME);
		nextCal.set(Calendar.MINUTE, 59);
		nextCal.set(Calendar.SECOND, 0);
		return nextCal;
	}
	
	/** 주간 퀘스트가 갱신되었는지를 반환한다. **/
	public boolean isUpdateWeekQuest(Timestamp ts){
		if (ts == null)
			return true;
		
		// timezone 세팅에 의해 소수단위 ms 틀림으로 갱신 안되는 현상 간혹발생.. 1초 이내 오차는 모두 같은 것으로.
		long time = Math.abs(ts.getTime() - _updateStamp.getTime());
		if (time <= 1000)
			return false;
		return true;
	}
	
	public Timestamp getUpdateStamp(){
		return _updateStamp;
	}
	
	/** 현재 시간으로부터 업데이트를 실시한다. **/
	public void setUpdate(){
		setUpdate(System.currentTimeMillis());
	}
	
	/** 주어진 시간으로부터 업데이트를 실시한다. **/
	public void setUpdate(long nowMillis){
		_updateStamp = new Timestamp(nowMillis);	// 현재 시간을 지정하고
		setLastTime();								// DB에 저장한다.
		// 다음주 까지 쉰다.
		long sleepTime = getNextWeekCalendar(_updateStamp.getTime()).getTimeInMillis() - _updateStamp.getTime();
		setNextUpdate(sleepTime);
	}
	
	/** 다음 업데이트가 실행되도록 한다. **/
	private void setNextUpdate(long sleepTime){
		WeekQuestLoader.reload();
		// 어떤 이유에서건 리로드 될 가능성이 있다면, 한번만 처리하도록,
		if (_updator == null)
			_updator = new WeekUpdator();
		
		_future = GeneralThreadPool.getInstance().schedule(_updator, sleepTime);
	}
	
	public synchronized void reloadTime(){
		if (_future != null)
			_future.cancel(true);
		
		setUpdate(System.currentTimeMillis());
//AUTO SRM: 		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "주간 퀘스트가 갱신되었습니다. 케릭터를 재시작하면 새 퀘스트 목록을 받아볼 수 있습니다."), true); // CHECKED OK
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(66)), true);
	}
	
	/** 주기적 업데이트를 도와줄 헬퍼 **/
	class WeekUpdator implements Runnable{
		
		@Override
		public void run() {
			try{
				long now = System.currentTimeMillis();
				if (_updateStamp.getTime() > now){
					GeneralThreadPool.getInstance().schedule(this, _updateStamp.getTime() - now);
					return;
				}
				
				//System.out.println("[시스템 알림] 주간 퀘스트 갱신 완료");
				System.out.println("[System Notification] Weekly quest update completed");
				setUpdate(now);
//AUTO SRM: 				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "주간 퀘스트가 갱신되었습니다. 케릭터를 재시작하면 새 퀘스트 목록을 받아볼 수 있습니다."), true); // CHECKED OK
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,S_SystemMessage.getRefText(66)), true);
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}


