package l1j.server.server.controller;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.AccountAlreadyLoginException;
import l1j.server.server.GameClient;
import l1j.server.server.GameServerFullException;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.serverpackets.message.S_ServerMessage;

public class LoginController {
	private Map<String, GameClient> _accounts	= new ConcurrentHashMap<String, GameClient>(Config.SERVER.MAX_ONLINE_USERS);
	private Map<String, Integer> _countIp		= new ConcurrentHashMap<String, Integer>(Config.SERVER.MAX_ONLINE_USERS);
	private Map<String, Integer> _countCClass	= new ConcurrentHashMap<String, Integer>(Config.SERVER.MAX_ONLINE_USERS);
	private Map<String, ArrayList<GameClient>> _ipClients = new ConcurrentHashMap<>();

	private int _maxAllowedOnlinePlayers;

	private static LoginController _instance;
	public static LoginController getInstance() {
		if (_instance == null) {
			_instance = new LoginController();
		}
		return _instance;
	}
	
	private LoginController() {}

	public GameClient[] getAllAccounts() {
		return _accounts.values().toArray(new GameClient[_accounts.size()]);
	}

	public int getOnlinePlayerCount() {
		return _accounts.size();
	}

	public int getMaxAllowedOnlinePlayers() {
		return _maxAllowedOnlinePlayers;
	}

	public void setMaxAllowedOnlinePlayers(int maxAllowedOnlinePlayers) {
		_maxAllowedOnlinePlayers = maxAllowedOnlinePlayers;
	}

	private void kickClient(final GameClient client) {
		if (client == null) {
			return;
		}
		if (client.getActiveChar() != null) {
			client.getActiveChar().sendPackets(new S_ServerMessage(357), true);
		}
		if (!client.isConnected()) {
			_accounts.remove(client.getAccountName());
		}

		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				client.kick();
			}
		}, 1000);
	}

	static public String getCClass(String ip) {
		return ip.substring(0, ip.lastIndexOf('.'));
	}

	public void login(GameClient client, Account account) throws GameServerFullException, AccountAlreadyLoginException {
		synchronized (this) {
			if (client.isInterServer() && _accounts.containsKey(account.getName())) {
				logout(client);// 기존 등록된 어카운트 제거
			}
			if (!account.isValid()) {
				// 패스워드 인증이되어 있지 않은, 혹은 인증에 실패한 어카운트가 지정되었다.
				// 이 코드는, 버그 검출을 위해 마셔 존재한다.
				//throw new IllegalArgumentException("인증되지 않은 계정입니다");
				throw new IllegalArgumentException("The account is not authenticated");
			} else if ((getMaxAllowedOnlinePlayers() <= getOnlinePlayerCount()) && !account.isGameMaster()) {
				throw new GameServerFullException();
			} else if (_accounts.containsKey(account.getName())) {// 이미 값이 저장되어있을시
				kickClient(_accounts.remove(account.getName()));
				throw new AccountAlreadyLoginException();
			} else {
				String ip = client.getIp();
				_accounts.put(account.getName(), client);
				addCountIp(ip, client);
				String cClass = getCClass(ip);
				if (_countCClass.containsKey(cClass)) {
					_countCClass.put(cClass, _countCClass.get(cClass) + 1);
				} else {
					_countCClass.put(cClass, 1);
				}
			}
		}
	}
	
	public boolean isClientLogin(String name){
		return _accounts.containsKey(name);
	}

	public GameClient getClientByAccount(String name) {
		if (_accounts.containsKey(name)) {
			return _accounts.get(name);
		}
		return null;
	}
	
	public GameClient getClientByIp(String ip) {
		ArrayList<GameClient> list = _ipClients.get(ip);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(list.size() - 1);
	}

	public int getIpCount(String ip) {
		if (_countIp.containsKey(ip)) {
			return _countIp.get(ip);
		}
		return 0;
	}

	public int getCClassCount(String ip) {
		if (_countCClass.containsKey(getCClass(ip))) {
			return _countCClass.get(getCClass(ip));
		}
		return 0;
	}

	public boolean logout(GameClient client) {
		synchronized (this) {
			if (client == null || client.getAccountName() == null) {
				return false;
			}
			String ip = client.getIp();
			if (_countIp.containsKey(ip)) {
				removeCountIp(ip, client);
			}
			String cClass = getCClass(ip);
			if (cClass != null && _countCClass.containsKey(cClass)) {
				_countCClass.put(cClass, _countCClass.get(cClass) - 1);
				if (_countCClass.get(cClass) == 0) {
					_countCClass.remove(cClass);
				}
			}
			return _accounts.remove(client.getAccountName()) != null;
		}
	}
	
	void addCountIp(String ip, GameClient client) {
		if (_countIp.containsKey(ip)) {
			_countIp.put(ip, _countIp.get(ip) + 1);
		} else {
			_countIp.put(ip, 1);
		}
		addIpClient(ip, client);
	}
	
	void removeCountIp(String ip, GameClient client) {
		_countIp.put(ip, _countIp.get(ip) - 1);
		int countIp = _countIp.get(ip);
		if (countIp == 0) {
			_countIp.remove(ip);
		}
		removeIpClient(ip, client);
	}
	
	void addIpClient(String ip, GameClient client) {
		ArrayList<GameClient> list = _ipClients.get(ip);
		if (list == null) {
			list = new ArrayList<>();
			_ipClients.put(ip, list);
		}
		if (!list.contains(client)) {
			list.add(client);
		}
	}
	
	void removeIpClient(String ip, GameClient client) {
		ArrayList<GameClient> list = _ipClients.get(ip);
		if (list != null && list.contains(client)) {
			list.remove(client);
		}
	}
}

