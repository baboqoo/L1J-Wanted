package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.File;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.dispatcher.response.promotion.PromotionDAO;
import l1j.server.web.dispatcher.response.promotion.PromotionVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class PromotionDeleteDefine extends HttpJsonModel {
	public PromotionDeleteDefine() {}
	private PromotionDeleteDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		boolean result		= false;
		int id				= Integer.parseInt(request.read_post("id"));
		PromotionDAO dao	= PromotionDAO.getInstance();
		PromotionVO vo		= PromotionDAO.getPromotion(id);
		if (vo != null) {
			String img		= vo.getPromotionImg();
			String subImg	= vo.getListallImg();
			File file		= null;
			if (!StringUtil.isNullOrEmpty(img)) {
				img			= img.substring(img.indexOf(FileExcutor.UPLOAD_STRING)).replace(FileExcutor.UPLOAD_STRING, FileExcutor.UPLOAD_PATH_STRING);
				file		= new File(img);
				if (file.exists()) {
					file.delete();
				}
			}
			if (!StringUtil.isNullOrEmpty(subImg)) {
				subImg		= subImg.substring(subImg.indexOf(FileExcutor.UPLOAD_STRING)).replace(FileExcutor.UPLOAD_STRING, FileExcutor.UPLOAD_PATH_STRING);
				file		= new File(subImg);
				if (file.exists()) {
					file.delete();
				}
			}
			result = dao.deletePromotion(vo);
		}
		return create_response_json(HttpResponseStatus.OK, result ? TRUE_JSON : FALSE_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new PromotionDeleteDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

