package l1j.server.web.http;

import com.google.gson.Gson;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.util.CharsetUtil;
import l1j.server.web.dispatcher.DispatcherModel;

/**
 * JSON 응답 클래스
 * @author LinOffice
 */
public abstract class HttpJsonModel extends HttpResponseModel {
	protected static final String JSON_CONTENT_TYPE				= "application/json; charset=utf-8";
	protected static final HttpDataFactory FILE_DATA_FACTORY	= new DefaultHttpDataFactory(true); // Disk if size exceed
	static {
		DiskFileUpload.deleteOnExitTemporaryFile	= true;
		DiskFileUpload.baseDirectory				= null;
		DiskAttribute.deleteOnExitTemporaryFile		= true;
		DiskAttribute.baseDirectory					= null;
	}
	protected static final String TRUE_JSON						= new Gson().toJson(true);
	protected static final String FALSE_JSON					= new Gson().toJson(false);
	protected static final String CODE_MINUS_JSON				= new Gson().toJson(-1);
	protected static final String CODE_0_JSON					= new Gson().toJson(0);
	protected static final String CODE_1_JSON					= new Gson().toJson(1);
	protected static final String CODE_2_JSON					= new Gson().toJson(2);
	protected static final String CODE_3_JSON					= new Gson().toJson(3);
	protected static final String CODE_4_JSON					= new Gson().toJson(4);
	protected static final String CODE_5_JSON					= new Gson().toJson(5);
	protected static final String CODE_6_JSON					= new Gson().toJson(6);
	protected static final String CODE_7_JSON					= new Gson().toJson(7);
	protected static final String CODE_8_JSON					= new Gson().toJson(8);
	protected static final String CODE_9_JSON					= new Gson().toJson(9);
	protected static final String CODE_10_JSON					= new Gson().toJson(10);
	protected static final String CODE_11_JSON					= new Gson().toJson(11);
	protected static final String CODE_12_JSON					= new Gson().toJson(12);
	protected static final String CODE_13_JSON					= new Gson().toJson(13);
	protected static final String CODE_14_JSON					= new Gson().toJson(14);
	protected static final String CODE_15_JSON					= new Gson().toJson(15);
	
	public HttpJsonModel() {}

	protected HttpJsonModel(HttpRequestModel request, DispatcherModel dispatcher) {
		super(request, dispatcher);
	}

	/**
	 * 파일 업로드 요청 인스턴스 생성
	 * 파일 msg를 복호화한다.
	 * @param request
	 * @param msg
	 * @param model
	 * @return HttpJsonModel
	 * @throws Exception
	 */
	public abstract HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception;
	
	/**
	 * JSON 응답 생성
	 * @param status
	 * @param jsonString
	 * @return HttpResponse
	 */
	public HttpResponse create_response_json(HttpResponseStatus status, String jsonString) {
		ByteBuf response_buff = Unpooled.wrappedBuffer(jsonString.getBytes(CharsetUtil.UTF_8));
		FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), status, response_buff);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, JSON_CONTENT_TYPE);
		return response;
	}
}

