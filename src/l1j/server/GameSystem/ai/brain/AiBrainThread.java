package l1j.server.GameSystem.ai.brain;

import l1j.server.GameSystem.ai.constuct.AiBrainStatus;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1AiUserInstance;

/**
 * AI 액션 처리 쓰레드
 * @author LinOffice
 */
public class AiBrainThread implements Runnable {
	private final L1AiUserInstance _ai;
	private final AiBrainFactory _factory;
	private final AiBrainSundryThread _subThread;
	
	@Override
	public void run() {
		if (_ai == null || _ai.isDead() || _ai.getAiBrainStatus() == null) {// 종료 시점
			return;
		}
		try {
			if (_ai.getTeleport().isTeleport() || _ai.isStop() || _ai.isDesperado() || _ai.isOsiris()) {
				GeneralThreadPool.getInstance().schedule(this, 300L);
				return;
			}
			_factory.getHandler(_ai.getAiBrainStatus()).action();// 액션 실행
			if (_ai.getAiSleepTime() <= 0) {
				_ai.setAiSleepTime(300);// default sleep
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		GeneralThreadPool.getInstance().schedule(this, _ai.getAiSleepTime());
	}
	
	/**
	 * 생성자
	 * @param ai
	 */
	public AiBrainThread(L1AiUserInstance ai) {
		_ai			= ai;
		_factory	= new AiBrainFactory(_ai);// 상황별 액션 생성
		_subThread	= new AiBrainSundryThread(_ai);
	}
	
	/**
	 * 쓰레드 시작
	 */
	public void start(){
		_ai.setAiBrainStatus(AiBrainStatus.NONE);
		GeneralThreadPool.getInstance().execute(this);
		_subThread.start();// 아이템 사용, 버프 시전 쓰레드
	}
}

