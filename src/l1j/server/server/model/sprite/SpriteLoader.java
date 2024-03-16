package l1j.server.server.model.sprite;

import static l1j.server.server.ActionCodes.ACTION_AltAttack;
import static l1j.server.server.ActionCodes.ACTION_AltAttackAxe;
import static l1j.server.server.ActionCodes.ACTION_AltAttackBow;
import static l1j.server.server.ActionCodes.ACTION_AltAttackSpear;
import static l1j.server.server.ActionCodes.ACTION_Attack;
import static l1j.server.server.ActionCodes.ACTION_AttackSpear;
import static l1j.server.server.ActionCodes.ACTION_AxeAttack;
import static l1j.server.server.ActionCodes.ACTION_AxeDamage;
import static l1j.server.server.ActionCodes.ACTION_AxeWalk;
import static l1j.server.server.ActionCodes.ACTION_BowAttack;
import static l1j.server.server.ActionCodes.ACTION_BowWalk;
import static l1j.server.server.ActionCodes.ACTION_ChainSwordAttack;
import static l1j.server.server.ActionCodes.ACTION_ChainSwordDamage;
import static l1j.server.server.ActionCodes.ACTION_ChainSwordWalk;
import static l1j.server.server.ActionCodes.ACTION_ClawAttack;
import static l1j.server.server.ActionCodes.ACTION_ClawDamage;
import static l1j.server.server.ActionCodes.ACTION_ClawWalk;
import static l1j.server.server.ActionCodes.ACTION_DaggerAttack;
import static l1j.server.server.ActionCodes.ACTION_DaggerDamage;
import static l1j.server.server.ActionCodes.ACTION_DaggerWalk;
import static l1j.server.server.ActionCodes.ACTION_Damage;
import static l1j.server.server.ActionCodes.ACTION_DamageSpear;
import static l1j.server.server.ActionCodes.ACTION_DoubleAxeAttack;
import static l1j.server.server.ActionCodes.ACTION_DoubleAxeWalk;
import static l1j.server.server.ActionCodes.ACTION_EdoryuAttack;
import static l1j.server.server.ActionCodes.ACTION_EdoryuDamage;
import static l1j.server.server.ActionCodes.ACTION_EdoryuWalk;
import static l1j.server.server.ActionCodes.ACTION_Pickup;
import static l1j.server.server.ActionCodes.ACTION_SkillAttack;
import static l1j.server.server.ActionCodes.ACTION_SkillBuff;
import static l1j.server.server.ActionCodes.ACTION_SpearAttack;
import static l1j.server.server.ActionCodes.ACTION_SpearDamage;
import static l1j.server.server.ActionCodes.ACTION_SpearWalk;
import static l1j.server.server.ActionCodes.ACTION_SpellDirectionExtra;
import static l1j.server.server.ActionCodes.ACTION_StaffAttack;
import static l1j.server.server.ActionCodes.ACTION_StaffDamage;
import static l1j.server.server.ActionCodes.ACTION_StaffWalk;
import static l1j.server.server.ActionCodes.ACTION_SwordAttack;
import static l1j.server.server.ActionCodes.ACTION_SwordDamage;
import static l1j.server.server.ActionCodes.ACTION_SwordWalk;
import static l1j.server.server.ActionCodes.ACTION_ThrowingKnifeAttack;
import static l1j.server.server.ActionCodes.ACTION_ThrowingKnifeWalk;
import static l1j.server.server.ActionCodes.ACTION_TwoHandSwordAttack;
import static l1j.server.server.ActionCodes.ACTION_TwoHandSwordDamage;
import static l1j.server.server.ActionCodes.ACTION_TwoHandSwordWalk;
import static l1j.server.server.ActionCodes.ACTION_Walk;
import static l1j.server.server.ActionCodes.ACTION_WalkSpear;
import static l1j.server.server.ActionCodes.ACTION_Wand;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.ArrangeHelper;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 스프라이트 로더
 * @author LinOffice
 */
public class SpriteLoader {
	private static Logger _log = Logger.getLogger(SpriteLoader.class.getName());
	private static final HashMap<Integer, L1Sprite> DATA = new HashMap<>();// spr에 대한 모든 데이터
	private static final SpriteLoader _instance = new SpriteLoader();
	
	public static SpriteLoader getInstance() {
		return _instance;
	}
	
	/**
	 * sprite 데이터 조사(이미지가 존재하는 캐릭터들에게 할당한다)
	 * @param sprite_id
	 * @return L1Sprite
	 */
	public static L1Sprite get_sprite(int sprite_id) {
		return DATA.get(sprite_id);
	}
	
	/**
	 * 레벨에 대한 바운더리 레벨 조사
	 * @param level
	 * @return boundary_level
	 */
	public static int getBoundaryLevel(int level){
		for (int i=_levels.length - 1; i>=0; --i) {
			if (level >= _levels[i]) {
				return _levels[i];
			}
		}
		return 0;
	}
	
	/**
	 * 레벨에 대한 바운더리 레벨 인덱스 조사
	 * @param level
	 * @return boundary_level_index
	 */
	public static int getBoundaryLevelToIndex(int level){
		return level == 0 || _levelToIdx == null || level > _levelToIdx.length - 1 ? 0 : _levelToIdx[level];
	}
	
	/**
	 * 기본 생성자
	 * 데이터 로드
	 */
	private SpriteLoader() {
		loadSprite();
		loadSpriteAction();
		loadPolyFrameRate();
	}
	
	/**
	 * sprite 정보 로드
	 */
	private void loadSprite() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1Sprite sprite			= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM spr_info");
			rs		= pstm.executeQuery();
			while(rs.next()){
				sprite	= new L1Sprite(
						rs.getInt("spr_id"),
						rs.getInt("shadow"),
						rs.getInt("type"),
						rs.getInt("attr"),
						rs.getInt("width"),
						rs.getInt("height"),
						rs.getInt("flying_type"),
						rs.getInt("action_count"));
				DATA.put(sprite.sprite_id, sprite);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	/**
	 * sprite action 로드
	 */
	private void loadSpriteAction() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		ResultSet rs			= null;
		L1Sprite spr			= null;
		L1SpriteAction action	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM spr_action");
			rs		= pstm.executeQuery();
			while(rs.next()){
				int spr_id = rs.getInt("spr_id");
				spr = DATA.get(spr_id);
				
				if (spr == null) {
					System.out.println(String.format("[SpriteLoader] UNDEFINED_SPRITE : ID(%d)", spr_id));
					continue;
				}

				int actid		= rs.getInt("act_id");
				int frameCount	= rs.getInt("framecount");
				int frameRate	= rs.getInt("framerate");
				if (actid == ACTION_AttackSpear || actid == ACTION_AltAttackSpear) {
					frameCount += 4;// 원거리폼 속도 감소 처리 4정도가 적당
				}
				action = spr.action;
				if (action == null) {
					System.out.println(String.format("[SpriteLoader] UNDEFINED_SPRITE_ACTION : ID(%d)", spr_id));
					continue;
				}
				action.put_frame_count(actid, frameCount);
				
				int speed = spr.calcActionSpeed(frameCount, frameRate);// 액션의 interval 취득
				
				action.put_action_speed(actid, speed);

				switch (actid) {
				case ACTION_Walk:
				case ACTION_SwordWalk:
				case ACTION_AxeWalk:
				case ACTION_BowWalk:
				case ACTION_SpearWalk:
				case ACTION_StaffWalk:
				case ACTION_DaggerWalk:
				case ACTION_TwoHandSwordWalk:
				case ACTION_EdoryuWalk:
				case ACTION_ClawWalk:
				case ACTION_ThrowingKnifeWalk:
				case ACTION_ChainSwordWalk:
				case ACTION_DoubleAxeWalk:
				case ACTION_WalkSpear:
					action.put_move_speed(actid, speed);
					break;
				case ACTION_SkillAttack:
					action.set_dir_spell_speed(speed);
					break;
				case ACTION_SkillBuff:
					action.set_nodir_spell_speed(speed);
					break;
				case ACTION_Attack:
				case ACTION_SwordAttack:
				case ACTION_AxeAttack:
				case ACTION_BowAttack:
				case ACTION_SpearAttack:
				case ACTION_AltAttack:
				case ACTION_SpellDirectionExtra:
				case ACTION_StaffAttack:
				case ACTION_DaggerAttack:
				case ACTION_TwoHandSwordAttack:
				case ACTION_EdoryuAttack:
				case ACTION_ClawAttack:
				case ACTION_ThrowingKnifeAttack:
				case ACTION_ChainSwordAttack:
				case ACTION_DoubleAxeAttack:
				case ACTION_AltAttackAxe:
				case ACTION_AltAttackBow:
				case ACTION_AttackSpear:
				case ACTION_AltAttackSpear:
					action.put_attack_speed(actid, speed);
					break;
				case ACTION_Damage:
				case ACTION_SwordDamage:
				case ACTION_AxeDamage:
				case ACTION_SpearDamage:
				case ACTION_StaffDamage:
				case ACTION_DaggerDamage:
				case ACTION_TwoHandSwordDamage:
				case ACTION_EdoryuDamage:
				case ACTION_ClawDamage:
				case ACTION_ChainSwordDamage:
				case ACTION_DamageSpear:
					action.put_damage_speed(actid, speed);
					break;
				case ACTION_Pickup:
					action.set_pickup_speed(speed);
					break;
				case ACTION_Wand:
					action.set_wand_speed(speed);
					break;
				default:
					break;
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	// document PolyFrameRate load
	private static final String POLY_FRAME_RATE_PATH = "./data/xml/PolyFrameRate/PolyFrameRate.xml";
	private void loadPolyFrameRate(){
		try {
			Document doc = DocumentBuilderFactory
					.newInstance()
					.newDocumentBuilder()
					.parse(new File(POLY_FRAME_RATE_PATH));
			
			loadLevelInformation(doc);
			loadActionRates(doc);
			loadExceptionActionRates(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void settingFrameRate(Integer action, Integer sprite_id, Double[] rates){
		L1Sprite sprite					= get_sprite(sprite_id);
		if (sprite == null) {
			return;
		}
		PolyFrameSprite poly_frame_spr	= sprite.poly_frame_sprite;// sprite 취득
		if (poly_frame_spr == null) {
			poly_frame_spr = new PolyFrameSprite();
			poly_frame_spr.sprite = sprite_id;
			poly_frame_spr.actionList = new HashMap<Integer, PolyFrameAction>();
			sprite.poly_frame_sprite = poly_frame_spr;
		}
		
		PolyFrameAction poly_frame_action = poly_frame_spr.actionList.get(action);// action 취득
		if (poly_frame_action == null) {
			poly_frame_action = new PolyFrameAction();
			poly_frame_action.action = action;
			poly_frame_action.levelRateList = new HashMap<Integer, PolyFrameLevelRate>();
			poly_frame_spr.actionList.put(poly_frame_action.action, poly_frame_action);
		}
		
		for (int l=0; l<_levels.length; l++) {
			Integer lvl = _levels[l];
			PolyFrameLevelRate poly_frame_level_rate = poly_frame_action.levelRateList.get(lvl);// 레벨에 해당하는 rate 취득
			if (poly_frame_level_rate == null) {
				poly_frame_level_rate = new PolyFrameLevelRate();
				poly_frame_level_rate.level = lvl;
				poly_frame_level_rate.rate = rates[l];
				poly_frame_action.levelRateList.put(poly_frame_level_rate.level, poly_frame_level_rate);
			}
		}
	}
	
	// 바운더리 레벨 구간 세팅(이벤트 레벨도 동일하다)
	private static Integer[] _levelToIdx;
	private static Integer[] _levels;
	private void loadLevelInformation(Document doc){
		Integer[] lvlToIdx = new Integer[128];
		Element	element	= (Element) doc.getElementsByTagName("Level").item(0);
		_levels = parsingInteger(element.getAttribute("range"), StringUtil.CommaString);
		for(int i=0; i<_levels.length; i++){
			if(i+1 >= _levels.length){
				ArrangeHelper.<Integer>setArrayValues(lvlToIdx, _levels[i], 127, i);
				break;
			}
			ArrangeHelper.<Integer>setArrayValues(lvlToIdx, _levels[i], _levels[i + 1] - 1, i);
		}
		_levelToIdx = lvlToIdx;
	}
	
	// sprite 액션 속도 세팅
	private void loadActionRates(Document doc){
		Integer[] targets = parsingNodeList(doc.getElementsByTagName("Target"));// sprite list
		NodeList nodes = ((Element)((NodeList)doc.getElementsByTagName("PolyFrameRate")).item(0)).getChildNodes();
		
		for (int i=nodes.getLength() - 1; i>=0; --i) {
			Node node = nodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType()) {
				continue;
			}
			Element 	element	= (Element)node;
			Double[] 	rates	= parsingDouble(element.getAttribute("rate"), StringUtil.CommaString);
			Integer[] 	actions = parsingInteger(element.getAttribute("action"), StringUtil.CommaString);
			
			for (Integer action : actions) {
				for (Integer sprite : targets) {
					settingFrameRate(action, sprite, rates);
				}
			}
		}
	}
	
	// 예외 속도 세팅(이동속도:난쟁이,바포메트 등)
	private void loadExceptionActionRates(Document doc){
		Integer[] exceptionSprite = parsingInteger(((Element) ((NodeList) doc.getElementsByTagName("Sprite")).item(0)).getAttribute("target"), StringUtil.CommaString);
		NodeList nodes = ((Element)((NodeList)doc.getElementsByTagName("PolyFrameException")).item(0)).getChildNodes();
		for (int i=nodes.getLength() - 1; i>=0; --i) {
			Node node = nodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType()) {
				continue;
			}
			if (!node.getNodeName().equalsIgnoreCase("Frame")) {
				continue;
			}
			Element 	element = (Element)node;
			Double[] 	rates	= parsingDouble(element.getAttribute("rate"), StringUtil.CommaString);
			Integer[] 	actions = parsingInteger(element.getAttribute("action"), StringUtil.CommaString);
			
			for (Integer action : actions) {
				for (Integer sprite : exceptionSprite) {
					settingFrameRate(action, sprite, rates);
				}
			}
			break;
		}
	}
	
	private Integer[] parsingNodeList(NodeList nodes){
		Integer[] result = new Integer[nodes.getLength()];
		for (int i=0; i<result.length; i++) {
			result[i] = Integer.parseInt(((Element)nodes.item(i)).getTextContent().trim());
		}
		return result;
	}
	
	private Integer[] parsingInteger(String str, String splitStr){
		if (StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		String[] array = str.split(splitStr);
		Integer[] result = new Integer[array.length];
		for (int i=0; i<result.length; i++) {
			result[i] = Integer.parseInt(array[i].trim());
		}
		return result;
	}
	
	private Double[] parsingDouble(String str, String splitStr){
		if (StringUtil.isNullOrEmpty(str)) {
			return null;
		}
		String[] array = str.split(splitStr);
		Double[] result = new Double[array.length];
		for (int i=0; i<result.length; i++) {
			result[i] = Double.parseDouble(array[i].trim());
		}
		return result;
	}
	
}

