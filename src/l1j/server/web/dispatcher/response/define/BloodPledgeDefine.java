package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.world.BloodPledgeDAO;
import l1j.server.web.dispatcher.response.world.BloodPledgeVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

public class BloodPledgeDefine extends HttpJsonModel {
	public BloodPledgeDefine() {}
	private BloodPledgeDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}
	
	private static final String[] INGMAE_JOIN_BTN_TAG = {
		"<a class=\"button-pledge-join0\" href=\"javascript:L1.ReqJoinPledge('%s',0);\"><i class=\"icon-pledge\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 9 9\"><path fill=\"#A2A8B2\" fill-rule=\"evenodd\" d=\"M4.091 0h1v9h-1z\"></path><path fill=\"#A2A8B2\" fill-rule=\"evenodd\" d=\"M9 4.091v1H0v-1z\"></path></svg></i> 즉시 가입</a>",
		"<a class=\"button-pledge-join1\" href=\"javascript:L1.ReqJoinPledge('%s',1);\"><i class=\"icon-pledge\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 11 7\"><path fill=\"none\" stroke=\"#A2A8B2\" d=\"M1 2.68L4.25 7 10 0\"></path></svg></i> 가입 신청</a>",
		"<a class=\"button-pledge-join2\" href=\"javascript:L1.ReqJoinPledge('%s',2);\"><i class=\"icon-pledge\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 9 11\"><g fill=\"none\" fill-rule=\"evenodd\" stroke=\"#A2A8B2\" transform=\"translate(0 1)\"><rect width=\"8\" height=\"6\" x=\".5\" y=\"3.5\" rx=\"1\"></rect><path d=\"M2 3V1.755C2.09.585 2.901 0 4.433 0 6.73 0 7 .962 7 1.755V3\"></path></g>&gt;</svg></i> 암호 가입</a>",
		"<a class=\"button-pledge-cancel\" href=\"javascript:L1.CancelJoinRequest(%d);\"><i class=\"icon-pledge\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 9 7\"><path fill=\"#A2A8B2\" fill-rule=\"evenodd\" d=\"M1 0l7 7m0-7L1 7\"></path></svg></i> 신청 취소</a>"
	};
	private static final PointComparator POINT_COMP			= new PointComparator();
	private static final BirthdayComparator BIRTHDAY_COMP	= new BirthdayComparator();

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		String query		= post.get("query");
		int sort			= Integer.parseInt(post.get("sortType"));
		
		List<BloodPledgeVO> list;
		if (StringUtil.isNullOrEmpty(query)) {
			list = BloodPledgeDAO.getList();
		} else {
			list = BloodPledgeDAO.getList(query);
		}
		if (sort != 0) {
			list = sortList(list, sort);
		}
		setPledgeJoinType(list);
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(list));
	}
	
	void setPledgeJoinType(List<BloodPledgeVO> list) {
		if (list == null) {
			return;
		}
		boolean isIngame = account != null && account.isIngame() && player != null;
		for (BloodPledgeVO vo : list) {
			if (!isIngame) {
				vo.setJoinBtn(StringUtil.EmptyString);
				continue;
			}
			vo.setJoinBtn(getIngameJoinBtn(vo));
		}
	}
	
	/**
	 * 인게임 내장함수 태그
	 * @param vo
	 * @return String
	 */
	String getIngameJoinBtn(BloodPledgeVO vo) {
		L1Clan clan = L1World.getInstance().getClan(vo.getPledgeId());
		if (clan.isJoinningList(player.getObjId())) {
			return String.format(INGMAE_JOIN_BTN_TAG[3], clan.getClanId());
		}
		return String.format(INGMAE_JOIN_BTN_TAG[clan.getJoinType().toInt()], clan.getClanName());
	}
	
	List<BloodPledgeVO> sortList(List<BloodPledgeVO> clanList, int sortType){
		List<BloodPledgeVO> list;
		switch(sortType) {
		case 3:// 공성 탑10
			list = new ArrayList<BloodPledgeVO>(clanList);
			Collections.sort(list, POINT_COMP.reversed());
			return list;
		case 5:// 전통
			list = new ArrayList<BloodPledgeVO>(clanList);
			Collections.sort(list, BIRTHDAY_COMP);
			return list;
		case 6:// 아지트
			list = new ArrayList<BloodPledgeVO>();
			for (BloodPledgeVO vo : clanList) {
				if (!StringUtil.isNullOrEmpty(vo.getHasCastle()) || (!vo.getHasHouse().equals(StringUtil.MinusString) && !StringUtil.isNullOrEmpty(vo.getHasHouse()))) {
					list.add(vo);
				}
			}
			return list;
		default:
			return clanList;
		}
	}
	
	static class PointComparator implements Comparator<BloodPledgeVO> {
		@Override
		public int compare(BloodPledgeVO o1, BloodPledgeVO o2) {
			if (o1.getExp() > o2.getExp()) {
				return 1;
			}
			if (o1.getExp() < o2.getExp()) {
				return -1;
			}
			return 0;
		}
	}

	static class BirthdayComparator implements Comparator<BloodPledgeVO> {
		@Override
		public int compare(BloodPledgeVO o1, BloodPledgeVO o2) {
			if (o1.getBirthday().getTime() > o2.getBirthday().getTime()) {
				return 1;
			}
			if (o1.getBirthday().getTime() < o2.getBirthday().getTime()) {
				return -1;
			}
			return 0;
		}
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new BloodPledgeDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

