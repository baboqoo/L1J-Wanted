package l1j.server.web.dispatcher.response.define;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.report.ReportDAO;
import l1j.server.web.dispatcher.response.report.ReportType;
import l1j.server.web.dispatcher.response.report.ReportVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class ReportInsertDefine extends HttpJsonModel {
	public ReportInsertDefine() {}
	private ReportInsertDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		int status = 0;
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, new Gson().toJson(getResultCallBack(status)));
		}
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, new Gson().toJson(getResultCallBack(status)));
		}
		Map<String, String> post = request.get_post_datas();
		
		String target	= post.get("targetName");
		ReportType type	= ReportType.fromInt(Integer.parseInt(post.get("type")));
		String log 		= post.get("log");
		if (target.equals(player.getName())) {
			status = 3;
		} else {
			ReportDAO dao = ReportDAO.getInstance();
			LinkedList<ReportVO> reportList = dao.getData();
			if (reportList != null && !reportList.isEmpty()) {
				for (ReportVO report : reportList) {
					if (report.getName().equals(player.getName()) && report.getTargetName().equals(target) && report.getLog().equals(log)) {
						status = 2;
						break;
					}
				}
			}
			if (status != 2) {
				ReportVO report = new ReportVO(dao.nextId(), player.getName(), target, type, log, new Timestamp(System.currentTimeMillis()), dao.get_object(log));
				status = dao.insert(report);
			}
		}
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(getResultCallBack(status)));
	}
	
	static final String[] RESULT_CALLBACK = {
		/*"신고에 실패하였습니다.",
		"신고가 정상적으로 완료되었습니다.",
		"이미 신고가 완료된 대상입니다.",
		"자기 자신은 신고할 수 없습니다."*/
		"Failed to report.",
		"Report completed successfully.",
		"Report has already been completed for this target.",
		"You cannot report yourself."		
	};
	
	private String getResultCallBack(int status) {
		return RESULT_CALLBACK[status];
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new ReportInsertDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

