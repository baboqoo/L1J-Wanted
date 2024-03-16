package l1j.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.MapsTable.MapData;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1V1Map;
import l1j.server.server.utils.StringUtil;

/**
 * 텍스트 맵(maps/\d*.txt)을 읽어들인다.
 */
public class TextMapReader extends MapReader {

	/** 메세지 로그용. */
	private static Logger _log = Logger.getLogger(TextMapReader.class.getName());
	/** 맵 홀더. */
	private static final String MAP_DIR = "./maps/";

	/**
	 * 지정의 맵 번호의 텍스트 맵을 읽어들인다.
	 * 
	 * @param mapId
	 *            맵 번호
	 * @param xSize
	 *            X좌표의 사이즈
	 * @param ySize
	 *            Y좌표의 사이즈
	 * @return byte[][]
	 * @throws IOException
	 */
	public byte[][] read(final int mapId, final int xSize, final int ySize) throws IOException {
		byte[][] map = new byte[xSize][ySize];
		LineNumberReader in = new LineNumberReader(new BufferedReader(new FileReader(MAP_DIR + mapId + ".txt")));

		int x, y = 0;
		String line;
		byte tile;
		StringTokenizer tok = null;
		while ((line = in.readLine()) != null) {
			if (line.trim().length() == 0 || line.startsWith("#")) {
				continue; // 빈줄, 코멘트는 스킵
			}
			x = 0;
			tok = new StringTokenizer(line, StringUtil.CommaString);
			while(tok.hasMoreTokens()){
				// byte tile = Byte.parseByte(tok.nextToken());
				//tile = (byte) Short.parseShort(tok.nextToken());
				tile = (byte) (Integer.parseInt(tok.nextToken()) & 0xFF);
				map[x][y] = tile;
				x++;
			}
			y++;
		}
		in.close();
		return map;
	}

	/**
	 * 지정의 맵 번호의 텍스트 맵을 읽음
	 * 
	 * @param id
	 *            맵 번호
	 * @return L1Map
	 * @throws IOException
	 */
	@Override
	public L1Map read(final int id) throws IOException {
		MapsTable maptable = MapsTable.getInstance();
		MapData data = maptable.getMap(id);
		return getV1Map(data.getMapId(), data);
	}

	/**
	 * 모든 텍스트 맵을 읽음
	 * 
	 * @return Map
	 * @throws IOException
	 */
	@Override
	public Map<Integer, L1Map> read() throws IOException {
		Map<Integer, L1Map> maps = new HashMap<Integer, L1Map>();
		L1V1Map map = null;
		MapsTable mapTable = MapsTable.getInstance();
		for (Map.Entry<Integer, MapData> mapData : mapTable.getMaps().entrySet()) {
			try {
				map = getV1Map(mapData.getKey(), mapData.getValue());
				maps.put(mapData.getKey(), map);
			} catch (IOException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		return maps;
	}
	
	private L1V1Map getV1Map(int mapId, MapData data) throws IOException {
		int xSize = data.getEndX() - data.getStartX() + 1;
		int ySize = data.getEndY() - data.getStartY() + 1;
		return new L1V1Map((short) mapId, this.read(mapId, xSize, ySize), data.getStartX(), data.getStartY(), 
				data.isUnderwater(), data.isMarkable(), data.isTeleportable(), data.isEscapable(),
				data.isUseResurrection(), data.isUsePainwand(), data.isEnabledDeathPenalty(), data.isTakePets(),
				data.isRecallPets(), data.isUsableItem(), data.isUsableSkill(), data.isDungeon(), 
				0, 0,
				data.isDecreaseHp(), data.isDominationTeleport(), data.isBeginZone(), data.isRedKnightZone(), data.isRuunCastleZone(),
				data.isInterWarZone(), data.isGeradBuffZone(), data.isGrowBuffZone(), data.getInter(), data.getScript());
	}

}
