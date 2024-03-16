package l1j.server.web.dispatcher.response.define;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Random;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.coupon.CouponDAO;
import l1j.server.web.dispatcher.response.coupon.CouponType;
import l1j.server.web.dispatcher.response.coupon.CouponVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class CouponCreateDefine extends HttpJsonModel {
	public CouponCreateDefine() {}
	private CouponCreateDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null || !account.isGm()) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		CouponDAO dao = CouponDAO.getInstance();
		String number;
		while(true) {
			number = createCoupon();
			if (dao.getCoupon(number) == null) {// 중복 쿠폰번호 체크
				break;
			}
		}
		Map<String, String> post = request.get_post_datas();
		CouponType type	= CouponType.fromString(post.get("type"));
		int price		= Integer.parseInt(post.get("price"));
		CouponVO coupon	= new CouponVO(number, type, price, false, null, new Timestamp(System.currentTimeMillis()), null);
		if (dao.insert(coupon)) {
			return create_response_json(HttpResponseStatus.OK, new Gson().toJson(coupon));
		}
		return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
	}
	
	private static final Random rnd = new Random();
	private static final int COUNT = 10; // n자리 쿠폰 
	private static final char[] CHARS = { 
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 
		'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	};
	
	private String createCoupon() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < COUNT; i++) {
			sb.append(CHARS[rnd.nextInt(CHARS.length)]);
		}
		return sb.toString();
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CouponCreateDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

