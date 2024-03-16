package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import l1j.server.server.GameClient;
import l1j.server.server.controller.LoginController;
import l1j.server.server.monitor.Logger.ConnectType;
import l1j.server.server.monitor.LoggerInstance;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;
import l1j.server.web.http.connector.HttpProcessMergeResult;
import l1j.server.web.http.connector.HttpProcessMergeValidation;

import com.google.gson.Gson;

public class ProcessMergeDefine extends HttpJsonModel {
	private String _account;
	private String _process;
	
	public ProcessMergeDefine() {}
	private ProcessMergeDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
		parseParameters();
	}
	
	private void parseParameters(){
		_account = request.read_parameters_at_once("account");
		_process = request.read_parameters_at_once("process");
	}
	
	private boolean validation(){
		return !StringUtil.isNullOrEmpty(_account) && !StringUtil.isNullOrEmpty(_process);
	}
	
	private HttpResponse result(HttpProcessMergeValidation validation){
		HttpProcessMergeResult result = new HttpProcessMergeResult();
		result.result_code = validation.getMessage();
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(result));
	}

	@Override
	public HttpResponse get_response() throws Exception {
		if (!validation()) {
			LoggerInstance.getInstance().addConnector(ConnectType.PROCESS, _account, null, HttpProcessMergeValidation.FAIL_NOT_FOUND_PARAMETERS.getMessage());
			return result(HttpProcessMergeValidation.FAIL_NOT_FOUND_PARAMETERS);
		}
		GameClient client = LoginController.getInstance().getClientByAccount(_account);
		if (client == null) {
			LoggerInstance.getInstance().addConnector(ConnectType.PROCESS, _account, null, HttpProcessMergeValidation.FAIL_NOT_FOUND_CLIENT.getMessage());
			return result(HttpProcessMergeValidation.FAIL_NOT_FOUND_CLIENT);
		}
		if (client.getLoginSession() == null) {
			LoggerInstance.getInstance().addConnector(ConnectType.PROCESS, _account, null, HttpProcessMergeValidation.FAIL.getMessage());
			return result(HttpProcessMergeValidation.FAIL);
		}
		client.getLoginSession().setProcess(_process);// merge
		return result(HttpProcessMergeValidation.SUCCESS);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new ProcessMergeDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
	
}

