package l1j.server.server.utils;

import java.util.HashMap;

import l1j.server.common.bin.potential.CommonPotentialInfo;
import l1j.server.common.data.Material;
import l1j.server.server.model.item.ablity.ItemAbilityFactory;
import l1j.server.server.templates.L1Item;

/**
 * 아이템 표기처리 패킷 Stream
 * @author LinOffice
 */
public class BinaryOutputStreamExtraDescription extends BinaryOutputStream {
	public BinaryOutputStreamExtraDescription() {
		super();
	}
	
	// 손상도
	public void writeDurability(int value){
		writeC(3);
		writeC(value);
	}
	
	// 양손 무기
	public void writeTwohandedWeapon(){
		writeC(4);
	}
	
	// 사용 클래스
	public void writeClassPermit(int value){
		writeC(7);
		writeH(value);
	}
	
	// 영양
	public void writeFoodVolume(int value){
		writeC(21);
		writeH(value);
	}
	
	// 밝기
	public void writeLightRange(int value){
		writeC(22);
		writeH(value);
	}
	
	// 일반
	public void writeEtcDefault(){
		writeC(23);
	}
	
	// 이벤트 타입
	public void writeEtcEvent(){
		writeC(20);
		writeC(23);
	}
	
	// 재질
	public void writeMaterial(Material value){
		writeC(value.toInt());
	}
	
	// 무게
	public void writeWeight(int value){
		writeD(value);
	}
	
	// 대미지 큰몹/작은몹
	public void writeWeaponDefaultDamage(int small, int large){
		writeC(1);
		writeC(small);
		writeC(large);
	}
	
	// 인챈트 수치에 대한 대미지 or AC
	public void writeEnchatValue(int enchant, int value){
		writeC(2);
		writeC(enchant);
		writeC(107);
		writeC(value);
		writeC(value);
	}
	
	// AC
	public void writeArmorDefaultAc(int value){
		writeC(19);
		writeC(value);
	}
	
	// AC 추가 설정
	public void writeAcSub(int value){
		writeC(ItemAbilityFactory.AC_SUB.get_enum_key());
		writeC(value);
	}
	
	// custom String
	public void writeString(String value){
		writeC(39);
		writeS(value);
	}
	
	// 특성
	public void writeCharacteristic(int value, boolean init) {
		if (!init) {
			writeC(67);
		}
		writeH(value);
	}
	
	// 성향치
	public void writeAlignment(int value){
		writeC(75);
		writeC(value);
	}
	
	// 단계
	public void writeGrade(int value){
		writeC(77);
		writeC(value);
	}
	
	// 속성
	public void writeSkillAttr(int value){
		writeC(78);
		writeC(value);
	}
	
	// 사용 레벨
	public void writeUseLevel(int value){
		writeC(79);
		writeC(value);
	}
	
	// 추가 방어력
	public void writeAcBonus(int value){
		writeC(ItemAbilityFactory.AC_BONUS.get_enum_key());
		writeC(value);
	}
	
	// 힘
	public void writeStr(int value){
		writeC(ItemAbilityFactory.STR.get_enum_key());
		writeC(value);
	}
	
	// 덱
	public void writeDex(int value){
		writeC(ItemAbilityFactory.DEX.get_enum_key());
		writeC(value);
	}
	
	// 콘
	public void writeCon(int value){
		writeC(ItemAbilityFactory.CON.get_enum_key());
		writeC(value);
	}
	
	// 위즈
	public void writeWis(int value){
		writeC(ItemAbilityFactory.WIS.get_enum_key());
		writeC(value);
	}
	
	// 인트
	public void writeInt(int value){
		writeC(ItemAbilityFactory.INT.get_enum_key());
		writeC(value);
	}
	
	// 카리
	public void writeCha(int value){
		writeC(ItemAbilityFactory.CHA.get_enum_key());
		writeC(value);
	}
	
	// 최대 HP
	public void writeMaxHp(int value){
		writeC(ItemAbilityFactory.MAX_HP.get_enum_key());
		writeH(value);
	}
	
	// 최대 MP
	public void writeMaxMp(int value){
		writeC(ItemAbilityFactory.MAX_MP.get_enum_key());
		writeH(value);
	}
	
	// HP회복
	public void writeHpRegen(int value){
		writeC(ItemAbilityFactory.HP_REGEN.get_enum_key());
		writeC(value);
	}
	
	// MP회복
	public void writeMpRegen(int value){
		writeC(ItemAbilityFactory.MP_REGEN.get_enum_key());
		writeC(value);
	}
	
	// 흡수: HP
	public void writeHpDrain(){
		writeC(ItemAbilityFactory.HP_DRAIN.get_enum_key());
	}
	
	// 흡수: MP
	public void writeMpDrain(){
		writeC(ItemAbilityFactory.MP_DRAIN.get_enum_key());
	}
	
	// HP절대 회복 +d(32초)
	public void writeHpAbsolRegen32Second(int value){
		writeC(ItemAbilityFactory.HP_ABSOL_REGEN_32_SECOND.get_enum_key());
		writeC(value);
	}
	
	// MP절대 회복 +d(64초)
	public void writeMpAbsolRegen64Second(int value){
		writeC(ItemAbilityFactory.MP_ABSOL_REGEN_64_SECOND.get_enum_key());
		writeC(value);
	}
	
	// MP절대 회복 +d(16초)
	public void writeMpAbsolRegen16Second(int value){
		writeC(ItemAbilityFactory.MP_ABSOL_REGEN_16_SECOND.get_enum_key());
		writeC(value);
	}
	
	// MR
	public void writeMagicRegist(int value){
		writeC(ItemAbilityFactory.MAGIC_REGIST.get_enum_key());
		writeH(value);
	}
	
	// 근거리 명중
	public void writeShortHit(int value){
		writeC(ItemAbilityFactory.SHORT_HIT.get_enum_key());
		writeC(value);
	}
	
	// 근거리 대미지
	public void writeShortDamage(int value){
		writeC(ItemAbilityFactory.SHORT_DAMAGE.get_enum_key());
		writeC(value);
	}
	
	// 근거리 치명타
	public void writeShortCritical(int value){
		writeC(ItemAbilityFactory.SHORT_CRITICAL.get_enum_key());
		writeC(value);
	}
	
	// 원거리 명중
	public void writeLongHit(int value){
		writeC(ItemAbilityFactory.LONG_HIT.get_enum_key());
		writeC(value);
	}
	
	// 원거리 대미지
	public void writeLongDamage(int value){
		writeC(ItemAbilityFactory.LONG_DAMAGE.get_enum_key());
		writeC(value);
	}
	
	// 원거리 치명타
	public void writeLongCritical(int value){
		writeC(ItemAbilityFactory.LONG_CRITICAL.get_enum_key());
		writeC(value);
	}
	
	// SP
	public void writeSpellPower(int value){
		writeC(ItemAbilityFactory.SPELLPOWER.get_enum_key());
		writeC(value);
	}
	
	// 마법 명중
	public void writeMagicHit(int value){
		writeC(ItemAbilityFactory.MAGIC_HIT.get_enum_key());
		writeC(value);
	}
	
	// 마법 치명타
	public void writeMagicCritical(int value){
		writeC(ItemAbilityFactory.MAGIC_CRITICAL.get_enum_key());
		writeH(value);
	}
	
	// 불 속성
	public void writeAttrFire(int value){
		writeC(ItemAbilityFactory.ATTR_FIRE.get_enum_key());
		writeC(value);
	}
	
	// 물 속성
	public void writeAttrWater(int value){
		writeC(ItemAbilityFactory.ATTR_WATER.get_enum_key());
		writeC(value);
	}
	
	// 바람 속성
	public void writeAttrWind(int value){
		writeC(ItemAbilityFactory.ATTR_WIND.get_enum_key());
		writeC(value);
	}
	
	// 땅 속성
	public void writeAttrEarth(int value){
		writeC(ItemAbilityFactory.ATTR_EARTH.get_enum_key());
		writeC(value);
	}
	
	// 전체 속성
	public void writeAttrAll(int value){
		writeC(ItemAbilityFactory.ATTR_ALL.get_enum_key());
		writeC(value);
	}
	
	// 빙결 내성
	public void writeRegistFreeze(int value){
		writeC(ItemAbilityFactory.REGIST_FREEZE.get_enum_key());
		writeC(1);
		writeC(value);
	}
	
	// 석화 내성
	public void writeRegistStone(int value){
		writeC(ItemAbilityFactory.REGIST_STONE.get_enum_key());
		writeC(2);
		writeH(value);
	}
	
	// 수면 내성
	public void writeRegistSleep(int value){
		writeC(ItemAbilityFactory.REGIST_SLEEP.get_enum_key());
		writeC(3);
		writeH(value);
	}
	
	// 홀드 내성
	public void writeRegistBlind(int value){
		writeC(ItemAbilityFactory.REGIST_BLIND.get_enum_key());
		writeC(4);
		writeH(value);
	}
	
	// EXP
	public void writeExpBonus(int value){
		writeC(ItemAbilityFactory.EXP_BONUS.get_enum_key());
		writeC(value);
	}
	
	// DG
	public void writeEvasion(int value){
		writeC(ItemAbilityFactory.EVASION.get_enum_key());
		writeC(value);
	}
	
	// ER
	public void writeEvasionRegist(int value){
		writeC(ItemAbilityFactory.EVASION_REGIST.get_enum_key());
		writeC(value);
	}
	
	// ME
	public void writeMagicEvasion(int value){
		writeC(ItemAbilityFactory.MAGIC_EVASION.get_enum_key());
		writeD(value);
	}
	
	// 최대 소지 무게 감소
	public void writeCarryBonus(int value){
		writeC(ItemAbilityFactory.CARRY_BONUS.get_enum_key());
		writeH(value);
	}
	
	// 대미지 감소
	public void writeDamageReduction(int value){
		writeC(ItemAbilityFactory.DAMAGE_REDUCTION.get_enum_key());
		writeC(value);
	}
	
	// 대미지 감소 무시
	public void writeDamageReductionEgnor(int value){
		writeC(ItemAbilityFactory.DAMAGE_REDUCTION_EGNOR.get_enum_key());
		writeC(value);
	}
	
	// 대미지 감소 확률
	public void writeDamageReductionChance(int[] value){
		writeC(ItemAbilityFactory.DAMAGE_REDUCTION_CHANCE.get_enum_key());
		writeC(value[1]);
		writeC(value[0]);
	}
	
	// 추가 대미지 확률 +%d(%d)
	public void writeDamageChance(int[] value){
		writeC(ItemAbilityFactory.DAMAGE_CHANCE.get_enum_key());
		writeC(value[1]);
		writeC(value[0]);
	}
	
	// 추가 대미지 확률 +%d
	public void writeDamageChanceEtc(int value){
		writeC(ItemAbilityFactory.DAMAGE_CHANCE_ETC.get_enum_key());
		writeC(value);
	}
	
	// PVP추가 대미지
	public void writePVPDamage(int value){
		writeC(ItemAbilityFactory.PVP_DAMAGE.get_enum_key());
		writeC(value);
	}
	
	// PVP대미지 감소
	public void writePVPDamageReduction(int value){
		writeC(ItemAbilityFactory.PVP_DAMAGE_REDUCTION.get_enum_key());
		writeC(value);
	}
	
	// PVP마법 대미지 감소
	public void writePVPMagicDamageReduction(int value){
		writeC(ItemAbilityFactory.PVP_MAGIC_DAMAGE_REDUCTION.get_enum_key());
		writeC(value);
	}
	
	// PVP대미지 감소 무시
	public void writePVPDamageReductionEgnor(int value){
		writeC(ItemAbilityFactory.PVP_DAMAGE_REDUCTION_EGNOR.get_enum_key());
		writeC(value);
	}
	
	// PVP마법 대미지 감소 무시
	public void writePVPMagicDamageReductionEgnor(int value){
		writeC(ItemAbilityFactory.PVP_MAGIC_DAMAGE_REDUCTION_EGNOR.get_enum_key());
		writeC(value);
	}
	
	// 물약 회복량 %d% + %d
	public void writePotionPercentAndValue(int percent, int value){
		writeC(65);
		writeC(percent);
		writeC(value);
	}
	
	// 물약 회복량 %
	public void writePotionPercent(int value) {
		writeC(ItemAbilityFactory.POTION_PERCENT.get_enum_key());
		writeC(value);
	}
	
	// 물약 회복량 +
	public void writePotionValue(int value) {
		writeC(ItemAbilityFactory.POTION_VALUE.get_enum_key());
		writeC(value);
	}
	
	// 회복 악화 방어
	public void writePotionRegist(int value) {
		writeC(ItemAbilityFactory.POTION_REGIST.get_enum_key());
		writeC(value);
	}
	
	// 독 내성
	public void writePoisonRegist(int value){
		writeC(ItemAbilityFactory.POISON_REGIST.get_enum_key());
		writeC(value);
	}
	
	// 발동
	public void writeMagicName(String value){
		writeC(ItemAbilityFactory.MAGIC_NAME.get_enum_key());
		writeS(value);
	}
	
	// 발동
	public void writeMagicNameSecond(String value){
		writeC(156);
		writeS(value);
	}
	
	// 포우 슬레이어 대미지
	public void writeFowSlayerDamage(int value){
		writeC(ItemAbilityFactory.FOW_DAMAGE.get_enum_key());
		writeC(value);
	}
	
	// 타이탄 계열 증가
	public void writeTitanUp(int value){
		writeC(ItemAbilityFactory.TITAN_UP.get_enum_key());
		writeC(value);
	}
	
	// 속성 대미지
	public void writeAttrDamageSimple(int value){
		writeC(109);
		writeC(value);
	}
	
	// 속성 대미지
	public void writeAttrDamage(int value){
		writeC(110);
		writeC(value);
	}
	
	// 대미지 언데드
	public void writeUndead(int value){
		writeC(ItemAbilityFactory.UNDEAD.get_enum_key());
		writeD(value);
	}
	
	// 대미지 데몬
	public void writeDemon(int value){
		writeC(ItemAbilityFactory.DEMON.get_enum_key());
		writeD(value);
	}
	
	// 축복 소모 효율
	public void writeRestExpReduceEfficiency(int value){
		writeC(ItemAbilityFactory.REST_EXP_REDUCE_EFFICIENCY.get_enum_key());
		writeH(value);
	}
	
	// 내성: 기술
	public void writeToleranceSkill(int value){
		writeC(ItemAbilityFactory.TOLERANCE_SKILL.get_enum_key());
		writeC(value);
	}
	
	// 내성: 정령
	public void writeToleranceSpirit(int value){
		writeC(ItemAbilityFactory.TOLERANCE_SPIRIT.get_enum_key());
		writeC(value);
	}
	
	// 내성: 용언
	public void writeToleranceDragon(int value){
		writeC(ItemAbilityFactory.TOLERANCE_DRAGON.get_enum_key());
		writeC(value);
	}
	
	// 내성: 공포
	public void writeToleranceFear(int value){
		writeC(ItemAbilityFactory.TOLERANCE_FEAR.get_enum_key());
		writeC(value);
	}
	
	// 내성: 전체
	public void writeToleranceAll(int value){
		writeC(ItemAbilityFactory.TOLERANCE_ALL.get_enum_key());
		writeC(value);
	}
	
	// 적중: 기술
	public void writeHitupSkill(int value){
		writeC(ItemAbilityFactory.HITUP_SKILL.get_enum_key());
		writeC(value);
	}
	
	// 적중: 정령
	public void writeHitupSpirit(int value){
		writeC(ItemAbilityFactory.HITUP_SPIRIT.get_enum_key());
		writeC(value);
	}
	
	// 적중: 용언
	public void writeHitupDragon(int value){
		writeC(ItemAbilityFactory.HITUP_DRAGON.get_enum_key());
		writeC(value);
	}
	
	// 적중: 공포
	public void writeHitupFear(int value){
		writeC(ItemAbilityFactory.HITUP_FEAR.get_enum_key());
		writeC(value);
	}
	
	// 적중: 전체
	public void writeHitupAll(int value){
		writeC(ItemAbilityFactory.HITUP_ALL.get_enum_key());
		writeC(value);
	}
	
	// 비손상
	public void writeCanbeDamage(){
		writeC(131);
		writeD(1);
	}
	
	// 지속 시간
	public void writeBuffDurationSecond(int value){
		writeC(ItemAbilityFactory.BUFF_DURATION_SECOND.get_enum_key());
		writeH(value);
	}
	
	// 경험치 추가 %d%
	public void writeAddExpPercent(int value){
		writeC(ItemAbilityFactory.ADD_EXP_PERCENT.get_enum_key());
		writeH(value);
	}
	
	// PC방 경험치 추가 %d%
	public void writeAddExpPercentPCCafe(int value){
		writeC(ItemAbilityFactory.ADD_EXP_PERCENT_PC_CAFE.get_enum_key());
		writeH(value);
	}
	
	// 음식
	public void writeFoodType(int value){
		writeC(ItemAbilityFactory.FOOD_TYPE.get_enum_key());
		writeC(value);
	}
	
	// 최대 HP증가 +%d%
	public void writeBaseHpRate(int value){
		writeC(ItemAbilityFactory.BASE_HP_RATE.get_enum_key());
		writeC(value);
	}
	
	// 최대 MP증가 +%d%
	public void writeBaseMpRate(int value){
		writeC(ItemAbilityFactory.BASE_MP_RATE.get_enum_key());
		writeC(value);
	}
	
	// 드래곤 피해 감소
	public void writeDragonDamageReduction(int value){
		writeC(ItemAbilityFactory.DRAGON_DAMAGE_REDUCTION.get_enum_key());
		writeC(value);
	}
	
	// 축복경험치
	public void writeBlessExp(int value){
		writeC(ItemAbilityFactory.BLESS_EXP.get_enum_key());
		writeC(value);
	}
	
	// 3단 가속
	public void writeThirdSpeed(){
		writeC(ItemAbilityFactory.THIRD_SPEED.get_enum_key());
		writeC(1);
	}
	
	// 4단 가속
	public void writeFourthSpeed(){
		writeC(ItemAbilityFactory.FOURTH_SPEED.get_enum_key());
		writeC(1);
	}
	
	// 마법 대미지 감소
	public void writeMagicDamageReduction(int value){
		writeC(ItemAbilityFactory.MAGIC_DAMAGE_REDUCTION.get_enum_key());
		writeC(value);
	}
	
	// 이뮨 무시
	public void writeEmunEgnor(int value){
		writeC(ItemAbilityFactory.EMUN_EGNOR.get_enum_key());
		writeD(value);
	}
	
	// 스턴 지속 시간
	public void writeStunDuration(int value){
		writeC(ItemAbilityFactory.STUN_DURATION.get_enum_key());
		writeC(value);
	}
	
	// 대미지 드라니움
	public void writeDranium(int value){
		writeC(ItemAbilityFactory.DRANIUM.get_enum_key());
		writeC(value);
	}
	
	// 대미지 감소 %d%
	public void writeDamageReductionPercent(int value){
		writeC(ItemAbilityFactory.DAMAGE_REDUCTION_PERCENT.get_enum_key());
		writeC(value);
	}
	
	// 효과: 마법 대미지
	public void writeMagicDamage(int value){
		writeC(ItemAbilityFactory.MAGIC_DAMAGE.get_enum_key());
		writeD(value);
	}
	
	// 뱅가드 재사용 시간 감소
	public void writeVanguardTime(int value){
		writeC(ItemAbilityFactory.VANGUARD_TIME.get_enum_key());
		writeD(value);
	}
	
	// 마법 치명타 대미지 상승
	public void writeMagicCriticalDamageAdd(int value){
		writeC(ItemAbilityFactory.MAGIC_CRITICAL_DMG_ADD.get_enum_key());
		writeH(value);
	}
	
	// 반격 회피 무시
	public void writeReflectEmasculate(int value){
		writeC(ItemAbilityFactory.REFLECT_EMASCULATE.get_enum_key());
		writeC(value);
	}
	
	// 상태 이상 시간 증가
	public void writeStrangeTimeIncrease(int value){
		writeC(ItemAbilityFactory.STRANGE_TIME_INCREASE.get_enum_key());
		writeH(value);
	}
	
	// 귀환 불가 시간 증가
	public void writeReturnLockDuraion(int value){
		writeC(ItemAbilityFactory.RETURNLOCK_DURATION.get_enum_key());
		writeH(value);
	}
	
	// 공격 속도 증가
	public void writeAttackSpeedDelayRate(int value){
		writeC(ItemAbilityFactory.ATTACK_SPEED_DELAY_RATE.get_enum_key());
		writeH(value);
	}
	
	// 이동 속도 증가
	public void writeMoveSpeedDelayRate(int value){
		writeC(ItemAbilityFactory.MOVE_SPEED_DELAY_RATE.get_enum_key());
		writeH(value);
	}
	
	// 제한
	public void writeLimit(int value1, int value2){
		writeC(111);
		writeC(value1);
		writeH(value2);
	}
	
	// 제한 시간
	public void writeLimitTime(int value){
		writeC(112);
		writeD(value);
	}
	
	// 삭제 시간
	public void writeDeleteTime(int value){
		writeC(61);
		writeD(value);
	}
	
	// 불가 창고
	public void writeRetrieve(int value){
		writeC(130);
		writeD(value);
	}
	
	// 창고 저장 가능(인챈트 수치)
	public void writeRetrieveEnchantOver(int value){
		writeC(132);
		writeD(value);
	}
	
	// 계정당 구매가능 횟수(1일)
	public void writeAccountBuyLimitDay(int max, int current) {
		writeC(133);
		writeH(max);
		writeH(current);
	}
	
	// 계정당 구매가능 횟수(1주)
	public void writeAccountBuyLimitWeek(int max, int current) {
		writeC(134);
		writeH(max);
		writeH(current);
	}
	
	// 특수:클래스
	public void writeLevelStore(){
		writeC(141);
		writeH(0x19c0);
	}
	
	// 예약(시계 아이콘 표기)
	public void writeScheduled(boolean flag) {
		writeC(145);
		writeC(1);
		writeC(185);
		writeB(flag);
	}
	
	// 판매: 시작날짜 ~ 종료날짜
	// 개수: %d개
	public void writeBuyCount(int value, int start, int end) {
		writeC(147);
		writeD(value);
		writeD(start);
		writeD(0);
		writeD(end);
		writeD(0);
	}
	
	// 불가: 창고 인첸트 %d이상 가능
	public void writeRetrieveEnchant(int value){
		writeC(148);
		writeC(value);
	}
	
	// 잠재력
	public void writePotential(CommonPotentialInfo.BonusInfoT bonus){
		writeC(161);
		writeD(bonus.get_bonus_grade());
		writeD(bonus.get_bonus_desc());
	}
	
	// 혈맹당 구매가능개수(1일)
	public void writePledgeBuyLimitDay(int max, int current){
		writeC(167);
		writeH(max);
		writeH(current);
	}
	
	// 혈맹당 구매가능개수(1주)
	public void writePledgeBuyLimitWeek(int max, int current){
		writeC(168);
		writeH(max);
		writeH(current);
	}
	
	// 안전 인챈트
	public void writeSafeEnchant(int value){
		writeC(169);
		writeC(value);
	}
	
	// 변신
	public void writePolyDesc(int value){
		writeC(ItemAbilityFactory.POLY_DESC.get_enum_key());
		writeH(value);
	}
	
	// PVP대미지 감소 +%d%
	public void writePVPDamageReductionPercent(int value) {
		writeC(ItemAbilityFactory.PVP_DAMAGE_REDUCTION_PERCENT.get_enum_key());
		writeC(value);
	}
	
	// MP절대 회복 +d(64초)
	public void writeMpAbsolRegen64SecondSub(int value){
		writeC(204);
		writeC(value);
	}
	
	// HP절대 회복 +d(32초)
	public void writeHpAbsolRegen32SecondSub(int value){
		writeC(205);
		writeC(value);
	}
	
	// 적용 상태 이상
	public void writeAbnormalStatus(int value) {
		// 더하면 추가된다.
		// 2:석화
		// 32:수면
		// 64:결빙
		// 2048: 스턴
		// 4096: 홀드
		// 8192: 파워그립
		// 16384: 포스스턴
		// 32768: 데스페라도
		// 65536: 이터니티
		// 131072: 데몰리션
		// 262144: 귀환불가
		// 1048576: 텔레포트 불가
		writeC(210);
		writeD(value);
	}
	
	// 상태이상 시 대미지 감소
	public void writeAbnormalStatusDamageReduction(int value) {
		writeC(ItemAbilityFactory.ABNORMAL_STATUS_DAMAGE_REDUCTION.get_enum_key());
		writeC(value);
	}
	
	// 상태이상 시 PvP 대미지 감소
	public void writeAbnormalStatusPVPDamageReduction(int value) {
		writeC(ItemAbilityFactory.ABNORMAL_STATUS_PVP_DAMAGE_REDUCTION.get_enum_key());
		writeC(value);
	}
	
	// PVP 대미지 증가 %
	public void writePVPDamagePercent(int value) {
		writeC(ItemAbilityFactory.PVP_DAMAGE_PERCENT.get_enum_key());
		writeC(value);
	}
	
	// 상태 이상 시간 감소 +%d
	public void writeStrangeTimeDecrease(int value){
		writeC(ItemAbilityFactory.STRANGE_TIME_DECREASE.get_enum_key());
		writeH(value);
	}
	
	// HP 포션 딜레이 감소 +%d
	public void writeHpPotionDelayDecrease(int value) {
		writeC(ItemAbilityFactory.HP_POTION_DELAY_DECREASE.get_enum_key());
		writeH(value);
	}
	
	// HP 포션 크리티컬 확률 +%d%
	public void writeHpPotionCriticalProb(int value) {
		writeC(ItemAbilityFactory.HP_POTION_CRITICAL_PROB.get_enum_key());
		writeH(value);
	}
	
	// 갑옷 마법 발동 확률 +%d%
	public void writeIncreaseArmorSkillProb(int value) {
		writeC(ItemAbilityFactory.INCREASE_ARMOR_SKILL_PROB.get_enum_key());
		writeH(value);
	}
	
	// 슬롯 표기
	public void writeSlot(HashMap<Integer, L1Item> slots) {
		writeC(0xb5);
		writeC(1);
		writeC(0xe6);
		writeC(slots.containsKey(0) ? 0 : 1);// 1번 슬롯 장착: 0, 1번,2번 슬롯 장착: 0, 2번슬롯만 장착: 1
		
		int slotSize = slots.size();
		for (L1Item slot : slots.values()) {
			if (slot.getAcBonus() > 0) {
				writeAcBonus(slot.getAcBonus());
			}
			if (slot.getAddStr() > 0) {
				writeStr(slot.getAddStr());
			}
			if (slot.getAddCon() > 0) {
				writeCon(slot.getAddCon());
			}
			if (slot.getAddDex() > 0) {
				writeDex(slot.getAddDex());
			}
			if (slot.getAddInt() > 0) {
				writeInt(slot.getAddInt());
			}
			if (slot.getAddWis() > 0) {
				writeWis(slot.getAddWis());
			}
			if (slot.getAddCha() > 0) {
				writeCha(slot.getAddCha());
			}
			if (slot.getMr() > 0) {
				writeMagicRegist(slot.getMr());
			}
			if (slot.getAddHp() > 0) {
				writeMaxHp(slot.getAddHp());
			}
			if (slot.getAddMp() > 0) {
				writeMaxMp(slot.getAddMp());
			}
			if (slot.getPotionPercent() > 0 || slot.getPotionValue() > 0) {
				writePotionPercent(slot.getPotionPercent());
				writePotionValue(slot.getPotionValue());
			}
			if (slot.getHprAbsol32Second() > 0) {
				writeHpAbsolRegen32Second(slot.getHprAbsol32Second());
			}
			if (slot.getMprAbsol64Second() > 0) {
				writeMpAbsolRegen64Second(slot.getMprAbsol64Second());
			}
			if (slot.getAttrFire() > 0) {
				writeAttrFire(slot.getAttrFire());
			}
			if (slot.getAttrWind() > 0) {
				writeAttrWind(slot.getAttrWind());
			}
			if (slot.getAttrEarth() > 0) {
				writeAttrEarth(slot.getAttrEarth());
			}
			if (slot.getAttrWater() > 0) {
				writeAttrWater(slot.getAttrWater());
			}
			if (slot.getAttrAll() > 0) {
				writeAttrAll(slot.getAttrAll());
			}
			if (slot.getCarryBonus() > 0) {
				writeCarryBonus(slot.getCarryBonus());
			}
			if (slot.getDamageReduction() > 0) {
				writeDamageReduction(slot.getDamageReduction());
			}
			if (slot.getPVPDamageReduction() > 0) {
				writePVPDamageReduction(slot.getPVPDamageReduction());
			}
			if (slot.getPVPDamageReductionPercent() > 0) {
				writePVPDamageReductionPercent(slot.getPVPDamageReductionPercent());
			}
			if (slot.getPVPMagicDamageReduction() > 0) {
				writePVPMagicDamageReduction(slot.getPVPMagicDamageReduction());
			}
			if (slot.getToleranceSkill() > 0) {
				writeToleranceSkill(slot.getToleranceSkill());
			}
			if (slot.getToleranceSpirit() > 0) {
				writeToleranceSpirit(slot.getToleranceSpirit());
			}
			if (slot.getToleranceDragon() > 0) {
				writeToleranceDragon(slot.getToleranceDragon());
			}
			if (slot.getToleranceFear() > 0) {
				writeToleranceFear(slot.getToleranceFear());
			}
			if (slot.getToleranceAll() > 0) {
				writeToleranceAll(slot.getToleranceAll());
			}
			if (slot.getExpBonus() > 0) {
				writeExpBonus(slot.getExpBonus());
			}
			if (slot.getStrangeTimeDecrease() > 0) {
				writeStrangeTimeDecrease(slot.getStrangeTimeDecrease());
			}
			if (slot.getIncreaseArmorSkillProb() > 0) {
				writeIncreaseArmorSkillProb(slot.getIncreaseArmorSkillProb());
			}
			if (slot.getAttackSpeedDelayRate() > 0) {
				writeAttackSpeedDelayRate(slot.getAttackSpeedDelayRate());
			}
			if (slot.getMoveSpeedDelayRate() > 0) {
				writeMoveSpeedDelayRate(slot.getMoveSpeedDelayRate());
			}
			if (slot.getAbnormalStatusDamageReduction() > 0) {
				writeAbnormalStatus(1316864);
				writeAbnormalStatusDamageReduction(slot.getAbnormalStatusDamageReduction());
			}
			if (slot.getAbnormalStatusPVPDamageReduction() > 0) {
				writeAbnormalStatus(1316864);
				writeAbnormalStatusPVPDamageReduction(slot.getAbnormalStatusPVPDamageReduction());
			}
			if (slot.getPVPDamagePercent() > 0) {
				writePVPDamagePercent(slot.getPVPDamagePercent());
			}
			
			writeC(0xf7);
			writeC(0);
			
			writeC(slotSize == 2 ? 0xe6 : 0xb6);
			writeC(--slotSize);
		}
	}
	
}

