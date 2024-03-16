package l1j.server.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StreamUtil;

public class Announcements {
	private static Announcements _instance;
	private final List<S_SystemMessage> _announcements = new ArrayList<S_SystemMessage>();
	private static Logger _log = Logger.getLogger(Announcements.class.getName());

	private Announcements() {
		loadAnnouncements();
	}

	public static Announcements getInstance() {
		if (_instance == null) {
			_instance = new Announcements();
		}
		return _instance;
	}

	private void loadAnnouncements() {
		_announcements.clear();
		File file = new File("data/announcements.txt");
		if (file.exists()) {
			readFromDisk(file);
		} else {
			_log.config("data/announcements.txt doesn't exist");
		}
	}

	public void showAnnouncements(L1PcInstance showTo) {
		for (S_SystemMessage msg : _announcements) {
			showTo.sendPackets(msg);
		}
	}

	private void readFromDisk(File file) {
		LineNumberReader lnr = null;
		try {
			int i = 0;
			String line = null;
			lnr = new LineNumberReader(new FileReader(file));
			StringTokenizer st = null;
			while((line = lnr.readLine()) != null){
				st = new StringTokenizer(line, "\n\r");
				if (st.hasMoreTokens()) {
					_announcements.add(new S_SystemMessage(st.nextToken()));
					i++;
				}
			}

			//_log.config("공지사항" + i + "로드");
			_log.config("Loading notice " + i);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			StreamUtil.close(lnr);
		}
	}

	public void announceToAll(String msg) {
		L1World.getInstance().broadcastServerMessage(msg, true);
	}
}
