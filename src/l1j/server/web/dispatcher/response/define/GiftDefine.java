package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.sql.Timestamp;
import java.util.Map;

import l1j.server.server.Account;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.monitor.Logger.WebActionType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.message.S_MsgAnnounce;
import l1j.server.server.utils.ColorUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.alim.AlimDAO;
import l1j.server.web.dispatcher.response.alim.AlimVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class GiftDefine extends HttpJsonModel {
	public GiftDefine() {}
	private GiftDefine(HttpRequestModel request, DispatcherModel model) {
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
		int ncoin_amount			= Integer.parseInt(post.get("ncoin_amount"));
		String friend_name			= post.get("friend_name");
		
		if (acc.getNcoin() < ncoin_amount) {
			return create_response_json(HttpResponseStatus.OK, CODE_6_JSON);
		}
		L1PcInstance friend			= L1World.getInstance().getPlayer(friend_name);
		if (friend == null || friend.getNetConnection() == null || friend.getAccount() == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_7_JSON);
		}
		if (pc == friend) {
			return create_response_json(HttpResponseStatus.OK, CODE_8_JSON);
		}
		Account	target_acc			= friend.getAccount();
		
		int owner_afterCoin			= acc.getNcoin() - ncoin_amount;
		acc.setNcoin(owner_afterCoin);
		acc.updateNcoin();
		
		int target_afterCoin		= target_acc.getNcoin() + ncoin_amount;
		target_acc.setNcoin(target_afterCoin);
		target_acc.updateNcoin();
		
		StringBuilder sb = new StringBuilder();
		//sb.append("NCOIN ").append(StringUtil.EmptyOneString).append(StringUtil.comma(ncoin_amount)).append("COIN").append(", 대상 : ").append(friend_name).append("</br>").append("선물완료");
		sb.append("NCOIN ").append(StringUtil.EmptyOneString).append(StringUtil.comma(ncoin_amount)).append("COIN").append(", Recipient: ").append(friend_name).append("</br>").append("Gift completed");
		AlimVO alim = new AlimVO(acc.getName(), sb.toString(), 1, new Timestamp(System.currentTimeMillis()), false);
		AlimDAO.getInstance().insert(alim);
		
		String message = String.format(
				//"안녕하세요 '%s'님. '%s'님께서 고객님에게  NCOIN '%s'COIN을 선물하였습니다. 앱센터에서 보유하신 'NCOIN'을 확인하시기 바랍니다. 감사합니다.", 
				"Hello, '%s'. '%s' has gifted you NCOIN '%s' COIN. Please check your 'NCOIN' balance in the App Center. Thank you.",
				pc.getName(), friend_name, StringUtil.comma(ncoin_amount));
		friend.sendPackets(new S_MsgAnnounce(message, ColorUtil.getWhiteRgbBytes()), true);

		LoggerInstance.getInstance().addWebAction(WebActionType.NCOIN_GIFT, acc.getName(), pc.getName(), 
				String.format("NCOIN(%d), TARGET_NAME(%s)", ncoin_amount, friend_name));
		return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GiftDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

