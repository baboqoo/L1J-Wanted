package l1j.server.server.command.executor;

import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 커멘드 실행 처리 인터페이스
 * 
 * 커멘드 처리 클래스는, 이 인터페이스 메소드 이외에<br>
 * public static L1CommandExecutor getInstance()<br>
 * (을)를 실장해야 한다.
 * 통상, 자클래스를 인스턴스화해 돌려주지만, 필요에 따라서 캐쉬된 인스턴스를 돌려주거나 다른 클래스를 인스턴스화해 돌려줄 수가 있다.
 */
public interface L1CommandExecutor {
	/**
	 * 이 커멘드를 실행한다.
	 * 
	 * @param pc
	 *            실행자
	 * @param cmdName
	 *            실행된 커멘드명
	 * @param arg
	 *            인수
	 */
	public boolean execute(L1PcInstance pc, String cmdName, String arg);
}

