package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.sql.Timestamp;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.engine.EngineLogDAO;
import l1j.server.web.dispatcher.response.engine.EngineLogVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.connector.HttpEngineLogResult;
import l1j.server.web.http.connector.HttpEngineLogValidation;

import com.google.gson.Gson;

public class EngineLogDefine extends HttpJsonModel {
	private String _account;
	private String _engine;
	
	public EngineLogDefine() {}
	private EngineLogDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
		parseParameters();
	}
	
	private void parseParameters(){
		_account	= request.read_parameters_at_once("account");
		_engine		= request.read_parameters_at_once("engine");
	}
	
	private HttpResponse result(HttpEngineLogValidation validation){
		HttpEngineLogResult result = new HttpEngineLogResult();
		result.result_code = validation.getMessage();
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(result));
	}
	
	private boolean validateParameters(){
		return !StringUtil.isNullOrEmpty(_account) && !StringUtil.isNullOrEmpty(_engine);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (!validateParameters()) {
			return result(HttpEngineLogValidation.FAIL);
		}
		EngineLogVO vo = new EngineLogVO(_account, _engine, new Timestamp(System.currentTimeMillis()));
		if (EngineLogDAO.getInstance().insert(vo)) {
			//String log = String.format("불허가 엔진 사용 계정이 탐지 되었습니다. 계정[%s] 엔진[%s]", _account, _engine);
			String log = String.format("Unauthorized engine usage detected. Account[%s] Engine[%s]", _account, _engine);
			S_SystemMessage message = new S_SystemMessage(log);
			for (L1PcInstance pc : L1World.getInstance().getAllGms()) {
				pc.sendPackets(message);
			}
			message.clear();
			message = null;
			System.out.println(log);
			return result(HttpEngineLogValidation.SUCCESS);
		}
		return result(HttpEngineLogValidation.CLOSE);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new EngineLogDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
	
}

