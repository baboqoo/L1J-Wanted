package l1j.server.web.http.connector;

import l1j.server.server.utils.StringUtil;

/**
 * 런처 계정생성 응답
 * @author LinOffice
 */
public class HttpAccountCreateResult {
	public String result_code;
	public HttpAccountCreateResult() {
		result_code = StringUtil.EmptyString;
	}
}

