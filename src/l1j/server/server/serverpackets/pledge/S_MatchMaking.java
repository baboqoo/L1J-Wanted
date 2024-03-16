package l1j.server.server.serverpackets.pledge;

import java.util.ArrayList;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanMatching;
import l1j.server.server.model.L1ClanMatching.ClanMatchingList;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_MatchMaking extends ServerBasePacket {
	private static final String S_MATCH_MAKING = "[C] S_MatchMaking";

	/**
	 * type
	 * 0: 등록, 수정				' 완료 '
	 * 1: 등록취소, 군주에게만
	 * 2: 추천혈맹, 새로고침		' 완료 ' 
	 * 3: 신청목록, 새로고침
	 * 4: 요청목록, 새로고침
	 * 5: 신청하기. 8c bb 84 10
	 * 6: 신청취소.
	 */
	public S_MatchMaking(L1PcInstance pc, int type, int objid, String text1, int htype) {
		L1Clan clan = null;
		L1ClanMatching cml = L1ClanMatching.getInstance();
		String clanname = null;
		String text = null;

		writeC(Opcodes.S_MATCH_MAKING);
		writeC(type);
		if (type == 2) { // 추천혈맹
			ArrayList<ClanMatchingList> _list = new ArrayList<ClanMatchingList>();
			String result = null;
			for (int i=0; i<cml.getMatchingList().size(); i++) {
				result = cml.getMatchingList().get(i)._clanname;
				if (!pc.getCMAList().contains(result)){
					_list.add(cml.getMatchingList().get(i));
				}
			}
			int type2 = 0;
			int size = _list.size();
			writeC(0x00);
			writeC(size); // 갯수.
			for (int i=0; i<size; i++) {
				clanname = _list.get(i)._clanname;
				text = _list.get(i)._text;
				type2 = _list.get(i)._type;
				clan = L1World.getInstance().getClan(clanname);
				writeD(clan.getClanId()); // 혈마크
				writeS(clan.getClanName()); // 혈맹 이름.
				writeS(clan.getLeaderName()); // 군주이름
				writeD(clan.getOnlineMaxUser()); // 혈맹 규모 : 주간 최대 접속자 수 
				
				writeC(type2); // 0: 사냥, 1: 전투, 2: 친목

				if (clan.getHouseId()!=0) writeC(0x01); // 아지트 0: X , 1: O
				else writeC(0x00);

				boolean inWar = false;
				List<L1War> warList = L1World.getInstance().getWarList(); // 전쟁 리스트를 취득
				for (L1War war : warList) {
					if (war.CheckClanInWar(clanname)) { // 자크란이 이미 전쟁중
						inWar = true;
						break;
					}
				}

				if (inWar) writeC(0x01); // 전쟁 상태	0: X , 1: O
				else writeC(0x00);
				writeC(0x00); // 고정값.
				writeS(text);// 소개멘트.
				writeD(clan.getClanId()); // 혈맹 objid
			}
			_list.clear();
			_list = null;
		} else if (type == 3) { // 신청목록
			int size = pc.getCMAList().size();
			int type2 = 0;
			writeC(0x00);
			writeC(size); // 갯수.
			
			for (int i=0; i<size; i++) {
				clanname = pc.getCMAList().get(i);
				text = cml.getClanMatchingList(clanname)._text;
				type2 = cml.getClanMatchingList(clanname)._type;
				clan = L1World.getInstance().getClan(clanname);
				writeD(clan.getClanId()); // 삭제 누를때 뜨는 obj값
				writeC(0x00);
				writeD(clan.getClanId()); // 혈마크.
				writeS(clan.getClanName()); // 혈맹 이름.
				writeS(clan.getLeaderName()); // 군주이름
				writeD(clan.getOnlineMaxUser());// 혈맹 규모 : 주간 최대 접속자 수 
				writeC(type2); // 0: 사냥, 1: 전투, 2: 친목

				if (clan.getHouseId()!=0) writeC(0x01); // 아지트 0: X , 1: O
				else writeC(0x00);

				boolean inWar = false;
				List<L1War> warList = L1World.getInstance().getWarList(); // 전쟁 리스트를 취득
				for (L1War war : warList) {
					if (war.CheckClanInWar(clanname)) { // 자크란이 이미 전쟁중
						inWar = true;
						break;
					}
				}

				if (inWar) writeC(0x01); // 전쟁 상태	0: X , 1: O
				else writeC(0x00);
				writeC(0x00); // 고정값.
				writeS(text);// 소개멘트.
				writeD(clan.getClanId()); // 혈맹 objid
			}
		} else if (type == 4) { // 요청목록
			
			if (!cml.isClanMatchingList(pc.getClanName())) {
				writeC(0x82); // 요청 목록이 없을땐 이것만 날린다.
			} else {
				int size = pc.getCMAList().size();
				String username = null;
				writeC(0x00);
				writeC(0x02);
				writeC(0x00);// 고정
				writeC(size); // size
				L1PcInstance user = null;
				for (int i=0; i<size; i++) {
					username = pc.getCMAList().get(i);
					user = L1World.getInstance().getPlayer(username);
					if (user == null) {
						try {
							user = CharacterTable.getInstance().restoreCharacter(username);
							if (user == null) {
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					writeD(user.getId()); // 신청자의 objectid
					writeC(0x00);
					writeC(user.getOnlineStatus()); // 0x01:접속,  0x00:비접속
					writeS(username); // 신청자의 이름.
					writeC(user.getType()); // 캐릭터 클래스
					writeH(user.getAlignment()); // 성향
					writeC(user.getLevel()); // 레벨
					writeC(0x01); // 이름앞에 나오는 풀잎의 변경
				}
			}
		} else if (type == 5 || type == 6) {
			writeC(0x00);
			writeD(objid);
			writeC(htype);
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_MATCH_MAKING;
	}
}

