package l1j.server.server.model.npc.action;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.utils.IterableElementList;

public class L1NpcShowHtmlAction extends L1NpcXmlAction {
	private final String _htmlId;
	private final String[] _args;

	public L1NpcShowHtmlAction(Element element) {
		super(element);

		_htmlId = element.getAttribute("HtmlId");
		NodeList list = element.getChildNodes();
		ArrayList<String> dataList = new ArrayList<String>();
		for (Element elem : new IterableElementList(list)) {
			if (elem.getNodeName().equalsIgnoreCase("Data")) {
				dataList.add(elem.getAttribute("Value"));
			}
		}
		_args = dataList.toArray(new String[dataList.size()]);
	}

	@Override
	public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj, byte[] args) {
		return new L1NpcHtml(_htmlId, _args);
	}

}

