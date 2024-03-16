package l1j.server.server.model;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.templates.L1Npc;

public class L1EffectSpawn {
	private static final Logger _log = Logger.getLogger(L1EffectSpawn.class.getName());

	private static L1EffectSpawn _instance;

	private Constructor<?> _constructor;

	private L1EffectSpawn() {}

	public static L1EffectSpawn getInstance() {
		if (_instance == null) {
			_instance = new L1EffectSpawn();
		}
		return _instance;
	}

	public L1EffectInstance spawnEffect(int npcid, int time, int locX, int locY, short mapId, Object...o) {
		L1Npc template = NpcTable.getInstance().getTemplate(npcid);
		L1EffectInstance effect = null;

		if (template == null) {
			return null;
		}
		String className = (new StringBuilder()).append("l1j.server.server.model.Instance.").append(template.getImpl()).append("Instance").toString();
		try {
			_constructor = Class.forName(className).getConstructors()[0];
			Object obj[] = { template };
			effect = (L1EffectInstance) _constructor.newInstance(obj);
			effect.setId(IdFactory.getInstance().nextId());
			effect.setSpriteId(template.getSpriteId());
			effect.setX(locX);
			effect.setY(locY);
			effect.setHomeX(locX);
			effect.setHomeY(locY);
			effect.getMoveState().setHeading(0);
			effect.setMap(mapId);
			
			L1World world = L1World.getInstance();
			world.storeObject(effect);
			world.addVisibleObject(effect);
			for (L1PcInstance pc : world.getRecognizePlayer(effect)) {
				effect.onPerceive(pc);
			}
			if (time > 0) {
				new L1NpcDeleteTimer(effect, time).begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return effect;
	}
	
	public L1EffectInstance spawnEffect(int npcid, int time, int locX, int locY, short mapId) {
		L1Npc template = NpcTable.getInstance().getTemplate(npcid);
		L1EffectInstance effect = null;

		if (template == null) {
			return null;
		}
		String className = (new StringBuilder()).append("l1j.server.server.model.Instance.").append(template.getImpl()).append("Instance").toString();
		try {
			_constructor = Class.forName(className).getConstructors()[0];
			Object obj[] = { template };
			effect = (L1EffectInstance) _constructor.newInstance(obj);
			effect.setId(IdFactory.getInstance().nextId());
			effect.setSpriteId(template.getSpriteId());
			effect.setX(locX);
			effect.setY(locY);
			effect.setHomeX(locX);
			effect.setHomeY(locY);
			effect.getMoveState().setHeading(0);
			effect.setMap(mapId);
			
			L1World world = L1World.getInstance();
			world.storeObject(effect);
			world.addVisibleObject(effect);
		
			for (L1PcInstance pc : world.getRecognizePlayer(effect)) {
				effect.onPerceive(pc);
			}
			if (time > 0) {
				new L1NpcDeleteTimer(effect, time).begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return effect;
	}

	public void doSpawnFireWall(L1Character cha, int targetX, int targetY) {
		L1Npc firewall = NpcTable.getInstance().getTemplate(81157);
		int duration = SkillsTable.getTemplate(L1SkillId.FIRE_WALL).getBuffDuration();
		if (firewall == null)
			throw new NullPointerException("FireWall data not found:npcid=81157");
		L1Character base = cha;
		for (int i = 0; i < 8; i++) {
			int a = base.targetDirection(targetX, targetY);
			int x = base.getX();
			int y = base.getY();
			switch(a){
			case 1: x++; y--; break;
			case 2: x++; break;
			case 3: x++; y++; break;
			case 4: y++; break;
			case 5: x--; y++; break;
			case 6: x--; break;
			case 7: x--; y--; break;
			case 0: y--; break;
			default: break;
			}
			if (!base.isAttackPosition(x, y, 1,false)) {
				x = base.getX();
				y = base.getY();
			}
			L1Map map = L1WorldMap.getInstance().getMap(cha.getMapId());
			if (!map.isAttackable(x, y, cha.getMoveState().getHeading(), false)) {
				break;
			}
			L1EffectInstance effect = spawnEffect(81157, duration * 1000, x, y,	cha.getMapId());
			if (effect == null) {
				break;
			}
			L1EffectInstance npc = null;
			for (L1Object objects : L1World.getInstance().getVisibleObjects(effect, 0)) {
				if (objects instanceof L1EffectInstance) {
					npc = (L1EffectInstance) objects;
					if (npc.getNpcTemplate().getNpcId() == 81157) {
						npc.deleteMe();
					}
				}
			}
			if (targetX == x && targetY == y) {
				break;
			}
			base = effect;
		}
	}
	
}

