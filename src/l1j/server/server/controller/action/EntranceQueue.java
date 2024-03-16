package l1j.server.server.controller.action;

import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.clientpackets.C_CharacterSelect;
import l1j.server.server.controller.LoginController;
import l1j.server.server.serverpackets.S_EntranceInfo;
import l1j.server.server.serverpackets.message.S_CommonNews;

/**
 * 접속 대기열 관리 클래스
 * @author LinOffice
 * 10초마다 호출 thread
 */
public class EntranceQueue implements ControllerInterface {
	private static class newInstance {
		public static final EntranceQueue INSTANCE = new EntranceQueue();
	}
	public static EntranceQueue getInstance() {
		return newInstance.INSTANCE;
	}
	private EntranceQueue(){}
	
	private static final Deque<GameClient> STANBY_QUEUE = new ConcurrentLinkedDeque<GameClient>();// 접속 대기열 큐

	LoginController login = LoginController.getInstance();
	
	@Override
	public void execute() {
		if (STANBY_QUEUE.isEmpty() || login.getMaxAllowedOnlinePlayers() <= login.getOnlinePlayerCount()) {
			return;
		}
		poll();
		sendStanbyPacket();
	}

	@Override
	public void execute(l1j.server.server.model.Instance.L1PcInstance pc) {
	}
	
	/**
	 * 대기열 체크 메소드
	 * @param client
	 * @param account
	 * @return 접속가능 true or 대기열 추가 false
	 */
	public boolean isStanby(final GameClient client, final Account account){
		if (account.isGameMaster()) {
			return true;// 운영자는 대기열에 들어가지 않는다.
		}
		if (!STANBY_QUEUE.isEmpty() && !STANBY_QUEUE.contains(client)) {// 대기자 존재하고 대기열에 안들어가 잇을시
			Runnable callBack = () -> {
				callBack(client, account, login);
			};
			regist(client, callBack);
			return false;
		}
		if ((login.getMaxAllowedOnlinePlayers() <= login.getOnlinePlayerCount())) {// 인원제한 상태
			if (!STANBY_QUEUE.contains(client)) {// 대기열에 없는경우
				Runnable callBack = () -> {
					callBack(client, account, login);
				};
				regist(client, callBack);
			}
			return false;
		}
		return true;
	}
	
	/**
	 * 대기열 번호 업데이트
	 */
	void queueCountUpdate(){
		GameClient client = null;
		int cnt = 0;
		Iterator<GameClient> itr = STANBY_QUEUE.iterator();
		while(itr.hasNext()){
			client = itr.next();
			if (client == null || client.accessStanbyCallBack == null) {
				STANBY_QUEUE.remove(client);
				continue;
			}
			client.accessStanbyCount = ++cnt;
		}
	}
	
	/**
	 * 대기열 순번 패킷 출력
	 */
	void sendStanbyPacket(){
		S_EntranceInfo stanby = null;
		for (GameClient client : STANBY_QUEUE) {
			if (client == null || client.accessStanbyCallBack == null) {
				continue;
			}
			stanby = new S_EntranceInfo(client.accessStanbyCount);// 대기자 카운트 출력
			client.sendPacket(stanby);
			stanby.clear();
			stanby = null;
		}
	}
	
	/**
	 * 대기열에서 첫번째 client를 꺼내 접속처리
	 */
	void poll(){
		GameClient client = STANBY_QUEUE.poll();// 첫번째 큐 꺼낸다
		if (client != null && client.accessStanbyCallBack != null) {
			client.accessStanbyCallBack.run();// 접속
			client.accessStanbyRelease();
		}
		queueCountUpdate();
	}
	
	/**
	 * 대기열 등록
	 * @param client
	 * @param callBack
	 */
	void regist(GameClient client, Runnable callBack){
		STANBY_QUEUE.offer(client);
		client.accessStanbyCount	= STANBY_QUEUE.size();
		client.accessStanbyCallBack	= callBack;
		S_EntranceInfo stanby = new S_EntranceInfo(client.accessStanbyCount);// 대기자 카운트 출력
		client.sendPacket(stanby);
		stanby.clear();
		stanby = null;
		//System.out.println(String.format("======== 접속 대기열 -> add IP : %s, restCount : %d ========", client.getIp(), STANBY_QUEUE.size()));
		System.out.println(String.format("======== Connection queue -> add IP: %s, restCount: %d ========", client.getIp(), STANBY_QUEUE.size()));
	}
	
	/**
	 * 대기열 취소
	 * @param client
	 */
	public void cancel(GameClient client){
		if (!STANBY_QUEUE.contains(client)) {
			return;
		}
		client.accessStanbyRelease();
		STANBY_QUEUE.remove(client);
		queueCountUpdate();
	}
	
	/**
	 * 대기열 완료후 접속
	 * @param client
	 * @param account
	 * @param login
	 */
	static void callBack(GameClient client, Account account, LoginController login) {
		try {
			login.login(client, account);
			account.updateLastActive(client.getIp());// 최종 로그인일을 갱신한다
			client.setAccount(account);
			client.setEnterReady(true);
			String accountName = client.getAccountName();
			// 읽어야할 공지가 있는지 체크
			if (S_CommonNews.getNoticeCount(accountName) > 0) {
				S_CommonNews news = new S_CommonNews(accountName, client);
				client.sendPacket(news);
				news.clear();
				news = null;
			} else {
				new C_CharacterSelect(client);
				client.setLoginAvailable();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

