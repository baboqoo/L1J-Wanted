package l1j.server.web.http.connector;

import l1j.server.Base64;
import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.utils.StringUtil;
import l1j.server.server.utils.CharsetUtil;


/**
 * 런처 설정 응답
 * @author LinOffice
 */
public class HttpConnectorInfoResult {
	private String result_code;
	private String serverIp;
	private String serverPort;
	private String browserUrl;
	private String linbin;	
	private String linbinSize;	
	private String linbinVersion;	
	private String libcocos;
	private String libcocosSize;	
	private String libcocosVersion;
	private String msdll;
	private String msdllSize;	
	private String msdllVersion;
	private String boxdll;
	private String craft;
	private String patch;	
	private String patchVersion;	
	private String engines;
	private String log;
	private String clientCount;
	private String createUri;
	private String loginUri;
	private String clientSideKey;
	private String dllPassword;
	private String C_CHANNEL;
	private String C_LOGIN;
	private String C_LOGOUT;
	private String S_CUSTOM_MESSAGE_BOX;
	private String S_KEY;
	private String S_EXTENDED_PROTO_BUF;
	private String C_EXTENDED_PROTO_BUF;
	//private String appcenterIp;
	//private int appcenterPort;
	//private String hompage;
	//private boolean chromBrowserUse;
	//private boolean chromCacheUse;
	//private int chromDebugPort;
	//private String loader;
	//private boolean patchMerge;
	//private boolean processMerge;
	//private int processInterval;
	//private String encryptKey;
	//private String sessionKey;
	//private String loginmergeUri;
	//private String engineUri;
	//private String processUri;
	
	public HttpConnectorInfoResult(
		    String _serverIp, int _serverPort, String _browserUrl, 
			String _linbin, int _linbinSize, String _linbinVersion,
			String _msdll, int _msdllSize, String _msdllVersion,
			String _libcocos, int _libcocosSize, String _libcocosVersion,
			String _boxdll, String _craft, String _patch, 
			String _engines, 
			int _clientCount, boolean _log, String _createUri, String _loginUri, int _patchVersion, int _clientSideKey, int _dllPassword
			//String appcenterIp, int appcenterPort, String hompage, boolean chromBrowserUse, boolean chromCacheUse, int chromDebugPort,
			//String loader, boolean patchMerge, boolean processMerge, int processInterval, String encryptKey, String sessionKey,
			//String loginmergeUri, String engineUri, String processUri						
			) {
		try {
			this.result_code			= StringUtil.EmptyString;
			this.serverIp				= Base64.encryptToBase64(_serverIp, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.serverPort				= Base64.encryptToBase64(Integer.toString(_serverPort), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.browserUrl				= Base64.encryptToBase64(_browserUrl, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.linbin					= Base64.encryptToBase64(_linbin, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.linbinSize				= Base64.encryptToBase64(Integer.toString(_linbinSize), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.linbinVersion			= Base64.encryptToBase64(_linbinVersion, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.libcocos				= Base64.encryptToBase64(_libcocos, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.libcocosSize			= Base64.encryptToBase64(Integer.toString(_libcocosSize), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);			
			this.libcocosVersion		= Base64.encryptToBase64(_libcocosVersion, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);			
			this.msdll					= Base64.encryptToBase64(_msdll, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.msdllSize				= Base64.encryptToBase64(Integer.toString(_msdllSize), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);			
			this.msdllVersion			= Base64.encryptToBase64(_msdllVersion, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.boxdll					= Base64.encryptToBase64(_boxdll, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.craft					= Base64.encryptToBase64(_craft, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.patch					= Base64.encryptToBase64(_patch, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.patchVersion			= Base64.encryptToBase64(Integer.toString(_patchVersion), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.engines				= Base64.encryptToBase64(_engines, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.log					= Base64.encryptToBase64(Boolean.toString(_log), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.clientCount			= Base64.encryptToBase64(Integer.toString(_clientCount), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.createUri				= Base64.encryptToBase64(_createUri, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.loginUri				= Base64.encryptToBase64(_loginUri, Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.clientSideKey			= Base64.encryptToBase64(Integer.toString(_clientSideKey), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.dllPassword			= Base64.encryptToBase64(Integer.toString(_dllPassword), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.C_CHANNEL				= Base64.encryptToBase64(Integer.toString(Opcodes.C_CHANNEL), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.C_LOGIN				= Base64.encryptToBase64(Integer.toString(Opcodes.C_LOGIN), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.C_LOGOUT				= Base64.encryptToBase64(Integer.toString(Opcodes.C_LOGOUT), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.S_CUSTOM_MESSAGE_BOX	= Base64.encryptToBase64(Integer.toString(3), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);;
			this.S_KEY					= Base64.encryptToBase64(Integer.toString(Opcodes.S_KEY), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.S_EXTENDED_PROTO_BUF	= Base64.encryptToBase64(Integer.toString(Opcodes.S_EXTENDED_PROTOBUF), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);
			this.C_EXTENDED_PROTO_BUF	= Base64.encryptToBase64(Integer.toString(Opcodes.C_EXTENDED_PROTOBUF), Config.LAUNCHER.CONNECTOR_ENCRYPT_KEY);

			//this.appcenterIp			= appcenterIp;
			//this.appcenterPort		= appcenterPort;
			//this.hompage				= hompage;
			//this.chromBrowserUse		= chromBrowserUse;
			//this.chromCacheUse		= chromCacheUse;
			//this.chromDebugPort		= chromDebugPort;
			//this.loader				= loader;
			//this.patchMerge			= patchMerge;
			//this.processMerge			= processMerge;
			//this.processInterval		= processInterval;
			//this.encryptKey			= encryptKey;
			//this.sessionKey			= sessionKey;
			//this.loginmergeUri		= loginmergeUri;
			//this.engineUri			= engineUri;
			//this.processUri			= processUri;

		} catch(Exception e) {
			System.out.println("Error");		
		}			
	}
	
	/**
	 * 결과 코드 설정
	 * @param value
	 */
	public void setResultCode(String value) {
		result_code = value;
	}
	
	/**
	 * 유효성 검사
	 * @return boolean
	 */
	public boolean validation(){
		return !StringUtil.isNullOrEmpty(serverIp)
				&& !StringUtil.isNullOrEmpty(serverPort)
				//&& !StringUtil.isNullOrEmpty(appcenterIp)
				//&& appcenterPort > 0
				&& !StringUtil.isNullOrEmpty(linbin)
				&& !StringUtil.isNullOrEmpty(linbinVersion)
				&& !StringUtil.isNullOrEmpty(msdll)
				&& !StringUtil.isNullOrEmpty(boxdll)				
				//&& !StringUtil.isNullOrEmpty(loader)
				//&& !StringUtil.isNullOrEmpty(encryptKey)
				//&& !StringUtil.isNullOrEmpty(sessionKey)
				&& !StringUtil.isNullOrEmpty(createUri)
				&& !StringUtil.isNullOrEmpty(loginUri)
				//&& !StringUtil.isNullOrEmpty(loginmergeUri)
				//&& !StringUtil.isNullOrEmpty(engineUri)
				//&& !StringUtil.isNullOrEmpty(processUri)
				;
	}
	
	@Override
	public String toString() {
		try {
			return new StringBuilder()
			.append("resul_code:\t").append(result_code)			
			.append("\r\nserverIp:\t").append(serverIp)
			.append("\r\nserverPortito:\t").append(serverPort)
			//.append("\r\nappcenterIp:\t").append(appcenterIp)
			//.append("\r\nappcenterPort:\t").append(appcenterPort)
			.append("\r\nversion:\t").append(linbinVersion)
			.append("\r\nversion:\t").append(libcocosVersion)
			.append("\r\nversion:\t").append(msdllVersion)
			//.append("\r\nhompage:\t").append(hompage)
			.append("\r\nbrowserUrl:\t").append(browserUrl)
			//.append("\r\nchromBrowserUse:\t").append(chromBrowserUse)
			//.append("\r\nchromCacheUse:\t").append(chromCacheUse)
			//.append("\r\nchromDebugPort:\t").append(chromDebugPort)
			.append("\r\nlinbin:\t\t").append(linbin)
			.append("\r\nmsdll:\t\t").append(msdll)
			.append("\r\nboxdll:\t\t").append(boxdll)
			//.append("\r\nloader:\t\t").append(loader)
			.append("\r\npatch:\t\t").append(patch)
			.append("\r\ncraft:\t\t").append(craft)
			.append("\r\nengines:\t").append(engines)
			.append("\r\nclientcount:\t").append(clientCount)
			.append("\r\nlog:\t\t").append(log)
			//.append("\r\npatchMerge:\t").append(patchMerge)
			//.append("\r\nprocessMerge:\t").append(processMerge)
			//.append("\r\nprocessInterval:\t").append(processInterval)
			//.append("\r\nencryptKey:\t").append(encryptKey)
			//.append("\r\nsessionKey:\t").append(sessionKey)
			.append("\r\ncreateUri:\t").append(createUri)
			.append("\r\nloginUri:\t").append(loginUri)
			//.append("\r\nloginmergeUri:\t").append(loginmergeUri)
			//.append("\r\nengineUri:\t").append(engineUri)
			//.append("\r\nprocessUri:\t").append(processUri)
			.append("\r\npatchVersion:\t").append(patchVersion)
			.append("\r\nClientSideKey:\t").append(clientSideKey)
			.append("\r\nC_CHANNEL:\t").append(C_CHANNEL)
			.append("\r\nC_LOGIN:\t").append(C_LOGIN)
			.append("\r\nC_LOGOUT:\t").append(C_LOGOUT)
			.append("\r\nS_CUSTOM_MESSAGE_BOX:\t").append(S_CUSTOM_MESSAGE_BOX)
			.append("\r\nS_KEY:\t").append(S_KEY)
			.append("\r\nS_EXTENDED_PROTO_BUF:\t").append(S_EXTENDED_PROTO_BUF)
			.append("\r\nC_EXTENDED_PROTO_BUF:\t").append(C_EXTENDED_PROTO_BUF)
			.append("\r\ndll_password:\t").append(dllPassword)
			.toString();
		} catch(Exception e) {
			System.out.println("Error");		
			return "";
		}			
	}
}

