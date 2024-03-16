package l1j.server.web.http.connector;

import l1j.server.server.utils.StringUtil;

/**
 * 런처 불허가 엔진 사용 로그 응답
 * @author LinOffice
 */
public class HttpEngineLogResult {
	public String result_code;
	public HttpEngineLogResult() {
		result_code = StringUtil.EmptyString;
	}
}

