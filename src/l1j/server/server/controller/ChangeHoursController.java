package l1j.server.server.controller;

import java.util.Calendar;

import l1j.server.Config;
import l1j.server.GameSystem.eventpush.EventPushManager;
import l1j.server.web.dispatcher.response.keyword.KeywordLoader;

public class ChangeHoursController implements l1j.server.server.model.gametime.TimeListener {// 1시간마다 호출되는 액션
	private l1j.server.server.controller.action.ControllerInterface resetDay		= l1j.server.server.controller.action.ResetDay.getInstance();
	private l1j.server.server.controller.action.ControllerInterface resetWeek		= l1j.server.server.controller.action.ResetWeek.getInstance();
	private l1j.server.server.controller.action.ControllerInterface ranking			= l1j.server.server.controller.action.UserRanking.getInstance();
	private l1j.server.server.controller.action.ControllerInterface ruunReset		= l1j.server.server.controller.action.RuunReset.getInstance();
	private l1j.server.server.controller.action.ControllerInterface notification	= l1j.server.server.controller.action.NotificationRefresh.getInstance();
	
	private static class newInstance {
		public static final ChangeHoursController INSTANCE = new ChangeHoursController();
	}
	public static ChangeHoursController getInstance() {
		return newInstance.INSTANCE;
	}
	
	public void start() {
		l1j.server.server.model.gametime.RealTimeClock.getInstance().addListener(newInstance.INSTANCE, Calendar.HOUR_OF_DAY);
	}

	@Override
	public void onMonthChanged(l1j.server.server.model.gametime.BaseTime time) {}

	@Override
	public void onDayChanged(l1j.server.server.model.gametime.BaseTime time) {}

	@Override
	public void onHourChanged(l1j.server.server.model.gametime.BaseTime time) {
		try{
			int hours = time.get(Calendar.HOUR_OF_DAY);
			if (hours == Config.ALT.DAILY_RESET_HOUR) {
				resetDay.execute();
			}
			if (hours == 10 && Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 4) {
				resetWeek.execute();// 수요일 정기정검
			}
			if (hours % Config.RANKING.RANKING_UPDATE_HOUR == 0) {
				ranking.execute();
			}
			if (hours % 2 == 0) {
				ruunReset.execute();// 2시간 마다
			}
			if (Config.PUSH.HOT_TIME_ENABLE && Config.PUSH.HOT_TIME_HOUR.contains(hours)) {
				EventPushManager.getInstance().hot_time_push();
			}
			if (hours % 12 == 0) {// 12시간 마다 알림 재출력
				notification.execute();
			}
			KeywordLoader.reload();// 앱센터 키워드 리로드
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onMinuteChanged(l1j.server.server.model.gametime.BaseTime time) {}
	
	@Override
	public void onSecondChanged(l1j.server.server.model.gametime.BaseTime time) {}

}

