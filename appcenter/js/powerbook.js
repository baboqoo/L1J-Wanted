$(function() {
	$('.recommend__list').slick({
        dots:			true,
        arrows:			false,
        infinite:		true,
        slidesToShow:	1,
        slidesToScroll:	1
	});
	$('.recommend__controller-prev').on('click', function (e) {
		$('.recommend__list').slick('slickPrev');
	});
	$('.recommend__controller-next').on('click', function (e) {
		$('.recommend__list').slick('slickNext');
	});
	
	var guideSlick = $('.guide__list');
	var slickOptions = {
		dots:			false,
		arrows:			false,
		infinite:		true,
		slidesToShow:	1,
		slidesToScroll:	1	
	}
	
	let slick_flag = false;
	if ($(window).width() < 640) {
		guideSlick.not('.slick-initialized').slick(slickOptions);
		slick_flag = true;
	}
	
	$(window).resize(function (){
		if (!slick_flag && $(window).width() < 640) {
			guideSlick.not('.slick-initialized').slick(slickOptions);
			slick_flag = true;
		} else {
			if (slick_flag) {
				guideSlick.slick("unslick");
				slick_flag = false;
			}
		}
	})
	
	let searchInputArea	= $('#suggestForm #suggestInput'),
		searchFormArea	= $('#suggestForm'),
		searchResetBtn	= $('#suggestForm #suggest_delete'),
		searchSubmitBtn	= $('#suggestForm #suggestSubmit');
	searchResetBtn.on('click', function(e){
		searchInputArea.val('');
		searchInputArea.focus();
		$(this).css('display', 'none');
		$('#suggestForm .suggest_wrap > div > div > ul').children().remove();
		$('#suggestForm .suggest_wrap').css('display', 'none');
	});
	searchSubmitBtn.on('click', function(e){
		let suggestForm = document.suggestForm;
		if(!suggestForm.query.value)return;
		suggestForm.submit();
	});

	searchInputArea.on('focus', function(e){
		if ($('.suggest_list > div > ul > li').length > 0) {
			$('.suggest_wrap').css('display', 'block');
		} else {
			var query = searchInputArea.val();
			if (query.length > 0) {
				sub_suggestAjax(query);
			}
		}
	});

	// 영역밖 클릭
	$('body').on('click', function(e){
		if (searchFormArea.has(e.target).length === 0) {// 통합검색 제거
			$('#suggestForm .suggest_wrap').css('display', 'none');
		}
	});
});

// 검색 추천어 설정
var sub_suggestLock = false;
var sub_suggestTimeLock = false;
var sub_pre_suggest;
function sub_searchInputKeyup(obj, e) {
	if (e.keyCode === 37 || e.keyCode === 39) {// 좌우
		return;
	}
	var searchSuggestForm	= $(obj).closest('#suggestForm');
	var searchSuggestWrap	= searchSuggestForm.find('.suggest_wrap');
	var searchSuggestList	= searchSuggestWrap.find('ul');
	var searchDeleteBtn	= searchSuggestForm.children('#suggest_delete');
	var query = $(obj).val();
	var queryLength = query.length;
	
	if (e.keyCode === 38) {// 상
		sub_suggestDirectionKey(obj, false);
		return;
	}
	if (e.keyCode === 40) {// 하
		sub_suggestDirectionKey(obj, true);
		return;
	}
	if (sub_pre_suggest === query) {
		return;
	}
	sub_pre_suggest = query;
	if (queryLength > 0) {
		searchDeleteBtn.css('display', 'inline');
		if (!sub_suggestLock && !sub_suggestTimeLock && suggestEnable === 'true') {
			sub_suggestTimeLock = true;
			setTimeout(function(){
				var searchQuery = $(obj).val();
				if (searchQuery.length > 0) {
					sub_suggestAjax(searchQuery);
				}
				sub_suggestTimeLock = false;
			}, 1000);
		}
	} else {
		searchSuggestList.children().remove();
		searchSuggestWrap.css('display', 'none');
		searchDeleteBtn.css('display', 'none');
	}
}

function sub_suggestAjax(searchQuery) {
	if (sub_suggestLock) {
		return;
	}
	sub_suggestLock = true;
	$.ajax ({
		type:		'post',
		datatype:	'json',
		url:		'/define/suggest',
		data:		{'query': searchQuery, 'limit': 5, 'type': '0'},
		success:function(data){
			if (data.length > 0) {
				$('#suggestForm .suggest_wrap ul').children().remove();
				var suggestHtml = '';
				$.each(data, function(index, item){
					suggestHtml += '<li data-index=\"' + index + '\" data-keyword=\"' + item + '\" onClick="javascript:powebookSuggestClick(this);">' + item + '</li>';
				});
				$('#suggestForm .suggest_wrap ul').html(suggestHtml);
				$('#suggestForm .suggest_wrap').css('display', 'block');
			}
			sub_suggestLock = false;
		}, error: function(request, status, error){
			sub_suggestLock = false;
		}
	});
}

function sub_suggestDirectionKey(obj, updown) {
	var searchSuggestForm = $(obj).closest('#suggestForm');
	var suggestList = searchSuggestForm.find('.suggest_wrap > .suggest_list > div > ul > li');
	if (suggestList.length > 0) {
		var focusSuggest = searchSuggestForm.find('.suggest_wrap > .suggest_list > div > ul > .focus');
		if (focusSuggest.length > 0) {
			var curIndex = Number(focusSuggest.attr('data-index'));
			searchSuggestForm.find('.suggest_wrap ul li').removeClass('focus');
			searchSuggestForm.find('.suggest_wrap ul li[data-index=' + (updown ? curIndex + 1 : curIndex - 1) + ']').addClass('focus');
		} else {
			searchSuggestForm.find('.suggest_wrap ul li[data-index=' + (updown ? 0 : suggestList.length - 1) + ']').addClass('focus');
		}
		$(obj).val($('.suggest_wrap > .suggest_list > div > ul > .focus').attr('data-keyword'));
	}
}

function powebookSuggestClick(obj) {
	suggestForm.query.value = $(obj).attr('data-keyword');
	suggestForm.submit();
}

function powerbookSearchCheck(){
	if (!suggestForm.query.value) {
		return false;
	}
	return true;
}

function powerbookDelete(id){
	//if (confirm("정말로 삭제하시겠습니까?")) {
	if (confirm("Are you sure you want to delete it?")) {

		location.href='/powerbook/delete?id=' + id;
	}
}