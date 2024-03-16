package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.report.ReportDAO;
import l1j.server.web.dispatcher.response.report.ReportVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class GMReportDeleteDefine extends HttpJsonModel {
	public GMReportDeleteDefine() {}
	private GMReportDeleteDefine(HttpRequestModel request, DispatcherModel model) {
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
		
		int report_id			= Integer.parseInt(request.read_post("report_id"));
		ReportDAO dao			= ReportDAO.getInstance();
		ReportVO vo				= dao.getReport(report_id);
		if (vo == null) {
			return create_response_json(HttpResponseStatus.OK, CODE_2_JSON);
		}
		
		if (dao.delete(vo)) {
			return create_response_json(HttpResponseStatus.OK, CODE_1_JSON);
		}
		return create_response_json(HttpResponseStatus.OK, CODE_0_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GMReportDeleteDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

