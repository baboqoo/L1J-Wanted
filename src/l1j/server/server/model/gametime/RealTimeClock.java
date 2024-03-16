package l1j.server.server.model.gametime;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.server.GeneralThreadPool;

public class RealTimeClock {
	private static RealTimeClock					_instance;
	private volatile RealTime						_currentTime = new RealTime();
	private RealTime								_previousTime = null;
	private HashMap<Integer, List<TimeListener>>	_listeners;
	
	private RealTimeClock() {
		_listeners = new HashMap<Integer, List<TimeListener>>(8);
		_listeners.put(Calendar.MONTH, 			new CopyOnWriteArrayList<TimeListener>());
		_listeners.put(Calendar.DAY_OF_MONTH, 	new CopyOnWriteArrayList<TimeListener>());
		_listeners.put(Calendar.HOUR_OF_DAY, 	new CopyOnWriteArrayList<TimeListener>());
		_listeners.put(Calendar.MINUTE, 		new CopyOnWriteArrayList<TimeListener>());
		_listeners.put(Calendar.SECOND, 		new CopyOnWriteArrayList<TimeListener>());
		GeneralThreadPool.getInstance().execute(new TimeUpdater());
	}

	private class TimeUpdater implements Runnable {
		@Override
		public void run() {
			try {
				_previousTime = null;
				_previousTime = _currentTime;
				_currentTime = new RealTime();
				notifyChanged();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				GeneralThreadPool.getInstance().schedule(this, 1000);
			}
		}
	}

	private boolean isFieldChanged(int field) {
		return _previousTime.get(field) != _currentTime.get(field);
	}

	private void notifyChanged() {
		if (isFieldChanged(Calendar.MONTH)) {
			for (TimeListener listener : _listeners.get(Calendar.MONTH)) {
				if (listener != null) {
					listener.onMonthChanged(_currentTime);
				}
			}
		}
		if (isFieldChanged(Calendar.DAY_OF_MONTH)) {
			for (TimeListener listener : _listeners.get(Calendar.DAY_OF_MONTH)) {
				if (listener != null) {
					listener.onDayChanged(_currentTime);
				}
			}
		}
		if (isFieldChanged(Calendar.HOUR_OF_DAY)) {
			for (TimeListener listener : _listeners.get(Calendar.HOUR_OF_DAY)) {
				if (listener != null) {
					listener.onHourChanged(_currentTime);
				}
			}
		}
		if (isFieldChanged(Calendar.MINUTE)) {
			for (TimeListener listener : _listeners.get(Calendar.MINUTE)) {
				if (listener != null) {
					listener.onMinuteChanged(_currentTime);
				}
			}
		}
		if (isFieldChanged(Calendar.SECOND)) {
			for (TimeListener listener : _listeners.get(Calendar.SECOND)) {
				if (listener != null) {
					listener.onSecondChanged(_currentTime);
				}
			}
		}
	}

	public static void init() {
		_instance = new RealTimeClock();
	}

	public static RealTimeClock getInstance() {
		return _instance;
	}

	public RealTime getRealTime() {
		return _currentTime;
	}

	public void addListener(TimeListener listener, int type) {
		List<TimeListener> list = _listeners.get(type);
		if (list == null)
			throw new IllegalArgumentException(String.format("invalid listener type...%d", type));
		list.add(listener);
	}

	public void removeListener(TimeListener listener, int type) {
		List<TimeListener> list = _listeners.get(type);
		if (list == null)
			throw new IllegalArgumentException(String.format("invalid listener type...%d", type));
		list.remove(listener);
	}

	public Calendar getRealTimeCalendar() {
		return Calendar.getInstance(TimeZone.getTimeZone("GMT+9"));// 한국 시간
	}
}

