package l1j.server.web.http.connector;

import l1j.server.server.utils.StringUtil;

/**
 * 런처 유저 프로세스 갱신 응답
 * @author LinOffice
 */
public class HttpProcessMergeResult {
	public String result_code;
	public HttpProcessMergeResult() {
		result_code = StringUtil.EmptyString;
	}
}

