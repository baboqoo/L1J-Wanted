package l1j.server.server.datatables;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.model.npc.action.L1NpcXmlParser;
import l1j.server.server.utils.FileUtil;
import l1j.server.server.utils.StringUtil;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class NpcActionTable {
	private static Logger _log = Logger.getLogger(NpcActionTable.class.getName());
	private static NpcActionTable _instance;
	private final List<L1NpcAction> _actions = new ArrayList<L1NpcAction>();
	private final List<L1NpcAction> _talkActions = new ArrayList<L1NpcAction>();

	private List<L1NpcAction> loadAction(File file, String nodeName) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);

		if (!doc.getDocumentElement().getNodeName().equalsIgnoreCase(nodeName))
			return new ArrayList<L1NpcAction>();
		return L1NpcXmlParser.listActions(doc.getDocumentElement());
	}

	private void loadAction(File file) throws Exception {
		_actions.addAll(loadAction(file, "NpcActionList"));
	}

	private void loadTalkAction(File file) throws Exception {
		_talkActions.addAll(loadAction(file, "NpcTalkActionList"));
	}

	private void loadDirectoryActions(File dir) throws Exception {
		File f = null;
		for (String file : dir.list()) {
			f = new File(dir, file);
			if (FileUtil.getExtension(f).equalsIgnoreCase("xml")) {
				loadAction(f);
				loadTalkAction(f);
			}
		}
	}

	private NpcActionTable() throws Exception {
		File usersDir = new File("./data/xml/NpcActions/users/");
		if (usersDir.exists())
			loadDirectoryActions(usersDir);
		loadDirectoryActions(new File("./data/xml/NpcActions/"));
	}

	public static void load() {
		try {
//			PerformanceTimer timer = new PerformanceTimer();
//			System.out.print("■ 엔피씨액션 데이터 .......................... ");
			_instance = new NpcActionTable();
//			System.out.println("■ 로딩 정상 완료 " + timer.get() + "ms");
		} catch (Exception e) {
			//_log.log(Level.SEVERE, "NpcAction를 읽어들일 수 없었습니다", e);
			_log.log(Level.SEVERE, "NpcAction could not be read", e);
			System.exit(0);
		}
	}

	public static NpcActionTable getInstance() {
		return _instance;
	}

	public L1NpcAction get(String actionName, L1PcInstance pc, L1Object obj) {
		for (L1NpcAction action : _actions) {
			if (action.acceptsRequest(actionName, pc, obj)) {
				return action;
			}
		}
		return null;
	}

	public L1NpcAction get(L1PcInstance pc, L1Object obj) {
		for (L1NpcAction action : _talkActions) {
			if (action.acceptsRequest(StringUtil.EmptyString, pc, obj)) {
				return action;
			}
		}
		return null;
	}
}

