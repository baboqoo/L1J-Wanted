package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Account;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.spell.S_SpellBuffNoti;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.SQLUtil;

/**
 * 아인하사드 관리 클래스
 * Account(계정)에 할당된다.
 * @author LinOffice
 */
public class L1Einhasad {
	static final IntRange RANGE = new IntRange(0, Config.EIN.REST_EXP_LIMIT_CHARGE_VALUE);
	
	private final Account owner;
	private int point;// 아인하사드 수치
	private int dayBonus;// 일일 보너스 충전
	
	/**
	 * 생성자
	 * @param owner
	 * @param point
	 * @param dayBonus
	 */
	public L1Einhasad(Account owner, int point, int dayBonus) {
		this.owner		= owner;
		this.point		= point;
		this.dayBonus	= dayBonus;
	}

	public int getPoint() {
		return point;
	}
	
	public void addPoint(int value, L1PcInstance pc) {
        point = RANGE.ensure(point + value);
        setBuff(pc);
    }

	public void setPoint(int value, L1PcInstance pc) {
		point = RANGE.ensure(value);
		setBuff(pc);
	}

	public int getDayBonus() {
		return dayBonus;
	}

	public void setDayBonus(int dayBonus) {
		this.dayBonus = dayBonus;
	}
	
	/**
	 * 버프 처리
	 * @param pc
	 */
	void setBuff(L1PcInstance pc){
		if (pc == null) {
        	return;
        }
		setBuff(pc, point >= Config.EIN.REST_EXP_DEFAULT_RATION);
	}
	
	void setBuff(L1PcInstance pc, boolean flag){
    	if (flag) {
    		if (pc._isDragonFavor) {
    			setDragonFavor(pc, false);
        	}
        	if (!pc._isDragonBless) {
        		setDragonBless(pc, true);
        	}
    	} else {
    		if (pc._isDragonBless) {
    			setDragonBless(pc, false);
        	}
    		if (!pc._isDragonFavor && !pc.isPCCafe() && pc.getSkill().hasSkillEffect(L1SkillId.EINHASAD_FAVOR)) {
    			setDragonFavor(pc, true);
    		}
    	}
    }
	
	/**
	 * 드래곤의 축복 버프
	 * @param pc
	 * @param flag
	 */
	public void setDragonBless(L1PcInstance pc, boolean flag){
		pc._isDragonBless = flag;
		pc.ability.addDamageReduction(flag ? 4 : -4);
		pc.getResistance().addToleranceAll(flag ? 5 : -5);
		pc.ability.addPVPDamageReduction(flag ? 5 : -5);
		pc.addCarryBonus(flag ? 500 : -500);
		pc.sendPackets(flag ? S_SpellBuffNoti.DRAGON_BLESS_ON : S_SpellBuffNoti.DRAGON_BLESS_OFF);
	}
	
	/**
	 * 드래곤의 가호 버프
	 * @param pc
	 * @param flag
	 */
	public void setDragonFavor(L1PcInstance pc, boolean flag){
		pc._isDragonFavor = flag;
		pc.ability.addDamageReduction(flag ? 2 : -2);
		pc.addCarryBonus(flag ? 100 : -100);
		pc.sendPackets(flag ? S_SpellBuffNoti.DRAGON_FAVOR_ON : S_SpellBuffNoti.DRAGON_FAVOR_OFF);
	}
	
	/**
	 * 드래곤의 가호(PC) 버프
	 * @param pc
	 * @param flag
	 */
	public void setDragonFavorPCCafe(L1PcInstance pc, boolean flag) {
		pc._isDragonFavorPCCafe = flag;
		pc.ability.addDamageReduction(flag ? 2 : -2);
		pc.addCarryBonus(flag ? 100 : -100);
		pc.sendPackets(flag ? S_SpellBuffNoti.DRAGON_FAVOR_PCCAFE_ON : S_SpellBuffNoti.DRAGON_FAVOR_PCCAFE_OFF);
	}
	
	/**
	 * 아인하사드 수치 갱신
	 */
	public void updateEinhasad() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET Einhasad=? WHERE login=?");
			pstm.setInt(1, point);
			pstm.setString(2, owner.getName());
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	/**
	 * 일일 보너스 충전 갱신
	 */
	public void updateEinDayBonus() {
		Connection con			= null;
		PreparedStatement pstm	= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("UPDATE accounts SET EinDayBonus=? WHERE login=?");
			pstm.setInt(1, dayBonus);
			pstm.setString(2, owner.getName());
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
}

