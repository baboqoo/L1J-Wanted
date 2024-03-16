package l1j.server.server.clientpackets;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.GameClient;
import l1j.server.server.construct.message.L1ServerMessage;
import l1j.server.server.construct.message.L1SystemMessage;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.datatables.SpamTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.message.S_ServerMessage;
import l1j.server.server.serverpackets.message.S_SystemMessage;

public class C_MailBox extends ClientBasePacket {
	private static final String C_MAIL_BOX = "[C] C_MailBox";
	private static Logger _log = Logger.getLogger(C_MailBox.class.getName());

	private static final int TYPE_PRIVATE_MAIL			= 0; // 개인 편지
	private static final int TYPE_BLOODPLEDGE_MAIL		= 1; // 혈맹 편지
	private static final int TYPE_KEPT_MAIL				= 2; // 보관 편지

	private static final int READ_PRIVATE_MAIL			= 16; // 개인 편지읽기
	private static final int READ_BLOODPLEDGE_MAIL		= 17; // 혈맹 편지읽기
	private static final int READ_KEPT_MAIL				= 18; // 보관함 편지읽기

	private static final int WRITE_PRIVATE_MAIL			= 32; // 개인 편지쓰기
	private static final int WRITE_BLOODPLEDGE_MAIL		= 33; // 혈맹 편지쓰기

	private static final int DEL_PRIVATE_MAIL			= 48; // 개인 편지삭제
	private static final int DEL_BLOODPLEDGE_MAIL		= 49; // 혈맹 편지삭제
	private static final int DEL_KEPT_MAIL				= 50; // 보관함 편지삭제

	private static final int TO_KEEP_MAIL				= 64; // 편지 보관하기

	private static final int PRICE_PRIVATEMAIL			= 50; // 개인 편지 가격

	private static final int DEL_PRIVATE_LIST_MAIL		= 96; // 개인 편지리스트삭제
	private static final int DEL_BLOODPLEDGE_LIST_MAIL	= 97; // 혈맹 편지리스트삭제
	private static final int DEL_KEEP_LIST				= 98; // 보관편지 리스트삭제

	private static final int PRICE_BLOODPLEDGEMAIL		= 1000; // 혈맹 편지 가격

	private static final int SIZE_PRIVATE_MAILBOX		= 40; // 개인 편지함 크기
	private static final int SIZE_BLOODPLEDGE_MAILBOX	= 80; // 혈맹 편지함 크기
	private static final int SIZE_KEPTMAIL_MAILBOX		= 10; // 편지보관함 크기

	public C_MailBox(byte abyte0[], GameClient client) {
		super(abyte0);
		try {
			int type = readC();
			L1PcInstance pc = client.getActiveChar();
			synchronized (LetterTable.getInstance()) {
				switch (type) {
				case TYPE_PRIVATE_MAIL:
					if (pc.isGm()) {
						LetterList(pc, TYPE_PRIVATE_MAIL, 1000);
						break;
					}
					LetterList(pc, TYPE_PRIVATE_MAIL, SIZE_PRIVATE_MAILBOX);
					break;
				case TYPE_BLOODPLEDGE_MAIL:
					LetterList(pc, TYPE_BLOODPLEDGE_MAIL, SIZE_BLOODPLEDGE_MAILBOX);
					break;
				case TYPE_KEPT_MAIL:
					LetterList(pc, TYPE_KEPT_MAIL, SIZE_KEPTMAIL_MAILBOX);
					break;
				case READ_PRIVATE_MAIL:
					ReadLetter(pc, READ_PRIVATE_MAIL, 0);
					break;
				case READ_BLOODPLEDGE_MAIL:
					ReadLetter(pc, READ_BLOODPLEDGE_MAIL, 0);
					break;
				case READ_KEPT_MAIL:
					ReadLetter(pc, READ_KEPT_MAIL, 0);
					break;
				case WRITE_PRIVATE_MAIL:
					WritePrivateMail(pc);
					break;
				case WRITE_BLOODPLEDGE_MAIL:
					WriteBloodPledgeMail(pc);
					break;
				case DEL_PRIVATE_MAIL:
					DeleteLetter(pc, DEL_PRIVATE_MAIL, TYPE_PRIVATE_MAIL);
					break;
				case DEL_BLOODPLEDGE_MAIL:
					DeleteLetter(pc, DEL_BLOODPLEDGE_MAIL, TYPE_BLOODPLEDGE_MAIL);
					break;
				case DEL_KEPT_MAIL:
					DeleteLetter(pc, DEL_KEPT_MAIL, TYPE_KEPT_MAIL);
					break;
				case TO_KEEP_MAIL:
					SaveLetter(pc, TO_KEEP_MAIL, TYPE_KEPT_MAIL);
					break;
				case DEL_PRIVATE_LIST_MAIL:
					DeleteLetter_List(pc, DEL_PRIVATE_MAIL, SIZE_PRIVATE_MAILBOX);
					break;
				case DEL_BLOODPLEDGE_LIST_MAIL:
					DeleteLetter_List(pc, DEL_BLOODPLEDGE_MAIL, SIZE_BLOODPLEDGE_MAILBOX);
					break;
				case DEL_KEEP_LIST:
					DeleteLetter_List(pc, DEL_KEPT_MAIL, SIZE_KEPTMAIL_MAILBOX);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}

	void DeleteLetter_List(L1PcInstance pc, int deletetype, int type) {
		int delete_num = readD();
		for (int i = 0; i < delete_num; i++) {
			int id = readD();
			LetterTable.getInstance().deleteLetter(id);
			pc.sendPackets(new S_LetterList(pc, deletetype, id, true), true);
		}
	}

	boolean payMailCost(final L1PcInstance RECEIVER, final int PRICE) {
		int AdenaCnt = RECEIVER.getInventory().countItems(L1ItemId.ADENA);
		if (AdenaCnt < PRICE) {
			RECEIVER.sendPackets(L1ServerMessage.sm189);
			return false;
		}
		RECEIVER.getInventory().consumeItem(L1ItemId.ADENA, PRICE);
		return true;
	}

	void WritePrivateMail(L1PcInstance sender) {
		long sysTime = System.currentTimeMillis();
		long curtime = sysTime / 1000;
		if (sender.getQuizTime() + 10 > curtime) {
			long sec = (sender.getQuizTime() + 10) - curtime;
			//sender.sendPackets(new S_SystemMessage(String.format("%s초 후에 사용할 수 있습니다.", sec)), true);
			sender.sendPackets(new S_ServerMessage(S_ServerMessage.getStringIdx(25), String.valueOf(sec)), true);
			return;
		}
		if (sender.getLevel() < 30) {
			sender.sendPackets(L1SystemMessage.LETTER_WRITE_LEVEL);
			return;
		}
		if (!payMailCost(sender, PRICE_PRIVATEMAIL)) {
			return;
		}
		int paper = readH(); // 편지지

		String receiverName = readS();
		String subject = readSS();
		String content = readSS();
		
		if (subject.length() > 20) {
//AUTO SRM: 			sender.sendPackets(new S_SystemMessage("편지 제목이 너무 길어 전송에 실패 하였습니다."), true); // CHECKED OK
			sender.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(101), true), true);
			return;
		}
		if (content.length() > 2000) {
//AUTO SRM: 			sender.sendPackets(new S_SystemMessage("편지 내용이 너무 길어 전송에 실패 하였습니다."), true); // CHECKED OK
			sender.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(102), true), true);
			return;
		}

		if (!checkCountMail(sender, receiverName, TYPE_PRIVATE_MAIL, SIZE_PRIVATE_MAILBOX)) {
			return;
		}
		L1PcInstance target = L1World.getInstance().getPlayer(receiverName);

		if (target != null) {
			L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(target.getId());
			if (exList.contains(1, sender.getName())) {
				sender.sendPackets(L1ServerMessage.sm3082);
				return;
			}
		}
		Timestamp dTime = new Timestamp(sysTime);
		int id = LetterTable.getInstance().writeLetter(paper, dTime, sender.getName(), receiverName, TYPE_PRIVATE_MAIL, subject, content);
		if (target != null && target.getOnlineStatus() != 0) {
			target.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id, S_LetterList.TYPE_RECEIVE, sender.getName(), subject), true); // 받는사람
		}
		
		/** 리마스터 보내는 PC 서버패킷 **/
		sender.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_PRIVATE_MAIL, id, S_LetterList.TYPE_SEND, receiverName, subject), true); //보내는 사람
		sender.sendPackets(S_LetterList.WRITE_PRIVATE_MAIL); //보내는 사람
		
		sender.setQuizTime(curtime);
	}

	void WriteBloodPledgeMail(L1PcInstance sender) {
		if (!payMailCost(sender, PRICE_BLOODPLEDGEMAIL)) {
			return;
		}
		int paper = readH(); // 편지지
		String receiverName = readS();
		String subject = readSS();
		String content = readSS();
		
		if (subject.length() > 20) {
//AUTO SRM: 			sender.sendPackets(new S_SystemMessage("편지 제목이 너무 길어 전송에 실패 하였습니다."), true); // CHECKED OK
			sender.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(101), true), true);
			return;
		}
		if (content.length() > 2000) {
//AUTO SRM: 			sender.sendPackets(new S_SystemMessage("편지 내용이 너무 길어 전송에 실패 하였습니다."), true); // CHECKED OK
			sender.sendPackets(new S_SystemMessage(S_SystemMessage.getRefText(102), true), true);
			return;
		}

		L1Clan targetClan = null;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getClanName().toLowerCase().equals(receiverName.toLowerCase())) {
				targetClan = clan;
				break;
			}
		}
		String name;
		L1PcInstance target = null;
		ArrayList<ClanMember> clanMemberList = targetClan.getClanMemberList();
		try {
			Timestamp dTime = new Timestamp(System.currentTimeMillis());
			for (int i = 0, a = clanMemberList.size(); i < a; i++) {
				name = clanMemberList.get(i).name;
				target = L1World.getInstance().getPlayer(name);
				if (!checkCountMail(sender, name, TYPE_BLOODPLEDGE_MAIL, SIZE_BLOODPLEDGE_MAILBOX)) {
					continue;
				}
				if (name.equalsIgnoreCase(sender.getName())) {
					continue;
				}
				int id = LetterTable.getInstance().writeLetter(paper, dTime, sender.getName(), name, TYPE_BLOODPLEDGE_MAIL, subject, content);
			
				if (target != null && target.getOnlineStatus() != 0) {
					target.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_BLOODPLEDGE_MAIL, id, S_LetterList.TYPE_RECEIVE, sender.getName(), subject), true); // 받는사람
				}
				/** 리마스터 보내는 PC 서버패킷 **/
				sender.sendPackets(new S_LetterList(S_LetterList.WRITE_TYPE_BLOODPLEDGE_MAIL, id, S_LetterList.TYPE_SEND, receiverName, subject), true); //보내는 사람
				sender.sendPackets(S_LetterList.WRITE_BLOODPLEDGE_MAIL); //보내는 사람
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
	
	void DeleteLetter(L1PcInstance pc, int type, int letterType) {
		int id = readD();
		LetterTable.getInstance().deleteLetter(id);
		pc.sendPackets(new S_LetterList(pc, type, id, true), true);
	}
	
	void ReadLetter(L1PcInstance pc, int type, int read) {
		int id = readD();
		LetterTable.getInstance().CheckLetter(id);
		pc.sendPackets(new S_LetterList(pc, type, id, read), true);
	}

	void LetterList(L1PcInstance pc, int type, int count) {
		//pc.sendPackets(new S_LetterList(pc, type, count), true); //리마스터 서버패킷 null
	}
	
	void SaveLetter(L1PcInstance pc, int type, int letterType) {
		int id = readD();
		LetterTable.getInstance().SaveLetter(id, letterType);
		pc.sendPackets(new S_LetterList(pc, type, id, true), true);
	}

	boolean checkCountMail(L1PcInstance from, String to, int type, int max) {
		if (!CharacterTable.getInstance().isContainNameList(to)) {
			from.sendPackets(L1ServerMessage.sm1240);// 편지: 보내기 불가(상대방 편지함 포화 상태)
			return false;
		}
		int cntMailInMailBox = LetterTable.getInstance().getLetterCount(to, type);
		if (cntMailInMailBox >= max) { // 편지함 만땅
			from.sendPackets(L1ServerMessage.sm1240);// 편지: 보내기 불가(상대방 편지함 포화 상태)
			return false;
		}
		return true;
	}

	@Override
	public String getType() {
		return C_MAIL_BOX;
	}
}


