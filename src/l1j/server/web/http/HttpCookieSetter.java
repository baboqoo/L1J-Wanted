package l1j.server.web.http;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;

import java.util.ArrayList;
import java.util.Collection;

import l1j.server.Config;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;

/**
 * 쿠키 설정 클래스
 * @author LinOffice
 */
public class HttpCookieSetter {
	
	/**
	 * 쿠키 설정
	 * @param response - 응답
	 * @param maxAge - 만료 기간(-1: 세션[브라우저 종료시 제거])
	 * @param name - 쿠키 키
	 * @param value - 쿠키 값
	 */
	public static void set(HttpResponse response, long maxAge, String name, String value) {
		set(response, maxAge, null, StringUtil.SlushString, false, false, name, value);
	}
	
	/**
	 * 쿠키 설정
	 * @param response - 응답
	 * @param maxAge - 만료 기간(-1: 세션[브라우저 종료시 제거])
	 * @param domain - 도메인
	 * @param path - 경로
	 * @param httpOnly - javascript.cookie에서 접근 막기
	 * @param name - 쿠키 키
	 * @param value - 쿠키 값
	 */
	public static void set(HttpResponse response, long maxAge, String domain, String path, boolean httpOnly, String name, String value) {
		set(response, maxAge, null, StringUtil.SlushString, httpOnly, false, name, value);
	}
	
	/**
	 * 쿠키 설정
	 * @param response - 응답
	 * @param maxAge - 만료 기간(-1: 세션[브라우저 종료시 제거])
	 * @param domain - 도메인
	 * @param path - 경로
	 * @param httpOnly - javascript.cookie에서 접근 막기
	 * @param secure - https 제한
	 * @param name - 쿠키 키
	 * @param value - 쿠키 값
	 */
	public static void set(HttpResponse response, long maxAge, String domain, String path, boolean httpOnly, boolean secure, String name, String value) {
		if (StringUtil.isNullOrEmpty(name)) {
			return;
		}
		Cookie cookie = new DefaultCookie(name, value);
		if (maxAge >= 0) {// 만료 기간(-1: 세션)
			cookie.setMaxAge(maxAge);
		}
		if (!StringUtil.isNullOrEmpty(domain)){
			cookie.setDomain(domain);
		}
		cookie.setPath(path);
		if (httpOnly) {// javascript.cookie에서 접근 막기
			cookie.setHttpOnly(true);
		}
		if (secure) {// https 제한
			cookie.setSecure(true);
		}
		response.headers().set(HttpHeaderNames.SET_COOKIE, get_server_cookie_encoder().encode(cookie));
	}
	
	/**
	 * 쿠키 설정
	 * @param response - 응답
	 * @param maxAge - 만료 기간(-1: 세션[브라우저 종료시 제거])
	 * @param cookie_list - 설정할 쿠키 리스트
	 */
	public static void set(HttpResponse response, long maxAge, ArrayList<KeyValuePair<String, String>> cookie_list) {
		set(response, maxAge, null, StringUtil.SlushString, false, false, cookie_list);
	}
	
	/**
	 * 쿠키 설정
	 * @param response - 응답
	 * @param maxAge - 만료 기간(-1: 세션[브라우저 종료시 제거])
	 * @param domain - 도메인
	 * @param path - 경로
	 * @param httpOnly - javascript.cookie에서 접근 막기
	 * @param secure - https 제한
	 * @param cookie_list - 설정할 쿠키 리스트
	 */
	public static void set(HttpResponse response, long maxAge, String domain, String path, boolean httpOnly, boolean secure, ArrayList<KeyValuePair<String, String>> cookie_list) {
		if (cookie_list == null || cookie_list.isEmpty()) {
			return;
		}
		for (KeyValuePair<String, String> pair : cookie_list) {
			if (StringUtil.isNullOrEmpty(pair.key)) {
				continue;
			}
			Cookie cookie = new DefaultCookie(pair.key, pair.value);
			if (maxAge >= 0) {// 만료 기간(-1: 세션)
				cookie.setMaxAge(maxAge);
			}
			if (!StringUtil.isNullOrEmpty(domain)){
				cookie.setDomain(domain);
			}
			cookie.setPath(path);
			if (httpOnly) {// javascript.cookie에서 접근 막기
				cookie.setHttpOnly(true);
			}
			if (secure) {// https 제한
				cookie.setSecure(true);
			}
			response.headers().add(HttpHeaderNames.SET_COOKIE, get_server_cookie_encoder().encode(cookie));
		}
	}
	
	/**
	 * 쿠키 삭제
	 * @param response - 응답
	 * @param name - 쿠키 키
	 */
	public static void remove(HttpResponse response, String name) {
		if (StringUtil.isNullOrEmpty(name)) {
			return;
		}
		Cookie cookie = new DefaultCookie(name, StringUtil.EmptyString);
		cookie.setMaxAge(0);
		response.headers().set(HttpHeaderNames.SET_COOKIE, get_server_cookie_encoder().encode(cookie));
	}
	
	/**
	 * 쿠키 전체 삭제
	 * @param response
	 * @param collection
	 */
	public static void clear(HttpResponse response, Collection<Cookie> collection) {
		if (collection == null) {
			return;
		}
		for (Cookie cookie : collection) {
			cookie.setMaxAge(0);
			response.headers().set(HttpHeaderNames.SET_COOKIE, get_server_cookie_encoder().encode(cookie));
		}
	}
	
	/**
	 * 쿠키 SameSite
	 * @return ServerCookieEncoder
	 */
	static ServerCookieEncoder get_server_cookie_encoder() {
		switch (Config.WEB.COOKIE_SAME_SITE) {
		case LAX:
			return ServerCookieEncoder.LAX;
		case STRICT:
			return ServerCookieEncoder.STRICT;
		default:
			throw new IllegalArgumentException("invalid arguments ServerCookieEncoder null");
		}
	}
}

