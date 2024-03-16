package l1j.server;

import java.io.IOException;
import java.util.Map;

import l1j.server.server.model.map.L1Map;

public abstract class MapReader {
	/**
	 * 모든 텍스트 맵을 읽어들인다(추상 클래스)
	 *
	 * @return Map
	 * @throws IOException
	 */
	public abstract Map<Integer, L1Map> read() throws IOException;

	/**
	 * 지정의 맵 번호의 텍스트 맵을 읽어들인다.
	 *
	 * @param id
	 *            맵 ID
	 * @return L1Map
	 * @throws IOException
	 */
	public abstract L1Map read(int id) throws IOException;

	/**
	 * 읽어들이는 맵 파일을 판단한다(텍스트 맵 or 캐쉬 맵 or V2텍스트 맵).
	 *
	 * @return MapReader
	 */
	public static MapReader getDefaultReader() {
		if (Config.SERVER.CACHE_MAP_FILES) {
			return new CachedMapReader();
		}
		return new TextMapReader();
	}
}

