package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Map;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.dispatcher.response.editor.FileExcutor.TempStatus;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class FileInsertCancelDefine extends HttpJsonModel {
	public FileInsertCancelDefine() {}
	private FileInsertCancelDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		String tempList		= post.get("tempList");
		String oriList		= post.get("oriList");
		FileExcutor.excute(tempList, null, TempStatus.INSERT_CANCEL);
		FileExcutor.delete(oriList);
		return create_response_json(HttpResponseStatus.OK, TRUE_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new FileInsertCancelDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

