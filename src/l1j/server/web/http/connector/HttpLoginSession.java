package l1j.server.web.http.connector;

import java.util.Base64;

import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.controller.LoginController;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.DelayClose;
import l1j.server.server.utils.StringUtil;

/**
 * 런처 로그인 세션
 * @author LinOffice
 */
public class HttpLoginSession {
	private String clientIp;
	private int clientPort;
	private String account;
	private String password;
	private String authToken;
	private String macAddress;
	private String hddId;
	private String boardId;
	private String nicId;
	private String process;
	private Account sessionAccount;
	
	public HttpLoginSession(String clientIp, int clientPort, 
			String account, String password, 
			String authToken, String macAddress, String hddId, String boardId, String nicId, String process){
		this.clientIp	= clientIp;
		this.clientPort	= clientPort;
		this.account	= account;
		this.password	= password;
		this.authToken	= authToken;
		this.macAddress	= macAddress;
		this.hddId		= hddId;
		this.boardId	= boardId;
		this.nicId		= nicId;
		this.process	= process;
	}

	public String getClientIp(){
		return clientIp;
	}
	public int getClientPort() {
		return clientPort;
	}
	public String getAccount(){
		return account;
	}
	public String getPassword(){
		return password;
	}
	public String getAuthToken(){
		return authToken;
	}
	public String getMacAddress(){
		return macAddress;
	}
	public String getHddId(){
		return hddId;
	}
	public String getBoardId(){
		return boardId;
	}
	public String getNicId(){
		return nicId;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String value) {
		process = value;
	}
	public Account getSessionAccount(){
		return sessionAccount;
	}
	
	public HttpLoginValidation validation(boolean merge) {
		if (!checkParameters()) {
			return HttpLoginValidation.FAIL_NOT_FOUND_PARAMETERS;
		}
		
		sessionAccount = Account.load(account);
		if (sessionAccount == null) {
			return HttpLoginValidation.FAIL_NOT_FOUND_ACCOUNT;
		}
		if (!sessionAccount.validatePassword(password)) {
			return HttpLoginValidation.FAIL_INVALID_ACCOUNT;
		}
		// 개발자 모드일때 운영자 계정만 접속 가능
		if (Config.SERVER.CONNECT_DEVELOP_LOCK && !sessionAccount.isGameMaster()) {
			return HttpLoginValidation.FAIL_DEVELOPMENT;
		}
		
		GameClient client = LoginController.getInstance().getClientByAccount(account);// 접속중인 클라이언트
		if (client != null) {
			if (!merge) {
				return HttpLoginValidation.FAIL_MERGE_ACCOUNT;
			}
			mergeClient(client);
		}
		return HttpLoginValidation.SUCCESS;
	}
	
	void mergeClient(GameClient client) {
		try {
			//TODO 중복 접속 시 기존 클라이언트 종료
			client.sendPacket(S_Disconnect.DISCONNECT_MERGE);
			GeneralThreadPool.getInstance().schedule(new DelayClose(client), 500L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkParameters() {
		return !StringUtil.isNullOrEmpty(clientIp) &&
				!StringUtil.isNullOrEmpty(account) &&
				!StringUtil.isNullOrEmpty(password) &&
				!StringUtil.isNullOrEmpty(macAddress) &&
				!StringUtil.isNullOrEmpty(hddId) &&
				!StringUtil.isNullOrEmpty(boardId) &&
				!StringUtil.isNullOrEmpty(nicId) &&
				!StringUtil.isNullOrEmpty(process) &&
				account.length() <= 40 &&
				password.length() <= 40;
	}
	
	public void makeAuthToken() {
		byte[] encoded = String.format("%s.%d", this.account, System.currentTimeMillis()).getBytes(CharsetUtil.UTF_8);
		byte[] password = this.password.getBytes(CharsetUtil.UTF_8);
		CommonUtil.encode_xor(encoded, password, 0, encoded.length);
		authToken = String.format("%s=", Base64.getEncoder().encodeToString(encoded));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(128);
		sb.append("ip : ").append(clientIp).append("\n");
		sb.append("account : ").append(account).append("\n");
		sb.append("password : ").append(password).append("\n");
		sb.append("auth_token : ").append(authToken).append("\n");
		sb.append("mac_address : ").append(macAddress).append("\n");
		sb.append("hdd_id : ").append(hddId).append("\n");
		sb.append("board_id : ").append(boardId).append("\n");
		sb.append("nic_id : ").append(nicId).append("\n");
		return sb.toString();
	}
}

