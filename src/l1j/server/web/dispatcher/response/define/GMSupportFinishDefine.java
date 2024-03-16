package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.warehouse.PackageWarehouse;
import l1j.server.server.serverpackets.message.S_MsgAnnounce;
import l1j.server.server.serverpackets.warehouse.S_GoodsInvenNoti;
import l1j.server.server.utils.ColorUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.support.SupportDAO;
import l1j.server.web.dispatcher.response.support.SupportStatus;
import l1j.server.web.dispatcher.response.support.SupportVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class GMSupportFinishDefine extends HttpJsonModel {
	public GMSupportFinishDefine() {}
	private GMSupportFinishDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}
	
	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_0_JSON);
		}
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_0_JSON);
		}
		
		int support_id			= Integer.parseInt(request.read_post("support_id"));
		SupportVO vo			= SupportDAO.getSupport(support_id);
		if (vo == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
		}
		
		if (vo.getStatus() == SupportStatus.FINISH) {
			return create_response_json(HttpResponseStatus.OK, CODE_3_JSON);
		}
		
		int support_rate		= Config.WEB.SUPPORT_PAY_REWARD_RATE;
		if (vo.getPay_amount() < support_rate) {
			return create_response_json(HttpResponseStatus.OK, CODE_4_JSON);
		}
		
		L1PcInstance pc			= L1World.getInstance().getPlayer(vo.getCharacter_name());
		if (pc == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_5_JSON);
		}
		
		int support_count		= vo.getPay_amount() / support_rate;
		int less_pay_amount		= vo.getPay_amount() % support_rate;
		int reward_count		= support_count * Config.WEB.SUPPORT_PAY_REWARD_COUNT;
		int reward_item_id		= Config.WEB.SUPPORT_PAY_REWARD_ITEM_ID;
		
		String message = null;
		if (reward_item_id > 0) {
			// 아이템 지급
			if (!PackageWarehouse.itemShop(vo.getAccount_name(), Config.WEB.SUPPORT_PAY_REWARD_ITEM_ID, 0, reward_count)) {
				return create_response_json(HttpResponseStatus.OK, CODE_6_JSON);
			}
			pc.sendPackets(S_GoodsInvenNoti.GOODS_INVEN_ON);
			
			if (less_pay_amount > 0) {// 나머지 처리
				pc.addNcoin(less_pay_amount);
				pc.getAccount().updateNcoin();
				message = String.format(
						"Hello '%s'. Thank you so much! Your contribution of '%s'$ has been confirmed and processed. Please check the Service Warehouse for items received. '%d' NCoins have also been delivered to your account.", 
						pc.getName(), StringUtil.comma(vo.getPay_amount()), StringUtil.comma(less_pay_amount));
			} else {
				message = String.format(
						"Hello '%s'. Thank you so much! Your contribution of '%s'$ has been confirmed and processed. Please check the Service Warehouse for items received.", 
						pc.getName(), StringUtil.comma(vo.getPay_amount()));
			}
		} else {
			// NCOIN 지급
			if (less_pay_amount > 0) {// 나머지 처리
				reward_count += less_pay_amount;
			}
			pc.addNcoin(reward_count);
			pc.getAccount().updateNcoin();
			if (less_pay_amount > 0) {// 나머지 처리
				message = String.format(
						"Hello '%s'. Thank you so much! Your contribution of '%s'$ has been confirmed and processed. Please check that '%d' NCoins have been delivered to your account.", 
						pc.getName(), StringUtil.comma(vo.getPay_amount()), StringUtil.comma(less_pay_amount));
			} else {
				message = String.format(
						"Hello '%s'. Thank you so much! Your contribution of '%s'$ has been confirmed and processed. Please check that '%d' NCoins have been delivered to your account.", 
						pc.getName(), StringUtil.comma(vo.getPay_amount()));
			}
		}
		
		if (message != null) {
			pc.sendPackets(new S_MsgAnnounce(message, ColorUtil.getWhiteRgbBytes()), true);
		}
		
		// 상태 변경
		vo.setStatus(SupportStatus.FINISH);
		if (SupportDAO.getInstance().update(vo)) {
			return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
		}
		return create_response_json(HttpResponseStatus.OK, CODE_7_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GMSupportFinishDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

