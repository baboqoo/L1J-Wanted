package l1j.server.web.dispatcher.response.define;

import java.sql.Timestamp;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.Config;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.customer.TelegramTransfer;
import l1j.server.web.dispatcher.response.support.SupportDAO;
import l1j.server.web.dispatcher.response.support.SupportStatus;
import l1j.server.web.dispatcher.response.support.SupportVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class SupportMessageDefine extends HttpJsonModel {
	//private static final String HEAD_MSG	= Config.WEB.WEB_SERVER_NAME + " 입금완료 메세지\n\n";
	private static final String HEAD_MSG = Config.WEB.WEB_SERVER_NAME + " Deposit Completed Message\n\n";
	
	public SupportMessageDefine() {}
	private SupportMessageDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		// 인게임 검증
		if (!request.isIngame()){
			return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
		}
		// 계정 검증
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_3_JSON);
		}
		// 대표 캐릭터 검증
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_4_JSON);
		}
		// TODO 인게임 캐릭터 검증
		L1PcInstance pc		= getIngamePlayer();
		if (pc == null){
			return create_response_json(HttpResponseStatus.OK, CODE_5_JSON);
		}
		
		String msg			= request.read_post("msg");
		int pay_amount		= Integer.parseInt(msg.replaceAll(StringUtil.CommaString, StringUtil.EmptyString));
		boolean result		= do_support_insert(pc, pay_amount);
		if (!result) {
			return create_response_json(HttpResponseStatus.OK, CODE_0_JSON);
		}
		if (Config.WEB.TELEGRAM_BOT_ACTIVE) {
			StringBuilder sb	= new StringBuilder();
			/*sb.append(HEAD_MSG).append("계정:\t").append(pc.getAccountName());
			sb.append("\n케릭터:\t").append(pc.getName());
			sb.append("\n입금액:\t").append(msg).append("원");*/
			sb.append(HEAD_MSG).append("Account:\t").append(pc.getAccountName());
			sb.append("\nCharacter:\t").append(pc.getName());
			sb.append("\nDeposit Amount:\t").append(msg).append("$");			
			boolean message_flag = TelegramTransfer.excute(sb.toString());
			return create_response_json(HttpResponseStatus.OK, message_flag ? CODE_1_JSON : CODE_6_JSON);
		}
		return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
	}
	
	/**
	 * 후원 내용 디비 저장
	 * @param pay_amount
	 * @return boolean
	 */
	boolean do_support_insert(L1PcInstance pc, int pay_amount) {
		SupportDAO dao = SupportDAO.getInstance();
		SupportVO vo = new SupportVO(dao.get_id(), pc.getAccountName(), pc.getName(), pay_amount, new Timestamp(System.currentTimeMillis()), SupportStatus.STANBY);
		return dao.insert(vo);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new SupportMessageDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

