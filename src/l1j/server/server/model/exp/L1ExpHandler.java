package l1j.server.server.model.exp;

import java.util.ArrayList;

import l1j.server.server.GameServerSetting;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;

/**
 * 경험치 처리 담당 핸들러
 * @author LinOffice
 */
public abstract class L1ExpHandler {
	protected final L1Character _owner;
	protected L1PcInstance _player;
	protected L1PetInstance _pet;
	
	public L1ExpHandler(L1Character owner) {
		_owner		= owner;
		if (_owner instanceof L1PcInstance) {
			_player	= (L1PcInstance)_owner;
		} else if (_owner instanceof L1PetInstance) {
			_pet	= (L1PetInstance)_owner;
		}
	}
	
	/**
	 * 캐릭터가 처지한 몬스터의 경험치를 타격자에게 분배한다.
	 * @param npc
	 * @param acquisitorList
	 * @param hateList
	 * @param exp
	 */
	public void calcExp(L1NpcInstance npc, ArrayList<?> acquisitorList, ArrayList<?> hateList, long exp) {
		try {
			if (npc == null) {
				return;
			}
			int i = 0;
			double party_level = 0, dist = 0;
			long member_exp = 0;
			int member_align = 0;

			// 헤이트의 합계를 취득
			L1Character acquisitor;
			int hate = 0, acquire_align = 0, party_align = 0, totalHateAlign = 0, partyHateAlign = 0;
			long acquire_exp = 0, party_exp = 0, partyHateExp = 0, totalHateExp = 0, ownHateExp = 0;
			if (acquisitorList.size() != hateList.size()) {
				return;
			}
			for (i = hateList.size() - 1; i >= 0; i--) {
				acquisitor = (L1Character) acquisitorList.get(i);
				hate = (Integer) hateList.get(i);
				if (acquisitor != null && acquisitor.getMapId() == npc.getMapId() && !acquisitor.isDead()) {
					totalHateExp += hate;
					if (acquisitor instanceof L1PcInstance) {
						totalHateAlign += hate;
					}
				} else { // null였거나 죽어 있으면(자) 배제
					acquisitorList.remove(i);
					hateList.remove(i);
				}
			}
			if (totalHateExp == 0) {
				return;// 취득자가 없는 경우
			}

			if (npc != null && !(npc instanceof L1PetInstance) && !(npc instanceof L1SummonInstance)) {
				if (!GameServerSetting.PROCESSING_CONTRIBUTION_TOTAL && _player.getHomeTownId() > 0) {
					_player.addContribution(npc.getLevel() / 10);
				}
				int align = npc.getAlignment();
				if (_player.isInParty()) {// 파티중
					// 파티의 헤이트의 합계를 산출
					// 파티 멤버 이외에는 그대로 배분
					partyHateExp = 0;
					partyHateAlign = 0;
					for (i = hateList.size() - 1; i >= 0; i--) {
						acquisitor = (L1Character) acquisitorList.get(i);
						hate = (Integer) hateList.get(i);
						if (acquisitor instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) acquisitor;
							if (pc == _player) {
								partyHateExp += hate;
								partyHateAlign += hate;
							} else if (_player.getParty().isMember(pc)) {
								partyHateExp += hate;
								partyHateAlign += hate;
							} else {
								if (totalHateExp > 0) {
									acquire_exp = (exp * hate / totalHateExp);
								}
								if (totalHateAlign > 0) {
									acquire_align = (align * hate / totalHateAlign);
								}
								pc.getExpHandler().addExp(acquire_exp, acquire_align);
							}
						} else if (acquisitor instanceof L1PetInstance) {
							L1PetInstance pet = (L1PetInstance) acquisitor;
							L1PcInstance master = (L1PcInstance) pet.getMaster();
							if (master == _player) {
								partyHateExp += hate;
							} else if (_player.getParty().isMember(master)) {
								partyHateExp += hate;
							} else {
								if (totalHateExp > 0) {
									acquire_exp = (exp * hate / totalHateExp);
								}
								pet.getExpHandler().addExp(acquire_exp);
							}
						} else if (acquisitor instanceof L1SummonInstance) {
							L1SummonInstance summon = (L1SummonInstance) acquisitor;
							L1PcInstance master = (L1PcInstance) summon.getMaster();
							if (master == _player) {
								partyHateExp += hate;
							} else if (_player.getParty().isMember(master)) {
								partyHateExp += hate;
							}
						}
					}
					if (totalHateExp > 0) {
						party_exp = (exp * partyHateExp / totalHateExp);
					}
					if (totalHateAlign > 0) {
						party_align = (align * partyHateAlign / totalHateAlign);
					}

					// 프리보나스
					double pri_bonus = 0;
					L1PcInstance leader = _player.getParty().getLeader();
					if (leader.isCrown() && (_player.knownsObject(leader) || _player.equals(leader))) {
						pri_bonus = 0.059D;
					}

					// 파티 경험치의 계산
					L1PcInstance[] ptMembers = _player.getParty().getMembersArray();
					double pt_bonus = 0;
					for (L1PcInstance each : ptMembers) {
						boolean screen = _player.knownsObject(each);
						if (screen || _player.equals(each)) {
							party_level += each.getLevel() * each.getLevel();
						}
						if (screen) {
							pt_bonus += 0.01D;
						}
					}
					party_exp = (long) (party_exp * (1 + pt_bonus + pri_bonus));
					// 자캐릭터와 그 애완동물·서먼의 헤이트의 합계를 산출
					if (party_level > 0) {
						dist = ((_player.getLevel() * _player.getLevel()) / party_level);
					}
					member_exp = (long) (party_exp * dist);
					member_align = (int) (party_align * dist);
					ownHateExp = 0;
					for (i=hateList.size()-1; i>=0; i--) {
						acquisitor = (L1Character) acquisitorList.get(i);
						hate = (Integer) hateList.get(i);
						if (acquisitor instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) acquisitor;
							if (pc == _player) {
								ownHateExp += hate;
							}
						} else if (acquisitor instanceof L1PetInstance) {
							L1PetInstance pet = (L1PetInstance) acquisitor;
							L1PcInstance master = (L1PcInstance) pet.getMaster();
							if (master == _player) {
								ownHateExp += hate;
							}
						} else if (acquisitor instanceof L1SummonInstance) {
							L1SummonInstance summon = (L1SummonInstance) acquisitor;
							L1PcInstance master = (L1PcInstance) summon.getMaster();
							if (master == _player) {
								ownHateExp += hate;
							}
						}
					}
					// 자캐릭터와 그 애완동물·서먼에 분배
					if (ownHateExp != 0) {// 공격에 참가하고 있었다
						for (i=hateList.size()-1; i>=0; i--) {
							acquisitor = (L1Character) acquisitorList.get(i);
							hate = (Integer) hateList.get(i);
							if (acquisitor instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) acquisitor;
								if (pc == _player) {
									if (ownHateExp > 0) {
										acquire_exp = (member_exp * hate / ownHateExp);
									}
									pc.getExpHandler().addExp(acquire_exp, member_align);
								}
							} else if (acquisitor instanceof L1SummonInstance) {
								L1SummonInstance summon = (L1SummonInstance) acquisitor;
								L1PcInstance master = (L1PcInstance) summon.getMaster();
								if (master == _player) {
									if (ownHateExp > 0) {
										acquire_exp = (member_exp * hate / ownHateExp);
									}
									master.getExpHandler().addExp(acquire_exp, 0);
								}
							} else if (acquisitor instanceof L1PetInstance) {
								L1PetInstance pet = (L1PetInstance) acquisitor;
								L1PcInstance master = (L1PcInstance) pet.getMaster();
								if (master == _player) {
									if (ownHateExp > 0) {
										acquire_exp = (member_exp * hate / ownHateExp);
									}
									pet.getExpHandler().addExp(acquire_exp);
								}
							}
						}
					} else {// 공격에 참가하고 있지 않았다
						_player.getExpHandler().addExp(member_exp, member_align);
					}
					// 파티 멤버와 그 애완동물·서먼의 헤이트의 합계를 산출
					for (int cnt = 0; cnt < ptMembers.length; cnt++) {
						if (_player.knownsObject(ptMembers[cnt])) {
							if(party_level > 0)dist = ((ptMembers[cnt].getLevel() * ptMembers[cnt].getLevel()) / party_level);
							member_exp = (long) (party_exp * dist);
							member_align = (int) (party_align * dist);
							ownHateExp = 0;
							for (i = hateList.size() - 1; i >= 0; i--) {
								acquisitor = (L1Character) acquisitorList.get(i);
								hate = (Integer) hateList.get(i);
								if (acquisitor instanceof L1PcInstance) {
									L1PcInstance pc = (L1PcInstance) acquisitor;
									if (pc == ptMembers[cnt]) {
										ownHateExp += hate;
									}
								} else if (acquisitor instanceof L1PetInstance) {
									L1PetInstance pet = (L1PetInstance) acquisitor;
									L1PcInstance master = (L1PcInstance) pet.getMaster();
									if (master == ptMembers[cnt]) {
										ownHateExp += hate;
									}
								} else if (acquisitor instanceof L1SummonInstance) {
									L1SummonInstance summon = (L1SummonInstance) acquisitor;
									L1PcInstance master = (L1PcInstance) summon.getMaster();
									if (master == ptMembers[cnt]) {
										ownHateExp += hate;
									}
								}
							}
							// 파티 멤버와 그 애완동물·서먼에 분배
							if (ownHateExp != 0) {// 공격에 참가하고 있었다
								for (i=hateList.size()-1; i>=0; i--) {
									acquisitor = (L1Character) acquisitorList.get(i);
									hate = (Integer) hateList.get(i);
									if (acquisitor instanceof L1PcInstance) {
										L1PcInstance pc = (L1PcInstance) acquisitor;
										if (pc == ptMembers[cnt]) {
											if (ownHateExp > 0) {
												acquire_exp = (member_exp * hate / ownHateExp);
											}
											pc.getExpHandler().addExp(acquire_exp, member_align);
										}
									} else if (acquisitor instanceof L1SummonInstance) {
										L1SummonInstance summon = (L1SummonInstance) acquisitor;
										L1PcInstance master = (L1PcInstance) summon.getMaster();
										if (master == ptMembers[cnt]) {
											if (ownHateExp > 0) {
												acquire_exp = (member_exp * hate / ownHateExp);
											}
											master.getExpHandler().addExp(acquire_exp, 0);
										}
									} else if (acquisitor instanceof L1PetInstance) {
										L1PetInstance pet = (L1PetInstance) acquisitor;
										L1PcInstance master = (L1PcInstance) pet.getMaster();
										if (master == ptMembers[cnt]) {
											if (ownHateExp > 0) {
												acquire_exp = (member_exp * hate / ownHateExp);
											}
											pet.getExpHandler().addExp(acquire_exp);
										}
									}
								}
							} else {// 공격에 참가하고 있지 않았다
								ptMembers[cnt].getExpHandler().addExp(member_exp, member_align);
							}
						}
					}
				} else {// 파티를 짜지 않았다
					// EXP, 로우훌의 분배
					for (i=hateList.size()-1; i>=0; i--) {
						acquisitor = (L1Character) acquisitorList.get(i);
						hate = (Integer) hateList.get(i);
						acquire_exp = (exp * hate / totalHateExp);
						if (acquisitor instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) acquisitor;
							if (totalHateAlign > 0) {
								acquire_align = (align * hate / totalHateAlign);
							}
							pc.getExpHandler().addExp(acquire_exp, acquire_align);
						} else if (acquisitor instanceof L1SummonInstance) {
							L1SummonInstance summon = (L1SummonInstance) acquisitor;
							L1PcInstance master = (L1PcInstance) summon.getMaster();
							if (master != null) {
								master.getExpHandler().addExp(acquire_exp, 0);
							}
						} else if (acquisitor instanceof L1PetInstance) {
							L1PetInstance pet = (L1PetInstance) acquisitor;
							pet.getExpHandler().addExp(acquire_exp);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 경험치와 라우풀을 추가한다.
	 * @param exp
	 * @param align
	 */
	public abstract void addExp(long exp, int align);
	
	/**
	 * 경험치를 추가한다.
	 * @param exp
	 */
	public abstract void addExp(long exp);
}

