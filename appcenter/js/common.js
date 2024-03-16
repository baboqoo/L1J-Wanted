$(function() {
	const body					=	$('body'),
		searchArea				=	$('.ncc-search'),
		searchOpenBtn			=	$('.ncc-search legend'),
		searchCloseBtn			=	$('.ncc-search-close'),
		searchInputArea			=	$('#nccSuggestInput'),
		searchDeleteBtn			=	$('.ncc-search-input-delete'),
		left_button			=	$('.ncc-ncservice-btn'),
		left_close_button			=	$('.ncc-left-panel-close'),
		left_panel				=	$('.ncc-left-panel'),
		right_button			=	$('.ncc-login--after'),
		right_close_button		=	$('.ncc-right-panel-close'),
		right_panel			=	$('.ncc-right-panel'),
		right_char_button		=	$('.ncc-profile-charchange'),
		right_char_close_button	=	$('.ncc-character-panel-close'),
		right_char_panel			=	$('.ncc-character-panel'),
		top_menu				=	$('.menu_item'),
		overlay_screen			=	$('.ncc-dimmed'),
		chage_char				=	$('.ncc-character-list > li'),
		chage_char_2			=	$('.my-character-list .list .items');
	
	// side action
	_show = function(showMenue) {// 출현
		showMenue.addClass('is-active');
		overlay_screen.addClass('is-active-left');
		body.addClass('ncc-dimmed');
	}
	
	_hide = function(hideMenue) {// 감춤
		hideMenue.removeClass('is-active');
		overlay_screen.removeClass('is-active-left');
		body.removeClass('ncc-dimmed');
	}
	
	// left action
	left_button.on('click', function(event) {// 출현
		_show(left_panel);
		eventStop(event);
	});
	left_close_button.on('click', function(event) {// 감춤
		_hide(left_panel);
		eventStop(event);
	});
	
	// right action
	right_button.on('click', function(event) {// 출현
		_show(right_panel);
		eventStop(event);
	});
	right_close_button.on('click', function(event) {// 감춤
		_hide(right_panel);
		eventStop(event);
	});
	
	// right char action
	right_char_button.on('click', function(event) {// 출현
		right_char_panel.addClass('is-active');
		eventStop(event);
	});
	right_char_close_button.on('click', function(event) {// 감춤
		right_char_panel.removeClass('is-active');
		eventStop(event);
	});
	
	// 통합 검색
	searchArea.click(function (e) {
		e.stopPropagation();
	});
	
	searchOpenBtn.on("click", function (e) {
		e.preventDefault();
		showSearchField();
	});
	
	searchCloseBtn.on("click", () => {
		hideSearchField();
		return false;
	});
	
	searchDeleteBtn.on('click', function(e){
		resetSearchField();
		searchInputArea.click().focus();
		return false;
	});
	
	// 영역밖 클릭
	body.on('click', function(e){
		const $tgPoint		= $(e.target);
		var $leftpanel		= $tgPoint.hasClass('ncc-left-panel'),
			$rightpanel		= $tgPoint.hasClass('ncc-right-panel'),
			$rightcharpanel	= $tgPoint.hasClass('ncc-character-panel'),
			$overpanel		= $tgPoint.hasClass('ncc-dimmed');
	    
	    if (!$leftpanel && !$rightpanel && !$rightcharpanel && $overpanel) {
	    	left_panel.removeClass('is-active');
	    	right_panel.removeClass('is-active');
	    	right_char_panel.removeClass('is-active');
	    	overlay_screen.removeClass('is-active-left');
	    	body.removeClass('ncc-dimmed');
	    }
	    
	    if (searchArea.has(e.target).length === 0) {// 통합검색 제거
	    	hideSearchField();
	    }
	});
	
	// 대표케릭터 선택(오른쪽 메뉴)
	chage_char.on('click', function(event) {
		if ($(this).find('span').html() == $('.ncc-login--info__char').html()) {// 동일 케릭명
			right_char_panel.removeClass('is-active');
			eventStop(event);
			return;
		}
		const select_char_choice	= $(this).find('#select_charter_choice').val(),
			currUrl					= window.location.href;
		if(select_char_choice == undefined || select_char_choice == '')return;
		charChangeForm(select_char_choice, currUrl, 'post', '/account/chageCharacter');
	});
	
	// 대표케릭터 선택(마이 페이지)
	chage_char_2.on('click', function(event) {
		if($(this).find('.name').html() == $('.ncc-login--info__char').html()){// 동일 케릭명
			$('.ui-signature .btn').removeClass('open');
			$('.my-character-list').hide();
			return;
		}
		const select_char_choice	= $(this).find('#select_charter_choice').val(),
			currUrl					= window.location.href;
		if(select_char_choice == undefined || select_char_choice == '')return;
		charChangeForm(select_char_choice, currUrl, 'post', '/account/chageCharacter');
	});
	
	// cnb event
	var hoverBar		= $("#nc-cnb .ncc-lnb-hover");
	var hoverBarHalf		= hoverBar.width() >> 1;
	var transeX		= 0, transeW		= 0;
	var cnbType		= typeof pageCnbType === 'undefined' ? undefined : pageCnbType, 
	cnbSubType		= typeof pageCnbSubType === 'undefined' ? undefined : pageCnbSubType;
	if (cnbType !== undefined) {
		$('.ncc-lnb').removeClass('ncc-lnb-type--main');
		$('.ncc-lnb-item.m' + cnbType).addClass('is-active');
		$('.ncc-lnb .m' + cnbType + ' .ncc-lnb-item__sub > .s' + cnbSubType).addClass('is-active');
		const cnbSub	= $('.ncc-lnb .m'+ cnbType + ' .ncc-lnb-item__sub > .s' + cnbSubType);
		transeX		= !cnbSub || !cnbSub.offset() ? 0 : parseInt(cnbSub.offset().left + (cnbSub.width() >> 1) - hoverBarHalf, 10);
		transeW		= !cnbSub || !cnbSub.offset() ? 0 : cnbSub.length ? (94 * cnbSub.width() / 100 * .01 * 1.4).toFixed(2) : .1,
		$('.ncc-lnb-hover').css({
			'transform' : 'translateX(' +  transeX + 'px) scaleX(' + transeW + ')',
			'opacity': '1'
		});
		if (device === 'ingame' || device === 'launcher') {
			pageIngameCnbChange(cnbType, cnbSubType);
		} else {
			pageCnbChange(cnbType, cnbSubType);
		}
	} else if (device === 'ingame' || device === 'launcher') {
		pageIngameCnbChange(cnbType, cnbSubType);
	}
	
	var navi_over = $('.ncc-lnb-type--main');
	$('.ncc-lnb-item').hover(function(){
		$('.ncc-lnb-item').removeClass('is-active');
		$(this).addClass('is-over');
		navi_over.addClass('ncc-lnb-type--main-over');
	}, function(){
		$(this).removeClass('is-over');
		navi_over.removeClass('ncc-lnb-type--main-over');
		if (cnbType !== undefined) {
			$('.ncc-lnb-item.m' + cnbType).addClass('is-active');
		}
	});
	
	var navi_suv = $('.ncc-lnb-item__sub > li');
	navi_suv.hover(function(){
		$('.ncc-lnb-item__sub > li').removeClass('is-active');
		$(this).addClass('is-active');
		const divX		= parseInt($(this).offset().left + ($(this).width() >> 1) - hoverBarHalf, 10),
			divWidth	= $(this).length ? (94 * $(this).width() / 100 * .01 * 1.4).toFixed(2) : .1;
		$('.ncc-lnb-hover').css({
			'transform' : 'translateX(' +  divX + 'px) scaleX(' + divWidth + ')',
			'opacity': '1'
		});
	}, function(){
		$(this).removeClass('is-active');
		$('.ncc-lnb-hover').css({
			'opacity': '0'
		});
		if(cnbType !== undefined){
			$('.ncc-lnb .m' + cnbType + ' .ncc-lnb-item__sub > .s' + cnbSubType).addClass('is-active');
			$('.ncc-lnb-hover').css({
				'transform' : 'translateX(' + transeX + 'px) scaleX(' + transeW + ')',
				'opacity': '1'
			});
		}
	});

	$(window).scroll(function() {
		if($(this).scrollTop() > 100)	$('.is-top').addClass('is-sticky');
		else							$('.is-top').removeClass('is-sticky');
	});

	$(document).ready(function(){
	    $('.scrollbar-macosx').scrollbar();
	});
});

// 통합 검색 추천어 설정
var suggestLock = false;
var suggestTimeLock = false;
var pre_suggest;
function searchInputKeyup(obj, e) {
	if (e.keyCode == 37 || e.keyCode == 39) {// 좌우
		return;
	}
	var searchSuggestForm	= $(obj).closest('#nccSuggestForm');
	var searchSuggestWrap	= searchSuggestForm.find('#nccSuggestWrap');
	var searchSuggestList	= searchSuggestWrap.find('ul');
	var searchDeleteBtn	= searchSuggestForm.children('.ncc-search-input-delete');
	var query = $(obj).val();
	var queryLength = query.length;
	
	if (e.keyCode == 38) {// 상
		nccSuggestDirectionKey(obj, false);
		return;
	}
	if (e.keyCode == 40) {// 하
		nccSuggestDirectionKey(obj, true);
		return;
	}
	if (pre_suggest == query) {
		return;
	}
	pre_suggest = query;
	if (queryLength > 0) {
		searchDeleteBtn.css('display', 'inline');
		if (!suggestLock && !suggestTimeLock && suggestEnable == 'true') {
			suggestTimeLock = true;
			setTimeout(function(){
				var searchQuery = $(obj).val();
				if (searchQuery.length > 0) {
					nccSuggestAjax(searchQuery);
				}
				suggestTimeLock = false;
			}, 1000);
		}
	} else {
		searchSuggestList.children().remove();
		searchSuggestWrap.css('display', 'none');
		searchDeleteBtn.css('display', 'none');
	}
}

function nccSuggestAjax(searchQuery) {
	if (suggestLock) {
		return;
	}
	suggestLock = true;
	$.ajax ({
		type:		'post',
		datatype:	'json',
		url:		'/define/suggest',
		data:		{'query': searchQuery, 'limit': 10, 'type': '0'},
		success:function(data){
		if (data.length > 0) {
			$('#nccSuggestForm #nccSuggestWrap ul').children().remove();
			var suggestHtml = '';
			$.each(data, function(index, item){
				suggestHtml += '<li data-index=\"' + index + '\" data-keyword=\"' + item + '\" onClick="javascript:suggestClick(this);">' + item + '</li>';
			});
			$('#nccSuggestForm #nccSuggestWrap ul').html(suggestHtml);
			$('#nccSuggestForm #nccSuggestWrap').css('display', '');
		}
		suggestLock = false;
		}, error: function(request, status, error){
			suggestLock = false;
		}
	});
}

// 추천 검색어 키보드 방향키에 따른 이벤트
function nccSuggestDirectionKey(obj, updown) {
	var searchSuggestForm = $(obj).closest('#nccSuggestForm');
	var suggestList = searchSuggestForm.find('#nccSuggestWrap > #nccSuggestList > div > ul > li');
	if (suggestList.length > 0) {
		var focusSuggest = searchSuggestForm.find('#nccSuggestWrap > #nccSuggestList > div > ul > .focus');
		if (focusSuggest.length > 0) {
			var curIndex = Number(focusSuggest.attr('data-index'));
			searchSuggestForm.find('#nccSuggestWrap ul li').removeClass('focus');
			searchSuggestForm.find('#nccSuggestWrap ul li[data-index=' + (updown ? curIndex + 1 : curIndex - 1) + ']').addClass('focus');
		} else {
			searchSuggestForm.find('#nccSuggestWrap ul li[data-index=' + (updown ? 0 : suggestList.length - 1) + ']').addClass('focus');
		}
		$(obj).val($('#nccSuggestWrap > #nccSuggestList > div > ul > .focus').attr('data-keyword'));
	}
}

function pageCnbChange(cnbType, cnbSubType){
	const headerName	= $('.wrap-header .header .header-title').html(),
		targetCnb		= $('.ncc-lnb-wrap .ncc-lnb .ncc-lnb-list .ncc-lnb-item.m' + cnbType),
		targetCnbUrl	= targetCnb.children('a').attr('href'),
		targetCnb2Depth	= targetCnb.children('.ncc-lnb-item__sub').children('li');
	
	let targetCnb2DepthHtml = '';
	$.each(targetCnb2Depth, function(index, item){
		targetCnb2DepthHtml += '<span class="ncc-depth2-list-items s' + (index + 1) + '"><a href="' + $(item).children('a').attr('href') + '">' + $(item).find('span').html() + '</a></span>';
	});
	
	const cnbHtml = 
		'<nav class="ncc-lnb-title" style="z-index:1;">' +
    		'<p class="ncc-lnb-title--current"><a href="' + targetCnbUrl + '">' + headerName +'</a></p>' +
    		'<div class="ncc-depth2-list-wrap" id="ncc-depth2-tap"><div class="ncc-depth2-scroller" style="display:inline-block;"><div class="ncc-depth2-list">' + targetCnb2DepthHtml + '</div></div></div>' +
    	'</nav>';
	
	$('.ncc-gnb-wrap').append(cnbHtml);
	$('.ncc-lnb-title .ncc-depth2-list .ncc-depth2-list-items.s' + cnbSubType).addClass('selected');
	$('.ncc-gnb-wrap .ncc-bi').css('display', 'none');
}

function pageIngameCnbChange(cnbType, cnbSubType){
	const targetCnb		= $('.ncc-lnb-wrap .ncc-lnb .ncc-lnb-list .ncc-lnb-item.m' + cnbType),
		targetCnb1Depth = $('.ncc-lnb-wrap .ncc-lnb .ncc-lnb-list .ncc-lnb-item > a'),
		targetCnb2Depth	= targetCnb.children('.ncc-lnb-item__sub').children('li');
	
	let targetCnb1DepthHtml = '';
	$.each(targetCnb1Depth, function(index, item){
		targetCnb1DepthHtml += '<a href="' + $(item).attr('href') + '">' + $(item).find('span').html() + '</a>';
	});
	
	let targetCnb2DepthHtml = '';
	$.each(targetCnb2Depth, function(index, item){
		targetCnb2DepthHtml += '<span class="ncc-depth2-list-items s' + (index + 1) + '"><a href="' + $(item).children('a').attr('href') + '">' + $(item).find('span').html() + '</a></span>';
	});
	
	const ingameCnbHtml = 
		'<nav class="ncc-lnb-title" style="z-index:1;">' +
    		'<p class="ncc-lnb-title--current">' + '<a href="/index">Home</a>' + targetCnb1DepthHtml + '</p>' +
			(targetCnb2DepthHtml.length > 0 ? '<div class="ncc-depth2-list-wrap" id="ncc-depth2-tap"><div class="ncc-depth2-scroller" style="display:inline-block;"><div class="ncc-depth2-list">' + targetCnb2DepthHtml + '</div></div></div>' : '') +
    	'</nav>';
	$('.ncc-gnb-wrap').append(ingameCnbHtml);
	
	if (cnbType !== undefined) {
		const indexNumber = Number(cnbType) - (Number(cnbType) > 4 ? 1 : 0);
		$('.ncc-lnb-title .ncc-lnb-title--current > a').eq(indexNumber).addClass('selected');
		$('.ncc-lnb-title .ncc-depth2-list .ncc-depth2-list-items.s' + cnbSubType).addClass('selected');
	}
	$('.ncc-gnb-wrap .ncc-bi').css('display', 'none');
}

// 페이징 이동 처리를 위한 세션 생성
function createSessionConfig(actionUrl, types) {
	switch(actionUrl){
	case '/notice/view':
		communityInfiniteList = {
			board: 'notice',
			type: types,
			list_count: $('#cursize').val(),
			scroll_Y: window.scrollY
		};
		setSession('COMMUNITY_INFINITE_LIST', JSON.stringify(communityInfiniteList));
		break;
	case '/board/view':
		communityInfiniteList = {
			board: 'free',
			list_count: $('#cursize').val(),
			scroll_Y: window.scrollY
		};
		setSession('COMMUNITY_INFINITE_LIST', JSON.stringify(communityInfiniteList));
		break;
	case '/contents/view':
		communityInfiniteList = {
			board: 'content',
			list_count: $('#cursize').val(),
			scroll_Y: window.scrollY
		};
		setSession('COMMUNITY_INFINITE_LIST', JSON.stringify(communityInfiniteList));
		break;
	case '/pitch/view':
		communityInfiniteList = {
			board: 'pitch',
			list_count: $('#cursize').val(),
			scroll_Y: window.scrollY
		};
		setSession('COMMUNITY_INFINITE_LIST', JSON.stringify(communityInfiniteList));
		break;
	default:
		break;
	}
}

function urlform(num, methodtype, actionUrl){
	createSessionConfig(actionUrl);
	var form = document.createElement("form");
	form.setAttribute('charset', 'UTF-8');
	form.setAttribute('method', methodtype);
	form.setAttribute('action', actionUrl);
	
	if (num != undefined && num != 'null') {
		var hiddenField = document.createElement('input');
		hiddenField.setAttribute('type', 'hidden');
		hiddenField.setAttribute('name', 'num');
		hiddenField.setAttribute('value', num);
		form.appendChild(hiddenField);
	}
	document.body.appendChild(form);
	form.submit();
}

function urlTypeform(type, num, methodtype, actionUrl){
	createSessionConfig(actionUrl, type);
	var form = document.createElement("form");
	form.setAttribute('charset', 'UTF-8');
	form.setAttribute('method', methodtype);
	form.setAttribute('action', actionUrl);
	
	if (type != undefined && type != 'null') {
		var hiddenField = document.createElement('input');
		hiddenField.setAttribute('type', 'hidden');
		hiddenField.setAttribute('name', 'type');
		hiddenField.setAttribute('value', type);
		form.appendChild(hiddenField);
	}
	
	if (num != undefined && num != 'null') {
		var hiddenField = document.createElement('input');
		hiddenField.setAttribute('type', 'hidden');
		hiddenField.setAttribute('name', 'num');
		hiddenField.setAttribute('value', num);
		form.appendChild(hiddenField);
	}
	
	document.body.appendChild(form);
	form.submit();
}

function urlQueryform(query, methodtype, actionUrl){
	var form = document.createElement("form");
	form.setAttribute('charset', 'UTF-8');
	form.setAttribute('method', methodtype);
	form.setAttribute('action', actionUrl);
	
	if (query != undefined && query != 'null') {
		var hiddenField = document.createElement('input');
		hiddenField.setAttribute('type', 'hidden');
		hiddenField.setAttribute('name', 'query');
		hiddenField.setAttribute('value', query);
		form.appendChild(hiddenField);
	}
	
	document.body.appendChild(form);
	form.submit();
}

// 주 케릭터 변경
function charChangeForm(num, urlType, methodtype, actionUrl){
	var form = document.createElement("form");
	form.setAttribute('charset', 'UTF-8');
	form.setAttribute('method', methodtype);
	form.setAttribute('action', actionUrl);
	
	if (urlType != undefined && urlType != 'null') {
		var hiddenField = document.createElement('input');
		hiddenField.setAttribute('type', 'hidden');
		hiddenField.setAttribute('name', 'urlType');
		hiddenField.setAttribute('value', urlType);
		form.appendChild(hiddenField);
	}
	
	if (num != undefined && num != 'null') {
		var hiddenField = document.createElement('input');
		hiddenField.setAttribute('type', 'hidden');
		hiddenField.setAttribute('name', 'num');
		hiddenField.setAttribute('value', num);
		form.appendChild(hiddenField);
	}
	
	document.body.appendChild(form);
	form.submit();
}

// 로그인 처리
function login(){
	var form = document.createElement("form");
	form.setAttribute('charset', 'UTF-8');
	form.setAttribute('method', 'post');
	if (device === 'ingame') {
		form.setAttribute('action', '/login_ingame');
	} else {
		form.setAttribute('action', '/login');
	}
	
	var hiddenField = document.createElement('input');
	hiddenField.setAttribute('type', 'hidden');
	hiddenField.setAttribute('name', 'urlType');
	hiddenField.setAttribute('value', window.location.href);
	form.appendChild(hiddenField);
	
	document.body.appendChild(form);
	form.submit();
}

// 로그인 처리(인게임)
function loginIngame(){
	var form = document.createElement("form");
	form.setAttribute('charset', 'UTF-8');
	form.setAttribute('method', 'post');
	form.setAttribute('action', '/login_ingame');
	
	var hiddenField = document.createElement('input');
	hiddenField.setAttribute('type', 'hidden');
	hiddenField.setAttribute('name', 'urlType');
	hiddenField.setAttribute('value', window.location.href);
	form.appendChild(hiddenField);
	
	document.body.appendChild(form);
	form.submit();
}

// 로그아웃 처리
function logout(){
	if (device === 'launcher') {
		$.ajax ({
			type:		'GET',
			datatype:		'json',
			url:		'/define/logout',
			success:function(data){
				console.log(data);
			}, error: function(request, status, error){
				console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	} else {
		location.href='/account/logout';
	}
}

// 통합 검색 버튼 클릭 이벤트
function totalSearchCheck(){
	if (!nccSuggestForm.query.value) {
		return false;
	}
	nccSuggestForm.action = '/search';
	return true;
}

// 통합 검색 추천어 클릭 이벤트
function suggestClick(obj) {
	nccSuggestForm.query.value = $(obj).attr('data-keyword');
	nccSuggestForm.action = '/search';
	nccSuggestForm.submit();
}

// 통합 검색 인풋 표시
function showSearchField() {
    if (!$('.ncc-search').hasClass('is-active')) {
    	$('.ncc-search').addClass('is-active');
        //$('.ncc-search legend').fadeOut();
        $('.ncc-search legend').css('display', 'none');
        $('.ncc-lnb').addClass('search-active');
        window.setTimeout(() => {
        	$('.ncc-search-input').click().focus();
        }, 300);
    }
}

// 통합 검색 인풋 숨기기
function hideSearchField() {
	if ($('.ncc-search').hasClass('is-active')) {
		$('.ncc-search').removeClass('is-active');
	    $('.ncc-search legend').fadeIn();
	    $('.ncc-lnb').removeClass('search-active');
	    this.resetSearchField();
	}
}

// 통합 검색 인풋 초기화
function resetSearchField() {
    $('#nccSuggestWrap').css('display', 'none');
    $('#nccSuggestWrap ul').children().remove();
    $('.ncc-search-input').val('');
    $('.ncc-search-input-delete').hide();
}

// 알림 내역 삭제
function alimDelete(id){
	$.ajax ({
		type:		'post',
		datatype:	'json',
		url:		'/define/alim/delete',
		data:		{'id': id},
		success:function(data){
			$('#notiList #alim_' + id).remove();
			if (data.length <= 0) {
				$('.ncc-noti-tab span').css('display', 'none');
				$('#notiList ul').append('<li class="on"><div class="wrapNotice"><a href="javascript:;"><p class="noticeMsg">No notifications.</p><div class="thumb"><img class="icon" src="/img/27009.png" alt="nickname"></div></a></div></li>');
			} else {
				$('.ncc-noti-tab span').html(data.length);
			}
		}, error: function(request, status, error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}

// 윈도우 팝업 생성
function openPopup(obj,objWidth,objHeight,objName,objScroll,deny,objFull,addParam){
	try {
		if (objScroll !== 1 && objScroll !==0 && objScroll !== '1' && objScroll !=='0') {
			var objScrollCopy=objScroll;
			objScroll=objName;
			objName=objScrollCopy;
		}

		var isMac = navigator.platform.toUpperCase().indexOf('MAC')>=0;
		if (isMac) {
			var ih = objHeight+22;
		} else {
			var ih = objHeight;
		}
		if (typeof(obj)=='string') {
			var setup="width="+objWidth+",height="+objHeight+", innerHeight="+ih+",toolbar=no,location=no,status=no,menubar=no,top=20,left=20,scrollbars="+objScroll+",resizable=no";
			if(objName==""||!objName)objName="popup";
			if(objFull)setup="fullscreen=1,scrollbars=0";
			var win=window.open(obj,objName,setup);
			if(win!=null)
			win.focus();
			return win;
		}
		if(!objName)objName="popup";
		if(!objScroll)objScroll="auto";
		var url=addParam?obj.href+'?'+addParam:obj.href;
		var setup="width="+objWidth+",height="+objHeight+", innerHeight="+ih+",toolbar=no,location=no,status=no,menubar=no,top=20,left=20,scrollbars="+objScroll+",resizable=no";
		if(objFull)setup="fullscreen=1,scrollbars=0";
		var win=window.open(url,objName,setup);
		if(deny){
		//if(win==null)alert('팝업 차단을 해제하여 주시기 바랍니다.');
		if (win == null) alert('Please disable the popup blocker.');
		else win.focus();
		}

		return win;
	}
	catch(e){}
}

// 런처에서 계정 정보 요청
function get_account_from_launcher() {
	const result = {
		"NCOIN": !account ? 0 : commaAdd(account.ncoin),
		"NPOINT": !account ? 0 : commaAdd(account.npoint)
	};
	return result;
}

// 런처 게임시작 승인 요청
function get_gamestart_auth_from_launcher(token) {
	if (device !== 'launcher') {
		return {"AUTH_CODE": 2};
	}
	if (!account) {
		return {"AUTH_CODE": 3};
	}
	if (!token) {
		return {"AUTH_CODE": 5};
	}

	let auth_code = 0;
	$.ajax({
      		url: '/define/gamestart',
      		type: "GET",
      		dataType: "json",
      		data: {"token": token},
		async: false,
      		success: (res) => {
        			auth_code = res;
      		}
    	});
	return {"AUTH_CODE": auth_code};
}