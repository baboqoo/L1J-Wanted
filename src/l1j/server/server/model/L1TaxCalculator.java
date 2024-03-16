package l1j.server.server.model;

public class L1TaxCalculator {
	/**
	 * 전쟁세는15% 고정
	 */
	//private static final int WAR_TAX_RATES = 15;
	private static final int WAR_TAX_RATES = 0;

	/**
	 * 국세는10% 고정(지역세에 대한 비율)
	 */
	private static final int NATIONAL_TAX_RATES = 10;

	/**
	 * 디아드세는10% 고정(전쟁세에 대한 비율)
	 */
	private static final int DIAD_TAX_RATES = 10;

	private final int _taxRatesCastle;
	private final int _taxRatesTown;
	private final int _taxRatesWar = WAR_TAX_RATES;

	/**
	 * @param merchantNpcId
	 *            계산 대상 상점의 NPCID
	 */
	public L1TaxCalculator(int merchantNpcId) {
		_taxRatesCastle = L1CastleLocation.getCastleTaxRateByNpcId(merchantNpcId);
		_taxRatesTown = L1TownLocation.getTownTaxRateByNpcid(merchantNpcId);
	}

	public int calcTotalTaxPrice(int price) {
		int taxCastle = price * _taxRatesCastle;
		int taxTown = price * _taxRatesTown;
		int taxWar = price * WAR_TAX_RATES;
		return (taxCastle + taxTown + taxWar) / 100;
	}

	// XXX 개별적으로 계산하기 때문에(위해), 둥근 오차가 나온다.
	public int calcCastleTaxPrice(int price) {
		return (price * _taxRatesCastle) / 100 - calcNationalTaxPrice(price);
	}

	public int calcNationalTaxPrice(int price) {
		return (price * _taxRatesCastle) / 100 / (100 / NATIONAL_TAX_RATES);
	}

	public int calcTownTaxPrice(int price) {
		return (price * _taxRatesTown) / 100;
	}

	public int calcWarTaxPrice(int price) {
		return (price * _taxRatesWar) / 100;
	}

	public int calcDiadTaxPrice(int price) {
		return (price * _taxRatesWar) / 100 / (100 / DIAD_TAX_RATES);
	}

	/**
	 * 과세 후의 가격을 요구한다.
	 * 
	 * @param price
	 *            과세전의 가격
	 * @return 과세 후의 가격
	 */
	public int layTax(int price) {
		return price + calcTotalTaxPrice(price);
	}
}

