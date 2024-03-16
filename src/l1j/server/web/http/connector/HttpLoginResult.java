package l1j.server.web.http.connector;

import l1j.server.server.utils.StringUtil;

/**
 * 런처 로그인 응답
 * @author LinOffice
 */
public class HttpLoginResult {
	public String result_code;
	public String auth_token;
	public HttpLoginResult() {
		result_code = StringUtil.EmptyString;
		auth_token	= StringUtil.EmptyString;
	}
}

