package l1j.server.web.dispatcher.response.define;

import java.sql.Timestamp;
import java.util.Map;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MsgAnnounce;
import l1j.server.server.utils.ColorUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.customer.CustomerDAO;
import l1j.server.web.dispatcher.response.customer.CustomerVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class CustomerCommentInsertDefine extends HttpJsonModel {
	public CustomerCommentInsertDefine() {}
	private CustomerCommentInsertDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		int type			= Integer.parseInt(post.get("type"));
		int rownum			= Integer.parseInt(post.get("rownum"));
		String comment		= post.get("comment");
		CustomerDAO custom	= CustomerDAO.getInstance();
		CustomerVO vo		= custom.getCustomerInfo(rownum, type);
		// This is an status that is stored in DB. If we want to translate it we have to take care, modify DB, a few units...
		vo.setStatus("Answered");
		vo.setComment(comment);
		if (vo.getCommentDate() != null) {
			vo.getCommentDate().setTime(System.currentTimeMillis());
		} else {
			vo.setCommentDate(new Timestamp(System.currentTimeMillis()));
		}
		boolean result = custom.updateComment(vo);
		if (result) {
			send_comment_noti(vo);
		}
		return create_response_json(HttpResponseStatus.OK, result ? TRUE_JSON : FALSE_JSON);
	}
	
	void send_comment_noti(CustomerVO vo) {
		String account_name = vo.getLogin();
		for (L1PcInstance user : L1World.getInstance().getAllPlayers()) {
			if (user == null || user.getNetConnection() == null) {
				continue;
			}
			if (!user.getAccountName().equals(account_name)) {
				continue;
			}
			String message = String.format(
				"Hello '%s'. An additional response from the administrator has been added to your %s '%s' submitted to the customer center. Please check it in the customer center. Thank you.", 
				user.getName(), vo.getType() == 1 ? "Inquiry" : "Report", vo.getTitle());
			user.sendPackets(new S_MsgAnnounce(message, ColorUtil.getWhiteRgbBytes()), true);
			break;
		}
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new CustomerCommentInsertDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

