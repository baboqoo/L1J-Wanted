package l1j.server.server.model.gametime;

public class GameTimeAdapter implements TimeListener {
	@Override
	public void onMonthChanged(BaseTime time) {}

	@Override
	public void onDayChanged(BaseTime time) {}

	@Override
	public void onHourChanged(BaseTime time) {}

	@Override
	public void onMinuteChanged(BaseTime time) {}
	
	@Override
	public void onSecondChanged(BaseTime time) {}
}

