package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.sql.Timestamp;

import l1j.server.server.Account;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.warehouse.PackageWarehouse;
import l1j.server.server.monitor.Logger.WebActionType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.serverpackets.warehouse.S_GoodsInvenNoti;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.alim.AlimDAO;
import l1j.server.web.dispatcher.response.alim.AlimVO;
import l1j.server.web.dispatcher.response.coupon.CouponDAO;
import l1j.server.web.dispatcher.response.coupon.CouponVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class CouponUseDefine extends HttpJsonModel {
	public CouponUseDefine() {}
	private CouponUseDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		// 인게임 검증
		if (!request.isIngame()){
			return create_response_json(HttpResponseStatus.OK, CODE_3_JSON);
		}
		// 계정 검증
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_4_JSON);
		}
		// 대표 케릭터 검증
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_5_JSON);
		}
		// TODO 인게임 캐릭터 검증
		L1PcInstance pc		= getIngamePlayer();
		if (pc == null){
			return create_response_json(HttpResponseStatus.OK, CODE_6_JSON);
		}
		CouponDAO dao		= CouponDAO.getInstance();
		CouponVO coupon		= dao.getCoupon(request.read_post("number"));
		if (coupon == null){
			return create_response_json(HttpResponseStatus.OK, CODE_7_JSON);
		}
		
		// 이미 사용한 쿠폰
		if (coupon.isStatus()){
			return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
		}
		
		Account	acc			= pc.getAccount();
		coupon.setStatus(true);// 상태값 변경
		coupon.setUseAccount(acc.getName());
		coupon.setUseTime(new Timestamp(System.currentTimeMillis()));
		if (!dao.updateStatus(coupon)){
			return create_response_json(HttpResponseStatus.OK, CODE_MINUS_JSON);
		}
		
		switch (coupon.getType()) {
		case VOUCHER:// 수표 생성
			if (!PackageWarehouse.itemShop(acc.getName(), L1ItemId.CHEQUE, 0, coupon.getValue())) {
				return create_response_json(HttpResponseStatus.OK, CODE_MINUS_JSON);
			}
			pc.sendPackets(S_GoodsInvenNoti.GOODS_INVEN_ON);
			break;
		case NCOIN:// NCOIN 충전
			int afterNcoin = acc.getNcoin() + coupon.getValue();
			acc.setNcoin(afterNcoin);
			acc.updateNcoin();
			//pc.sendPackets(new S_SystemMessage(String.format("N코인 %s 원 충전되었습니다.(쿠폰사용)", StringUtil.comma(coupon.getValue()))), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(102), StringUtil.comma(coupon.getValue())), true);
			break;
		case NPOINT:// NPOINT 충전
			int afterNpoint = acc.getNpoint() + coupon.getValue();
			acc.setNpoint(afterNpoint);
			acc.updateNpoint();
			//pc.sendPackets(new S_SystemMessage(String.format("N포인트 %s 원 적립되었습니다.(쿠폰사용)", StringUtil.comma(coupon.getValue()))), true);
			pc.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(103), StringUtil.comma(coupon.getValue())), true);
			break;
		}
		
		// 알림 로그 등록
		StringBuilder sb = new StringBuilder();
		sb.append(coupon.getType().toDesc()).append(StringUtil.comma(coupon.getValue())).append(coupon.getType().toExtension());
		//sb.append(" 쿠폰").append("</br>").append("사용완료");
		sb.append(" Coupon").append("</br>").append("Used");
		AlimVO alim = new AlimVO(acc.getName(), sb.toString(), 3, new Timestamp(System.currentTimeMillis()), false);
		AlimDAO.getInstance().insert(alim);
		
		LoggerInstance.getInstance().addWebAction(WebActionType.COUPON_USE, acc.getName(), pc.getName(), 
				String.format("COUPON_TYPE(%s), COUPON_NUMBER(%s), COUPON_VALUE(%d)", 
						coupon.getType().name(), coupon.getNumber(), coupon.getValue()));
		return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);// 성공코드 1
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CouponUseDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}


