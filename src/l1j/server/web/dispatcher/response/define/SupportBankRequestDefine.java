package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.sql.Timestamp;

import l1j.server.Config;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.customer.TelegramTransfer;
import l1j.server.web.dispatcher.response.support.SupportBankRequestVO;
import l1j.server.web.dispatcher.response.support.SupportDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class SupportBankRequestDefine extends HttpJsonModel {
	//private static final String HEAD_MSG	= Config.WEB.WEB_SERVER_NAME + " 후원 계좌 요청 메세지\n\n";
	private static final String HEAD_MSG = Config.WEB.WEB_SERVER_NAME + " Donation Account Request Message\n\n";
	
	public SupportBankRequestDefine() {}
	private SupportBankRequestDefine(HttpRequestModel request, DispatcherModel model) {
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
		// 케릭터 검증
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_4_JSON);
		}
		// TODO 인게임 캐릭터 검증
		L1PcInstance pc		= getIngamePlayer();
		if (pc == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_5_JSON);
		}
		
		// 요청 정보 DB 적재
		SupportDAO dao = SupportDAO.getInstance();
		SupportBankRequestVO vo = new SupportBankRequestVO(dao.get_req_id(), 
				pc.getAccountName(), 
				pc.getName(), 
				new Timestamp(System.currentTimeMillis()), 
				null, 
				null);
		
		boolean result = dao.insertRequest(vo);
		if (!result) {
			return create_response_json(HttpResponseStatus.OK, CODE_0_JSON);
		}
		
		if (Config.WEB.TELEGRAM_ACTIVE) {
			// 텔레그램 메세지 전송
			StringBuilder sb	= new StringBuilder();
			//sb.append(HEAD_MSG).append("요청 계정:\t").append(pc.getAccountName());
			//sb.append("\n요청 케릭터:\t").append(pc.getName());
			sb.append(HEAD_MSG).append("Requested Account:\t").append(pc.getAccountName());
			sb.append("\nRequested Character:\t").append(pc.getName());				
			TelegramTransfer.excute(sb.toString());
		}
		return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new SupportBankRequestDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

