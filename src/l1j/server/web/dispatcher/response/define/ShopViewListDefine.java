package l1j.server.web.dispatcher.response.define;

import java.util.ArrayList;
import java.util.Map;

import l1j.server.server.utils.StringUtil;
import l1j.server.web.SearchConstruct;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.market.MPSECore;
import l1j.server.web.dispatcher.response.market.MPSEElement;
import l1j.server.web.dispatcher.response.market.MarketItemObject;
import l1j.server.web.dispatcher.response.market.MarketLoader;
import l1j.server.web.dispatcher.response.market.MarketSearchObject;
import l1j.server.web.dispatcher.response.market.MarketSearchObject.BlessObject;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class ShopViewListDefine extends HttpJsonModel {
	public ShopViewListDefine() {}
	private ShopViewListDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		Map<String, String> post = request.get_post_datas();
		String tradeType	= post.get("tradeType");
		int enchant			= Integer.parseInt(post.get("enchant"));
		String bless		= post.get("bless");
		if (StringUtil.isNullOrEmpty(bless)) {
			bless = SearchConstruct.SEARCH_ALL;
		}
		String attr			= post.get("attr");
		if (StringUtil.isNullOrEmpty(attr)) {
			attr = SearchConstruct.SEARCH_ALL;
		}
		String searchItem	= post.get("itemId");// 상세보기
		
		MarketSearchObject searchTotalList = new MarketSearchObject();
		
		if (!StringUtil.isNullOrEmpty(searchItem)) {
			MPSECore core = MPSECore.getInstance();
			setSearchSetting(searchItem, searchTotalList, core, tradeType, enchant, bless, attr);
		}
		priceInfo(searchTotalList, tradeType);
		
		return create_response_json(HttpResponseStatus.OK, new Gson().toJson(searchTotalList));
	}
	
	private ArrayList<MarketItemObject> checkItemList(ArrayList<MarketItemObject> list, int enchant, String attr){
		if (list == null || list.isEmpty()) {
			return null;
		}
		ArrayList<MarketItemObject> itemList	= new ArrayList<MarketItemObject>();
		ArrayList<MarketItemObject> deleteList	= new ArrayList<MarketItemObject>(list.size());// 제외할 아이템
		for (MarketItemObject obj : list) {
			if (enchant != -1 && obj.enchant != enchant) {
				deleteList.add(obj);
			} else if (!attr.equals(SearchConstruct.SEARCH_ALL)) {
				int objAttr = obj.attr;
				switch(attr) {
				case SearchConstruct.ATTR_FIRE:
					if (!(objAttr >= 1 && objAttr <= 5)) {
						deleteList.add(obj);
					}
					break;
				case SearchConstruct.ATTR_WATER:
					if (!(objAttr >= 6 && objAttr <= 10)) {
						deleteList.add(obj);
					}
					break;
				case SearchConstruct.ATTR_EARTH:
					if (!(objAttr >= 16 && objAttr <= 20)) {
						deleteList.add(obj);
					}
					break;
				case SearchConstruct.ATTR_WIND:
					if (!(objAttr >= 11 && objAttr <= 15)) {
						deleteList.add(obj);
					}
					break;
				case SearchConstruct.ATTR_NONE:
					if (objAttr > 0) {
						deleteList.add(obj);
					}
					break;
				}
			}
		}
		for (MarketItemObject obj : list) {
			if (!deleteList.contains(obj)) {
				itemList.add(obj);
			}
		}
		deleteList.clear();
		deleteList = null;
		return itemList;
	}
	
	private void setSearchSetting(String searchName, MarketSearchObject searchObj, MPSECore core, String tradeType, int enchant, String bless, String attr) {
		MarketLoader loader = MarketLoader.getInstance();
		
		ArrayList<BlessObject> blessList = new ArrayList<MarketSearchObject.BlessObject>();
		
		MPSEElement element = core.getElement(searchName);// 키워드에 해당하는 아이템
		
		searchObj.setName(element.name);
		searchObj.setGfxId(element.invGfx);
		
		if (bless.equals(SearchConstruct.SEARCH_ALL) || bless.equals(SearchConstruct.BLESS_NORMAL)) {
			ArrayList<MarketItemObject> subNormalList = tradeType.contentEquals(SearchConstruct.SEARCH_SELL) ? loader.getSellings(element.normalId) : loader.getPurchasings(element.normalId);
			if (enchant != -1 || !attr.equals(SearchConstruct.SEARCH_ALL)) {
				subNormalList = checkItemList(subNormalList, enchant, attr);
			}
			setBlessList(subNormalList, blessList, 1);
		}
		
		if (bless.equals(SearchConstruct.SEARCH_ALL) || bless.equals(SearchConstruct.BLESS_BLESS)) {
			ArrayList<MarketItemObject> subBlessList = tradeType.contentEquals(SearchConstruct.SEARCH_SELL) ? loader.getSellings(element.blessId) : loader.getPurchasings(element.blessId);
			if (enchant != -1 || !attr.equals(SearchConstruct.SEARCH_ALL)) {
				subBlessList = checkItemList(subBlessList, enchant, attr);
			}
			setBlessList(subBlessList, blessList, 0);
		}
		
		if (bless.equals(SearchConstruct.SEARCH_ALL) || bless.equals(SearchConstruct.BLESS_CURSE)) {
			ArrayList<MarketItemObject> subCurseList = tradeType.contentEquals(SearchConstruct.SEARCH_SELL) ? loader.getSellings(element.curseId) : loader.getPurchasings(element.curseId);
			if (enchant != -1 || !attr.equals(SearchConstruct.SEARCH_ALL)) {
				subCurseList = checkItemList(subCurseList, enchant, attr);
			}
			setBlessList(subCurseList, blessList, 2);
		}
		
		if (tradeType.contentEquals(SearchConstruct.SEARCH_SELL)) {
			searchObj.setSellList(blessList);
		} else {
			searchObj.setBuyList(blessList);
		}
	}
	
	private void setBlessList(ArrayList<MarketItemObject> list, ArrayList<BlessObject> blesslist, int bless) {
		BlessObject blessObjct = null;
		if (list != null && !list.isEmpty()) {
			blessObjct = new BlessObject();
			blessObjct.bless = bless;
			blessObjct.itemObjList = list;
			blesslist.add(blessObjct);
		}
	}
	
	private void priceInfo(MarketSearchObject searchObj, String tradeType) {
		if (tradeType.contentEquals(SearchConstruct.SEARCH_SELL) && searchObj.getSellList() == null) {
			return;
		}
		if (tradeType.contentEquals(SearchConstruct.SEARCH_BUY) && searchObj.getBuyList() == null) {
			return;
		}
		
		int totalLow	=	2000000000, totalHight	=	0;
		int normalLow	=	2000000000, normalHight	=	0;
		int blessLow	=	2000000000, blessHight	=	0;
		int curseLow	=	2000000000, curseHight	=	0;

		for (BlessObject obj : tradeType.contentEquals(SearchConstruct.SEARCH_SELL) ? searchObj.getSellList() : searchObj.getBuyList()) {
			for (MarketItemObject item : obj.itemObjList) {
				switch(obj.bless) {
				case 0:
					if (item.price < blessLow) {
						blessLow = item.price;
					}
					if (item.price > blessHight) {
						blessHight = item.price;
					}
					if (tradeType.contentEquals(SearchConstruct.SEARCH_SELL)) {
						searchObj.setSellBlessCount(searchObj.getSellBlessCount() + item.count);
					} else {
						searchObj.setBuyBlessCount(searchObj.getBuyBlessCount() + item.count);
					}
					break;
				case 1:
					if (item.price < normalLow) {
						normalLow = item.price;
					}
					if (item.price > normalHight) {
						normalHight = item.price;
					}
					if (tradeType.contentEquals(SearchConstruct.SEARCH_SELL)) {
						searchObj.setSellNormalCount(searchObj.getSellNormalCount() + item.count);
					} else {
						searchObj.setBuyNormalCount(searchObj.getBuyNormalCount() + item.count);
					}
					break;
				case 2:
					if (item.price < curseLow) {
						curseLow = item.price;
					}
					if (item.price > curseHight) {
						curseHight = item.price;
					}
					if (tradeType.contentEquals(SearchConstruct.SEARCH_SELL)) {
						searchObj.setSellCurseCount(searchObj.getSellCurseCount() + item.count);
					} else {
						searchObj.setBuyCurseCount(searchObj.getBuyCurseCount() + item.count);
					}
					break;
				}
				if (item.price < totalLow) {
					totalLow = item.price;
				}
				if (item.price > totalHight) {
					totalHight = item.price;
				}
				
				if (tradeType.contentEquals(SearchConstruct.SEARCH_SELL)) {
					searchObj.setSellTotalCount(searchObj.getSellTotalCount() + item.count);
				} else {
					searchObj.setBuyTotalCount(searchObj.getBuyTotalCount() + item.count);
				}
			}
		}
		
		if (totalLow != 2000000000 && totalHight != 0) {
			if (tradeType.contentEquals(SearchConstruct.SEARCH_SELL)) {
				searchObj.setSellTotalPriceInfo(priceFormat(priceToString(totalLow), priceToString(totalHight)));
				searchObj.setSellTotalPriceArray(searchObj.getSellTotalPriceInfo().replaceAll(StringUtil.EmptyOneString, StringUtil.EmptyString).split("~"));
			} else {
				searchObj.setBuyTotalPriceInfo(priceFormat(priceToString(totalLow), priceToString(totalHight)));
				searchObj.setBuyTotalPriceArray(searchObj.getBuyTotalPriceInfo().replaceAll(StringUtil.EmptyOneString, StringUtil.EmptyString).split("~"));
			}
		}
		if (normalLow != 2000000000 && normalHight != 0) {
			if (tradeType.contentEquals(SearchConstruct.SEARCH_SELL)) {
				searchObj.setSellNormalPriceInfo(priceFormat(priceToString(normalLow), priceToString(normalHight)));
			} else {
				searchObj.setBuyNormalPriceInfo(priceFormat(priceToString(normalLow), priceToString(normalHight)));
			}
		}
		if (blessLow != 2000000000 && blessHight != 0) {
			if (tradeType.contentEquals(SearchConstruct.SEARCH_SELL)) {
				searchObj.setSellBlessPriceInfo(priceFormat(priceToString(blessLow), priceToString(blessHight)));
			} else {
				searchObj.setBuyBlessPriceInfo(priceFormat(priceToString(blessLow), priceToString(blessHight)));
			}
		}
		if (curseLow != 2000000000 && curseHight != 0) {
			if (tradeType.contentEquals(SearchConstruct.SEARCH_SELL)) {
				searchObj.setSellCursePriceInfo(priceFormat(priceToString(curseLow), priceToString(curseHight)));
			} else {
				searchObj.setBuyCursePriceInfo(priceFormat(priceToString(curseLow), priceToString(curseHight)));
			}
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
		return new ShopViewListDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}
}

