package l1j.server.server.serverpackets.object;

import java.util.List;

import l1j.server.Config;
import l1j.server.IndunSystem.occupy.OccupyHandler;
import l1j.server.IndunSystem.occupy.OccupyManager;
import l1j.server.IndunSystem.occupy.OccupyType;
import l1j.server.common.data.eBloodPledgeRankType;
import l1j.server.server.ActionCodes;
import l1j.server.server.construct.L1InterServer;
import l1j.server.server.model.L1CharacterConfig;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1MoveState;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_PCObject extends WorldPutObjectStream {
	private static final String S_PC_OBJECT = "[S] S_PCObject";
	private byte[] _byte = null;
	
	private L1InterServer inter;
	private int pledge_id;
	private L1Clan pledge;
	private L1MoveState move;
	
	public S_PCObject(L1PcInstance pc) {
		inter		= pc.getMap().getInter();
		pledge_id	= pc.getClanid();
		pledge		= pc.getClan();
		move		= pc.getMoveState();
		write_init();
		write_point(pc.getX(), pc.getY());
		write_objectnumber(pc.getId());
		write_objectsprite(pc.isDead() ? pc.getTempCharGfxAtDead() : pc.getSpriteId());
		write_action(getAction(pc));
		write_direction(move.getHeading());
		write_lightRadius(pc.getLight().getChaLightSize());
		write_objectcount(1);
		write_alignment(pc.getAlignment());
		write_desc(pc.getName());
		write_title(pc.getTitle());
		write_speeddata(move.getMoveSpeed());
		write_emotion(move.getBraveSpeed());
		write_drunken(move.getDrunken());
		write_isghost(pc.isGhost());
		write_isparalyzed(pc.getParalysis() != null);
		write_isuser(true);
		write_isinvisible(pc.isInvisble() || pc.isGmInvis());
		write_ispoisoned(pc.getPoison() != null);
		write_emblemid(pledge != null ? pledge.getEmblemId() : 0);
		write_pledgename(pledge != null ? pledge.getClanName() : null);
		write_mastername(null);
		write_altitude(0);
		write_hitratio(-1);
		write_safelevel(0);
		write_shoptitle(pc.getShopChat());
		write_weaponsprite(-1);
		write_couplestate(0);
		write_boundarylevelindex(pc.getBoundaryLevelIndex());
		write_manaratio(-1);
		write_homeserverno(Config.VERSION.SERVER_NUMBER);
		if (pledge_id == 2) {// 애니메이션 혈마크 
			write_team_id(L1Clan.RED_KNIGHT_TEAM_ID);
		} else if (inter != null) {
			inter_team(pc);
		}
		if (pledge_id > 0 && pc.warZone) {
			List<L1War> warList = L1World.getInstance().getWarList();
			if (!warList.isEmpty()) {
				proclamation_siege(pc, warList);
			}
		}
		write_user_game_class(pc.getType());
		if (pc.getConfig().getAnonymityType() != null) {
			anonymity(pc.getConfig());
		}
		write_forth_gear(move.isFourthGear());
		write_speed_bonus(pc);
		write_is_excavated(false);
		int above_head_mark_id = 0;
		if (inter != null && pc._occupyTeamType != null && L1InterServer.isOccupyInter(inter)) {
			OccupyHandler handler = OccupyManager.getInstance().getHandler(inter == L1InterServer.OCCUPY_HEINE ? OccupyType.HEINE : OccupyType.WINDAWOOD);
			above_head_mark_id = handler != null && handler.isLargeBadge(pc) ? 1 : 0;
		}
		write_above_head_mark_id(above_head_mark_id);
		write_invisible_level(0);
		writeH(0x00);
	}
	
	int getAction(L1PcInstance pc) {
		if (pc.isDead()) {
			return pc.getActionStatus();
		}
		if (pc.isPrivateShop()) {
			return ActionCodes.ACTION_Shop;
		}
		if (pc.isFishing()) {
			return ActionCodes.ACTION_Fishing;
		}
		return pc.getCurrentWeapon();
	}
	
	void proclamation_siege(L1PcInstance pc, List<L1War> warList){
		for (L1War war : warList) {
			boolean ret = war.CheckClanInWar(pc.getClanName());
			if (ret) {
				if (pc.isCrown() && pc.getBloodPledgeRank() == eBloodPledgeRankType.RANK_NORMAL_KING) {// 머리위 군주 표시
					write_proclamation_siege_mark(true);
				} else {
					write_proclamation_siege_pledge(true);
				}
				break;
			}
		}
	}
	
	void inter_team(L1PcInstance pc) {
		if (L1InterServer.isTeamMappingInter(inter)) {
			write_team_id(pc.getClanid() == 0 || pledge == null ? L1Clan.INTER_TEAM_IDS[L1Clan.INTER_TEAM_IDS.length - 1] : pledge.getTeamId());
		} else if (inter == L1InterServer.WORLD_WAR) {// 월드 공성전
			if (pc.getClanid() == 0 || pledge == null) {// 다른편
				write_team_id(1);
			}
		} else if (L1InterServer.isOccupyInter(inter) && pc._occupyTeamType != null) {// 수호탑 점령전
			write_team_id(pc._occupyTeamType.getTeamId());
		}
	}
	
	void anonymity(L1CharacterConfig config){
		write_anonymity_name(config.getAnonymityName().getBytes());
		write_anonymity_type(config.getAnonymityType().toInt());
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
	
	@Override
	public String getType() {
		return S_PC_OBJECT;
	}

}
