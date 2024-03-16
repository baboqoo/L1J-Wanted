package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1ClanRanking.RankData;

public class S_RankingClan extends ServerBasePacket {
	private static final String S_RANKING_CLAN = "[S] S_RankingClan";
	
	public static final int CLAN_RANK = 0x0093;

	private byte[] _byte = null;
	
	/*
	 * @param Serverbasepacket 
	 * S_RankingClan.java make by 맞고	
	 * 수정보완 : 프리섭인 관계로 해산 혈맹은 추가하지 않음. 
	 */
	public S_RankingClan(RankData[] datas) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CLAN_RANK);
		
		for (int i = 0; i < datas.length; i++) {
			writeC(0x0a);// 총길이
			int currentRank = datas[i].getCurrentR();
			int oldRank	= datas[i].getPastR();
			int clanId	= datas[i].getClanid();
			String name = datas[i].getClanName();
			int time	= datas[i].getComTime();
			int data	= (int) (datas[i].getDate().getTime() / 1000L);			
			int size	= 9 + bitLengh(currentRank) + bitLengh(oldRank)+ bitLengh(clanId) + name.getBytes().length + bitLengh(time) + bitLengh(data) ;			
			writeC(size);
			
			writeC(0x08);// 현재랭킹
			writeBit(currentRank);
			
			writeC(0x10);// 지난랭킹
			writeBit(oldRank);
			
			writeC(0x18);// 혈맹아이디
			writeBit(clanId);
			
			writeC(0x22);// 혈맹이름
			writeS2(name);
			
			writeC(0x28);// 0:활동 1:해산
			writeC(0);
			
			writeC(0x30);// 클리어 시간(단위:초)
			writeBit(time);
			
			writeC(0x38);// 클리어 날짜
			writeBit(data);		
		}
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_RANKING_CLAN;
	}
}
