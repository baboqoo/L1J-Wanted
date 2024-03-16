package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.sql.Timestamp;
import java.util.Map;

import l1j.server.server.Account;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.warehouse.PackageWarehouse;
import l1j.server.server.monitor.Logger.WebActionType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.warehouse.S_GoodsInvenNoti;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.alim.AlimDAO;
import l1j.server.web.dispatcher.response.alim.AlimVO;
import l1j.server.web.dispatcher.response.goods.GoodsDAO;
import l1j.server.web.dispatcher.response.goods.GoodsPriceType;
import l1j.server.web.dispatcher.response.goods.GoodsVO;
import l1j.server.web.dispatcher.response.item.ItemDAO;
import l1j.server.web.dispatcher.response.item.ItemVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class GoodsBuyDefine extends HttpJsonModel {
	public GoodsBuyDefine() {}
	private GoodsBuyDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		// 인게임 검증
		if (!request.isIngame()) {
			return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
		}
		// 계정 검증
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_3_JSON);
		}
		// 대표 케릭터 검증
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_4_JSON);
		}
		// TODO 인게임 캐릭터 검증
		L1PcInstance pc				= getIngamePlayer();
		if (pc == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_5_JSON);
		}
		
		Account	acc					= pc.getAccount();
		Map<String, String> post	= request.get_post_datas();
		int id						= Integer.parseInt(post.get("id"));
		GoodsVO goods				= GoodsDAO.getGoodsInfo(id);
		int itemId					= goods.getItemid();
		int itemCount				= Integer.parseInt(post.get("itemCount"));
		int itemPrice				= Integer.parseInt(post.get("itemPrice"));
		int itemEnchant				= goods.getEnchant();
		GoodsPriceType price_type	= goods.getPriceType();
		int saved_point				= goods.getSavedPoint();
		ItemVO item					= ItemDAO.getItemInfo(itemId);
		if (item == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_6_JSON);
		}
		// 가격 검증
		switch (price_type) {
		case NCOIN:
			if (acc.getNcoin() < itemPrice) {
				return create_response_json(HttpResponseStatus.OK, CODE_7_JSON);
			}
			break;
		case NPOINT:
			if (acc.getNpoint() < itemPrice) {
				return create_response_json(HttpResponseStatus.OK, CODE_8_JSON);
			}
			break;
		}
		
		// 구매 처리
		if (!PackageWarehouse.itemShop(acc.getName(), itemId, itemEnchant, itemCount)) {
			return create_response_json(HttpResponseStatus.OK, CODE_6_JSON);
		}
		pc.sendPackets(S_GoodsInvenNoti.GOODS_INVEN_ON);
		
		// 포인트 처리
		switch (price_type) {
		case NCOIN:
			int afterCoin	= acc.getNcoin() - itemPrice;
			acc.setNcoin(afterCoin);
			acc.updateNcoin();
			break;
		case NPOINT:
			int afterPoint	= acc.getNpoint() - itemPrice;
			acc.setNpoint(afterPoint);
			acc.updateNpoint();
			break;
		}
		
		StringBuilder sb = new StringBuilder();
		if (itemEnchant > 0) {
			sb.append(StringUtil.PlusString).append(itemEnchant);
		}
		//sb.append(item.getName()).append(StringUtil.EmptyOneString).append(StringUtil.comma(itemCount)).append("개").append("</br>").append("구매완료");
		sb.append(item.getName()).append(StringUtil.EmptyOneString).append(StringUtil.comma(itemCount)).append(" units").append("</br>").append("Purchase completed");
		AlimVO alim = new AlimVO(acc.getName(), sb.toString(), 1, new Timestamp(System.currentTimeMillis()), false);
		AlimDAO.getInstance().insert(alim);
		
		// 적립 포인트
		int save_point = 0;
		if (saved_point > 0) {
			save_point = (goods.getPack() > 0 ? itemCount / goods.getPack() : itemCount) * saved_point;
			int afterPoint	= acc.getNpoint() + save_point;
			acc.setNpoint(afterPoint);
			acc.updateNpoint();
		}
		LoggerInstance.getInstance().addWebAction(WebActionType.GOODS_BUY, acc.getName(), pc.getName(), 
				String.format("ITEM_ID(%d), COUNT(%d), PRICE(%d), SAVE_POINT(%d)", itemId, itemCount, itemPrice, save_point));
		return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GoodsBuyDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

