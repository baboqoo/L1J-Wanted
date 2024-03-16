package l1j.server.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.message.S_SystemMessage;
import l1j.server.server.utils.StreamUtil;

public class Announcecycle {
	private static Logger _log = Logger.getLogger(Announcecycle.class.getName());

	private static Announcecycle _instance;

	private List<S_SystemMessage> _Announcecycle = new ArrayList<S_SystemMessage>();

	private int _Announcecyclesize = 0;

	private Announcecycle() {
		loadAnnouncecycle();
	}

	public static Announcecycle getInstance() {
		if (_instance == null) {
			_instance = new Announcecycle();
		}
		return _instance;
	}

	public void loadAnnouncecycle() {
		_Announcecycle.clear();
		File file = new File("data/Announcecycle.txt");
		if (file.exists()) {
			readFromDiskmulti(file);
			doAnnouncecycle();
		} else {
			_log.config("data/Announcecycle.txt");
		}
	}

	private void readFromDiskmulti(File file) {
		LineNumberReader lnr = null;
		try {
			int i = 0;
			String line = null;
			lnr = new LineNumberReader(new FileReader(file));
			StringTokenizer st = null;
			while((line = lnr.readLine()) != null){
				st = new StringTokenizer(line, "\n\r");
				if (st.hasMoreTokens()) {
					_Announcecycle.add(new S_SystemMessage(st.nextToken(), true));
					i++;
				}
			}

			_log.config("Announcecycle: Loaded " + i + " Announcecycle.");
		} catch (IOException e1) {
			_log.log(Level.SEVERE, "Error reading Announcecycle", e1);
		} finally {
			StreamUtil.close(lnr);
		}
	}

	public void doAnnouncecycle() {
		GeneralThreadPool.getInstance().scheduleAtFixedRate(new AnnouncTask(), 180000,	60000 * Config.ALT.Show_Announcecycle_Time);

	}

	/** The task launching the function doAnnouncCycle() */
	class AnnouncTask implements Runnable {
		@Override
		public void run() {
			try {
				L1World.getInstance().broadcastPacketToAll(_Announcecycle.get(_Announcecyclesize));
				if (++_Announcecyclesize >= _Announcecycle.size()) {
					_Announcecyclesize = 0;
				}
			} catch (Exception e) {
				_log.log(Level.WARNING, "", e);
			}
		}
	}

}

