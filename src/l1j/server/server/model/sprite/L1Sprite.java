package l1j.server.server.model.sprite;

import l1j.server.server.model.sprite.AcceleratorChecker.ACT_TYPE;
import l1j.server.server.utils.StringUtil;

/**
 * 이미지 정보
 * @author LinOffice
 */
public class L1Sprite {
	private static final double BASIC_MILLIS		= 1000.0D;
	
	protected int sprite_id;						// 이미지 번호
	protected int shadow;							// 그림자
	protected int type;								// 타입
	protected int attr;								// 속성
	protected int width;							// 사이즈
	protected int height;							// 사이즈
	protected int flying_type;						// 하늘 날기 타입
	protected int action_count;						// 액션 수
	protected L1SpriteAction action;				// 액션 정보
	protected PolyFrameSprite poly_frame_sprite;	// 프레임 정보
	
	public L1Sprite(int sprite_id, int shadow, int type, int attr, int width, int height, int flying_type, int action_count) {
		this.sprite_id		= sprite_id;
		this.shadow			= shadow;
		this.type			= type;
		this.attr			= attr;
		this.width			= width;
		this.height			= height;
		this.flying_type	= flying_type;
		this.action_count	= action_count;
		this.action			= new L1SpriteAction();
	}

	/**
	 * 이미지 넓이
	 * @return width
	 */
	public int get_width() {
		return width;
	}

	/**
	 * 이미지 높이
	 * @return height
	 */
	public int get_height() {
		return height;
	}
	
	/**
	 * sprite, action, level에 따른 frame에 대한 속도를 계산하는 메소드(polyframe.xml의 설정된 배율를 계산한다)
	 * @param action_id 조사하는 액션
	 * @param boundaryLevel 유저의 현재 바운더리 레벨 인덱스
	 * @param type 조사하는 타입
	 * @return double 반환할 속도 배율
	 */
	public double getActionSpeed(int action_id, int boundaryLevel, ACT_TYPE type){
		try {
			if (action == null) {
				return getActionSpeedDefault(action_id, type);
			}
			int frameCount = action.frame_count.get(action_id);// original framCount
			if (frameCount <= 0) {
				return getActionSpeedDefault(action_id, type);
			}
			if (poly_frame_sprite == null) {// poly_frame_sprite check
				return getActionSpeedDefault(action_id, type);
			}
			PolyFrameAction poly_frame_action = poly_frame_sprite.actionList.get(action_id);// poly_frame_action check
			if (poly_frame_action == null) {
				return getActionSpeedDefault(action_id, type);
			}
			PolyFrameLevelRate poly_frmae_level_rate = poly_frame_action.levelRateList.get(boundaryLevel);// poly_frame_level_rate check
			if (poly_frmae_level_rate == null) {
				return getActionSpeedDefault(action_id, type);
			}
			return calcActionSpeed(frameCount, poly_frmae_level_rate.rate);// calc
		} catch(Exception e) {
			return 0D;
		}
	}
	
	/**
	 * total action speed Exception
	 * @param action_id
	 * @param boundaryLevel
	 * @return double
	 */
	public double getActionSpeed(int action_id, int boundaryLevel){
		try {
			if (action == null) {
				return getActionSpeed(action_id);
			}
			int frameCount = action.frame_count.get(action_id);// original framCount
			if (frameCount <= 0) {
				return getActionSpeed(action_id);
			}
			if (poly_frame_sprite == null) {// poly_frame_sprite check
				return getActionSpeed(action_id);
			}
			PolyFrameAction poly_frame_action = poly_frame_sprite.actionList.get(action_id);// poly_frame_action check
			if (poly_frame_action == null) {
				return getActionSpeed(action_id);
			}
			PolyFrameLevelRate poly_frmae_level_rate = poly_frame_action.levelRateList.get(boundaryLevel);// poly_frame_level_rate check
			if (poly_frmae_level_rate == null) {
				return getActionSpeed(action_id);
			}
			return calcActionSpeed(frameCount, poly_frmae_level_rate.rate);// calc
		} catch(Exception e) {
			return 0D;
		}
	}
	
	/**
	 * total action speed
	 * @param action_id
	 * @return int
	 */
	public int getActionSpeed(int action_id){
		if (action == null || !action.action_speed.containsKey(action_id)) {
			return 0;
		}
		return action.action_speed.get(action_id);
	}
	
	/**
	 * frame에 대한 default 속도 반환
	 * @param sprite_id
	 * @param action_id
	 * @param type
	 * @return double
	 */
	public double getActionSpeedDefault(int action_id, ACT_TYPE type){
		return type == ACT_TYPE.ATTACK ? getAttackSpeed(action_id) : type == ACT_TYPE.MOVE ? getMoveSpeed(action_id) : 0;
	}
	
	/**
	 * 공격 속도 조사
	 * @param action_id - 무기의 종류를 나타내는 값. L1Item.getType1()의 변환값 +1과 일치한다
	 * @return 공격 속도(ms)
	 */
	public int getAttackSpeed(int action_id){
		if (action == null || !action.attack_speed.containsKey(action_id)) {
			return 0;
		}
		return action.attack_speed.get(action_id);
	}
	
	/**
	 * 이동 속도 조사
	 * @param action_id
	 * @return 이동 속도(ms)
	 */
	public int getMoveSpeed(int action_id) {
		if (action == null || !action.move_speed.containsKey(action_id)) {
			return 0;
		}
		return action.move_speed.get(action_id);
	}
	
	/**
	 * 공격 스펠 속도 조사
	 * @return 공격 스펠 속도(ms)
	 */
	public int getDirSpellSpeed() {
		if (action == null) {
			return 0;
		}
		return action.dir_spell_speed;
	}
	
	/**
	 * 버프 스펠 속도 조사
	 * @return 버프 스펠 속도(ms)
	 */
	public int getNodirSpellSpeed() {
		if (action == null) {
			return 0;
		}
		return action.nodir_spell_speed;
	}
	
	/**
	 * 대미지 액션 속도 조사
	 * @param action_id
	 * @return 대미지 액션 속도(ms)
	 */
	public int getDamageSpeed(int action_id) {
		if (action == null || !action.damage_speed.containsKey(action_id)) {
			return 0;
		}
		return action.damage_speed.get(action_id);
	}
	
	/**
	 * 아이템 줍기 속도 조사
	 * @return 아이템 줍기 속도(ms)
	 */
	public int getPickUpSpeed() {
		if (action == null) {
			return 0;
		}
		return action.pickup_speed;
	}
	
	/**
	 * 막대 사용 속도 조사
	 * @return 막대 사용 속도(ms)
	 */
	public int getWandSpeed() {
		if (action == null) {
			return 0;
		}
		return action.wand_speed;
	}
	
	/**
	 * 프레임수와 frame rate로부터 액션의 합계 시간(ms)을 계산해 돌려준다.
	 */
	protected int calcActionSpeed(double frameCount, double frameRate) {
		return (int)(fromRPS(frameCount, calcRps(frameRate)));
	}
	
	protected double fromRPS(double frameCount, double rps){
		return frameCount * rps;
	}
	
	protected double calcRps(double frameRate){
		return calcRps(BASIC_MILLIS, frameRate);
	}
	
	protected double calcRps(double basic_millis, double rate){
		return basic_millis / rate;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("----- SPRITE_ID >>> ").append(sprite_id).append(" START -----\r\n");
		sb.append("SHADOW: ").append(shadow).append(StringUtil.LineString);
		sb.append("TYPE: ").append(type).append(StringUtil.LineString);
		sb.append("ATTR: ").append(attr).append(StringUtil.LineString);
		sb.append("WIDTH: ").append(width).append(StringUtil.LineString);
		sb.append("HEIGHT: ").append(height).append(StringUtil.LineString);
		sb.append("FLYING_TYPE: ").append(flying_type).append(StringUtil.LineString);
		if (action != null) {
			sb.append("ACTIONS ----->>\r\n");
			for (java.util.Map.Entry<Integer, Integer> entry : action.action_speed.entrySet()) {
				sb.append("ID: ").append(entry.getKey()).append(" / SPEED: ").append(entry.getValue()).append(StringUtil.LineString);
			}
		}
		sb.append("------------ END --------------");
		return sb.toString();
	}
}

