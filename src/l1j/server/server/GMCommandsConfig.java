package l1j.server.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import l1j.server.server.model.L1Location;
import l1j.server.server.templates.L1ItemSetItem;
import l1j.server.server.utils.IterableElementList;

public class GMCommandsConfig {
	private static Logger _log = Logger.getLogger(GMCommandsConfig.class.getName());

	private interface ConfigLoader {
		public void load(Element element);
	}

	private abstract class ListLoaderAdapter implements ConfigLoader {
		private final String _listName;

		public ListLoaderAdapter(String listName) {
			_listName = listName;
		}

		@Override
		public final void load(Element element) {
			NodeList nodes = element.getChildNodes();
			for (Element elem : new IterableElementList(nodes)) {
				if (elem.getNodeName().equalsIgnoreCase(_listName)) {
					loadElement(elem);
				}
			}
		}

		public abstract void loadElement(Element element);
	}

	private class RoomLoader extends ListLoaderAdapter {
		public RoomLoader() {
			super("Room");
		}

		@Override
		public void loadElement(Element element) {
			int id		= Integer.valueOf(element.getAttribute("Id"));
			String name	= element.getAttribute("Name");
			int locX	= Integer.valueOf(element.getAttribute("LocX"));
			int locY	= Integer.valueOf(element.getAttribute("LocY"));
			int mapId	= Integer.valueOf(element.getAttribute("MapId"));
			L1Location loc = new L1Location(locX, locY, mapId);
			GMRoom room = new GMRoom();
			room.id = id;
			room.name = name;
			room.loc = loc;
			ROOMS_FROM_NAME.put(name.toLowerCase(), room);
			ROOMS_FROM_ID.put(id, room);
		}
	}

	private class ItemSetLoader extends ListLoaderAdapter {
		public ItemSetLoader() {
			super("ItemSet");
		}

		public L1ItemSetItem loadItem(Element element) {
			int id		= Integer.valueOf(element.getAttribute("Id"));
			int amount	= Integer.valueOf(element.getAttribute("Amount"));
			int enchant	= Integer.valueOf(element.getAttribute("Enchant"));
			return new L1ItemSetItem(id, amount, enchant);
		}

		@Override
		public void loadElement(Element element) {
			List<L1ItemSetItem> list = new ArrayList<L1ItemSetItem>();
			NodeList nodes = element.getChildNodes();
			for (Element elem : new IterableElementList(nodes)) {
				if (elem.getNodeName().equalsIgnoreCase("Item"))
					list.add(loadItem(elem));
			}
			String name = element.getAttribute("Name");
			ITEM_SETS.put(name.toLowerCase(), list);
		}
	}

	private static HashMap<String, ConfigLoader> _loaders = new HashMap<String, ConfigLoader>();
	static {
		GMCommandsConfig instance = new GMCommandsConfig();
		_loaders.put("roomlist", instance.new RoomLoader());
		_loaders.put("itemsetlist", instance.new ItemSetLoader());
	}
	
	public static class GMRoom {
		public int id;
		public String name;
		public L1Location loc;
	}

	public static LinkedHashMap<String, GMRoom> ROOMS_FROM_NAME = new LinkedHashMap<String, GMRoom>();
	public static LinkedHashMap<Integer, GMRoom> ROOMS_FROM_ID = new LinkedHashMap<Integer, GMRoom>();
	public static HashMap<String, List<L1ItemSetItem>> ITEM_SETS = new HashMap<String, List<L1ItemSetItem>>();

	private static Document loadXml(String file)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return builder.parse(file);
	}

	public static void load() {
		try {
			Document doc		= loadXml("./data/xml/GmCommands/GMCommands.xml");
			NodeList nodes		= doc.getDocumentElement().getChildNodes();
			ConfigLoader loader	= null;
			for (int i = 0; i < nodes.getLength(); i++) {
				loader = _loaders.get(nodes.item(i).getNodeName().toLowerCase());
				if (loader != null) {
					loader.load((Element) nodes.item(i));
				}
			}
		} catch (Exception e) {
			//_log.log(Level.SEVERE, "GMCommands.xml의 read에 실패", e);
			_log.log(Level.SEVERE, "Failed to read GMCommands.xml", e);
		}
	}
}

