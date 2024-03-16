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
import l1j.server.web.http.connector.HttpAccountCreateResult;
import l1j.server.web.http.connector.HttpAccountCreateValidation;
import l1j.server.web.http.connector.HttpAccountManager;

import com.google.gson.Gson;

public class AccountCreateDefine extends HttpJsonModel {
	private String _account;
	private String _password;
	private String _phone;
	private String _hddId;
	private String _boardId;
	private String _macAddress;
	private String _nicId;
	private String _macInfo;
	
	public AccountCreateDefine() {}
	private AccountCreateDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
		parseParameters();
	}
	
	private void parseParameters(){
		_account		= request.read_parameters_at_once("account");
		_password		= request.read_parameters_at_once("password");
		_phone			= request.read_parameters_at_once("phone");
		_hddId			= request.read_parameters_at_once("hdd_id");
		_boardId		= request.read_parameters_at_once("board_id");
		_macAddress		= request.read_parameters_at_once("mac_address");
		_nicId			= request.read_parameters_at_once("nic_id");
		_macInfo		= request.read_parameters_at_once("mac_info");
	}
	
	private HttpResponse result(HttpAccountCreateValidation validation){
		HttpAccountCreateResult result = new HttpAccountCreateResult();
		result.result_code = validation.getMessage();
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(result));
	}
	
	private boolean validateParameters(){
		return !StringUtil.isNullOrEmpty(_account) &&
				!StringUtil.isNullOrEmpty(_password) &&
				!StringUtil.isNullOrEmpty(_phone) &&
				!StringUtil.isNullOrEmpty(_hddId) &&
				!StringUtil.isNullOrEmpty(_boardId) &&
				!StringUtil.isNullOrEmpty(_macAddress) &&
				!StringUtil.isNullOrEmpty(_nicId) &&
				!StringUtil.isNullOrEmpty(_macInfo) &&
				_account.length() <= 40 &&
				_password.length() <= 40;
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (!Config.SERVER.AUTO_CREATE_ACCOUNTS) {
			LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.AUTO_CREATE_ACCOUNT_FALSE.getMessage());
			//return notFound("페이지를 찾을 수 없습니다.");
			return notFound("Page not found.");
		}
		
		String ip = request.get_remote_address_string();
		
		if (StringUtil.isNullOrEmpty(ip)) {
			//System.out.println(String.format("%s 아이피가 없는 요청이 시도를 했습니다. 계정[%s]", request.get_remote_address(), _account));
			System.out.println(String.format("%s IP attempted a request without authorization. Account[%s]", request.get_remote_address(), _account));
			LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_NOT_FOUND_IP.getMessage());
			return result(HttpAccountCreateValidation.FAIL_NOT_FOUND_IP);
		}
		
		if (StringUtil.isNullOrEmpty(_macInfo)) {
			//System.out.println(String.format("%s:%d 사용자가 웹에서 계정생성 시도를 했습니다.", ip, request.get_remote_address_port()));
			System.out.println(String.format("%s:%d User attempted account creation on the web.", ip, request.get_remote_address_port()));
			LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.MAC_INFO_EMPTY.getMessage());
			//return notFound("페이지를 찾을 수 없습니다.");
			return notFound("Page not found.");
		}
		
		if (!validateParameters()) {
			LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_NOT_FOUND_PARAMETERS.getMessage());
			return result(HttpAccountCreateValidation.FAIL_NOT_FOUND_PARAMETERS);
		}
		
		String hmac = HttpAccountManager.checkHmac(_hddId, _macAddress, dispatcher.getUri());

		/*String logOut = 
		"HDID: " + _hddId +
		"\nMacAddress: " + _macAddress +
		"\nURI: " + dispatcher.getUri() +
		"\nhMac: " + hmac;
		System.out.println("******************* MAC CREATE ACCOUNT ******************");
		System.out.println(logOut);
		System.out.println("******************* MAC CREATE ACCOUNT ******************");*/

		if (!hmac.equalsIgnoreCase(_macInfo)) {
			//System.out.println(String.format("사용자 계정생성 실패 invalid mac value\r\nrequest : %s,\r\nmake mac : %s", _macInfo, hmac));
			System.out.println(String.format("User account creation failed due to invalid MAC value\r\nRequest: %s,\r\nGenerated MAC: %s", _macInfo, hmac));
			LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_HMAC.getMessage());
			return result(HttpAccountCreateValidation.FAIL_HMAC);
		}
		
		if (HarddriveTable.isBan(_hddId)) {
			//System.out.println(String.format("하드밴 클라이언트 계정생성 차단 : %s, %s", _account, _hddId));
			System.out.println(String.format("Hard ban - Client account creation blocked: %s, %s", _account, _hddId));
			LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_HDD_BAN.getMessage());
			return result(HttpAccountCreateValidation.FAIL_HDD_BAN);
		}
		
		if (MatherBoardTable.isBan(_boardId)) {
			//System.out.println(String.format("메인보드밴 클라이언트 계정생성 차단 : %s, %s", _account, _boardId));
			System.out.println(String.format("Mainboard ban - Client account creation blocked: %s, %s", _account, _boardId));
			LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_BOARD_BAN.getMessage());
			return result(HttpAccountCreateValidation.FAIL_BOARD_BAN);
		}
		
		if (IpTable.isBannedIp(ip)) {
			//System.out.println(String.format("IP밴 클라이언트 계정생성 차단 : %s, %s, %s", _account, _password, IpTable.getBanIp(ip).getReason().getReason()));
			System.out.println(String.format("IP ban - Client account creation blocked: %s, %s, %s", _account, _password, IpTable.getBanIp(ip).getReason().getReason()));
			LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_IP_BAN.getMessage());
			return result(HttpAccountCreateValidation.FAIL_IP_BAN);
		}
		
		if (Config.SERVER.IP_PROTECT && !AuthIP.isWhiteIp(ip)) {
			//System.out.println(String.format("VPN또는 해외IP 클라이언트 계정생성 차단 : %s, %s", ip, _account));
			System.out.println(String.format("VPN or overseas IP - Client account creation blocked: %s, %s", ip, _account));
			LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_AUTH_IP.getMessage());
			return result(HttpAccountCreateValidation.FAIL_AUTH_IP);
		}
		
		try {
			Account account = Account.load(_account);
			if (account != null) {
				LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_DUPLICATE_ACCOUNT.getMessage());
				return result(HttpAccountCreateValidation.FAIL_DUPLICATE_ACCOUNT);
			}
			if (Account.checkCountFromIP(ip)) {
				LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_2CHECK_IP.getMessage());
				return result(HttpAccountCreateValidation.FAIL_2CHECK_IP);
			}
			if (Account.checkCountFromHDD(_hddId)) {
				LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_2CHECK_HDD.getMessage());
				return result(HttpAccountCreateValidation.FAIL_2CHECK_HDD);
			}
			if (!HttpAccountManager.isValidAccount(_account)) {
				LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_ACCOUNT_NAME.getMessage());
				return result(HttpAccountCreateValidation.FAIL_ACCOUNT_NAME);
			}
			if (!HttpAccountManager.isValidPassword(_password)) {
				LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_PASSWORD_NAME.getMessage());
				return result(HttpAccountCreateValidation.FAIL_PASSWORD_NAME);
			}
			Account.create(_account, _password, ip, ip, _phone, _hddId, _boardId);
			account = Account.load(_account);
			if (account == null) {
				LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_ERROR.getMessage());
				return result(HttpAccountCreateValidation.FAIL_ERROR);
			}
			LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.SUCCESS.getMessage());
			return result(HttpAccountCreateValidation.SUCCESS);
		} catch (Exception e) {
			LoggerInstance.getInstance().addConnector(ConnectType.CREATE, _account, _password, HttpAccountCreateValidation.FAIL_ERROR.getMessage());
			return result(HttpAccountCreateValidation.FAIL_ERROR);
		}
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new AccountCreateDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
	
}

