package l1j.server.server.clientpackets;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.GameClient;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.pledge.S_BloodPledgeEmblem;

public class C_EmblemUpload extends ClientBasePacket {
	private static final String C_EMBLEM_UPLOAD = "[C] C_EmblemUpload";
	private static Logger _log = Logger.getLogger(C_EmblemUpload.class.getName());

	public C_EmblemUpload(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		try{
			L1PcInstance player = client.getActiveChar();
			if (player == null || player.getClanid() == 0) {
				return;
			}
			if (player.getBloodPledgeRank() != eBloodPledgeRankType.RANK_NORMAL_KING) {
				return;
			}
			ClanTable clanTable = ClanTable.getInstance();
			L1Clan clan = clanTable.getTemplate(player.getClanid());
			if (clan == null) {
				return;
			}
			
			File deleteFile = null;
			try {
				deleteFile = new File(String.format("emblem/%d", clan.getEmblemId()));
				if (deleteFile.exists()) {
					deleteFile.delete();// 기존 파일 삭제
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				deleteFile = null;
			}
			
			FileOutputStream fos = null;
			try {
				byte[] buff = readByte();
				int newEmblemdId = IdFactory.getInstance().nextId();
				fos = new FileOutputStream(String.format("emblem/%d", newEmblemdId));
				fos.write(buff, 0, buff.length);// 파일 생성
				
				clan.setEmblemId(newEmblemdId);
				clanTable.updateClan(clan);
				for (L1PcInstance pc : clan.getOnlineClanMember()) {
					pc.broadcastPacketWithMe(new S_BloodPledgeEmblem(pc.getId(), newEmblemdId), true);
				}
			} catch(Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				if (fos != null) {
					fos.close();
				}
				fos = null;
			}
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_EMBLEM_UPLOAD;
	}
}

