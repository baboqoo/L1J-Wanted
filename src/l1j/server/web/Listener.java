package l1j.server.web;

import l1j.server.web.dispatcher.DispatcherLoader;
import l1j.server.web.dispatcher.response.account.LoginFactory;
import l1j.server.web.dispatcher.response.alim.AlimDAO;
import l1j.server.web.dispatcher.response.board.BoardDAO;
import l1j.server.web.dispatcher.response.content.ContentDAO;
import l1j.server.web.dispatcher.response.coupon.CouponDAO;
import l1j.server.web.dispatcher.response.customer.CustomerDAO;
import l1j.server.web.dispatcher.response.editor.Emblem;
import l1j.server.web.dispatcher.response.goods.GoodsDAO;
import l1j.server.web.dispatcher.response.guide.GuideDAO;
import l1j.server.web.dispatcher.response.item.ItemDAO;
import l1j.server.web.dispatcher.response.keyword.KeywordLoader;
import l1j.server.web.dispatcher.response.market.MPSECore;
import l1j.server.web.dispatcher.response.notice.NoticeDAO;
import l1j.server.web.dispatcher.response.pitch.PitchDAO;
import l1j.server.web.dispatcher.response.powerbook.L1InfoDAO;
import l1j.server.web.dispatcher.response.promotion.PromotionDAO;
import l1j.server.web.dispatcher.response.report.ReportDAO;
import l1j.server.web.dispatcher.response.support.SupportDAO;
import l1j.server.web.dispatcher.response.trade.TradeDAO;
import l1j.server.web.dispatcher.response.world.BloodPledgeDAO;
import l1j.server.web.http.connector.HttpAccountManager;

/**
 * 웹 데이터 Listener
 * @author LinOffice
 */
public class Listener {
	
	/**
	 * 웹 데이터(캐시) 로드
	 */
	public static void initialized() {
		DispatcherLoader.getInstance();
		CNB.load();
		loadCache();
	}
	
	static void loadCache() {
		Emblem.getInstance();
		PromotionDAO.getInstance();
		ItemDAO.getInstance();
		MPSECore.getInstance();
		BloodPledgeDAO.getInstance();
		TradeDAO.getInstance();
		NoticeDAO.getInstance();
		BoardDAO.getInstance();
		ContentDAO.getInstance();
		PitchDAO.getInstance();
		GuideDAO.getInstance();
		L1InfoDAO.getInstance();
		CustomerDAO.getInstance();
		GoodsDAO.getInstance();
		AlimDAO.getInstance();
		CouponDAO.getInstance();
		ReportDAO.getInstance();
		KeywordLoader.getInstance();
		SupportDAO.getInstance();
	}
	
	/**
	 * 웹 데이터(캐시) 해제
	 */
	public static void destroyed() {
		Emblem.release();
		PromotionDAO.release();
		ItemDAO.release();
		MPSECore.release();
		BloodPledgeDAO.release();
		TradeDAO.release();
		NoticeDAO.release();
		BoardDAO.release();
		ContentDAO.release();
		PitchDAO.release();
		GuideDAO.release();
		L1InfoDAO.release();
		CustomerDAO.release();
		GoodsDAO.release();
		AlimDAO.release();
		CouponDAO.release();
		ReportDAO.release();
		KeywordLoader.release();
		SupportDAO.release();
		LoginFactory.clear();
		HttpAccountManager.clear();
		CNB.dispose();
		WebAuthorizatior.clear_auth_address();
		DispatcherLoader.release();
	}
}

