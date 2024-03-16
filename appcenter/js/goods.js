$(function() {
	var searchInput = $('#SearchInput');
	searchInput.focus(function(){
		$(this).addClass('on');
		$(this).val('');
	});
	
	$('.gnb .btn_area .btn_reset').on('click', function(event) {
		searchInput.val('');
		searchInput.focus();
	});
	
	var searchBtn = $('#searchPanel').find('.btn_search');
	searchBtn.click(function() {
		var searchform = document.searchForm;
		if (!searchform.query.value) {
			searchform.query.value='ALL';
		}
		searchform.submit();
	});
	
	var menuBtnArea	= $('.myshop.btn_menu'),
		menuArea	= $('.menu_area');
	menuBtnArea.on('click', function(e) {
		if ($(this).hasClass('on')) {
			$(this).removeClass('on');
			menuArea.css('display', 'none');
			$(this).children('span').html('My N Shop');
		} else {
			$(this).addClass('on');
			menuArea.css('display', 'block');
			$(this).children('span').html('Close');
		}
		eventStop(e);
	});
	
	// 영역밖 클릭
	$('body').mouseup(function (e){
		var searchPanel = $('#searchPanel');
		if (searchInput.has(e.target).length === 0 && searchPanel.has(e.target).length === 0 && searchInput.hasClass('on')) {
			searchInput.removeClass('on');
		}
		
		if (($(e.target).hasClass("input_search") || (!$(e.target).hasClass("on") && menuBtnArea.has(e.target).length === 0 && menuArea.has(e.target).length === 0)) && menuBtnArea.hasClass('on')) {
			menuBtnArea.removeClass('on');
			menuBtnArea.children('span').html('My N Shop');
			menuArea.css('display', 'none');
		}
		eventStop(e);
	});
	
	const buyCountBox = $('.cell .box');
	buyCountBox.find('.counterWrap').children('.count_btn.down').on('click', function(event) {
		countSetting(-1);
	});
	buyCountBox.find('.counterWrap').children('.count_btn.up').on('click', function(event) {
		countSetting(1);
	});
});

function countSetting(num){
	let buyCountBox	= $('.cell .box'),
		price	= $('#goodsPrice').val(),
		saved_point	= $('#goodsSavedPoint').val();
	let countnum	= buyCountBox.find('.count_num');
	let count		= countnum.val();
	if (count == 1 && num == -1) {
		popupShow('The minimum purchase quantity is 1 piece.', '<span class="type2"><a href="javascript:popupClose();" class="close">Ok</a></span>', null);
		return;
	} else if (count == Number(goodsLimitCount) && num == 1) {
		popupShow('The maximum purchase quantity is ' + count + ' piece', '<span class="type2"><a href="javascript:popupClose();" class="close">Ok</a></span>', null);
		return;
	}
	countnum.val(Number(count) + num);
	
	buyCountBox.find('#itemPrice').html(commaAdd(Number(price) * Number(countnum.val())));
	buyCountBox.find('#itemCount').val(commaAdd(Number($('#goodsCount').val()) * Number(countnum.val())));
	if (saved_point != '0') {
		buyCountBox.find('#rewardAmount').html(commaAdd(Number(saved_point) * Number(countnum.val())));
	}
}

function countSettingCheck(obj){
	let buyCountBox	= $('.cell .box'),
		price		= $('#goodsPrice').val(),
		saved_point	= $('#goodsSavedPoint').val();
		countnum	= $(obj);
	if (countnum.val() < 1) {
		countnum.val(1);
		buyCountBox.find('#itemPrice').html(commaAdd(Number(price) * Number(countnum.val())));
		buyCountBox.find('#itemCount').val(commaAdd(Number($('#goodsCount').val()) * Number(countnum.val())));
		return;
	} else if (countnum.val() > Number(goodsLimitCount)) {
		countnum.val(Number(goodsLimitCount));
		buyCountBox.find('#itemPrice').html(commaAdd(Number(price) * Number(countnum.val())));
		buyCountBox.find('#itemCount').val(commaAdd(Number($('#goodsCount').val()) * Number(countnum.val())));
		return;
	}
	buyCountBox.find('#itemPrice').html(commaAdd(Number(price)*Number(countnum.val())));
	buyCountBox.find('#itemCount').val(commaAdd(Number($('#goodsCount').val())*Number(countnum.val())));
	if (saved_point != '0') {
		buyCountBox.find('#rewardAmount').html(commaAdd(Number(saved_point) * Number(countnum.val())));
	}
}

function get_goods(id) {
	for (var i=0; i<goods_items.length; i++) {
		if (id === goods_items[i].id) {
			return goods_items[i];
		}
	}
	return null;
}

function buyValidation(){
	if (!account) {
		popupShow('You can use it after logging in.</br>Do you want to log in?', '<span class="type2"><a href="javascript:login();" class="close">Yes</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">No</a></span>');
		return false;
	}
	return true;
}

function buyCheck(){
	if (!buyValidation()) {
		return;
	}
	if (goodsPriceType === 'NCOIN' && account.ncoin < Number(commaRemove($('.cell .box #itemPrice').html()))) {
		popupShow('You don\'t have enough N Coin.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	if (goodsPriceType === 'NPOINT' &&  account.npoint < Number(commaRemove($('.cell .box #itemPrice').html()))) {
		popupShow('You don\'t have enough N Points', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	popupShow('Are you sure you want to buy?', '<span class="type2"><a href="javascript:goodsBuy();" class="close">Yes</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">No</a></span>');
}

function buyListCheck(id, itemcount, itemprice){
	if (!buyValidation()) {
		return;
	}
	const goods = get_goods(Number(id));
	if (!goods) {
		popupShow('We couldn\'t find any products to buy', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	if (goods.price_type === 'NCOIN' &&  account.ncoin < Number(itemprice)) {
		popupShow('You don\'t have enough N Coin.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	if (goods.price_type === 'NPOINT' &&  account.npoint < Number(itemprice)) {
		popupShow('You don\'t have enough N Points', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	popupShow('Are you sure you want to buy it?', '<span class="type2"><a href="javascript:goodsListBuy(' + id + ', ' + itemcount + ', ' + itemprice + ');" class="close">Yes</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">No</a></span>');
}

function goodsBuy(){
	if (!account) {
		return;
	}
	const id		= $('#goodsId').val(),
		itemCount	= commaRemove($('#itemCount').val()),
		itemPrice	= commaRemove($('#itemPrice').html());
	const senddata	= {"id":id, "itemCount":itemCount, "itemPrice":itemPrice};
	goodsBuyTransfer(senddata);
}

function goodsListBuy(id, itemCount, itemPrice, enchant){
	if (!account) {
		return;
	}
	const senddata = {"id":id, "itemCount":itemCount, "itemPrice":itemPrice};
	goodsBuyTransfer(senddata);
}

var buyLock = false;// 인게임과의 통신시 딜레이 방지
function goodsBuyTransfer(sendParam){
	if (buyLock) {
		return;
	}
	buyLock = true;// 잠금
	$.ajax ({
		type:		"post",
		datatype:	"json",
		url:		"/define/goods/buy",
		data:		sendParam,
		success:function(data){
			switch (data) {
			case 1:
				popupShow('Your purchase has been completed.</br></br>Please check the additional item storage</br>.', '<span class="type2"><a href="javascript:popupCloseReload();" class="close">Close</a></span>', null);
				break;
			case 2:
				popupShow('You can buy it from within the game', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			case 3:
				popupShow('We couldn\'t find your account information.', '<span class="type2"><a href="javascript:loginIngame();" class="close">Login</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 4:
				popupShow('Can\'t find that character', '<span class="type2"><a href="javascript:loginIngame();" class="close">Login</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 5:
				popupShow('Can\'t find that character', '<span class="type2"><a href="javascript:loginIngame();" class="close">Login</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">Close</a></span>');
				break;
			case 6:
				popupShow('The item is not available', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			case 7:
				popupShow('You don\'t have enough N Coin.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			case 8:
				popupShow('You don\'t have enough N Points', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			default:
				popupShow('Purchase failed.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
				break;
			}
			buyLock = false;// 해재
		}, error: function(request, status, error){
			buyLock = false;// 해재
		}
	});
}

function pointbuyCheck(){
	if (!account) {
		popupShow('You can use it after logging in.</br>Do you want to log in?', '<span class="type2"><a href="javascript:login();" class="close">Yes</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">No</a></span>');
		return;
	}
	popupShow('Are you sure you want to buy it?', '<span class="type2"><a href="javascript:ncoinBuy();" class="close">Yes</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">No</a></span>');
}

function ncoinBuy(){
	var count	= commaRemove($('#itemCount').val());
	var price	= commaRemove($('#itemPrice').html());
	ncoinForm(price, count, 'post', '/ncoin/auth');
}

function ncoinForm(price, count, methodtype, actionUrl){
	var f			=	document.ncoinForm;
	f.price.value	=	price;
	f.count.value	=	count;
	f.method		=	methodtype;
	f.action		=	actionUrl;
	f.submit();
}

// =============================================================
// 선물 팝업
// =============================================================
function openNCoinGiftPopup() {
	if (!account) {
		popupShow('You can use it after logging in.</br>Do you want to log in?', '<span class="type2"><a href="javascript:login();" class="close">Yes</a></span>', '<span class="type1"><a href="javascript:popupClose();" class="close">No</a></span>');
		return;
	}
	if (!account.ingame) {
		popupShow('You can only make a gift from within the game', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	if (account.ncoin <= 0) {
		popupShow('You don\'t have enough N Coin.', '<span class="type2"><a href="javascript:popupClose();" class="close">Close</a></span>', null);
		return;
	}
	var ncoinGiftUrl = "/gift/form";
	if (device == "ingame") {
		openPopup(ncoinGiftUrl, 480, 650, 'ncoinGift', 1, 0, false);
	} else {
		openPopup(ncoinGiftUrl , 480, 650, 'giftForm', 1, 0, false);
	}
}