package l1j.server.server.serverpackets.pledge;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;

public class S_BloodPledgeInfo extends ServerBasePacket {
	private static final String S_BLOODPLEDGE_INFO = "[S] S_BloodPledgeInfo";
	private byte[] _byte = null;
	
	public static final int HTML_PLEDGE_ANNOUNCE			= 167;
	public static final int HTML_PLEDGE_REALEASE_ANNOUNCE	= 168;
	public static final int HTML_PLEDGE_WRITE_NOTES			= 169;
	public static final int HTML_PLEDGE_MEMBERS				= 170;
	public static final int HTML_PLEDGE_ONLINE_MEMBERS		= 171;
	public static final int PLEDGE_EMBLEM_STATUS			= 173;// 문장주시
	
	private static byte[] MEMBER_NOTE_EMPTY_BYTES = new byte[62];
	static {
		for (int i = 0; i < 62; i++) {
			MEMBER_NOTE_EMPTY_BYTES[i] = (byte)0x00;
		}
	}

	public S_BloodPledgeInfo(int pledge_id) {
		L1Clan clan = ClanTable.getInstance().getTemplate(pledge_id);
		writeC(Opcodes.S_EVENT);
		writeC(HTML_PLEDGE_ANNOUNCE);
		writeS(clan.getClanName());
		writeS(clan.getLeaderName());
        writeD(clan.getEmblemId());
        writeC(clan.getHouseId() != 0 ? 1 : 0);
        writeC(clan.getCastleId() != 0 ? 1 : 0);    
        writeC(0);
		writeD((int) (clan.getClanBirthDay().getTime() / 1000)); 
		try {
			byte[] text = new byte[478];
			Arrays.fill(text, (byte) 0);
			int i = 0;
			for (byte b : clan.getAnnouncement().replaceAll(StringUtil.MinusString, StringUtil.EmptyString).getBytes(CharsetUtil.EUC_KR_STR)) {
				text[i++] = b;
			}
			writeByte(text);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        writeH(0);
	}
	
	public S_BloodPledgeInfo(L1PcInstance pc, int page, int current_page, ArrayList<ClanMember> list) {
        writeC(Opcodes.S_EVENT);
        writeC(HTML_PLEDGE_MEMBERS);      
        writeC(page);
        writeC(current_page);
        writeC(list.size());
        for (ClanMember member : list) {
            if (member == null) {
            	continue;
            }
            try {
            	writeS(member.name);// 케릭터이름
            	writeC(member.rank.toInt());// 혈맹랭크
            	writeC(member.level);// 케릭터레벨
                if (member.notes != null) {
    				writeS(member.notes);
    				int empty_count = 61 - member.notes.getBytes().length;
    				for (int j = 0; j < empty_count; j++) {
    					writeC(0x00);
    				}
    			} else {
    				writeByte(MEMBER_NOTE_EMPTY_BYTES);
    			}
                writeD(member.memberId);// 오브젝트 아이디
                writeC(member.type);// 클래스 타입
                writeD(member.join_date);// 가입일자
                
                // 공헌도
                writeD(member.memberId == pc.getId() ? pc.getClanWeekContribution() : member.contributionWeek);// 주간 개인 공헌도
                writeD(0x00);
                writeD(member.memberId == pc.getId() ? pc.getClanContribution() : member.contribution);// 전체 공헌도
                writeD(0x00);
                
                writeC(member.online ? 1 : 0);// 온라인상태
                writeD(member.logout_date);// 로그아웃 일자
                writeC(0x00);// 모름
            } catch (Exception e) {
            }
        }
        writeH(0);
    }

	public S_BloodPledgeInfo(String name, String notes){
		writeC(Opcodes.S_EVENT);
		writeC(HTML_PLEDGE_WRITE_NOTES);
		writeS(name);

		byte[] text = new byte[62];
		Arrays.fill(text, (byte) 0);
		if (notes.length() != 0) {
			int i = 0;
			try {
				for (byte b : notes.getBytes(CharsetUtil.EUC_KR_STR)) {
					text[i++] = b;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		writeByte(text);
	}
	
	public S_BloodPledgeInfo(Object[] names) {
		writeC(Opcodes.S_EVENT);
		writeC(HTML_PLEDGE_ONLINE_MEMBERS);
		writeH(names.length);
		for (Object name : names) {
			if (name == null) {
				continue;
			}
			L1PcInstance pc = (L1PcInstance) name;
			writeS(pc.getName());
			writeC(0);
		}
	}
	
	public S_BloodPledgeInfo(String announce) {
		writeC(Opcodes.S_EVENT);
		writeC(HTML_PLEDGE_REALEASE_ANNOUNCE);
		writeS(announce);
	}
	
	public S_BloodPledgeInfo(boolean emblem_on) {
		writeC(Opcodes.S_EVENT);
		writeC(PLEDGE_EMBLEM_STATUS);
		writeC(1);
		writeC(emblem_on ? 1 : 0);// 0 : 해제 1 : 켜짐
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_BLOODPLEDGE_INFO;
	}
}

