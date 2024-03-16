package l1j.server.web.dispatcher.response.define;

import java.util.ArrayList;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.item.ItemDAO;
import l1j.server.web.dispatcher.response.market.MPSECore;
import l1j.server.web.dispatcher.response.market.MPSEElement;
import l1j.server.web.dispatcher.response.market.MarketEnchatObject;
import l1j.server.web.dispatcher.response.market.MarketItemObject;
import l1j.server.web.dispatcher.response.market.MarketLoader;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class ShopListDefine extends HttpJsonModel {
	private static final int MAX_VALUE = 2000000000;
	private static final int MIN_VALUE = 0;
	
	public ShopListDefine() {}
	private ShopListDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		String keyword = request.read_post("keyword");// 검색한 아이템 명
		if (StringUtil.isNullOrEmpty(keyword)) {
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		ArrayList<MarketEnchatObject> searchTotalList = new ArrayList<MarketEnchatObject>();// view로 보낼 데이터
		int enchant		= -1;
		if (keyword.startsWith(StringUtil.PlusString)) {
			keyword = keyword.replace(StringUtil.PlusString, StringUtil.EmptyString);
		}
		String startNum	= keyword.substring(0, 1);
		boolean check	= StringUtil.isNumber(startNum);
		if (check) {
			enchant = Integer.parseInt(startNum);
			keyword = keyword.replaceFirst(startNum, StringUtil.EmptyString);
			if (keyword.startsWith(StringUtil.EmptyOneString)) {
				keyword = keyword.replaceFirst(StringUtil.EmptyOneString, StringUtil.EmptyString);
			}
		}
		String searchKeyword = ItemDAO.getSearchKeyword(keyword);// 속어 검색
		if (!StringUtil.isNullOrEmpty(searchKeyword)) {
			keyword = searchKeyword;
		}
		
		MPSECore core = MPSECore.getInstance();
		ArrayList<String> searchList = core.getKeyworlds(keyword);// 키워드에 해당하는 아이템이름 리스트
		setSearchSetting(searchList, searchTotalList, core, enchant);
		
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(searchTotalList));
	}
	
	private void setSearchSetting(ArrayList<String> searchList, ArrayList<MarketEnchatObject> searchTotalList, MPSECore core, int enchant) {
		if (searchList != null && !searchList.isEmpty()) {
			MarketLoader loader = MarketLoader.getInstance();
			MarketEnchatObject searchObj = null;
			for (String itemName : searchList) {
				MPSEElement element = core.getElement(itemName);// 키워드에 해당하는 아이템
				
				ArrayList<MarketItemObject> subSellNormalList = loader.getSellings(element.normalId);// 판매중인 일반 아이템 리스트
				ArrayList<MarketItemObject> subSellBlessList = loader.getSellings(element.blessId);// 판매중인 축복 아이템 리스트
				ArrayList<MarketItemObject> subSellCurseList = loader.getSellings(element.curseId);// 판매중인 저주 아이템 리스트
				
				ArrayList<MarketItemObject> subBuyNormalList = loader.getPurchasings(element.normalId);// 구매중인 일반 아이템 리스트
				ArrayList<MarketItemObject> subBuyBlessList = loader.getPurchasings(element.blessId);// 구매중인 축복 아이템 리스트
				ArrayList<MarketItemObject> subBuyCurseList = loader.getPurchasings(element.curseId);// 구매중인 저주 아이템 리스트
				
				if (enchant != -1) {// 인첸트 조건에 따라 제거
					checkItemList(subSellNormalList, enchant);
					checkItemList(subSellBlessList, enchant);
					checkItemList(subSellCurseList, enchant);
					checkItemList(subBuyNormalList, enchant);
					checkItemList(subBuyBlessList, enchant);
					checkItemList(subBuyCurseList, enchant);
				}
				
				if ((subSellNormalList == null || subSellNormalList.isEmpty()) 
						&& (subSellBlessList == null || subSellBlessList.isEmpty()) 
						&& (subSellCurseList == null || subSellCurseList.isEmpty()) 
						&& (subBuyNormalList == null || subBuyNormalList.isEmpty()) 
						&& (subBuyBlessList == null || subBuyBlessList.isEmpty())
						&& (subBuyCurseList == null || subBuyCurseList.isEmpty())) {
					continue;
				}
				
				for (int i=0; i<16; i++) {// 인첸트 수치별로 반복
					int sellNormalCount = 0;// 판매중인 일반 개수
					int sellBlessCount = 0;// 판매중인 축복 개수
					int sellCurseCount = 0;// 판매중인 저주 개수
					int buyNormalCount = 0;// 구매중인 일반 개수
					int buyBlessCount = 0;// 구매중인 축복 개수
					int buyCurseCount = 0;// 구매중인 저주 개수
					
					// 인첸트 수치별로 개수 체크
					if (subSellNormalList != null && !subSellNormalList.isEmpty()) {
						for (MarketItemObject obj : subSellNormalList) {
							if (obj.enchant == i) {
								sellNormalCount++;
							}
						}
					}
					if (subSellBlessList != null && !subSellBlessList.isEmpty()) {
						for (MarketItemObject obj : subSellBlessList) {
							if (obj.enchant == i) {
								sellBlessCount++;
							}
						}
					}
					if (subSellCurseList != null && !subSellCurseList.isEmpty()) {
						for (MarketItemObject obj : subSellCurseList) {
							if (obj.enchant == i) {
								sellCurseCount++;
							}
						}
					}
					
					if (subBuyNormalList != null && !subBuyNormalList.isEmpty()) {
						for (MarketItemObject obj : subBuyNormalList) {
							if (obj.enchant == i) {
								buyNormalCount++;
							}
						}
					}
					if (subBuyBlessList != null && !subBuyBlessList.isEmpty()) {
						for (MarketItemObject obj : subBuyBlessList) {
							if (obj.enchant == i) {
								buyBlessCount++;
							}
						}
					}
					if (subBuyCurseList != null && !subBuyCurseList.isEmpty()) {
						for (MarketItemObject obj : subBuyCurseList) {
							if (obj.enchant == i) {
								buyCurseCount++;
							}
						}
					}
					
					int sellTotalCount = sellNormalCount + sellBlessCount + sellCurseCount;// 판매중인 총 개수
					int buyTotalCount = buyNormalCount + buyBlessCount + buyCurseCount;// 구매중인 총 개수
					
					if (sellTotalCount + buyTotalCount <= 0) {// 아무것도 없을경우
						continue;
					}
					
					searchObj = new MarketEnchatObject();
					searchObj.setName(element.name);
					searchObj.setGfxId(element.invGfx);
					searchObj.setEnchant(i);
					
					searchObj.setSellTotalCount(sellTotalCount);
					searchObj.setBuyTotalCount(buyTotalCount);
					
					searchObj.setSellNormalCount(sellNormalCount);
					searchObj.setSellBlessCount(sellBlessCount);
					searchObj.setSellCurseCount(sellCurseCount);
					
					searchObj.setBuyNormalCount(buyNormalCount);
					searchObj.setBuyBlessCount(buyBlessCount);
					searchObj.setBuyCurseCount(buyCurseCount);
					
					int sellTotalLowPrice = MAX_VALUE;// 판매중인 최저 가격
					int sellNormalLowPrice = MAX_VALUE;// 판매중인 일반 최저 가격
					int sellBlessLowPrice = MAX_VALUE;// 판매중인 축복 최저 가격
					int sellCurseLowPrice = MAX_VALUE;// 판매중인 저주 최저 가격
					
					int sellTotalHightPrice = MIN_VALUE;// 판매중인 최대 가격
					int sellNormalHightPrice = MIN_VALUE;// 판매중인 일반 최대 가격
					int sellBlessHightPrice = MIN_VALUE;// 판매중인 축복 최대 가격
					int sellCurseHightPrice = MIN_VALUE;// 판매중인 저주 최대 가격
					
					int buyTotalLowPrice = MAX_VALUE;// 구매중인 최저 가격
					int buyNormalLowPrice = MAX_VALUE;// 구매중인 일반 최저 가격
					int buyBlessLowPrice = MAX_VALUE;// 구매중인 축복 최저 가격
					int buyCurseLowPrice = MAX_VALUE;// 구매중인 저주 최저 가격
					
					int buyTotalHightPrice = MIN_VALUE;// 구매중인 최대 가격
					int buyNormalHightPrice = MIN_VALUE;// 구매중인 일반 최대 가격
					int buyBlessHightPrice = MIN_VALUE;// 구매중인 축복 최대 가격
					int buyCurseHightPrice = MIN_VALUE;// 구매중인 저주 최대 가격
					
					// 판매 가격
					if (subSellNormalList != null && !subSellNormalList.isEmpty()) {
						for (MarketItemObject obj : subSellNormalList) {
							if (obj.enchant == i) {
								if (obj.price < sellNormalLowPrice) {
									sellNormalLowPrice = obj.price;
								}
								if (obj.price > sellNormalHightPrice) {
									sellNormalHightPrice = obj.price;
								}
								if (obj.price < sellTotalLowPrice) {
									sellTotalLowPrice = obj.price;
								}
								if (obj.price > sellTotalHightPrice) {
									sellTotalHightPrice = obj.price;
								}
							}
						}
					}
					if (subSellBlessList != null && !subSellBlessList.isEmpty()) {
						for (MarketItemObject obj : subSellBlessList) {
							if (obj.enchant == i) {
								if (obj.price < sellBlessLowPrice) {
									sellBlessLowPrice = obj.price;
								}
								if (obj.price > sellBlessHightPrice) {
									sellBlessHightPrice = obj.price;
								}
								if (obj.price < sellTotalLowPrice) {
									sellTotalLowPrice = obj.price;
								}
								if (obj.price > sellTotalHightPrice) {
									sellTotalHightPrice = obj.price;
								}
							}
						}
					}
					if (subSellCurseList != null && !subSellCurseList.isEmpty()) {
						for (MarketItemObject obj : subSellCurseList) {
							if (obj.enchant == i) {
								if (obj.price < sellCurseLowPrice) {
									sellCurseLowPrice = obj.price;
								}
								if (obj.price > sellCurseHightPrice) {
									sellCurseHightPrice = obj.price;
								}
								if (obj.price < sellTotalLowPrice) {
									sellTotalLowPrice = obj.price;
								}
								if (obj.price > sellTotalHightPrice) {
									sellTotalHightPrice = obj.price;
								}
							}
						}
					}
					
					// 구매 가격
					if (subBuyNormalList != null && !subBuyNormalList.isEmpty()) {
						for (MarketItemObject obj : subBuyNormalList) {
							if (obj.enchant == i) {
								if (obj.price < buyNormalLowPrice) {
									buyNormalLowPrice = obj.price;
								}
								if (obj.price > buyNormalHightPrice) {
									buyNormalHightPrice = obj.price;
								}
								if (obj.price < buyTotalLowPrice) {
									buyTotalLowPrice = obj.price;
								}
								if (obj.price > buyTotalHightPrice) {
									buyTotalHightPrice = obj.price;
								}
							}
						}
					}
					if (subBuyBlessList != null && !subBuyBlessList.isEmpty()) {
						for (MarketItemObject obj : subBuyBlessList) {
							if (obj.enchant == i) {
								if (obj.price < buyBlessLowPrice) {
									buyBlessLowPrice = obj.price;
								}
								if (obj.price > buyBlessHightPrice) {
									buyBlessHightPrice = obj.price;
								}
								if (obj.price < buyTotalLowPrice) {
									buyTotalLowPrice = obj.price;
								}
								if (obj.price > buyTotalHightPrice) {
									buyTotalHightPrice = obj.price;
								}
							}
						}
					}
					if (subBuyCurseList != null && !subBuyCurseList.isEmpty()) {
						for (MarketItemObject obj : subBuyCurseList) {
							if (obj.enchant == i) {
								if (obj.price < buyCurseLowPrice) {
									buyCurseLowPrice = obj.price;
								}
								if (obj.price > buyCurseHightPrice) {
									buyCurseHightPrice = obj.price;
								}
								if (obj.price < buyTotalLowPrice) {
									buyTotalLowPrice = obj.price;
								}
								if (obj.price > buyTotalHightPrice) {
									buyTotalHightPrice = obj.price;
								}
							}
						}
					}
					
					if (sellTotalLowPrice != MAX_VALUE && sellTotalHightPrice != MIN_VALUE) {
						searchObj.setSellTotalPriceInfo(priceFormat(priceToString(sellTotalLowPrice), priceToString(sellTotalHightPrice)));
					}
					if (sellNormalLowPrice != MAX_VALUE && sellNormalHightPrice != MIN_VALUE) {
						searchObj.setSellNormalPriceInfo(priceFormat(priceToString(sellNormalLowPrice), priceToString(sellNormalHightPrice)));
					}
					if (sellBlessLowPrice != MAX_VALUE && sellBlessHightPrice != MIN_VALUE) {
						searchObj.setSellBlessPriceInfo(priceFormat(priceToString(sellBlessLowPrice), priceToString(sellBlessHightPrice)));
					}
					if (sellCurseLowPrice != MAX_VALUE && sellCurseHightPrice != MIN_VALUE) {
						searchObj.setSellCursePriceInfo(priceFormat(priceToString(sellCurseLowPrice), priceToString(sellCurseHightPrice)));
					}
					
					if (buyTotalLowPrice != MAX_VALUE && buyTotalHightPrice != MIN_VALUE) {
						searchObj.setBuyTotalPriceInfo(priceFormat(priceToString(buyTotalLowPrice), priceToString(buyTotalHightPrice)));
					}
					if (buyNormalLowPrice != MAX_VALUE && buyNormalHightPrice != MIN_VALUE) {
						searchObj.setBuyNormalPriceInfo(priceFormat(priceToString(buyNormalLowPrice), priceToString(buyNormalHightPrice)));
					}
					if (buyBlessLowPrice != MAX_VALUE && buyBlessHightPrice != MIN_VALUE) {
						searchObj.setBuyBlessPriceInfo(priceFormat(priceToString(buyBlessLowPrice), priceToString(buyBlessHightPrice)));
					}
					if (buyCurseLowPrice != MAX_VALUE && buyCurseHightPrice != MIN_VALUE) {
						searchObj.setBuyCursePriceInfo(priceFormat(priceToString(buyCurseLowPrice), priceToString(buyCurseHightPrice)));
					}
					
					searchTotalList.add(searchObj);
				}
			}
		}
	}
	
	private void checkItemList(ArrayList<MarketItemObject> list, int enchant){
		if (list == null || list.isEmpty()) {
			return;
		}
		ArrayList<MarketItemObject> deleteList = new ArrayList<MarketItemObject>(list.size());
		for (MarketItemObject obj : list) {
			if (enchant != -1 && obj.enchant != enchant) {
				deleteList.add(obj);
			}
		}
		for (MarketItemObject obj : deleteList) {
			list.remove(obj);
		}
	}
	
	private static final String FORMAT_REG = "%s ~ %s";
	private String priceFormat(String low, String hight) {
		return String.format(FORMAT_REG, low, hight);
	}
	
	private String priceToString(int value) {
		if (value <= 0) {
			return StringUtil.ZeroString;
		}
		StringBuilder sb = new StringBuilder();
		if (value > 100000000) {
			int aa = value / 100000000;
			value -= aa * 100000000;
			//sb.append(StringUtil.comma(aa)).append("억");
			sb.append(StringUtil.comma(aa)).append(" billion");
		}
		if (value > 10000) {
			int aa = value / 10000;
			value -= aa * 10000;
			//sb.append(StringUtil.EmptyOneString).append(StringUtil.comma(aa)).append("만");
			sb.append(StringUtil.EmptyOneString).append(StringUtil.comma(aa)).append(" ten thousand");
		}
		if (value > 0) {
			sb.append(StringUtil.EmptyOneString).append(StringUtil.comma(value));
		}
		return sb.toString();
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new ShopListDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

