package l1j.server.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import l1j.server.server.utils.StreamUtil;

public class BadNamesList {
	private static Logger _log = Logger.getLogger(BadNamesList.class.getName());

	private static BadNamesList _instance;

	private ArrayList<String> _nameList = new ArrayList<String>();

	public static BadNamesList getInstance() {
		if (_instance == null) {
			_instance = new BadNamesList();
		}
		return _instance;
	}

	private BadNamesList() {
		LineNumberReader lnr = null;

		try {
			File mobDataFile = new File("data/badnames.txt");
			lnr = new LineNumberReader(new BufferedReader(new FileReader(mobDataFile)));

			String line = null;
			StringTokenizer st = null;
			while((line = lnr.readLine()) != null) {
				if (line.trim().length() == 0 || line.startsWith("#")) {
					continue;
				}
				st = new StringTokenizer(line, ";");
				_nameList.add(st.nextToken());
			}

			_log.config("loaded " + _nameList.size() + " bad names");
		} catch (FileNotFoundException e) {
			_log.warning("badnames.txt is missing in data folder");
		} catch (Exception e) {
			_log.warning("error while loading bad names list : " + e);
		} finally {
			StreamUtil.close(lnr);
		}
	}

	public boolean isBadName(String name) {
		String lower = name.toLowerCase();
		for (String badName : _nameList) {
			if (lower.contains(badName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public String[] getAllBadNames() {
		return _nameList.toArray(new String[_nameList.size()]);
	}
}

