package l1j.server.server.clientpackets.proto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

/**
 * 프로토 패킷 Database 클래스
 * @author LinOffice
 */
public class ProtoPacketLoader {
	private static Logger _log = Logger.getLogger(ProtoPacketLoader.class.getName());
	
	private static ProtoPacketLoader _instance;
	public static ProtoPacketLoader getInstance(){
		if (_instance == null) {
			_instance = new ProtoPacketLoader();
		}
		return _instance;
	}
	
	/**
	 * ProtoPacket Information Map
	 * Key: code / Value: className
	 */
	private static final ConcurrentHashMap<Integer, String> DATA = new ConcurrentHashMap<>();
	
	/**
	 * 클라이언트에 할당할 프로토 패킷 인스턴스 생성
	 * 객체를 생성하여 할당한다.(비동기)
	 * @return Map
	 */
	protected static ConcurrentHashMap<Integer, ProtoHandler> createHandlers() {
		ConcurrentHashMap<Integer, ProtoHandler> handlers = new ConcurrentHashMap<Integer, ProtoHandler>();
		try {
			for (Map.Entry<Integer, String> entry : DATA.entrySet()) {
				Class<?> cls	= getClass(entry.getValue());
				if (cls == null) {
					System.out.println(String.format("[ProtoPacketLoader] not found class (%s)", entry.getValue()));
					continue;
				}
				ProtoHandler ins = (ProtoHandler) cls.newInstance();
				handlers.put(entry.getKey(), ins);
			}
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return handlers;
	}
	
	/**
	 * 클래스 로드
	 * @param path
	 * @return Class
	 * @throws ClassNotFoundException
	 */
	private static Class<?> getClass(String path) throws ClassNotFoundException {
		return Class.forName(String.format("l1j.server.server.clientpackets.proto.%s", path));
	}
	
	/**
	 * 기본 생성자
	 * 데이터 로드
	 */
	private ProtoPacketLoader(){
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM proto_packet");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				int code	= Integer.parseInt(rs.getString("code").replace("0x", StringUtil.EmptyString), 16);
				DATA.put(code, rs.getString("className"));
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
}

