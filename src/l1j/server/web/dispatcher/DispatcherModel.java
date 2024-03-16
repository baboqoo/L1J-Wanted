package l1j.server.web.dispatcher;

import l1j.server.Config;
import l1j.server.web.http.HttpModel;
import l1j.server.web.http.HttpResponseModel;

/**
 * Appcenter Dispatcher Model
 * 앱센터의 요청과 응답 데이터
 * @author LinOffice
 */
public class DispatcherModel {
	private String uri;				// 요청 uri
	private String path;			// html 파일 경로
	private String html;			// html 문서 데이터
	private HttpModel response;		// 응답 모델(업무처리)
	private String cnbType;			// 페이지 주 번호(1depth)
	private String cnbSubType;		// 페이지 서브 번호(2depth)
	private boolean needIngame;		// 인게임 전용
	private boolean needLauncher;	// 런처 전용
	private boolean needLogin;		// 로그인 전용
	private boolean needGm;			// 관리자 전용
	private boolean Json;			// JSON 전용
	private boolean fileUpload;		// 파일 업로드 처리
	
	public DispatcherModel(String uri, String path, String html, HttpModel response, String cnbType, String cnbSubType, boolean needIngame, boolean needLauncher, boolean needLogin, boolean needGm, boolean Json, boolean fileUpload) {
		this.uri			= uri;
		this.path			= path;
		this.html			= html;
		this.response		= response;
		this.cnbType		= cnbType;
		this.cnbSubType		= cnbSubType;
		this.needIngame		= needIngame;
		this.needLauncher	= needLauncher;
		this.needLogin		= needLogin;
		this.needGm			= needGm;
		this.Json			= Json;
		this.fileUpload		= fileUpload;
	}
	
	public String getUri() {
		return uri;
	}
	public String getPath() {
		return path;
	}
	public String getHtml() {
		if (Config.WEB.WEB_FILE_NO_CACHE && html != null) {
			return ((HttpResponseModel)response).load_file_string(path);// html문서 로드
		}
		return html;
	}
	public HttpModel getResponse() {
		return response;
	}
	public String getCnbType() {
		return cnbType;
	}
	public String getCnbSubType() {
		return cnbSubType;
	}
	public boolean isNeedIngame() {
		return needIngame;
	}
	public boolean isNeedLauncher() {
		return needLauncher;
	}
	public boolean isNeedLogin() {
		return needLogin;
	}
	public boolean isNeedGm() {
		return needGm;
	}
	public boolean isJson() {
		return Json;
	}
	public boolean isFileUpload() {
		return fileUpload;
	}
}

