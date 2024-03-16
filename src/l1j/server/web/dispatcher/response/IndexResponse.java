package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l1j.server.Config;
import l1j.server.server.utils.KeyValuePair;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.AccountDAO;
import l1j.server.web.dispatcher.response.account.AccountVO;
import l1j.server.web.dispatcher.response.account.LoginFactory;
import l1j.server.web.dispatcher.response.board.BoardDAO;
import l1j.server.web.dispatcher.response.content.ContentDAO;
import l1j.server.web.dispatcher.response.goods.GoodsDAO;
import l1j.server.web.dispatcher.response.keyword.KeywordLoader;
import l1j.server.web.dispatcher.response.notice.NoticeDAO;
import l1j.server.web.dispatcher.response.powerbook.L1InfoDAO;
import l1j.server.web.dispatcher.response.promotion.PromotionDAO;
import l1j.server.web.http.HttpCookieSetter;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

import com.google.gson.Gson;

public class IndexResponse extends HttpResponseModel {
	private static KeyValuePair<String, Object> GUIDE_PAIR;
	private static KeyValuePair<String, Object> GOODS_PAIR;
	
	private static final KeyValuePair<String, String> TELEGRAM_PAIR		= new KeyValuePair<String, String>("{TELEGRAM}",	null);
	//private static final String TELEGRAM_INGAME		= "<a href=\"javascript:popupShowSimple('인게임에선 사용할 수 없습니다.');\">Telegram</a>";
	private static final String TELEGRAM_INGAME = "<a href=\"javascript:popupShowSimple('Cannot be used in-game.');\">Telegram</a>";
	private static final String TELEGRAM_NOT_INGAME	= "<a href=\"https://t.me/" + Config.WEB.TELEGRAM_ID.substring(1) + "\" target=\"_blank\">Telegram</a>";
	
	private static final KeyValuePair<String, Object> PROMOTIONS_PAIR	= new KeyValuePair<String, Object>("PROMOTIONS",	null);
	private static final KeyValuePair<String, Object> NOTICE_PAIR		= new KeyValuePair<String, Object>("NOTICE",		null);
	private static final KeyValuePair<String, Object> UPDATE_PAIR		= new KeyValuePair<String, Object>("UPDATE",		null);
	private static final KeyValuePair<String, Object> EVENT_PAIR		= new KeyValuePair<String, Object>("EVENT",			null);
	private static final KeyValuePair<String, Object> KEYWORD_PAIR		= new KeyValuePair<String, Object>("KEYWORD",		null);
	
	private static final KeyValuePair<String, Object> COMMUNITY_PAIR	= new KeyValuePair<String, Object>("COMMUNITY",		null);
	private static final KeyValuePair<String, Object> CONTENT_PAIR		= new KeyValuePair<String, Object>("CONTENT",		null);
	
	private static final KeyValuePair<String, Object> MODAL_PAIR;
	static {
		if (StringUtil.isNullOrEmpty(Config.WEB.INDEX_PAGE_MODAL_TITLE) || StringUtil.isNullOrEmpty(Config.WEB.INDEX_PAGE_MODAL_SRC)) {
			MODAL_PAIR	= null;
		} else {
			Map<String, Object> modal_data = new HashMap<String, Object>();
			modal_data.put("TITLE", Config.WEB.INDEX_PAGE_MODAL_TITLE);
			modal_data.put("SRC", Config.WEB.INDEX_PAGE_MODAL_SRC);
			modal_data.put("COOKIE_NAME", Config.WEB.INDEX_PAGE_MODAL_COOKIE_NAME);
			modal_data.put("COOKIE_VALUE", Config.WEB.INDEX_PAGE_MODAL_COOKIE_VALUE);
			modal_data.put("COOKIE_MAX_AGE", Config.WEB.INDEX_PAGE_MODAL_COOKIE_MAX_AGE);
			modal_data.put("COOKIE_SAME_SITE", Config.WEB.COOKIE_SAME_SITE.toDesc());
			MODAL_PAIR		= new KeyValuePair<String, Object>("MODAL",	modal_data);
		}
	}
	
	public IndexResponse() {}
	private IndexResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		AccountVO launcher_account = null;
		if (request.isLauncher()) {
			launcher_account = launcher_login();
		}
		
		ArrayList<KeyValuePair<String, String>> params = new ArrayList<KeyValuePair<String, String>>();
		params.add(SERVER_NAME_PAIR);
		params.add(SUGGEST_ENABLE_PAIR);
		params.add(get_cnb_pair());
		params.add(get_user_data_pair());
		params.add(get_now_pair());
		
		TELEGRAM_PAIR.value		= !request.isIngame() && Config.WEB.TELEGRAM_ACTIVE ? TELEGRAM_NOT_INGAME : TELEGRAM_INGAME;
		params.add(TELEGRAM_PAIR);
		
		// 인덱스 페이지에서 사용될 데이터
		ArrayList<KeyValuePair<String, Object>> index_data_params				= new ArrayList<KeyValuePair<String, Object>>();
		PROMOTIONS_PAIR.value	= PromotionDAO.getPromotionList();
		NOTICE_PAIR.value		= NoticeDAO.getList(1, 4, 0);
		UPDATE_PAIR.value		= NoticeDAO.getList(1, 4, 1);
		EVENT_PAIR.value		= NoticeDAO.getList(1, 4, 2);
		KEYWORD_PAIR.value		= KeywordLoader.getKeyword();
		index_data_params.add(PROMOTIONS_PAIR);
		index_data_params.add(NOTICE_PAIR);
		index_data_params.add(UPDATE_PAIR);
		index_data_params.add(EVENT_PAIR);
		index_data_params.add(KEYWORD_PAIR);
		if (GUIDE_PAIR == null) {
			GUIDE_PAIR = new KeyValuePair<String, Object>("GOUIDE",				L1InfoDAO.getInfoMainList());
		}
		index_data_params.add(GUIDE_PAIR);
		
		COMMUNITY_PAIR.value	= BoardDAO.getList(1, 4);
		CONTENT_PAIR.value		= ContentDAO.getList(1, 4);
		index_data_params.add(COMMUNITY_PAIR);
		index_data_params.add(CONTENT_PAIR);
		if (GOODS_PAIR == null) {
			GOODS_PAIR = new KeyValuePair<String, Object>("GOODS",				GoodsDAO.getList(1, 4));
		}
		index_data_params.add(GOODS_PAIR);
		if (MODAL_PAIR != null) {
			index_data_params.add(MODAL_PAIR);
		}
		params.add(new KeyValuePair<String, String>("{INDEX_DATA}",				new Gson().toJson(index_data_params)));
		
		// parameter define
		String html = StringUtil.replace(dispatcher.getHtml(), params);
		params.clear();
		params = null;
		
		HttpResponse response = create_response_html(HttpResponseStatus.OK, html);
		if (launcher_account != null) {
			HttpCookieSetter.set(response, -1, LoginFactory.COOKIE_AUTH_TOKEN, launcher_account.getAuthToken());
		}
		return response;
	}
	
	/**
	 * 런처에 의한 로그인
	 * @return AccountVO
	 */
	AccountVO launcher_login() {
		String launcher_account		= request.read_parameters_at_once("account");
		String launcher_password	= request.read_parameters_at_once("password");
		if (StringUtil.isNullOrEmpty(launcher_account) || StringUtil.isNullOrEmpty(launcher_password)) {
			return null;
		}
		// 동일한 계정 존재
		if (account != null && account.getName().equals(launcher_account)) {
			return null;
		}
		// 계정이 존재하지만 다른 계정으로 로그인
		if (account != null) {
			LoginFactory.remove_launcher(account);
		}
		AccountVO vo				= AccountDAO.getInstance().getAccount(launcher_account, launcher_password);
		if (vo == null) {
			return null;
		}
		vo.setIp(request.get_remote_address_string());
		LoginFactory.put_launcher(vo);
		account = vo;
		return vo;
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new IndexResponse(request, model);
	}

}

