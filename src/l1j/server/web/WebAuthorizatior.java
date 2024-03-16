package l1j.server.web;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.utils.StringUtil;

/**
 * Authorization Handler
 * @author LinOffice
 */
public class WebAuthorizatior {
	private static final ArrayList<String> AUTH_ADDRESS = new ArrayList<String>();
	
	/**
	 * 관리자 승인 검증
	 * @param address
	 * @return boolean
	 */
	protected static boolean is_auth_address(final String address) {
		return !StringUtil.isNullOrEmpty(address) && AUTH_ADDRESS.contains(address);
	}
	
	/**
	 * 관리자 승인 등록
	 * @param address
	 */
	public static void add_auth_address(final String address) {
		if (StringUtil.isNullOrEmpty(address) || AUTH_ADDRESS.contains(address)) {
			return;
		}
		AUTH_ADDRESS.add(address);
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				remove_auth_address(address);
			}
		}, Config.WEB.WEB_GM_AUTH_SECOND * 1000L);
	}
	
	/**
	 * 관리자 승인 제거
	 * @param address
	 */
	public static void remove_auth_address(final String address) {
		if (StringUtil.isNullOrEmpty(address)) {
			return;
		}
		AUTH_ADDRESS.remove(address);
	}
	
	/**
	 * 관리자 승인 목록 전체 제거
	 */
	protected static void clear_auth_address() {
		AUTH_ADDRESS.clear();
	}
}

