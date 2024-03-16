package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.File;

import l1j.server.Config;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.monitor.Logger.ConnectType;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.connector.HttpConnectorInfoResult;
import l1j.server.web.http.connector.HttpConnectorInfoValidation;

import com.google.gson.Gson;

public class ConnectorInfoDefine extends HttpJsonModel {
	
	private static HttpConnectorInfoResult result;
	
	private static String SUCCESS_JSON;
	private static String FAIL_REQUIRED_INFO_JSON;
	
	public ConnectorInfoDefine() {}
	private ConnectorInfoDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
		if (result == null) {
			createResult();
		}
	}
	
	/**
	 * 파일 존재 여부 검사
	 * @param path
	 * @return true or false
	 */
	private boolean connectorFileValidataion(String path){
		String filePath = String.format("./appcenter/connector%s", path);
		File file = new File(filePath);
		if (!file.exists()){
			System.out.println(String.format("[ConnectorInfoDefine] file not found : path(%s)", filePath));
			return false;
		}
		return true;
	}
	
	/**
	 * 응답 데이터 생성
	 */
	private void createResult(){
		StringBuilder sb = new StringBuilder();
		
		// 기본 파일 위치 경로
		//sb.append("http://").append(Config.SERVER.LOGIN_SERVER_ADDRESS).append(StringUtil.ColonString).append(Config.WEB.WEB_SERVER_PORT).append("/connector");
		sb.append("/connector");
		String fileContextPath = sb.toString();
		sb.setLength(0);
		
		// Lin.bin 파일 경로
		String linbinPath = StringUtil.EmptyString;
		if (connectorFileValidataion(sb.append("/linbin/").append(Config.LAUNCHER.CONNECTOR_LINBIN_PATH).toString())) {
			linbinPath = fileContextPath + sb.toString();
		}
		sb.setLength(0);
		
		// Patch.zip 경로
		String patchPath = StringUtil.EmptyString;
		//if (!StringUtil.isNullOrEmpty(Config.LAUNCHER.CONNECTOR_PATCH_PATH) && connectorFileValidataion(sb.append("/patch/").append(Config.LAUNCHER.CONNECTOR_PATCH_PATH).toString())) {
			//patchPath = sb.toString();
		//sb.append("/patch/");
		//patchPath = fileContextPath + sb.toString();

		patchPath = sb.append(Config.LAUNCHER.CONNECTOR_PATCH_PATH).toString();		
		sb.setLength(0);
		
		// Ms.dll 파일 경로
		String msdllPath = StringUtil.EmptyString;
		if (connectorFileValidataion(sb.append("/msdll/").append(Config.LAUNCHER.CONNECTOR_MSDLL_PATH).toString())) {
			msdllPath = fileContextPath + sb.toString();
		}
		sb.setLength(0);

		// Libcocos2d.zip 파일 경로
		String libcocosPath = StringUtil.EmptyString;
		if (connectorFileValidataion(sb.append("/libcocos/").append(Config.LAUNCHER.CONNECTOR_LIBCOCOS_PATH).toString())) {
			libcocosPath = fileContextPath + sb.toString();
		}
		sb.setLength(0);
		
		// Boxer.dll 파일 경로
		String boxdllPath = StringUtil.EmptyString;
		if (connectorFileValidataion(sb.append("/boxer/").append(Config.LAUNCHER.CONNECTOR_BOXDLL_PATH).toString())) {
			boxdllPath = fileContextPath + sb.toString();
		}
		sb.setLength(0);
		
		// Loader.bin 파일 경로
		String loaderPath = StringUtil.EmptyString;
		if (connectorFileValidataion(sb.append("/loader/").append(Config.LAUNCHER.CONNECTOR_LOADER_PATH).toString())) {
			loaderPath = fileContextPath + sb.toString();
		}
		sb.setLength(0);
		
		// Craft-common.bin 파일 경로
		String craftPath = StringUtil.EmptyString;
		if (!StringUtil.isNullOrEmpty(Config.LAUNCHER.CONNECTOR_CRAFT_PATH) && connectorFileValidataion(sb.append("/craft/").append(Config.LAUNCHER.CONNECTOR_CRAFT_PATH).toString())) {
			craftPath = fileContextPath + sb.toString();
		}
		sb.setLength(0);
		
		// 차단할 엔진 프로그램명
		String engineNames = StringUtil.EmptyString;
		if (!StringUtil.isNullOrEmpty(Config.LAUNCHER.CONNECTOR_ENGINE_NAMES)) {
			engineNames = Config.LAUNCHER.CONNECTOR_ENGINE_NAMES.toLowerCase();
		}
	
		result = new HttpConnectorInfoResult(
				Config.SERVER.LOGIN_SERVER_ADDRESS,
				Config.SERVER.LOGIN_SERVER_PORT,
				Config.LAUNCHER.CONNECTOR_BROWSER_URL,

				linbinPath,			
				Config.LAUNCHER.CONNECTOR_LINBIN_SIZE,
				Config.VERSION.CLIENT_VERSION_TO_STRING,				
				
				msdllPath,
				Config.LAUNCHER.CONNECTOR_MSDLL_SIZE,
				Config.VERSION.MSDL_VERSION_TO_STRING,
				
				libcocosPath,
				Config.LAUNCHER.CONNECTOR_LIBCOCOS_SIZE,
				Config.VERSION.LIBCOCOS_VERSION_TO_STRING,
					
				boxdllPath,
				craftPath,
				patchPath,				
	
				engineNames,

				Config.LAUNCHER.CONNECTOR_CLIENT_MAX_COUNT,
				Config.LAUNCHER.CONNECTOR_LOG,
				Config.LAUNCHER.CONNECTOR_CREATE_URI,
				Config.LAUNCHER.CONNECTOR_LOGIN_URI,
				Config.LAUNCHER.CONNECTOR_PATCH_VERSION,
				Config.LAUNCHER.CONNECTOR_CLIENT_SIDE_KEY,
				Config.LAUNCHER.CONNECTOR_DLL_PASSWORD

				//Config.SERVER.LOGIN_SERVER_ADDRESS,
				//Config.WEB.WEB_SERVER_PORT,
				//Config.LAUNCHER.CONNECTOR_HOMPAGE_URL,
				//Config.LAUNCHER.CONNECTOR_CHROM_BROWSER_USE,
				//Config.LAUNCHER.CONNECTOR_CHROM_CACHE_USE,
				//Config.LAUNCHER.CONNECTOR_CHROM_DEBUG_PORT,
				//loaderPath,
				//Config.LAUNCHER.CONNECTOR_PATCH_MERGE,
				//Config.LAUNCHER.CONNECTOR_PROCESS_MERGE,
				//Config.LAUNCHER.CONNECTOR_PROCESS_INTERVAL,
				//Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY,
				//Config.LAUNCHER.CONNECTOR_SESSION_KEY,
				//Config.LAUNCHER.CONNECTOR_LOGIN_MERGE_URI,
				//Config.LAUNCHER.CONNECTOR_ENGINE_URI,
				//Config.LAUNCHER.CONNECTOR_PROCESS_URI

				);
		
		result.setResultCode(HttpConnectorInfoValidation.SUCCESS.getMessage());
		SUCCESS_JSON			= new Gson().toJson(result);

		//System.out.println("sucess: " + SUCCESS_JSON);
		
		result.setResultCode(HttpConnectorInfoValidation.FAIL_REQUIRED_INFO.getMessage());
		FAIL_REQUIRED_INFO_JSON	= new Gson().toJson(result);

		//System.out.println("FAIL: " + FAIL_REQUIRED_INFO_JSON);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		// 필수 데이터 유효성 검사
		if (!result.validation()) {
			System.out.println(String.format("[ConnectorInfoDefine] required info failure -->\r\n%s", result.toString()));
			LoggerInstance.getInstance().addConnector(ConnectType.INFO, null, null, HttpConnectorInfoValidation.FAIL_REQUIRED_INFO.getMessage());
			return create_response_json(HttpResponseStatus.OK, FAIL_REQUIRED_INFO_JSON);
		}
		return create_response_json(HttpResponseStatus.OK, SUCCESS_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new ConnectorInfoDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
	
}

