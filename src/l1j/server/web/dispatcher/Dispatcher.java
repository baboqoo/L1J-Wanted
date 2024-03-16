package l1j.server.web.dispatcher;

import io.netty.handler.codec.http.FullHttpMessage;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.utils.FileUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.WebClient;
import l1j.server.web.WebServer;
import l1j.server.web.dispatcher.response.AlertRedirectResponse;
import l1j.server.web.dispatcher.response.FileResponse;
import l1j.server.web.dispatcher.response.LoginIngameResponse;
import l1j.server.web.dispatcher.response.NotFoundResponse;
import l1j.server.web.dispatcher.response.ProtectResponse;
import l1j.server.web.dispatcher.response.SendRedirectResponse;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

/**
 * URI에 대한 응답 class
 * @author LinOffice
 */
public class Dispatcher {
	private static final ConcurrentHashMap<String, FileResponse> FILES	= new ConcurrentHashMap<>();
	private static final String REDIRECT_LOGIN_MAPPED					= "/login?urlType=%s";
	private static final String REDIRECT_LOGIN_INGAME_MAPPED			= "/login_ingame?urlType=%s";
	/*private static final String INGAME_PAGE_MESSAGE						= "인게임 전용 페이지입니다.";
	private static final String NOT_AUTH_PAGE_MESSAGE					= "페이지를 볼 수 있는 권한이 없습니다.";
	private static final String ACCOUNT_BAN_TITLE						= "해당 계정은 이용하실 수 없습니다.";
	private static final String ACCOUNT_BAN_CONTENT						= "고객님의 계정은 BAN 처리되어 이용하실 수 없습니다.";*/
	private static final String INGAME_PAGE_MESSAGE = "This is an in-game exclusive page.";
	private static final String NOT_AUTH_PAGE_MESSAGE = "You do not have permission to view this page.";
	private static final String ACCOUNT_BAN_TITLE = "This account is not available for use.";
	private static final String ACCOUNT_BAN_CONTENT = "Your account has been banned and is not available for use.";	
	private static DispatcherModel INGAME_LOGIN_DISPATCH;
	
	/**
	 * response 읍답
	 * @param request
	 * @param msg
	 * @return HttpModel
	 * @throws Exception
	 */
	public static HttpModel dispatch(HttpRequestModel request, FullHttpMessage msg) throws Exception {
		DispatcherModel dispatch	= DispatcherLoader.getDispatcher(request.get_request_uri());
		if (dispatch == null){
			// TODO 파일 응답(CSS, JS, IMG...)
			return getFileResponse(request);
		}
		// TODO 런쳐 유효성 검사(Connector)
		if (dispatch.isNeedLauncher() && !request.isLauncher()){
			return notLauncherResponse(request);
		}
		HttpModel http			= dispatch.getResponse();
		if (http instanceof HttpJsonModel){
			// TODO 데이터 응답(ajax)
			HttpJsonModel Json	= (HttpJsonModel) http;
			return dispatch.isFileUpload() ? Json.copyInstance(request, msg, dispatch) : Json.copyInstance(request, dispatch);
		}
		// TODO 페이지 응답(HTML)
		HttpResponseModel response	= ((HttpResponseModel) http).copyInstance(request, dispatch);
		if (response.getAccount() != null && response.getAccount().isBan()) {
			return accountBanResponse(request, response);
		}
		// TODO 인게임 디바이스 계정 검증
		if (request.isIngame() && (response.getAccount() == null || response.getPlayer() == null)) {
			return getIngameLoginResponse(request, dispatch);
		}
		return dispatch.isNeedLogin() ? getAuthResponse(request, dispatch, response) : response;
	}
	
	/**
	 * 파일 response
	 * @param request
	 * @return FileResponse
	 */
	private static FileResponse getFileResponse(HttpRequestModel request){
		FileResponse file = FILES.get(request.get_request_uri());
		if (file == null) {
			if (!isFileValidation(request.get_request_uri())){
				return null;
			}
			file = new FileResponse(request);
			FILES.put(request.get_request_uri(), file);
		}
		return file;
	}
	
	/**
	 * 승인 페이지 response
	 * @param request
	 * @param dispath
	 * @param response
	 * @return HttpModel
	 */
	private static HttpModel getAuthResponse(HttpRequestModel request, DispatcherModel dispatch, HttpResponseModel response){
		// TODO 로그인 페이지
		if (response.getPlayer() == null) {
			return new SendRedirectResponse(request, String.format(request.isIngame() ? REDIRECT_LOGIN_INGAME_MAPPED : REDIRECT_LOGIN_MAPPED, dispatch.getUri()));// 로그인 페이지 이동
		}
		// TODO 인게임 페이지
		if (dispatch.isNeedIngame() && !request.isIngame()) {
			//WebClient.print(request.get_ctx(), String.format("웹에서 인게임 페이지 호출 시도 ACCOUNT[%s] URL[%s]", response.getAccount().getName(), dispatch.getUri()));
			WebClient.print(request.get_ctx(), String.format("Attempting to call an in-game page from the web ACCOUNT[%s] URL[%s]", response.getAccount().getName(), dispatch.getUri()));
			return new AlertRedirectResponse(request, INGAME_PAGE_MESSAGE, StringUtil.SlushString);// 메세지 후 메인페이지 이동
		}
		// TODO 권한 페이지
		if (dispatch.isNeedGm() && !response.getPlayer().isGm()) {
			//WebClient.print(request.get_ctx(), String.format("관리자 페이지 호출 시도 ACCOUNT[%s] URL[%s]", response.getAccount().getName(), dispatch.getUri()));
			WebClient.print(request.get_ctx(), String.format("Attempting to call the administrator page ACCOUNT[%s] URL[%s]", response.getAccount().getName(), dispatch.getUri()));
			return new AlertRedirectResponse(request, NOT_AUTH_PAGE_MESSAGE, StringUtil.SlushString);// 메세지 후 메인페이지 이동
		}
		return response;
	}
	
	/**
	 * 인게임 디바이스에서 계정이 없을 경우 로그인페이지 호출
	 * @param request
	 * @param dispatch
	 * @return HttpModel
	 */
	private static HttpModel getIngameLoginResponse(HttpRequestModel request, DispatcherModel dispatch) {
		if (INGAME_LOGIN_DISPATCH == null) {
			INGAME_LOGIN_DISPATCH = DispatcherLoader.getDispatcher("/login_ingame");
		}
		return new LoginIngameResponse(request, INGAME_LOGIN_DISPATCH, dispatch.getUri());// 로그인 페이지 이동
	}
	
	/**
	 * 허용되지 않은 디바이스에서 런처페이지 호출 처리
	 * @param request
	 * @return HttpModel
	 */
	private static HttpModel notLauncherResponse(HttpRequestModel request){
		//WebClient.print(request.get_ctx(), String.format("허용하지 않는 디바이스에서 런처페이지 호출 시도 IP[%s] URL[%s]", request.get_remote_address_string(), request.get_request_uri()));
		WebClient.print(request.get_ctx(), String.format("Attempting to call the launcher page from an unauthorized device IP[%s] URL[%s]", request.get_remote_address_string(), request.get_request_uri()));
		WebServer.addBlockAddr(request.get_remote_address_string());
		return new NotFoundResponse(request);
	}
	
	/**
	 * 방화벽 페이지 response
	 * @param request
	 * @param response
	 * @return HttpModel
	 */
	private static HttpModel accountBanResponse(HttpRequestModel request, HttpResponseModel response){
		//WebClient.print(request.get_ctx(), String.format("밴처리된 계정 호출 시도 ACCOUNT[%s] URL[%s]", response.getAccount().getName(), request.get_request_uri()));
		WebClient.print(request.get_ctx(), String.format("Attempting to call a banned account ACCOUNT[%s] URL[%s]", response.getAccount().getName(), request.get_request_uri()));
		return new ProtectResponse(request, DispatcherLoader.getDispatcher("/protect"), ACCOUNT_BAN_TITLE, ACCOUNT_BAN_CONTENT);
	}
	
	/**
	 * 요청된 파일 유효성 검사
	 * 승인된 파일 확장자(app_auth_extension)
	 * @param uri
	 * @return boolean
	 */
	private static boolean isFileValidation(String uri){
		// 확장자만 가져온다.
		String extension = FileUtil.getExtension(uri);
		if (StringUtil.isNullOrEmpty(extension)) {
			return false;
		}
		// 승인된 파일 확장자 체크
		if (!DispatcherLoader.isAuthExtension(extension.toLowerCase())) {
			return false;
		}
		return true;
	}
}

