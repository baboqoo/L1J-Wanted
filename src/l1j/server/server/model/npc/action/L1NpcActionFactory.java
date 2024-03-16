package l1j.server.server.model.npc.action;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Element;

public class L1NpcActionFactory {
	private static Logger _log = Logger.getLogger(L1NpcActionFactory.class.getName());
	private static Map<String, Constructor<L1NpcAction>> _actions = new HashMap<String, Constructor<L1NpcAction>>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Constructor<L1NpcAction> loadConstructor(Class c) throws NoSuchMethodException {
		return c.getConstructor(new Class[] { Element.class });
	}

	static {
		try {
			_actions.put("Action",		loadConstructor(L1NpcListedAction.class));
			_actions.put("MakeItem",	loadConstructor(L1NpcMakeItemAction.class));
			_actions.put("ShowHtml",	loadConstructor(L1NpcShowHtmlAction.class));
			_actions.put("SetQuest",	loadConstructor(L1NpcSetQuestAction.class));
			_actions.put("Teleport",	loadConstructor(L1NpcTeleportAction.class));
		} catch (NoSuchMethodException e) {
			//_log.log(Level.SEVERE, "NpcAction의 클래스 로드에 실패", e);
			_log.log(Level.SEVERE, "Failed to load class of NpcAction", e);
		}
	}

	public static L1NpcAction newAction(Element element) {
		try {
			Constructor<L1NpcAction> con = _actions.get(element.getNodeName());
			return con.newInstance(element);
		} catch (NullPointerException e) {
			//_log.warning(element.getNodeName() + " 미정도리의 NPC 액션입니다");
			_log.warning(element.getNodeName() + "Undefined NPC action");
		} catch (Exception e) {
			//_log.log(Level.SEVERE, "NpcAction의 클래스 로드에 실패", e);
			_log.log(Level.SEVERE, "Failed to load class of NpcAction", e);
			System.out.println(element.getNodeName());
		}
		return null;
	}
}

