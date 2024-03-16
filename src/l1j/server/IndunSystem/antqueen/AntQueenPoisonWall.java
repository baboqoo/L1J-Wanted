package l1j.server.IndunSystem.antqueen;

import l1j.server.Config;
import l1j.server.server.construct.L1CharacterInfo;
import l1j.server.server.construct.L1CharacterInfo.L1Class;
import l1j.server.server.model.L1EffectSpawn;

/**
 * 여왕개미 은신처 대미지 독 생성 쓰레드
 * @author LinOffice
 */
public class AntQueenPoisonWall implements Runnable {
	private int _x_space, _z_space;
	private AntQueen _ant;
	private L1EffectSpawn _spawn;

	protected AntQueenPoisonWall() {
		_x_space = _z_space = 8;
		_ant	= AntQueen.getInstance();
		_spawn	= L1EffectSpawn.getInstance();
	}

	@Override
	public void run() {
		int[][] cpos = new int[4][2];
		int nextTime	= 600000;
		try {
			for (int width = 96; width >= 0; width -= _z_space) {
				if(!_ant.running)return;
				int left		= 32830 - width;
				int top			= 32830 - width;
				int right		= 32830 + width;
				int bottom		= 32830 + width;
				int endTime		= _ant._timer;

				AntQueenUtil.sendAllMessage("$31981");// 여왕개미의 의지가 발휘되고 있습니다. 경계의 변화에 대비하세요.
				Thread.sleep(10000L);
				
				for (int i = 0; i < width << 1; i += _x_space) {
					cpos[0][0] = left + i;
					cpos[0][1] = top;

					cpos[1][0] = right;
					cpos[1][1] = top + i + _x_space;

					cpos[2][0] = right - i - _x_space;
					cpos[2][1] = bottom;

					cpos[3][0] = left;
					cpos[3][1] = bottom - i;
					if (width > 15) {
						for (int j = 0; j < 4; j++) {
							int effect_id = (j == 0 || j == 2) ? 5191 : 5190;
							if (Config.DUNGEON.ANT_QUEEN_INCLUDE) {
								_spawn.spawnEffect(effect_id, nextTime, cpos[j][0], cpos[j][1], (short) 15881);// 벽
							} else {
								for (int k=0; k<L1CharacterInfo.CLASS_SIZE; k++) {
									int mapId = k == L1Class.LANCER.getType() ? 15901 : 15881+k;
									_spawn.spawnEffect(effect_id, nextTime, cpos[j][0], cpos[j][1], (short) mapId);// 벽
								}
							}
						}
					}
				}
				
				for (int i = 0; i < width << 2; i += _x_space) {
					cpos[0][0] = left + i;
					cpos[0][1] = top;

					cpos[1][0] = right;
					cpos[1][1] = top + i;

					cpos[2][0] = right - i;
					cpos[2][1] = bottom + _x_space;

					cpos[3][0] = left - _x_space;
					cpos[3][1] = bottom - i;
					if (width > 15) {
						for (int j = 0; j < 4; j++) {
							if (Config.DUNGEON.ANT_QUEEN_INCLUDE) {
								_spawn.spawnEffect(7800057, endTime, cpos[j][0] + 5, cpos[j][1] - 5, (short) 15881);// 대미지 독 필드
							} else {
								for (int k=0; k<L1CharacterInfo.CLASS_SIZE; k++) {
									int mapId = k == L1Class.LANCER.getType() ? 15901 : 15881+k;
									_spawn.spawnEffect(7800057, endTime, cpos[j][0] + 5, cpos[j][1] - 5, (short) mapId);// 대미지 독 필드
								}
							}
						}
					}
				}
				AntQueenUtil.sendAllMessage("$31982");// 여왕개미의 의지가 관철되었습니다. 경계가 변화되었습니다.
				Thread.sleep(nextTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

