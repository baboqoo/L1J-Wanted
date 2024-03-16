package l1j.server.common.bin;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.common.bin.quest.QuestCommonBin;
import l1j.server.common.bin.quest.QuestT;
import l1j.server.common.bin.quest.QuestCommonBin.QuestCommonBinExtend;

/**
 * quest-common.bin 파일 로더
 * @author LinOffice
 */
public class QuestCommonBinLoader {
	private static Logger _log = Logger.getLogger(QuestCommonBinLoader.class.getName());
	private static QuestCommonBin bin;
	
	private static QuestCommonBinLoader _instance;
	public static QuestCommonBinLoader getInstance() {
		if (_instance == null) {
			_instance = new QuestCommonBinLoader();
		}
		return _instance;
	}
	
	public static HashMap<Integer, QuestCommonBinExtend> getQuestList() {
		return bin.get_quest_list();
	}
	
	public static QuestT getQuest(int quest_number){
		QuestCommonBinExtend extend = bin.get_quest(quest_number);
		return extend == null ? null : extend.get_quest();
	}

	private QuestCommonBinLoader() {
		loadFile();
	}
	
	private void loadFile(){
		bin = QuestCommonBin.newInstance();
		try {
			bin.readFrom(ProtoInputStream.newInstance("./data/Contents/quest-common.bin"));// bin 파일을 읽는다
		} catch (IOException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		if (!bin.isInitialized())
			throw new IllegalArgumentException(String.format("fail initialized info data.(QuestCommonBin) %d", bin.getInitializeBit()));
	}
	
	public static void reload() {
		_instance.loadFile();
	}
}

