package l1j.server.GameSystem.ai;

import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.GameSystem.ai.constuct.AiArea;
import l1j.server.GameSystem.ai.constuct.AiMent;
import l1j.server.GameSystem.ai.constuct.AiPledge;
import l1j.server.GameSystem.ai.constuct.AiType;
import l1j.server.server.construct.L1ServerType;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1AiUserInstance;
import l1j.server.server.utils.StringUtil;

/**
 * AI 최상위 추상 쓰레드
 * 최초 지역별 출현 및 종료 담당
 * @author LinOffice
 */
public abstract class AiHandler implements Runnable {
	private static Logger _log		= Logger.getLogger(AiHandler.class.getName());
	
	protected final AiLoader _loader;
	protected final FastTable<L1AiUserInstance> _aiList;
	protected final AiArea _aiArea;
	protected final AiPledge _pledge;
	protected final String _serverName;
	protected final L1World _world;
	protected boolean _running;
	private int _timer;// 제한시간
	
	/**
	 * 부모 생성자
	 * @param aiArea
	 * @param clanType
	 * @param second
	 */
	protected AiHandler(AiArea aiArea, AiPledge pledge, int second){
		_loader		= AiLoader.getInstance();
		_aiList		= new FastTable<L1AiUserInstance>();
		_aiArea		= aiArea;
		_pledge		= pledge;
		_serverName	= L1ServerType.getRandomServer().getName().substring(0, 2) + "@";
		_world		= L1World.getInstance();
		_timer		= second;
	}
	
	@Override
	public void run() {
		try {
			setting();
			login();
			party();
			while (_running) {
				try {
					timerDecrease();
					aiListCheck();
				} catch(Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				} finally {
					Thread.sleep(1000L);
				}
			}
		}catch(Exception e){
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 제한 시간 감소 처리
	 */
	void timerDecrease(){
		if (_timer > 0) {
			_timer--;
		} else {
			timeOutEnd();
		}
	}
	
	/**
	 * ai 생존 조사
	 */
	void aiListCheck(){
		if (!_running) {
			return;
		}
		if (_aiList == null || _aiList.isEmpty()) {
			end();
			return;
		}
		for (L1AiUserInstance ai : _aiList) {
			if (!ai.isDead()) {// 생존 여부
				return;
			}
		}
		end();// 모든 ai 사망
	}
	
	/**
	 * 접속 처리
	 */
	void login(){
		try {
			FastTable<String> loginMent = _loader.getMent(AiMent.LOGIN);
			for (L1AiUserInstance ai : _aiList) {
				if (AiType.isScene(ai.getAiType())) {
					ai.setName(_serverName + ai.getName());
				}
				ai.login(_aiArea);// 로그인
				Thread.sleep(100L);
				ai.send_ment(loginMent);
			}
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 파티 맺기
	 */
	void party(){
		L1Party party = new L1Party();
		for (L1AiUserInstance ai : _aiList) {
			party.addMember(ai);
		}
	}
	
	/**
	 * 생성할 ai 설정
	 */
	protected abstract void setting();
	
	/**
	 * 쓰레드 시작
	 */
	protected abstract void start();
	
	/**
	 * 쓰레드 종료
	 */
	protected abstract void end();
	
	/**
	 * 시간 종료에 의한 쓰레드 종료
	 */
	protected abstract void timeOutEnd();
	
	/**
	 * ai 종료 처리
	 */
	protected void dispose(){
		try {
			FastTable<String> logoutMent = _loader.getMent(AiMent.LOGOUT);
			for (L1AiUserInstance ai : _aiList) {
				ai.send_ment(logoutMent);
				Thread.sleep(500L);
				if (AiType.isScene(ai.getAiType())) {
					ai.setName(ai.getName().replace(_serverName, StringUtil.EmptyString));
				}
				ai.logout();
			}
			_aiList.clear();
			AiManager.getInstance().delete(_aiArea);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
}

