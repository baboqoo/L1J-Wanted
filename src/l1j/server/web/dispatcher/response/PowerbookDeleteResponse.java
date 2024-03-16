package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpResponse;

import java.io.File;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.PowerbookType;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.dispatcher.response.powerbook.L1Info;
import l1j.server.web.dispatcher.response.powerbook.L1InfoDAO;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class PowerbookDeleteResponse extends HttpResponseModel {
	public PowerbookDeleteResponse() {}
	private PowerbookDeleteResponse(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		int id				= Integer.parseInt(request.read_parameters_at_once("id"));
		L1InfoDAO dao 		= L1InfoDAO.getInstance();
		L1Info info			= dao.getInfoGuide(PowerbookType.GUIDE.toInt(), id);
		if (info != null) {
			String mainImg	= (String)info.getInfo().get("mainImg");
			if (!StringUtil.isNullOrEmpty(mainImg)) {
				mainImg		= mainImg.substring(mainImg.indexOf(FileExcutor.UPLOAD_STRING)).replace(FileExcutor.UPLOAD_STRING, FileExcutor.UPLOAD_PATH_STRING);
				File file	= new File(mainImg);
				if (file.exists()) {
					file.delete();
				}
			}
			dao.guideDelete(id);
		}
		return sendRedirect("/powerbook");
	}

	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new PowerbookDeleteResponse(request, model);
	}
}

