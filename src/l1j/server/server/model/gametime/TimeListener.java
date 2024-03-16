package l1j.server.server.model.gametime;

/**
 * <p>
 * 아덴 시간의 변화를 받기 위한 Listener 인터페이스.
 * </p>
 * <p>
 * 아덴 시간의 변화를 감시해야 할 클래스는, 이 인터페이스에 포함할 수 있어 모든 메소드를 정의해 이 인터페이스를 실장하는지, 관련하는
 * 메소드만을 오버라이드(override) 해 abstract 클래스 L1GameTimeAdapter를 확장한다.
 * </p>
 * <p>
 * 그러한 클래스로부터 작성된 청취자 오브젝트는, L1GameTimeClock의 addListener 메소드를 사용해
 * L1GameTimeClock에 등록된다. 아덴 시간 변화의 통지는, 세월 시분이 각각 바뀌었을 때에 행해진다.
 * </p>
 * <p>
 * 이러한 메소드는, L1GameTimeClock의 thread상에서 동작한다. 이러한 메소드의 처리에 시간이 걸렸을 경우, 다른
 * 청취자에게로의 통지가 늦을 가능성이 있다. 완료까지 시간을 필요로 하는 처리나, thread를 블록 하는 메소드의 호출이 포함되는 처리를
 * 실시하는 경우는, 내부에서 새롭게 thread를 작성해 처리를 실시해야 하는 것이다.
 * </p>
 * 
 */
public interface TimeListener {
	/**
	 * 아덴 시간에 달이 바뀌었을 때에 불려 간다.
	 * 
	 * @param time
	 *            최신의 아덴 시간
	 */
	public void onMonthChanged(BaseTime time);

	/**
	 * 아덴 시간에 날이 바뀌었을 때에 불려 간다.
	 * 
	 * @param time
	 *            최신의 아덴 시간
	 */
	public void onDayChanged(BaseTime time);

	/**
	 * 아덴 시간에 시간이 바뀌었을 때에 불려 간다.
	 * 
	 * @param time
	 *            최신의 아덴 시간
	 */
	public void onHourChanged(BaseTime time);

	/**
	 * 아덴 시간에 분이 바뀌었을 때에 불려 간다.
	 * 
	 * @param time
	 *            최신의 아덴 시간
	 */
	public void onMinuteChanged(BaseTime time);
	
	/**
	 * 아덴 시간에 초가 바뀌었을 때에 불려 간다.
	 * 
	 * @param time
	 *            최신의 아덴 시간
	 */
	public void onSecondChanged(BaseTime time);
}

