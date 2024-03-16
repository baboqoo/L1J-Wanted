package l1j.server.server;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class TownTeleport {
	private static Logger _log = Logger.getLogger(TownTeleport.class.getName());
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "TownTeleportConfiguration")
	private static class TownTeleportConfiguration implements Iterable<TownTeleport> {
		@XmlElement(name = "TownTeleport")
		private List<TownTeleport> _list;
		
		public Iterator<TownTeleport> iterator() {
			return _list.iterator();
		}
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Point {
		@XmlAttribute(name = "x")
		private int x;
		
		@XmlAttribute(name = "y")
		private int y;
		
		public int get_x() {
			return x;
		}
		
		public int get_y() {
			return y;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("x : ").append(x).append(", y : ").append(y);
			return sb.toString();
		}
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Territory {
		@XmlElement(name = "Point")
		private CopyOnWriteArrayList<Point> points;
		
		public List<Point> get_points() {
			return points;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			if (points != null) {
				for (Point val : points) {
					sb.append("\r\npoint : ").append(val.toString());
				}
			}
			return sb.toString();
		}
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TeleportTarget {
		@XmlAttribute(name = "index")
		private int index;
		
		@XmlElement(name = "Point")
		private Point point;
		
		public int get_index() {
			return index;
		}
		
		public Point get_point() {
			return point;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("index : ").append(index).append(", point : ").append(point.toString());
			return sb.toString();
		}
	}
	
	@XmlAttribute(name = "group")
	private int group;
	
	@XmlAttribute(name = "map")
	private int map;
	
	@XmlAttribute(name = "safetyZoneOnly")
	private int safetyZoneOnly;
	
	public int get_group() {
		return group;
	}
	
	public int get_map() {
		return map;
	}
	
	public boolean is_safetyZoneOnly() {
		return safetyZoneOnly == 1;
	}
	
	@XmlElement(name = "Territory")
	private Territory territory;
	
	@XmlElement(name = "TeleportTarget")
	private CopyOnWriteArrayList<TeleportTarget> teleport_target;
	
	public Territory get_territory() {
		return territory;
	}
	
	public List<TeleportTarget> get_teleport_target() {
		return teleport_target;
	}
	
	private static final String PATH = "./data/xml/TownTeleport/TownTeleport.xml";
	
	private static final HashMap<Integer, TownTeleport> DATA = new HashMap<Integer, TownTeleport>();
	
	public static TownTeleport get_data(int group) {
		return DATA.get(group);
	}
	
	public static void load() {
		try {
			JAXBContext context = JAXBContext.newInstance(TownTeleport.TownTeleportConfiguration.class);
			Unmarshaller um = context.createUnmarshaller();
			
			File file = new File(PATH);
			TownTeleport.TownTeleportConfiguration list = (TownTeleport.TownTeleportConfiguration) um.unmarshal(file);
			
			for (TownTeleport val : list) {
				DATA.put(val.group, val);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, "Failed to read TownTeleport.xml", e);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("group : ").append(group);
		sb.append("\r\nmap : ").append(map);
		sb.append("\r\nsafetyZoneOnly : ").append(safetyZoneOnly);
		if (territory != null) {
			sb.append("\r\nterritory : ").append(territory.toString());
		}
		if (teleport_target != null) {
			for (TeleportTarget val : teleport_target) {
				sb.append("\r\nteleport_target : ").append(val.toString());
			}
		}
		return sb.toString();
	}
}

