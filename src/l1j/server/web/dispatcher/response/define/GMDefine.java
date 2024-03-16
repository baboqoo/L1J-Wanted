package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ThreadPoolExecutor;

import l1j.server.Config;
import l1j.server.common.data.ChatType;
import l1j.server.server.GMCommands;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.PerformAdapter;
import l1j.server.server.controller.LoginController;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_MsgAnnounce;
import l1j.server.server.utils.ColorUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.account.AccountDAO;
import l1j.server.web.dispatcher.response.gm.GMServerModel;
import l1j.server.web.dispatcher.response.gm.GM_TYPE_FLAG;
import l1j.server.web.dispatcher.response.report.ReportDAO;
import l1j.server.web.dispatcher.response.support.SupportDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

public class GMDefine extends HttpJsonModel {
	public GMDefine() {}
	private GMDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}
	
	static final int QUEUE_MAX_SIZE = 100;
	static final Deque<String> CHAT_QUEUE = new ConcurrentLinkedDeque<String>();
	
	@Override
	public HttpResponse get_response() throws Exception {
		if (account == null) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		if (player == null) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		GM_TYPE_FLAG flag = GM_TYPE_FLAG.fromString(request.read_post("typeFlag"));
		if (flag == null) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		switch (flag) {
		case SERVER:
			return create_response_json(HttpResponseStatus.OK, new Gson().toJson(get_server()));
		case USER:
			return create_response_json(HttpResponseStatus.OK, new Gson().toJson(get_user()));
		case SUPPORT:
			return create_response_json(HttpResponseStatus.OK, new Gson().toJson(get_support()));
		case REPORT:
			return create_response_json(HttpResponseStatus.OK, new Gson().toJson(get_report()));
		case CHAT:
			return get_chat();
		case CHAT_COMMAND:
			return get_chat_command();
		}
		return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
	}
	
	static GMServerModel server_model;
	void updateExecutor(ThreadPoolExecutor executor, GMServerModel.ResourceThreadModel model){
		model.active = executor.getActiveCount();
		model.poolSize = executor.getPoolSize();
		model.queue = executor.getQueue().size();
		model.task = executor.getTaskCount();
		model.complete = executor.getCompletedTaskCount();
	}
	
	Object get_server() {
		if (server_model == null) {
			server_model = new GMServerModel();
		}
		server_model.cpu_usage = PerformAdapter.CPU_USAGE;
		server_model.thread_count = PerformAdapter.THREAD_USAGE;
		
		Runtime r = Runtime.getRuntime();
		server_model.total_memory = (int) (r.totalMemory() / 1024);
		server_model.free_memory = (int) (r.freeMemory() / 1024);
		server_model.used_memory = server_model.total_memory - server_model.free_memory;
		server_model.max_memory = (int) (r.maxMemory() / 1024);
		updateExecutor(GeneralThreadPool.getInstance().executor(), server_model.executor);
		updateExecutor(GeneralThreadPool.getInstance().scheduler(), server_model.scheduler);
		updateExecutor(GeneralThreadPool.getInstance().pcScheduler(), server_model.pcScheduler);
		server_model.clients = LoginController.getInstance().getOnlinePlayerCount();
		server_model.clientsLimit = Config.SERVER.MAX_ONLINE_USERS;
		server_model.npcs = L1World.getInstance().get_npc_size();
		server_model.items = L1World.getInstance().get_item_size();
		return server_model;
	}
	
	Object get_user() {
		return AccountDAO.getInstance().getAllChracter();
	}
	
	Object get_support() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("DEFAULT", SupportDAO.getData());
		result.put("REQUEST", SupportDAO.getRequestData());
		return result;
	}
	
	Object get_report() {
		return ReportDAO.getInstance().getData();
	}
	
	public static void add_chat(ChatType type, L1PcInstance user, L1PcInstance wisper, String message) {
		if (CHAT_QUEUE.size() >= QUEUE_MAX_SIZE) {
			max_poll();
		}
		String result;
		switch (type) {
		case CHAT_WHISPER:
			//result = String.format("[귓속말] (%s) -> (%s) : %s", user.getName(), wisper.getName(), message);
			result = String.format("[Whisper] (%s) -> (%s): %s", user.getName(), wisper.getName(), message);
			break;
		case CHAT_WORLD:
			//result = String.format("[전체] (%s) : %s", user.getName(), message);
			result = String.format("[All] (%s): %s", user.getName(), message);
			break;
		case CHAT_TRADE:
			//result = String.format("[장사] (%s) : %s", user.getName(), message);
			result = String.format("[Trade] (%s): %s", user.getName(), message);
			break;
		case CHAT_PLEDGE:
		case CHAT_PLEDGE_PRINCE:
		case CHAT_PLEDGE_NOTICE:
		case CHAT_PLEDGE_ALLIANCE:
			//result = String.format("['%s'혈맹] (%s) : %s", user.getClanName(), user.getName(), message);
			result = String.format("['%s' Pledge] (%s): %s", user.getClanName(), user.getName(), message);
			break;
		case CHAT_HUNT_PARTY:
		case CHAT_CHAT_PARTY:
			//result = String.format("[파티] (%s) : %s", user.getName(), message);
			result = String.format("[Party] (%s): %s", user.getName(), message);
			break;
		default:
			//result = String.format("[일반] (%s) : %s", user.getName(), message);
			result = String.format("[General] (%s): %s", user.getName(), message);
			break;
		}
		CHAT_QUEUE.offer(result);
	}
	
	public static void max_poll() {
		for (int i=0; i<10; i++) {
			CHAT_QUEUE.poll();
		}
	}
	
	HttpResponse get_chat() {
		if (CHAT_QUEUE.isEmpty()) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		String chat_json = new Gson().toJson(CHAT_QUEUE);
		CHAT_QUEUE.clear();
		return create_response_json(HttpResponseStatus.OK, chat_json);
	}
	
	private static L1PcInstance gm = null;
	HttpResponse get_chat_command() {
		String param = request.read_post("command");
		if (param.startsWith(StringUtil.PeriodString)) {// 명령어
			String command = param.substring(1);
			if (gm == null) {
				gm = new L1PcInstance();
				//gm.setName("[웹관리자]");
				gm.setName("[Web Administrator]");
				gm.setAccessLevel((short)Config.ALT.GMCODE);
			}
			boolean result = GMCommands.getInstance().handleCommands(gm, command);
			//CHAT_QUEUE.offer(String.format("[관리자 명령어] (%s) : %s --> 결과[%b]", player.getName(), param, result));
			CHAT_QUEUE.offer(String.format("[Administrator Command] (%s): %s --> Result[%b]", player.getName(), param, result));
		} else {
			L1World.getInstance().broadcastPacketToAll(new S_MsgAnnounce(param, ColorUtil.getWhiteRgbBytes()), true);
			//CHAT_QUEUE.offer(String.format("[전체] (%s) : %s", player.getName(), param));
			CHAT_QUEUE.offer(String.format("[All] (%s): %s", player.getName(), param));
		}
		String chat_json = new Gson().toJson(CHAT_QUEUE);
		CHAT_QUEUE.clear();
		return create_response_json(HttpResponseStatus.OK, chat_json);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new GMDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

