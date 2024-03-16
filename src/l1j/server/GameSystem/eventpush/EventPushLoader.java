package l1j.server.GameSystem.eventpush;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.eventpush.bean.EventPushObject;
import l1j.server.GameSystem.eventpush.user.EventPushUser;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.eventpush.S_EventPushInfoListNoti;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

/**
 * 시스템 편지 시스템(푸시)
 * @author LinOffice
 */
public class EventPushLoader {
	private static Logger _log = Logger.getLogger(EventPushLoader.class.getName());
	
	private static class newInstance {
		public static final EventPushLoader INSTANCE = new EventPushLoader();
	}
	public static EventPushLoader getInstance(){
		return newInstance.INSTANCE;
	}
	
	private static final FastMap<Integer, EventPushUser> USER_INFO		= new FastMap<>();// 모든 유저의 정보
	private static int _id;
	
	/**
	 * 유저의 푸시 정보 조사
	 * @param objId
	 * @return EventPushUser
	 */
	public static EventPushUser getInfo(int objId){
		return USER_INFO.get(objId);
	}
	
	private EventPushLoader(){
		load();
	}
	
	void load(){
		Connection con				= null;
		PreparedStatement pstm		= null;
		ResultSet rs				= null;
		PreparedStatement tempPstm	= null;
		ResultSet tempRs			= null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			
			pstm = con.prepareStatement("DELETE FROM character_eventpush WHERE enable_date <= NOW()");
			pstm.executeUpdate();
			SQLUtil.close(pstm);
			
			pstm = con.prepareStatement("SELECT DISTINCT(objId) FROM character_eventpush");
			rs = pstm.executeQuery();
			EventPushObject alarmTemp = null;
			while (rs.next()) {
				try {
					int objId	= rs.getInt("objId");
					tempPstm	= con.prepareStatement("SELECT T.* FROM (SELECT * FROM character_eventpush WHERE objId = ? ORDER BY enable_date DESC LIMIT " + Config.PUSH.LIMIT_SIZE + ") T ORDER BY T.enable_date ASC");
					tempPstm.setInt(1, objId);
					tempRs		= tempPstm.executeQuery();
					EventPushUser alarmUser = new EventPushUser();
					while (tempRs.next()) {
						int push_id					= tempRs.getInt("push_id");
						String subject				= tempRs.getString("subject");
						String content				= tempRs.getString("content");
						String web_url				= tempRs.getString("web_url");
						int itemId					= tempRs.getInt("itemId");
						L1Item item					= ItemTable.getInstance().getTemplate(itemId);
						if (item == null) {
							continue;
						}
						int item_amount				= tempRs.getInt("item_amount");
						int item_enchant			= tempRs.getInt("item_enchant");
						boolean used_immediately	= Boolean.parseBoolean(tempRs.getString("used_immediately"));
						int status					= tempRs.getInt("status");
						Timestamp enable_date		= tempRs.getTimestamp("enable_date");
						int image_id				= tempRs.getInt("image_id");
						alarmTemp					= new EventPushObject(push_id, subject, content, web_url, itemId, item, item_amount, item_enchant, used_immediately, status, enable_date, image_id);
						alarmUser.add(push_id, alarmTemp);
						if (alarmTemp.getEventPushId() > _id) {
							_id = alarmTemp.getEventPushId();
						}
					}
					USER_INFO.put(objId, alarmUser);
				} catch(Exception e) {
					e.printStackTrace();
				} finally {
					SQLUtil.close(tempRs, tempPstm);
				}
			}
		} catch(SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(tempRs, tempPstm);
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	/**
	 * 데이터 저장
	 * @param pc
	 */
	public void store(L1PcInstance pc){
		if (USER_INFO == null || !USER_INFO.containsKey(pc.getId()) || USER_INFO.get(pc.getId()).getInfo() == null) {
			return;
		}
		FastTable<EventPushObject> list	=	new FastTable<>(USER_INFO.get(pc.getId()).getInfo().values());
		EventPushObject obj				=	null;
		Connection con						=	null;
		PreparedStatement pstm				=	null;
		try {
			con 	=	L1DatabaseFactory.getInstance().getConnection();
			pstm	=	con.prepareStatement("DELETE FROM character_eventpush WHERE objId=?");
			pstm.setInt(1, pc.getId());
			pstm.execute();
			SQLUtil.close(pstm);
			if (list.isEmpty()) {
				return;
			}
			con.setAutoCommit(false);
			pstm	=	con.prepareStatement("INSERT INTO character_eventpush SET push_id=?, objId=?, subject=?, content=?, web_url=?, itemId=?, "
					+ "item_amount=?, item_enchant=?, used_immediately=?, status=?, enable_date=?, image_id=?");
			for (int i=0; i<list.size(); i++) {
				obj = list.get(i);
				int index = 0;
				pstm.setInt(++index, obj.getEventPushId());
				pstm.setInt(++index, pc.getId());
				pstm.setString(++index, obj.getSubject());
				pstm.setString(++index, obj.getText());
				pstm.setString(++index, obj.getWebUrl());
				pstm.setInt(++index, obj.getItem().getItemId());
				pstm.setInt(++index, obj.getItemAmount());
				pstm.setInt(++index, obj.getItemEnchant());
				pstm.setString(++index, String.valueOf(obj.isUsedImmediately()));
				pstm.setInt(++index, obj.getStatus());
				pstm.setTimestamp(++index, obj.getEnableDate());
				pstm.setInt(++index, obj.getImageId());
				pstm.addBatch();
				pstm.clearParameters();
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			try {
				con.rollback();
			} catch(SQLException sqle){
				_log.log(Level.SEVERE, sqle.getLocalizedMessage(), sqle);
			}
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
			SQLUtil.close(pstm, con);
		}
	}

	/**
	 * 정보 출력
	 * @param pc
	 */
	public void sendInfoPacket(L1PcInstance pc){
		EventPushUser userInfo = getInfo(pc.getId());
		if (userInfo == null) {
			return;
		}
		FastMap<Integer, EventPushObject> map = userInfo.getInfo();
		if (map == null || map.isEmpty()) {
			return;
		}
		pc.sendPackets(new S_EventPushInfoListNoti(pc, map.values()), true);
	}
	
	/**
	 * 데이터 등록
	 * @param pc
	 * @param itemId
	 * @param subject
	 * @param content
	 * @param item_amount
	 * @param item_enchant
	 * @param used_immediately
	 * @return boolean
	 */
	public boolean create(L1PcInstance pc, int itemId, String subject, String content, int item_amount, int item_enchant, boolean used_immediately){
		EventPushUser user = null;
		if (USER_INFO.containsKey(pc.getId())) {
			user = USER_INFO.get(pc.getId());
			if (user.getInfo().size() >= Config.PUSH.LIMIT_SIZE) {
				return false;
			}
		} else {
			user = new EventPushUser();
			USER_INFO.put(pc.getId(), user);
		}
		L1Item item				= ItemTable.getInstance().getTemplate(itemId);
		Timestamp enable_date	= new Timestamp(System.currentTimeMillis() + (Config.PUSH.ENABLE_SECOND * 1000L));
		EventPushObject obj		= new EventPushObject(++_id, subject, content, null, item.getItemId(), item, item_amount, item_enchant, used_immediately, 0, enable_date, 0);
		user.add(obj.getEventPushId(), obj);
		sendInfoPacket(pc);
		return true;
	}
	
	/**
	 * 메모리 영역 제거
	 * @param objid
	 */
	public void removeInfo(int objid){
		if (USER_INFO.containsKey(objid)) {
			USER_INFO.get(objid).getInfo().clear();
			USER_INFO.remove(objid);
		}
	}
	
}

