package l1j.server.server.model.sprite;

import java.util.HashMap;

/**
 * 이미지의 액션 정보
 * @author LinOffice
 */
public class L1SpriteAction {
	protected final HashMap<Integer, Integer> frame_count;// original action frame
	protected final HashMap<Integer, Integer> action_speed;// all action speed
	protected final HashMap<Integer, Integer> move_speed;
	protected final HashMap<Integer, Integer> attack_speed;
	protected final HashMap<Integer, Integer> damage_speed;
	protected int nodir_spell_speed;
	protected int dir_spell_speed;
	protected int pickup_speed;
	protected int wand_speed;
	
	protected L1SpriteAction() {
		frame_count			= new HashMap<Integer, Integer>();
		action_speed		= new HashMap<Integer, Integer>();
		move_speed			= new HashMap<Integer, Integer>();
		attack_speed		= new HashMap<Integer, Integer>();
		damage_speed		= new HashMap<Integer, Integer>();
		nodir_spell_speed	= dir_spell_speed = wand_speed = 1200;
		pickup_speed		= 400;
	}
	
	protected void put_frame_count(int action_id, int count) {
		frame_count.put(action_id, count);
	}
	
	protected void put_action_speed(int action_id, int speed) {
		action_speed.put(action_id, speed);
	}
	
	protected void put_move_speed(int action_id, int speed) {
		move_speed.put(action_id, speed);
	}
	
	protected void put_attack_speed(int action_id, int speed) {
		attack_speed.put(action_id, speed);
	}
	
	protected void put_damage_speed(int action_id, int speed) {
		damage_speed.put(action_id, speed);
	}
	
	protected void set_nodir_spell_speed(int speed) {
		nodir_spell_speed = speed;
	}
	
	protected void set_dir_spell_speed(int speed) {
		dir_spell_speed = speed;
	}
	
	protected void set_pickup_speed(int speed) {
		pickup_speed = speed;
	}
	
	protected void set_wand_speed(int speed) {
		wand_speed = speed;
	}
	
}

