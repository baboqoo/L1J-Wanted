package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.AuthIP;
import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.datatables.HarddriveTable;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.datatables.MatherBoardTable;
import l1j.server.server.monitor.Logger.ConnectType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.connector.HttpAccountManager;
import l1j.server.web.http.connector.HttpLoginResult;
import l1j.server.web.http.connector.HttpLoginSession;
import l1j.server.web.http.connector.HttpLoginValidation;

import com.google.gson.Gson;

public class LoginMergeDefine extends HttpJsonModel {
	private HttpLoginSession login;
	private String _macInfo;
	
	public LoginMergeDefine() {}
	private LoginMergeDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
		parseParameters();
	}
	
	private void parseParameters(){
		login = new HttpLoginSession(request.get_remote_address_string(), 
				request.get_remote_address_port(), 
				request.read_parameters_at_once("account"), 
				request.read_parameters_at_once("password"), 
				StringUtil.EmptyString, 
				request.read_parameters_at_once("mac_address"),
				request.read_parameters_at_once("hdd_id"),
				request.read_parameters_at_once("board_id"),
				request.read_parameters_at_once("nic_id"),
				request.read_parameters_at_once("process"));
		_macInfo = request.read_parameters_at_once("mac_info");
	}
	
	private HttpResponse result(HttpLoginValidation validation){
		HttpLoginResult result = new HttpLoginResult();
		result.result_code = validation.getMessage();
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(result));
	}
	
	private HttpResponse result(HttpLoginValidation validation, String authToken) throws Exception{
		HttpLoginResult result	= new HttpLoginResult();
		result.result_code	= validation.getMessage();
		result.auth_token	= authToken;
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(result));
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (StringUtil.isNullOrEmpty(_macInfo)) {
			//System.out.println(String.format("%s:%d 사용자가 웹에서 로그인 시도를 했습니다.", login.getClientIp(), login.getClientPort()));
			System.out.println(String.format("%s:%d User attempted login on the web.", login.getClientIp(), login.getClientPort()));
			LoggerInstance.getInstance().addConnector(ConnectType.LOGIN, login.getAccount(), login.getPassword(), HttpLoginValidation.MAC_INFO_EMPTY.getMessage());
			login = null;
			//return notFound("페이지를 찾을 수 없습니다.");
			return notFound("Page not found.");
		}
		
		if (StringUtil.isNullOrEmpty(login.getClientIp())) {
			//System.out.println(String.format("%s 아이피가 없는 요청이 시도를 했습니다. 계정[%s]", request.get_remote_address(), login.getAccount()));
			System.out.println(String.format("%s IP attempted a request without authorization. Account[%s]", request.get_remote_address(), login.getAccount()));
			LoggerInstance.getInstance().addConnector(ConnectType.LOGIN, login.getAccount(), login.getPassword(), HttpLoginValidation.FAIL_NOT_FOUND_IP.getMessage());
			login = null;
			return result(HttpLoginValidation.FAIL_NOT_FOUND_IP);
		}
		
		String hmac = HttpAccountManager.checkHmac(login.getHddId(), login.getMacAddress(), dispatcher.getUri());
		if (!hmac.equalsIgnoreCase(_macInfo)) {
			//System.out.println(String.format("%s:%d 사용자 로그인 실패 invalid mac value\r\nrequest : %s,\r\nmake mac : %s", 
			System.out.println(String.format("%s:%d User login failed due to invalid MAC value\r\nRequest: %s,\r\nGenerated MAC: %s",
					login.getClientIp(), login.getClientPort(), _macInfo, hmac));
			LoggerInstance.getInstance().addConnector(ConnectType.LOGIN, login.getAccount(), login.getPassword(), HttpLoginValidation.FAIL_HMAC.getMessage());
			login = null;
			return result(HttpLoginValidation.FAIL_HMAC);
		}
		
		if (HarddriveTable.isBan(login.getHddId())) {
			//System.out.println(String.format("하드밴 클라이언트 접속 차단 : %s, %s", login.getAccount(), login.getHddId()));
			System.out.println(String.format("Hard ban - Client connection blocked: %s, %s", login.getAccount(), login.getHddId()));
			LoggerInstance.getInstance().addConnector(ConnectType.LOGIN, login.getAccount(), login.getPassword(), HttpLoginValidation.FAIL_HDD_BAN.getMessage());
			login = null;
			return result(HttpLoginValidation.FAIL_HDD_BAN);
		}
		
		if (MatherBoardTable.isBan(login.getBoardId())) {
			//System.out.println(String.format("메인보드밴 클라이언트 접속 차단 : %s, %s", login.getAccount(), login.getBoardId()));
			System.out.println(String.format("Mainboard ban - Client connection blocked: %s, %s", login.getAccount(), login.getBoardId()));
			LoggerInstance.getInstance().addConnector(ConnectType.LOGIN, login.getAccount(), login.getPassword(), HttpLoginValidation.FAIL_BOARD_BAN.getMessage());
			login = null;
			return result(HttpLoginValidation.FAIL_BOARD_BAN);
		}
		
		if (IpTable.isBannedIp(login.getClientIp())) {
			//System.out.println(String.format("IP밴 클라이언트 접속 차단 : %s, %s, %s", login.getAccount(), login.getPassword(), IpTable.getBanIp(login.getClientIp()).getReason().getReason()));
			System.out.println(String.format("IP ban - Client connection blocked: %s, %s, %s", login.getAccount(), login.getPassword(), IpTable.getBanIp(login.getClientIp()).getReason().getReason()));
			LoggerInstance.getInstance().addConnector(ConnectType.LOGIN, login.getAccount(), login.getPassword(), HttpLoginValidation.FAIL_IP_BAN.getMessage());
			login = null;
			return result(HttpLoginValidation.FAIL_IP_BAN);
		}
		
		if (Config.SERVER.IP_PROTECT && !AuthIP.isWhiteIp(login.getClientIp())) {
			//System.out.println(String.format("VPN또는 해외IP 클라이언트 접속 차단 : %s, %s", login.getClientIp(), login.getAccount()));
			System.out.println(String.format("VPN or overseas IP - Client connection blocked: %s, %s", login.getClientIp(), login.getAccount()));
			LoggerInstance.getInstance().addConnector(ConnectType.LOGIN, login.getAccount(), login.getPassword(), HttpLoginValidation.FAIL_AUTH_IP.getMessage());
			login = null;
			return result(HttpLoginValidation.FAIL_AUTH_IP);
		}
		
		HttpLoginValidation validation = login.validation(true);
		LoggerInstance.getInstance().addConnector(ConnectType.LOGIN, login.getAccount(), login.getPassword(), validation.getMessage());
		if (validation.equals(HttpLoginValidation.SUCCESS)) {
			login.makeAuthToken();
			HttpAccountManager.put(login);
			Account account = login.getSessionAccount();
			// 하드 정보가 다르다면 업데이트한다.
			if (account.getHddId() == null || !account.getHddId().equals(login.getHddId())) {
				account.updateHddId(login.getHddId());
			}
			// 마더보드 정보가 다르다면 업데이트한다.
			if (account.getBoardId() == null || !account.getBoardId().equals(login.getBoardId())) {
				account.updateBoardId(login.getBoardId());
			}
			return result(validation, login.getAuthToken());
		}
		return result(validation);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new LoginMergeDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
	
}

