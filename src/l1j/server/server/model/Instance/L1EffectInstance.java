package l1j.server.server.model.Instance;

import java.util.Random;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.construct.L1RegionStatus;
import l1j.server.server.controller.action.War;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.skill.L1BuffUtil;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.action.S_DoActionGFX;
import l1j.server.server.serverpackets.object.S_EffectObject;
import l1j.server.server.templates.L1Npc;

public class L1EffectInstance extends L1NpcInstance {
	private static final Random random			= new Random(System.nanoTime());
	private static final long serialVersionUID	= 1L;
	private static final long DAMAGE_INTERVAL	= 1000L;

	public L1EffectInstance(L1Npc template) {
		super(template);
		switch(getNpcTemplate().getNpcId()){
		case 81157:
			GeneralThreadPool.getInstance().execute(new FwDamageTimer(this));
			break;
		case 7800057:
			GeneralThreadPool.getInstance().execute(new DamageField());
			break;
		case 7800056:
			GeneralThreadPool.getInstance().execute(new DamageFire());
			break;
		case 900521:
			GeneralThreadPool.getInstance().execute(new zakenImunTrap());
			break;
		default:break;
		}
	}
	
	S_EffectObject objPck;
	
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		if (objPck == null) {
			objPck = new S_EffectObject(this);
		}
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(objPck);
	}

	@Override
	public void onAction(L1PcInstance pc) {}

	@Override
	public void deleteMe() {
		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		allTargetClear();
		_master = null;
		if (objPck != null) {
			objPck.clear();
			objPck = null;
		}
		L1World world = L1World.getInstance();
		world.removeVisibleObject(this);
		world.removeObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			if (pc == null) {
				continue;
			}
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this), true);
		}
		removeAllKnownObjects();
	}

	class FwDamageTimer implements Runnable {
		private L1EffectInstance _effect;

		public FwDamageTimer(L1EffectInstance effect) {
			_effect = effect;
		}

		@Override
		public void run() {
			if (_effect == null) {
				return;
			}
			L1PcInstance pc			= null;
			L1Magic magic			= null;
			L1MonsterInstance mob	= null;
			try {
				for (L1Object objects : L1World.getInstance().getVisibleObjects(_effect, 0)) {
					if (objects instanceof L1PcInstance) {
						pc			= (L1PcInstance) objects;
						if (pc.isDead()) {
							continue;
						}
						if (pc.getRegion() == L1RegionStatus.SAFETY) {
							boolean isNowWar	= false;
							int castleId		= L1CastleLocation.getCastleIdByArea(pc);
							if (castleId > 0) {
								isNowWar = War.getInstance().isNowWar(castleId);
							}
							if (!isNowWar) {
								continue;
							}
						}
						magic		= new L1Magic(_effect, pc);
						int damage	= magic.calcNpcFireWallDamage();
						if (damage == 0) {
							continue;
						}
						pc.broadcastPacketWithMe(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage), true);
						pc.receiveDamage(_effect, damage);
					} else if (objects instanceof L1MonsterInstance) {
						mob			= (L1MonsterInstance) objects;
						if (mob.isDead()) {
							continue;
						}
						magic		= new L1Magic(_effect, mob);
						int damage	= magic.calcNpcFireWallDamage();
						if (damage == 0) {
							continue;
						}
						mob.broadcastPacket(new S_DoActionGFX(mob.getId(), ActionCodes.ACTION_Damage), true);
						mob.receiveDamage(_effect, damage);
					}
				}
				if (!_destroyed) {
					GeneralThreadPool.getInstance().schedule(this, DAMAGE_INTERVAL);
				}
			} catch (Exception e) {
			}
		}
	}
	
	class DamageField implements Runnable {
		@Override
		public void run() {
			if (_destroyed || isDead()) {
				return;
			}
			try {
				for (L1PcInstance pc : L1World.getInstance().getVisibleBoxPlayer(L1EffectInstance.this, 1, 5, 5)) {
					if (pc == null || pc.isDead() || pc.isGhost()) {
						continue;
					}
					pc.receiveDamage(L1EffectInstance.this, random.nextInt(30) + 30);
				}
				GeneralThreadPool.getInstance().schedule(this, DAMAGE_INTERVAL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class DamageFire implements Runnable {
		@Override
		public void run() {
			if (_destroyed || isDead()) {
				return;
			}
			try {
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(L1EffectInstance.this, 2)) {
					if (pc == null || pc.isDead() || pc.isGhost()) {
						continue;
					}
					pc.broadcastPacketWithMe(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage), true);
					pc.receiveDamage(L1EffectInstance.this, random.nextInt(50) + 50);
				}
				GeneralThreadPool.getInstance().schedule(this, DAMAGE_INTERVAL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	class zakenImunTrap implements Runnable {
		@Override
		public void run() {
			if (_destroyed || isDead()) {
				return;
			}
			try {
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(L1EffectInstance.this, 1)) {
					if (pc == null || pc.isDead() || pc.isGhost()) {
						continue;
					}
					L1BuffUtil.skillAction(pc, L1SkillId.IMMUNE_TO_HARM);
				}
				GeneralThreadPool.getInstance().schedule(this, DAMAGE_INTERVAL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}

