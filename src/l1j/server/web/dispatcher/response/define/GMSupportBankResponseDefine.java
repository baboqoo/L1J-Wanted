package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.sql.Timestamp;

import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.message.S_MsgAnnounce;
import l1j.server.server.utils.ColorUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.support.SupportBankRequestVO;
import l1j.server.web.dispatcher.response.support.SupportDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class GMSupportBankResponseDefine extends HttpJsonModel {
	public GMSupportBankResponseDefine() {}
	private GMSupportBankResponseDefine(HttpRequestModel request, DispatcherModel model) {
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
		
		int request_id				= Integer.parseInt(request.read_post("request_id"));
		String request_content		= request.read_post("request_content");
		
		
		SupportDAO dao				= SupportDAO.getInstance();
		SupportBankRequestVO vo		= SupportDAO.getSupportRequest(request_id);
		if (vo == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
		}
		
		if (!StringUtil.isNullOrEmpty(vo.getResponse())) {
			return create_response_json(HttpResponseStatus.OK, CODE_3_JSON);
		}
		
		String target_char_name		= vo.getCharacter_name();
		L1PcInstance pc				= L1World.getInstance().getPlayer(target_char_name);
		if (pc == null || pc.getOnlineStatus() == 0) {
			return create_response_json(HttpResponseStatus.OK, CODE_4_JSON);
		}
		
		Timestamp dTime = new Timestamp(System.currentTimeMillis());
		vo.setResponse(request_content);
		vo.setResponse_date(dTime);
		
		boolean result				= dao.updateRequest(vo);
		if (!result) {
			return create_response_json(HttpResponseStatus.OK, CODE_5_JSON);
		}
		
		int id = LetterTable.getInstance().writeLetter(949, dTime, player.getName(), target_char_name, 0, "[Sponsorship Info]", request_content);
		if (id < 0) {
			return create_response_json(HttpResponseStatus.OK, CODE_6_JSON);
		}
		pc.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id, S_LetterList.TYPE_RECEIVE, player.getName(), "[Sponsorship Info]"), true); // 받는사람
		String message = String.format("Hello '%s'. The sponsorship account information requested by the customer has been sent by letter. Please check your mailbox. thank you", pc.getName());
		pc.sendPackets(new S_MsgAnnounce(message, ColorUtil.getWhiteRgbBytes()), true);
		return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GMSupportBankResponseDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

