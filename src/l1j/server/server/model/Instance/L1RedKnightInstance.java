package l1j.server.server.model.Instance;

import l1j.server.server.ActionCodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.action.S_AttackPacket;
import l1j.server.server.serverpackets.action.S_ChangeHeading;
import l1j.server.server.serverpackets.object.S_NPCObject;
import l1j.server.server.templates.L1Npc;

/**
 * 훈련소 붉은 기사단 인스턴스
 * @author LinOffice
 */
public class L1RedKnightInstance extends L1MerchantInstance {
	private static final long serialVersionUID = 1L;
	
	protected boolean isAction;
	protected boolean isPerceive;
	protected boolean isLink;
	
	public L1RedKnightInstance(L1Npc template) {
		super(template);
		int npcId	= template.getNpcId();
		isAction	= npcId == 5122
				|| npcId == 202077
				|| npcId == 202089
				|| npcId == 202084
				|| npcId == 202090
				|| npcId == 202079
				|| npcId == 202080;
		isPerceive	= npcId == 5123
				|| npcId == 5124
				|| npcId == 5127
				|| npcId == 202086
				|| npcId == 202087
				|| npcId == 202100
				|| npcId == 5122
				|| npcId == 202077
				|| npcId == 202089
				|| npcId == 202084
				|| npcId == 202090
				|| npcId == 202079
				|| npcId == 202080;
		isLink		= npcId == 5123
				|| npcId == 5124
				|| npcId == 5127
				|| npcId == 202086
				|| npcId == 202087
				|| npcId == 202100;
	}
	
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		if (perceivedFrom == null || this == null) {
			return;
		}
		perceivedFrom.addKnownObject(this);
		perceivedFrom.sendPackets(new S_NPCObject(this), true);
		if (isPerceive) {
			onNpcAI();
		}
	}
	
	@Override
	public void searchTarget() {
		if (!isLink) {
			return;
		}
		L1MerchantInstance targetMer	= null;
		L1ScarecrowInstance targetCrow	= null;
		
		L1Character cha = getSearchTarget();
		if (cha instanceof L1MerchantInstance) {
			L1MerchantInstance mer = (L1MerchantInstance) cha;
			if (mer != null) {
				targetMer = mer;
			}
		} else if (cha instanceof L1ScarecrowInstance) {
			L1ScarecrowInstance crow = (L1ScarecrowInstance) cha;
			if (crow != null) {
				targetCrow = crow;
			}
		}
		
		if (targetMer != null) {
			_hateList.add(targetMer, 0);
			_target = targetMer;
		}
		
		if (targetCrow != null) {
			_hateList.add(targetCrow, 0);
			_target = targetCrow;
		}
	}
	
	private L1Character getSearchTarget(){
		L1Character cha = null;
		if (getNpcTemplate().getNpcId() == 5123) { // 우드백 창
			if(getX() == 32601 && getY() == 33234){
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1ScarecrowInstance) {
						L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
						if (crow != null && crow.getX() == 32603 && crow.getY() == 33234) {
							cha = crow;
							break;
						}
					}
				}
			} else if (getX() == 32601 && getY() == 33236) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1ScarecrowInstance) {
						L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
						if (crow != null && crow.getX() == 32603 && crow.getY() == 33236) {
							cha = crow;
							break;
						}
					}
				}
			}
		} else if (getNpcTemplate().getNpcId() == 5124) { // 우드백 검
			if ((getX() == 32615 && getY() == 33229) || (getX() == 32617 && getY() == 33229)) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1ScarecrowInstance) {
						L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
						if (crow != null && crow.getX() == 32616 && crow.getY() == 33229) {
							cha = crow;
							break;
						}
					}
				}
			} else if ((getX() == 32615 && getY() == 33232) || (getX() == 32617 && getY() == 33232)) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1ScarecrowInstance) {
						L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
						if (crow != null && crow.getX() == 32616 && crow.getY() == 33232) {
							cha = crow;
							break;
						}
					}
				}
			} else if ((getX() == 32615 && getY() == 33235) || (getX() == 32617 && getY() == 33235)) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1ScarecrowInstance) {
						L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
						if (crow != null && crow.getX() == 32616 && crow.getY() == 33235) {
							cha = crow;
							break;
						}
					}
				}
			}
		} else if (getNpcTemplate().getNpcId() == 5127) { // 우드백 활
			if (getX() == 32601 && getY() == 33220) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1ScarecrowInstance) {
						L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
						if (crow != null && crow.getNpcTemplate().getNpcId() == 5214 && crow.getX() == 32601 && crow.getY() == 33214) {
							cha = crow;
							break;
						}
					}
				}
			} else if (getX() == 32598 && getY() == 33220) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1ScarecrowInstance) {
						L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
						if (crow != null && crow.getNpcTemplate().getNpcId() == 5214 && crow.getX() == 32598 && crow.getY() == 33214) {
							cha = crow;
							break;
						}
					}
				}
			} else if (getX() == 32595 && getY() == 33220) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1ScarecrowInstance) {
						L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
						if (crow != null && crow.getNpcTemplate().getNpcId() == 5214 && crow.getX() == 32595 && crow.getY() == 33214) {
							cha = crow;
							break;
						}
					}
				}
			}
		} else if (getNpcTemplate().getNpcId() == 202086) { // 훈련소 검
			if (getX() == 32610 && getY() == 32817) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1MerchantInstance) {
						L1MerchantInstance mer = (L1MerchantInstance) obj;
						if (mer != null && mer.getNpcTemplate().getNpcId() == 202087 && mer.getX() == 32610 && mer.getY() == 32818) {
							cha = mer;
							break;
						}
					}
				}
			} else if (getX() == 32597 && getY() == 32819) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1ScarecrowInstance) {
						L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
						if (crow != null && crow.getNpcTemplate().getNpcId() == 202097 && crow.getX() == 32597 && crow.getY() == 32820) {
							cha = crow;
							break;
						}
					}
				}
			} else if (getX() == 32593 && getY() == 32819) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1ScarecrowInstance) {
						L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
						if (crow != null && crow.getNpcTemplate().getNpcId() == 202097 && crow.getX() == 32593 && crow.getY() == 32818) {
							cha = crow;
							break;
						}
					}
				}
			} else if ((getX() == 32705 && getY() == 32817) || (getX() == 32704 && getY() == 32816)) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1ScarecrowInstance) {
						L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
						if (crow != null && crow.getNpcTemplate().getNpcId() == 202098 && crow.getX() == 32705 && crow.getY() == 32816) {
							cha = crow;
							break;
						}
					}
				}
			} else if (getX() == 32705 && getY() == 32805) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1ScarecrowInstance) {
						L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
						if (crow != null && crow.getNpcTemplate().getNpcId() == 202098 && crow.getX() == 32705 && crow.getY() == 32806) {
							cha = crow;
							break;
						}
					}
				}
			} else if (getX() == 32679 && getY() == 32811) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1MerchantInstance) {
						L1MerchantInstance mer = (L1MerchantInstance) obj;
						if (mer != null && mer.getNpcTemplate().getNpcId() == 202087 && mer.getX() == 32679 && mer.getY() == 32810) {
							cha = mer;
							break;
						}
					}
				}
			} else if (getX() == 32676 && getY() == 32811) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1MerchantInstance) {
						L1MerchantInstance mer = (L1MerchantInstance) obj;
						if (mer != null && mer.getNpcTemplate().getNpcId() == 202087 && mer.getX() == 32676 && mer.getY() == 32810) {
							cha = mer;
							break;
						}
					}
				}
			} else if (getX() == 32679 && getY() == 32819) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1MerchantInstance) {
						L1MerchantInstance mer = (L1MerchantInstance) obj;
						if (mer != null && mer.getNpcTemplate().getNpcId() == 202087 && mer.getX() == 32679 && mer.getY() == 32818) {
							cha = mer;
							break;
						}
					}
				}
			} else if (getX() == 32676 && getY() == 32819) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1MerchantInstance) {
						L1MerchantInstance mer = (L1MerchantInstance) obj;
						if (mer != null && mer.getNpcTemplate().getNpcId() == 202087 && mer.getX() == 32676 && mer.getY() == 32818) {
							cha = mer;
							break;
						}
					}
				}
			}
		} else if (getNpcTemplate().getNpcId() == 202087) { // 훈련소 창
			if (getX() == 32610 && getY() == 32818) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1MerchantInstance) {
						L1MerchantInstance mer = (L1MerchantInstance) obj;
						if (mer != null && mer.getNpcTemplate().getNpcId() == 202086 && mer.getX() == 32610 && mer.getY() == 32817) {
							cha = mer;
							break;
						}
					}
				}
			} else if (getX() == 32679 && getY() == 32810) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1MerchantInstance) {
						L1MerchantInstance mer = (L1MerchantInstance) obj;
						if (mer != null && mer.getNpcTemplate().getNpcId() == 202086 && mer.getX() == 32679 && mer.getY() == 32811) {
							cha = mer;
							break;
						}
					}
				}
			} else if (getX() == 32676 && getY() == 32810) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1MerchantInstance) {
						L1MerchantInstance mer = (L1MerchantInstance) obj;
						if (mer != null && mer.getNpcTemplate().getNpcId() == 202086 && mer.getX() == 32676 && mer.getY() == 32811) {
							cha = mer;
							break;
						}
					}
				}
			} else if (getX() == 32679 && getY() == 32818) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1MerchantInstance) {
						L1MerchantInstance mer = (L1MerchantInstance) obj;
						if (mer != null && mer.getNpcTemplate().getNpcId() == 202086 && mer.getX() == 32679 && mer.getY() == 32819) {
							cha = mer;
							break;
						}
					}
				}
			} else if (getX() == 32676 && getY() == 32818) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1MerchantInstance) {
						L1MerchantInstance mer = (L1MerchantInstance) obj;
						if (mer != null && mer.getNpcTemplate().getNpcId() == 202086 && mer.getX() == 32676 && mer.getY() == 32819) {
							cha = mer;
							break;
						}
					}
				}
			}
		} else if (getNpcTemplate().getNpcId() == 202100) { // 수련자
			if (getX() == 32699 && getY() == 32813) {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
					if (obj instanceof L1ScarecrowInstance) {
						L1ScarecrowInstance crow = (L1ScarecrowInstance) obj;
						if (crow != null && crow.getNpcTemplate().getNpcId() == 202098 && crow.getX() == 32700 && crow.getY() == 32813) {
							cha = crow;
							break;
						}
					}
				}
			}
		}
		return cha;
	}
	
	@Override
	public void setLink(L1Character cha) {
		if (isLink && cha != null && _hateList.isEmpty()) {
			_hateList.add(cha, 0);
			checkTarget();
		}
	}
	
	private int _redMoveX = 0, _redMoveY = 0, _redActionCnt = 0;
	
	protected void action(){
		int npcId = getNpcId(), sleeptime = getNpcTemplate().getAtkSpeed(), x = getX(), y = getY();
		try {
			if (npcId == 202077 && x == 32703 && y == 32802) {// 바라크
				this.broadcastPacket(new S_AttackPacket(this, 0, ActionCodes.ACTION_Attack, true), true);
			} else if (npcId == 202080 && x == 32689 && y == 32837) {// 데포로쥬
				this.broadcastPacket(new S_AttackPacket(this, 0, ActionCodes.ACTION_Idle_TWO, true), true);
			} else if (npcId == 5122 || npcId == 202089 || npcId == 202084 || npcId == 202090 || npcId == 202079) {// 감독관
				sleeptime = getNpcTemplate().getPassiSpeed();
				int homeX = getHomeX(), homeY = getHomeY();
				if (homeX == 32595 && homeY == 33224) {// 우드백 감독관
					if (x == 32595 && y == 33224) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 4000;
							_redActionCnt++;
							break;
						case 1:
							_redMoveX = 32601;
							_redMoveY = 33224;
							_redActionCnt = 0;
							break;
						}
					} else if (x == 32601 && y == 33224) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 4000;
							_redActionCnt++;
							break;
						case 1:
							_redMoveX = 32595;
							_redMoveY = 33224;
							_redActionCnt = 0;
							break;
						}
					}
				} else if (homeX == 32614 && homeY == 33227) {// 우드백 감독관
					if (x == 32614 && y == 33227) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 4000;
							_redActionCnt++;
							break;
						case 1:
							_redMoveX = 32614;
							_redMoveY = 33237;
							_redActionCnt = 0;
							break;
						}
					} else if (x == 32614 && y == 33237) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 4000;
							_redActionCnt++;
							break;
						case 1:
							_redMoveX = 32614;
							_redMoveY = 33227;
							_redActionCnt = 0;
							break;
						}
					}
				} else if (homeX == 32599 && homeY == 32822) {// 글루딘 감독관
					if (x == 32599 && y == 32822) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 4000;
							_redActionCnt++;
							break;
						case 1:
							_redMoveX = 32589;
							_redMoveY = 32822;
							_redActionCnt = 0;
							break;
						}
					} else if (x == 32589 && y == 32822) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 4000;
							_redActionCnt++;
							break;
						case 1:
							_redMoveX = 32599;
							_redMoveY = 32822;
							_redActionCnt = 0;
							break;
						}
					}
				} else if (homeX == 32702 && homeY == 32833) {// 붉은 기사단 훈련소 대장장이 란디스
					if (x == 32702 && y == 32833) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 1000;
							_redActionCnt++;
							break;
						case 1:
							this.getMoveState().setHeading(2);
							this.broadcastPacket(new S_ChangeHeading(this), true);
							sleeptime = 2000;
							_redActionCnt++;
							break;
						case 2:
							this.broadcastPacket(new S_AttackPacket(this, 0, ActionCodes.ACTION_SwordIdle, true), true);
							sleeptime = 12000;
							_redActionCnt++;
							break;
						case 3:
							_redMoveX = 32699;
							_redMoveY = 32836;
							_redActionCnt = 0;
							break;
						}
					} else if (x == 32699 && y == 32836) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 1000;
							_redActionCnt++;
							break;
						case 1:
							this.getMoveState().setHeading(4);
							this.broadcastPacket(new S_ChangeHeading(this), true);
							sleeptime = 1000;
							_redActionCnt++;
							break;
						case 2:
							this.broadcastPacket(new S_AttackPacket(this, 0, ActionCodes.ACTION_SkillBuff, true), true);
							sleeptime = 4000;
							_redActionCnt++;
							break;
						case 3:
							this.broadcastPacket(new S_AttackPacket(this, 0, ActionCodes.ACTION_SwordIdle, true), true);
							sleeptime = 12000;
							_redActionCnt++;
							break;
						case 4:
							_redMoveX = 32698;
							_redMoveY = 32833;
							_redActionCnt = 0;
							break;
						}
					} else if (x == 32698 && y == 32833) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 1000;
							_redActionCnt++;
							break;
						case 1:
							this.getMoveState().setHeading(0);
							this.broadcastPacket(new S_ChangeHeading(this), true);
							sleeptime = 2000;
							_redActionCnt++;
							break;
						case 2:
							this.broadcastPacket(new S_AttackPacket(this, 0, ActionCodes.ACTION_SwordIdle, true), true);
							sleeptime = 12000;
							_redActionCnt++;
							break;
						case 3:
							_redMoveX = 32700;
							_redMoveY = 32833;
							_redActionCnt = 0;
							break;
						}
					} else if (x == 32700 && y == 32833) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 1000;
							_redActionCnt++;
							break;
						case 1:
							this.getMoveState().setHeading(0);
							this.broadcastPacket(new S_ChangeHeading(this), true);
							sleeptime = 12000;
							_redActionCnt++;
							break;
						case 2:
							_redMoveX = 32702;
							_redMoveY = 32833;
							_redActionCnt = 0;
							break;
						}
					}
				} else if (homeX == 32725 && homeY == 32836) {// 붉은 기사단 훈련소 군터
					if (x == 32725 && y == 32836) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 12000;
							_redActionCnt++;
							break;
						case 1:
							_redMoveX = 32725;
							_redMoveY = 32839;
							_redActionCnt = 0;
							break;
						}
					} else if (x == 32725 && y == 32839) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 1000;
							_redActionCnt++;
							break;
						case 1:
							this.getMoveState().setHeading(5);
							this.broadcastPacket(new S_ChangeHeading(this), true);
							sleeptime = 12000;
							_redActionCnt++;
							break;
						case 2:
							_redMoveX = 32727;
							_redMoveY = 32840;
							_redActionCnt = 0;
							break;
						}
					} else if (x == 32727 && y == 32840) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 1000;
							_redActionCnt++;
							break;
						case 1:
							this.getMoveState().setHeading(2);
							this.broadcastPacket(new S_ChangeHeading(this), true);
							sleeptime = 1000;
							_redActionCnt++;
							break;
						case 2:
							this.broadcastPacket(new S_AttackPacket(this, 0, ActionCodes.ACTION_SkillAttack, true), true);
							sleeptime = 12000;
							_redActionCnt++;
							break;
						case 3:
							_redMoveX = 32725;
							_redMoveY = 32836;
							_redActionCnt = 0;
							break;
						}
					} 
				} else if (homeX == 32678 && homeY == 32803) {// 붉은 기사단 훈련소 티메그
					if (x == 32678 && y == 32803) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 1000;
							_redActionCnt++;
							 break;
						case 1:
							this.getMoveState().setHeading(0);
							this.broadcastPacket(new S_ChangeHeading(this), true);
							sleeptime = 12000;
							_redActionCnt++;
							break;
						case 2:
							_redMoveX = 32673;
							_redMoveY = 32803;
							_redActionCnt = 0;
							break;
						}
					} else if (x == 32673 && y == 32803) {
						_redMoveX = _redMoveY = 0;
						switch(_redActionCnt){
						case 0:
							sleeptime = 1000;
							_redActionCnt++;
							break;
						case 1:
							this.getMoveState().setHeading(0);
							this.broadcastPacket(new S_ChangeHeading(this), true);
							sleeptime = 12000;
							_redActionCnt++;
							break;
						case 2:
							_redMoveX = 32678;
							_redMoveY = 32803;
							_redActionCnt = 0;
							break;
						}
					}
				}
				if (_redMoveX != 0 && _redMoveY != 0) {
					int dir = moveDirection(this.getMapId(), _redMoveX, _redMoveY);
					if (dir != -1) {
						setDirectionMove(dir);
					}
				}
			}
			setSleepTime(sleeptime);
		} catch(Exception e){}
	}
}

