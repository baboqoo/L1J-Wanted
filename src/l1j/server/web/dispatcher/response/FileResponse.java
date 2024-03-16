package l1j.server.web.dispatcher.response;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.HttpResponseModel;

public class FileResponse extends HttpResponseModel {
	private static final String CONTEXT_PATH		= "./appcenter%s";
	private static final String EMPTY_PATH			= "./appcenter/";
	private static final String MAX_AGE				= "max-age=604800";
	private static final String FAIL_LOG_FORMAT		= "load failure file. %s";
	
	public FileResponse(HttpRequestModel request) {
		super(request, null);
	}
	
	@Override
	public HttpResponse get_response() throws Exception {
		String path = String.format(CONTEXT_PATH, request.get_request_uri());
		if (path.equals(EMPTY_PATH)) {
			return create_empty(HttpResponseStatus.NOT_FOUND);
		}
		
		byte[] buff = load_file(path);
		if (buff == null) {
			append_log(String.format(FAIL_LOG_FORMAT, path));
			return create_empty(HttpResponseStatus.NOT_FOUND);
		}
		
		HttpResponse response = create_response(HttpResponseStatus.OK, buff);
		response.headers().set(HttpHeaderNames.CACHE_CONTROL, MAX_AGE);
		return response;
	}
	
	@Override
	public HttpResponseModel copyInstance(HttpRequestModel request, DispatcherModel dispatcher) throws Exception {
		return null;
	}
}

