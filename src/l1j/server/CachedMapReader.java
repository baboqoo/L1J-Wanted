package l1j.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MapsTable.MapData;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1V1Map;
import l1j.server.server.utils.FileUtil;

/**
 * 텍스트 맵을 캐싱 해 read 시간을 단축한다.
 */
public class CachedMapReader extends MapReader {

	/** 텍스트 맵 홀더.  */
	private static final String MAP_DIR = "./maps/";

	/** 캐싱 하는 맵 홀더.  */
	private static final String CACHE_DIR = "./data/mapcache/";

	/**
	 * 전맵 ID의 리스트를 돌려준다.
	 *
	 * @return ArraryList
	 */
	private ArrayList<Integer> listMapIds() {
		ArrayList<Integer> ids = new ArrayList<Integer>();

		File mapDir = new File(MAP_DIR);
		File mapFile = null;
		for (String name : mapDir.list()) {
			mapFile = new File(mapDir, name);
			if (!mapFile.exists()) {
				continue;
			}
			if (!FileUtil.getExtension(mapFile).toLowerCase().equals("txt")) {
				continue;
			}
			int id = 0;
			try {
				String idStr = FileUtil.getNameWithoutExtension(mapFile);
				id = Integer.parseInt(idStr);
			} catch (NumberFormatException e) {
				continue;
			}
			ids.add(id);
		}
		return ids;
	}

	/**
	 * 지정의 맵 번호의 텍스트 맵을 캐쉬 맵으로 변경한다.
	 *
	 * @param mapId
	 *            맵 번호
	 * @return L1V1Map
	 * @throws IOException
	 */
	private L1V1Map cacheMap(final int mapId) throws IOException {
		File file = new File(CACHE_DIR);
		if (!file.exists()) {
			file.mkdir();
		}
		L1V1Map map = (L1V1Map) new TextMapReader().read(mapId);

		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(CACHE_DIR + mapId + ".map")));

		out.writeInt(map.getId());
		out.writeInt(map.getX());
		out.writeInt(map.getY());
		out.writeInt(map.getWidth());
		out.writeInt(map.getHeight());

		for (byte[] line : map.getRawTiles()) {
			for (byte tile : line) {
				out.writeByte(tile);
			}
		}
		out.flush();
		out.close();

		return map;
	}

	/**
	 * 지정의 맵 번호의 캐쉬 맵을 읽어들인다.
	 *
	 * @param mapId
	 *            맵 번호
	 * @return L1Map
	 * @throws IOException
	 */
	@Override
	public L1Map read(final int mapId) throws IOException {
		File file = new File(CACHE_DIR + mapId + ".map");
		if (!file.exists()) {
			return cacheMap(mapId);
		}
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(CACHE_DIR + mapId + ".map")));

		int id = in.readInt();
		if (mapId != id) {
			extracted();
		}

		int xLoc = in.readInt();
		int yLoc = in.readInt();
		int width = in.readInt();
		int height = in.readInt();

		byte[][] tiles = new byte[width][height];
		for (byte[] line : tiles) {
			in.read(line);
		}
		MapData data		= MapsTable.getInstance().getMap(mapId);
		in.close();
		L1V1Map map = null;
		if (data != null) {
			map = new L1V1Map(
					id, tiles, xLoc, yLoc,
					data.isUnderwater(), data.isMarkable(), data.isTeleportable(), data.isEscapable(),
					data.isUseResurrection(), data.isUsePainwand(), data.isEnabledDeathPenalty(), data.isTakePets(),
					data.isRecallPets(), data.isUsableItem(), data.isUsableSkill(), data.isDungeon(), 
					data.getDmgModiPc2Npc(), data.getDmgModiNpc2Pc(),
					data.isDecreaseHp(), data.isDominationTeleport(), data.isBeginZone(), data.isRedKnightZone(), data.isRuunCastleZone(),
					data.isInterWarZone(), data.isGeradBuffZone(), data.isGrowBuffZone(), data.getInter(), data.getScript());
		} else {
			map = new L1V1Map(
					id, tiles, xLoc, yLoc,
					false, false, false, false,
					false, false, false, false,
					false, false, false, false, 
					0, 0,
					false, false, false, false, false, false, false, false, null, null);
		}
		return map;
	}

	private void extracted() throws FileNotFoundException {
		throw new FileNotFoundException();
	}
	/**
	 * 모든 텍스트 맵을 읽어들인다.
	 *
	 * @return Map
	 * @throws IOException
	 */
	@Override
	public Map<Integer, L1Map> read() throws IOException {
		Map<Integer, L1Map> maps = new HashMap<Integer, L1Map>();
		for (int id : listMapIds()) {
			maps.put(id, read(id));
		}
		return maps;
	}
}

