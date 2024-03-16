package l1j.server.server.model.gametime;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.server.GeneralThreadPool;

public class GameTimeClock {
	private static GameTimeClock _instance;
	private volatile GameTime _currentTime = new GameTime();
	private GameTime _previousTime = null;
	private List<TimeListener> _listeners = new CopyOnWriteArrayList<TimeListener>();

	private class TimeUpdater implements Runnable {
		@Override
		public void run() {
			try {
				_previousTime = null;
				_previousTime = _currentTime;
				_currentTime = new GameTime();
				notifyChanged();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				GeneralThreadPool.getInstance().schedule(this, 500);
			}
		}
	}

	private boolean isFieldChanged(int field) {
		return _previousTime.get(field) != _currentTime.get(field);
	}

	private void notifyChanged() {
		if (isFieldChanged(Calendar.MONTH)) {
			for (TimeListener listener : _listeners) {
				listener.onMonthChanged(_currentTime);
			}
		}
		if (isFieldChanged(Calendar.DAY_OF_MONTH)) {
			for (TimeListener listener : _listeners) {
				listener.onDayChanged(_currentTime);
			}
		}
		if (isFieldChanged(Calendar.HOUR_OF_DAY)) {
			for (TimeListener listener : _listeners) {
				listener.onHourChanged(_currentTime);
			}
		}
		if (isFieldChanged(Calendar.MINUTE)) {
			for (TimeListener listener : _listeners) {
				listener.onMinuteChanged(_currentTime);
			}
		}
	}

	private GameTimeClock() {
		GeneralThreadPool.getInstance().execute(new TimeUpdater());
	}

	public static void init() {
		_instance = new GameTimeClock();
	}

	public static GameTimeClock getInstance() {
		return _instance;
	}

	public GameTime getGameTime() {
		return _currentTime;
	}

	public void addListener(TimeListener listener) {
		_listeners.add(listener);
	}

	public void removeListener(TimeListener listener) {
		_listeners.remove(listener);
	}
}

